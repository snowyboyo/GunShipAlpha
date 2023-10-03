import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.ArrayList;

class MouseState {
   private Point startPoint;
   private ArrayList<Point> dragPoints = new ArrayList<>();
   private Point2D originalDirection;
   private final double THRESHOLD_ANGLE = Math.toRadians(15);
   public void handleMousePress(MouseEvent e){
       startPoint = e.getPoint();
       dragPoints.clear();
       dragPoints.add(startPoint);
       originalDirection = null;
   }
    public void handleMouseDrag(MouseEvent e, Game game) {
        Point currentPoint = e.getPoint();
        dragPoints.add(currentPoint);
        checkinitialDirection();
        if (originalDirection != null) {
            handleMouseDragWithOriginalDirection(e, currentPoint, game);
        }
    }

    private void handleMouseDragWithOriginalDirection(MouseEvent e, Point currentPoint, Game game) {
        Point2D currentDirection = new Point2D.Double(
                currentPoint.x - dragPoints.get(dragPoints.size() - 2).x,
                currentPoint.y - dragPoints.get(dragPoints.size() - 2).y
        );
        double angle = angleBetween2Lines(originalDirection, currentDirection);
        if (angle > THRESHOLD_ANGLE) {
            dragPoints.remove(dragPoints.size() - 1);
        }
    }
    public void handleMouseRelease(MouseEvent e, Game game, boolean isRightButton) {
        if (isRightButton) {
            Point2D direction = new Point2D.Double(e.getX() - startPoint.x, e.getY() - startPoint.y);
            Projectile projectile = new Projectile(startPoint.x, startPoint.y, direction);
            Game.gs.addProjectile(projectile);
        } else {
            addLine(e, game);
        }
    }

    private void addLine(MouseEvent e, Game game) {
        if (startPoint != null) {
            Game.gs.addLine(e, startPoint);
            startPoint = null;
            originalDirection = null;
            game.repaint();
        }
    }

    public void drawLines(Graphics g) {
        for (int i = 0; i < dragPoints.size() - 1; i++) {
            g.drawLine(dragPoints.get(i).x, dragPoints.get(i).y, dragPoints.get(i + 1).x, dragPoints.get(i + 1).y);
        }
    }
    public void checkinitialDirection() {
        if (originalDirection == null) {
            initializeOriginalDirection();
        }
    }

    private void initializeOriginalDirection() {
        if (dragPoints.size() > 1) {
            originalDirection = new Point2D.Double(
                    dragPoints.get(1).x - dragPoints.get(0).x,
                    dragPoints.get(1).y - dragPoints.get(0).y
            );
        }
    }

    public double angleBetweenStartPointAndCurrentPoint(Point currentPoint) {
        if (originalDirection == null) {
            throw new IllegalStateException("Original direction is not set.");
        }

        Point2D currentDirection = new Point2D.Double(startPoint.x - currentPoint.x, startPoint.y - currentPoint.y);
        return angleBetween2Lines(originalDirection, currentDirection);
    }

    public boolean shouldDrawLine(Point currentPoint) {
        return angleBetweenStartPointAndCurrentPoint(currentPoint) < THRESHOLD_ANGLE;
    }
    double angleBetween2Lines(Point2D line1, Point2D line2) {
        double angle1 = Math.atan2(line1.getY(), line1.getX());
        double angle2 = Math.atan2(line2.getY(), line2.getX());
        double result = angle1 - angle2;
        if (result < 0) {
            result += 2 * Math.PI;
        }
        return Math.abs(result);
    }
    public void drawLineIfNeeded(Graphics g, Component component) {
        if (startPoint != null) {
            Point currentPoint = MouseInfo.getPointerInfo().getLocation();
            SwingUtilities.convertPointFromScreen(currentPoint, component);
            initializeOriginalDirection();
            if (shouldDrawLine(currentPoint)) {
                drawFinishedLine(g, currentPoint);
            }
        }
    }

    private void drawFinishedLine(Graphics g, Point currentPoint) {
        g.drawLine(startPoint.x, startPoint.y, currentPoint.x, currentPoint.y);
    }


}
