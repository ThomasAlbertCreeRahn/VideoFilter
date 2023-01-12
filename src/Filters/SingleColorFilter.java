package Filters;
import Interfaces.PixelFilter;
import core.DImage;
import processing.core.PImage;
import javax.swing.JOptionPane;


public class SingleColorFilter implements PixelFilter{
    private static final int rF = 255 << 16;
    private static final int bF = 255;
    private static final int gF = 255 << 8;
    @Override
    public DImage processImage(DImage img) {
        PImage internal = new PImage(img.getWidth(), img.getHeight());
        String color = JOptionPane.showInputDialog("what color do you want to filter on?");
        int[][] pixels = img.getColorPixelGrid();
        int filter = 0;
        switch(color){
            case("R"): filter = rF; break;
            case("G"): filter = gF; break;
            case("B"): filter = bF; break;
        }
        for (int i = 0; i < pixels.length; i++) {
            for (int j = 0; j < pixels[0].length; j++) {
                internal.set(j, i, (pixels[i][j] & filter));
            }
        }
        return new DImage(internal);
    }
}
