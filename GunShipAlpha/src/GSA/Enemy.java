package GSA;
import java.util.*;
import java.awt.*;
        import java.awt.geom.Point2D;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static GSA.Game.gs;

class Enemy {
    protected Point location;
    protected Point target;
    protected int health = 2;
    private Timer firingTimer;
    protected EnemySprites sprites;
    protected List<ExplosionParticle> lastGeneratedParticles;
    protected final int size = 50;
    protected double TAU = Math.PI * 2;
    protected double rotationAngle = 0.0;

    public Enemy(int x, int y, int targetX, int targetY) {
        this.location = new Point(x, y);
        this.target = new Point(targetX, targetY);
        this.sprites = new EnemySprites("Tank.png");
        this.rotationAngle = calculateRotationAngle(target);
        scheduleFiring();
    }
    public List<ExplosionParticle> returnLastGeneratedParticles(){
        return Collections.emptyList();
    }
    public List<ExplosionParticle> explode() {
        return Collections.emptyList();
    }
    public void update(Explosion explosion, ArrayList<Line> lines, CopyOnWriteArrayList<Projectile> projectiles, Player player) {
        if (!gs.isBehemothBlocked(this)) {
            moveTowardTarget();
        }
    }
    public double calculateRotationAngle(Point playerLocation) {
        double deltaY = playerLocation.y - location.y;
        double deltaX = playerLocation.x - location.x;
        return Math.atan2(deltaY, deltaX);
    }
    void scheduleFiring() {
        if (firingTimer != null) {
            firingTimer.cancel();
        }
        firingTimer = new Timer();
        TimerTask firingTask = new TimerTask() {
            @Override
            public void run() {
                if (Game.gameOver) {
                    firingTimer.cancel();
                    return;
                }
                Projectile projectile = fireAtPlayer();
                gs.addProjectile(projectile);
                scheduleFiring();
            }
        };
        Random random = new Random();
        long delay = random.nextInt(3000);
        firingTimer.schedule(firingTask, delay);
    }
    public void moveTowardTarget() {
        double dx = target.x  - location.x;
        double dy = target.y - location.y;
        double distance = Math.sqrt(dx * dx + dy * dy);
        double unitX = dx / distance;
        double unitY = dy / distance;
        int speed = 4;
        double velocityX = unitX * speed;
        double velocityY = unitY * speed;
        location.x += (int) velocityX;
        location.y += (int) velocityY;
    }
    public void setTarget(Point newTarget) {
        this.target = newTarget;
    }

   protected boolean hasEnemyReachedTarget() {
        if(location.x == target.x && location.y == target.y ) {
            health-=2;
            gs.removeEnemy();
            return true;
        }
        return false;
    }

    void removeHealth() {
        health--;
        if (isDead()) {
            if (firingTimer != null) {
                firingTimer.cancel();
            }
        }
    }
    public boolean isCollidingWithProjectile(Projectile projectile) {
      Rectangle enemyBounds = sprites.getBounds(location.x, location.y);
        return projectile.isInsideEnemy(enemyBounds);
    }
    public boolean isCollidingWithParticle(ExplosionParticle particle) {
        Rectangle enemyBounds = sprites.getBounds(location.x, location.y);
        return particle.isInsideEnemy(enemyBounds);
    }
    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        int centerX = location.x + size / 2;
        int centerY = location.y + size / 2;
        drawRotatedImage(g2d, sprites.getSprite(), calculateRotationAngle(target), centerX, centerY);
    }

    public void drawRotatedImage(Graphics2D g2d, Image img, double angle, int x, int y) {
        int w = img.getWidth(null);
        int h = img.getHeight(null);
        int drawX = x - w/2;
        int drawY = y - h/2;

        g2d.rotate(angle, x, y);
        g2d.drawImage(img, drawX, drawY, w, h, null);
        g2d.rotate(-angle, x, y);
    }
    public boolean isDead() {
        return health <= 0;
    }
    public boolean isIntersectingLine(Line line) {
        Rectangle enemyBounds = sprites.getBounds(location.x, location.y);
        return line.intersects(enemyBounds);
    }
    public Projectile fireAtPlayer() {
        Point2D.Double directionVector = vectorBetween(location, target);
        Point2D firePosition = sprites.getFireOffset(location, target);
        return new Projectile((int) firePosition.getX(), (int) firePosition.getY(),
                directionVector, false);
    }

    public Point2D.Double vectorBetween(Point from, Point to) {
        double directionX = to.x - from.x;
        double directionY = to.y - from.y;
        double magnitude = Math.sqrt(directionX * directionX + directionY * directionY);
        return new Point2D.Double(directionX / magnitude, directionY / magnitude);
    }
    public Rectangle getBounds() {
        int width = 50;
        int height = 50;
        return new Rectangle(location.x, location.y, width, height);
    }


}
class Mine extends Enemy {
    private Mediator M;
    private Random random = new Random();


