package mathStuff;

import java.awt.geom.Point2D;
import java.util.ArrayList;

public class BezierCurve implements Curve{
    private ArrayList<Point2D.Double> controls;
    private double[] coefficients;
    public BezierCurve(ArrayList<Point2D.Double> controlPoints){

    }
    public void calcCoefficients(){

    }
    @Override
    public Point2D.Double valueAt(double t) {
        return null;
    }

    /**
     * @param n total population size
     * @param k the number taken out
     * @return n choose k
     */
    private static int binomial(int n, int k){
        return 0;
    }
    private static int linProd(int low, int high){
        return 0;
    }
}
