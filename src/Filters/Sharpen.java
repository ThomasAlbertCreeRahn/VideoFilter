package Filters;

public class Sharpen extends BasicBlur{
    @Override
    protected double[][] generateMatrix(int in) {
        return invertMatrix(super.generateMatrix(in));
    }
    private static double[][] invertMatrix(double[][] in){
        double[][] matrixOfMinors = new double[in.length][in.length];
        for (int i = 0; i < in.length; i++) {
            for (int j = 0; j < in.length; j++) {
                matrixOfMinors[i][j] = det(getSubArray(i, j, in));
            }
        }
        double d = 0.0;
        int m = -1;
        for (int i = 0; i < in.length; i++) {
            m*=-1;
            in[0][i]+=m*matrixOfMinors[0][i];
        }
        matrixOfMinors = cofactor(matrixOfMinors);
        matrixOfMinors = transpose(matrixOfMinors);
        scale(matrixOfMinors, 1.0/d);
        return matrixOfMinors;
    }
    private static double[][] scale(double[][] in, double sf){
        for (int i = 0; i < in.length; i++) {
            for (int j = 0; j < in[0].length; j++) {
                in[i][j]*=sf;
            }
        }
        return in;
    }
    private static double[][] cofactor(double[][] in){
        for (int i = 0; i < in.length; i++) {
            for (int j = 0; j < in[0].length; j++) {
                in[i][j]*=((i&1) == (j&1))?1:-1;
            }
        }
        return in;
    }
    private static double[][] transpose(double[][] in){
        double[][] out = new double[in[0].length][in.length];
        for (int i = 0; i < in.length; i++) {
            for (int j = 0; j < in[0].length; j++) {
                out[j][i] = in[i][j];
            }
        }
        return out;
    }
    private static double det(double[][] in){
        if(in.length == 2){
            return (in[0][0]*in[1][1]) - (in[0][1]*in[1][0]);
        }
        int m = 1;
        double total = 0;
        for (int i = 0; i < in.length; i++) {
            double[][] stuff = getSubArray(0, i, in);
            total+=m*det(stuff);
            m*=-1;
        }
        return total;
    }
    private static double[][] getSubArray(int r, int c, double[][] data){
        double[][] out = new double[data.length - 1][data.length - 1];
        for (int i = 0; i < out.length; i++) {
            for (int j = 0; j < out.length; j++) {
                out[i][j] = data[i + ((i >= r)? 1: 0)][j + ((j >= c)? 1: 0)];
            }
        }
        return out;
    }
}
