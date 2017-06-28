package fr.sylvainmetayer.tetris.metier;

import android.util.Log;

import java.util.ArrayList;
import java.util.Random;

import fr.sylvainmetayer.tetris.MainActivity;
import fr.sylvainmetayer.tetris.R;
import fr.sylvainmetayer.tetris.metier.pieces.Piece_I;
import fr.sylvainmetayer.tetris.metier.pieces.Piece_S;
import fr.sylvainmetayer.tetris.metier.pieces.Piece_Square;
import fr.sylvainmetayer.tetris.utils.Utils;

public class Game {
    private int[][] gameboard;
    private ArrayList<Piece> pieces;
    private boolean pause;
    private int score;

    /**
     * Initialize a gameboard, with a list of piece which will be added to the 2D int matrix.
     *
     * @param pieces     {@link java.util.List<Piece>}
     * @param nb_lines   int
     * @param nb_columns int
     */
    public Game(ArrayList<Piece> pieces, int nb_lines, int nb_columns) {

        this.pieces = pieces;
        gameboard = new int[nb_lines][nb_columns];
        this.pause = true;
        this.score = 0;

        resetGame();

        for (Piece piece : pieces) {
            int column = piece.getStartColumn();
            int line = piece.getStartLine();
            int[][] matrice = piece.getMatrix();
            Log.d("INIT", piece.toString());
            Log.d("INIT", "The piece start at " + Utils.formatPosition(line, column));

            for (int matriceLine = 0; matriceLine < matrice.length; matriceLine++) {
                for (int matriceColumn = 0; matriceColumn < matrice[matriceLine].length; matriceColumn++) {
                    int matriceValue = piece.getColor(matriceLine, matriceColumn);
                    int pieceColumn = matriceColumn + column;
                    int pieceLine = matriceLine + line;
                    Log.d("INIT", "I will affect the value '" + matriceValue + "' at " + Utils.formatPosition(pieceLine, pieceColumn));
                    gameboard[pieceLine][pieceColumn] = matriceValue;
                }
            }
        }
        Log.d("INIT_ENDED", logGameboard(gameboard));
    }

    /**
     * This function reset the gameboard
     */
    private void resetGame() {
        for (int line = 0; line < gameboard.length; line++) {
            for (int column = 0; column < gameboard[line].length; column++) {
                gameboard[line][column] = 0;
            }
        }
    }

    /**
     * Return a 2D array as a 1D array
     */
    public Integer[] getGameboard() {

        Integer newArray[] = new Integer[gameboard.length * gameboard[0].length];
        for (int i = 0; i < gameboard.length; i++) {
            int[] row = gameboard[i];
            for (int j = 0; j < row.length; j++) {
                int number = gameboard[i][j];
                newArray[i * row.length + j] = number;
            }
        }
        return newArray;
    }

    /**
     * This function add a given piece to the gameboard
     *
     * @param piece The piece to add
     * @return true if the piece has been added, false otherwise
     */
    private boolean addPieceToGameBoard(Piece piece) {

        if (piece == null)
            return false;

        int column = piece.getStartColumn();
        int line = piece.getStartLine();
        int[][] matrice = piece.getMatrix();

        Log.d("ADD_PIECE", piece.toString());

        for (int matriceLine = 0; matriceLine < matrice.length; matriceLine++) {
            for (int matriceColumn = 0; matriceColumn < matrice[matriceLine].length; matriceColumn++) {
                int matriceValue = piece.getColor(matriceLine, matriceColumn);
                int pieceColumn = matriceColumn + column;
                int pieceLine = matriceLine + line;
                Log.d("ADD_PIECE", "affect value '" + matriceValue + "' at " + Utils.formatPosition(pieceLine, pieceColumn));

                if (gameboard[pieceLine][pieceColumn] != Piece.getEmptyPieceValue() && matriceValue != Piece.getEmptyPieceValue()) {
                    return false;
                }
                gameboard[pieceLine][pieceColumn] = matriceValue;
            }
        }
        return true;
    }

