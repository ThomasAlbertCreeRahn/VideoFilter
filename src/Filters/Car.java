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
    @Override
    public DImage processImage(DImage img) {
        BufferedImage image = null;
        File f = new File("/Users/hans/VideoFilter/Cars");
        File[] listOfFiles = f.listFiles();
        assert listOfFiles != null;
        File car = listOfFiles[(int)(listOfFiles.length*Math.random())];
        try {
            image = ImageIO.read(car);
        } catch (IOException e) {
        }
        PImage p = new PImage(image);
        return new DImage(p);
    }
}
