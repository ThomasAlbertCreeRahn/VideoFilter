package Filters;

import Interfaces.PixelFilter;
import core.DImage;

import javax.swing.*;
import java.util.HashMap;
import java.util.Locale;


public class removeColor implements PixelFilter{
    private static final HashMap<String, Integer> dict = new HashMap<>();
    static{
        dict.put("red", 0xff0000);
        dict.put("green", 0xff00);
        dict.put("blue", 0xff);
        dict.put("purple", 0xa020f0);
        //etc.
    }
    @Override
    public DImage processImage(DImage img) {
        ColorMatrixFilter f = new ColorMatrixFilter();
        double[][] matrix = getMatrix(getColor(JOptionPane.showInputDialog("Enter the color to remove")));
        return f.processImage(img, matrix);
    }
    private static double[][] getMatrix(int color){
        double d = 1.0/255;
        double[][] out = new double[4][4];
        for (int i = 0; i < out.length; i++) {
            double g = d*((color>>>(i*8))&255);
            out[i][i] = 1 - g;
        }
        return out;
    }
    private static int getColor(String in){
        //allow for rgb(), hsv() and hex code
        //also accept preset colors
        String copy = in.toLowerCase(Locale.ROOT);
        if(dict.containsKey(copy)) return dict.get(copy);
        if(copy.startsWith("rgb(")){
            return readRGB(copy);
        }
        return readHex(copy);
    }
    private static int readHex(String in){
        int out = 0;
        for (int i = 2; i < in.length(); i++) {
            char c = in.charAt(i);
            out<<=4;
            if(c <= '9' && c >= '0'){
                out|=((c - '0'));
            }else{
                out|=((c - 'a' + 10)&15);
            }
        }
        return out|(255<<24);
    }
    private static int readRGB(String in){
        String[] data = in.substring(4, in.length() - 1).split(",\\s*");
        int out = 0;
        for (int i = 0; i < Math.min(3, data.length); i++) {
            int g = Integer.parseInt(data[i]);
            out|=(g<<((2 - i)*8));
        }
        return out|(255<<24);
    }
}
