package Filters;
import Interfaces.PixelFilter;
import core.DImage;
import processing.core.PImage;

public class BlackWhiteFilter implements PixelFilter{
    private static final int filter = 255;

    @Override
    public DImage processImage(DImage img) {
        int[][] data = img.getColorPixelGrid();
        PImage out = new PImage(img.getWidth(), img.getHeight());
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[0].length; j++) {
                int c = data[i][j];
                int b = c & filter;
                int g = (c>>8) & filter;
                int r = (c>>16) & filter;
                int v = (r + g + b)/3;
                v = (v << 16) + (v << 8) + v;
                out.set(j, i, v);
            }
        }
        return new DImage(out);
    }
}
