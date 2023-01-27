package Filters;


import Interfaces.Interactive;
import core.DImage;

public class InteractiveThreshold extends BasicThreshold implements Interactive {
    private static final double difference = 1;
    public DImage processImage(DImage img){
        if(threshold == null){
            initializeVars(img, "Enter the color", "Enter initial Threshold");
        }
        int[][] out = process(img);
        return toDImage(out);
    }
    @Override
    public void mouseClicked(int mouseX, int mouseY, DImage img) {

    }

    @Override
    public void keyPressed(char key) {
        if (key == '+') {
            threshold+=difference;
        }else if(key == '-'){
            threshold-=difference;
        }
    }
}
