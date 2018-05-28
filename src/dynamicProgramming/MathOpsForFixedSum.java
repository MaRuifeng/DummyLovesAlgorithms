package dynamicProgramming;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import utils.FunIntAlgorithm;

/** 
 * Given an array of integers and fixed another K, apply +/- operations to the array items such that the result is K. 
 * State so if no solution can be found. 
 * 
 * E.g. for {2, 3, 4} and 1, the solution is 2+3-4=1. 
 * 
 * It is worth noting that this problem also belongs to the NP-complete category because of its analogy to the 
 * subset sum problem. 
 * 
 * @author ruifengm
 * @since 2018-Apr-12
 */

public class MathOpsForFixedSum extends FunIntAlgorithm {
	
	/** 
	 * We can use recursion to determine whether a solution can be found. 
	 * Let A(n) be the array of integers of size n and let K be the fixed sum, and let Sol(A(n), K) be the boolean value
	 * that indicates whether a solution can be found. 
	 * 
	 *     Sol(A(n), K) = Sol(A(n-1), K-A[n-1]) ||       --> last operation is add
	 *                    Sol(A(n-1), K+A[n-1])          --> last operation is subtract
	 */
	private static boolean recursiveCheckMathOps(int[] a, int size, int sum) {
		if (size == 1) {
			if (a[size-1] == sum || a[size-1] == -sum) return true; 
			else return false; 
		}
		return recursiveCheckMathOps(a, size-1, sum-a[size-1]) || recursiveCheckMathOps(a, size-1, sum+a[size-1]);
	}
	private static boolean recursiveCheckMathOpsDriver(int[] a, int sum) {
		return recursiveCheckMathOps(a, a.length, sum);
	}
	
	/**
	 * We try to optimize the recursive method using a DP memoization lookup table.
	 */
	private static boolean recursiveCheckMathOpsDPMemo(int[] a, int size, int sum, Boolean[][] table, int shift) {
		if (table[size][sum+shift] != null) return table[size][sum+shift];
		else {
			if (size == 1) {
				if (a[size-1] == sum || a[size-1] == -sum) table[size][sum+shift] = true; 
				else table[size][sum+shift] = false ; 
			}
			else table[size][sum+shift] = recursiveCheckMathOpsDPMemo(a, size-1, sum-a[size-1], table, shift) || 
					recursiveCheckMathOpsDPMemo(a, size-1, sum+a[size-1], table, shift);
			return table[size][sum+shift]; 
		}
	}
	private static boolean recursiveCheckMathOpsDPMemoDriver(int[] a, int sum) {
		int absSum = 0; 
		for (int i: a) absSum += Math.abs(i);
		Boolean[][] DPLookUp = new Boolean[a.length+1][absSum*2+1];
		int shift = absSum - sum; // value shift to the right so as to keep table index non-negative
		boolean res = recursiveCheckMathOpsDPMemo(a, a.length, sum, DPLookUp, shift);
		// for (Boolean[] row: DPLookUp) System.out.println(Arrays.toString(row));
		return res;
	}
	
	/**
	 * We try to optimize the recursive method using a DP tabulation lookup table.
	 */
	private static boolean iterativeCheckMathOpsDPTabu(int[] a, int sum) {
		int absSum = 0; 
		for (int i: a) absSum += Math.abs(i);
		boolean[][] table = new boolean[a.length+1][absSum*2+1]; // DP look up table
		int shift = absSum - sum; // value shift to the right so as to keep table index non-negative
		for (int i=1; i<=a.length; i++) {
			for (int j=0; j<=absSum*2; j++) {
				if (i==1) {
					if (j == a[i-1] + shift || j == -a[i-1] + shift) table[i][j] = true;
					else table[i][j] = false; 
				}
				else {
					if (j-a[i-1] >=0 && j-a[i-1] <= absSum*2 
							&& j+a[i-1] >=0 && j+a[i-1] <= absSum*2) 
						table[i][j] = table[i-1][j-a[i-1]] || table[i-1][j+a[i-1]];
				}
			}
		}
		//for (boolean[] row: table) System.out.println(Arrays.toString(row));
		if (table[a.length][sum+shift] == true) { // examine the tabulation table to get one possible solution
			System.out.println("One possible solution: ");
			int arrIdx = a.length, sumIdx = sum + shift;
			while (arrIdx > 0) {
				if (table[arrIdx-1][sumIdx - a[arrIdx-1]] == true) {
					System.out.printf("+(" + a[arrIdx-1] + ") ");
					sumIdx -= a[arrIdx-1];
				}
				else if (table[arrIdx-1][sumIdx + a[arrIdx-1]] == true) {
					System.out.printf("-(" + a[arrIdx-1] + ") ");
					sumIdx += a[arrIdx-1];
				}
				arrIdx--; 
			}
			if (sumIdx == a[0] + shift) System.out.printf("+(" + a[0] + ") ");
			else if (sumIdx == -a[0] + shift) System.out.printf("-(" + a[0] + ") ");
		}
		System.out.println();
		return table[a.length][sum+shift];
	}
	
