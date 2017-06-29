package fr.sylvainmetayer.tetris.metier.pieces;

import android.content.Context;

import fr.sylvainmetayer.tetris.metier.Piece;

public class Piece_T extends Piece {

    private static int[][] matrice =
            {
                    {1, 1, 1},
                    {0, 1},
                    {0, 1},
            };

    public Piece_T(int line, int column, Context context) {
        super(matrice, line, column, 4, context);

        bottomPointsPosition0.add("0,0");
        bottomPointsPosition0.add("0,2");
        bottomPointsPosition0.add("2,1");

        bottomPointsPosition1.add("1,0");
        bottomPointsPosition1.add("1,1");
        bottomPointsPosition1.add("2,2");

        bottomPointsPosition2.add("2,0");
        bottomPointsPosition2.add("2,1");
        bottomPointsPosition2.add("2,2");

        bottomPointsPosition3.add("2,0");
        bottomPointsPosition3.add("1,1");
        bottomPointsPosition3.add("1,2");

        rightPointsPosition0.add("0,2");
        rightPointsPosition0.add("1,1");
        rightPointsPosition0.add("2,1");

        rightPointsPosition1.add("0,2");
        rightPointsPosition1.add("1,2");
        rightPointsPosition1.add("2,2");

        rightPointsPosition2.add("0,1");
        rightPointsPosition2.add("1,1");
        rightPointsPosition2.add("2,2");

        rightPointsPosition3.add("0,0");
        rightPointsPosition3.add("1,2");
        rightPointsPosition3.add("2,0");

        leftPointsPosition0.add("0,0");
        leftPointsPosition0.add("1,1");
        leftPointsPosition0.add("1,2");

        leftPointsPosition1.add("0,2");
        leftPointsPosition1.add("1,0");
        leftPointsPosition1.add("2,2");

        leftPointsPosition2.add("0,1");
        leftPointsPosition2.add("1,1");
        leftPointsPosition2.add("2,0");

        leftPointsPosition3.add("0,0");
        leftPointsPosition3.add("1,0");
        leftPointsPosition3.add("2,0");

        matricePosition0 = Piece_T.matrice;
        matricePosition1 = new int[][]{
                {0, 0, 1},
                {1, 1, 1},
                {0, 0, 1}
        };
        matricePosition2 = new int[][]{
                {0, 1, 0},
                {0, 1, 0},
                {1, 1, 1}
        };
        matricePosition3 = new int[][]{
                {1, 0, 0},
                {1, 1, 1},
                {1, 0, 0}
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
