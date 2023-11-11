import javax.swing.*;
import java.awt.*;
import java.util.Random;
import javax.swing.Timer;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Game extends Canvas {
    static GameState gs = new GameState();
    static MouseState ms = new MouseState();
    private Player player = new Player();
private boolean gameOver = false;

    public Game() {
        startGameLoop();
        setupMouseListeners();
    }
    private void startGameLoop() {
        Timer gameLoop = new Timer(16, e -> {
            if (player.isDead()) {
                gameOver = true;
                repaint();
            } else {
            moveEnemies();
            boolean playerHit = gs.handleProjectileCollisionAndMoveEnemies();
            if (gs.checkBehemothPlayerCollisions(player, getWidth(), getHeight()) || playerHit||gs.checkTankPlayerCollisions(player, getWidth(), getHeight())) {
                player.reduceHealth();
            }

            repaint();
            }
        });
        gameLoop.start();
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
        gs.handleBehemothMovement();
        gs.handleTankMovement();
        gs.removeExpiredProjectilesAndLines(getWidth(), getHeight());
    }

    public void spawnBehemoths(int count) {
        Random rand = new Random();
        for (int i = 0; i < count; i++) {
            int side = rand.nextInt(4);
            int x = 0, y = 0;
            Game.spawnEnemiesRandomLocation result = getSpawnEnemiesRandomLocation(side, y, x, rand);
            Behemoth behemoth = new Behemoth(result.x(), result.y(), getWidth() / 2, getHeight() / 2);
            gs.addBehemoth(behemoth);
        }
    }
    public void spawnTanks(int count) {
        Random rand = new Random();
        for (int i = 0; i < count; i++) {
            int side = rand.nextInt(4);
            int x = 0, y = 0;
            Game.spawnEnemiesRandomLocation result = getSpawnEnemiesRandomLocation(side, y, x, rand);
            Tank tank = new Tank(result.x(), result.y(), getWidth() / 2, getHeight() / 2); // Changed to Tank
            gs.addTank(tank);
        }
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
            drawGameOverScreen(g);
            player.draw(g, getWidth(), getHeight());
            render(g);
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