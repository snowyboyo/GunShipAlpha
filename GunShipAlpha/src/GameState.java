import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

class GameState {
    TankExplosion explosion = new TankExplosion();


    private CopyOnWriteArrayList<Enemy> enemies = new CopyOnWriteArrayList<>();
    private ArrayList<Line> lines = new ArrayList<>();
    private CopyOnWriteArrayList<Projectile> projectiles = new CopyOnWriteArrayList<>();
    public boolean areAllEnemiesDefeated() {
        return enemies.isEmpty();
    }

    public void update(Player player) {
        forEachEnemy(enemy -> {
            if (!isBehemothBlocked(enemy)) {
                enemy.moveTowardTarget();
            }
            if (enemy instanceof Tank) {
                Tank tank = (Tank) enemy;
                if (tank.isDead()) {
                    return;
                }
                if (explosion.shouldExplode(tank, lines, projectiles, player)) {
                    tank.explode();
                }
            }
        });
    }
    public void removeDeadEnemies() {
        enemies.removeIf(Enemy::isDead);
    }



    boolean isBehemothBlocked(Enemy enemy) {
        for (Line line : lines) {
            if (enemy.isIntersectingLine(line)) {
                return true;
            }
        }
        return false;
    }

    boolean handleProjectileCollison(Player player) {
        boolean playerHit = false;
        ArrayList<Projectile> projectilesToRemove = new ArrayList<>();
        for (Projectile projectile : projectiles) {
            for (Line line : lines) {
                if (projectile.isCollidingWithLine(line)) {
                    projectilesToRemove.add(projectile);
                    break;
                }
            }
            if (projectilesToRemove.contains(projectile)) continue;

            if (doesProjectileHitAnyBehemoths(projectile)) {
                handleHitForProjectile(projectile, projectilesToRemove);
            }

            if (projectile.isCollidingWithPlayer()) {
                playerHit = true;
                player.reduceHealth();
                projectilesToRemove.add(projectile);
            }
        }

        projectiles.removeAll(projectilesToRemove);
        return playerHit;
    }

    boolean doesProjectileHitAnyBehemoths(Projectile projectile) {
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
    public void forEachEnemy(Consumer<Enemy> action) {
        for (Enemy enemy : enemies) {
            action.accept(enemy);
        }
    }

    void addLine(MouseEvent e, Point startPoint) {
        Line newLine = new Line(startPoint, e.getPoint());
        lines.add(newLine);
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
    public boolean checkBehemothPlayerCollisions(Player player) {
        for (Enemy enemy : enemies) {
            if (player.isTouchedByEnemy(enemy)) {
                player.reduceHealth();
                enemy.removeHealth();
                enemy.removeHealth();

                return true;
            }
        }
        return false;
    }
    public int getBehemothCount(){
        return enemies.size();
    }
    public int getLineListSize(){
        return lines.size();
    }
    public boolean isSpaceOccupied(Rectangle newEnemyBounds) {
        for (Enemy existingEnemy : enemies) {
            Rectangle existingEnemyBounds = existingEnemy.getBounds();
            if (existingEnemyBounds.intersects(newEnemyBounds)) {
                return true;
            }
        }
        return false;
    }
    public void handleExplosion(Point center, int radius,Player player) {
        player.checkAndHandleExplosion(center, radius);
        lines.removeIf(line -> line.isWithinExplosionRange(center, radius));

    }
}
