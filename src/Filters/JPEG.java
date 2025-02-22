package Filters;

import Interfaces.PixelFilter;
import core.DImage;

import javax.swing.*;

public class JPEG implements PixelFilter{
    private final int[][] Q = {
            {16 , 11 , 10 , 16 , 24 , 40 , 51 , 61 },
            {12 , 12 , 14 , 19 , 26 , 58 , 60 , 55 },
            {14 , 13 , 16 , 24 , 40 , 57 , 69 , 56 },
            {14 , 17 , 22 , 29 , 51 , 87 , 80 , 62 },
            {18 , 22 , 37 , 56 , 68 ,109 ,103 , 77 },
            {24 , 35 , 55 , 64 , 81 ,104 ,113 , 92 },
            {49 , 64 , 78 , 87 ,103 ,121 ,120 ,101 },
            {72 , 92 , 95 , 98 ,112 ,100 ,103 , 99 },
    };
    @Override
    public DImage processImage(DImage img) {
        int quality = Integer.parseInt(JOptionPane.showInputDialog("Enter quality 1-100"));
        return processImage(img, quality);
    }
    public DImage processImage(DImage img, int quality) {
        EvenBetterDownsampling e = new EvenBetterDownsampling();
        DImage newImg = e.processImage(img, 8*(int)Math.ceil(img.getWidth()/8.0), 8*(int)Math.ceil(img.getHeight()/8.0));
        int[][] data = newImg.getColorPixelGrid();
        changeQuality(quality);
        int[][] stuff = doAllWork(data, Q);
        DImage d = new DImage(img.getWidth(), img.getHeight());
        d.setPixels(stuff);
        return d;
    }
    private void changeQuality(int q){
        double S = (q >=50)?200 - 2*q:5000.0/q;
        for (int i = 0; i < Q.length; i++) {
            for (int j = 0; j < Q[0].length; j++) {
                Q[i][j] = round((50 + S*Q[i][j])/100.0);
            }
        }
    }
    private static int[][] doAllWork(int[][] in, int[][] Q){
        int[][] out = new int[in.length][in[0].length];
        for (int i = 0; i < in.length; i+=8) {
            for (int j = 0; j < in[0].length; j+=8) {
                for (int k = 0; k < 3; k++) {
                    double[][] section = getSubArray(in, i, j, k);
                    double[][] transformed = DCT(section);
                    printArr(transformed);
                    int[][] G = quantize(transformed, Q);
                    unQuantize(G, Q);
                    printArr(G);
                    addBack(out, iDCT(G), i, j, k);
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
    private static void unQuantize(int[][] data, int[][] Q){
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[0].length; j++) {
                data[i][j]*=Q[j][i];
            }
        }
    }
    private static int[][] iDCT(int[][] out){
        int[][] data = new int[out.length][out[0].length];
        for (int i = 0; i < out.length; i++) {
            for (int j = 0; j < out[0].length; j++) {
                data[i][j] = invertSingle(out, i, j);
            }
        }
        return data;
    }
    private static int invertSingle(double[][] transformed, int u, int v){
        double t = 0;
        for (int i = 0; i < transformed.length; i++) {
            for (int j = 0; j < transformed[0].length; j++) {
                t+=a(i)*a(j)*transformed[i][j]*specialCos(u, i)*specialCos(v, j);
            }
        }
        return (int)(0.25*t);
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
    private static int[][] quantize(double[][] transformed, int[][] Q){
        int[][] out = new int[transformed.length][transformed[0].length];
        for (int i = 0; i < transformed.length; i++) {
            for (int j = 0; j < transformed[0].length; j++) {
                out[i][j] = (int)round(transformed[i][j]/Q[j][i]);
            }
        }
        return out;
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
    private static void printArr(double[][] arr){
        for (double[] doubles : arr) {
            System.out.print("[");
            for (int j = 0; j < arr[0].length; j++) {
                System.out.print(doubles[j] + special(j, arr[0].length));
            }
            System.out.println();
        }
        System.out.println();
    }
    private static void printArr(int[][] arr){
        for (int[] doubles : arr) {
            System.out.print("[");
            for (int j = 0; j < arr[0].length; j++) {
                System.out.print(doubles[j] + special(j, arr[0].length));
            }
            System.out.println();
        }
        System.out.println();
    }
    private static String special(int j, int l){
        return (j < l - 1)? ", ": "]";
    }
    private static int round(double d){
        return (int)(d + 0.5*Math.signum(d));
    }
}
