package Filters;

import Interfaces.PixelFilter;
import core.DImage;

public class AddBorder implements PixelFilter{
    private static final int BW = 100;
    private static final int BH  =100;

    @Override
    public DImage processImage(DImage img) {
        int w = img.getWidth() + BW*2;
        int h = img.getHeight() + BH*2;
        DImage out = new DImage(w, h);
        int[][] temp = new int[h][w];
        int[][] stuff = img.getColorPixelGrid();
        for (int i = 0; i < temp.length; i++) {
            for (int j = 0; j < temp[0].length; j++) {
                temp[i][j] = (255<<24);
            }
        }
        for (int i = 0; i < stuff.length; i++) {
            System.arraycopy(stuff[i], 0, temp[i + BH], 100, stuff[0].length);
        }
        out.setPixels(temp);
        return out;
    }
}
