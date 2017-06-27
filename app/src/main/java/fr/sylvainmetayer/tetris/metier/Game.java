package fr.sylvainmetayer.tetris.metier;

import android.util.Log;

import java.util.ArrayList;
import java.util.Random;

import fr.sylvainmetayer.tetris.MainActivity;
import fr.sylvainmetayer.tetris.R;
import fr.sylvainmetayer.tetris.metier.pieces.Piece_S;
import fr.sylvainmetayer.tetris.utils.Utils;

public class Game {
    private int[][] gameboard;
    private ArrayList<Piece> pieces;
    private boolean pause;
    private int score;

    public Game(ArrayList<Piece> pieces, int nb_lines, int nb_columns) {

        this.pieces = pieces;
        gameboard = new int[nb_lines][nb_columns];
        this.pause = true;
        this.score = 0;

        resetGame();

        for (Piece piece : pieces) {
            int column = piece.getStartColumn();
            int line = piece.getStartLine();
            int[][] matrice = piece.getMatrice();
            Log.d("INIT", piece.toString());
            Log.d("INIT", "The piece start at " + Utils.formatPosition(line, column));

            for (int matriceLineIterator = 0; matriceLineIterator < matrice.length; matriceLineIterator++) {
                for (int matriceColumnIterator = 0; matriceColumnIterator < matrice[matriceLineIterator].length; matriceColumnIterator++) {
                    int matriceValue = matrice[matriceLineIterator][matriceColumnIterator];
                    int pieceColumn = matriceColumnIterator + column;
                    int pieceLine = matriceLineIterator + line;
                    Log.d("INIT", "I will affect the value '" + matriceValue + "' at " + Utils.formatPosition(pieceLine, pieceColumn));
                    gameboard[pieceLine][pieceColumn] = matriceValue;
                }
            }
        }
        Log.d("INIT_ENDED", logGameboard(gameboard));
    }

    private void resetGame() {
        for (int line = 0; line < gameboard.length; line++) {
            for (int column = 0; column < gameboard[line].length; column++) {
                gameboard[line][column] = 0;
            }
        }
    }

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