	/**
	 * Let A(n) be the array of integers of size n and let K be the fixed number, and let Res(A(n)) be the result of current math operations (add and subtract), and
	 * let Ops(n) be an array of 1 and -1 which denotes the math operation needed for each element of A (1 for '+' and -1 for '-').
	 * There is a recursive pattern
	 *       Set     Ops[n-1] = 1
	 *       Check   Res(A(n)) = Res(A(n-1)) + A[n-1] ?= K
	 *       Set     Ops[n-1] = -1
	 *       Check   Res(A(n)) = Res(A(n-1)) - A[n-1] ?= K
	 *       
	 * The problem can be generalized as given an empty character array of size n, each field can only be filled up with 
	 * either '+' or '-', find all possible combinations. 
	 * The total number of possibilities is 2^n. 
	 * Particularly for this problem we can stop searching when a solution is found, so we don't need to traverse through all of the possibilities. 
	 * Use below binary tree for illustration: examine the tree leaves from left to right, when we have found 
	 * a tree leave whose value is either 2 or -2, we know a solution has been found. 
	 * 
	 *                          1
	 *                       +/   \-
	 *         4            -3     5
	 *                    +/  \- +/  \-
	 *         3         -6    0 2    8
	 *
	 *         2
	 * 
	 * If we iteratively find out this tree from top to bottom and do the searching, the time complexity will always be at O(2^N). 
	 * In the recursive call stack, the tree is formed from left to right, so when a solution is found, the rest of the tree won't be built any more. 
	 * This gives three scenarios of different time complexities. 
	 * 
	 * 1)-SUM(A(n))   <  K  SUM(A(n))            -->     random, between O(N) and O(2^N)
	 * 2)K == SUM(A(n))                          -->     best case, O(N)
	 * 3)K == -SUM(A(n))                         -->     worst case O(2^N)
	 * 
	 * The best case and worst case can be swapped depending on whether recursion on addition occurs first or recursion on subtraction occurs first. 
	 */
	private static int recursiveGetMathOps(int[] a, int size, int[] ops, int sum) {
		// base case
		if (size == 1) {
			if (a[size-1] == sum) {
				ops[size-1] = 1; 
				return a[size-1]; 
			}
			else if (a[size-1] == -sum) {
				ops[size-1] = -1; 
				return -a[size-1];
			}
			else return Integer.MIN_VALUE;
		}
		// recur with add
		ops[size-1] = 1; 
		int res = recursiveGetMathOps(a, size-1, ops, sum - a[size-1]);
		if (res != Integer.MIN_VALUE) return res + a[size-1];
		// recur with subtract
		ops[size-1] = -1; 
		res = recursiveGetMathOps(a, size-1, ops, sum + a[size-1]);
		if (res != Integer.MIN_VALUE) return res - a[size-1];
		return Integer.MIN_VALUE;
	}
	private static String recursiveGetMathOpsDriver(int[] a, int sum) {
		int[] ops = new int[a.length];
		int res =  recursiveGetMathOps(a, a.length, ops, sum);
		String sol = "";
		if (res == sum) { // found
			if (ops[0] == -1) sol = "-(" + a[0] + ") ";
			else sol = String.valueOf(a[0]);
			for (int i=1; i<ops.length; i++) {
				if (ops[i] == 1) sol += "+(" + a[i] + ") "; 
				if (ops[i] == -1) sol += "-(" + a[i] + ") "; 
			}
			sol += " = " + sum;
		}
		else sol = "NOT FOUND";
		return sol;
	}
	
	
	/**
	 * Find all possible solutions via recursion.
	 */
	private static void recursiveGetAllMathOps(ArrayList<String> solution, int[] a, int size, int sum) {
		if (size == 0) {
			if (sum==0) {
				for (String s: solution) System.out.print(s);
				System.out.println("");
			}
		}
		else {
			ArrayList<String> addSolution = solution;
			ArrayList<String> subtractSolution = new ArrayList<String>();
			subtractSolution.addAll(solution);
			addSolution.add("+(" + a[size-1] + ") ");
			subtractSolution.add("-(" + a[size-1] + ") ");
			recursiveGetAllMathOps(addSolution, a, size-1, sum-a[size-1]);
			recursiveGetAllMathOps(subtractSolution, a, size-1, sum+a[size-1]); 
		}
	}
	private static void recursiveGetAllMathOpsDriver(int[] a, int sum) {
		ArrayList<String> solution = new ArrayList<String>();
		recursiveGetAllMathOps(solution, a, a.length, sum);
	}
	
