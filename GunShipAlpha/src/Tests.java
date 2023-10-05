import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.awt.*;
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
        boolean isBlocked = behemoth.isIntersectingLine(line);`
        assertTrue(isBlocked);
    }
}
