package Filters;

import Interfaces.PixelFilter;
import core.DImage;

public class WeirdThresholdFilter implements PixelFilter{
    private short pos = 0;

    @Override
    public DImage processImage(DImage img) {
        int[][] data = img.getColorPixelGrid();
        int[][] outData = new int[data.length][data[0].length];
        int currThreshold = 3*(pos & 255);
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[0].length; j++) {
                int f = (((data[i][j]>>16)&255) + ((data[i][j]>>8)&255) + (data[i][j]&255));
                outData[i][j] = (f > currThreshold)?-1:(255<<24);
            }
        }
        pos++;
        System.out.println(pos);
        DImage out = new DImage(img.getWidth(), img.getHeight());
        out.setPixels(outData);
        return out;
    }
}
