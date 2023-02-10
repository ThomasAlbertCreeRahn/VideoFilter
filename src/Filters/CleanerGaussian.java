package Filters;

public class CleanerGaussian extends BasicBlur{
    @Override
    protected double[][] generateMatrix(int in) {;
        final int m = 150;
        final double f = 1.0 / (1.4 * 1.4 * 2);
        System.out.println(f);
        double[][] out = new double[in][in];
        for (int i = 0; i < out.length; i++) {
            for (int j = 0; j < out[0].length; j++) {
                double v = Math.exp(-f * ((i - in>>1) * (i - in>>1) + (j - in>>1) * (j - in>>1)));
                out[i][j] = ((f / Math.PI) * v * m);
            }
        }
        return out;
    }
}
