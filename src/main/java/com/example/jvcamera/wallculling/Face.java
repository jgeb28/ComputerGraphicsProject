package com.example.jvcamera.wallculling;

import java.util.ArrayList;
import java.util.List;

public class Face implements Comparable<Face> {
    double[][] vertices; // 4 punkty ściany
    javafx.scene.paint.Color color;

    public Face(double[][] vertices, javafx.scene.paint.Color color) {
        this.vertices = vertices;
        this.color = color;
    }

    public double[] getNormal() {
        double[] A = vertices[0];
        double[] B = vertices[1];
        double[] C = vertices[2];

        double[] AB = {B[0] - A[0], B[1] - A[1], B[2] - A[2]};
        double[] AC = {C[0] - A[0], C[1] - A[1], C[2] - A[2]};

        double nx = AB[1]*AC[2] - AB[2]*AC[1];
        double ny = AB[2]*AC[0] - AB[0]*AC[2];
        double nz = AB[0]*AC[1] - AB[1]*AC[0];

        return new double[]{nx, ny, nz};
    }

    public boolean isVisible() {
        double[] normal = getNormal();
        double[] A = vertices[0];
        double[] toCamera = {-A[0], -A[1], -A[2]};

        double dot = normal[0]*toCamera[0] + normal[1]*toCamera[1] + normal[2]*toCamera[2];
        return dot > 0;
    }

    public double[] getPlaneEquation() {
        double[] normal = getNormal();
        double A = normal[0];
        double B = normal[1];
        double C = normal[2];
        double[] point = vertices[0];

        // D = -(A*x0 + B*y0 + C*z0)
        double D = -(A * point[0] + B * point[1] + C * point[2]);

        return new double[]{A, B, C, D};
    }

    public SplitResult splitFaces(Face other) {
        double[] plane = getPlaneEquation();
        List<double[]> positiveVertices = new ArrayList<>();
        List<double[]> negativeVertices = new ArrayList<>();
        ArrayList<double[]> intersections = new ArrayList<>();

        int numVertices = other.vertices.length;

        for (int i = 0; i < numVertices; i++) {
            double[] current = other.vertices[i];
            double[] next = other.vertices[(i + 1) % numVertices];

            double currentSide = plane[0] * current[0] + plane[1] * current[1] + plane[2] * current[2] + plane[3];
            double nextSide = plane[0] * next[0] + plane[1] * next[1] + plane[2] * next[2] + plane[3];

            if (currentSide >= 0) positiveVertices.add(current);
            if (currentSide <= 0) negativeVertices.add(current);

            if ((currentSide > 0 && nextSide < 0) || (currentSide < 0 && nextSide > 0)) {
                double t = currentSide / (currentSide - nextSide);
                double[] intersection = {
                        current[0] + t * (next[0] - current[0]),
                        current[1] + t * (next[1] - current[1]),
                        current[2] + t * (next[2] - current[2])
                };

                positiveVertices.add(intersection);
                negativeVertices.add(intersection);
                intersections.add(intersection);
            }
        }

        // Create the two new faces
        Face positiveFace = new Face(positiveVertices.toArray(new double[0][0]), other.color);
        Face negativeFace = new Face(negativeVertices.toArray(new double[0][0]), other.color);

        ArrayList<Face> result = new ArrayList<>();
        if (positiveVertices.size() >= 3) result.add(positiveFace);
        if (negativeVertices.size() >= 3) result.add(negativeFace);

        SplitResult fullResult = new SplitResult(intersections, result);

        return fullResult;
    }

    public double[] getCenter() {
        double[] center = new double[3];
        for (double[] vertex : vertices) {
            center[0] += vertex[0];
            center[1] += vertex[1];
            center[2] += vertex[2];
        }
        center[0] /= vertices.length;
        center[1] /= vertices.length;
        center[2] /= vertices.length;
        return center;
    }


    public boolean checkIfIntersect(Face other) {
        double[] plane = getPlaneEquation();
        boolean hasPositive = false;
        boolean hasNegative = false;

        for (double[] vertex : other.vertices) {
            double result = plane[0] * vertex[0] + plane[1] * vertex[1] + plane[2] * vertex[2] + plane[3];

            if (result > 0) {
                hasPositive = true;
            } else if (result < 0) {
                hasNegative = true;
            }

            if (hasPositive && hasNegative) {
                return true;
            }
        }

        return false;
    }


    @Override
    public int compareTo(Face other) {
            double[] plane = getPlaneEquation();
            boolean allPositive = true;
            boolean allNegative = true;

            for (double[] vertex : other.vertices) {
                double result = plane[0] * vertex[0] + plane[1] * vertex[1] + plane[2] * vertex[2] + plane[3];
                if (result > 0) allNegative = false;
                if (result < 0) allPositive = false;
            }

            if (allPositive) return 1;
            if (allNegative) return -1;

            return 0;
    }



    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Face:\n");
        sb.append("Color: ").append(color.toString()).append("\n");
        sb.append("Vertices:\n");
        for (double[] vertex : vertices) {
            sb.append("  (")
                    .append(String.format("%.2f", vertex[0])).append(", ")
                    .append(String.format("%.2f", vertex[1])).append(", ")
                    .append(String.format("%.2f", vertex[2])).append(")\n");
        }
        return sb.toString();
    }
}
