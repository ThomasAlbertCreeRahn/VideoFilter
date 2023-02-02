package Filters;

import Interfaces.PixelFilter;
import core.DImage;

public class advancedBWFilter implements PixelFilter{
    private static final double[][] matrix = new double[][]{{-3.0/8.0,1.0/8.0},{3.0/8.0,-1.0/8.0}};
    @Override
    public DImage processImage(DImage img) {
        BlackWhiteFilter b = new BlackWhiteFilter();
        DImage i = b.processImage(img);
        i.setPixels(dither(i.getColorPixelGrid()));
        return i;
    }
    protected static int[][] dither(int[][] data){
        //so we read the grid, and we read the bits inward
        final int h = data.length & -2;
        final int w = data[0].length & -2;
        int[][] out = new int[h][w];
        double[][] m = matrix;
        final double threshold = 127.5;
        //since -2 is all 1's then 1 0, and we just want to make the last bit 0, we do this. shush
        int t = 1; int upper = Math.max(h, w); int x = 0; int y = 0;
        final double s = 1.0/256.0;
        while(t < upper){
            t<<=1;
            if((h & t) != 0){
                for (int j = 0; j + t < w; j+=t){
                    for (int k = 0; k < t; k++) {
                        for (int l = 0; l < t; l++) {
                            if(((data[y + k][j + l]&255)-threshold)*s > m[k][l]){
                                out[y + k][j + l] = -1;
                            }else{
                                out[y + k][j + l] = 255<<24;
                            }
                        }
                    }
                }
                y+=t;
            }
            if((w & t) != 0){
                for (int j = 0; j + t < h; j++) {
                    for (int k = 0; k < t; k++) {
                        for (int l = 0; l < t; l++) {
                            if(((data[j + k][x + l]&255)-threshold)*s > m[k][l]){
                                out[j + k][x + l] = -1;
                            }else{
                                out[j + k][x + l] = 255<<24;
                            }
                        }
                    }
                }
                x+=t;
            }
            printArr(m);
            m = scaleUpMatrix(m);
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
    protected static double[][] scale(int[][] in, double scaleF){
        //just to make my program completely non-understandable and slow
        double[][] out = new double[in.length][];
        for (int i = 0; i < in.length; i++) {
            out[i] = new double[in[i].length];
            for (int j = 0; j < in[i].length; j++) {
                out[i][j] = in[i][j]*scaleF;
            }
        }
        return out;
    }
    protected static int chooseClosest(int in, int[] choices){
        //chooses the closest color
        double minDist = Double.MAX_VALUE;
        int best = 0;
        for (int i = 0; i < choices.length; i++) {
            double d = dist(in, choices[i]);
            if(d < minDist){
                best = choices[i];
                minDist = d;
            }
        }
        return best;
    }
    protected static double dist(int a, int b){
        int totalDist = 0;
        for (int i = 0; i < 3; i++) {
            int t = (((a>>>(i*8))&255) - ((b>>>(i*8))&255));
            totalDist += t*t;
        }
        return Math.sqrt(totalDist);
    }
    protected static double[][] scaleUpMatrix(double[][] in){
        double[][] out = new double[2*in.length][2*in[0].length];
        for (int i = 0; i < out.length; i++) {
            for (int j = 0; j < out[0].length; j++) {
                //this could be more efficient.
                out[i][j] = in[i%in.length][j%in[0].length] + 0.25*matrix[i/in.length][j/in[0].length];
            }
        }
        return out;
    }
    protected static double[][] generateMatrix(int w){
        if(w == matrix.length){
            return matrix;
        }
        int sW = w/matrix.length;
        double[][] lowerMatrix = generateMatrix(sW);
        return scaleUpMatrix(lowerMatrix);
    }
}
