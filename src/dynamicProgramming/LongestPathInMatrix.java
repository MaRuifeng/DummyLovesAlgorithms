package dynamicProgramming;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import integerArray.SpiralNumber;
import utils.FunIntAlgorithm;

/**
 * Given an n*n matrix with distinct numbers, find the maximum length path such that 
 * all cells along the path are in increasing order with a difference of 1. 
 * 
 * E.g. for below matrix
 * 		1 2 9
 *      5 3 8
 *      4 5 7 
 * the longest path that meet the constraints is 6 -> 7 -> 8 -> 9. 
 * 
 * @author ruifengm
 * @since 2018-Apr-26
 * 
 * https://www.geeksforgeeks.org/find-the-longest-path-in-a-matrix-with-given-constraints/
 *
 */
public class LongestPathInMatrix extends FunIntAlgorithm {
	
	/**
	 * We can find the longest path by examining all such paths that start from every single cell. 
	 * This is a brute force method. 
	 */
	private static int[] bruteForceFindLongestPath(int[][] matrix) {
		ArrayList<Integer> longestPath = new ArrayList<Integer>();
		ArrayList<Integer> path = new ArrayList<Integer>();
		
		int size = matrix.length;
		for (int i=0; i<size; i++) {
			for (int j=0; j<size; j++) {
				path = new ArrayList<Integer>();
				int m = i, n = j;
				boolean endOfPath = false;
				while (!endOfPath) {
					path.add(matrix[m][n]);
					if (n>0 && (matrix[m][n-1] - matrix[m][n]) == 1) { // check left
						n--;
						continue; 
					}
					if (n<size-1 && (matrix[m][n+1] - matrix[m][n]) == 1) { // check right
						n++; 
						continue; 
					}
					if (m>0 && (matrix[m-1][n] - matrix[m][n]) == 1) { // check top
						m--;
						continue; 
					}
					if (m<size-1 && (matrix[m+1][n] - matrix[m][n]) == 1) { // check below
						m++; 
						continue; 
					}
					endOfPath = true; 
				}
				if (longestPath.size() < path.size()) {
					longestPath.removeAll(longestPath);
					longestPath.addAll(path);
				}	
			}
		}
		int[] res = new int[longestPath.size()];
		for (int i=0; i<longestPath.size(); i++) res[i] = longestPath.get(i);
		return res;
	}
	
	/**
	 * Repeated computations can be easily spotted in above brute force method. 
	 * A DP lookup table can be constructed in bottom up manner. If the next cell in the path is found 
	 * for a cell, we just need to check with the lookup table and see whether that next cell already 
	 * has its path. If yes, add current cell to its head to from the path for current cell. 
	 */
	
	private static int[] dpFindLongestPath(int[][] matrix) {
		Queue<Integer> longestPath = new LinkedList<Integer>();
		Queue<Integer> path = new LinkedList<Integer>();
		HashMap<Integer, Queue<Integer>> table = new HashMap<Integer, Queue<Integer>>();
		
		int size = matrix.length;
		for (int i=0; i<size; i++) {
			for (int j=0; j<size; j++) {
				path = new LinkedList<Integer>();
				int m = i, n = j;
				boolean endOfPath = false;
				while (!endOfPath) {
					path.add(matrix[m][n]);
					if (n>0 && (matrix[m][n-1] - matrix[m][n]) == 1) { // check left
						if (table.containsKey(matrix[m][n-1])) {
							path.addAll(table.get(matrix[m][n-1]));
							break;
						} else {
							n--;
							continue; 
						}
					}
					if (n<size-1 && (matrix[m][n+1] - matrix[m][n]) == 1) { // check right
						if (table.containsKey(matrix[m][n+1])) {
							path.addAll(table.get(matrix[m][n+1]));
							break;
						} else {
							n++; 
							continue; 
						}
					}
					if (m>0 && (matrix[m-1][n] - matrix[m][n]) == 1) { // check top
						if (table.containsKey(matrix[m-1][n])) {
							path.addAll(table.get(matrix[m-1][n]));
							break;
						} else {
							m--;
							continue; 
						}
					}
					if (m<size-1 && (matrix[m+1][n] - matrix[m][n]) == 1) { // check below
						if (table.containsKey(matrix[m+1][n])) {
							path.addAll(table.get(matrix[m+1][n]));
							break;
						} else {
							m++; 
							continue; 
						}
					}
					endOfPath = true; 
				}
				table.put(matrix[i][j], path);
				if (longestPath.size() < path.size()) {
					longestPath.removeAll(longestPath);
					longestPath.addAll(path);
				}	
			}
		}
		int len = longestPath.size();
		int[] res = new int[len];
		for (int i=0; i<len; i++) res[i] = longestPath.poll();
		return res;
	}
	

	
	
	public static void main(String[] args) {
		//int[][] matrix = {{1, 2, 9}, {5, 3, 8}, {4, 6, 7}};
		//int[][] matrix = {{1, 2, 3}, {8, 9, 4}, {7, 6, 5}};
		//int[][] matrix = {{1, 2, 9, 12, 19}, {5, 3, 8, 15, 13}, {4, 6, 7, 14, 17}, {10, 11, 22, 23, 20}, {18, 16, 21, 24, 25}};
		int[][] matrix = SpiralNumber.fillSpiralMatrix(50);
		System.out.println("Welcome to the rabbit hole of longest paths in a matrix!\n"
				+ "The matrix of distinct integers is \n"); 
		for (int[] a: matrix) System.out.println(Arrays.toString(a));
		
		try {
			runIntMatrixFuncAndCalculateTime("[Brute Force]        Longest path:", (int[][] m) -> bruteForceFindLongestPath(m), matrix);
			runIntMatrixFuncAndCalculateTime("[DP Tabulation]      Longest path:", (int[][] m) -> dpFindLongestPath(m), matrix);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("All rabbits gone.");
	}

}
