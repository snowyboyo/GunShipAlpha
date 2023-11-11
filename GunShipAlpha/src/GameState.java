import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

class GameState {

    private CopyOnWriteArrayList<Behemoth> behemoths = new CopyOnWriteArrayList<>();
    private ArrayList<Line> lines = new ArrayList<>();
    private CopyOnWriteArrayList<Projectile> projectiles = new CopyOnWriteArrayList<>();
    private CopyOnWriteArrayList<Tank> tanks = new CopyOnWriteArrayList<>();


    void handleBehemothMovement() {
        Iterator<Behemoth> iterator = behemoths.iterator();
        while (iterator.hasNext()) {
            Behemoth enemy = iterator.next();
            if (!isBehemothBlocked(enemy)) {
                enemy.moveTowardTarget();
                removeEnemy();
            }
        }
    }
    void handleTankMovement() {
        Iterator<Tank> iterator = tanks.iterator();
        while (iterator.hasNext()) {
            Tank tank = iterator.next();
            if (!isTankBlocked(tank)) {
                tank.moveTowardTarget();
                removeEnemy();
            }
        }
    }


    boolean isBehemothBlocked(Behemoth enemy) {
        for (Line line : lines) {
            if (enemy.isIntersectingLine(line)) {
                return true;
            }
        }
        return false;
    }
    boolean isTankBlocked(Tank tank) {
        for (Line line : lines) {
            if (tank.isTankIntersectingLine(line)) {
                return true;
            }
        }
        return false;
    }

    boolean handleProjectileCollisionAndMoveEnemies() {
        handleBehemothMovement();
        handleTankMovement();
        boolean playerHit = false;
        ArrayList<Projectile> projectilesToRemove = new ArrayList<>();
        for (Projectile projectile : projectiles) {
            if (doesProjectileHitAnyBehemoths(projectile)||doesProjectileHitAnyTanks(projectile)) {
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

    boolean doesProjectileHitAnyBehemoths(Projectile projectile) {
        for (Behemoth enemy : behemoths) {
            if (enemy.isCollidingWithProjectile(projectile)) {
                return true;
            }
        }
        return false;
    }
    boolean doesProjectileHitAnyTanks(Projectile projectile) {
        for (Tank tank : tanks) {
            if (tank.isCollidingWithProjectile(projectile)) {
                return true;
            }
        }
        return false;
    }

    void handleHitForProjectile(Projectile projectile, ArrayList<Projectile> projectilesToRemove) {
        for (Behemoth enemy : behemoths) {
            if (enemy.isCollidingWithProjectile(projectile)) {
                enemy.removeHealth();
                projectilesToRemove.add(projectile);
                return;
            }
        }
    }
    public void removeEnemy() {
        Iterator<Behemoth> iterator = behemoths.iterator();
        while (iterator.hasNext()) {
            Behemoth enemy = iterator.next();
            if (enemy.isDead()) {
                behemoths.remove(enemy);
            }
        }
    }

    void removeExpiredProjectilesAndLines(int canvasWidth, int canvasHeight) {
        projectiles.removeIf(projectile -> projectile.isOutOfBounds(canvasWidth, canvasHeight));
        lines.removeIf(Line::isExpired);
    }

    void drawBehemoths(Graphics g) {
        for (Behemoth enemy : behemoths) {
            enemy.draw(g);
        }
    }

    void addBehemoth(Behemoth enemy) {
        behemoths.add(enemy);
    }
    void addTank(Tank enemy) {
        tanks.add(enemy);
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
    public boolean checkBehemothPlayerCollisions(Player player, int canvasWidth, int canvasHeight) {
        for (Behemoth enemy : behemoths) {
            if (player.isTouchedbyBehemoth(enemy, canvasWidth, canvasHeight)) {
                return true;
            }
        }
        return false;
    }
    public boolean checkTankPlayerCollisions(Player player, int canvasWidth, int canvasHeight) {
        for (Tank tank : tanks) {
            if (player.isTouchedbyTank(tank, canvasWidth, canvasHeight)) {
                return true;
            }
        }
        return false;
    }
    public int getBehemothCount(){
        return behemoths.size();
    }
    public int getLineListSize(){
        return lines.size();
    }
}
