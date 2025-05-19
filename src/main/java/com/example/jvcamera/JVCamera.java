package com.example.jvcamera;

import com.example.jvcamera.wallculling.ColoredCubeScene;
import com.example.jvcamera.wireframe.CubeCameraScene;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;


import java.io.IOException;


public class JVCamera extends Application {
    double rotationValue = 0.01745*2;
    double translationValue = 10;
    double zoomValue = 10;

    @Override
    public void start(Stage stage) throws IOException {
        double viewPort= 300;
        double width = 800;
        double height = 600;
        CameraScene cameraScene = new ColoredCubeScene(Points.intersectingCubes,viewPort, width, height);
        //CameraScene cameraScene = new PyramidCameraScene(Points.generateSpikeFloor(), viewPort, width, height);
        //CameraScene cameraScene = new CubeCameraScene(Points.fourCubes, viewPort, width, height);
        Group root = cameraScene.drawScene();

        Scene scene = new Scene(root, width, height);

        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {

                switch (keyEvent.getCode()) {
                    case A:
                        double[] rightTranslation = {translationValue, 0, 0};
                        cameraScene.translate(rightTranslation);

                        root.getChildren().clear();
                        root.getChildren().addAll(cameraScene.drawScene().getChildren());
                        break;
                    case D:
                        double[] leftTranslation = {-translationValue, 0, 0};
                        cameraScene.translate(leftTranslation);

                        root.getChildren().clear();
                        root.getChildren().addAll(cameraScene.drawScene().getChildren());
                        break;
                    case E:
                        double[] topTranslation = {0, -translationValue, 0};
                        cameraScene.translate(topTranslation);

                        root.getChildren().clear();
                        root.getChildren().addAll(cameraScene.drawScene().getChildren());
                        break;
                    case Q:
                        double[] bottomTranslation = {0, translationValue, 0};
                        cameraScene.translate(bottomTranslation);

                        root.getChildren().clear();
                        root.getChildren().addAll(cameraScene.drawScene().getChildren());
                        break;
                    case W:
                        double[] frontTranslation = {0, 0, -translationValue};
                        cameraScene.translate(frontTranslation);

                        root.getChildren().clear();
                        root.getChildren().addAll(cameraScene.drawScene().getChildren());
                        break;
                    case S:
                        double[] backwardTranslation = {0, 0, translationValue};
                        cameraScene.translate(backwardTranslation);

                        root.getChildren().clear();
                        root.getChildren().addAll(cameraScene.drawScene().getChildren());
                        break;
                    case LEFT:
                        cameraScene.rotateY(rotationValue);

                        root.getChildren().clear();
                        root.getChildren().addAll(cameraScene.drawScene().getChildren());
                        break;
                    case RIGHT:
                        cameraScene.rotateY(-rotationValue);

                        root.getChildren().clear();
                        root.getChildren().addAll(cameraScene.drawScene().getChildren());
                        break;
                    case UP:
                        cameraScene.rotateX(rotationValue);

                        root.getChildren().clear();
                        root.getChildren().addAll(cameraScene.drawScene().getChildren());
                        break;
                    case DOWN:
                        cameraScene.rotateX(-rotationValue);

                        root.getChildren().clear();
                        root.getChildren().addAll(cameraScene.drawScene().getChildren());
                        break;

                    case PAGE_UP:
                        cameraScene.rotateZ(-rotationValue);

                        root.getChildren().clear();
                        root.getChildren().addAll(cameraScene.drawScene().getChildren());
                        break;

                    case PAGE_DOWN:
                        cameraScene.rotateZ(rotationValue);

                        root.getChildren().clear();
                        root.getChildren().addAll(cameraScene.drawScene().getChildren());
                        break;

                    case O:
                        cameraScene.zoomIn(zoomValue);

                        root.getChildren().clear();
                        root.getChildren().addAll(cameraScene.drawScene().getChildren());
                        break;

                    case P:
                        cameraScene.zoomOut(zoomValue);

                        root.getChildren().clear();
                        root.getChildren().addAll(cameraScene.drawScene().getChildren());
                        break;

                }
            }
        });

        stage.setTitle("JVCamera");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}