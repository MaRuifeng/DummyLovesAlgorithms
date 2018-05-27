package dynamicProgramming;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import utils.FunIntAlgorithm;

/**
 * Given a rod of length n inches and an array of prices that contains prices of all pieces 
 * of size smaller than n. 
 * Determine the maximum value obtainable by cutting up the rod and selling the pieces. 
 * 
 * E.g. if the rod is of length 3 and the prices for smaller pieces are given as below, 
 *          Length: 1  2  3
 *          Price:  2  4  2
 *      the maximum income obtainable is 6 which corresponds to cutting the rod into 2 pieces, 
 *      one of size 1 and the other 2, or 3 pieces each of size 1. 
 *      
 * The solution to this problem is analogous to that of the matrix chain multiplication problem. 
 *      
 * @author ruifengm
 * @since 2018-May-27
 * 
 * https://www.geeksforgeeks.org/dynamic-programming-set-13-cutting-a-rod/
 */

public class RodCutter extends FunIntAlgorithm {
	
	/**
	 * Let's consider the final cut.
	 * 
	 * The final cut can occur at any position from 1 to n-1, and rod cutting to the left and the right of the final cut should yield
	 * their maximum values respectively, so as to form the maximum gain after the final cut. 
	 * 
	 * Or...
	 * 
	 * The final cut occurs no where, meaning the rod is not cut at all, the price corresponding to the full rod is the gain.
	 * 
	 * We've found the sub-problem pattern and optimal sub-structure. Let's try to solve the problem recursively. 
	 */
	private static int recursiveFindMaxGain(int[] priceList, int start, int end) {
		if (start == end) return priceList[0]; // can't cut any more, return smallest piece price
		int max = priceList[end-start]; // initially assume the max gain is the price of the uncut rod
		for (int i=start; i<=end-1; i++) {
			int left = recursiveFindMaxGain(priceList, start, i); 
			int right = recursiveFindMaxGain(priceList, i+1, end);
			int gain = left + right; 
			if (max < gain) max = gain;
		}
		return max;
	}
	private static int recursiveFindMaxGain(int[] priceList) {
		return recursiveFindMaxGain(priceList, 0, priceList.length-1);
	}
	
	/**
	 * We try to reduce repeated computations via DP memoization. 
	 */
	private static int recursiveFindMaxGainDPMemo(int[] priceList, int start, int end, int[][] table) {
		if (table[start][end] != 0) return table[start][end]; 
		else {
			if (start == end) table[start][end] = priceList[0]; // can't cut any more, return smallest piece price
			else {
				int max = priceList[end-start]; // initially assume the max gain is the price of the uncut rod
				for (int i=start; i<=end-1; i++) {
					int gain = recursiveFindMaxGainDPMemo(priceList, start, i, table) + 
							recursiveFindMaxGainDPMemo(priceList, i+1, end, table);
					if (max < gain) max = gain;
				}
				table[start][end] = max; 
			}
			return table[start][end];
		}
	}
	private static int recursiveFindMaxGainDPMemo(int[] priceList) {
		int[][] DPLookUp = new int[priceList.length][priceList.length];
		int res = recursiveFindMaxGainDPMemo(priceList, 0, priceList.length-1, DPLookUp);
		// for (int[] row: DPLookUp) System.out.println(Arrays.toString(row));
		return res;
	}
	
	/**
	 * We try to reduce repeated computations via DP tabulation.
	 */
	private static int iterativeFindMaxGainDPTabu(int[] priceList) {
		int size = priceList.length;
		int[][] table = new int[size][size]; // DP lookup table
		// fill up the DP lookup table in a diagonal way
		for (int j=0; j<size; j++) 
			for (int i=j; i>=0; i--) {
				if (i==j) table[i][j] = priceList[0]; 
				else {
					int max = priceList[j-i]; 
					for (int k=i; k<=j-1; k++) {
						int gain = table[i][k] + table[k+1][j]; 
						if (max < gain) max = gain;
					}
					table[i][j] = max;
				}
			}
		// for (int[] row: table) System.out.println(Arrays.toString(row));
		return table[0][size-1];
	}
	
