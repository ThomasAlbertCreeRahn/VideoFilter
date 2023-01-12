package Filters;
import Interfaces.PixelFilter;
import core.DImage;

import java.util.Arrays;

public class ConvolutionFilter implements PixelFilter{
    private double[][] kernel = {{1, 2, 1}, {0, 1, 0}, {-1, -2, -1}};

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
    private void applyKernel(int x, int y, int[][] in, int[][] out){
        //TODO: clean up this BS, try to generalize
        int xO = kernel.length/2;
        int yO = kernel[0].length/2;
        int r = 0;
        int g = 0;
        int b = 0;
        for (int i = 0; i < kernel.length; i++) {
            for (int j = 0; j < kernel[0].length; j++) {
                    int d = in[(x + i - xO + in.length)%in.length][(y + j - yO + in[0].length)%in[0].length];
                    double k = kernel[i][j];
                    r+= k*((d)&0xff0000);
                    g+= k*((d)&0xff00);
                    b+= k*(d&0xff);
            }
        }
        r = Math.max(0, Math.min(r, 0xff0000));
        g = Math.max(0, Math.min(g, 0xff00));
        b = Math.max(0, Math.min(b, 255));

        out[x][y] = r + g + b;
        out[x][y]|=(255 << 24);
    }
    private static boolean isInBounds(int[][] arr, int x, int y){
        return x >= 0 && x < arr.length && y >= 0 && y < arr[0].length;
    }
}
