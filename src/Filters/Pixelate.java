package Filters;

import Interfaces.PixelFilter;
import core.DImage;

public class Pixelate implements PixelFilter{
    private static final int r = 2;
    @Override
    public DImage processImage(DImage img) {
        int[][] data = img.getColorPixelGrid();
        int[][] out = new int[data.length][data[0].length];
        int pW = (r<<1)|1;
        int pH = pW;
        return null;
    }
    private int getVal(int[][] a, int c){
        return 0;
    }
}
