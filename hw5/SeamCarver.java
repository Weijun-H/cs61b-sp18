import edu.princeton.cs.algs4.Picture;

import java.nio.channels.FileLock;
import java.util.*;


public class SeamCarver {
    private class Position {
        private int _x, _y;
        public Position(int x, int y) {
            _x = x;
            _y = y;
        }

        public int get_x() {
            return _x;
        }

        public int get_y() {
            return _y;
        }
    }
    private int _width, _height;
    private Picture _picture;
    private double[][] energyMatric;

    public SeamCarver(Picture picture) {
        _height = picture.height();
        _width = picture.width();
        _picture = picture;
        energyMatric = new double[_width][_height];
        for (int x = 0; x < _width; x++) {
            ArrayList<Double> rowEnergy = new ArrayList<>();
            for (int y = 0; y < _height; y++) {
                energyMatric[x][y] = energy(x, y);
            }
        }
    }

    public Picture picture() {
        return _picture;
    }

    public int width() {
        return _width;
    }
    public int height() {
        return _height;
    }
    public double energy(int x, int y) {
        if (x < 0 || x > _width - 1 || y < 0 || y > _height - 1) {
            throw  new IndexOutOfBoundsException();
        }
        return calcalateEngeryX(x, y) + calcalateEngeryY(x, y);
    }

    private double calcalateEngeryX(int x, int y) {

        int xL, xR;
        xR = (x + 1) % _width;
        xL = x != 0 ? x - 1 : width() - 1;
//        System.out.println("XL: " + xL + ", XR: " + xR);
        double redXL = _picture.get(xL, y).getRed();
        double redXR = _picture.get(xR, y).getRed();
        double greenXL = _picture.get(xL, y).getGreen();
        double greenXR = _picture.get(xR, y).getGreen();
        double blueXL = _picture.get(xL, y).getBlue();
        double blueXR = _picture.get(xR, y).getBlue();
        return (redXL - redXR) * (redXL - redXR) + (greenXL - greenXR) * (greenXL - greenXR)
                + (blueXL - blueXR) * (blueXL - blueXR);
    }

    private double calcalateEngeryY(int x, int y) {
        int yD, yU;
        yU = (y + 1) % _height;
        yD = y != 0 ? y - 1 : height() - 1;
        double redYD = _picture.get(x, yD).getRed();
        double redYU = _picture.get(x, yU).getRed();
        double greenYD = _picture.get(x, yD).getGreen();
        double greenYU = _picture.get(x, yU).getGreen();
        double blueYD = _picture.get(x, yD).getBlue();
        double blueYU = _picture.get(x, yU).getBlue();
        return (redYD - redYU) * (redYD - redYU) + (greenYD - greenYU) * (greenYD - greenYU)
                + (blueYD - blueYU) * (blueYD - blueYU);
    }

    public   int[] findHorizontalSeam() {
        Picture rotatePicture = new Picture(_height, _width);
        for (int i = 0; i < _height; i++) {
            for (int j = 0; j < _width; j++) {
                rotatePicture.set(i, j, this._picture.get(j,i));
            }
        }
        SeamCarver rotateSeam = new SeamCarver(rotatePicture);
        return rotateSeam.findVerticalSeam();
    }

    public int[] findVerticalSeam() {
        // Initialize the accumulated array
        double[][] M = new double[_width][_height];
        int[][] pushFlag = new int[_width][_height];
        int [][] track = new int[_width][_height];
        for (int i = 0; i < _width; i++) {
            for (int j = 0; j < _height; j++) {
                if (j == _height - 1) {
                    M[i][_height - 1] = energyMatric[i][_height - 1];
                } else {
                    M[i][j] = Double.MAX_VALUE;
                }
                track[i][j] = -1;
                pushFlag[i][j] = 0;
            }
        }

        Deque<Position> waitSearch = new LinkedList<>();
        for (int i = 0; i < _width; i++) {
            waitSearch.add(new Position(i, _height - 1));
            while (!waitSearch.isEmpty()){
                Position pos = waitSearch.poll();
                int x = pos.get_x();
                int y = pos.get_y();
                if (y == 0) continue;
                addNextStep(x, y, waitSearch, pushFlag);
                updateM(x, y, M, track);
            }
        }

        List<Integer> route = new ArrayList<>();
        double minDis = Double.MAX_VALUE;
        for (int i = 0; i < _width; i++) {
            if (minDis > M[i][0]) {
                minDis = M[i][0];
                if (!route.isEmpty()) route.remove(0);
                route.add(i);
            }
        }
        int startX = route.get(0);
        for (int i = 0; i < _height - 1; i++) {
            startX = track[startX][i];
            route.add(startX);
        }
//        System.out.println(route.toString());
//        Collections.reverse(route);
        int[] ans = new int[_height];
        for (int i = 0; i < _height; i++) {
            ans[i] = route.get(i);
        }

        return ans;
    }

    private void addNextStep(int x, int y, Deque<Position> waitSearch, int[][] Flags) {
        if (x != _width - 1) {
            if (Flags[x + 1][y - 1] == 0) {
                waitSearch.add(new Position(x + 1, y - 1));
                Flags[x + 1][y - 1] = 1;
            }
        }
        if (Flags[x][y - 1] == 0) {
            waitSearch.add(new Position(x, y - 1));
            Flags[x][y - 1] = 1;
        }

        if (x != 0) {
            if (Flags[x - 1][y - 1] == 0) {
                waitSearch.add(new Position(x - 1, y - 1));
                Flags[x - 1][y - 1] = 1;
            }
        }
    }

    private void updateM(int x, int y, double[][] M, int[][] track) {
//        System.out.println("X: " + x + ", Y: " + y);
        if (x != _width - 1) {
            if (M[x + 1][y - 1] > M[x][y] + energyMatric[x + 1][y - 1]) {
                M[x + 1][y - 1] = M[x][y] + energyMatric[x + 1][y - 1];
                track[x + 1][y - 1] = x;
            }
        }
            if (M[x][y - 1] > M[x][y] + energyMatric[x][y - 1]) {
                M[x][y - 1] = M[x][y] + energyMatric[x][y - 1];
                track[x][y - 1] = x;
            }

        if (x != 0) {
            if (M[x - 1][y - 1] > M[x][y] + energyMatric[x - 1][y - 1]) {
                M[x - 1][y - 1] = M[x][y] + energyMatric[x - 1][y - 1];
                track[x - 1][y - 1] = x;
            }
        }
    }

    public    void removeHorizontalSeam(int[] seam) {
        SeamRemover.removeHorizontalSeam(_picture, findHorizontalSeam());

    }// remove horizontal seam from picture
    public    void removeVerticalSeam(int[] seam) {
        SeamRemover.removeVerticalSeam(_picture, findVerticalSeam());

    }     // remove vertical seam from picture
}
