package GSA;

import java.awt.*;

public interface Mediator {
    void handleExplosion(Point location, int explosionSize);
    void updateTankTargets(Point newTarget);
}
