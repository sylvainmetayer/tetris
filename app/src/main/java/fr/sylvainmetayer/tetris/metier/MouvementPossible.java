package fr.sylvainmetayer.tetris.metier;

public interface MouvementPossible {

    boolean canRotate(int[][] gameboard);

    boolean canGoLeft(int[][] gameboard);

    boolean canGoRight(int[][] gameboard);

    boolean canGoDown(int[][] gameboard);

}