	/**
	 * We try to optimize the recursive method using a DP memoization lookup table.
	 */
	private static boolean recursiveGetAllMathOpsDPMemo(ArrayList<String> solution, int[] a, int size, int sum, Boolean[][] table, int shift) {
		if (table[size][sum+shift] != null && table[size][sum+shift] == false) {
			// no solution, print nothing, return boolean value only
			return table[size][sum+shift];
		}
		else {
			if (size == 0) {
				if (sum == 0) {
					// solution found, print
					for (String s: solution) System.out.print(s);
					System.out.println("");
					table[size][sum+shift] = true;
				} else table[size][sum+shift] = false;
			}
			else {
				ArrayList<String> addSolution = solution;
				ArrayList<String> subtractSolution = new ArrayList<String>();
				subtractSolution.addAll(solution);
				addSolution.add("+(" + a[size-1] + ") ");
				subtractSolution.add("-(" + a[size-1] + ") ");
				Boolean add = recursiveGetAllMathOpsDPMemo(addSolution, a, size-1, sum-a[size-1], table, shift);
				Boolean sub = recursiveGetAllMathOpsDPMemo(subtractSolution, a, size-1, sum+a[size-1], table, shift);
				table[size][sum+shift] = add || sub; 
			}
			return table[size][sum+shift]; 
		}
	}
	private static void recursiveGetAllMathOpsDPMemoDriver(int[] a, int sum) {
		int absSum = 0; 
		for (int i: a) absSum += Math.abs(i);
		Boolean[][] DPLookUp = new Boolean[a.length+1][absSum*2+1];
		int shift = absSum - sum; // value shift to the right so as to keep table index non-negative
		ArrayList<String> solution = new ArrayList<String>();
		recursiveGetAllMathOpsDPMemo(solution, a, a.length, sum, DPLookUp, shift);
	}
	
	/**
	 * We try to optimize the recursive method using a DP tabulation lookup table.
	 */
	private static void iterativeGetAllMathOpsDPTabu(int[] a, int sum) {
		int absSum = 0; 
		for (int i: a) absSum += Math.abs(i);
		boolean[][] table = new boolean[a.length+1][absSum*2+1]; // DP lookup table
		int shift = absSum - sum; // value shift to the right so as to keep table index non-negative
		// base state
		if (shift>=0) table[0][shift] = true;
		// proliferation
		for (int i=1; i<=a.length; i++) {
			for (int j=0; j<=absSum*2; j++) {
				if (j-a[i-1] >=0 && j-a[i-1] <= absSum*2 
						&& j+a[i-1] >=0 && j+a[i-1] <= absSum*2) 
					table[i][j] = table[i-1][j-a[i-1]] || table[i-1][j+a[i-1]];
			}
		}
		//System.out.println(table[a.length][sum+shift]);
		//for (boolean[] row: table) System.out.println(Arrays.toString(row));
		ArrayList<String> solution = new ArrayList<String>();
		System.out.println("The total number of solutions is " + recursiveLookupPrintAndCount(solution, a.length, table, sum, a, shift) + ".");
	}
	private static long recursiveLookupPrintAndCount(ArrayList<String> solution, int itemIdx, boolean[][] table, int sumIdx, int[] a, int shift) {
		if (itemIdx == 0) {
			if (sumIdx == 0) {
				// solution found, print
				for (String s: solution) System.out.print(s);
				System.out.println("");
				return 1;
			}
			return 0;
		}
		else {
			if (table[itemIdx][sumIdx+shift] == false) return 0; // no solution
			else {
				ArrayList<String> addSolution = solution;
				ArrayList<String> subtractSolution = new ArrayList<String>();
				subtractSolution.addAll(solution);
				addSolution.add("+(" + a[itemIdx-1] + ") ");
				subtractSolution.add("-(" + a[itemIdx-1] + ") ");
				return recursiveLookupPrintAndCount(addSolution, itemIdx - 1, table, sumIdx - a[itemIdx-1], a, shift) +
						recursiveLookupPrintAndCount(subtractSolution, itemIdx - 1, table, sumIdx + a[itemIdx-1], a, shift);		
			}
		}
	}
	
