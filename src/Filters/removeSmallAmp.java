package Filters;

import Interfaces.PixelFilter;
import core.DImage;

import javax.swing.*;

public class removeSmallAmp implements PixelFilter{
    @Override
    public DImage processImage(DImage img) {
        EvenBetterDownsampling e = new EvenBetterDownsampling();
        DImage newImg = e.processImage(img, 8*(int)Math.ceil(img.getWidth()/8.0), 8*(int)Math.ceil(img.getHeight()/8.0));
        int[][] data = newImg.getColorPixelGrid();
        int n = Integer.parseInt(JOptionPane.showInputDialog("number of waves to keep"));
        //i = y = height
        //j = x = width
        //this won't be bad at all
        int[][] stuff = doAllWork(data, n);
        DImage d = new DImage(img.getWidth(), img.getHeight());
        d.setPixels(stuff);
        return d;
    }
    private static int[][] doAllWork(int[][] in, int n){
        int[][] out = new int[in.length][in[0].length];
        for (int i = 0; i < in.length; i+=8) {
            for (int j = 0; j < in[0].length; j+=8) {
                for (int k = 0; k < 3; k++) {
                    double[][] section = getSubArray(in, i, j, k);
                    double[][] transformed = DCT(section);
                    transformed = quantize(transformed, n);
//                    unQuantize(G);
                    addBack(out, iDCT(transformed), i, j, k);
                }
            }
        }
        for (int i = 0; i < in.length; i++) {
            for (int j = 0; j < in[0].length; j++) {
                out[i][j]|=(255<<24);
            }
        }
        return out;
    }
    private static int[][] iDCT(double[][] transformed){
        int[][] out = new int[transformed.length][transformed[0].length];
        for (int i = 0; i < transformed.length; i++) {
            for (int j = 0; j < transformed[0].length; j++) {
                out[i][j] = invertSingle(transformed, i, j);
            }
        }
        return out;
    }
    private static void addBack(int[][] out, int[][] data, int i, int j, int k){
        for (int l = 0; l < 8; l++) {
            for (int m = 0; m < 8; m++) {
                int u = Math.max(0, Math.min(data[l][m]+128, 255));
                System.out.println(u);
                out[i + l][j + m]|=u<<(8*k);
            }
        }
    }
    private static double[][] getSubArray(int[][] data, int i, int j, int level){
        double[][] out = new double[8][8];
        for (int k = 0; k < 8; k++) {
            for (int l = 0; l < 8; l++) {
                out[k][l] = (data[i + k][j + l]>>>(8*level))&255;
                out[k][l]-=128;
            }
        }
        return out;
    }
    private static void iDCT(int[][] out){
        for (int i = 0; i < out.length; i++) {
            for (int j = 0; j < out[0].length; j++) {
                out[i][j] = invertSingle(out, i, j);
            }
        }
    }
    private static int invertSingle(double[][] transformed, int u, int v){
        double t = 0;
        for (int i = 0; i < transformed.length; i++) {
            for (int j = 0; j < transformed[0].length; j++) {
                t+=a(i)*a(j)*transformed[i][j]*specialCos(u, i)*specialCos(v, j);
            }
        }
        return (int)t/4;
    }
    private static int invertSingle(int[][] transformed, int u, int v){
        double t = 0;
        for (int i = 0; i < transformed.length; i++) {
            for (int j = 0; j < transformed[0].length; j++) {
                t+=a(i)*a(j)*transformed[i][j]*specialCos(u, i)*specialCos(v, j);
            }
        }
        return (int)t/4;
    }
    private static double[][] DCT(double[][] data){
        double[][] out = new double[data.length][data[0].length];
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[0].length; j++) {
                out[i][j] = singlePoint(data, i, j);
            }
        }
        return out;
    }
    private static double singlePoint(double[][] data, int u, int v){
        double t = 0;
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[0].length; j++) {
                t+=F(i, j, u, v, data);
            }
        }
        return t*a(u)*a(v)/4.0;
    }
    private static double[][] quantize(double[][] transformed, int n){
        double[] best = new double[n];
        coord[] bestCoords = new coord[n];
        for (int i = 0; i < transformed.length; i++) {
            for (int j = 0; j < transformed[0].length; j++) {
                if(Math.abs(transformed[i][j]) > best[0]){
                    addToBest(Math.abs(transformed[i][j]), new coord(i, j), bestCoords, best);
                }
            }
        }
        double[][] out = new double[transformed.length][transformed[0].length];
        for (coord bestCoord : bestCoords) {
            out[bestCoord.x][bestCoord.y] = transformed[bestCoord.x][bestCoord.y];
        }
        return out;
    }
    private static void addToBest(double val, coord loc, coord[] bestCoords, double[] best){
        int i = 1;
        for (; i < best.length && val > best[i]; i++) {
            best[i - 1] = best[i];
            bestCoords[i - 1] = bestCoords[i];
        }
        bestCoords[i - 1] = loc;
        best[i-1] = val;
    }
    private static class coord{
        int x, y;
        coord(int x, int y){
            this.x = x;
            this.y = y;
        }
    }
    private static double a(int u){
        return (u == 0)? Math.sqrt(0.5):1.0;
    }
    private static double F(int i, int j, int u, int v, double[][] data){
        return data[i][j]*specialCos(i, u)*specialCos(j, v);
    }
    private static final double c = Math.PI/16.0;
    private static double specialCos(int x, int u){
        return Math.cos((2*x+1)*(u)*c);
    }
}
