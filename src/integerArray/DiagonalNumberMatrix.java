package integerArray;

import java.util.Arrays;

/**
 * Print a matrix of given size where numbers are diagonally filled up.
 * 
 * @author ruifengm
 * @2018-Apr-16
 */
public class DiagonalNumberMatrix {
	
	private static int[][] printMatrix(int m, int n) {
		int[][] matrix = new int[m][n]; 
		int k=0; 
		int i = 0, j = 0; // bounds for diagonal fillup
		while (k < m*n) {
			if (i == 0 || j == n-1) { // fill up diagonal from top right to bottom left
				while (j>0 && i<m-1) {
					matrix[i][j] = k;
					k++;
					i++; 
					j--; 
				}
				if (j==0 && i<m-1) { // hit first column but not the last row
					matrix[i][j] = k;
					k++;
					i++; // move down
				} else {
					matrix[i][j] = k;
					k++;
					j++; // move right
				}
			}
			if (j == 0 || i == m-1) { // fill up diagonal from bottom left to top right
				while (i>0 && j<n-1) {
					matrix[i][j] = k;
					k++;
					i--; 
					j++; 
				}
				if (i==0 && j<n-1) { // hit first row but not the last column
					matrix[i][j] = k;
					k++;
					j++; // move right
				} else {
					matrix[i][j] = k;
					k++;
					i++; // move down
				}
			}
		}
		return matrix;
	}

	public static void main(String[] args) {
		int[][] matrix = printMatrix(4, 5);
		for (int[] row: matrix) System.out.println(Arrays.toString(row));
	}
}
