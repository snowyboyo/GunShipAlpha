import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

class GameState {

    private CopyOnWriteArrayList<Enemy> enemies = new CopyOnWriteArrayList<>();
    private ArrayList<Line> lines = new ArrayList<>();
    private CopyOnWriteArrayList<Projectile> projectiles = new CopyOnWriteArrayList<>();



    void handleEnemyMovement() {
        Iterator<Enemy> iterator = enemies.iterator();
        while (iterator.hasNext()) {
            Enemy enemy = iterator.next();
            if (!isEnemyBlocked(enemy)) {
                enemy.moveTowardTarget();
                removeEnemy();
            }
        }
    }


    boolean isEnemyBlocked(Enemy enemy) {
        for (Line line : lines) {
            if (enemy.isIntersectingLine(line)) {
                return true;
            }
        }
        return false;
    }

    boolean handleProjectileCollisionAndMoveEnemies() {
        handleEnemyMovement();
        boolean playerHit = false;
        ArrayList<Projectile> projectilesToRemove = new ArrayList<>();
        for (Projectile projectile : projectiles) {
            if (doesProjectileHitAnyEnemy(projectile)) {
                handleHitForProjectile(projectile, projectilesToRemove);
            }
            if (projectile.isCollidingWithPlayer()) {
                playerHit = true;
                System.out.println(playerHit);
                projectilesToRemove.add(projectile);
            }
        }
        projectiles.removeAll(projectilesToRemove);
        return playerHit;
    }

    boolean doesProjectileHitAnyEnemy(Projectile projectile) {
        for (Enemy enemy : enemies) {
            if (enemy.isCollidingWithProjectile(projectile)) {
                return true;
            }
        }
        return false;
    }

    void handleHitForProjectile(Projectile projectile, ArrayList<Projectile> projectilesToRemove) {
        for (Enemy enemy : enemies) {
            if (enemy.isCollidingWithProjectile(projectile)) {
                enemy.removeHealth();
                projectilesToRemove.add(projectile);
                return;
            }
        }
    }
    public void removeEnemy() {
        Iterator<Enemy> iterator = enemies.iterator();
        while (iterator.hasNext()) {
            Enemy enemy = iterator.next();
            if (enemy.isDead()) {
                enemies.remove(enemy);
            }
        }
    }

    void removeExpiredProjectilesAndLines(int canvasWidth, int canvasHeight) {
        projectiles.removeIf(projectile -> projectile.isOutOfBounds(canvasWidth, canvasHeight));
        lines.removeIf(Line::isExpired);
    }

    void drawBehemoths(Graphics g) {
        for (Enemy enemy : enemies) {
            enemy.draw(g);
        }
    }

    void addEnemy(Enemy enemy) {
        enemies.add(enemy);
    }

    void addLine(MouseEvent e, Point startPoint) {
        lines.add(new Line(startPoint, e.getPoint()));
    }

    void addProjectile(Projectile projectile) {
        projectiles.add(projectile);
    }

    void drawLines(Graphics g) {
        for (Line line : lines) {
            line.draw(g);
        }
    }

    void drawProjectile(Graphics g) {
        for (Projectile projectile : projectiles) {
            projectile.draw(g);
        }
    }

    void updateProjectile() {
        for (Projectile projectile : projectiles) {
            projectile.update();
        }
    }
    public boolean checkEnemyPlayerCollisions(Player player, int canvasWidth, int canvasHeight) {
        for (Enemy enemy : enemies) {
            if (player.isTouchedByEnemy(enemy, canvasWidth, canvasHeight)) {
                return true;
            }
        }
        return false;
    }
}
