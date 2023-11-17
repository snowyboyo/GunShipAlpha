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
       Point  location = new Point(350,350);
        Point  target = new Point(50,50);
        Enemy enemy = new Enemy(location, target);
        gameState.addEnemy(enemy);
        boolean isCollision = gameState.checkBehemothPlayerCollisions(game.player,game);
        assertTrue(isCollision);
    }
    @Test
    void testEnemyPlayerCollisionsToTheLeft() {
        GameState gameState = new GameState();
        Game game = new Game();
        Point  location = new Point(805,475);
        Point  target = new Point(350,350);
        Enemy enemy = new Enemy(location, target);
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
        Point  location = new Point(895,450);
        Point  target = new Point(400,450);
        Enemy enemy = new Enemy(location, target);
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
        Point  location = new Point(850,405);
        Point  target = new Point(850,405);
        Enemy enemy = new Enemy(location, target);
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
        Point  location = new Point(850,495);
        Point  target = new Point(850,495);
        Enemy enemy = new Enemy(location, target);
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
        Point  location = new Point(700,350);
        Point  target = new Point(50,50);
        Enemy enemy = new Enemy(location, target);
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
        Point  location = new Point(876,425);
        Point  target = new Point(880,430);
        Enemy enemy = new Enemy(location, target);
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
        Point  location = new Point(350,400);
        Point  target = new Point(395,400);
        Enemy enemy = new Enemy(location, target);
        Point2D direction = new Point2D.Double(450, 400);
        Projectile projectile = new Projectile(395,400, direction, true);
        gameState.addEnemy(enemy);
        boolean isCollision = enemy.isCollidingWithProjectile(projectile);
        assertTrue(isCollision);
    }
    @Test
    void testEnemyProjectileCollisionToTheLeft() {
        GameState gameState = new GameState();
        Point  location = new Point(350,350);
        Point  target = new Point(305,375);
        Enemy enemy = new Enemy(location, target);
        Point2D direction = new Point2D.Double(350, 400);
        Projectile projectile = new Projectile(350,375, direction, true);
        gameState.addEnemy(enemy);
        boolean isCollision = enemy.isCollidingWithProjectile(projectile);
        assertTrue(isCollision);
    }
    @Test
    void testEnemyProjectileCollisionAbove() {
        GameState gameState = new GameState();
        Point  location = new Point(350,350);
        Point  target = new Point(50,50);
        Enemy enemy = new Enemy(location, target);
        Point2D direction = new Point2D.Double(400, 350);
        Projectile projectile = new Projectile(375,350, direction, true);
        gameState.addEnemy(enemy);
        boolean isCollision = enemy.isCollidingWithProjectile(projectile);
        assertTrue(isCollision);
    }
    @Test
    void testEnemyProjectileCollisionBelow() {
        GameState gameState = new GameState();
        Point  location = new Point(350,350);
        Point  target = new Point(50,50);
        Enemy enemy = new Enemy(location, target);
        Point2D direction = new Point2D.Double(400, 449);
        Projectile projectile = new Projectile(375,399, direction , true);
        gameState.addEnemy(enemy);
        boolean isCollision = enemy.isCollidingWithProjectile(projectile);
        assertTrue(isCollision);
    }
    @Test
    void testEnemyProjectileCollisionOverlapping() {
        GameState gameState = new GameState();
        Point  location = new Point(350,350);
        Point  target = new Point(50,50);
        Enemy enemy = new Enemy(location, target);
        Point2D direction = new Point2D.Double(400, 400);
        Projectile projectile = new Projectile(355,355, direction, true);
        gameState.addEnemy(enemy);
        boolean isCollision = enemy.isCollidingWithProjectile(projectile);
        assertTrue(isCollision);
    }
    @Test
    void testEnemyProjectileNotColliding() {
        GameState gameState = new GameState();
        Point  location = new Point(350,350);
        Point  target = new Point(50,50);
        Enemy enemy = new Enemy(location, target);
        Point2D direction = new Point2D.Double(500, 500);
        Projectile projectile = new Projectile(500,500, direction, false);
        gameState.addEnemy(enemy);
        boolean isCollision = enemy.isCollidingWithProjectile(projectile);
        assertFalse(isCollision);
    }
