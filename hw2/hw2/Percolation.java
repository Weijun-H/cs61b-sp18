package hw2;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;
import java.util.Set;
import java.util.HashSet;

public class Percolation {
    private WeightedQuickUnionUF uf;
    private int N;
    private boolean[] status;
    private boolean percolated = false;

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

    /**
     * open the site (row, col) if it is not open already
     * @param row
     * @param col
     */
    public void open(int row, int col){
        int indexIn1D = xyToIndexIn1D(row, col);

        if (status[indexIn1D]){
            return;
        }
        status[indexIn1D] = true;
        int id = uf.find(indexIn1D);
        if (indexIn1D < N){

        }
        if(N * N - indexIn1D <= N){

        }
    }
    public boolean isOpen(int row, int col)  // is the site (row, col) open?
    public boolean isFull(int row, int col)  // is the site (row, col) full?
    public int numberOfOpenSites()           // number of open sites
    public boolean percolates()              // does the system percolate?
    public static void main(String[] args)   // use for unit testing (not required)
    {

    }
}
