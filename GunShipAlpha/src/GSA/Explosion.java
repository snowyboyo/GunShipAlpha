package GSA;


import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

public class Explosion {

    public boolean shouldExplode(Mine mine, ArrayList<Line> lines,
                                 CopyOnWriteArrayList<Projectile> projectiles,
                                 Player player) {
        return mine.isIntersectingAnyLine(lines) ||
                mine.isCollidingWithAnyProjectile(projectiles) ||
                player.isTouchedByEnemy(mine);
    }
}
