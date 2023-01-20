package Filters;

import Interfaces.*;
import core.DImage;

import java.util.Dictionary;

public class NoiseThing implements PixelFilter, ContinuousInteractive{
    private c[][] noise;
    private double[][] weights;
    private double[][] totalNoise;
    @Override
    public void normalFrame(int mouseX, int mouseY, DImage img) {
        for (int i = 0; i < weights.length; i++) {
            for (int j = 0; j < weights[0].length; j++) {
                double f = 100 + (mouseX - j)*(mouseX - j) + (mouseY - i)*(mouseY - i);
                f = 100.0/(f);
                weights[i][j] += f;
                totalNoise[i][j] = f*(Math.random()-0.5);
                weights[i][j] = Math.max(0, weights[i][j] - 0.05);
            }
        }

    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, DImage img) {

    }

    @Override
    public void keyPressed(char key) {

    }

    @Override
    public DImage processImage(DImage img) {
        if(noise == null) {
            noise = new c[img.getHeight()][img.getWidth()];
            weights = new double[img.getHeight()][img.getWidth()];
            totalNoise = new double[img.getHeight()][img.getWidth()];
            for (int i = 0; i < noise.length; i++) {
                for (int j = 0; j < noise[0].length; j++) {
                    noise[i][j] = new c((short)(255*Math.random()), (short)(255*Math.random()), (short)(255*Math.random()));
                }
            }
        }
        DImage d = new DImage(img.getWidth(), img.getHeight());
        d.setPixels(applyNoise(img.getColorPixelGrid()));
        return d;
    }
    private int[][] applyNoise(int[][] in){
        int[][] out = new int[in.length][in[0].length];
        for (int i = 0; i < out.length; i++) {
            for (int j = 0; j < out[0].length; j++) {
                short r = (short)((in[i][j]>>16)&255);
                short g = (short)((in[i][j]>>8)&255);
                short b = (short)(in[i][j]&255);
                r = (short)((Math.random() - 0.5)*weights[i][j]*16 + (r + weights[i][j]*noise[i][j].r)/(1 + weights[i][j]));
                g = (short)((Math.random() - 0.5)*16*weights[i][j] + (g + weights[i][j]*noise[i][j].g)/(1 + weights[i][j]));
                b = (short)((Math.random() - 0.5)*16*weights[i][j] + (b + weights[i][j]*noise[i][j].b)/(1 + weights[i][j]));
                r = (short)(r&255);
                g = (short)(g&255);
                b = (short)(b&255);
                out[i][j]|=(255<<24)|(r<<16)|(g<<8)|b;
            }
        }
        return out;
    }
    private static class c{
        short r, g, b;
        c(short r, short g, short b){
            this.r = r;
            this.g = g;
            this.b = b;
        }
    }
}
