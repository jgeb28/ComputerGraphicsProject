package com.example.jvcamera.wireframe;

import com.example.jvcamera.CameraScene;
import javafx.scene.Group;
import javafx.scene.shape.Line;

public class PyramidCameraScene extends CameraScene {

    public PyramidCameraScene(double[][] points, double viewPortDistance, double width, double height) {
        super(points, viewPortDistance, width, height);
    }

    @Override
    public Group drawScene() {
        Group root = new Group();

        root.setTranslateX(width/2);
        root.setTranslateY(height/2);

        int numOfPyramids = points.length / 4;

        for(int j=0; j < numOfPyramids; j++) {
            double[][] pyramid = new double[4][3];
            System.arraycopy(points,j*4,pyramid,0,4);

            // draw base
            Line baseLine1 = drawLine(pyramid[0], pyramid[1], viewPortDistance);
            if (baseLine1 != null) root.getChildren().add(baseLine1);

            Line baseLine2 = drawLine(pyramid[1], pyramid[2], viewPortDistance);
            if (baseLine2 != null) root.getChildren().add(baseLine2);

            Line baseLine3 = drawLine(pyramid[2], pyramid[0], viewPortDistance);
            if (baseLine3 != null) root.getChildren().add(baseLine3);

            // draw sides
            for (int i=0; i< 3; i++) {
                Line sideLine = drawLine(pyramid[3], pyramid[i], viewPortDistance);
                if (sideLine != null) root.getChildren().add(sideLine);
            }
        }

        return root;
    }
}