    private boolean addPieceToGameBoard(Piece piece) {
        int column = piece.getStartColumn();
        int line = piece.getStartLine();
        int[][] matrice = piece.getMatrice();

        Log.d("ROTATION_ADD", piece.toString());

        for (int matriceLineIterator = 0; matriceLineIterator < matrice.length; matriceLineIterator++) {
            for (int matriceColumnIterator = 0; matriceColumnIterator < matrice[matriceLineIterator].length; matriceColumnIterator++) {
                int matriceValue = matrice[matriceLineIterator][matriceColumnIterator];
                int pieceColumn = matriceColumnIterator + column;
                int pieceLine = matriceLineIterator + line;
                Log.d("ADD_PIECE", "affect value '" + matriceValue + "' at " + Utils.formatPosition(pieceLine, pieceColumn));

                if (gameboard[pieceLine][pieceColumn] != Piece.getEmptyPieceValue() && matriceValue != Piece.getEmptyPieceValue()) {
                    Log.d("ROTATION", "Error :" + Utils.formatPosition(pieceLine, pieceColumn) + " is not empty !");
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
        } else
            createNewPiece(activity);
        if (checkFullLines(lastPiece))
            createNewPiece(activity);
        Log.d("GBOARD", logGameboard(gameboard));
    }

    private void createNewPiece(MainActivity activity) {
        Log.d("PERFORM", "Creation of a new piece");
        // TODO Generate a new random piece

        Random r = new Random();
        int randomColumn = r.nextInt(activity.getResources().getInteger(R.integer.maxColumns) - 1);
        Piece start_piece = new Piece_S(0, randomColumn, activity);

        if (!addPieceToGameBoard(start_piece)) // Can't add piece, game over
            endGame();
        pieces.add(start_piece);
    }

    /**
     * This method is used to remove full lines
     *
     * @param piece The current piece
     */
    private boolean checkFullLines(Piece piece) {
        // FIXME TODO
        for (int gameLineIterator = gameboard.length - 1; gameLineIterator >= 0; gameLineIterator--) {
            int cpt = 0;
            for (int gameColumnIterator = 0; gameColumnIterator < gameboard[gameLineIterator].length; gameColumnIterator++) {
                int pieceValue = gameboard[gameLineIterator][gameColumnIterator];
                if (pieceValue != Piece.getEmptyPieceValue())
                    cpt++;
            }

            if (cpt == gameboard[gameLineIterator].length) {
                // Line is full
                for (int gameColumnIterator = 0; gameColumnIterator < gameboard[gameLineIterator].length; gameColumnIterator++) {
                    gameboard[gameLineIterator][gameColumnIterator] = Piece.getEmptyPieceValue();
                    score += cpt;
                }
                moveAllPieceDownAboveThisLine(gameLineIterator);
                if (isPieceNeedToBeReplaced(piece, gameLineIterator))
                    return true;
            }
        }
        return false;
    }

    private boolean isPieceNeedToBeReplaced(Piece piece, int lineThatWasRemoved) {
        int line = piece.getStartLine();
        int[][] matrice = piece.getMatrice();

        for (int matriceLineIterator = 0; matriceLineIterator < matrice.length; matriceLineIterator++) {
            for (int matriceColumnIterator = 0; matriceColumnIterator < matrice[matriceLineIterator].length; matriceColumnIterator++) {
                int pieceLine = matriceLineIterator + line;

                if (lineThatWasRemoved == pieceLine)
                    return true;
            }
        }
        return false;
    }

    private void moveAllPieceDownAboveThisLine(int line) {
        for (int gameLineIterator = 0; gameLineIterator < line; gameLineIterator++) {
            for (int gameColumnIterator = 0; gameColumnIterator < gameboard[gameLineIterator].length; gameColumnIterator++) {
                int pieceValue = gameboard[gameLineIterator][gameColumnIterator];
                gameboard[gameLineIterator][gameColumnIterator] = Piece.getEmptyPieceValue();
                gameboard[gameLineIterator + 1][gameColumnIterator] = pieceValue;
            }
        }
    }

    private void endGame() {
        //Toast.makeText(activity, "Game over", Toast.LENGTH_LONG).show();
        Log.i("END", "GAME OVER");
        this.togglePause();
    }

    private Piece getCurrentPiece() {
        return pieces.get(pieces.size() - 1);
    }

    static String logGameboard(int[][] gameboard) {
        StringBuilder sb = new StringBuilder();
        for (int[] aGameboard : gameboard) {
            for (int anAGameboard : aGameboard) {
                sb.append(anAGameboard).append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    public void togglePause() {
        this.pause = !pause;
    }

    public boolean isPause() {
        return pause;
    }

    public String getScore() {
        return "Score : " + score;
    }

    public void moveLeft(MainActivity activity) {
        if (this.isPause())
            return;

        Piece piece = getCurrentPiece();

        if (!piece.canGoLeft(gameboard))
            return;

        moveColumn(piece, -1);
        piece.left();
        if (checkFullLines(piece))
            createNewPiece(activity);
    }

    private void moveDown(Piece piece) {
        int column = piece.getStartColumn();
        int line = piece.getStartLine();
        int[][] matrice = piece.getMatrice();

        for (int matriceLineIterator = matrice.length - 1; matriceLineIterator >= 0; matriceLineIterator--) {
            for (int matriceColumnIterator = 0; matriceColumnIterator < matrice[matriceLineIterator].length; matriceColumnIterator++) {
                int matriceValue = matrice[matriceLineIterator][matriceColumnIterator];
                int pieceColumn = matriceColumnIterator + column;
                int pieceLine = matriceLineIterator + line;

                gameboard[pieceLine][pieceColumn] = Piece.getEmptyPieceValue();
                gameboard[pieceLine + 1][pieceColumn] = matriceValue;
            }
        }
    }

    public void moveRight(MainActivity activity) {
        if (this.isPause())
            return;

        Piece piece = getCurrentPiece();

        if (!piece.canGoRight(gameboard))
            return;

        moveColumn(piece, 1);
        piece.right();
        if (checkFullLines(piece))
            createNewPiece(activity);
    }

    private void moveColumn(Piece piece, int columnValue) {
        int column = piece.getStartColumn();
        int line = piece.getStartLine();
        int[][] matrice = piece.getMatrice();

        for (int matriceLineIterator = 0; matriceLineIterator < matrice.length; matriceLineIterator++) {
            for (int matriceColumnIterator = 0; matriceColumnIterator < matrice[matriceLineIterator].length; matriceColumnIterator++) {
                int matriceValue = matrice[matriceLineIterator][matriceColumnIterator];
                int pieceColumn = matriceColumnIterator + column;
                int pieceLine = matriceLineIterator + line;

                gameboard[pieceLine][pieceColumn] = 0;
                gameboard[pieceLine][pieceColumn + columnValue] = matriceValue;
            }
        }
    }

    public void rotate(MainActivity activity) {
        if (this.isPause())
            return;

        Piece piece = getCurrentPiece();

        if (!piece.canRotate(gameboard))
            return;

        Log.d("ROTATION", logGameboard(gameboard));
        Log.d("ROTATION", "Etape 1 : Retirer l'ancienne piece du plateau d'entier");
        removePieceFromGameBoard(piece);
        Log.d("ROTATION", "Etape 2 : Changer la matrice de la piece");
        Log.d("ROTATION", logGameboard(gameboard));
        piece.rotate();
        Log.d("ROTATION", "Etape 3 : Ajouter la nouvelle matrice de la piece sur le plateau d'entier");
        addPieceToGameBoard(piece);
        Log.d("ROTATION", logGameboard(gameboard));
        //if (checkFullLines(piece))
        //    createNewPiece(activity);
    }

    private void removePieceFromGameBoard(Piece piece) {
        int column = piece.getStartColumn();
        int line = piece.getStartLine();
        int[][] matrice = piece.getMatrice();

        for (int matriceLineIterator = 0; matriceLineIterator < matrice.length; matriceLineIterator++) {
            for (int matriceColumnIterator = 0; matriceColumnIterator < matrice[matriceLineIterator].length; matriceColumnIterator++) {
                int matriceValue = matrice[matriceLineIterator][matriceColumnIterator];
                int pieceColumn = matriceColumnIterator + column;
                int pieceLine = matriceLineIterator + line;

                if (matriceValue != Piece.getEmptyPieceValue()) {
                    gameboard[pieceLine][pieceColumn] = 0;
                    Log.d("REMOVE", "I will remove" + Utils.formatPosition(pieceLine, pieceColumn) + " from gameboard");
                }

            }
        }
    }
}
