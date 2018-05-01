package dynamicProgramming;

import java.text.DecimalFormat;
import java.util.ArrayList;
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
 * Subset sum problem is a special case of the decision form of the Knapsack Problem where for each item, the
 * weight equals to the value. Hence it's NP-complete and the solution runs in exponential time. 
 * 
 * Though DP can be used to provide a pseudo-polynomial solution which runs substantially faster given the same
 * input, it does not eliminate the prolem's NP-completeness. The time complexity is O(n*sum), where sum is not polynomial
 * in the length of the input to the problem. The length of the sum input to the problem is proportional to the number
 * of bits needed to represent it, which is log(sum). Hence the time complexity is still exponential with 
 * respect to the input length of the problem. 
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
	 * Optimize the recursion using a DP lookup table built through top down memoization, which provides 
	 * a pseudo-polynomial solution.
	 */
	private static Boolean recursiveCheckSubsetSumDPMemo(int[] a, int size, int sum, Boolean[][] table) {
		if (table[size][sum] != null) return table[size][sum];
		else {
			if (size == 0 || sum == 0) table[size][sum] = new Boolean(sum == 0);
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
	
	/**
	 * Let A(n) be an integer array of size n, and its subsets of sum S can be found via below recursive pattern.
	 * To print all subsets of A(n) whose elements sum up to S ==> 
	 * 		              Include element A[n-1]
	 *                        Print all subsets of A(n-1) whose elements sum up to S-A[n-1]
	 *                    Exclude element A[n-1]
	 *                        Print all subsets of A(n-1) whose elements sum up S
	 * Time complexity is exponential due to the NP-complete nature of the problem.
	 */
	private static void recursivePrintSubsetOfSum(ArrayList<Integer> solution, ArrayList<Integer> set, int sum) {
		if (sum == 0) {
			// sum achieved, print solution
			for (Integer i: solution) System.out.print(i + " ");
			System.out.println("");
		}
		else if (set.size() == 0 || sum < 0) {
			// set is depleted or no solution can be found, print nothing
		} else {
			ArrayList<Integer> newSolution = new ArrayList<Integer>();
			newSolution.addAll(solution);
			newSolution.add(set.get(set.size()-1));
			ArrayList<Integer> newSet = new ArrayList<Integer>();
			newSet.addAll(set);
			newSet.remove(set.get(set.size()-1));
			// include last element of set and recur
			recursivePrintSubsetOfSum(newSolution, newSet, sum - set.get(set.size()-1));
			// exclude last element of set and recur
			recursivePrintSubsetOfSum(solution, newSet, sum);
		}
	}
	private static void recursivePrintSubsetOfSumDriver(int[] a, int sum) {
		ArrayList<Integer> solution = new ArrayList<Integer>(); // convert to ArrayList for its elasticity
		ArrayList<Integer> set = new ArrayList<Integer>(); // convert to ArrayList for its elasticity
		for (int i: a) set.add(i);
		recursivePrintSubsetOfSum(solution, set, sum);
	}
	
	/** 
	 * Optimize the recursive print function by eliminating recursions done for subsets that do not produce a given sum. 
	 * This can be controlled via a boolean lookup table constructed via DP memoization. 
	 * DP provides a pseudo-polynomial solution to the problem, whose time complexity varies depending on the numeric values of the input.
	 */
	private static Boolean recursivePrintSubsetOfSumDPMemo(ArrayList<Integer> solution, ArrayList<Integer> set, int sum, Boolean[][] table) {
		if (table[set.size()][sum] != null && table[set.size()][sum] == false) {
			// no solution, print nothing, return boolean check only
			return table[set.size()][sum];
		}
		else {
			if (sum == 0) {
				// sum achieved, print solution
				for (Integer i: solution) System.out.print(i + " ");
				System.out.println("");
				// update DP Lookup table
				table[set.size()][sum] = new Boolean(true);
			}
			else if (set.size() == 0) {
				// set is depleted and no solution found, print nothing
				// update DP Lookup table
				table[set.size()][sum] = new Boolean(false);
			} else {
				ArrayList<Integer> newSolution = new ArrayList<Integer>();
				newSolution.addAll(solution);
				newSolution.add(set.get(set.size()-1));
				ArrayList<Integer> newSet = new ArrayList<Integer>();
				newSet.addAll(set);
				newSet.remove(set.get(set.size()-1));
				if(sum < set.get(set.size()-1)) table[set.size()][sum] = 
						// exclude last element of set and recur
						recursivePrintSubsetOfSumDPMemo(solution, newSet, sum, table); 
				else {
					// include last element of set and recur
					Boolean incl = recursivePrintSubsetOfSumDPMemo(newSolution, newSet, sum - set.get(set.size()-1), table);
					// exclude last element of set and recur
					Boolean excl = recursivePrintSubsetOfSumDPMemo(solution, newSet, sum, table);
					table[set.size()][sum] = incl || excl;
				}
			}
			return table[set.size()][sum];
		}
	}
	private static void recursivePrintSubsetOfSumDPMemoDriver(int[] a, int sum) {
		ArrayList<Integer> solution = new ArrayList<Integer>(); // convert to ArrayList for its elasticity
		ArrayList<Integer> set = new ArrayList<Integer>(); // convert to ArrayList for its elasticity
		for (int i: a) set.add(i);
		Boolean[][] DPLookUp = new Boolean[a.length+1][sum+1];
		recursivePrintSubsetOfSumDPMemo(solution, set, sum, DPLookUp);
	}
	
	/**
	 * Recursively print the subsets by examining the DP lookup table generated through tabulation.
	 */
	private static void printSubsetSumDPTabu(int[] a, int sum) {
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
		ArrayList<Integer> solution = new ArrayList<Integer>();
		recursiveLookupAndPrint(solution, a.length, DPLookUp, sum, a);
	}
	private static void recursiveLookupAndPrint(ArrayList<Integer> solution, int itemIdx, boolean[][] table, int sumIdx, int[] a) {
		if (sumIdx == 0) {
			// sum achieved, print solution
			for (Integer i: solution) System.out.print(i + " ");
			System.out.println("");
		} else if (itemIdx == 0) {} // no solution
		else {
			if (table[itemIdx][sumIdx] == false) return; // no solution
			else {
				ArrayList<Integer> newSolution = new ArrayList<Integer>();
				newSolution.addAll(solution);
				newSolution.add(a[itemIdx-1]);
				if (sumIdx < a[itemIdx-1]) 
					// exclude last element and recur
					recursiveLookupAndPrint(solution, itemIdx - 1, table, sumIdx, a);
				else {
					// include last element and recur
					recursiveLookupAndPrint(newSolution, itemIdx - 1, table, sumIdx - a[itemIdx-1], a);
					// exclude last element and recur
					recursiveLookupAndPrint(solution, itemIdx - 1, table, sumIdx, a);		
				}
			}
		}
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
    
	@FunctionalInterface
	protected interface IntArrayToVoidFunction {
		void apply(int[] array, int sum) throws Exception;  
	}
	
    protected static void runIntArrayFuncAndCalculateTime(String message, IntArrayToVoidFunction intArrFunc, int[] array, int sum) throws Exception {
    	long startTime = System.nanoTime();
    	System.out.printf("%-70s\n", message);
    	intArrFunc.apply(array, sum);
    	long endTime = System.nanoTime();
    	long totalTime = new Long(TimeUnit.MICROSECONDS.convert(endTime - startTime, TimeUnit.NANOSECONDS));
    	DecimalFormat formatter = new DecimalFormat("#,###");
    	System.out.printf("%-70s%s\n\n", "Function execution time in micro-seconds: ", formatter.format(totalTime));
    }
    
	public static void main(String[] args) {
		//int[] intArray = genRanIntArr(20, 10, 19);
		int[] intArray = {1, 2, 4, 5, 3, 7, 8, 6, 9, 10, 11, 14, 15, 14, 12, 16, 17, 18, 19, 20, 21, 22, 23, 995, 1000};
		int sum = 270; 
		System.out.println("Welcome to the rabbit hole of subset sums!\n"
				+ "The integer set is \n" + Arrays.toString(intArray) + "\n"
				+ "The sum value is " + sum + ".\n"); 
		
		try {
			runIntArrayFuncAndCalculateTime("[Recursion][NP-complete]          Subset exists? ", (int[] a, int s) -> recursiveCheckSubsetSumDriver(a, s), intArray, sum);
			runIntArrayFuncAndCalculateTime("[Recursion][DP Memo][O(sum*n)]    Subset exists? ", (int[] a, int s) -> recursiveCheckSubsetSumDPMemoDriver(a, s), intArray, sum);
			runIntArrayFuncAndCalculateTime("[Iteration][DP Tabu][O(sum*n)]    Subset exists? ", (int[] a, int s) -> iterativeCheckSubsetSumDPTabu(a, s), intArray, sum);
			runIntArrayFuncAndCalculateTime("[Recursion][NP-complete]          Subset(s):\n ", (int[] a, int s) -> recursivePrintSubsetOfSumDriver(a, s), intArray, sum);
			runIntArrayFuncAndCalculateTime("[Recursion][DP Memo]              Subset(s):\n ", (int[] a, int s) -> recursivePrintSubsetOfSumDPMemoDriver(a, s), intArray, sum);
			runIntArrayFuncAndCalculateTime("[Iteration][DP Tabu]              Subset(s):\n ", (int[] a, int s) -> printSubsetSumDPTabu(a, s), intArray, sum);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("All rabbits gone.");
	}

}
