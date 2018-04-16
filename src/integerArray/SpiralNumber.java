package integerArray;

import java.util.Arrays;

/**
 * Given a number n, print out 1 to n^2 in spiral form. 
 * 
 * E.g. given 3, the printout should be like
 * 
 * 7 6 5
 * 8 1 4
 * 9 2 3
 * 
 * @author ruifengm
 * @since 2018-Apr-16
 *
 */
public class SpiralNumber {
	
	private static int[][] fillSpiralMatrix(int n) {
		int[][] matrix = new int[n][n]; 
		int k = 1, i, j; 
		// determine starting position
		if (n%2 != 0) {
			i = n/2; 
			j = n/2;
		} else {
			i = n/2 - 1; 
			j = n/2 - 1;
		}
		// initialize moving direction (downwards)
		matrix[i][j] = k++;
		if (n == 1) return matrix; 
		matrix[++i][j] = k++;  
		// proliferation
		while (k<=n*n) {
			while (j<n-1 && matrix[i][j+1] != 0 && i<n-1 && matrix[i+1][j] == 0) matrix[++i][j] = k++;// moving downwards
			while (i>0 && matrix[i-1][j] != 0 && j<n-1 && matrix[i][j+1] == 0) matrix[i][++j] = k++; // moving rightwards
			while (j>0 && matrix[i][j-1] != 0 && i>0 && matrix[i-1][j] == 0) matrix[--i][j] = k++;// moving upwards
			while (i<n-1 && matrix[i+1][j] != 0 && j>0 && matrix[i][j-1] == 0) matrix[i][--j] = k++; // moving leftwards
		}
		return matrix;
	}
	
	public static void main(String[] args) {
		int[][] matrix = fillSpiralMatrix(12);
		//for (int[] row: matrix) System.out.println(Arrays.toString(row));
		for (int[] row: matrix) {
			for (int i=0; i<row.length; i++) System.out.printf("%-4d", row[i]);
			System.out.println("");
		}
	}

}
