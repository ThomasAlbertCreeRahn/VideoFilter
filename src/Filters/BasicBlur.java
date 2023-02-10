package Filters;

import Interfaces.PixelFilter;
import core.DImage;

import javax.swing.*;

public class BasicBlur implements PixelFilter{

    @Override
    public DImage processImage(DImage img) {
        int w = Integer.parseInt(JOptionPane.showInputDialog("enter width of kernel (odd please)"))|1;
        ConvolutionFilter f = new ConvolutionFilter();
        return f.processImage(img, this.generateMatrix(w));
    }
    protected double[][] generateMatrix(int in){
        double[][] out = new double[in][in];
        for (int i = 0; i < out.length; i++) {
            for (int j = 0; j < out[0].length; j++) {
                out[i][j] = 1;
            }
        }
        return out;
    }
}
