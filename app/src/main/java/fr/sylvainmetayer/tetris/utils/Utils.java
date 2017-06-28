package fr.sylvainmetayer.tetris.utils;

import java.util.Arrays;

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

    /**
     * @param original int[][]
     * @return int[][]
     * @see "https://stackoverflow.com/a/1564856"
     */
    public static int[][] deepCopy(int[][] original) {
        if (original == null) {
            return null;
        }

        final int[][] result = new int[original.length][];
        for (int i = 0; i < original.length; i++) {
            result[i] = Arrays.copyOf(original[i], original[i].length);
            // For Java versions prior to Java 6 use the next:
            // System.arraycopy(original[i], 0, result[i], 0, original[i].length);
        }
        return result;
    }
}