	/**
	 * Count all possible solutions via recursion.
	 */
	private static long recursiveCountAllMathOps(int[] a, int size, int sum) {
		if (size == 0) {
			if (sum==0) return 1; 
			else return 0;
		}
		return recursiveCountAllMathOps(a, size-1, sum-a[size-1]) + 
				recursiveCountAllMathOps(a, size-1, sum+a[size-1]); 
	}
	private static long recursiveCountAllMathOpsDriver(int[] a, int sum) {
		return recursiveCountAllMathOps(a, a.length, sum);
	}
	
	/**
	 * We try to optimize the recursive method with DP memoizaiton. 
	 */
	private static long recursiveCountAllMathOpsDPMemo(int[] a, int size, int sum, long[][] table, int shift) {
		if (table[size][sum+shift] != -1) return table[size][sum+shift];
		else {
			if (size == 0) {
				if (sum==0) table[size][sum+shift] = 1; 
				else table[size][sum+shift] = 0;
			}
			else table[size][sum+shift] = recursiveCountAllMathOpsDPMemo(a, size-1, sum-a[size-1], table, shift) + 
					recursiveCountAllMathOpsDPMemo(a, size-1, sum+a[size-1], table, shift); 
			return table[size][sum+shift];
		}
	}
	private static long recursiveCountAllMathOpsDPMemoDriver(int[] a, int sum) {
		int absSum = 0; 
		for (int i: a) absSum += Math.abs(i);
		long[][] DPLookUp = new long[a.length+1][absSum*2+1];
		for (long[] row: DPLookUp) Arrays.fill(row, -1);
		int shift = absSum - sum; // value shift to the right so as to keep table index non-negative
		return recursiveCountAllMathOpsDPMemo(a, a.length, sum, DPLookUp, shift);
	}
	
	/**
	 * We try to optimize the recursive method with DP tabulation. 
	 */
	private static long iterativeCountAllMathOpsDPTabu(int[] a, int sum) {
		int absSum = 0; 
		for (int i: a) absSum += Math.abs(i);
		long[][] table = new long[a.length+1][absSum*2+1]; // DP lookup table
		int shift = absSum - sum; // value shift to the right so as to keep table index non-negative
		// base state
		if (shift>=0) table[0][shift] = 1;
		// proliferation
		for (int i=1; i<=a.length; i++) {
			for (int j=0; j<=absSum*2; j++) {
				if (j-a[i-1] >=0 && j-a[i-1] <= absSum*2 
						&& j+a[i-1] >=0 && j+a[i-1] <= absSum*2) 
					table[i][j] = table[i-1][j-a[i-1]] + table[i-1][j+a[i-1]];
			}
		}
		return table[a.length][sum+shift];
	}
	

	@FunctionalInterface
	protected interface IntArrayToStringFunction {
	   String apply(int[] array, int sum) throws Exception;  
	}
	
	@FunctionalInterface
	protected interface IntArrayToBooleanFunction {
	   boolean apply(int[] array, int sum) throws Exception;  
	}
	
	@FunctionalInterface
	protected interface IntArrayToVoidFunction {
	   void apply(int[] array, int sum) throws Exception;  
	}
	
	@FunctionalInterface
	protected interface IntArrayToLongFunction {
	   long apply(int[] array, int sum) throws Exception;  
	}
	
    protected static void runIntArrayFuncAndCalculateTime(String message, IntArrayToStringFunction intArrFunc, int[] array, int sum) throws Exception {
    	long startTime = System.nanoTime();
    	System.out.printf("%-70s%s\n", message, intArrFunc.apply(array, sum));
    	long endTime = System.nanoTime();
    	long totalTime = new Long(TimeUnit.MICROSECONDS.convert(endTime - startTime, TimeUnit.NANOSECONDS));
    	DecimalFormat formatter = new DecimalFormat("#,###");
    	System.out.printf("%-70s%s\n\n", "Function execution time in micro-seconds: ", formatter.format(totalTime));
    }
    
    protected static void runIntArrayFuncAndCalculateTime(String message, IntArrayToBooleanFunction intArrFunc, int[] array, int sum) throws Exception {
    	long startTime = System.nanoTime();
    	System.out.printf("%-70s%s\n", message, String.valueOf(intArrFunc.apply(array, sum)));
    	long endTime = System.nanoTime();
    	long totalTime = new Long(TimeUnit.MICROSECONDS.convert(endTime - startTime, TimeUnit.NANOSECONDS));
    	DecimalFormat formatter = new DecimalFormat("#,###");
    	System.out.printf("%-70s%s\n\n", "Function execution time in micro-seconds: ", formatter.format(totalTime));
    }
    
