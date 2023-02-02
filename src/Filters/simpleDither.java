package Filters;

import core.DImage;

import javax.swing.*;

public class simpleDither extends ColorReductionFilter{
    private double u = 10;
    public DImage processImage(DImage img, int numColors){
        point3D[][] data = splitColors(img.getColorPixelGrid());
        point3D[] colors = colorsSelected(data, numColors);
        addRandom(data, u);
        return toDImage(convertToIntArr(colors, data));
    }
    private static void addRandom(point3D[][] data, double maxIncrease){
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[0].length; j++) {
                addRandom(data[i][j], maxIncrease);
            }
        }
    }
    private void addRandom(point3D p){
        addRandom(p, u);
    }
    private static void addRandom(point3D p, double maxIncrease){
        p.x += (2*maxIncrease*(Math.random() - 0.5));
        p.y += (2*maxIncrease*(Math.random() - 0.5));
        p.z += (2*maxIncrease*(Math.random() - 0.5));
    }
    public DImage processImage(DImage img){
        int options = Integer.parseInt(JOptionPane.showInputDialog("Enter the number of colors"));
        return processImage(img, options);
    }
}
