import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

class GameState {

    private ArrayList<Enemy> enemies = new ArrayList<>();
    private ArrayList<LineLogic> lines = new ArrayList<>();
    private ArrayList<Projectile> projectiles = new ArrayList<>();

    void handleEnemyMovement() {
        for (Enemy enemy : enemies) {
            if (!isEnemyBlocked(enemy)) {
                enemy.moveTowardTarget();
            }
        }
    }

    boolean isEnemyBlocked(Enemy enemy) {
        for (LineLogic line : lines) {
            if (enemy.isIntersectingLine(line)) {
                return true;
            }
        }
        return false;
    }

    void handleProjectileCollision() {
        ArrayList<Projectile> projectilesToRemove = new ArrayList<>();
        for (Projectile projectile : projectiles) {
            if (doesProjectileHitAnyEnemy(projectile)) {
                handleHitForProjectile(projectile, projectilesToRemove);
            }
        }
        projectiles.removeAll(projectilesToRemove);
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
                if (enemy.isDead()) {
                    enemies.remove(enemy);
                }
                return;
            }
        }
    }

    void removeExpiredProjectilesAndLines(int canvasWidth, int canvasHeight) {
        projectiles.removeIf(projectile -> projectile.isOutOfBounds(canvasWidth, canvasHeight));
        lines.removeIf(LineLogic::isExpired);
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
        lines.add(new LineLogic(startPoint, e.getPoint()));
    }

    void addProjectile(Projectile projectile) {
        projectiles.add(projectile);
    }

    void drawLines(Graphics g) {
        for (LineLogic line : lines) {
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
}