    public Mine(int x, int y, int targetX, int targetY, Mediator M) {
        super(x, y, targetX, targetY);
        this.sprites = new MineSprite();
        this.health = 1;
        this.M = M;

    }

    @Override
    public List<ExplosionParticle> explode() {
            System.out.println("BOOM!");
            ArrayList<ExplosionParticle> particles = createExplosionParticles();
            int explosionSize = 150;
            M.handleExplosion(location, explosionSize);
            health = 0;
            Rectangle explosionArea = createExplosionArea();
            return particles;
    }
    @Override
    public void update(Explosion explosion, ArrayList<Line> lines, CopyOnWriteArrayList<Projectile> projectiles, Player player) {
        super.update(explosion, lines, projectiles, player);

        if (isDead()) {
            return;
        }
        if (explosion.shouldExplode(this, lines, projectiles, player)) {
            lastGeneratedParticles = explode();
        }
    }
    @Override
    public List<ExplosionParticle> returnLastGeneratedParticles() {
        return lastGeneratedParticles;
    }

    private Rectangle createExplosionArea() {
        int explosionSize = 200;
        Rectangle explosionArea = new Rectangle(
                location.x - explosionSize / 2,
                location.y - explosionSize / 2,
                explosionSize,
                explosionSize);
        return explosionArea;
    }

    public boolean isIntersectingAnyLine(ArrayList<Line> lines) {
        for (Line line : lines) {
            if (this.isIntersectingLine(line)) {
                return true;
            }
        }
        return false;
    }

    public boolean isCollidingWithAnyProjectile(CopyOnWriteArrayList<Projectile> projectiles) {
        for (Projectile projectile : projectiles) {
                if (this.isCollidingWithProjectile(projectile)) {
                    return true;
                }
        }
        return false;
    }
    @Override
    protected void scheduleFiring() {
        //preventing mines from firing projectiles by overriding it with empty method
    }
    private ArrayList<ExplosionParticle> createExplosionParticles() {
        ArrayList<ExplosionParticle> particles = new ArrayList<>();
        ArrayList<Point> usedVelocities = new ArrayList<>();
        int centerX = location.x + size / 2;
        int centerY = location.y + size / 2;

        for (int i = 0; i < 50; i++) {
            Point newVelocity = createUniqueVelocity(usedVelocities);
            usedVelocities.add(newVelocity);
            particles.add(new ExplosionParticle(new Point(centerX, centerY), newVelocity));
        }

        return particles;
    }

    private Point createUniqueVelocity(ArrayList<Point> usedVelocities) {
        Point newVelocity;
        int counter = 0;
        do {
            if (counter > 500) {
                System.out.println("Failed to find unique velocity");
                return new Point(0, 0);
            }
//            double angle = Math.random() * TAU;
            int velocityX = random.nextInt(6) - 3;
            int velocityY = random.nextInt(6) - 3;
            newVelocity = new Point(velocityX, velocityY);
            counter++;
        } while (usedVelocities.contains(newVelocity) || (newVelocity.x == 0 && newVelocity.y == 0));
        return newVelocity;
    }
}


class Ginker extends Enemy {

    public Ginker(int x, int y, int targetX, int targetY) {
        super(x, y, targetX, targetY);
        this.sprites = new GinkerSprite();
    }
    @Override
    public void moveTowardTarget() {
        if (hasEnemyReachedTarget()) return;
        if (location.x < target.x) location.x += 4;
        if (location.x> target.x) location.x -= 4;
        if (location.y < target.y) location.y += 4;
        if (location.y > target.y) location.y -= 4;
    }
    @Override
    protected void scheduleFiring() {
        //preventing Ginkers from firing projectiles by overriding it with empty method
    }

}
class Behemoth extends Enemy {

    public Behemoth(int x, int y, int targetX, int targetY) {
        super(x, y, targetX, targetY);
        this.sprites = new BehemothSprite();
        this.health = 3;
    }
    @Override
    protected void scheduleFiring() {
        //preventing Behemoths from firing projectiles by overriding it with empty method
    }

}
