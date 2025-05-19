package com.example.jvcamera.wallculling;

import java.util.ArrayList;
import java.util.List;

public class BinaryTree {

    static class BinaryNode {
        Face face;
        BinaryNode front;
        BinaryNode back;

        public BinaryNode(Face face) {
            this.face = face;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            for (double[] vertex : face.vertices) {
                sb.append("(").append(vertex[0]).append(", ")
                        .append(vertex[1]).append(", ")
                        .append(vertex[2]).append(") ");
            }
            return sb.toString();
        }
    }

    public static BinaryNode buildBinaryTree(List<Face> faces, List<double[]> pointsToAdd) {
        if (faces.isEmpty()) return null;

        Face splitter = faces.get(0);
        List<Face> frontList = new ArrayList<>();
        List<Face> backList = new ArrayList<>();

        for (int i = 1; i < faces.size(); i++) {
            Face face = faces.get(i);
            SplitResult splitResult = splitter.splitFaces(face);
            if (splitResult.faces.size() == 2) {
                frontList.add(splitResult.faces.get(0));
                backList.add(splitResult.faces.get(1));
                if(!splitResult.intersections.isEmpty()) {
                    pointsToAdd.add(splitResult.intersections.get(0));
                    pointsToAdd.add(splitResult.intersections.get(1));
                }
            } else {
                double[] plane = splitter.getPlaneEquation();
                boolean allPositive = true;
                boolean allNegative = true;

                for (double[] vertex : face.vertices) {
                    double distance = plane[0] * vertex[0] + plane[1] * vertex[1] + plane[2] * vertex[2] + plane[3];
                    if (distance > 0) allNegative = false;
                    if (distance < 0) allPositive = false;
                }

                if (allPositive) frontList.add(face);
                else if (allNegative) backList.add(face);
                else {
                    frontList.add(face);
                }
            }
        }

        BinaryNode node = new BinaryNode(splitter);
        node.front = buildBinaryTree(frontList, pointsToAdd);
        node.back = buildBinaryTree(backList, pointsToAdd);
        return node;
    }

    public static List<BinaryNode> getDrawOrder(BinaryNode node) {
        List<BinaryNode> order = new ArrayList<>();
        collectDrawOrder(node, order);
        return order;
    }

    private static void collectDrawOrder(BinaryNode node, List<BinaryNode> order) {
        if (node == null) return;

        if (node.face.isVisible()) {
            collectDrawOrder(node.back, order);
            order.add(node);  // add current node AFTER back subtree
            collectDrawOrder(node.front, order);
        } else {
            collectDrawOrder(node.front, order);
            order.add(node);  // add current node AFTER front subtree
            collectDrawOrder(node.back, order);
        }
    }
}
