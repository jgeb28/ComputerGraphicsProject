package com.example.jvcamera.wallculling;

import com.example.jvcamera.CameraScene;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

import java.util.ArrayList;
import java.util.List;

import static com.example.jvcamera.wallculling.BinaryTree.*;

public class ColoredCubeScene extends CameraScene {

    BinaryTree.BinaryNode rootNode;
    ArrayList<Face> faces;

    public ColoredCubeScene(double[][] points,double viewPortDistance, double width, double height) {
        super(points,viewPortDistance, width, height);
        faces = new ArrayList<>();

        int numOfCubes = points.length / 8;

        if (points.length % 8 != 0) {
            throw new IllegalArgumentException("This function accepts only cubical shapes");
        }

        for (int j = 0; j < numOfCubes; j++) {

            double[][] cube = new double[8][3];
            System.arraycopy(points, j * 8, cube, 0, 8);

            double[][] bottom = {cube[0], cube[3], cube[2], cube[1]};
            double[][] top = {cube[4], cube[5], cube[6], cube[7]};
            double[][] front = {cube[0], cube[4], cube[7], cube[3]};
            double[][] back = {cube[1], cube[2], cube[6], cube[5]};
            double[][] right = {cube[3], cube[7], cube[6], cube[2]};
            double[][] left = {cube[0], cube[1], cube[5], cube[4]};


            faces.add(new Face(bottom, Color.RED));     // bottom
            faces.add(new Face(top, Color.GREEN));   // top

            faces.add(new Face(front, Color.BLUE));    // front
            faces.add(new Face(back, Color.YELLOW));  // back

            faces.add(new Face(right, Color.ORANGE));  // right
            faces.add(new Face(left, Color.PURPLE));  // left


        }
        ArrayList<double[]> pointsToAdd = new ArrayList<>();
        rootNode = buildBinaryTree(faces, pointsToAdd);
        if(!pointsToAdd.isEmpty()) {
            int len = points.length;
            int n = pointsToAdd.size();

            double[][] newpoints = new double[len + n][3];

            System.arraycopy(points, 0, newpoints, 0, len);

            for (int i = 0; i < n; i++) {
                newpoints[len + n - i - 1] = pointsToAdd.get(i);
            }

            super.points = newpoints;
        }


    }

    @Override
    public Group drawScene() {
        Group root = new Group();

        root.setTranslateX(width / 2);
        root.setTranslateY(height / 2);

        List<BinaryNode> drawOrder = getDrawOrder(rootNode);
        for(BinaryNode node : drawOrder) {
            if (!node.face.isVisible()) continue; // Back Sides Culling

            ArrayList<double[]> clippedVertices = new ArrayList<>();

            for (int i = 0; i < node.face.vertices.length; i++) {
                double[] current = node.face.vertices[i];
                double[] next = node.face.vertices[(i + 1) % node.face.vertices.length];

                boolean currentVisible = current[2] > 1;
                boolean nextVisible = next[2] > 1;

                double[] newStartPoint = current;
                double[] newEndPoint = next;

                if (!currentVisible && !nextVisible) continue;

                if (!currentVisible) newStartPoint = findIntersectionOnPlane(current, next, 1);
                if (!nextVisible) newEndPoint = findIntersectionOnPlane(next, current, 1);

                double[] projStart = getProjection(newStartPoint, viewPortDistance);
                double[] projEnd = getProjection(newEndPoint, viewPortDistance);

                if (projStart != null) clippedVertices.add(projStart);
                if (projEnd != null && !clippedVertices.contains(projEnd)) clippedVertices.add(projEnd);
            }

            clippedVertices = sutherlandHodgmanClip(clippedVertices);

            if (clippedVertices.size() < 3) continue;

            Polygon polygon = new Polygon();
            for (double[] vertex : clippedVertices) {
                polygon.getPoints().addAll(vertex[0], -vertex[1]);
            }

            polygon.setFill(node.face.color);

            root.getChildren().add(polygon);
        }

        return root;
    }

}
