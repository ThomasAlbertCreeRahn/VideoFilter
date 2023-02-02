package Filters;
import Interfaces.PixelFilter;
import core.DImage;

import java.util.Arrays;

public class ConvolutionFilter implements PixelFilter{
    private double[][] kernel = {
            {-1, -1, -1},
            {-1, 8, -1},
            {-1, -1, -1}
    };
    //make a better UI to change the system
    @Override
    public DImage processImage(DImage img) {
        int[][] data = img.getColorPixelGrid();
        int[][] newData = new int[data.length][data[0].length];
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[0].length; j++) {
                applyKernel(i, j, data, newData);
            }
        }
        DImage out = new DImage(img.getWidth(), img.getHeight());
        out.setPixels(newData);
        return out;
    }
    public static DImage processImage(DImage img, double[][] kernel){
        int[][] data = img.getColorPixelGrid();
        int[][] newData = new int[data.length][data[0].length];
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[0].length; j++) {
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
    private static void applyKernel(int x, int y, int[][] in, int[][] out, double[][] kernel){
        //TODO: clean up this BS, try to generalize
        int xO = kernel.length>>1;
        int yO = kernel[0].length>>1;
        double[] color = new double[3];
        for (int i = 0; i < kernel.length; i++) {
            for (int j = 0; j < kernel[0].length; j++) {
                int d = in[(x + i - xO + in.length)%in.length][(y + j - yO + in[0].length)%in[0].length];
                double k = kernel[i][j];
                for (int l = 0; l < color.length; l++) {
                    color[l] += k*(d>>>(8*l));
                }
            }
        }
        for (int i = 0; i < color.length; i++) {
            color[i] = Math.min(255, Math.max(color[i], 0));
            out[x][y]|=(((int)color[i])<<(8*i));
        }
        out[x][y]|=(0xff << 24);
    }
    private static boolean isInBounds(int[][] arr, int x, int y){
        return x >= 0 && x < arr.length && y >= 0 && y < arr[0].length;
    }
}
