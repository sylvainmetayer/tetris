package fr.sylvainmetayer.tetris.metier;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import fr.sylvainmetayer.tetris.R;
import fr.sylvainmetayer.tetris.utils.Utils;

public abstract class Piece implements Mouvement, MouvementPossible {

    private int[][] matrice;
    private int startLine;
    private int startColumn;
    private int color;
    private Context context;
    protected List<String> bottomPointsToCheckPosition0;
    protected List<String> bottomPointsToCheckPosition1;
    protected List<String> bottomPointsToCheckPosition2;
    protected List<String> bottomPointsToCheckPosition3;

    protected List<String> leftPointsToCheckPosition0;
    protected List<String> leftPointsToCheckPosition1;
    protected List<String> leftPointsToCheckPosition2;
    protected List<String> leftPointsToCheckPosition3;

    protected List<String> rightPointsToCheckPosition0;
    protected List<String> rightPointsToCheckPosition1;
    protected List<String> rightPointsToCheckPosition2;
    protected List<String> rightPointsToCheckPosition3;


    /**
     * This maximum number of ratation the piece can make
     */
    private int maxRotation;
    /**
     * This current rotation state of a piece.
     */
    private int currentRotation;

    /**
     * The first position of a piece. Required
     */
    protected int[][] matricePosition0;

    /**
     * The second position of a piece when rotating. Required.
     */
    protected int[][] matricePosition1;

    /**
     * This third position of a piece when rotating. Optional.
     */
    protected int[][] matricePosition2;

    /**
     * The fourth position of a piece when rotating. Optional.
     */
    protected int[][] matricePosition3;

    public Piece(int[][] matrice, int line, int column, int color, Context context) {
        this.matrice = matrice;
        this.startLine = line;
        this.startColumn = column;
        this.color = color;
        this.context = context;

        this.bottomPointsToCheckPosition0 = new ArrayList<>();
        this.bottomPointsToCheckPosition1 = new ArrayList<>();
        this.bottomPointsToCheckPosition2 = new ArrayList<>();
        this.bottomPointsToCheckPosition3 = new ArrayList<>();

        this.leftPointsToCheckPosition0 = new ArrayList<>();
        this.leftPointsToCheckPosition1 = new ArrayList<>();
        this.leftPointsToCheckPosition2 = new ArrayList<>();
        this.leftPointsToCheckPosition3 = new ArrayList<>();

        this.rightPointsToCheckPosition0 = new ArrayList<>();
        this.rightPointsToCheckPosition1 = new ArrayList<>();
        this.rightPointsToCheckPosition2 = new ArrayList<>();
        this.rightPointsToCheckPosition3 = new ArrayList<>();
        this.maxRotation = 0;
        this.currentRotation = 0;
    }

    public Context getContext() {
        return context;
    }

    public int[][] getMatrix() {
        return matrice;
    }

    public int getStartLine() {
        return startLine;
    }

    public int getStartColumn() {
        return startColumn;
    }

    public static int getImage(int value) {
        switch (value) {
            case 1:
                return R.drawable.blue_image;
            case 0:
                return R.drawable.black_image;
        }
        return R.drawable.black_image;
    }

    private List<String> getLeftPointsToCheck(int position) {
        switch (position) {
            case 0:
                return leftPointsToCheckPosition0;
            case 1:
                return leftPointsToCheckPosition1;
            case 2:
                return leftPointsToCheckPosition2;
            case 3:
                return leftPointsToCheckPosition3;
        }
        return new ArrayList<>();
    }

    private List<String> getBottomPointsToCheck(int position) {
        switch (position) {
            case 0:
                return bottomPointsToCheckPosition0;
            case 1:
                return bottomPointsToCheckPosition1;
            case 2:
                return bottomPointsToCheckPosition2;
            case 3:
                return bottomPointsToCheckPosition3;
        }
        return new ArrayList<>();
    }

    private List<String> getRightPointsToCheck(int position) {
        switch (position) {
            case 0:
                return rightPointsToCheckPosition0;
            case 1:
                return rightPointsToCheckPosition1;
            case 2:
                return rightPointsToCheckPosition2;
            case 3:
                return rightPointsToCheckPosition3;
        }
        return new ArrayList<>();
    }

    public List<String> getPointsToCheck(String state, int position) {
        switch (state) {
            case "left":
                return getLeftPointsToCheck(position);
            case "down":
                return getBottomPointsToCheck(position);
            case "right":
                return getRightPointsToCheck(position);
        }
        return new ArrayList<>();
    }

    @Override
    public boolean canGoLeft(int[][] gameboard) {
        return checkColumnLimits(gameboard, -1, getPointsToCheck("left", currentRotation));
    }

