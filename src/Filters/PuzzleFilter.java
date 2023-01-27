package Filters;

import Interfaces.PixelFilter;
import core.DImage;

public class PuzzleFilter implements PixelFilter{
    @Override
    public DImage processImage(DImage img) {

        return null;
    }
    private DImage formatImage(puzzle p){
        return null;
    }
    private DImage toImage(int[][] in){
        DImage d = new DImage(in[0].length, in.length);
        d.setPixels(in);
        return d;
    }
    private static class PuzzlePiece{

    }
    private static class puzzle{
        PuzzlePiece[][] pieces;
        private PuzzlePiece[][] randomize(){
            //possibly work our way down the sizes of groups of puzzle pieces, starting with maybe 4x4 groups of pieces, finding groups
            //with the same edges, and then randomly shuffling those,
            //then all the way down to 1x1
            return null;
        }
    }

    private enum edgeDirs{
        OUT, IN, NONE
    }
}
