package GSA;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

class GameState {
    TankExplosion explosion = new TankExplosion();
    protected CopyOnWriteArrayList<Tank> enemies = new CopyOnWriteArrayList<>();
    protected ArrayList<Line> lines = new ArrayList<>();
    protected CopyOnWriteArrayList<Projectile> projectiles = new CopyOnWriteArrayList<>();
    public boolean areAllEnemiesDefeated() {
        return enemies.isEmpty();
    }



    public void updateTankTargets(Point newTarget) {
        for (Tank tank : enemies) {
            tank.setTarget(newTarget);
        }
    }
    public void removeDeadEnemies() {
        enemies.removeIf(Tank::isDead);
    }



    boolean isBehemothBlocked(Tank enemy) {
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
        for (Tank enemy : enemies) {
            if (enemy.isCollidingWithProjectile(projectile)) {
                return true;
            }
        }
        return false;
    }

    void handleHitForProjectile(Projectile projectile, ArrayList<Projectile> projectilesToRemove) {
        for (Tank enemy : enemies) {
            if (enemy.isCollidingWithProjectile(projectile)) {
                enemy.removeHealth();
                projectilesToRemove.add(projectile);
                Game.addPlayerScore();
                return;
            }
        }
    }
    public void removeEnemy() {
        Iterator<Tank> iterator = enemies.iterator();
        while (iterator.hasNext()) {
            Tank enemy = iterator.next();
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
        for (Tank enemy : enemies) {
            enemy.draw(g);
        }
    }

    void addEnemy(Tank enemy) {
        enemies.add(enemy);
    }
    public void forEachEnemy(Consumer<Tank> action) {
        for (Tank enemy : enemies) {
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
    public boolean checkBehemothPlayerCollisions(Player player) {
        for (Tank enemy : enemies) {
            if (player.isTouchedByEnemy(enemy)) {
                player.reduceHealth();
                enemy.removeHealth();
                enemy.removeHealth();

                return true;
            }
        }
        return false;
    }
    public boolean isSpaceOccupied(Rectangle newEnemyBounds) {
        for (Tank existingEnemy : enemies) {
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
