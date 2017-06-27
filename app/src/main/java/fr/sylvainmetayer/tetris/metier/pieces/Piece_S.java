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
        bottomPointsToCheck.add("0,2");
        bottomPointsToCheck.add("1,0");
        bottomPointsToCheck.add("1,1");
        leftPointsToCheck.add("0,1");
        leftPointsToCheck.add("1,0");
        rightPointsToCheck.add("0,2");
        rightPointsToCheck.add("1,1");

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