	/**
	 * We try to return a complete solution that tells how to cut the rod via the DP tabulation method. 
	 * Note that there might be multiple ways of cutting which return the same max gain. 
	 * We try to find the one that involves the least number of cuts. 
	 */
	static class Solution {
		String cutPoints; 
		int numOfCuts;
		int gain; 
		public Solution(String cutPoints, int numOfCuts, int gain) {
			this.cutPoints = cutPoints; 
			this.numOfCuts = numOfCuts;
			this.gain = gain;
		}
		/* Override the equals() and hashcode() methods to check content only so as for the this class to be usable in hash set */
		@Override
		public boolean equals(Object obj) {
			if (obj == this) return true; 
			if (! (obj instanceof Solution)) return false;
			Solution sol = (Solution) obj; 
			return this.gain == sol.gain && 
					this.numOfCuts == sol.numOfCuts &&
					Objects.equals(this.cutPoints, sol.cutPoints);
		}
		@Override
		public int hashCode() {
			return Objects.hash(this.cutPoints, this.numOfCuts, this.gain);
		}
	}
	private static Solution iterativeFindMaxGainSolutionDPTabu(int[] priceList) {
		int size = priceList.length;
		Solution[][] table = new Solution[size][size]; // DP lookup table
		// fill up the DP lookup table in a diagonal way
		for (int j=0; j<size; j++) 
			for (int i=j; i>=0; i--) {
				if (i==j) table[i][j] = new Solution(" " + String.valueOf(i+1) + " ", 0, priceList[0]); 
				else {
					String str = " ";
					for (int k=i; k<=j; k++) str += (k+1) + " ";
					Solution optimal = new Solution(str, 0, priceList[j-i]); 
					for (int k=i; k<=j-1; k++) {
						Solution left = table[i][k], right = table[k+1][j];
						Solution sol = new Solution(left.cutPoints + "|" + right.cutPoints, 
								left.numOfCuts + right.numOfCuts + 1, left.gain + right.gain);
						if (optimal.gain < sol.gain) optimal = sol;
						if (optimal.gain == sol.gain && optimal.numOfCuts > sol.numOfCuts) optimal = sol;
					}
					table[i][j] = optimal;
				}
			}
		return table[0][size-1];
	}
	
	@SuppressWarnings("unchecked")
	private static void iterativePrintAllMaxGainSolutionDPTabu(int[] priceList) {
		int size = priceList.length;
		HashSet<Solution>[][] table = new HashSet[size][size]; // DP lookup table
		                                                       // use hashset to avoid identical solutions collected
		// fill up the DP lookup table in a diagonal way
		for (int j=0; j<size; j++) 
			for (int i=j; i>=0; i--) {
				if (i==j) {
					table[i][j] = new HashSet<Solution>();
					table[i][j].add(new Solution(" " + String.valueOf(i+1) + " ", 0, priceList[0])); 
				}
				else {
					String str = " ";
					for (int k=i; k<=j; k++) str += (k+1) + " ";
					Solution optimal = new Solution(str, 0, priceList[j-i]); 
					table[i][j] = new HashSet<Solution>();
					table[i][j].add(optimal);
					for (int k=i; k<=j-1; k++) {
						HashSet<Solution> left = table[i][k], right = table[k+1][j];
						for (Solution lSol: left) {
							for (Solution rSol: right) {
								Solution sol = new Solution(lSol.cutPoints + "|" + rSol.cutPoints, 
										lSol.numOfCuts + rSol.numOfCuts + 1, lSol.gain + rSol.gain);
								if (optimal.gain < sol.gain) {
									optimal = sol;
									table[i][j].clear(); // reset
								}
								if (optimal.gain == sol.gain) table[i][j].add(sol);
							}
						}
					}
				}
			}
		System.out.println("\n/************* List of all rod cutting solutions that yield the max gain *************/");
		System.out.println("Size of the list: " + table[0][size-1].size() + "\n");
		if (table[0][size-1].size() < 40) { // do not print if the list is too long
			for (Solution sol: table[0][size-1]) {
		    	System.out.println("Rod cutting points: " + sol.cutPoints);
		    	System.out.println("Max gain: " + sol.gain);
		    	System.out.println("Number of cuts: " + sol.numOfCuts);
		    	System.out.println();
			}
		}
	}
	
