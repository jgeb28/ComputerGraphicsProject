package com.example.jvcamera;

public class Points {

    public static double[][] oneCube = {{-100, -100, 300}, {-100, -100, 500}, {100, -100, 500}, {100, -100, 300},
    {-100, 100, 300}, {-100, 100, 500}, {100, 100, 500}, {100, 100, 300}};

    public static double[][] intersectingCubes = {
            {-100, -100, 300}, {-100, -100, 500}, {100, -100, 500}, {100, -100, 300},
            {-100, 100, 300}, {-100, 100, 500}, {100, 100, 500}, {100, 100, 300},
            {-150, -50, 300}, {-150, -50, 500}, {50, -50, 500}, {50, -50, 300},
            {-150, 150, 300}, {-150, 150, 500}, {50, 150, 500}, {50, 150, 300}
    };


    public static double[][] fourCubes = {{-100, -100, 300}, {-100, -100, 500}, {100, -100, 500}, {100, -100, 300},
            {-100, 100, 300}, {-100, 100, 500}, {100, 100, 500}, {100, 100, 300},

            {-100, -100, 600}, {-100, -100, 800}, {100, -100, 800}, {100, -100, 600},
            {-100, 100, 600}, {-100, 100, 800}, {100, 100, 800}, {100, 100, 600},

            {200, -100, 600}, {200, -100, 800}, {400, -100, 800}, {400, -100, 600},
            {200, 100, 600}, {200, 100, 800}, {400, 100, 800}, {400, 100, 600},

            {200, -100, 300}, {200, -100, 500}, {400, -100, 500}, {400, -100, 300},
            {200, 100, 300}, {200, 100, 500}, {400, 100, 500}, {400, 100, 300},
    };

    public static double[][] generateCubeOfCubes5x5() {
        double[][] points = new double[125 * 8][3];
        int index = 0;

        for (int z = 0; z < 5; z++) {          // Depth
            for (int y = 0; y < 5; y++) {      // Height
                for (int x = 0; x < 5; x++) {  // Width
                    double x0 = -500 + x * 200;
                    double x1 = x0 + 200;
                    double y0 = -500 + y * 200;
                    double y1 = y0 + 200;
                    double z0 = z * 200;
                    double z1 = z0 + 200;

                    // Cube corners
                    points[index++] = new double[]{x0, y0, z0};
                    points[index++] = new double[]{x0, y0, z1};
                    points[index++] = new double[]{x1, y0, z1};
                    points[index++] = new double[]{x1, y0, z0};
                    points[index++] = new double[]{x0, y1, z0};
                    points[index++] = new double[]{x0, y1, z1};
                    points[index++] = new double[]{x1, y1, z1};
                    points[index++] = new double[]{x1, y1, z0};
                }
            }
        }
        return points;
    }

    public static double[][] generateSpiralPyramids() {
        int numPyramids = 150;
        double angleStep = 15; // degrees between each pyramid
        double radiusGrowth = 20; // radius growth per pyramid
        double baseSize = 100; // size of the pyramid base
        double height = 200; // height of the pyramid

        double[][] pyramids = new double[numPyramids * 4][3];
        int index = 0;

        for (int i = 0; i < numPyramids; i++) {
            double angleDeg = i * angleStep;
            double angleRad = Math.toRadians(angleDeg);
            double radius = i * radiusGrowth / 360 * angleStep;

            double centerX = radius * Math.cos(angleRad);
            double centerY = radius * Math.sin(angleRad);
            double baseHalf = baseSize / 2;

            // Define triangular base points
            double x1 = centerX - baseHalf;
            double y1 = centerY;
            double x2 = centerX + baseHalf;
            double y2 = centerY;
            double x3 = centerX;
            double y3 = centerY - baseHalf;

            double z0 = 0;
            double apexZ = height;

            // 3 base corners
            pyramids[index++] = new double[]{x1, y1, z0};
            pyramids[index++] = new double[]{x2, y2, z0};
            pyramids[index++] = new double[]{x3, y3, z0};

            // Apex
            pyramids[index++] = new double[]{centerX, centerY, apexZ};
        }

        return pyramids;
    }

    public static double[][] generateSpikeFloor() {
        int numPyramids = 30;
        int rows = 6; // 6 rows of pyramids
        int cols = 5; // 5 columns of pyramids

        double baseSize = 100; // size of the pyramid base
        double height = 200; // height of each pyramid

        double[][] pyramids = new double[numPyramids * 4][3]; // 4 points per pyramid (3 base + 1 apex)
        int index = 0;

        // For each row and column in the grid
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                double centerX = col * (baseSize + 50); // Spacing between pyramids on the x-axis
                double centerY = row * (baseSize + 50); // Spacing between pyramids on the y-axis
                double baseHalf = baseSize / 2;

                // Define triangular base points
                double x1 = centerX - baseHalf;
                double y1 = centerY;
                double x2 = centerX + baseHalf;
                double y2 = centerY;
                double x3 = centerX;
                double y3 = centerY - baseHalf;

                double z0 = 0;
                double apexZ = height;

                // 3 base corners
                pyramids[index++] = new double[]{x1, y1, z0};
                pyramids[index++] = new double[]{x2, y2, z0};
                pyramids[index++] = new double[]{x3, y3, z0};

                // Apex
                pyramids[index++] = new double[]{centerX, centerY, apexZ};
            }
        }

        return pyramids;
    }

}
