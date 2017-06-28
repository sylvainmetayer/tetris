package fr.sylvainmetayer.tetris.metier.pieces;

import android.content.Context;

import fr.sylvainmetayer.tetris.metier.Piece;

public class Piece_S extends Piece {

    private static int[][] matrice = {
            {0, 1, 1},
            {1, 1}
    };

    public Piece_S(int line, int column, Context context) {
        super(matrice, line, column, 1, context);
        bottomPointsToCheckPosition0.add("0,2");
        bottomPointsToCheckPosition0.add("1,0");
        bottomPointsToCheckPosition0.add("1,1");

        bottomPointsToCheckPosition1.add("1,0");
        bottomPointsToCheckPosition1.add("2,1");

        bottomPointsToCheckPosition2.add("0,2");
        bottomPointsToCheckPosition2.add("1,0");
        bottomPointsToCheckPosition2.add("1,1");

        bottomPointsToCheckPosition3.add("0,0");
        bottomPointsToCheckPosition3.add("1,1");
        bottomPointsToCheckPosition3.add("1,2");

        leftPointsToCheckPosition0.add("0,1");
        leftPointsToCheckPosition0.add("1,0");

        leftPointsToCheckPosition1.add("0,0");
        leftPointsToCheckPosition1.add("1,0");
        leftPointsToCheckPosition1.add("2,1");

        leftPointsToCheckPosition2.add("0,1");
        leftPointsToCheckPosition2.add("1,0");

        leftPointsToCheckPosition3.add("0,0");
        leftPointsToCheckPosition3.add("1,1");

        rightPointsToCheckPosition0.add("0,2");
        rightPointsToCheckPosition0.add("1,1");

        rightPointsToCheckPosition1.add("0,0");
        rightPointsToCheckPosition1.add("1,1");
        rightPointsToCheckPosition1.add("2,1");

        rightPointsToCheckPosition2.add("0,2");
        rightPointsToCheckPosition2.add("1,1");

        rightPointsToCheckPosition3.add("0,1");
        rightPointsToCheckPosition3.add("1,2");

        matricePosition0 = Piece_S.matrice;
        matricePosition1 = new int[][]{
                {1, 0},
                {1, 1},
                {0, 1}
        };
        matricePosition2 = Piece_S.matrice;
        matricePosition3 = new int[][]{
                {1, 1},
                {0, 1, 1}
        };
        setMaxRotation(4);

    }

    @Override
    public void left() {
        setStartColumn(getStartColumn() - 1);
    }

    @Override
    public void right() {
        setStartColumn(getStartColumn() + 1);
    }

    @Override
    public void down() {
        setStartLine(getStartLine() + 1);
    }

}
