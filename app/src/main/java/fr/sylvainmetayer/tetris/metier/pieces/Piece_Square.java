package fr.sylvainmetayer.tetris.metier.pieces;

import android.content.Context;

import fr.sylvainmetayer.tetris.metier.Piece;

public class Piece_Square extends Piece {

    private static int[][] matrice =
            {
                    {1, 1, 1},
                    {1, 1, 1},
                    {1, 1, 1}
            };

    public Piece_Square(int line, int column, Context context) {
        super(matrice, line, column, 2, context);

        bottomPointsPosition0.add("2,0");
        bottomPointsPosition0.add("2,1");
        bottomPointsPosition0.add("2,2");

        rightPointsPosition0.add("0,2");
        rightPointsPosition0.add("1,2");
        rightPointsPosition0.add("2,2");


        leftPointsPosition0.add("0,0");
        leftPointsPosition0.add("1,0");
        leftPointsPosition0.add("2,0");

        matricePosition0 = Piece_Square.matrice;
        setMaxRotation(1);
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
