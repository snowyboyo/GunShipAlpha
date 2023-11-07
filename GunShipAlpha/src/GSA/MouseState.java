package GSA;

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
    private int angleExceedCount = 0;
    private final int maxAngleExceed = 15;
    private final int minDistance = 4;

    private boolean isFarEnough(Point point1, Point point2) {
        return point1.distance(point2) >= minDistance;
    }

    public void handleMousePress(MouseEvent e) {
        startPoint = e.getPoint();
        dragPoints.clear();
        dragPoints.add(startPoint);
        originalDirection = null;
    }

    public void handleMouseDrag(MouseEvent e) {
        Point currentPoint = e.getPoint();
        if (originalDirection == null) {
            initializeOriginalDirection(currentPoint);
        } else {
            if (isFarEnough(currentPoint, dragPoints.get(dragPoints.size() - 1))) {
                handleMouseDragWithOriginalDirection(currentPoint);
            }
        }
    }

    private void handleMouseDragWithOriginalDirection(Point currentPoint) {
        Point lastPoint = dragPoints.get(dragPoints.size() - 1);
        Point2D currentDirection = new Point2D.Double(
                currentPoint.x - lastPoint.x,
                currentPoint.y - lastPoint.y
        );
        double angle = angleBetween2Lines(originalDirection, currentDirection);
        if (Math.abs(angle) < THRESHOLD_ANGLE) {
            dragPoints.add(currentPoint);
        } else {
            angleExceedCount++;
            if (angleExceedCount >= maxAngleExceed) {
                addLine(currentPoint);
                angleExceedCount = 0;
            }
        }
    }

    public void handleMouseRelease(MouseEvent e, Game game, boolean isRightButton) {
        if (isRightButton) {
            double dx = e.getX() - game.player.location.x;
            double dy = e.getY() - game.player.location.y;
            int halfPlayerSize = 25;
            Point2D direction = new Point2D.Double(dx, dy);
            game.addPlayerProjectile(direction);
        } else {
            if (!dragPoints.isEmpty()) {
                Point endPoint = dragPoints.get(dragPoints.size() - 1);
                addLine(endPoint);
            }
        }
    }

    private void addLine(Point endPoint) {
        if (startPoint != null && !dragPoints.isEmpty()) {
            Game.gs.addLine(startPoint, endPoint);
            dragPoints.clear();
            startPoint = null;
            originalDirection = null;
        }
    }

    private void initializeOriginalDirection(Point currentPoint) {
        if (startPoint != null) {
            originalDirection = new Point2D.Double(
                    currentPoint.x - startPoint.x,
                    currentPoint.y - startPoint.y
            );
            dragPoints.add(currentPoint);
        }
    }


    public double angleBetweenStartPointAndCurrentPoint(Point currentPoint) {
        initializeOriginalDirection(currentPoint);
        Point2D currentDirection = new Point2D.Double(startPoint.x - currentPoint.x, startPoint.y - currentPoint.y);
        return angleBetween2Lines(originalDirection, currentDirection);
    }

    public boolean shouldDrawLine(Point currentPoint) {
        return angleBetweenStartPointAndCurrentPoint(currentPoint) < THRESHOLD_ANGLE;
    }

    double angleBetween2Lines(Point2D line1, Point2D line2) {
        if (line1 == null || line2 == null) {
            return Double.NaN;
        }
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
            initializeOriginalDirection(currentPoint);
            if (shouldDrawLine(currentPoint)) {
                drawFinishedLine(g, currentPoint);
            }
        }
    }

    private void drawFinishedLine(Graphics g, Point currentPoint) {
        g.drawLine(startPoint.x, startPoint.y, currentPoint.x, currentPoint.y);
    }


}
