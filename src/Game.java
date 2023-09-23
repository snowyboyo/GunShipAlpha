import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import javax.swing.Timer;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;

public class Game extends Canvas {
    private BufferedImage sprite;
    static GameState gs = new GameState();
    static MouseState ms = new MouseState();


    public Game() {
        loadSprites();
        startGameLoop();
        setupMouseListeners();
    }

    private void loadSprites() {
        try {
            sprite = ImageIO.read(new File("src/Images/GunShip.png"));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startGameLoop() {
        Timer gameLoop = new Timer(16, e -> {
            moveEnemies();
            repaint();
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
        gs.handleEnemyMovement();
        gs.handleProjectileCollision();
        gs.removeExpiredProjectilesAndLines(getWidth(),getHeight());
    }
    public void spawnBehemoths(int count) {
        Random rand = new Random();
        for (int i = 0; i < count; i++) {
            int side = rand.nextInt(4);
            int x = 0, y = 0;
            Game.spawnEnemiesRandomLocation result = getSpawnEnemiesRandomLocation(side, y, x, rand);
            Game.Enemy enemy = new Game.Enemy(result.x(), result.y(), getWidth() / 2, getHeight() / 2);
            gs.addEnemy(enemy);
        }
    }




    private spawnEnemiesRandomLocation getSpawnEnemiesRandomLocation(int side, int y, int x, Random rand) {
        switch (side) {
            case 0: y = 0; x = rand.nextInt(getWidth()); break;
            case 1: x = getWidth(); y = rand.nextInt(getHeight()); break;
            case 2: y = getHeight(); x = rand.nextInt(getWidth()); break;
            case 3: x = 0; y = rand.nextInt(getHeight()); break;
        }
        spawnEnemiesRandomLocation result = new spawnEnemiesRandomLocation(x, y);
        return result;
    }

    private record spawnEnemiesRandomLocation(int x, int y) {
    }

    @Override
    public void paint(Graphics g) {
        int canvasWidth = getWidth();
        int canvasHeight = getHeight();
        int spriteX = (canvasWidth - sprite.getWidth()) / 2;
        int spriteY = (canvasHeight - sprite.getHeight()) / 2;
        g.drawImage(sprite, spriteX, spriteY, null);
        render(g);
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





    class Enemy {
        int x, y;
        final int targetX, targetY;
        int health = 2;
        public Enemy(int x, int y, int targetX, int targetY) {
            this.x = x;
            this.y = y;
            this.targetX = targetX;
            this.targetY = targetY;
        }

        public void moveTowardTarget() {
            if (x < targetX) x++;
            if (x > targetX) x--;
            if (y < targetY) y++;
            if (y > targetY) y--;
        }
    }

    static class lineLogic {
        Point start, end;
        long creationTime;

        public lineLogic(Point start, Point end) {
            this.start = start;
            this.end = end;
            this.creationTime = System.currentTimeMillis();
        }

        boolean intersects(Enemy enemy) {
            gs.createEnemyHitbox(enemy);
           Rectangle enemyBounds = gs.createEnemyHitbox(enemy);
            return enemyBounds.intersectsLine(start.x, start.y, end.x, end.y);
        }
    }
    class Projectile {
        double x, y;
        double dx, dy;
        static final double SPEED = 5.0;

        public Projectile(double x, double y, Point2D direction) {
            this.x = x;
            this.y = y;
            double magnitude = Math.sqrt(direction.getX() * direction.getX() + direction.getY() * direction.getY());
            this.dx = SPEED * direction.getX() / magnitude;
            this.dy = SPEED * direction.getY() / magnitude;
        }

        public void update() {
            x += dx;
            y += dy;
        }

        public void draw(Graphics g) {
            g.setColor(Color.RED);
            g.fillOval((int)x-5, (int)y-5, 10, 10);
        }
    }
}