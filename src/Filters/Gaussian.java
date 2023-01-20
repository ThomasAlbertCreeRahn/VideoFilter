package Filters;

import Interfaces.PixelFilter;
import core.DImage;

public class Gaussian implements PixelFilter{

    @Override
    public DImage processImage(DImage img) {
        int[][] newData = blur(img.getColorPixelGrid(), 2, 1);
        DImage d = new DImage(img.getWidth(), img.getHeight());
        d.setPixels(newData);
        return d;
    }
    public static int[][] blur(int[][] in, int r, int stdev){
        int[][] kernel = kernel(r, stdev);
        double invTotal = 1.0/getTotal(kernel);
        int[][] out = new int[Math.max(in.length - (r << 1), 0)][Math.max(in[0].length - (r << 1), 0)];
        for (int i = r; i < in.length - r; i++) {
            for (int j = r; j < in[0].length - r; j++) {
                out[i - r][j - r] = apply(in, kernel, i, j, invTotal);
            }
        }
        return out;
    }
    private static int apply(int[][] data, int[][] kernel, int i, int j, double f){
        int out = 0;
        for (int n = 0; n < 3; n++) {
            int t = 0;
            for (int k = 0; k < kernel.length; k++) {
                for (int l = 0; l < kernel[0].length; l++) {
                    int c = (data[i + k - kernel.length/2][j + l - kernel[0].length/2]>>>(8*n))&255;
                    c*=kernel[k][l];
                    t+=c;
                }
            }
            t*=f;
            t=Math.max(0, Math.min(255, t));
            out|=(t<<(8*n));
        }
        out|=(255<<24);
        return out;
    }
    private static void printArr(int[][] arr){
        for (int[] ints : arr) {
            System.out.print("[");
            for (int j = 0; j < ints.length; j++) {
                System.out.print(ints[j] + gbg(ints.length, j));
            }
            System.out.println();
        }
    }
    private static String gbg(int w, int j){
        return (j < w - 1)? ", ": "]";
    }
    private static int getTotal(int[][] in){
        int total = 0;
        for(int[] i: in){
            for(int j: i){
                total+=j;
            }
        }
        return total;
    }
    private static int[][] kernel(int r, int stdev) {
        final int w = (r << 1) | 1;
        final int m = 150;
        final double f = 1.0 / (stdev * stdev * 2);
        System.out.println(f);
        int[][] out = new int[w][w];
        for (int i = 0; i < out.length; i++) {
            for (int j = 0; j < out[0].length; j++) {
                double v = Math.exp(-f * ((i - r) * (i - r) + (j - r) * (j - r)));
                out[i][j] = (int) ((f / Math.PI) * v * m);
            }
        }
        return out;
    }
}
