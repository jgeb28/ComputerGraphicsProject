package com.example.jvcamera.wireframe;

import com.example.jvcamera.CameraScene;
import javafx.scene.Group;
import javafx.scene.shape.Line;

public class CubeCameraScene extends CameraScene {

    public CubeCameraScene(double[][] points, double viewPortDistance, double width, double height) {
        super(points, viewPortDistance, width, height);
    }

    @Override
    public Group drawScene() {
        Group root = new Group();

        root.setTranslateX(width/2);
        root.setTranslateY(height/2);

        int numOfCubes = points.length / 8;

        for(int j=0; j < numOfCubes; j++) {

            double[][] cube = new double[8][3];
            System.arraycopy(points,j*8,cube,0,8);
            // Draw Bottom
            for (int i = 0; i < 3; i++) {
                Line line = drawLine(cube[i], cube[i + 1], viewPortDistance);
                if (line != null) root.getChildren().add(line);


            }

            Line line2 = drawLine(cube[0], cube[3], viewPortDistance);
            if (line2 != null) root.getChildren().add(line2);


            Line line3 = drawLine(cube[4], cube[7], viewPortDistance);
            if (line3 != null) root.getChildren().add(line3);

            // Draw Top
            for (int i = 4; i < 7; i++) {
                Line line = drawLine(cube[i], cube[i + 1], viewPortDistance);
                if (line != null) root.getChildren().add(line);

            }
            //Draw horizontal lines
            for (int i = 0; i < 4; i++) {
                Line line = drawLine(cube[i], cube[i + 4], viewPortDistance);
                if (line != null) root.getChildren().add(line);
            }
        }

        return root;
    }
}
