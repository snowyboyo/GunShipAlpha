import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

public class TankExplosion {

    public boolean shouldExplode(Tank tank, ArrayList<Line> lines,
                                 CopyOnWriteArrayList<Projectile> projectiles,
                                 Player player) {
        return tank.isIntersectingAnyLine(lines) ||
                tank.isCollidingWithAnyProjectile(projectiles) ||
                player.isTouchedByEnemy(tank);
    }
}
