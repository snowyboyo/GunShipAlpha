package GSA;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import java.util.Iterator;
import java.util.Objects;
import java.util.Random;
import javax.swing.Timer;

public class Game extends Canvas implements Mediator, KeyListener {
    static GameState gs = new GameState();
    static MouseState ms = new MouseState();
    protected Player player = new Player(850, 450);
    protected static boolean gameOver = false;
    private int waveNumber = 0;
    protected ArrayList<ExplosionParticle> explosionParticles = new ArrayList<>();
    private ArrayList<Integer> keysPressed = new ArrayList<>();
    private static int score = 0;
    private final int screenWidth = 1800;
    private final int screenHeight = 1000;
    private final int moveSpeed = 3;
    private final int playerSize = 50;
    private BufferedImage splashImage1;
    private BufferedImage splashImage2;
    private boolean showSplash1 = true;
    private boolean showSplash2 = false;
    private Point cursorPosition = new Point();
    private static Timer shakeTimer;
    private Timer splashTimer;
    private static final int shakeDuration = 500;
    private static final int shakeIntensity = 5;


    @Override
    public void handleExplosion(Point location, int explosionSize) {
        gs.handleExplosion(location, explosionSize);
    }

    @Override
    public void updateTankTargets(Point newTarget) {
        gs.updateTankTargets(newTarget);
    }

