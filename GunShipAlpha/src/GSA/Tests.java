package GSA;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.awt.*;
import java.awt.geom.Point2D;

class GameStateTest {

    @Test
    void testEnemyPlayerCollisionsOverlapping() {
        GameState gameState = new GameState();
       Game game = new Game();
        Enemy enemy = new Enemy(350, 350, 50, 50);
        gameState.addEnemy(enemy);
        boolean isCollision = gameState.checkBehemothPlayerCollisions(game.player,game);
        assertTrue(isCollision);
    }
    @Test
    void testEnemyPlayerCollisionsToTheLeft() {
        GameState gameState = new GameState();
        Game game = new Game();
        Enemy enemy = new Enemy(805, 475, 350, 350);
        //placing the enemy to the left of the player
        gameState.addEnemy(enemy);
        enemy.moveTowardTarget();
        boolean isCollision = gameState.checkBehemothPlayerCollisions(game.player,game);
        assertTrue(isCollision);
    }
    @Test
    void testEnemyPlayerCollisionsToTheRight() {
        GameState gameState = new GameState();
        Game game = new Game();
        Enemy enemy = new Enemy(895, 450, 900, 450);
        //placing the enemy to the right of the player
        gameState.addEnemy(enemy);
        enemy.moveTowardTarget();
        boolean isCollision = gameState.checkBehemothPlayerCollisions(game.player,game);
        assertTrue(isCollision);
    }
    @Test
    void testEnemyPlayerCollisionsFromTheTop() {
        GameState gameState = new GameState();
        Game game = new Game();
        Enemy enemy = new Enemy(850, 405, 850, 405);
        //placing the enemy above the player
        gameState.addEnemy(enemy);
        enemy.moveTowardTarget();
        boolean isCollision = gameState.checkBehemothPlayerCollisions(game.player,game);
        assertTrue(isCollision);
    }
    @Test
    void testEnemyPlayerCollisionsFromTheBottom() {
        GameState gameState = new GameState();
        Game game = new Game();
        Enemy enemy = new Enemy(850, 495, 850, 495);
        //placing the enemy below the player
        gameState.addEnemy(enemy);
        enemy.moveTowardTarget();
        boolean isCollision = gameState.checkBehemothPlayerCollisions(game.player,game);
        assertTrue(isCollision);
    }
    @Test
    void testEnemyPlayerNotColliding() {
        GameState gameState = new GameState();
        Game game = new Game();
        Enemy enemy = new Enemy(700, 350, 50, 50);
        //350,350 = one pixel away from the player
        gameState.addEnemy(enemy);
        boolean isCollision = gameState.checkBehemothPlayerCollisions(game.player,game);
        assertFalse(isCollision);
    }
    @Test
    void testExplosionLineCollision() {
        Point location = new Point(350,400);
        Point velocity = new Point(350,400);
        ExplosionParticle particle = new ExplosionParticle(location,velocity);
        Point start = new Point(350,350);
        Point end = new Point(350,450);
        GameState gs = new GameState();
        gs.addLine(end,start);
        boolean shouldRemove = true;
        boolean isCollision = Game.particleCollidedWithLine(particle,shouldRemove);
        assertTrue(isCollision);
    }
    @Test
    void testExplosionEnemyCollisionToTheRight() {
        Game game = new Game();
        Enemy enemy = new Enemy(876,425,880,430);
        game.addParticles(enemy);
        boolean isCollision = game.particleCollidedWithEnemy(enemy,true);
        assertTrue(isCollision);
    }
    @Test
    void testExplosionPlayerCollisionToTheRight() {
        Game game = new Game();
        Point location = new Point(876,425);
        Point velocity = new Point(350,400);
        ExplosionParticle particle = new ExplosionParticle(location,velocity);
        boolean isCollision = game.particleCollidedWithPlayer(particle,true);
        assertTrue(isCollision);
    }
    @Test
    void testExplosionPlayerCollisionToTheLeft() {
        Game game = new Game();
        Point location = new Point(799,425);
        Point velocity = new Point(350,425);
        ExplosionParticle particle = new ExplosionParticle(location,velocity);
        boolean isCollision = game.particleCollidedWithPlayer(particle,true);
        assertTrue(isCollision);
    }
    @Test
    void testExplosionPlayerCollisionAbove() {
        Game game = new Game();
        Point location = new Point(875,449);
        Point velocity = new Point(350,400);
        ExplosionParticle particle = new ExplosionParticle(location,velocity);
        boolean isCollision = game.particleCollidedWithPlayer(particle,true);
        assertTrue(isCollision);
    }
    @Test
    void testExplosionPlayerCollisionBelow() {
        Game game = new Game();
        Point location = new Point(875,501);
        Point velocity = new Point(350,400);
        ExplosionParticle particle = new ExplosionParticle(location,velocity);
        boolean isCollision = game.particleCollidedWithPlayer(particle,true);
        assertTrue(isCollision);
    }
    @Test
    void testEnemyProjectileCollisionToTheRight() {
        GameState gameState = new GameState();
        Enemy behemoth = new Enemy(350, 400, 395, 400);
        Point2D direction = new Point2D.Double(450, 400);
        Projectile projectile = new Projectile(395,400, direction, true);
        gameState.addEnemy(behemoth);
        boolean isCollision = behemoth.isCollidingWithProjectile(projectile);
        assertTrue(isCollision);
    }
    @Test
    void testEnemyProjectileCollisionToTheLeft() {
        GameState gameState = new GameState();
        Enemy behemoth = new Enemy(350, 350, 305, 375);
        Point2D direction = new Point2D.Double(350, 400);
        Projectile projectile = new Projectile(350,375, direction, true);
        gameState.addEnemy(behemoth);
        boolean isCollision = behemoth.isCollidingWithProjectile(projectile);
        assertTrue(isCollision);
    }
    @Test
    void testEnemyProjectileCollisionAbove() {
        GameState gameState = new GameState();
        Enemy behemoth = new Enemy(350, 350, 50, 50);
        Point2D direction = new Point2D.Double(400, 350);
        Projectile projectile = new Projectile(375,350, direction, true);
        gameState.addEnemy(behemoth);
        boolean isCollision = behemoth.isCollidingWithProjectile(projectile);
        assertTrue(isCollision);
    }
    @Test
    void testEnemyProjectileCollisionBelow() {
        GameState gameState = new GameState();
        Enemy behemoth = new Enemy(350, 350, 50, 50);
        Point2D direction = new Point2D.Double(400, 449);
        Projectile projectile = new Projectile(375,399, direction , true);
        gameState.addEnemy(behemoth);
        boolean isCollision = behemoth.isCollidingWithProjectile(projectile);
        assertTrue(isCollision);
    }
    @Test
    void testEnemyProjectileCollisionOverlapping() {
        GameState gameState = new GameState();
        Enemy behemoth = new Enemy(350, 350, 50, 50);
        Point2D direction = new Point2D.Double(400, 400);
        Projectile projectile = new Projectile(355,355, direction, true);
        gameState.addEnemy(behemoth);
        boolean isCollision = behemoth.isCollidingWithProjectile(projectile);
        assertTrue(isCollision);
    }
    @Test
    void testEnemyProjectileNotColliding() {
        GameState gameState = new GameState();
        Enemy behemoth = new Enemy(350, 350, 50, 50);
        Point2D direction = new Point2D.Double(500, 500);
        Projectile projectile = new Projectile(500,500, direction, false);
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
        Point start = new Point(400,350);
        Point end = new Point(400,375);
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
        Point start = new Point(350,400);
        Point end = new Point(375,399);
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
        assertEquals(0, gameState.getEnemyCount());
    }
    @Test
    void testAddLine() {
        GameState gameState = new GameState();
        Point start = new Point(350,350);
        Point end = new Point(450,450);
        gameState.addLine(end,start);
        assertEquals(1, gameState.getLineCount());
    }
    @Test
    void testRemovePlayerHealth() {
        Game game = new Game();
        for(int i = 0; i < 10; i++) {
            game.player.reduceHealth();
        }
        assertTrue(game.player.isDead());
    }
    @Test
    void testPlayerProjectileCollisionToTheRight() {
        Point2D direction = new Point2D.Double(850, 425);
        Projectile projectile = new Projectile(900,475, direction, false);
        //placing the projectile to the right of the player
        Game game = new Game();
        boolean isCollision = projectile.isCollidingWithPlayer(game.player);
        assertTrue(isCollision);
    }
    @Test
    void testPlayerProjectileCollisionToTheLeft() {
        Point2D direction = new Point2D.Double(850, 400);
        Projectile projectile = new Projectile(850,475, direction, false);
        Game game = new Game();
        boolean isCollision = projectile.isCollidingWithPlayer(game.player);
        assertTrue(isCollision);
    }
    @Test
    void testPlayerProjectileCollisionAbove() {
        Point2D direction = new Point2D.Double(875, 450);
        Projectile projectile = new Projectile(875,449, direction, false);
        Game game = new Game();
        boolean isCollision = projectile.isCollidingWithPlayer(game.player);
        assertTrue(isCollision);
    }
    @Test
    void testPlayerProjectileCollisionBelow() {
        Point2D direction = new Point2D.Double(875, 500);
        Projectile projectile = new Projectile(875,501, direction, false);
        Game game = new Game();
        boolean isCollision = projectile.isCollidingWithPlayer(game.player);
        assertTrue(isCollision);
    }
    @Test
    void testPlayerProjectileNotColliding() {
        Point2D direction = new Point2D.Double(800, 425);
        Projectile projectile = new Projectile(800,425, direction, false);
        Game game = new Game();
        boolean isCollision = projectile.isCollidingWithPlayer(game.player);
        assertFalse(isCollision);
    }
}
