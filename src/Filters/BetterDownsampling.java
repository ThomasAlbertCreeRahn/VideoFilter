package Filters;

import Interfaces.PixelFilter;
import core.DImage;
import javax.swing.JOptionPane;

public class BetterDownsampling implements PixelFilter{
    @Override
    public DImage processImage(DImage img) {
        int[][] data = img.getColorPixelGrid();
        DImage out = new DImage(img.getWidth()/2, img.getHeight()/2);
        int[][] temp = out.getColorPixelGrid();
        int d1 = Integer.parseInt(JOptionPane.showInputDialog("height of the sampling area"));
        int d2 = Integer.parseInt(JOptionPane.showInputDialog("width of the sampling area"));
        for (int i = 0; i < data.length/d1; i++) {
            for (int j = 0; j < data[0].length/d2; j++) {
                temp[i][j] = downSample(data, i, j, d2, d1);
            }
        }
        out.setPixels(temp);
        return out;
    }
    private int downSample(int[][] in, int r, int c, int w, int h){
        int[] channels = new int[4];
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                for (int k = 0; k < channels.length; k++) {
                    channels[k]+=(in[r*h + i][c*w + j]>>>(k<<3))&(0xff);
                }
            }
        }
        int out = 0;
        for (int i = 0; i < channels.length; i++) {
            channels[i]/=(w*h);
            out+=channels[i]<<(i<<3);
        }
        return out;
    }
}
