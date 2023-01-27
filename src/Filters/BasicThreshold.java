package Filters;

import Interfaces.PixelFilter;
import core.DImage;

import javax.swing.*;
import java.util.HashMap;
import java.util.Locale;

public class BasicThreshold implements PixelFilter{
    private static final HashMap<String, Integer> dict = new HashMap<>();
    static{
        dict.put("red", 0xff0000);
        dict.put("green", 0xff00);
        dict.put("blue", 0xff);
        dict.put("purple", 0xa020f0);
        //etc.
    }
    protected double[][] dists;
    protected Double threshold;
    protected Integer color;
    @Override
    public DImage processImage(DImage img) {
        initializeVars(img);
        int[][] out = process(img);
        return toDImage(out);
    }
    protected void initializeVars(DImage img){
        color = getColor(JOptionPane.showInputDialog("enter a color"));
        threshold = Double.parseDouble(JOptionPane.showInputDialog("enter the threshold"));
        dists = getDists(img.getColorPixelGrid(), color);
    }
    protected void initializeVars(DImage img, String msg1, String msg2){
        color = getColor(JOptionPane.showInputDialog(msg1));
        threshold = Double.parseDouble(JOptionPane.showInputDialog(msg2));
        dists = getDists(img.getColorPixelGrid(), color);
    }
    protected int[][] process(DImage img){
        int[][] out = new int[img.getHeight()][img.getWidth()];
        double t1 = threshold*threshold;
        for (int i = 0; i < out.length; i++) {
            for (int j = 0; j < out[0].length; j++) {
                out[i][j] = (dists[i][j] > t1)?(255<<24):-1;

            }
        }
        return out;
    }
    protected static DImage toDImage(int[][] data){
        DImage d = new DImage(data[0].length, data.length);
        d.setPixels(data);
        return d;
    }
    protected static int getColor(String in){
        //allow for rgb(), hsv() and hex code
        //also accept preset colors
        String copy = in.toLowerCase(Locale.ROOT);
        if(dict.containsKey(copy)) return dict.get(copy);
        if(copy.startsWith("rgb(")){
            return readRGB(copy);
        }
        return readHex(copy);
    }
    protected double[][] getDists(int[][] data, int color){
        final int[] c = splitRGB(color);
        double[][] out = new double[data.length][data[0].length];
        for (int i = 0; i < out.length; i++) {
            for (int j = 0; j < out[0].length; j++) {
                double total = 0;
                for (int k = 0; k < 3; k++) {
                    int v = (data[i][j]>>>(k*8))&255;
                    total+=(c[k] - v)*(c[k] - v);
                }
                out[i][j] = Math.sqrt(total);
            }
        }
        return out;
    }
    private static int[] splitRGB(int c){
        //outputs B, G, R, A
        int[] out = new int[4];
        for (int i = 0; i < out.length; i++) {
            out[i] = (c>>>(i*8))&255;
            System.out.println(out[i]);
        }
        return out;
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
    //TODO: add HSV... future problem ngl
}
