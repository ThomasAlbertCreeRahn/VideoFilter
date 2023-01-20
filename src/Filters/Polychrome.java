package Filters;

import Interfaces.PixelFilter;
import core.DImage;

import javax.swing.*;

public class Polychrome implements PixelFilter{
    @Override
    public DImage processImage(DImage img) {
        int numSections = Integer.parseInt(JOptionPane.showInputDialog("please enter an integer"))&255;
        double sectionL = 128.0/numSections;
        double s1 = numSections/3.0;
        double s2 = 1.0/numSections;
        int[][] data = img.getColorPixelGrid();
        int[][] newData = new int[data.length][data[0].length];
        for (int i = 0; i < newData.length; i++) {
            for (int j = 0; j < newData[0].length; j++) {
                int d = data[i][j];
                int r = (d>>>16)&255;
                int g = (d>>>8)&255;
                int b = d&255;
                d = r + g + b;
                d*=s1;
                d>>=8;
                d<<=8;
                d*=s2;
                d+=sectionL;
                newData[i][j]|=(255<<24);
                for (int k = 0; k < 24; k+=8) {
                    newData[i][j]|=(d<<k);
                }
            }
        }
        DImage d = new DImage(img.getWidth(), img.getHeight());
        d.setPixels(newData);
        return d;
    }
}
