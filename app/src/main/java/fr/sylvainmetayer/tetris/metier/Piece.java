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

    /**
     * Each {@link List<String>} listed below are lists of points to check, for each position of a piece
     *
     * @see Piece#matricePosition0 & others to see the main usage of theses lists
     */
    protected List<String> bottomPointsPosition0;
    protected List<String> bottomPointsPosition1;
    protected List<String> bottomPointsPosition2;
    protected List<String> bottomPointsPosition3;

    protected List<String> leftPointsPosition0;
    protected List<String> leftPointsPosition1;
    protected List<String> leftPointsPosition2;
    protected List<String> leftPointsPosition3;

    protected List<String> rightPointsPosition0;
    protected List<String> rightPointsPosition1;
    protected List<String> rightPointsPosition2;
    protected List<String> rightPointsPosition3;


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

        this.bottomPointsPosition0 = new ArrayList<>();
        this.bottomPointsPosition1 = new ArrayList<>();
        this.bottomPointsPosition2 = new ArrayList<>();
        this.bottomPointsPosition3 = new ArrayList<>();

        this.leftPointsPosition0 = new ArrayList<>();
        this.leftPointsPosition1 = new ArrayList<>();
        this.leftPointsPosition2 = new ArrayList<>();
        this.leftPointsPosition3 = new ArrayList<>();

        this.rightPointsPosition0 = new ArrayList<>();
        this.rightPointsPosition1 = new ArrayList<>();
        this.rightPointsPosition2 = new ArrayList<>();
        this.rightPointsPosition3 = new ArrayList<>();

        this.maxRotation = 0;
        this.currentRotation = 0;
    }

    private Context getContext() {
        return context;
    }

    int[][] getMatrix() {
        return matrice;
    }

    protected int getStartLine() {
        return startLine;
    }

    protected int getStartColumn() {
        return startColumn;
    }

    /**
     * This function return the image of a piece, according to its value
     *
     * @param value int
     * @return int
     */
    public static int getImage(int value) {
        switch (value) {
            case 3:
                return R.drawable.green_image;
            case 2:
                return R.drawable.orange_image;
            case 1:
                return R.drawable.blue_image;
            case 0:
                return R.drawable.black_image;
        }
        return R.drawable.black_image;
    }

    /**
     * This function return the lists of points to check for a move to the left, according to the current rotation of the piece
     *
     * @param position int
     * @return List
     */
    private List<String> getLeftPointsToCheck(int position) {
        switch (position) {
            case 0:
                return leftPointsPosition0;
            case 1:
                return leftPointsPosition1;
            case 2:
                return leftPointsPosition2;
            case 3:
                return leftPointsPosition3;
        }
        return new ArrayList<>();
    }

    /**
     * This function return the lists of points to check for a move to the bottom, according to the current rotation of the piece
     *
     * @param position int
     * @return List
     */
    private List<String> getBottomPointsToCheck(int position) {
        switch (position) {
            case 0:
                return bottomPointsPosition0;
            case 1:
                return bottomPointsPosition1;
            case 2:
                return bottomPointsPosition2;
            case 3:
                return bottomPointsPosition3;
        }
        return new ArrayList<>();
    }

    /**
     * This function return the lists of points to check for a move to the right, according to the current rotation of the piece
     *
     * @param position int
     * @return List
     */
    private List<String> getRightPointsToCheck(int position) {
        switch (position) {
            case 0:
                return rightPointsPosition0;
            case 1:
                return rightPointsPosition1;
            case 2:
                return rightPointsPosition2;
            case 3:
                return rightPointsPosition3;
        }
        return new ArrayList<>();
    }

    /**
     * This function return the list of points to check for a move, according to
     * the current rotation of the piece and a move to a particular direction
     *
     * @param position 0 | 1 | 2 | 3
     * @param state    "left" | "right" | "down"
     * @return List
     */
    private List<String> getPointsToCheck(String state, int position) {
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
                    Log.i("CANGO" + "LEFT", "Out of boundary");
                    return false;
                }
            } else {
                if (newPieceColumnPosition >= getContext().getResources().getInteger(R.integer.maxColumns)) {
                    Log.i("CANGO" + "RIGHT", "Out of boundary");
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

    protected void setStartLine(int startLine) {
        this.startLine = startLine;
    }

    protected void setStartColumn(int startColumn) {
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

    static int getEmptyPieceValue() {
        return 0;
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
        // TODO
        /*
         * Here is an idea.
         * Get the next current rotation, apply the rotation to a copy of the piece,
         * and try to set it on a local copy of the gameboard.
         * Return the result of this simulation
         */
        return true;
    }

    protected void setMaxRotation(int maxRotation) {
        this.maxRotation = maxRotation;
    }

    private void setCurrentRotation(int currentRotation) {
        this.currentRotation = currentRotation;
    }
}