	@FunctionalInterface
	protected interface ArrayToSolutionFunction {
	   Solution apply(int[] a) throws Exception;  
	}
	
    protected static void runFuncAndCalculateTime(String message, ArrayToSolutionFunction func, int[] a) throws Exception {
    	long startTime = System.nanoTime();
    	Solution sol = func.apply(a);
    	System.out.printf("%-70s\n", message);
    	System.out.println("Rod cutting points: " + sol.cutPoints);
    	System.out.println("Max gain: " + sol.gain);
    	System.out.println("Least number of cuts: " + sol.numOfCuts);
    	long endTime = System.nanoTime();
    	long totalTime = new Long(TimeUnit.MICROSECONDS.convert(endTime - startTime, TimeUnit.NANOSECONDS));
    	DecimalFormat formatter = new DecimalFormat("#,###");
    	System.out.printf("%-70s%s\n\n", "Function execution time in micro-seconds: ", formatter.format(totalTime));
    }
	
	public static void main(String[] args) {
		// int rodLength = 3; // rod length is equal to size of the price list
//		int[] priceList = {2};
//		int[] priceList = {2, 4, 2, 4}; 
//		int[] priceList = {1, 5, 8, 9, 10, 17, 17, 20}; 
//		int[] priceList = {3, 5, 8, 9, 10, 17, 17, 20};
//		int[] priceList = {1, 2, 4, 3, 2, 1, 5, 1, 1, 4, 2, 1, 2, 4, 5, 4, 4, 4, 4, 3}; 
//		int[] priceList = {2, 4, 3, 1, 2, 3, 2, 5, 3, 3, 5, 3, 4, 3, 5, 4, 2, 4, 3, 4};
//		int[] priceList = {1, 2, 6, 1, 2, 35, 42, 8, 3, 3, 5, 3, 4, 7, 20, 4, 2, 4, 3, 4};
		int[] priceList = {1, 2, 6, 1, 2, 42, 35, 8, 3, 3, 5, 3, 4, 7, 20, 4, 2, 4, 3, 4};
//		int[] priceList = genRanIntArr(20, 1, 6);

		System.out.println("Welcome to the rabbit hole of rod cutters!\n"
				+ "The length of the rod is " + priceList.length + ".\n"
				+ "And the prices list is " + Arrays.toString(priceList) + ".\n"
				+ "Find maximum gain by cutting the rod and selling all pieces.\n"); 
		
		try {
			runIntArrayFuncAndCalculateTime("[Recursive]               Max gain:" , 
					(int[] a) -> recursiveFindMaxGain(a), priceList);
			runIntArrayFuncAndCalculateTime("[Recursive][DP Memo]      Max gain:" , 
					(int[] a) -> recursiveFindMaxGainDPMemo(a), priceList);	
			runIntArrayFuncAndCalculateTime("[Recursive][DP Tabu]      Max gain:" , 
					(int[] a) -> iterativeFindMaxGainDPTabu(a), priceList);	
			runFuncAndCalculateTime("[Recursive][DP Tabu]      Least rod cuts for max gain:" , 
					(int[] a) -> iterativeFindMaxGainSolutionDPTabu(a), priceList);	
			
			iterativePrintAllMaxGainSolutionDPTabu(priceList);
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("All rabbits gone.");
	}

}
