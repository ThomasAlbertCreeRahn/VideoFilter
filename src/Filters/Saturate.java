package Filters;

import Interfaces.PixelFilter;
import core.DImage;

public class Saturate implements PixelFilter{
    @Override
    public DImage processImage(DImage img) {
        int[][] data = img.getColorPixelGrid();
        int[][] out = new int[data.length][data[0].length];
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[0].length; j++) {
                out[i][j] = increaseSaturation(data[i][j]);
            }
        }
        return new DImage(out);
    }
    private static double saturation(int[] color){
        int M = Integer.MIN_VALUE;
        int m = Integer.MAX_VALUE;
        for (int i = 0; i < color.length; i++) {
            M = Math.max(color[i], M);
            m = Math.min(color[i], m);
        }
        return (M != 0)? ((double)(M - m))/M : 0;
    }
    private static double shift(double s){
        return Math.sqrt(1.0 - (1.0 - s)*(1.0 - s));
    }
    private static int increaseSaturation(int in){
        int[] color = new int[3];
        for (int i = 0; i < color.length; i++) {
            color[i] = (in>>>(i * 8))&255;
        }
        double s = shift(saturation(color));
        //so the ratio between r and g and r and b and b and g must be kept the same
        return -1;
    }

}
