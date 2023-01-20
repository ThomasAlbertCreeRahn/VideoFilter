package Filters;

import Interfaces.*;
import core.DImage;

public class Test2 implements PixelFilter, ContinuousInteractive{
    private boolean[][] isRed;
    public void normalFrame(int mouseX, int mouseY, DImage img) {
        if(0 <= mouseX && mouseX < isRed[0].length && mouseY < isRed.length && 0 < mouseY){
            isRed[mouseY][mouseX] = true;
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
        if(isRed == null) {
            isRed = new boolean[img.getHeight()][img.getWidth()];
        }
        int[][] out = addRed(img.getColorPixelGrid());
        DImage d = new DImage(img.getWidth(), img.getHeight());
        d.setPixels(out);
        return d;
    }
    private int[][] addRed(int[][] img){
        int[][] out = new int[img.length][img[0].length];
        for (int i = 0; i < img.length; i++) {
            for (int j = 0; j < img[0].length; j++) {
                out[i][j] = (isRed[i][j])? 0xff0000|(255<<24) : img[i][j];
            }
        }
        return out;
    }
}
