import javax.sound.sampled.Line;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.Timer;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;

public class GameCanvas extends Canvas {
    private static final double THRESHOLD_ANGLE = Math.toRadians(15);
    private BufferedImage sprite;
    private BufferedImage enemySprite;
    private Timer gameLoop;
    private ArrayList<Enemy> enemies = new ArrayList<>();
    private Point startPoint = null;
    private ArrayList<Line> lines = new ArrayList<>();
    private Point2D originalDirection = null;
    private static final long LINE_LIFETIME = 5000;

    public GameCanvas() {
        try {
            sprite = ImageIO.read(new File("src/Images/GunShip.png"));
            enemySprite = ImageIO.read(new File("src/Images/Behemoth.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        gameLoop = new Timer(16, e -> {
            moveEnemies();
            repaint();
        });
        gameLoop.start();
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                startPoint = e.getPoint();
                originalDirection = null;
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (startPoint != null) {
                    lines.add(new Line(startPoint, e.getPoint()));
                    startPoint = null;
                    originalDirection = null;
                    repaint();
                }
            }
        });
        this.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                repaint();
            }
        });
    }

    private void moveEnemies() {
        for (Enemy enemy : enemies) {
            boolean blocked = false;
            for (Line line : lines) {
                if (line.intersects(enemy)) {
                    blocked = true;
                    break;
                }
            }
            if (!blocked) {
                enemy.moveTowardTarget();
            }
        }

        lines.removeIf(Line::isExpired);
    }

    @Override
    public void paint(Graphics g) {
        g.setColor(Color.LIGHT_GRAY);
        int circleDiameter = 500;
        int canvasWidth = getWidth();
        int canvasHeight = getHeight();
        int circleX = (canvasWidth - circleDiameter) / 2;
        int circleY = (canvasHeight - circleDiameter) / 2;
        g.drawOval(circleX, circleY, circleDiameter, circleDiameter);

        int spriteX = (800 - sprite.getWidth()) / 2;
        int spriteY = (800 - sprite.getHeight()) / 2;
        g.drawImage(sprite, spriteX, spriteY, null);

        for (Enemy enemy : enemies) {
            g.drawImage(enemySprite, enemy.x, enemy.y, null);
        }

        for (Line line : lines) {
            g.drawLine(line.start.x, line.start.y, line.end.x, line.end.y);
        }
        if (startPoint != null) {
            Point currentPoint = MouseInfo.getPointerInfo().getLocation();
            SwingUtilities.convertPointFromScreen(currentPoint, this);
            if (originalDirection == null) {
                originalDirection = new Point2D.Double(startPoint.x - currentPoint.x, startPoint.y - currentPoint.y);
            }

            Point2D currentDirection = new Point2D.Double(startPoint.x - currentPoint.x, startPoint.y - currentPoint.y);
            double angle = angleBetween2Lines(originalDirection, currentDirection);

            if (angle < THRESHOLD_ANGLE) {
                g.drawLine(startPoint.x, startPoint.y, currentPoint.x, currentPoint.y);
            }
        }
    }

    private double angleBetween2Lines(Point2D line1, Point2D line2) {
        double angle1 = Math.atan2(line1.getY(), line1.getX());
        double angle2 = Math.atan2(line2.getY(), line2.getX());
        double result = angle1 - angle2;
        if (result < 0) {
            result += 2 * Math.PI;
        }
        return Math.abs(result);
    }

    class Enemy {
        int x, y;
        final int targetX, targetY;

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

    class Line {
        Point start, end;
        long creationTime;

        public Line(Point start, Point end) {
            this.start = start;
            this.end = end;
            this.creationTime = System.currentTimeMillis();
        }

        boolean isExpired() {
            return System.currentTimeMillis() - creationTime > LINE_LIFETIME;
        }

        boolean intersects(Enemy enemy) {
            Rectangle enemyBounds = new Rectangle(enemy.x, enemy.y, enemySprite.getWidth(), enemySprite.getHeight());
            return enemyBounds.intersectsLine(start.x, start.y, end.x, end.y);
        }
    }
}