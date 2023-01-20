package Filters;

import Interfaces.PixelFilter;
import core.DImage;

import java.util.HashMap;

public class cannyEdge implements PixelFilter{
    private static final int[][] SOBEL = {
            {1, 2, 1},
            {0, 0, 0},
            {-1, -2, -1},
    };
    private static final int[][] PREWITT = {
            {1, 1, 1},
            {0, 0, 0},
            {-1, -1, -1}
    };
    private static final int[][] ROBERTS = {
            {1, 0},
            {0, -1}
    };
    private static final HashMap<String, int[][]> kernels = new HashMap<>();
    private static final double threshold = 50;
    static{
        kernels.put("Sobel", SOBEL);
        kernels.put("Prewitt", PREWITT);
        kernels.put("Roberts", ROBERTS);
    }
    @Override
    public DImage processImage(DImage img) {
        short[][] bwGrid = img.getBWPixelGrid();
        int[][] kernel = PREWITT;
        short[][] gaussed = applyGaussian(bwGrid, 2, 1.4);
        vector[][] edges = edgeDetect(gaussed, kernel);
        boolean[][] stuff = thresholding1(edges);
        EdgeStrength[][] temp = threshold2(stuff, edges);
        final int[][] out = finalStuff(temp);
        DImage d = new DImage(out[0].length, out.length);
        d.setPixels(out);
        return d;
    }
    private static EdgeStrength[][] threshold2(boolean[][] stuff, vector[][] edges){
        EdgeStrength[][] strengths = new EdgeStrength[stuff.length][stuff[0].length];
        for (int i = 0; i < strengths.length; i++) {
            for (int j = 0; j < strengths[0].length; j++) {
                strengths[i][j] = (stuff[i][j])? (edges[i + 1][j + 1].mag > threshold)? EdgeStrength.STRONG : EdgeStrength.WEAK : EdgeStrength.NONE;
            }
        }
        for (int i = 0; i < strengths.length; i++) {
            for (int j = 0; j < strengths[0].length; j++) {
                if(strengths[i][j] == EdgeStrength.STRONG){
                    recursiveThing(stuff, strengths, i, j);
                }
            }
        }
        return strengths;
    }
    private static int[][] finalStuff(EdgeStrength[][] strengths){
        final int EDGE = -1;
        final int NOT_EDGE = 255<<24;
        int[][] out = new int[strengths.length][strengths[0].length];
        for (int i = 0; i < out.length; i++) {
            for (int j = 0; j < out[0].length; j++) {
                out[i][j] = (strengths[i][j] == EdgeStrength.STRONG)?EDGE:NOT_EDGE;
            }
        }
        return out;
    }
    private static void recursiveThing(boolean[][] allowed, EdgeStrength[][] strengths, int i, int j){
        for (int k = Math.max(0, i - 1); k < Math.min(allowed.length, i + 2); k++) {
            for (int l = Math.max(0, j - 1); l < Math.min(allowed[0].length, j + 2); l++) {
                if(strengths[k][l] == EdgeStrength.WEAK && allowed[k][l]){
                    strengths[k][l] = EdgeStrength.STRONG;
                    recursiveThing(allowed, strengths, k, l);
                }
            }
        }
    }
    private enum EdgeStrength{
        STRONG,
        WEAK,
        NONE
    }
    private static vector[][] edgeDetect(short[][] data, int[][] kernel){
        //kernel length must equal kernel height
        int h = Math.max(0, data.length - kernel.length + 1);
        int w = Math.max(0, data[0].length - kernel.length + 1);
        vector[][] stuff = new vector[h][w];
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                short Gx = applyX(data, i, j, kernel);
                short Gy = applyY(data, i, j, kernel);
                stuff[i][j] = new vector(Gx, Gy);
            }
        }
        return stuff;
    }
    private static int[][] getColorGrid(vector[][] data){
        int[][] out = new int[data.length][data[0].length];
        double sF = Math.sqrt(0.5);
        for (int i = 0; i < out.length; i++) {
            for (int j = 0; j < out[0].length; j++) {
                int u = (int)Math.max(0, Math.min(255, sF*data[i][j].mag));
                out[i][j]|=(u<<16)|(u<<8)|(u);
            }
        }
        return out;
    }
    private static boolean[][] thresholding1(vector[][] stuff){
        boolean[][] out = new boolean[Math.max(stuff.length - 2, 0)][Math.max(stuff[0].length - 2, 0)];
        HashMap<Short, coordinate> map = new HashMap<>();
        map.put((short)0, new coordinate(0, 1));
        map.put((short)1, new coordinate(-1,1));
        map.put((short)2, new coordinate(1, 0));
        map.put((short)3, new coordinate(1, 1));
        for (int i = 0; i < out.length; i++) {
            for (int j = 0; j < out[0].length; j++) {
                short angle = stuff[i + 1][j + 1].angle;
                coordinate o = map.get(angle);
                int i1 = i + 1; int j1 = j + 1;
                double currMag = stuff[i1][j1].mag;
                out[i][j] = (currMag > stuff[i1 + o.x][j1 + o.y].mag && currMag > stuff[i1 - o.x][j1 - o.y].mag);
            }
        }
        return out;
    }
    private static short applyX(short[][] data, int i, int j, int[][] kernel){
        short total = 0;
        for (int k = i; k < i + kernel.length; k++) {
            for (int l = j; l < j + kernel[0].length; l++) {
                total+=data[k][l]*kernel[k - i][l - j];
            }
        }
        return total;
    }
    private static short applyY(short[][] data, int i, int j, int[][] kernel){
        short total = 0;
        for (int k = i; k < i + kernel[0].length; k++) {
            for (int l = j; l < j + kernel.length; l++) {
                total+=data[k][l]*kernel[l - j][k - i];
            }
        }
        return total;
    }
    private static double[][] gaussianKernel(int r, double stdev){
        final int w = (r<<1)|1;
        final double f = 1.0 / (stdev * stdev * 2);
        double[][] out = new double[w][w];
        for (int i = 0; i < out.length; i++) {
            for (int j = 0; j < out[0].length; j++) {
                out[i][j] = Math.exp(-f * ((i - r) * (i - r) + (j - r) * (j - r)));
            }
        }
        double invTotal = 1/getTotal(out);
        for (int i = 0; i < out.length; i++) {
            for (int j = 0; j < out[0].length; j++) {
                out[i][j]*=invTotal;
            }
        }
        return out;
    }
    private static double getTotal(double[][] data){
        double total = 0;
        for (double[] datum : data) {
            for (double v : datum) {
                total += v;
            }
        }
        return total;
    }
    private static class vector{
        private static final double invPI = 1/Math.PI;
        double mag;
        short angle;
        vector(short x, short y){
            this.mag = Math.sqrt(x*x + y*y);
            double a = Math.atan2(y, x);
            a*=invPI;
            angle = (short)(((int)(a + 0.5))&3);
        }
    }
    private static class coordinate{
        int x, y;
        coordinate(int x, int y){
            this.x = x;
            this.y = y;
        }
    }
    private static short[][] applyGaussian(short[][] data, int r, double stdev){
        double[][] kernel = gaussianKernel(r, stdev);
        short[][] out = new short[Math.max(data.length - (r << 1), 0)][Math.max(data[0].length - (r << 1), 0)];
        for (int i = r; i < data.length - r; i++) {
            for (int j = r; j < data[0].length - r; j++) {
                out[i - r][j - r] = apply(data, kernel, i, j);
            }
        }
        return out;
    }
    private static short apply(short[][] data, double[][] kernel, int i, int j){
        short out = 0;
        for (int k = 0; k < kernel.length; k++) {
            for (int l = 0; l < kernel[0].length; l++) {
                int c = data[i + k - kernel.length/2][j + l - kernel[0].length/2];
                c*=kernel[k][l];
                out+=c;
            }
        }
        return out;
    }
}