    public void performAction(final MainActivity activity) {
        Piece lastPiece = this.getCurrentPiece();

        Log.d("LAST_PIECE", lastPiece.toString());

        if (lastPiece.canGoDown(gameboard) && !this.isPause()) {
            // Piece is currently going down
            this.moveDown(lastPiece);
            lastPiece.down();
            Log.d("STATE_AFTER", lastPiece.toString());
        } else {
            // Piece can't go down anymore, let's check full lines & create a new piece
            checkFullLines();
            createNewPiece(activity);
        }
        Log.d("GBOARD", logGameboard(gameboard));
    }

    private void createNewPiece(MainActivity activity) {
        Log.d("PERFORM", "Creation of a new piece");
        int maxClassPiece = 3;

        Random r = new Random();
        int classToChoose = r.nextInt(maxClassPiece);

        int randomColumn = r.nextInt(activity.getResources().getInteger(R.integer.maxColumns) - 2);

        Piece piece = getRandomPiece(activity, classToChoose, randomColumn);

        if (!addPieceToGameBoard(piece)) // Can't add piece, game over
            endGame(activity);
        pieces.add(piece);
    }

    private Piece getRandomPiece(MainActivity activity, int classToChoose, int randomColumn) {
        Piece piece = null;
        switch (classToChoose) {
            case 0:
                piece = new Piece_S(0, randomColumn, activity);
                break;
            case 1:
                piece = new Piece_I(0, randomColumn, activity);
                break;
            case 2:
                piece = new Piece_Square(0, randomColumn, activity);
                break;
        }
        return piece;
    }

    /**
     * This method is used to remove full lines
     */
    private void checkFullLines() {
        for (int gameLine = gameboard.length - 1; gameLine >= 0; gameLine--) {
            int cpt = 0;
            for (int gameColumn = 0; gameColumn < gameboard[gameLine].length; gameColumn++) {
                int pieceValue = gameboard[gameLine][gameColumn];
                if (pieceValue != Piece.getEmptyPieceValue())
                    cpt++;
            }

            if (cpt == gameboard[gameLine].length) {
                // Line is full
                for (int gameColumnIterator = 0; gameColumnIterator < gameboard[gameLine].length; gameColumnIterator++) {
                    gameboard[gameLine][gameColumnIterator] = Piece.getEmptyPieceValue();
                    score += cpt;
                }
                moveAllPieceDownAboveThisLine(gameLine);
            }
        }
    }

    /**
     * This function is used to move all piece that are below a given line.
     * This function is only called by @{@link Game#checkFullLines()}
     *
     * @param line int: the limit line
     */
    private void moveAllPieceDownAboveThisLine(int line) {
        for (int gameLine = 0; gameLine < line; gameLine++) {
            for (int gameColumn = 0; gameColumn < gameboard[gameLine].length; gameColumn++) {
                int pieceValue = gameboard[gameLine][gameColumn];
                gameboard[gameLine][gameColumn] = Piece.getEmptyPieceValue();
                gameboard[gameLine + 1][gameColumn] = pieceValue;
            }
        }
    }

    /**
     * This function is called when the game is over.
     *
     * @param activity {@link MainActivity}
     */
    private void endGame(MainActivity activity) {
        activity.endGame();
        Log.i("END", "GAME OVER");
    }

    /**
     * This function returns the piece that is currently moving on the gameboard
     *
     * @return Piece
     */
    private Piece getCurrentPiece() {
        return pieces.get(pieces.size() - 1);
    }

