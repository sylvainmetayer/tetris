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

        bottomPointsPosition0.add("2,0");

        bottomPointsPosition1.add("0,0");
        bottomPointsPosition1.add("0,1");
        bottomPointsPosition1.add("0,2");

        rightPointsPosition0.add("0,0");
        rightPointsPosition0.add("1,0");
        rightPointsPosition0.add("2,0");

        rightPointsPosition1.add("0,2");

        leftPointsPosition0.add("0,0");
        leftPointsPosition0.add("1,0");
        leftPointsPosition0.add("2,0");

        leftPointsPosition1.add("0,0");

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
