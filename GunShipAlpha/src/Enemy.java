import java.awt.*;

class Enemy {
    private int x, y;
    private final int targetX, targetY;
    private int health = 2;
    public Enemy(int x, int y, int targetX, int targetY) {
        this.x = x;
        this.y = y;
        this.targetX = targetX;
        this.targetY = targetY;
    }

    public void moveTowardTarget() {
        if (x < targetX) x++;
        if (x > targetX) x--;
        if (y < targetY) y++;
        if (y > targetY) y--;
    }
    void removeHealth() {
        health--;
    }
    public boolean isCollidingWithProjectile(Projectile projectile) {
        Rectangle enemyBounds = new Rectangle(x, y, EnemySprite.width(), EnemySprite.height());
        return projectile.isInsideEnemy(enemyBounds);
    }
    public void draw(Graphics g) {
        g.drawImage(EnemySprite.sprite(), x, y, null);
    }
    public boolean isDead() {
        return health <= 0;
    }
    public boolean isIntersectingLine(LineLogic line) {
        Rectangle enemyBounds = createBounds();
        return line.intersects(enemyBounds);
    }

    private Rectangle createBounds() {
        return new Rectangle(x, y, EnemySprite.width(), EnemySprite.height());
    }
}
