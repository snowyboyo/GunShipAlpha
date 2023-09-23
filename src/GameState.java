import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

class GameState {

    private ArrayList<Game.Enemy> enemies = new ArrayList<>();
   private ArrayList<Game.lineLogic> lines = new ArrayList<>();
    private ArrayList<Game.Projectile> projectiles = new ArrayList<>();
    BufferedImage enemySprite;
    {
        try {
            enemySprite = ImageIO.read(new File("src/Images/Behemoth.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    void handleEnemyMovement() {
        for (Game.Enemy enemy : enemies) {
            if (!isEnemyBlocked(enemy)) {
                enemy.moveTowardTarget();
            }
        }
    }
    boolean isEnemyBlocked(Game.Enemy enemy) {
        for (Game.lineLogic line : lines) {
            if (line.intersects(enemy)) {
                return true;
            }
        }
        return false;
    }

     void handleProjectileCollision() {
        ArrayList<Game.Projectile> projectilesToRemove = new ArrayList<>();
        for (Game.Projectile projectile : projectiles) {
            checkProjectileCollisions(projectile, projectilesToRemove);
        }
        projectiles.removeAll(projectilesToRemove);
    }

    void checkProjectileCollisions(Game.Projectile projectile, ArrayList<Game.Projectile> projectilesToRemove) {
        enemies.forEach(enemy -> handleProjectileCollision(enemy, projectile, projectilesToRemove));
    }

     void handleProjectileCollision(Game.Enemy enemy, Game.Projectile projectile, ArrayList<Game.Projectile> projectilesToRemove) {
        if (isProjectileCollidingWithEnemy(enemy, projectile)) {
            handleHit(enemy, projectile, projectilesToRemove);
        }
    }

     boolean isProjectileCollidingWithEnemy(Game.Enemy enemy, Game.Projectile projectile) {
        Rectangle enemyBounds = new Rectangle(enemy.x, enemy.y, enemySprite.getWidth(), enemySprite.getHeight());
        return enemyBounds.contains(projectile.x, projectile.y);
    }

     void handleHit(Game.Enemy enemy, Game.Projectile projectile, ArrayList<Game.Projectile> projectilesToRemove) {
        enemy.health--;
        projectilesToRemove.add(projectile);
        if (enemy.health <= 0) {
            enemies.remove(enemy);
        }
    }

    void removeExpiredProjectilesAndLines(int canvasWidth, int canvasHeight) {
        projectiles.removeIf(projectile -> projectile.x < 0 || projectile.x > canvasWidth || projectile.y < 0 || projectile.y > canvasHeight);
        lines.removeIf(line -> Game.ms.isExpired(line.creationTime));
    }
    Rectangle createEnemyHitbox(Game.Enemy enemy) {
        return new Rectangle(enemy.x, enemy.y, enemySprite.getWidth(), enemySprite.getHeight());
    }



     void drawBehemoths(Graphics g) {
        for (Game.Enemy enemy : enemies) {
            g.drawImage(enemySprite, enemy.x, enemy.y, null);
        }
    }
    void addEnemy(Game.Enemy enemy){
        enemies.add(enemy);
     }
     void addLine(MouseEvent e, Point startPoint){
         lines.add(new Game.lineLogic(startPoint, e.getPoint()));
     }
     void addProjectile(Game.Projectile projectile){
         projectiles.add(projectile);
     }
     void drawLines(Graphics g) {
        for (Game.lineLogic line : lines) {
            g.drawLine(line.start.x, line.start.y, line.end.x, line.end.y);
        }
    }
     void drawProjectile(Graphics g) {
        for (Game.Projectile projectile : projectiles) {
            projectile.draw(g);
        }
    }

     void updateProjectile() {
        for (Game.Projectile projectile : projectiles) {
            projectile.update();
        }
    }

}
