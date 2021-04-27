package lab14;

import lab14lib.Generator;

public class AcceleratingSawToothGenerator implements Generator {
    private double period;
    private int state;
    private double ratio;
    private int cnt;
    private int h = 0;
    public AcceleratingSawToothGenerator(double period, double ratio) {
        this.period = period;
        this.state = 0;
        this.cnt = 0;
        this.ratio = ratio;
    }

    public double next() {
        state += 1;
        double res = 2 * (state % period / period) - 1;
       // System.out.println("CNTL:" + cnt);
        if (state == (int)period) {
//            System.out.println("period:" + period);
            period *= ratio;
            state = 0;
        }
        return res;
    }
}