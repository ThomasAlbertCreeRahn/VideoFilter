package Filters;

import Interfaces.PixelFilter;
import core.DImage;

import javax.swing.*;

public class noise implements PixelFilter{
    @Override
    public DImage processImage(DImage img) {
        int[][] data = img.getColorPixelGrid();
        int[][] out = new int[data.length][data[0].length];
        double threshold = Double.parseDouble(JOptionPane.showInputDialog("p(random)"));
        for (int i = 0; i < out.length; i++) {
            for (int j = 0; j < out[0].length; j++) {
                out[i][j] = (Math.random() < threshold)?(int)(Math.random()*0xffffff)|(255<<24) : data[i][j];
            }
        }
        DImage d = new DImage(out[0].length, out.length);
        d.setPixels(out);
        return d;
    }
}
