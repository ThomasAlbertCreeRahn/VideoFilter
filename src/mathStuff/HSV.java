package mathStuff;

public class HSV {
    private int H;
    private double S, V;
    public int toRGB(){

    }
    public HSV(int r, int g, int b){
        int M = Math.max(r, Math.max(g,b));
        int m = Math.min(r, Math.min(g, b));
        int C = M - m;
        if(C == 0){
            H = 0;
        }else if(M == r){
            H = ((int)(60 * (g - b)/(double)C)%360 + 360)%360;
        }else if(M == b) {
            H = (int)(60 * (b - r)/(double)C) + 120;
        }else{
            H = (int)(60 * (r - g)/(double)C) + 240;
        }
        V = M;
        S = (M == 0)? 0: C/V;
    }
}
