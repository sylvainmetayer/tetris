package fr.sylvainmetayer.tetris.metier.pieces;

import android.content.Context;

import fr.sylvainmetayer.tetris.metier.Piece;

public class Piece_I extends Piece {

    private static int[][] matrice =
            {
                    {1},
                    {1},
                    {1}
            };

    public Piece_I(int line, int column, Context context) {
        super(matrice, line, column, 1, context);

        bottomPointsToCheckPosition0.add("2,0");

        bottomPointsToCheckPosition1.add("0,0");
        bottomPointsToCheckPosition1.add("0,1");
        bottomPointsToCheckPosition1.add("0,2");

        rightPointsToCheckPosition0.add("0,0");
        rightPointsToCheckPosition0.add("1,0");
        rightPointsToCheckPosition0.add("2,0");

        rightPointsToCheckPosition1.add("0,2");

        leftPointsToCheckPosition0.add("0,0");
        leftPointsToCheckPosition0.add("1,0");
        leftPointsToCheckPosition0.add("2,0");

        leftPointsToCheckPosition1.add("0,0");

        matricePosition0 = Piece_I.matrice;
        matricePosition1 = new int[][]{
                {1, 1, 1}
        };
        setMaxRotation(2);
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
