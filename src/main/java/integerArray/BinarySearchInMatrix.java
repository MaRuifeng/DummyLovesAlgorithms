package integerArray;

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

import utils.FunIntAlgorithm;

/**
 * Write an efficient algorithm that searches for a value in an m x n matrix.
 * This matrix has the following properties:
 * - Integers in each row are sorted from left to right.
 * - The first integer of each row is greater than the last integer of the previous row.
 * 
 * Target: O(log(n) + log(m)) in time
 * 
 * [Variation]
 * Write an efficient algorithm that searches for a value in an m x n matrix, return the occurrence of it.
 * This matrix has the following properties:
 * - Integers in each row are sorted from left to right.
 * - Integers in each column are sorted from up to bottom.
 * - No duplicate integers in each row or column.
 * 
 * @author ruifengm
 * @since 2018-Jun-10
 * 
 * https://www.lintcode.com/problem/search-a-2d-matrix/description
 * https://www.lintcode.com/problem/search-a-2d-matrix-ii/description
 */

public class BinarySearchInMatrix extends FunIntAlgorithm {
	
	static class Location {
		int rowIdx, colIdx; 
		public Location(int r, int c) {
			this.rowIdx = r; 
			this.colIdx = c;
		}
	}
	private static Location binarySearchInMatrix(int[][] matrix, int target) {
		// apply binary search on the first element of each row to look for row entry point
		int rowIdx = -1; 
		
		int rowStart = 0, rowEnd = matrix.length-1;
		while (rowStart <= rowEnd) {
			int rowMid = rowStart + (rowEnd-rowStart)/2;
			if (matrix[rowMid][0] == target) return new Location(rowMid, 0); 
			if (matrix[rowMid][0] > target) rowEnd = rowMid-1;
			else if (matrix[rowMid][matrix[rowMid].length-1] < target) rowStart = rowMid+1;
			else {
				rowIdx = rowMid;
				break;
			}
		}
		if (rowIdx == -1) return new Location(-1, -1);
		
		// apply binary search on row to locate target
		int colStart = 0, colEnd = matrix[rowIdx].length-1;
		while (colStart <= colEnd) {
			int colMid = colStart + (colEnd-colStart)/2; 
			if (matrix[rowIdx][colMid] == target) return new Location(rowIdx, colMid);
			if (matrix[rowIdx][colMid] > target) colEnd = colMid-1; 
			else colStart = colMid+1;
		}
		
		return new Location(-1, -1);
	}
	
	/**
	 * Since all matrix rows and columns are sorted in order, we can start looking from the bottom left
	 * corner to the top right corner. 
	 * - if element > target, rowIdx--
	 * - else if element < target, colIdx++
	 * - else, count++, rowIdx--, colIdx++
	 * 
	 * Time complexity: O(m+n)
	 */
	private static int countInMatrix(int[][] matrix, int target) {
		if (matrix == null || matrix.length == 0) return 0;
		int rowCount = matrix.length, colCount = matrix[0].length; 
		int count = 0, i = rowCount-1, j = 0; 
		while (i>-1 && j<colCount) {
			if (matrix[i][j] > target) i--; 
			else if (matrix[i][j] < target) j++;
			else {
				count++; i--; j++;
			}
		}
		return count;
	}
	
	public static void main(String[] args) {
		int m = 50, n = 60; // row & column
		int[][] matrix = new int[m][n];
		int start = 0, offset = 10, end = start + offset;
		for (int i=0; i<m; i++) {
			int[] array = new int[n]; 
			for (int j=0; j<n; j++) array[j] = ThreadLocalRandom.current().nextInt(start, end);
			quickSort(array, 0, array.length-1);
			matrix[i] = array; 
			start = end; 
			end += offset;
		}
		int target = matrix[ThreadLocalRandom.current().nextInt(0, m)][ThreadLocalRandom.current().nextInt(0, n)];
	
		System.out.println("Welcome to the rabbit hole of binary searches in a matrix!\n"
				+ "The matrix is given as below and the search target is " + target + ".\n"); 
		for (int i=0; i<m; i++) System.out.println(Arrays.toString(matrix[i]));
		
		Location loc = binarySearchInMatrix(matrix, target);
		System.out.println("\nLocation of the target in the matrix:\nrow: " + loc.rowIdx + ", column: " + loc.colIdx);
		
		int[][] sortedMatrix = {
				{1, 3, 5, 7},
				{2, 4, 7, 8}, 
				{3, 5, 9, 10},
		};
		target = 3; 
		System.out.println("\n\nThe sorted matrix is given as below and the search target is " + target + ".\n");
		for (int i=0; i<3; i++) System.out.println(Arrays.toString(sortedMatrix[i]));
		System.out.println("Occurrences of the target in the matrix: " + countInMatrix(sortedMatrix, target));
		
		System.out.println("\nAll rabbits gone.");
	}

}
