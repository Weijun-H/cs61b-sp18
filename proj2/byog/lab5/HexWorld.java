package byog.lab5;
import org.junit.Test;
import static org.junit.Assert.*;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.util.Random;

/**
 * Draws a world consisting of hexagonal regions.
 */
public class HexWorld {
    public void addHexagon (int side) {
        int total = side + (side - 1) * 2;
        for (int i = 0; i < side; i++) {
            for (int j = side - 1 - i; j > 0 ; j--) {
                System.out.print(' ');
            }
            for (int j = 0; j < side + i * 2; j++) {
               System.out.print("X");
            }
            System.out.println();
        }
        for (int i = side; i > 0; i--) {
            for (int j = side - i; j > 0 ; j--) {
                System.out.print(' ');
            }
            for (int j = 0; j < side + (i - 1) * 2; j++) {
                System.out.print("X");
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        HexWorld HM = new HexWorld();
        HM.addHexagon(3);
    }
}