//    @Test
//    void testEnemyLineinteractionToTheLeft() {
//        GameState gameState = new GameState();
//        Point  location = new Point(350,350);
//        Point  target = new Point(50,50);
//        Enemy enemy = new Enemy(location, target);
//        Point start = new Point(350,350);
//        Point end = new Point(350,450);
//        Line line = new Line(start,end);
//        gameState.addEnemy(enemy);
//        boolean isBlocked = enemy.isIntersectingLine(line);
//        assertTrue(isBlocked);
//    }
//    @Test
//    void testEnemyLineinteractionToTheRight() {
//        GameState gameState = new GameState();
//        Point  location = new Point(350,350);
//        Point  target = new Point(50,50);
//        Enemy enemy = new Enemy(location, target);
//        Point start = new Point(400,350);
//        Point end = new Point(400,375);
//        Line line = new Line(start,end);
//        gameState.addEnemy(enemy);
//        boolean isBlocked = enemy.isIntersectingLine(line);
//        assertTrue(isBlocked);
//    }
//    @Test
//    void testEnemyLineinteractionAbove() {
//        GameState gameState = new GameState();
//        Point  location = new Point(350,350);
//        Point  target = new Point(50,50);
//        Enemy enemy = new Enemy(location, target);
//        Point start = new Point(350,350);
//        Point end = new Point(450,350);
//        Line line = new Line(start,end);
//        gameState.addEnemy(enemy);
//        boolean isBlocked = enemy.isIntersectingLine(line);
//        assertTrue(isBlocked);
//    }
//    @Test
//    void testEnemyLineinteractionBelow() {
//        GameState gameState = new GameState();
//        Point  location = new Point(350,350);
//        Point  target = new Point(50,50);
//        Enemy enemy = new Enemy(location, target);
//        Point start = new Point(350,400);
//        Point end = new Point(375,399);
//        Line line = new Line(start,end);
//        gameState.addEnemy(enemy);
//        boolean isBlocked = enemy.isIntersectingLine(line);
//        assertTrue(isBlocked);
//    }
//    @Test
//    void testEnemyLineinteractionOverlapping() {
//        GameState gameState = new GameState();
//        Point  location = new Point(350,350);
//        Point  target = new Point(50,50);
//        Enemy enemy = new Enemy(location, target);
//        Point start = new Point(350,350);
//        Point end = new Point(450,450);
//        Line line = new Line(start,end);
//        gameState.addEnemy(enemy);
//        boolean isBlocked = enemy.isIntersectingLine(line);
//        assertTrue(isBlocked);
//    }
    @Test
    void testEnemyMovement() {
        GameState gameState = new GameState();
        Point  location = new Point(350,350);
        Point  target = new Point(50,50);
        Enemy enemy = new Enemy(location, target);
        gameState.addEnemy(enemy);
        double initialDistance = Math.sqrt(Math.pow(350 - 50, 2) + Math.pow(350 - 50, 2));
        enemy.moveTowardTarget();
        Rectangle bounds = enemy.getBounds();
        double newDistance = Math.sqrt(Math.pow(bounds.x - 50, 2) + Math.pow(bounds.y - 50, 2));
        assertTrue(newDistance < initialDistance);
    }
    @Test
    void testRemoveEnemy() {
        GameState gameState = new GameState();
        Point  location = new Point(350,350);
        Point  target = new Point(50,50);
        Enemy enemy = new Enemy(location, target);
        gameState.addEnemy(enemy);
        enemy.removeHealth();
        enemy.removeHealth();
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
