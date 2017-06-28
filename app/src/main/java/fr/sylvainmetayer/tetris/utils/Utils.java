package fr.sylvainmetayer.tetris.utils;

/**
 * Created by Sylvain on 09/06/2017.
 */

public class Utils {

    /**
     * This function is used to simplify the display of a Point coordinate
     *
     * @param line:   int
     * @param column: int
     * @return String
     */
    public static String formatPosition(int line, int column) {
        return "Position [" + line + "," + column + "]";
    }
}
