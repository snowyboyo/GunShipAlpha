import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;

class GameStateTest {

    @Test
    void testBehemothPlayerCollision() {
        GameState gameState = new GameState();
        Player player = new Player();
        Behemoth behemoth = new Behemoth(350, 350, 50, 50);
        gameState.addBehemoth(behemoth);
        behemoth.moveTowardTarget();
        boolean isCollision = gameState.checkBehemothPlayerCollisions(player, 800, 600);
        assertTrue(isCollision);
    }
    @Test
    void testBehemothProjectileCollision() {
        GameState gameState = new GameState();
        Behemoth behemoth = new Behemoth(350, 350, 50, 50);
        Point2D direction = new Point2D.Double(350, 350);
        Projectile projectile = new Projectile(350,350, direction);
        gameState.addBehemoth(behemoth);
        boolean isCollision = behemoth.isCollidingWithProjectile(projectile);
        assertTrue(isCollision);
    }
    @Test
    void testBehemothLineInteraction() {
        GameState gameState = new GameState();
        Behemoth behemoth = new Behemoth(350, 350, 50, 50);
        Point start = new Point(350,350);
        Point end = new Point(450,450);
        Line line = new Line(start,end);
        gameState.addBehemoth(behemoth);
        boolean isBlocked = behemoth.isIntersectingLine(line);
        assertTrue(isBlocked);
    }
    @Test
    void testBehemothMovement() {
        GameState gameState = new GameState();
        Behemoth behemoth = new Behemoth(350, 350, 50, 50);
        gameState.addBehemoth(behemoth);
        double initialDistance = Math.sqrt(Math.pow(350 - 50, 2) + Math.pow(350 - 50, 2));
        behemoth.moveTowardTarget();
        Rectangle bounds = behemoth.getBounds();
        double newDistance = Math.sqrt(Math.pow(bounds.x - 50, 2) + Math.pow(bounds.y - 50, 2));
        assertTrue(newDistance < initialDistance);
    }
    @Test
    void testRemoveEnemy() {
        GameState gameState = new GameState();
        Behemoth behemoth = new Behemoth(50, 50, 400, 400);
        behemoth.removeHealth();
        behemoth.removeHealth();
        gameState.addBehemoth(behemoth);
        gameState.removeEnemy();
        assertEquals(0, gameState.getBehemothCount());
    }
    @Test
    void testAddLine() {
        GameState gameState = new GameState();
        Point start = new Point(350,350);
        Point end = new Point(450,450);
        Component source = new Button();
        MouseEvent e = new MouseEvent(source, MouseEvent.MOUSE_PRESSED, System.currentTimeMillis(), 0, end.x, end.y, 1, false);
        gameState.addLine(e, start);
        assertEquals(1, gameState.getLineListSize());
    }
    @Test
    void testRemovePlayerHealth() {
        Player player = new Player();
        for(int i = 0; i < 10; i++) {
            player.reduceHealth();
        }
        assertTrue(player.isDead());
    }
    @Test
    void testPlayerProjectileCollision() {
        Point2D direction = new Point2D.Double(350, 350);
        Projectile projectile = new Projectile(350,350, direction);
        boolean isCollision = projectile.isCollidingWithPlayer();
        assertTrue(isCollision);
    }
}
