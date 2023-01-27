package Filters;

import Interfaces.PixelFilter;
import core.DImage;

public class swapColor implements PixelFilter{
    private static int[][] swapMatrix = new int[][]{
            {0, 1, 0, 0},
            {1, 0, 0, 0},
            {0, 0, 1, 0},
            {0, 0, 0, 1}
    };
    @Override
    public DImage processImage(DImage img) {
        ColorMatrixFilter c = new ColorMatrixFilter();
        return c.processImage(img, swapMatrix);
    }
}
