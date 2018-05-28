package dynamicProgramming;

import java.text.DecimalFormat;
import java.util.ArrayList;
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
	/**
	 * This isn't a neat solution hence duplicate solutions will be encountered and a hashset is used to avoid them. 
	 * Checkout the better approach below for a neater implementation that won't encounter duplicates. 
	 */
	private static void iterativePrintAllMaxGainSolutionDPTabu(int[] priceList) {
		int size = priceList.length;
		HashSet<Solution>[][] table = new HashSet[size][size]; // DP lookup table
		                                                       // use hashset to avoid duplicate solutions collected
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
	
	/**
	 * Let's try to find a neater solution than above. We can still consider the final cut, but this time we think in a way
	 * that cutting is always done from left to right (cutting sequence doesn't really matter). 
	 * Let n be the length of the rod, and P(n) be the array containing prices for sub-pieces, and Sol(n) is the the maximum gain
	 * for the rod, then there is
	 * 
	 *    Sol(n) = MAX(P[n-1-i] + Sol(i)), for i ranging from n-1 to 0       <-- final cut occurs at position i, hence right part of 
	 *                                                                            the rod remains, yielding price of P[n-1-i].
	 *                                                                            
	 * Another way to interpret this is that a sub-piece of the rod can be of size 1 to n, and for each of them, firstly we cut the rod
	 * to obtain that sub-piece and then cut the remaining rod in a way that the local maximum gain can be obtained for that particular 
	 * sub-piece. Then we obtain the global optimal from these local optimal points (greedy).
	 * 
	 * This neater solution has a much lower space complexity, and a time complexity of O(n^2) when DP is applied, whereas above
	 * solution has a time complexity of O(n^3) when DP is applied. 
	 */
	private static int recursiveFindMaxGainByCut(int[] priceList, int rodLen) {
		if (rodLen == 0) return 0; 
		int max = Integer.MIN_VALUE;
		for (int i=rodLen-1; i>=0; i--) { // look up all possible final cut positions
			max = Math.max(max, priceList[rodLen-1-i] + recursiveFindMaxGainByCut(priceList, i));
		}
		return max;
	}
	private static int recursiveFindMaxGainByCut(int[] priceList) {
		return recursiveFindMaxGainByCut(priceList, priceList.length);
	}
	
	/**
	 * We try to optimize the above solution with DP memoization.
	 */
	private static int recursiveFindMaxGainByCutDPMemo(int[] priceList, int rodLen, int[] table) {
		if (table[rodLen] != -1) return table[rodLen];
		else {
			if (rodLen == 0) table[rodLen] = 0;
			else {
				int max = Integer.MIN_VALUE;
				for (int i=rodLen-1; i>=0; i--) { // look up all possible final cut positions
					max = Math.max(max, priceList[rodLen-1-i] + recursiveFindMaxGainByCutDPMemo(priceList, i, table));
				}
				table[rodLen] = max;
			}
			return table[rodLen];
		}
	}
	private static int recursiveFindMaxGainByCutDPMemo(int[] priceList) {
		int[] DPLookUp = new int[priceList.length+1];
		Arrays.fill(DPLookUp, -1);
		int res = recursiveFindMaxGainByCutDPMemo(priceList, priceList.length, DPLookUp);
		// System.out.println(Arrays.toString(DPLookUp));
		return res;
	}
	
	/**
	 * We try to optimize above solution with DP tabulation.
	 */
	private static int iterativeFindMaxGainByCutDPTabu(int[] priceList) {
		int size = priceList.length; 
		int[] table = new int[size+1]; // DP lookup table
		table[0] = 0; // base case
		// fill up the DP lookup table from start to end
		for (int i=1; i<=size; i++) {
			int max = Integer.MIN_VALUE;
			for (int j=i-1; j>=0; j--) { // i is the current rod length
				max = Math.max(max, priceList[i-1-j] + table[j]);
			}
			table[i] = max;
		}
		// System.out.println(Arrays.toString(table));
		return table[size];
	}
	
	/**
	 * We try to return a complete solution that tells how to cut the rod via the DP tabulation method. 
	 * Note that there might be multiple ways of cutting which return the same max gain. 
	 * We try to find the one that involves the least number of cuts. 
	 */
	private static Solution iterativeFindMaxGainSolutionByCutDPTabu(int[] priceList) {
		int size = priceList.length; 
		Solution[] table = new Solution[size+1]; // DP lookup table
		table[0] = new Solution("", 0, 0); // base case
		// fill up the DP lookup table from start to end
		for (int i=1; i<=size; i++) {
			Solution optimal = new Solution("", 0, Integer.MIN_VALUE);
			for (int j=i-1; j>=0; j--) { // i is the current rod length
				String solStr = " "; 
				for (int k=j+1; k<=i; k++) solStr += String.valueOf(k) + " ";
				int solNumOfCuts = table[j].numOfCuts; 
				if (!table[j].cutPoints.equals("")) {
					solStr = table[j].cutPoints + "|" + solStr;
					solNumOfCuts += 1;
				} 
				Solution sol = new Solution(solStr, solNumOfCuts, table[j].gain + priceList[i-1-j]); 
				if (optimal.gain < sol.gain) optimal = sol; 
				if (optimal.gain == sol.gain && optimal.numOfCuts > sol.numOfCuts) optimal = sol;
			}
			table[i] = optimal;
		}
		return table[size];
	}
	
	@SuppressWarnings("unchecked")
	private static void iterativePrintAllMaxGainSolutionByCutDPTabu(int[] priceList) {
		int size = priceList.length; 
		ArrayList<Solution>[] table = new ArrayList[size+1]; // DP lookup table
		table[0] = new ArrayList<>();
		table[0].add(new Solution("", 0, 0)); // base case
		// fill up the DP lookup table from start to end
		for (int i=1; i<=size; i++) {
			Solution optimal = new Solution("", 0, Integer.MIN_VALUE);
			table[i] = new ArrayList<>();
			table[i].add(optimal);
			for (int j=i-1; j>=0; j--) { // i is the current rod length
				for (Solution prevSol: table[j]) {
					String solStr = " "; 
					for (int k=j+1; k<=i; k++) solStr += String.valueOf(k) + " ";
					int solNumOfCuts = prevSol.numOfCuts; 
					if (!prevSol.cutPoints.equals("")) {
						solStr = prevSol.cutPoints + "|" + solStr;
						solNumOfCuts += 1;
					} 
					Solution sol = new Solution(solStr, solNumOfCuts, prevSol.gain + priceList[i-1-j]); 
					if (optimal.gain < sol.gain) {
						optimal = sol; 
						table[i].clear(); // reset
					}
					if (optimal.gain == sol.gain) table[i].add(sol);
				}

			}
		}
		System.out.println("\n/************* List of all rod cutting solutions that yield the max gain *************/");
		System.out.println("Size of the list: " + table[size].size() + "\n");
		if (table[size].size() < 40) { // do not print if the list is too long
			for (Solution sol: table[size]) {
		    	System.out.println("Rod cutting points: " + sol.cutPoints);
		    	System.out.println("Max gain: " + sol.gain);
		    	System.out.println("Number of cuts: " + sol.numOfCuts);
		    	System.out.println();
			}
		}
	}
	
	/**
	 * We try to count the optimal solutions via the DP tabulation method. 
	 */
	static class SolutionCount {
		int count;
		int gain; 
		public SolutionCount(int count, int gain) {
			this.count = count;
			this.gain = gain;
		}
	}
	private static void iterativeCountAllMaxGainSolutionByCutDPTabu(int[] priceList) {
		int size = priceList.length; 
		SolutionCount[] table = new SolutionCount[size+1]; // DP lookup table
		table[0] = new SolutionCount(1, 0); // base case
		// fill up the DP lookup table from start to end
		for (int i=1; i<=size; i++) {
			SolutionCount optimal = new SolutionCount(1, Integer.MIN_VALUE);
			for (int j=i-1; j>=0; j--) { // i is the current rod length
				SolutionCount solCount = new SolutionCount(table[j].count, table[j].gain + priceList[i-1-j]); 
				if (optimal.gain < solCount.gain) optimal = solCount; // reset
				else if (optimal.gain == solCount.gain) optimal.count += solCount.count;
			}
			table[i] = optimal;
		}
		System.out.println("\n/************* Count of all rod cutting solutions that yield the max gain *************/");
    	System.out.println("Total count: " + table[size].count);
    	System.out.println("Max gain: " + table[size].gain);
    	System.out.println();
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
		int[] priceList = {1, 2, 6, 1, 2, 35, 42, 8, 3, 3, 5, 3, 4, 7, 20, 4, 2, 4, 3, 4};
//		int[] priceList = {1, 2, 6, 1, 2, 42, 35, 8, 3, 3, 5, 3, 4, 7, 20, 4, 2, 4, 3, 4};
//		int[] priceList = genRanIntArr(2000, 1, 6);

		System.out.println("Welcome to the rabbit hole of rod cutters!\n"
				+ "The length of the rod is " + priceList.length + ".\n"
				+ "And the prices list is " + Arrays.toString(priceList) + ".\n"
				+ "Find maximum gain by cutting the rod and selling all pieces.\n"); 
		
		try {
			runIntArrayFuncAndCalculateTime("[Recursive]               Max gain:" , 
					(int[] a) -> recursiveFindMaxGain(a), priceList);
			runIntArrayFuncAndCalculateTime("[Recursive][DP Memo]      Max gain:" , 
					(int[] a) -> recursiveFindMaxGainDPMemo(a), priceList);	
			runIntArrayFuncAndCalculateTime("[Iterative][DP Tabu]      Max gain:" , 
					(int[] a) -> iterativeFindMaxGainDPTabu(a), priceList);	
			runFuncAndCalculateTime("[Iterative][DP Tabu]      Least rod cuts for max gain:" , 
					(int[] a) -> iterativeFindMaxGainSolutionDPTabu(a), priceList);	
			runIntArrayFuncAndCalculateTime("[Recursive][Neater Approach]   Max gain:" , 
					(int[] a) -> recursiveFindMaxGainByCut(a), priceList);
			runIntArrayFuncAndCalculateTime("[Recursive][Neater Approach][DP Memo]   Max gain:" , 
					(int[] a) -> recursiveFindMaxGainByCutDPMemo(a), priceList);
			runIntArrayFuncAndCalculateTime("[Iterative][Neater Approach][DP Tabu]   Max gain:" , 
					(int[] a) -> iterativeFindMaxGainByCutDPTabu(a), priceList);
			runFuncAndCalculateTime("[Iterative][Neater Approach][DP Tabu]      Least rod cuts for max gain:" , 
					(int[] a) -> iterativeFindMaxGainSolutionByCutDPTabu(a), priceList);	
			
			iterativePrintAllMaxGainSolutionDPTabu(priceList);
			
			System.out.println("The neater approach...");
			iterativePrintAllMaxGainSolutionByCutDPTabu(priceList);
			iterativeCountAllMaxGainSolutionByCutDPTabu(priceList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("All rabbits gone.");
	}

}
