package dynamicProgramming;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import utils.FunIntAlgorithm;

/**
 * Given a set of non-negative integers, determine if there is a subset of the given set with sum
 * equal to a given value. 
 * 
 * E.g. given a set {1, 2, 4, 6} and sum value of 5, the answer is yes (1 + 4 = 5). 
 * 
 * The subset seeker problem can be referenced if we are keen to know which subsets produce 
 * the given sum value. 
 * 
 * @author ruifengm
 * @since 2018-Apr-27
 * 
 * https://www.geeksforgeeks.org/dynamic-programming-subset-sum-problem/
 */
public class SubsetOfFixedSum extends FunIntAlgorithm {
	
	/**
	 * Let A denote an array of n non-negative integers, S be the given sum value, and Sol(A(n), S) 
	 * be a boolean value that denotes whether there exists such a subset whose elements sum to S. 
	 * Below recursive pattern can be identified. 
	 * 		Sol(A(n), S) = Sol(A(n-1), S)               last element not included in the subset
	 * 					   ||
	 *                     Sol(A(n-1), S-A[n-1])        last element included in the subset
	 * The time complexity is exponential as the problem is NP-complete. 
	 */
	private static boolean recursiveCheckSubsetSum(int[] a, int size, int sum) {
		if (size == 0 || sum == 0) return sum == 0; 
		if (sum < 0) return false;
		return recursiveCheckSubsetSum(a, size-1, sum) || recursiveCheckSubsetSum(a, size-1, sum - a[size-1]);
	}
	private static boolean recursiveCheckSubsetSumDriver(int[] a, int sum) {
		return recursiveCheckSubsetSum(a, a.length, sum);
	}
	
	/**
	 * Optimize the recursion using a DP lookup table built through top down memoization. 
	 */
	private static Boolean recursiveCheckSubsetSumDPMemo(int[] a, int size, int sum, Boolean[][] table) {
		if (table[size][sum] != null) return table[size][sum];
		else {
			if (size == 0 || sum < 0) table[size][sum] = new Boolean(sum == 0);
			else {
				if (sum < a[size-1]) table[size][sum] = recursiveCheckSubsetSumDPMemo(a, size-1, sum, table);
				else table[size][sum] = recursiveCheckSubsetSumDPMemo(a, size-1, sum, table) || 
						           recursiveCheckSubsetSumDPMemo(a, size-1, sum-a[size-1], table);
			}
			return table[size][sum];
		}
	}
	private static boolean recursiveCheckSubsetSumDPMemoDriver(int[] a, int sum) {
		Boolean[][] DPLookUp = new Boolean[a.length+1][sum+1];
		return recursiveCheckSubsetSumDPMemo(a, a.length, sum, DPLookUp).booleanValue();
	}
	
	/**
	 * Use a DP lookup table built through bottom up tabulation. 
	 */
	private static boolean iterativeCheckSubsetSumDPTabu(int[] a, int sum) {
		boolean[][] DPLookUp = new boolean[a.length+1][sum+1];
		// base state
		for (int i=0; i<=a.length; i++) DPLookUp[i][0] = true; 
		for (int i=1; i<=sum; i++) DPLookUp[0][i] = false; 
		// proliferation
		for (int i=1; i<=a.length; i++) {
			for (int j=1; j<=sum; j++) {
				if (j < a[i-1]) DPLookUp[i][j] = DPLookUp[i-1][j]; 
				else DPLookUp[i][j] = DPLookUp[i-1][j] || DPLookUp[i-1][j-a[i-1]];
			}
		}
		return DPLookUp[a.length][sum];
	}
	
	@FunctionalInterface
	protected interface IntArrayToBooleanFunction {
		boolean apply(int[] array, int sum) throws Exception;  
	}
	
    protected static void runIntArrayFuncAndCalculateTime(String message, IntArrayToBooleanFunction intArrFunc, int[] array, int sum) throws Exception {
    	long startTime = System.nanoTime();
    	System.out.printf("%-70s%s\n", message, String.valueOf(intArrFunc.apply(array, sum)));
    	long endTime = System.nanoTime();
    	long totalTime = new Long(TimeUnit.MICROSECONDS.convert(endTime - startTime, TimeUnit.NANOSECONDS));
    	DecimalFormat formatter = new DecimalFormat("#,###");
    	System.out.printf("%-70s%s\n\n", "Function execution time in micro-seconds: ", formatter.format(totalTime));
    }
    
	public static void main(String[] args) {
		int[] intArray = genRanIntArr(28, 0, 9);
		int sum = 98; 
		System.out.println("Welcome to the rabbit hole of subset sums!\n"
				+ "The integer set is \n" + Arrays.toString(intArray) + "\n"
				+ "The sum value is " + sum + ".\n"); 
		
		try {
			runIntArrayFuncAndCalculateTime("[Recursion][Exponential]         Subset exists? ", (int[] a, int s) -> recursiveCheckSubsetSumDriver(a, s), intArray, sum);
			runIntArrayFuncAndCalculateTime("[Recursion][DP Memo][O(sum*n)    Subset exists? ", (int[] a, int s) -> recursiveCheckSubsetSumDPMemoDriver(a, s), intArray, sum);
			runIntArrayFuncAndCalculateTime("[Iteration][DP Tabu][O(sum*n)    Subset exists? ", (int[] a, int s) -> iterativeCheckSubsetSumDPTabu(a, s), intArray, sum);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("All rabbits gone.");
	}

}
