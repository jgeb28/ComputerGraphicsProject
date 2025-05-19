package com.example.jvcamera.wallculling;

import java.util.ArrayList;

public class SplitResult {

    ArrayList<double[]> intersections;
    ArrayList<Face> faces;

    public SplitResult(ArrayList<double[]> intersections, ArrayList<Face> faces) {
        this.intersections =intersections;
        this.faces = faces;
    }
}
