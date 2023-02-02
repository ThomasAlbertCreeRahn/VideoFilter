package Filters;

import Interfaces.PixelFilter;
import core.DImage;

public class LaplacianGaussian implements PixelFilter{
    public DImage processImage(DImage img, int w, double stdev) {
        double[][] garbage = generateKernel(w, stdev);
        return ConvolutionFilter.processImage(img, garbage);
    }
    private double[][] generateKernel(int w, double stdev){
        final double u = stdev*stdev;
        final double c = -1.0/(Math.PI*u*u);
        final double c1 = -1.0/(2.0*u);
        int size = (w<<1);
        double[][] out = new double[size | 1][size | 1];
        for (int i = 0; i <= w; i++) {
            for (int j = i; j <= w; j++) {
                int v2 = (i - w)*(i - w) + (j - w)*(j - w);
                double temp = (v2*c1);
                double val = c*(1.0 + temp)*Math.pow(Math.E, temp);
                out[size - i][size - j] = val;
                out[i][j] = val;
                out[j][i] = val;
                out[size - j][size - i] = val;
                out[size - i][j] = val;
                out[size - j][i] = val;
                out[j][size - i] = val;
                out[i][size - j] = val;
                System.out.println(val);
            }
        }
        return out;
    }
    private static void printArr(double[][] data){
        for (int i = 0; i < data.length; i++) {
            printArr(data[i]);
        }
    }
    private static void printArr(double[] data){
        System.out.print("{" + data[0]);
        for (int i = 1; i < data.length; i++) {
            System.out.print(", " + data[i]);
        }
        System.out.println("}");
    }
    private double[][] normalize(double[][] in){
        double total = 0;
        for (double[] doubles : in) {
            for (double aDouble : doubles) {
                total += aDouble;
            }
        }
        if(total == 0){
            return in;
        }
        total = 1.0/total;
        for (int i = 0; i < in.length; i++) {
            for (int j = 0; j < in[i].length; j++) {
                in[i][j]*=total;
            }
        }
        return in;
    }
    public DImage processImage(DImage img) {
        return processImage(img, 4, 1.4);
    }
}
