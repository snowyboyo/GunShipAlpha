import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;

class GameStateTest {

    @Test
    void testEnemyPlayerCollisionsOverlapping() {
        GameState gameState = new GameState();
        Player player = new Player();
        Enemy enemy = new Enemy(350, 350, 50, 50);
        gameState.addEnemy(enemy);
        boolean isCollision = gameState.checkBehemothPlayerCollisions(player);
        assertTrue(isCollision);
    }
    @Test
    void testEnemyPlayerCollisionsToTheLeft() {
        GameState gameState = new GameState();
        Player player = new Player();
        Enemy enemy = new Enemy(450, 350, 350, 350);
        //placing the enemy to the left of the player
        gameState.addEnemy(enemy);
        enemy.moveTowardTarget();
        boolean isCollision = gameState.checkBehemothPlayerCollisions(player);
        assertTrue(isCollision);
    }
    @Test
    void testEnemyPlayerCollisionsToTheRight() {
        GameState gameState = new GameState();
        Player player = new Player();
        Enemy enemy = new Enemy(250, 350, 350, 350);
        //placing the enemy to the right of the player
        gameState.addEnemy(enemy);
        enemy.moveTowardTarget();
        boolean isCollision = gameState.checkBehemothPlayerCollisions(player);
        assertTrue(isCollision);
    }
    @Test
    void testEnemyPlayerCollisionsFromTheTop() {
        GameState gameState = new GameState();
        Player player = new Player();
        Enemy enemy = new Enemy(350, 250, 350, 350);
        //placing the enemy above the player
        gameState.addEnemy(enemy);
        enemy.moveTowardTarget();
        boolean isCollision = gameState.checkBehemothPlayerCollisions(player);
        assertTrue(isCollision);
    }
    @Test
    void testEnemyPlayerCollisionsFromTheBottom() {
        GameState gameState = new GameState();
        Player player = new Player();
        Enemy enemy = new Enemy(350, 450, 350, 350);
        //placing the enemy below the player
        gameState.addEnemy(enemy);
        enemy.moveTowardTarget();
        boolean isCollision = gameState.checkBehemothPlayerCollisions(player);
        assertTrue(isCollision);
    }
    @Test
    void testEnemyPlayerNotColliding() {
        GameState gameState = new GameState();
        Player player = new Player();
        Enemy enemy = new Enemy(350, 350, 50, 50);
        //350,350 = one pixel away from the player
        gameState.addEnemy(enemy);
        boolean isCollision = gameState.checkBehemothPlayerCollisions(player);
        assertFalse(isCollision);
    }
    @Test
    void testExplosionPlayerCollisionToTheRight() {
        Player player = new Player();
        Point center = new Point(525,400);
        int radius= 200;
        boolean isCollision = player.isWithinRange(center,radius);
        assertTrue(isCollision);
    }
    @Test
    void testExplosionPlayerCollisionToTheLeft() {
        Player player = new Player();
        Point center = new Point(175,400);
        int radius= 200;
        boolean isCollision = player.isWithinRange(center,radius);
        assertTrue(isCollision);
    }
    @Test
    void testExplosionPlayerCollisionAbove() {
        Player player = new Player();
        Point center = new Point(400,175);
        int radius= 200;
        boolean isCollision = player.isWithinRange(center,radius);
        assertTrue(isCollision);
    }
    @Test
    void testExplosionPlayerCollisionBelow() {
        Player player = new Player();
        Point center = new Point(400,525);
        int radius= 200;
        boolean isCollision = player.isWithinRange(center,radius);
        assertTrue(isCollision);
    }
    @Test
    void testEnemyProjectileCollisionToTheRight() {
        GameState gameState = new GameState();
        Enemy behemoth = new Enemy(350, 350, 50, 50);
        Point2D direction = new Point2D.Double(450, 400);
        Projectile projectile = new Projectile(445,400, direction);
        gameState.addEnemy(behemoth);
        boolean isCollision = behemoth.isCollidingWithProjectile(projectile);
        assertTrue(isCollision);
    }
    @Test
    void testEnemyProjectileCollisionToTheLeft() {
        GameState gameState = new GameState();
        Enemy behemoth = new Enemy(350, 350, 50, 50);
        Point2D direction = new Point2D.Double(350, 400);
        Projectile projectile = new Projectile(350,400, direction);
        gameState.addEnemy(behemoth);
        boolean isCollision = behemoth.isCollidingWithProjectile(projectile);
        assertTrue(isCollision);
    }
    @Test
    void testEnemyProjectileCollisionAbove() {
        GameState gameState = new GameState();
        Enemy behemoth = new Enemy(350, 350, 50, 50);
        Point2D direction = new Point2D.Double(400, 350);
        Projectile projectile = new Projectile(400,350, direction);
        gameState.addEnemy(behemoth);
        boolean isCollision = behemoth.isCollidingWithProjectile(projectile);
        assertTrue(isCollision);
    }
    @Test
    void testEnemyProjectileCollisionBelow() {
        GameState gameState = new GameState();
        Enemy behemoth = new Enemy(350, 350, 50, 50);
        Point2D direction = new Point2D.Double(400, 449);
        Projectile projectile = new Projectile(400,449, direction);
        gameState.addEnemy(behemoth);
        boolean isCollision = behemoth.isCollidingWithProjectile(projectile);
        assertTrue(isCollision);
    }
    @Test
    void testEnemyProjectileCollisionOverlapping() {
        GameState gameState = new GameState();
        Enemy behemoth = new Enemy(350, 350, 50, 50);
        Point2D direction = new Point2D.Double(400, 400);
        Projectile projectile = new Projectile(400,400, direction);
        gameState.addEnemy(behemoth);
        boolean isCollision = behemoth.isCollidingWithProjectile(projectile);
        assertTrue(isCollision);
    }
    @Test
    void testEnemyProjectileNotColliding() {
        GameState gameState = new GameState();
        Enemy behemoth = new Enemy(350, 350, 50, 50);
        Point2D direction = new Point2D.Double(500, 500);
        Projectile projectile = new Projectile(500,500, direction);
        gameState.addEnemy(behemoth);
        boolean isCollision = behemoth.isCollidingWithProjectile(projectile);
        assertFalse(isCollision);
    }
    @Test
    void testEnemyLineinteractionToTheLeft() {
        GameState gameState = new GameState();
        Enemy behemoth = new Enemy(350, 350, 50, 50);
        Point start = new Point(350,350);
        Point end = new Point(350,450);
        Line line = new Line(start,end);
        gameState.addEnemy(behemoth);
        boolean isBlocked = behemoth.isIntersectingLine(line);
        assertTrue(isBlocked);
    }
    @Test
    void testEnemyLineinteractionToTheRight() {
        GameState gameState = new GameState();
        Enemy behemoth = new Enemy(350, 350, 50, 50);
        Point start = new Point(450,350);
        Point end = new Point(450,450);
        Line line = new Line(start,end);
        gameState.addEnemy(behemoth);
        boolean isBlocked = behemoth.isIntersectingLine(line);
        assertTrue(isBlocked);
    }
    @Test
    void testEnemyLineinteractionAbove() {
        GameState gameState = new GameState();
        Enemy behemoth = new Enemy(350, 350, 50, 50);
        Point start = new Point(350,350);
        Point end = new Point(450,350);
        Line line = new Line(start,end);
        gameState.addEnemy(behemoth);
        boolean isBlocked = behemoth.isIntersectingLine(line);
        assertTrue(isBlocked);
    }
    @Test
    void testEnemyLineinteractionBelow() {
        GameState gameState = new GameState();
        Enemy behemoth = new Enemy(350, 350, 50, 50);
        Point start = new Point(350,450);
        Point end = new Point(450,450);
        Line line = new Line(start,end);
        gameState.addEnemy(behemoth);
        boolean isBlocked = behemoth.isIntersectingLine(line);
        assertTrue(isBlocked);
    }
    @Test
    void testEnemyLineinteractionOverlapping() {
        GameState gameState = new GameState();
        Enemy behemoth = new Enemy(350, 350, 50, 50);
        Point start = new Point(350,350);
        Point end = new Point(450,450);
        Line line = new Line(start,end);
        gameState.addEnemy(behemoth);
        boolean isBlocked = behemoth.isIntersectingLine(line);
        assertTrue(isBlocked);
    }
    @Test
    void testEnemyMovement() {
        GameState gameState = new GameState();
        Enemy behemoth = new Enemy(350, 350, 50, 50);
        gameState.addEnemy(behemoth);
        double initialDistance = Math.sqrt(Math.pow(350 - 50, 2) + Math.pow(350 - 50, 2));
        behemoth.moveTowardTarget();
        Rectangle bounds = behemoth.getBounds();
        double newDistance = Math.sqrt(Math.pow(bounds.x - 50, 2) + Math.pow(bounds.y - 50, 2));
        assertTrue(newDistance < initialDistance);
    }
    @Test
    void testRemoveEnemy() {
        GameState gameState = new GameState();
        Enemy behemoth = new Enemy(50, 50, 400, 400);
        gameState.addEnemy(behemoth);
        behemoth.removeHealth();
        behemoth.removeHealth();
        gameState.removeEnemy();
        assertEquals(0, gameState.getBehemothCount());
    }
    @Test
    void testAddLine() {
        GameState gameState = new GameState();
        Point start = new Point(350,350);
        Point end = new Point(450,450);
        //arbitrary values to create a line
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
    void testPlayerProjectileCollisionToTheRight() {
        Point2D direction = new Point2D.Double(449, 400);
        Projectile projectile = new Projectile(449,400, direction);
        //placing the projectile to the right of the player
        boolean isCollision = projectile.isCollidingWithPlayer();
        assertTrue(isCollision);
    }
    @Test
    void testPlayerProjectileCollisionToTheLeft() {
        Point2D direction = new Point2D.Double(350, 400);
        Projectile projectile = new Projectile(350,400, direction);
        boolean isCollision = projectile.isCollidingWithPlayer();
        assertTrue(isCollision);
    }
    @Test
    void testPlayerProjectileCollisionAbove() {
        Point2D direction = new Point2D.Double(400, 350);
        Projectile projectile = new Projectile(400,350, direction);
        boolean isCollision = projectile.isCollidingWithPlayer();
        assertTrue(isCollision);
    }
    @Test
    void testPlayerProjectileCollisionBelow() {
        Point2D direction = new Point2D.Double(400, 450);
        Projectile projectile = new Projectile(400,449, direction);
        boolean isCollision = projectile.isCollidingWithPlayer();
        assertTrue(isCollision);
    }
    @Test
    void testPlayerProjectileNotColliding() {
        Point2D direction = new Point2D.Double(500, 500);
        Projectile projectile = new Projectile(500,500, direction);
        boolean isCollision = projectile.isCollidingWithPlayer();
        assertFalse(isCollision);
    }
}
