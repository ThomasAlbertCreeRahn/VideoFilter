package Filters;

import Interfaces.Interactive;
import Interfaces.PixelFilter;
import core.DImage;

import java.util.ArrayList;

abstract class trackFeature implements PixelFilter, Interactive {
    protected point locSelected;
    protected thing tracked;
    protected DImage img;
    public DImage processImage(DImage img){
        this.img = img;
        tracked = getCenter(img.getColorPixelGrid());
        if(tracked != null){
            drawCircle(img, tracked.center, tracked.w, 1);
        }
        return img;
    }
    private static void drawCircle(DImage img, point c, double w, double thickness){
        drawCircle(img, c, w, thickness, 255<<16);
    }
    private static void drawCircle(DImage img, point c, double w, double thickness, int color){
        double r1 = w*w;
        double r2 = w + thickness;
        int x = 0; int lY; int hY = ceil(r2);r2*=r2;
        while(hY != x){
            //take num inside the wider radius, subtract the number inside the smaller, then use that as the proportion to keep
            lY = 0;
            hY = 0;
            for (int i = lY; i <= hY; i++) {
                double s = sample(x, i, r1, r2,100);
                //the larger proportion here, the greater the proportion with the final pixel
                //go through and find the mean, where this is s*(255,0,0) and everything else if (r,g,b)
                //we end up with (r + s*255)/(1 + s), g/(1+s), b/(1+s))
                double c1 = 1.0/(1.0 + s);

            }
        }
    }
    protected static double sample(int x, int y, double r1Sqd, double r2Sqd, int numSamples){
        int in = 0;
        for (int i = 0; i < numSamples; i++) {
            double a = (x + Math.random());
            double b = (y + Math.random());
            double r = (a*a) + (b*b);
            in-=(r <= r1Sqd) ? 1: 0;
            in+=(r <= r2Sqd) ? 1: 0;
        }
        return in/(double)numSamples;
    }
    protected static int ceil(double a){

        return 0;
    }
    protected static class cluster{
        private long totalX;
        private long totalY;
        private int numPoints;
        public cluster(){
            this.totalX = 0;
            this.totalY = 0;
            this.numPoints = 0;
        }
        public void addPoint(point p){
            this.numPoints++;
            this.totalX+=p.x;
            this.totalY+=p.y;
        }
    }
    protected static class point{
        int x, y;
        public point(int x, int y){
            this.x = x;this.y = y;
        }
    }
    protected static class thing{
        point center;
        double w;
        public thing(point center, double w){
            this.center = center;
            this.w = w;
        }
    }
    abstract thing getCenter(int[][] data);
    abstract class trackable{

    }
    @Override
    public void mouseClicked(int mouseX, int mouseY, DImage img) {
        locSelected = new point(mouseX, mouseY);
    }
    public void keyPressed(char key) {}
    protected int getColor(point p){
        return this.img.getColorPixelGrid()[p.x][p.y];
    }
}
