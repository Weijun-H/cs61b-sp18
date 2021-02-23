package hw2;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;
import java.util.Set;
import java.util.HashSet;

public class Percolation {
    private WeightedQuickUnionUF uf;
    private int N;
    private boolean[] status;
    private boolean percolated = false;
    private Set<Integer> topFullIDs = new HashSet<>();
    private Set<Integer> bottomFullIDs = new HashSet<>();

    /* Returns 1D-array index corresponding to the given grid */
    private int xyToIndexIn1D(int row, int col) {
        return N * row + col;
    }

    /**
     * create N-by-N grid, with all sites initially blocked
     * @param N the number of rows/columns
     */
    public Percolation(int N){
        if (N <= 0){
            throw new java.lang.IllegalArgumentException();
        }
        uf = new WeightedQuickUnionUF(N * N);
        status = new boolean[N * N];
        for (int i = 0; i < N * N; i++) {
            status[i] = false;
        }
        this.N = N;
    }

    private boolean isInvalidIndex(int row, int col){
        if (row < 0 || row >= N || col < 0 || col >= N){
            return true;
        }
        return false;
    }

    /**
     * open the site (row, col) if it is not open already
     * @param row
     * @param col
     */
    public void open(int row, int col){
        if (isInvalidIndex(row, col)){
            throw new IndexOutOfBoundsException(
                    "Invalid arguments given: row = " + row + "col = " + col + "."
            );
        }
        int indexIn1D = xyToIndexIn1D(row, col);

        // Update the grid's condition if it's not already open
        if (status[indexIn1D]){
            return;
        }
        status[indexIn1D] = true;
        N += 1;
        int id = uf.find(indexIn1D);
        if (indexIn1D < N){
            topFullIDs.add(id);
        }
        if(N * N - indexIn1D <= N){
            bottomFullIDs.add(id);
        }

        updateConnection(row, col, row - 1, col);
        updateConnection(row, col, row, col + 1);
        updateConnection(row, col, row + 1, col);
        updateConnection(row, col, row, col - 1);

        int newId = uf.find(indexIn1D);
        if (topFullIDs.contains(newId) && bottomFullIDs.contains(newId)){
            percolated = true;
        }

    }

    /**
     * Update connection of a grid at (row, col) and (neighborRow, neighborCol),
     * and updates full conditions of them
     * @param row
     * @param col
     * @param neighborRow
     * @param neighborCol
     */
    private void updateConnection(int row, int col, int neighborRow, int neighborCol){

        int indexIn1D = xyToIndexIn1D(row, col);
        if (!isInvalidIndex(neighborRow, neighborCol) && isOpen(neighborRow, neighborCol)){

            // Union grid and neighbor's Set
            int id = uf.find(indexIn1D);
            int neighborIndex1D = xyToIndexIn1D(neighborRow, neighborCol);
            int oldId = uf.find(neighborIndex1D);
            uf.union(indexIn1D, neighborIndex1D);
            int newId = uf.find(neighborIndex1D);

            // Update full from top conditions
            if (topFullIDs.contains(id)){
                topFullIDs.remove(id);
                topFullIDs.add(newId);
            }
            if (topFullIDs.contains(oldId)) {
                topFullIDs.remove(oldId);
                topFullIDs.add(newId);
            }

            // Update full from bottom conditions
            if (bottomFullIDs.contains(id)){
                bottomFullIDs.remove(id);
                bottomFullIDs.add(newId);
            }
            if (bottomFullIDs.contains(oldId)){
                bottomFullIDs.remove(id);
                bottomFullIDs.add(newId);
            }
        }
    }

    /**
     * Checks is the site (row, col) open?
     * @param row
     * @param col
     * @return
     */
    public boolean isOpen(int row, int col){
        if (isInvalidIndex(row, col)){
            throw new IndexOutOfBoundsException(
                    "Invalid arguments given: row = " + row + "col = " + col + "."
            );
        }
        int indexIn1D = xyToIndexIn1D(row, col);
        return status[indexIn1D];
    }

    /**
     * Check is the site (row, col) full
     * @param row
     * @param col
     * @return
     */
    public boolean isFull(int row, int col)  {
        if (isInvalidIndex(row, col)){
            throw new IndexOutOfBoundsException(
                    "Invalid arguments given: row = " + row + "col = " + col + "."
            );
        }
        int indexIn1D = xyToIndexIn1D(row, col);
        return topFullIDs.contains(uf.find(indexIn1D));
    }

    /**
     * @return number of open sites
     */
    public int numberOfOpenSites(){
        return N;
    }

    /**
     * @return status of the system whether it percolates or not
     */
    public boolean percolates(){
        return percolated;
    }
    public static void main(String[] args)   // use for unit testing (not required)
    {

    }
}