    @Override
    public boolean canGoRight(int[][] gameboard) {
        return checkColumnLimits(gameboard, 1, getPointsToCheck("right", currentRotation));
    }

    /**
     * This function determines whether a piece can go left, or right, according to the value of @columnValue
     *
     * @param gameboard     the current gameboard
     * @param columnValue   1 if right, -1 for left.
     * @param pointsToCheck The points we want to check for this piece.
     * @return boolean
     */
    private boolean checkColumnLimits(int[][] gameboard, int columnValue, List<String> pointsToCheck) {
        boolean isLeft = (columnValue == -1);

        int column = this.getStartColumn();
        int line = this.getStartLine();

        for (String point : pointsToCheck) {
            int pointLine = Integer.parseInt(point.split(",")[0]);
            int pointColumn = Integer.parseInt(point.split(",")[1]);

            int pieceLine = pointLine + line;
            int pieceColumn = pointColumn + column;

            int newPieceColumnPosition = pieceColumn + columnValue;

            if (isLeft) {
                if (newPieceColumnPosition < 0) {
                    Log.i("CANGO" + "LEFT", "Out of boundary, continue");
                    return false;
                }
            } else {
                if (newPieceColumnPosition >= getContext().getResources().getInteger(R.integer.maxColumns)) {
                    Log.i("CANGO" + "RIGHT", "Out of boundary, continue");
                    return false;
                }
            }

            Log.d("CANGO" + (isLeft ? "LEFT" : "RIGHT"), isLeft + "Going to check" + Utils.formatPosition(pieceLine, newPieceColumnPosition));
            if (gameboard[pieceLine][newPieceColumnPosition] != getEmptyPieceValue()) {
                Log.w("CANGO" + (isLeft ? "LEFT" : "RIGHT"), "The position [" + (pieceLine) + "," + newPieceColumnPosition + "] is not empty !");
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean canGoDown(int[][] gameboard) {
        if (!(getStartLine() + getMatrix().length < getContext().getResources().getInteger(R.integer.maxLines)))
            return false;

        int column = this.getStartColumn();
        int line = this.getStartLine();

        List<String> pointsToCheck = getPointsToCheck("down", currentRotation);

        for (String point : pointsToCheck) {
            int pointLine = Integer.parseInt(point.split(",")[0]);
            int pointColumn = Integer.parseInt(point.split(",")[1]);

            int pieceLine = pointLine + line;
            int pieceColumn = pointColumn + column;

            if (pieceLine + 1 >= gameboard.length) {
                Log.i("CANGODOWN", "Out of boundary, continue");
                continue;
            }

            if (gameboard[pieceLine + 1][pieceColumn] != getEmptyPieceValue()) {
                Log.w("CANGODOWN", "The position [" + (pieceLine + 1) + "," + pieceColumn + "] is not empty !");
                return false;
            }
        }
        return true;
    }

    public void setStartLine(int startLine) {
        this.startLine = startLine;
    }

    public void setStartColumn(int startColumn) {
        this.startColumn = startColumn;
    }

    @Override
    public String toString() {
        StringBuilder sb;
        sb = new StringBuilder();
        sb.append("Matrice : ").append(Arrays.deepToString(matrice)).append("\n");
        sb.append("startLine : ").append(startLine).append(", ");
        sb.append("startColumn : ").append(startColumn).append(", ");
        sb.append("Color : ").append(color);
        return sb.toString();
    }

    public static int getEmptyPieceValue() {
        return 0;
    }

    public static int[][] transposeMatrix(int[][] m) {
        int[][] temp = new int[m[0].length][m.length];
        for (int i = 0; i < m.length; i++)
            for (int j = 0; j < m[0].length; j++)
                temp[j][i] = m[i][j];
        return temp;
    }

    @Override
    public void rotate() {
        Log.d("ROTATION_BEFORE", Arrays.deepToString(matrice));

        setCurrentRotation((currentRotation + 1) % maxRotation);
        Log.d("ROTATION", "Going to apply rotation n°" + currentRotation);
        matrice = getMatrix(currentRotation);
        Log.d("ROTATION_AFTER", Arrays.deepToString(matrice));
    }

    /**
     * Return the matri corresponding to a number
     *
     * @param rotationNumber int
     * @return int[][]
     */
    private int[][] getMatrix(int rotationNumber) {
        switch (rotationNumber) {
            case 0:
                return matricePosition0;
            case 1:
                return matricePosition1;
            case 2:
                return matricePosition2;
            case 3:
                return matricePosition3;
        }
        return new int[0][0];
    }

    @Override
    public boolean canRotate(int[][] gameboard) {
        return true;
    }

    public void setMaxRotation(int maxRotation) {
        this.maxRotation = maxRotation;
    }

    public void setCurrentRotation(int currentRotation) {
        this.currentRotation = currentRotation;
    }
}
