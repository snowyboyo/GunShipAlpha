package GSA;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

class GameState {
    Explosion explosion = new Explosion();
    protected CopyOnWriteArrayList<Enemy> enemies = new CopyOnWriteArrayList<>();
    protected ArrayList<Line> lines = new ArrayList<>();
    protected CopyOnWriteArrayList<Projectile> projectiles = new CopyOnWriteArrayList<>();

    public boolean areAllEnemiesDefeated() {
        return enemies.isEmpty();
    }


    public int getEnemyCount() {
        return enemies.size();
    }
    public int getLineCount() {
        return lines.size();
    }

    public void updateTankTargets(Point newTarget) {
        for (Enemy tank : enemies) {
            tank.setTarget(newTarget);
        }
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

    boolean handleProjectileCollison(Player player, Game game) {
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

            if (projectile.isCollidingWithPlayer(player)) {
                game.playerHit();
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
                Game.addPlayerScore();
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

    void addLine(Point endPoint, Point startPoint) {
        Line newLine = new Line(startPoint, endPoint);
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

    public boolean checkBehemothPlayerCollisions(Player player, Game game) {
        for (Enemy enemy : enemies) {
            if (player.isTouchedByEnemy(enemy)) {
                game.playerHit();
                player.reduceHealth();
                enemy.removeHealth();
                enemy.removeHealth();

                return true;
            }
        }
        return false;
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

    public void handleExplosion(Point center, int radius) {
        lines.removeIf(line -> line.isWithinExplosionRange(center, radius));

    }
}
