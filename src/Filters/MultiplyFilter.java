package Filters;

import Interfaces.PixelFilter;
import core.DImage;

import javax.swing.*;

public class MultiplyFilter implements PixelFilter{
    private int getData(int[][] data, double boxW, double boxH, double currR, double currC){
        double[] tempStore = new double[4];
        double maxR = currR + boxH;
        double maxC = currC + boxW;
        for (int i = (int)currR; i < maxR; i++) {
            for (int j = (int)currC; j < maxC; j++) {
                double weight = s(currR, maxR, i)*s(currC, maxC, j);
                for (int k = 0; k < tempStore.length; k++) {
                    double temp = ((data[i%data.length][j%data[0].length]>>>(k<<3))&255);
                    temp*=weight;
                    tempStore[k]+=temp;
                }
            }
        }
        int out = 0;
        double f = 1/(boxW*boxH);
        for (int i = 0; i < tempStore.length; i++) {
            int d = Math.max(0, Math.min((int)((tempStore[i]*f)), 255));
            d<<=(i<<3);
            out|=d;
        }
        return out;
    }
    private double s(double l, double h, double x){
        return Math.max(0,Math.min(0.5*(h - l + 1) - Math.abs(x - 0.5*(h + l - 1)), Math.min((h - l), 1)));
    }

    public DImage processImage(DImage img, double numDivY, double numDivX) {
        //i = row = y
        //j = column = x
        int[][] out = new int[img.getHeight()][img.getWidth()];
        int[][] data = img.getColorPixelGrid();
        for (int i = 0; i < out.length; i++) {
            for (int j = 0; j < out[0].length; j++) {
                out[i][j] = getData(data, numDivX, numDivY, numDivY*i, numDivX*j);
            }
        }
        DImage d = new DImage(out[0].length, out.length);
        d.setPixels(out);
        return d;
    }
    public DImage processImage(DImage img) {
        double divY = Double.parseDouble(JOptionPane.showInputDialog("enter number of Y divisions"));
        double divX = Double.parseDouble(JOptionPane.showInputDialog("enter number of X divisions"));
        return processImage(img, divY, divX);
    }
}