    public Game() {
        try {
            splashImage1 = ImageIO.read(Objects.requireNonNull(getClass().getResource("/images/SplashSscreen.png")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            splashImage2 = ImageIO.read(Objects.requireNonNull(getClass().getResource("/images/SplashScreen.png")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        startGameLoop();
        new Timer(5000, e -> showSplash1 = false).start();
        initializeSplashTimer();
        setupMouseListeners();
        this.addKeyListener(this);
        this.setFocusable(true);
        this.requestFocus();

    }
    private void initializeSplashTimer() {
        splashTimer = new Timer(5000, e -> {
            showSplash2 = false;
            splashTimer.stop(); // Optional: stop the timer after execution
        });
    }

    protected void startSplashScreenTimer() {
        showSplash2 = true;
        if (splashTimer.isRunning()) {
            splashTimer.stop();
        }

        splashTimer.start();
    }
    public void showSplashScreen(Graphics g){
        if(showSplash1) {
            g.drawImage(splashImage1, 1, 1, null);
        }
    }
    public void showSplashScreen2(Graphics g){
        if(showSplash2) {
            g.drawImage(splashImage2, 1, 100, null);
        }
    }

    private void startGameLoop() {

        Timer gameLoop = new Timer(16, e -> {
            startGameOverCountdown();
            if(!showSplash1&&!showSplash2)update();
            repaint();
        });
        gameLoop.start();
    }

    private void update() {
        moveEnemies();
        updateTankTargets(player.location);
        gs.handleProjectileCollison(player,this);
        gs.checkBehemothPlayerCollisions(player,this);
        updateTargets();
        gs.removeDeadEnemies();
        if (!player.isDead() && !gameOver) {
            takeKeyboardInput();
        }
        if (gs.areAllEnemiesDefeated() && !gameOver) {
            int numberOfEnemies = 3 * waveNumber;
            spawnEnemies(numberOfEnemies);
            if (waveNumber > 1) {
                score += 1000;
            }
            waveNumber++;
        }
    }

    private void startGameOverCountdown() {
        if (player.isDead() && !gameOver) {
            Timer delayTimer = new Timer(1000, delayEvent -> {
                gameOver = true;
            });
            delayTimer.setRepeats(false);
            delayTimer.start();
        }
    }
    public void playerHit() {
        if (shakeTimer != null && shakeTimer.isRunning()) {
            return;
        }
        long startTime = System.currentTimeMillis();
        shakeTimer = new Timer(50, e -> {
            if (System.currentTimeMillis() - startTime > shakeDuration) {
                shakeTimer.stop();
                this.setLocation(0, 0);
                return;
            }
            Random rand = new Random();
            int x = rand.nextInt(shakeIntensity * 2) - shakeIntensity;
            int y = rand.nextInt(shakeIntensity * 2) - shakeIntensity;
            this.setLocation(x, y);
        });
        shakeTimer.start();
    }

    private void updateTargets() {
        gs.forEachEnemy(enemy -> {
                enemy.setTarget(player.location);
        });
    }
    void addPlayerProjectile(Point2D direction){
        int halfPlayerSize = 25;
        Projectile projectile = new Projectile(player.location.x + halfPlayerSize, player.location.y + halfPlayerSize, direction,true);
        gs.addProjectile(projectile);
    }

    private void takeKeyboardInput() {
        movePlayerUp();
        movePlayerDown();
        movePlayerLeft();
        movePlayerRight();
    }

    private void movePlayerRight() {
        if (keysPressed.contains(KeyEvent.VK_D)) {
           if(player.location.x <= screenWidth - (playerSize + moveSpeed)) player.location.x += moveSpeed;
        }
    }

    private void movePlayerLeft() {
        if (keysPressed.contains(KeyEvent.VK_A)) {
            if (player.location.x >= moveSpeed ) player.location.x -= moveSpeed;
        }
    }

    private void movePlayerDown() {
        if (keysPressed.contains(KeyEvent.VK_S)) {
            if (player.location.y <= screenHeight - (playerSize + moveSpeed)) player.location.y += moveSpeed;
        }
    }

    private void movePlayerUp() {
        if (keysPressed.contains(KeyEvent.VK_W)) {
            if (player.location.y >= moveSpeed) player.location.y -= moveSpeed;
        }
    }
    protected void restartGame() {
            if (gameOver) {
                explosionParticles.clear();
                waveNumber = 0;
                score = 0;
                gameOver = false;
                gs.enemies.clear();
                gs.projectiles.clear();
                gs.lines.clear();
                player.resetHealth();
        }
    }

    public void updateEnemies() {
        updateParticles();
        gs.forEachEnemy(enemy -> {
            removeParticles(enemy);
            enemy.update(gs.explosion, gs.lines, gs.projectiles, player);
            addParticles(enemy);
        });
    }
    private void updateParticles() {
        for (Iterator<ExplosionParticle> iterator = explosionParticles.iterator(); iterator.hasNext(); ) {
            System.out.println(explosionParticles.size());
            ExplosionParticle particle = iterator.next();
            particle.update();
        }
    }
    private void removeParticles(Enemy enemy) {
        for (Iterator<ExplosionParticle> iterator = explosionParticles.iterator(); iterator.hasNext(); ) {
            ExplosionParticle particle = iterator.next();
            boolean shouldRemove = false;
            shouldRemove = particleCollidedWithPlayer(particle, shouldRemove);
            shouldRemove = particleCollidedWithLine(particle, shouldRemove);
            shouldRemove = particleCollidedWithEnemy(enemy,shouldRemove);
            if (shouldRemove) {
                iterator.remove();
            }
        }
    }

    protected void addParticles(Enemy enemy) {
        java.util.List<ExplosionParticle> particles = enemy.returnLastGeneratedParticles();
        if(particles != null) {
            explosionParticles.addAll(particles);
        }
    }


    public boolean particleCollidedWithPlayer(ExplosionParticle particle, boolean shouldRemove) {
        if (particle.isCollidingWithPlayer(player)) {
            player.reduceHealth();
            playerHit();
            shouldRemove = true;
        }
        return shouldRemove;
    }

    static boolean particleCollidedWithLine(ExplosionParticle particle, boolean shouldRemove) {
        for (Line line : gs.lines) {
            if (particle.isCollidingWithLine(line)) {
                shouldRemove = true;
                break;
            }
        }
        return shouldRemove;
    }
    protected boolean particleCollidedWithEnemy(Enemy enemy, boolean shouldRemove) {
             for (ExplosionParticle explosionParticle : explosionParticles) {
                 if (enemy.isCollidingWithParticle(explosionParticle)) {
                     enemy.removeHealth();
                     shouldRemove = true;
                     break;
                 }
             }
        return shouldRemove;
    }
    private void updateCursorPosition(MouseEvent e) {
        cursorPosition.setLocation(e.getPoint());
        player.updateAngle(cursorPosition);
    }

    private void setupMouseListeners() {
        setupMousePressListener();
        setupMouseReleaseListener();
        setupMouseDragListener();
        setupMouseMovedListener();
    }

    private void setupMousePressListener() {
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                handleMousePress(e);
            }
        });
    }
    private void setupMouseMovedListener(){
        this.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                updateCursorPosition(e);
            }
        });
    }

    private void handleMousePress(MouseEvent e) {
        if (!SwingUtilities.isRightMouseButton(e)) {
            ms.handleMousePress(e);
        }
    }

    private void setupMouseReleaseListener() {
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                handleMouseRelease(e);
            }
        });
    }

    private void handleMouseRelease(MouseEvent e) {
        if (SwingUtilities.isRightMouseButton(e)) {
            ms.handleMouseRelease(e, this, true);
        } else {
            ms.handleMouseRelease(e, this, false);
        }
    }

    private void setupMouseDragListener() {
        this.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                handleMouseDrag(e);
            }
        });
    }

    private void handleMouseDrag(MouseEvent e) {
        if (!SwingUtilities.isRightMouseButton(e)) {
            ms.handleMouseDrag(e);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (!keysPressed.contains(e.getKeyCode())) {
            keysPressed.add(e.getKeyCode());
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        keysPressed.remove((Integer) e.getKeyCode());
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    private void moveEnemies() {
        updateEnemies();
        gs.removeExpiredProjectilesAndLines(getWidth(), getHeight());

    }

    void spawnEnemies(int count) {
        for (int i = 0; i < count; i++) {
            if (!tryToSpawnEnemy()) {
                System.out.println("Failed to spawn enemy #" + (i + 1));
            }
        }
    }

    private boolean tryToSpawnEnemy() {
        Random rand = new Random();
        int enemyType = rand.nextInt(5);
        int maxAttempts = 10;
        int attempts = 0;

        while (attempts < maxAttempts) {
            Game.spawnEnemiesRandomLocation result = getRandomLocation(rand);

            if (canSpawnEnemyAt(result.x(), result.y())) {
                spawnEnemyOfTypeAtLocation(enemyType, result.x(), result.y());
                return true;
            } else {
                attempts++;
            }
        }

        return false;
    }

    private Game.spawnEnemiesRandomLocation getRandomLocation(Random rand) {
        int side = rand.nextInt(4);
        int x = 0, y = 0;
        return getSpawnEnemiesRandomLocation(side, y, x, rand);
    }

    private void spawnEnemyOfTypeAtLocation(int enemyType, int x, int y) {
        if (enemyType == 1) {
            spawnMineAt(x, y);
        } else if (enemyType == 2) {
            spawnEnemyAt(x, y);
        } else if (enemyType == 3) {
            spawnGinkerAt(x, y);
        } else {
            spawnBehemothAt(x, y);
        }
    }

    private boolean canSpawnEnemyAt(int x, int y) {
        int width = 50;
        int height =50;
        Rectangle newEnemyBounds = new Rectangle(x, y, width, height);
        return !gs.isSpaceOccupied(newEnemyBounds);
    }

    private void spawnMineAt(int x, int y) {
        Mine mine = new Mine(x, y, getWidth() / 2, getHeight() / 2, this);
        gs.addEnemy(mine);
    }

    private void spawnGinkerAt(int x, int y) {
        Ginker ginker = new Ginker(x, y, getWidth() / 2, getHeight() / 2);
        gs.addEnemy(ginker);
    }

    private void spawnBehemothAt(int x, int y) {
        Behemoth behemoth = new Behemoth(x, y, getWidth() / 2, getHeight() / 2);
        gs.addEnemy(behemoth);
    }

    private void spawnEnemyAt(int x, int y) {
        Enemy enemy = new Enemy(x, y, getWidth() / 2, getHeight() / 2);
        gs.addEnemy(enemy);
    }

    private spawnEnemiesRandomLocation getSpawnEnemiesRandomLocation(int side, int y, int x, Random rand) {
        if (getWidth() > 0 && getHeight() > 0) {
            switch (side) {
                case 0:
                    y = 0;
                    x = rand.nextInt(getWidth());
                    break;
                case 1:
                    x = getWidth();
                    y = rand.nextInt(getHeight());
                    break;
                case 2:
                    y = getHeight();
                    x = rand.nextInt(getWidth());
                    break;
                case 3:
                    x = 0;
                    y = rand.nextInt(getHeight());
                    break;
            }
        }
            spawnEnemiesRandomLocation result = new spawnEnemiesRandomLocation(x, y);
            return result;
    }



    private record spawnEnemiesRandomLocation(int x, int y) {
    }

    @Override
    public void paint(Graphics g) {
        showSplashScreen(g);
        showSplashScreen2(g);
        if (!showSplash1&&!showSplash2) {
            if (gameOver) {
                drawGameOverScreen(g);
            } else {
                player.draw(g);
                render(g);
            }
            drawExplosions(g);
            drawScoreAndWaveNumber(g);
            player.displayHealth(g);
        }
    }

    private void drawExplosions(Graphics g) {
        for (ExplosionParticle explosion : explosionParticles) {
            explosion.draw(g);
        }
    }

    private void drawScoreAndWaveNumber(Graphics g) {
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 24));
        g.drawString("Score: " + score, 10, 24);
        g.drawString("Wave Number: " + waveNumber, 1600, 24);
    }

    private void drawGameOverScreen(Graphics g) {
        g.setColor(Color.RED);
        g.setFont(new Font("Arial", Font.BOLD, 50));
        String gameOverText = "GAME OVER";
        int stringWidth = g.getFontMetrics().stringWidth(gameOverText);
        int stringHeight = g.getFontMetrics().getAscent();
        g.drawString(gameOverText, (getWidth() - stringWidth) / 2, (getHeight() + stringHeight) / 2);
    }

    private void render(Graphics g) {
        gs.drawBehemoths(g);
        drawLines(g);
        initLineDrawing(g);
        gs.updateProjectile();
        gs.drawProjectile(g);
    }


    private void drawLines(Graphics g) {
        gs.drawLines(g);
    }

    private void initLineDrawing(Graphics g) {
        ms.drawLineIfNeeded(g, this);
    }

    static void addPlayerScore() {
        score += 350;
    }


}