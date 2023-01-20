package Filters;

import Interfaces.PixelFilter;
import core.DImage;

public class Test implements PixelFilter{
    @Override
    public DImage processImage(DImage img) {
        int[][] data = img.getColorPixelGrid();
        int[][] out = new int[data.length/2][data[0].length/2];
        for (int i = 0; i < out.length; i++) {
            for (int j = 0; j < out[0].length; j++) {
                long g = data[2*i][2*j] + data[2*i + 1][2*j + 1] + data[2*i + 1][2*j] + data[2*i][2*j + 1];
                g/=4;
                out[i][j] = (int)g;
            }
        }
        DImage d = new DImage(img.getWidth(), img.getHeight());
        d.setPixels(out);
        return d;
    }
}
