package com.example.jvcamera;

import javafx.scene.Group;
import javafx.scene.shape.Line;

import java.util.ArrayList;

public abstract class CameraScene {

    protected double[][] points;
    protected double viewPortDistance;
    protected double width;
    protected double height;

    public CameraScene(double[][] points, double viewPortDistance, double width, double height) {
        this.points = points;
        this.viewPortDistance = viewPortDistance;
        this.width = width;
        this.height = height;
    }

    public Line drawLine(double[] startPoint, double[] endPoint, double viewPortDistance) {
        boolean startPointVisible = startPoint[2] > 1;
        boolean endPointVisible = endPoint[2] > 1;

        if (!startPointVisible && !endPointVisible) return null;

        double[] newStartPoint = startPoint;
        double[] newEndPoint = endPoint;

        if (!startPointVisible) newStartPoint = findIntersectionOnPlane(startPoint, endPoint, 1);
        if (!endPointVisible) newEndPoint = findIntersectionOnPlane(endPoint, startPoint, 1);

        double[] proj1 = getProjection(newStartPoint, viewPortDistance);
        double[] proj2 = getProjection(newEndPoint, viewPortDistance);

        if (proj1 == null || proj2 == null) return null;

        Line line = new Line();

        line.setStartX(proj1[0]);
        line.setStartY(-proj1[1]);
        line.setEndX(proj2[0]);
        line.setEndY(-proj2[1]);

        return line;
    }


    public void translate(double[] values) {
        for(double[] point : points) {
            point[0] += values[0];
            point[1] += values[1];
            point[2] += values[2];
        }
    }

    public void rotateX(double value) {
        for(double[] point : points) {
            double pointY = point[1];
            double pointZ = point[2];

            point[1] = Math.cos(value) * pointY - Math.sin(value) * pointZ;
            point[2] = Math.sin(value) * pointY + Math.cos(value) * pointZ;
        }
    }

    public void rotateY(double value) {
        for(double[] point : points) {
            double pointX = point[0];
            double pointZ = point[2];
            point[0] = Math.cos(value) * pointX + Math.sin(value) * pointZ;
            point[2] =  - Math.sin(value) * pointX + Math.cos(value) * pointZ;
        }
    }

    public void rotateZ(double value) {
        for(double[] point : points) {
            double pointX = point[0];
            double pointY = point[1];
            point[0] = Math.cos(value) * pointX - Math.sin(value) * pointY;
            point[1] = Math.sin(value) * pointX + Math.cos(value) * pointY;
        }
    }

    public void zoomIn(double value) {
        if( viewPortDistance < 600)
            viewPortDistance += value;
    }

    public void zoomOut(double value) {
        if( viewPortDistance > 300)
            viewPortDistance -= value;
    }

    public static double[] findIntersectionOnPlane(double[] startPoint, double[] endPoint, double frontClippingPlaneZ) {
        double t = (frontClippingPlaneZ - startPoint[2]) / (endPoint[2] - startPoint[2]);

        double[] intersection = new double[3];
        intersection[0] = startPoint[0] + t * (endPoint[0] - startPoint[0]);
        intersection[1] = startPoint[1] + t * (endPoint[1] - startPoint[1]);
        intersection[2] = frontClippingPlaneZ;

        return intersection;
    }

    public static double[] getProjection(double[] point, double viewPortDistance) {
        double[] projection = new double[2];

        double d = point[2];
        if (point[2] <= 0) {
            return null;
        }

        projection[0] = (point[0] * viewPortDistance)/d;
        projection[1] = (point[1] * viewPortDistance)/d;

        return  projection;
    }

    public abstract Group drawScene();

    public ArrayList<double[]> sutherlandHodgmanClip(ArrayList<double[]> vertices) {
        ArrayList<double[]> clippedVertices = new ArrayList<>(vertices);

        double[][] boundaries = {
                {-width / 2, 0}, {width / 2, 0},   // Left, Right
                {0, -height / 2}, {0, height / 2}  // Bottom, Top
        };

        for (double[] boundary : boundaries) {
            clippedVertices = clipAgainstBoundary(clippedVertices, boundary);
        }

        return clippedVertices;
    }

    private ArrayList<double[]> clipAgainstBoundary(ArrayList<double[]> vertices, double[] boundary) {
        ArrayList<double[]> result = new ArrayList<>();

        for (int i = 0; i < vertices.size(); i++) {
            double[] current = vertices.get(i);
            double[] next = vertices.get((i + 1) % vertices.size());

            boolean currentInside = isInside(current, boundary);
            boolean nextInside = isInside(next, boundary);

            if (currentInside && nextInside) {
                result.add(next);
            } else if (currentInside && !nextInside) {
                result.add(intersect(current, next, boundary));
            } else if (!currentInside && nextInside) {
                result.add(intersect(current, next, boundary));
                result.add(next);
            }
        }

        return result;
    }

    private boolean isInside(double[] point, double[] boundary) {
        if (boundary[0] == 0) {
            // Horizontal boundary
            return (boundary[1] < 0) ? point[1] > boundary[1] : point[1] < boundary[1];
        } else {
            // Vertical boundary
            return (boundary[0] < 0) ? point[0] > boundary[0] : point[0] < boundary[0];
        }
    }

    private double[] intersect(double[] p1, double[] p2, double[] boundary) {
        double x1 = p1[0], y1 = p1[1];
        double x2 = p2[0], y2 = p2[1];

        if (boundary[0] == 0) {
            // Horizontal boundary
            double y = boundary[1];
            double t = (y - y1) / (y2 - y1);
            double x = x1 + t * (x2 - x1);
            return new double[]{x, y};
        } else {
            // Vertical boundary
            double x = boundary[0];
            double t = (x - x1) / (x2 - x1);
            double y = y1 + t * (y2 - y1);
            return new double[]{x, y};
        }
    }


}
