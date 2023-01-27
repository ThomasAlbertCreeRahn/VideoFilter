package Filters;

import Interfaces.PixelFilter;
import core.DImage;

import javax.swing.*;
//not my best work, but it'll do I guess

public class ColorReductionFilter implements PixelFilter{
    @Override
    public DImage processImage(DImage img) {
        int numColors = Integer.parseInt(JOptionPane.showInputDialog("enter the number of colors"));
        return process(img, numColors);
    }
    private static DImage process(DImage d, int numColors){
        int[][] data = reduceColors(d.getColorPixelGrid(), numColors);
        DImage out = new DImage(d.getWidth(), d.getHeight());
        out.setPixels(data);
        return out;
    }
    private static point3D[] colorsSelected(point3D[][] data, point3D[] initial){
        point3D[] postStep = downwardSpiral(data, initial);
        if(!equals(postStep, initial)){
            System.out.println("iteration completed");
            return colorsSelected(data, postStep);
        }
        return initial;
    }
    private static point3D[] colorsSelected(point3D[][] data, int numColors){
        point3D[] randomPts = new point3D[numColors];
        for (int i = 0; i < randomPts.length; i++) {
            randomPts[i] = new point3D((int)(Math.random()*256), (int)(Math.random()*256), (int)(Math.random()*256));
        }
        return colorsSelected(data, randomPts);
    }
    private static boolean equals(point3D[] a, point3D[] b){
        if(a.length == b.length) {
            for (int i = 0; i < a.length; i++) {
                if(a[i].x != b[i].x || a[i].y != b[i].y || a[i].z != b[i].z){
                    return false;
                }
            }
            return true;
        }
        return false;
    }
    private static point3D[] downwardSpiral(point3D[][] data, point3D[] preStep){
        clusterAvg[] temp = new clusterAvg[preStep.length];
        for (int i = 0; i < temp.length; i++) temp[i] = new clusterAvg();
        for (point3D[] datum : data) {
            for (point3D d: datum){
                int t= chooseClosest(d, preStep);
                temp[t].add(d);
            }
        }
        point3D[] out = new point3D[temp.length];
        for (int i = 0; i < out.length; i++) {
            out[i] = temp[i].getVal();
        }
        return out;
    }
    private static int[][] reduceColors(int[][] data, int numColors){
        point3D[][] stuff = new point3D[data.length][data[0].length];
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[0].length; j++) {
                int r = ((data[i][j]>>16)&255);
                int g = ((data[i][j]>>8)&255);
                int b = (data[i][j]&255);
                stuff[i][j] = new point3D(r, g, b);
            }
        }
        point3D[] choices = colorsSelected(stuff, numColors);
        int[] colors = getColors(choices);
        int[][] out = new int[data.length][data[0].length];
        for (int i = 0; i < out.length; i++) {
            for (int j = 0; j < out[0].length; j++) {
                out[i][j] = colors[chooseClosest(stuff[i][j], choices)];
            }
        }
        return out;
    }
    private static int[] getColors(point3D[] data){
        int[] out = new int[data.length];
        for (int i = 0; i < out.length; i++) {
            out[i] = data[i].toInt();
        }
        return out;
    }
    private static int chooseClosest(point3D p, point3D[] potential){
        int out = -1;
        double minDist = Double.MAX_VALUE;
        for (int i = 0; i < potential.length; i++) {
            double d = point3D.dist(p, potential[i]);
            if(d < minDist){
                minDist = d;
                out = i;
            }
        }
        return out;
    }
    private static class point3D{
        short x, y, z;
        public point3D(int x, int y, int z){
            this.x = (short)x;
            this.y = (short)y;
            this.z = (short)z;
        }
        public static double dist(point3D a, point3D b){
            return (a.x - b.x)*(a.x - b.x) + (a.y - b.y)*(a.y - b.y) + (a.z - b.z)*(a.z - b.z);
        }
        public int toInt(){
            return z | (y << 8) | (x << 16);
        }

    }
    private static class clusterAvg{
        int numAssociated;
        long x, y, z;
        public clusterAvg(long x, long y, long z){
            this.x = x;
            this.y = y;
            this.z = z;
            this.numAssociated = 0;
        }
        public clusterAvg(){
            this.x = 0;
            this.y = 0;
            this.z = 0;
            this.numAssociated = 0;
        }
        public void add(point3D p){
            this.x += p.x;
            this.y += p.y;
            this.z += p.z;
            this.numAssociated++;
        }
        public point3D getVal(){
            if(numAssociated != 0) {
                return new point3D((short) (this.x / numAssociated), (short) (this.y / numAssociated), (short) (this.z / numAssociated));
            }
            return new point3D(0, 0, 0);
        }
    }
}
