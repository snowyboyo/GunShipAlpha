import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.Timer;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Game extends Canvas implements Mediator {
    static GameState gs = new GameState();
    static MouseState ms = new MouseState();
    private Player player = new Player();
private boolean gameOver = false;
    private int waveNumber = 1;
    private int explosionCounter = 0;
    private ArrayList<ExplosionParticle> ongoingExplosions = new ArrayList<>();
    @Override
    public void handleExplosion(Point location, int explosionSize) {
            gs.handleExplosion(location, explosionSize, player);
    }

    public Game() {
        startGameLoop();
        setupMouseListeners();
    }
    private void startGameLoop() {
        Timer gameLoop = new Timer(16, e -> {
            if (player.isDead()) {
                gameOver = true;
            } else {
                moveEnemies();
                gs.handleProjectileCollison(player);
                gs.checkBehemothPlayerCollisions(player);
                updateExplosions();
                gs.removeDeadEnemies();
                if (gs.areAllEnemiesDefeated() && !gameOver) {
                    int numberOfEnemies = 3 * waveNumber;
                    spawnEnemies(numberOfEnemies);
                    waveNumber++;
                }
            }
            repaint();
        });
        gameLoop.start();
    }
    private void updateExplosions() {
        gs.forEachEnemy(enemy -> {
            if (enemy instanceof Tank && enemy.isDead()) {
                ongoingExplosions.addAll(((Tank) enemy).explode());
            }
        });
        ongoingExplosions.forEach(ExplosionParticle::update);
    }


    private void setupMouseListeners() {
        setupMousePressListener();
        setupMouseReleaseListener();
        setupMouseDragListener();
    }

    private void setupMousePressListener() {
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                handleMousePress(e);
            }
        });
    }

    private void handleMousePress(MouseEvent e) {
        ms.handleMousePress(e);
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
        ms.handleMouseDrag(e, this);
    }

    private void moveEnemies() {
        gs.update(player);
        gs.removeExpiredProjectilesAndLines(getWidth(), getHeight());

    }

    public void spawnNextWave() {
        waveNumber++;
        int enemiesToSpawn = waveNumber * 5;  // Adjust this formula as needed
        spawnEnemies(enemiesToSpawn);
    }

    void spawnEnemies(int count) {
        for (int i = 0; i < count; i++) {
            if (!tryToSpawnEnemy()) {
                System.out.println("Failed to spawn enemy #" + (i+1));
            }
        }
    }

    private boolean tryToSpawnEnemy() {
        Random rand = new Random();
        boolean isTank = rand.nextBoolean();
        System.out.println(isTank);
        int maxAttempts = 10;
        int attempts = 0;

        while (attempts < maxAttempts) {
            Game.spawnEnemiesRandomLocation result = getRandomLocation(rand);

            if (canSpawnEnemyAt(result.x(), result.y())) {
                spawnEnemyOfTypeAtLocation(isTank, result.x(), result.y());
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

    private void spawnEnemyOfTypeAtLocation(boolean isTank, int x, int y) {
        if (isTank) {
            spawnTankAt(x, y);
        } else {
            spawnEnemyAt(x, y);
        }
    }

    private boolean canSpawnEnemyAt(int x, int y) {
        int width = 100;
        int height = 100;
        Rectangle newEnemyBounds = new Rectangle(x, y, width, height);
        return !gs.isSpaceOccupied(newEnemyBounds);
    }

    private void spawnTankAt(int x, int y) {
        Tank tank = new Tank(x, y, getWidth() / 2, getHeight() / 2,this);
        gs.addEnemy(tank);
    }

    private void spawnEnemyAt(int x, int y) {
        Enemy enemy = new Enemy(x, y, getWidth() / 2, getHeight() / 2);
        gs.addEnemy(enemy);
    }

    private spawnEnemiesRandomLocation getSpawnEnemiesRandomLocation(int side, int y, int x, Random rand) {
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
        spawnEnemiesRandomLocation result = new spawnEnemiesRandomLocation(x, y);
        return result;
    }

    private record spawnEnemiesRandomLocation(int x, int y) {
    }

    @Override
    public void paint(Graphics g) {

        if (gameOver) {
            drawGameOverScreen(g);
        } else {
            player.draw(g, getWidth(), getHeight());
            render(g);
        }
        for (ExplosionParticle explosion : ongoingExplosions) {
            explosion.draw(g);
        }
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
        drawSegmentedLine(g);
        gs.updateProjectile();
        gs.drawProjectile(g);
    }


    private void drawLines(Graphics g) {
        gs.drawLines(g);
    }

    private void initLineDrawing(Graphics g) {
        ms.drawLineIfNeeded(g, this);
    }

    private void drawSegmentedLine(Graphics g) {
        ms.drawLines(g);
    }


}