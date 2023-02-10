package Filters;

public class trackByColor extends trackFeature{
    private int mainColor = getColor(locSelected);
    private boolean[][] close;
    private int maxDist = 30;
    @Override
    thing getCenter(int[][] data) {
        long totalX = 0;
        long totalY = 0;
        int numPoints = 0;
        close = new boolean[data.length][data[0].length];
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[0].length; j++) {
                if(isClose(data[i][j])){
                    totalX+=i;
                    totalY+=j;
                    numPoints++;
                }
            }
        }

        double sf = (numPoints == 0)? 0: 1.0/numPoints;
        point p = new point((int)(sf*totalX), (int)(sf*totalY));
        int maxD = 0;
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[0].length; j++) {
                int d = distSquared(new point(i, j), p);
                if(close[i][j] && d > maxD){
                    maxD = d;
                }
            }
        }
        return new thing(p, Math.sqrt(maxD));
    }
    private boolean isClose(int color){
        int d = 0;
        for (int i = 0; i < 3; i++) {
            int c1 = (mainColor>>>(i*8))&255;
            int c2 = (color>>>(i*8))&255;
            d += (c2 - c1)*(c2 - c1);
        }
        return Math.sqrt(d) <= maxDist;
    }
    private static int distSquared(point p1, point p2){
        return (p1.x - p2.x)*(p1.x - p2.x) + (p1.y - p2.y)*(p1.y - p2.y);
    }

}
