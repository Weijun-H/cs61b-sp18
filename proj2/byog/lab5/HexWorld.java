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
    private static final int WIDTH = 60;
    private static final int HEIGHT = 60;

    private static class Position{
        int X,Y;
        Position(int x, int y){
            X = x;
            Y = y;
        }
    }
    public void addHexagon (TETile[][] world, Position p, int side, TETile t) {
        int total = side + (side - 1) * 2;
        int patternX, patternY;
        patternX = p.X;
        patternY = p.Y;
        for (int i = 0; i < side; i++) {
            patternX = p.X - i;
            patternY = p.Y + i;
            int startX = patternX;
            for (int j = 0; j < side + i * 2; j++) {
                world[startX][patternY] = t;
                startX++;
            }
        }
        for (int i = 0; i < side; i++) {
            patternY++;
            int startX = patternX;
            for (int j = side + (side - 1 - i) * 2; j > 0; j--) {
                world[startX][patternY] = t;
                startX++;
            }
            patternX++;
        }
    }

    public static void main(String[] args) {
        // initialize the tile rendering engine with a window of size WIDTH x HEIGHT
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);

        // initialize tiles
        TETile[][] world = new TETile[WIDTH][HEIGHT];
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j <HEIGHT ; j++) {
                world[i][j] = Tileset.NOTHING;
            }
        }

        HexWorld HW = new HexWorld();
        Position p1 = new Position(15, 15);
        TETile material = Tileset.WALL;
        HW.addHexagon(world, p1, 6, material);

        // draws the world to the screen
        ter.renderFrame(world);
    }
}
