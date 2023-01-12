package Filters;
import Interfaces.PixelFilter;
import core.DImage;
import processing.core.PImage;

import javax.xml.crypto.Data;

public class invertFilter implements PixelFilter{
    private static final int thing = 0xffffff;
    private static final int thing2 = 0xff000000;
    public DImage processImage(DImage img) {
        int[][] data = img.getColorPixelGrid();
        int[][] newData = new int[data.length][data[0].length];
        for (int i = 0; i < newData.length; i++) {
            for (int j = 0; j < newData[0].length; j++) {
                newData[i][j] = thing^data[i][j];
            }
        }
        DImage d = new DImage(img.getWidth(), img.getHeight());
        d.setPixels(newData);
        return d;
    }
}
