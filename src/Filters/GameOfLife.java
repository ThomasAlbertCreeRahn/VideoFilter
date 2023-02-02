package Filters;

import Interfaces.*;
import core.DImage;

public class GameOfLife implements PixelFilter, Interactive{
    private boolean[][] game;
    private boolean isPaused;
    private DImage image;

    @Override
    public DImage processImage(DImage img) {
        //do dithering
        image = img;
        if(game == null){
            createGame();
            return gameToImage();
        }
        if(!isPaused){
            simulateGame();
        }
        return gameToImage();
    }
    private void createGame(){
        isPaused = false;
        reset();
    }
    private void reset(){
        int[][] data = image.getColorPixelGrid();
        game = new boolean[data.length][data[0].length];
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[0].length; j++) {
                int v = 0;
                for (int k = 0; k < 3; k++) {
                    v+=(data[i][j]>>>(k*8))&255;
                }
                game[i][j] = (v > 382);
            }
        }
    }
    private void simulateGame(){
        boolean[][] out = new boolean[game.length][game[0].length];
        for (int i = 0; i < game.length; i++) {
            for (int j = 0; j < game[0].length; j++) {
                if(game[i][j]) {
                    int t = 0;
                    for (int k = Math.max(0, i - 1); k < Math.min(game.length, i + 2); k++) {
                        for (int l = Math.max(0, j - 1); l < Math.min(game[0].length, j + 2); l++) {
                            t += (game[k][l]) ? 1 : 0;
                        }
                    }
                    out[i][j] = t == 3||t == 4;
                }
            }
        }
        game = out;
    }
    private DImage gameToImage(){
        int[][] data = new int[game.length][game[0].length];
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[0].length; j++) {
                data[i][j] = (game[i][j])? -1 : (255<<24);
            }
        }
        DImage d = new DImage(data[0].length, data.length);
        d.setPixels(data);
        return d;
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, DImage img) {

    }

    @Override
    public void keyPressed(char key) {
        if(key == 'i'){
            isPaused = !isPaused;
        }
        if(key == 'r'){
            reset();
            isPaused = true;
        }
        if(key == 'R'){
            reset();
        }
    }
}
