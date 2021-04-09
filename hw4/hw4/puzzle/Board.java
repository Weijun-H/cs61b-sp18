package hw4.puzzle;

import edu.princeton.cs.algs4.Queue;

public class Board implements WorldState{
    private int N;
    private int[][] tiles;
    private int manhattan = 0;

    /**
     * Constructs a board from an N-by-N array of tiles where
     * tiles[i][j] = tile at row i, column j
     * @param tiles
     */
    public Board(int [][] tiles){
        this.N = tiles[0].length;
        this.tiles = new int[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                this.tiles[i][j] = tiles[i][j];
                if (tiles[i][j] == 0) {
                    continue;
                }
                if (this.tiles[i][j] != i * N + j + 1) {
                    int xpos = (tiles[i][j] - 1) / N;
                    int ypos = (tiles[i][j] - 1) % N;
                    manhattan = manhattan + Math.abs(xpos - i) + Math.abs(ypos - j);
                }
            }
        }
    }

    /**
     * Returns value of tile at row i, column j (or 0 if blank)
     */
    public int tileAt(int i, int j){
        if (i < 0 || i >= N || j < 0 || j >= N) {
            throw new java.lang.IndexOutOfBoundsException();
        }
        return tiles[i][j];
    }

    /**
     * Returns the board size N
     * @return
     */
    public int size(){return this.N;}

    /**
     * Returns the neighbors of the current board
     * @return
     */
    @Override
    public Iterable<WorldState> neighbors(){
        Queue<WorldState> neighbors = new Queue<>();
        int size = size();
        int xpos = -1;
        int ypos = -1;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (tileAt(i, j) == 0){         // find the blank
                    xpos = i;
                    ypos = j;
                }
            }
        }
        int[][] tmp = new int[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                tmp[i][j] = tileAt(i, j);
            }
        }
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (Math.abs(-xpos + i) + Math.abs(j - ypos) - 1 == 0) {    // find the neighbors of the blank
                    tmp[xpos][ypos] = tmp[i][j];
                    tmp[i][j] = 0;
                    Board neighbor = new Board(tmp);
                    neighbors.enqueue(neighbor);
                    tmp[i][j] = tmp[xpos][ypos];
                    tmp[xpos][ypos] = 0;
                }
            }
        }
        return neighbors;
    }

    /**
     * Hamming estimate described below
     * @return
     */
    public int hamming(){
        int num = 0;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (i == N - 1 && j == N -1){
                    continue;
                }
                if (tiles[i][j] != i * N + j + 1){
                    num++;
                }
            }
        }
        return num;
    }

    /**
     * Manhattan estimate described below
     * @return
     */
    public int manhattan(){
        int num = 0;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (tiles[i][j] == 0) {
                    continue;
                }
                if (tiles[i][j] != i * N + j + 1) {
                    int xpos = (tiles[i][j] - 1) / N;
                    int ypos = (tiles[i][j] - 1) % N;
                    num = num + Math.abs(xpos - i) + Math.abs(ypos - j);
                }
            }
        }
        return num;
    }

    /**
     * Estimated distance to goal. This method should
     * simply return the results of manhattan() when submitted to
     * Gradescope.
     * @return
     */
    public int estimatedDistanceToGoal(){return manhattan;}

    /**
     * Returns true if this board's tile values are the same
     * position as y's
     * @param y
     * @return
     */
    public boolean equals(Object y){
        if (this == y) {
            return true;
        }
        if (y == null || getClass() != y.getClass()) {
            return false;
        }

        Board board = (Board) y;
        if (this.N != board.N) {
            return false;
        }
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (this.tiles[i][j] != board.tiles[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    public String toString() {
        StringBuilder s = new StringBuilder();
        int N = size();
        s.append(N + "\n");
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                s.append(String.format("%2d ", tileAt(i,j)));
            }
            s.append("\n");
        }
        s.append("\n");
        return s.toString();
    }

}
