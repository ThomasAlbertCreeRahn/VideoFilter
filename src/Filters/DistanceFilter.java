package Filters;

import core.DImage;

import javax.swing.*;

public class DistanceFilter extends BasicThreshold{
    public DImage processImage(DImage img){
        if(dists == null){
            initializeVars(img);
        }
        double f = Math.sqrt(1.0/3);
        int[][] out = new int[dists.length][dists[0].length];
        for (int i = 0; i < dists.length; i++) {
            for (int j = 0; j < dists[0].length; j++) {
                int g = ((int)(dists[i][j]*f));
                g&=255;
                out[i][j]|=(255<<24)|(g<<16)|(g<<8)|g;
            }
        }
        return toDImage(out);
    }
}
