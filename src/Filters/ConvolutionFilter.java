package Filters;
import Interfaces.PixelFilter;
import core.DImage;

import java.util.Arrays;

public class ConvolutionFilter implements PixelFilter{
    private double[][] kernel = {
            {1, 1, 1},
            {1, 1, 1},
            {1, 1, 1}
    };
    //make a better UI to change the system
    @Override
    public DImage processImage(DImage img) {
        return processImage(img, this.kernel);
    }
    public static DImage processImage(DImage img, double[][] kernel){
        int[][] data = img.getColorPixelGrid();
        int[][] newData = new int[data.length - kernel.length&-2][data[0].length - kernel[0].length&-2];
        double total = getTotal(kernel);
        System.out.println(total);
        if(total != 0){
            scale(kernel, 1.0/total);
        }
        for (int i = 0; i < newData.length; i++) {
            for (int j = 0; j < newData[0].length; j++) {
                applyKernel(i, j, data, newData, kernel);
            }
        }
        DImage out = new DImage(img.getWidth(), img.getHeight());
        out.setPixels(newData);
        return out;
    }
    private void applyKernel(int x, int y, int[][] in, int[][] out){
        applyKernel(x, y, in, out, this.kernel);
    }
    private static void scale(double[][] arr, double sf){
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[0].length; j++) {
                arr[i][j]*=sf;
            }
        }
    }
    private static void applyKernel(int x, int y, int[][] in, int[][] out, double[][] kernel){
        //TODO: clean up this BS, try to generalize
        int xO = kernel.length>>1;
        int yO = kernel[0].length>>1;
        double[] color = new double[3];
        for (int i = 0; i < kernel.length; i++) {
            for (int j = 0; j < kernel[0].length; j++) {
                int d = in[(x + i + in.length)%in.length][(y + j + in[0].length)%in[0].length];
                double k = kernel[i][j];
                for (int l = 0; l < color.length; l++) {
                    color[l] += k*((d>>>(8*l))&255);
                }
            }
        }
        for (int i = 0; i < color.length; i++) {
            color[i] = Math.min(255, Math.max(color[i], 0));
            out[x][y]|=(((int)color[i])<<(8*i));
        }
        out[x][y]|=(0xff << 24);
    }
    private static double getTotal(double[][] arr){
        double out = 0;
        for (double[] doubles : arr) { for (double d: doubles) { out += d;  }}
        return out;
    }
    private static boolean isInBounds(int[][] arr, int x, int y){
        return x >= 0 && x < arr.length && y >= 0 && y < arr[0].length;
    }
}