    protected static void runIntArrayFuncAndCalculateTime(String message, IntArrayToLongFunction intArrFunc, int[] array, int sum) throws Exception {
    	long startTime = System.nanoTime();
    	System.out.printf("%-70s%d\n", message, intArrFunc.apply(array, sum));
    	long endTime = System.nanoTime();
    	long totalTime = new Long(TimeUnit.MICROSECONDS.convert(endTime - startTime, TimeUnit.NANOSECONDS));
    	DecimalFormat formatter = new DecimalFormat("#,###");
    	System.out.printf("%-70s%s\n\n", "Function execution time in micro-seconds: ", formatter.format(totalTime));
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
		//int[] intArray = genRanIntArr(30, -9, 9);
		int[] intArray = {2, 1, 7, 2, 6, 3, 7, 4, 2, 3, 6, 2, 3, 4, 8, 6, 5, 8, 8, 6, 5, 8, 7};
		int sum = 107;
		
		//int[] intArray = genRanUniqueIntArr(22);
		//int[] intArray = {108, -5, 95, -44, 6, -1, -23, 59, 62, -62, -80, -71, -94, 49, -68, -26, 51, 64, -22, -63, 35, -110};
	    //int sum = 1000;
		
		//int[] intArray = {0, 0, 0, 2, 2, 1, 1};
	    //int sum = 2;
		
		//int[] intArray = {2, 3, 4, 1};
	    //int sum = 4;
		System.out.println("Welcome to the rabbit hole of math operations for fixed sum!\n"
				+ "The integer array is \n" + Arrays.toString(intArray) + "\n"
				+ "The sum value is \n" + sum + "\n");
		
		try {
			runIntArrayFuncAndCalculateTime("[Recursive]                 Solution exists? ", 
					(int[] a, int v) -> recursiveCheckMathOpsDriver(a, v), intArray, sum);
			runIntArrayFuncAndCalculateTime("[Recursive][DP Memo]        Solution exists? ", 
					(int[] a, int v) -> recursiveCheckMathOpsDPMemoDriver(a, v), intArray, sum);
			runIntArrayFuncAndCalculateTime("[Iterative][DP Tabu]        Solution exists? ", 
					(int[] a, int v) -> iterativeCheckMathOpsDPTabu(a, v), intArray, sum);

			
			runIntArrayFuncAndCalculateTime("[Recursive]                 Solution count: ", 
					(int[] a, int v) -> recursiveCountAllMathOpsDriver(a, v), intArray, sum);
			runIntArrayFuncAndCalculateTime("[Recursive][DP Memo]        Solution count: ", 
					(int[] a, int v) -> recursiveCountAllMathOpsDPMemoDriver(a, v), intArray, sum);
			runIntArrayFuncAndCalculateTime("[Iterative][DP Tabu]        Solution count: ", 
					(int[] a, int v) -> iterativeCountAllMathOpsDPTabu(a, v), intArray, sum);

			runIntArrayFuncAndCalculateTime("[Recursive]                 List of all solutions: ", 
					(int[] a, int v) -> recursiveGetAllMathOpsDriver(a, v), intArray, sum);
			runIntArrayFuncAndCalculateTime("[Recursive][DP Memo]        List of all solutions: ", 
					(int[] a, int v) -> recursiveGetAllMathOpsDPMemoDriver(a, v), intArray, sum);
			runIntArrayFuncAndCalculateTime("[Iterative][DP Tabu]        List of all solutions: ", 
					(int[] a, int v) -> iterativeGetAllMathOpsDPTabu(a, v), intArray, sum);
			
			runIntArrayFuncAndCalculateTime("[Recursive][Random]                    For sum " + sum + " a solution is:", 
					(int[] a, int v) -> recursiveGetMathOpsDriver(a, v), intArray, sum);
			sum = 113; 
            runIntArrayFuncAndCalculateTime("[Recursive][Linear in best case]       For sum " + sum + " a solution is:", 
            		(int[] a, int v) -> recursiveGetMathOpsDriver(a, v), intArray, sum);
			sum = -113;
			runIntArrayFuncAndCalculateTime("[Recursive][Exponential in worst case] For sum " + sum + " a solution is:", 
					(int[] a, int v) -> recursiveGetMathOpsDriver(a, v), intArray, sum);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("All rabbits gone.");
	}
}
