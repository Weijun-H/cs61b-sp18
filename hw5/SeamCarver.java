import edu.princeton.cs.algs4.Picture;

public class SeamCarver {
    private int _width, _height;
    private Picture _picture;

    public SeamCarver(Picture picture) {
        _height = picture.height();
        _width = picture.width();
        _picture = picture;
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
        double greenXR = _picture.get(xL, y).getGreen();
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
        return null;
    }           // sequence of indices for horizontal seam
    public   int[] findVerticalSeam() {
        return null;
    }              // sequence of indices for vertical seam
    public    void removeHorizontalSeam(int[] seam) {

    }// remove horizontal seam from picture
    public    void removeVerticalSeam(int[] seam) {

    }     // remove vertical seam from picture
}
