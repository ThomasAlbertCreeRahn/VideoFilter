package Filters;

import Interfaces.PixelFilter;
import core.DImage;

import javax.swing.JOptionPane;

public class EvenBetterDownsampling implements PixelFilter{
    public DImage processImage(DImage img, int w, int h) {
        //i = row = y
        //j = column = x
        int[][] out = new int[h][w];
        int[][] data = img.getColorPixelGrid();
        double bH = data.length/(double)out.length;
        double bW = data[0].length/(double)out[0].length;
        for (int i = 0; i < out.length; i++) {
            for (int j = 0; j < out[0].length; j++) {
                out[i][j] = getData(data, bW, bH, bH*i, bW*j);
            }
        }
        DImage d = new DImage(out[0].length, out.length);
        d.setPixels(out);
        return d;
    }
    public DImage processImage(DImage img) {
        int newW = Integer.parseInt(JOptionPane.showInputDialog("enter new image w"));
        int newH = Integer.parseInt(JOptionPane.showInputDialog("enter new image H"));
        return processImage(img, newW, newH);
    }
    private int getData(int[][] data, double boxW, double boxH, double currR, double currC){
        double[] tempStore = new double[4];
        double maxR = currR + boxH;
        double maxC = currC + boxW;
        for (int i = (int)currR; i < maxR && i < data.length; i++) {
            for (int j = (int)currC; j < maxC && j < data[0].length; j++) {
                double weight = s(currR, maxR, i)*s(currC, maxC, j);
                for (int k = 0; k < tempStore.length; k++) {
                    double temp = ((data[i][j]>>>(k<<3))&255);
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
}
