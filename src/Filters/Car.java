package Filters;

import Interfaces.PixelFilter;
import core.DImage;
import processing.core.PImage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Car implements PixelFilter{
    private static DImage[] images;
    private int time = 0;
    private DImage image;
    @Override
    public DImage processImage(DImage img) {
        if(images == null) {
            File f = new File("Cars");
            File[] listOfFiles = f.listFiles();
            assert listOfFiles != null;
            images = new DImage[listOfFiles.length];
            for (int i = 0; i < listOfFiles.length; i++) {
                try {
                    System.out.println(listOfFiles[i].getName());
                    images[i] = new DImage(new PImage(ImageIO.read(listOfFiles[i])));

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            image = images[(int)(Math.random()*images.length)];
        }
        if(time%5 == 0){
            image = images[(int)(Math.random()*images.length)];
        }
        time++;
        return image;
    }
}
