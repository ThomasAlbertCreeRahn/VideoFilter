package Filters;

import Interfaces.PixelFilter;
import core.DImage;

public class ColorMatrixFilter implements PixelFilter{
    /**
     * matrices are simple, we multiply by a 4 high color matrix that goes red, green, blue, alpha
     */
    private static final int[][] basicMatrix = new int[][]{
            {1, 0, 0, 0}, // red
            {0, 1, 0, 0}, // green
            {0, 0, 1, 0}, // blue
            {0, 0, 0, 1}  // alpha
    };
    public DImage processImage(DImage img, int[][] matrix){
        int[][] data = img.getColorPixelGrid();
        int[][] out = new int[img.getHeight()][img.getWidth()];
        for (int i = 0; i < out.length; i++) {
            for (int j = 0; j < out[0].length; j++) {
                out[i][j] = getVal(data[i][j], matrix);
            }
        }
        return toDImage(out);
    }
    public DImage processImage(DImage img, double[][] matrix) {
        int[][] data = img.getColorPixelGrid();
        int[][] out = new int[img.getHeight()][img.getWidth()];
        for (int i = 0; i < out.length; i++) {
            for (int j = 0; j < out[0].length; j++) {
                out[i][j] = getVal(data[i][j], matrix);
            }
        }
        return toDImage(out);
    }
    private static DImage toDImage(int[][] in){
        DImage d = new DImage(in[0].length, in.length);
        d.setPixels(in);
        return d;
    }
    private static int getVal(int in, double[][] matrix){
        int out = 0;
        for (int i = 0; i < Math.min(4, matrix[0].length); i++) {
            double u = 0;
            for (int j = 0; j < Math.min(4, matrix.length); j++) {
                int v = (in>>>(j*8))&255;
                u += v*matrix[j][i];
            }
            u = Math.max(0, Math.min(u, 255));
            int o = (int)u;
            out|=(o << (i*8));
        }
        if(matrix.length < 4){
            out|=(255<<24);
        }
        return out;
    }
    private static int getVal(int in, int[][] matrix){
        int out = 0;
        for (int i = 0; i < Math.min(4, matrix[0].length); i++) {
            int u = 0;
            for (int j = 0; j < Math.min(4, matrix.length); j++) {
                int v = (in>>>(j*8))&255;
                u += v*matrix[j][i];
            }
            u = Math.max(0, Math.min(u, 255));
            out|=(u << (i*8));
        }
        if(matrix.length < 4){
            out|=(255<<24);
        }
        return out;
    }
    public DImage processImage(DImage img){
        return processImage(img, basicMatrix);
    }
}