    /**
     * This function logs a gameboard and return it's textual representation.
     *
     * @param gameboard int[][]
     * @return String
     */
    private static String logGameboard(int[][] gameboard) {
        StringBuilder sb = new StringBuilder();
        for (int[] aGameboard : gameboard) {
            for (int anAGameboard : aGameboard) {
                sb.append(anAGameboard).append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    /**
     * This function is used to toggle the {@link Game#pause} value
     */
    public void togglePause() {
        this.pause = !pause;
    }

    public boolean isPause() {
        return pause;
    }

    public String getScore() {
        return "Score : " + score;
    }

    /**
     * This function move the current piece to the left, if possible
     */
    public void moveLeft() {
        if (this.isPause())
            return;

        Piece piece = getCurrentPiece();

        if (!piece.canGoLeft(gameboard))
            return;

        // -1 because we want to move left.
        moveColumn(piece, -1);
        piece.left();
    }

    /**
     * This function move the current piece to the bottom of the gameboard.
     *
     * @param piece The piece we want to move down
     */
    private void moveDown(Piece piece) {
        int column = piece.getStartColumn();
        int line = piece.getStartLine();
        int[][] matrice = piece.getMatrix();

        for (int matriceLine = matrice.length - 1; matriceLine >= 0; matriceLine--) {
            for (int matriceColumn = 0; matriceColumn < matrice[matriceLine].length; matriceColumn++) {
                int matriceValue = piece.getColor(matriceLine, matriceColumn);
                int pieceColumn = matriceColumn + column;
                int pieceLine = matriceLine + line;

                if (matriceValue != Piece.getEmptyPieceValue() && gameboard[pieceLine + 1][pieceColumn] == Piece.getEmptyPieceValue()) {
                    gameboard[pieceLine][pieceColumn] = Piece.getEmptyPieceValue();
                    gameboard[pieceLine + 1][pieceColumn] = matriceValue;
                }
            }
        }
    }

    /**
     * This function move the current piece to the right, if possible
     */
    public void moveRight() {
        if (this.isPause())
            return;

        Piece piece = getCurrentPiece();

        if (!piece.canGoRight(gameboard))
            return;

        moveColumn(piece, 1);
        piece.right();
    }

    /**
     * This function move a piece on a column, according to the value of #columnValue
     *
     * @param piece       The piece we want to move
     * @param columnValue Whether we want to move it left (-1) or right (1)
     */
    private void moveColumn(Piece piece, int columnValue) {
        int column = piece.getStartColumn();
        int line = piece.getStartLine();
        int[][] matrice = piece.getMatrix();

        for (int matriceLine = 0; matriceLine < matrice.length; matriceLine++) {
            for (int matriceColumn = 0; matriceColumn < matrice[matriceLine].length; matriceColumn++) {
                int matriceValue = piece.getColor(matriceLine, matriceColumn);
                int pieceColumn = matriceColumn + column;
                int pieceLine = matriceLine + line;

                gameboard[pieceLine][pieceColumn] = 0;
                gameboard[pieceLine][pieceColumn + columnValue] = matriceValue;
            }
        }
    }

    /**
     * This function is used to rotate the current piece, if possible
     */
    public void rotate() {
        if (this.isPause())
            return;

        Piece piece = getCurrentPiece();

        Log.d("CANROTATE", String.valueOf(piece.canRotate(gameboard)));

        if (!piece.canRotate(gameboard))
            return;

        removePieceFromGameBoard(piece);
        piece.rotate();
        addPieceToGameBoard(piece);
    }

    /**
     * This function remove a piece from the gameboard
     * It should only be used by {@link Game#rotate()} to avoid side effect
     *
     * @param piece The piece to remove
     */
    private void removePieceFromGameBoard(Piece piece) {
        int column = piece.getStartColumn();
        int line = piece.getStartLine();
        int[][] matrice = piece.getMatrix();

        for (int matriceLine = 0; matriceLine < matrice.length; matriceLine++) {
            for (int matriceColumn = 0; matriceColumn < matrice[matriceLine].length; matriceColumn++) {
                int matriceValue = piece.getColor(matriceLine, matriceColumn);
                int pieceColumn = matriceColumn + column;
                int pieceLine = matriceLine + line;

                if (matriceValue != Piece.getEmptyPieceValue()) {
                    gameboard[pieceLine][pieceColumn] = 0;
                    Log.d("REMOVE", "I will remove" + Utils.formatPosition(pieceLine, pieceColumn) + " from gameboard");
                }

            }
        }
    }
}
