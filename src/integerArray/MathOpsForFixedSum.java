package integerArray;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import utils.FunIntAlgorithm;

/** 
 * Given an array of integers and fixed another K, apply +/- operations to the array items such that the result is K. 
 * State so if no solution can be found. 
 * 
 * E.g. for {2, 3, 4} and 1, the solution is 2+3-4=1. 
 * 
 * @author ruifengm
 * @since 2018-Apr-12
 */

public class MathOpsForFixedSum extends FunIntAlgorithm {
	
	/**
	 * Let A(n) be the array of integers of size n and let K be the fixed number, and let Res(A(n)) be the result of current math operations (add and subtract), and
	 * Let Ops(n) be an array of 1 and -1 which denote the math operation needed for each element of A (1 for '+' and -1 for '-').
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
	private static int mathOps(int[] a, int size, int[] ops, int sum) {
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
		int res = mathOps(a, size-1, ops, sum - a[size-1]);
		if (res != Integer.MIN_VALUE) return res + a[size-1];
		// recur with subtract
		ops[size-1] = -1; 
		res = mathOps(a, size-1, ops, sum + a[size-1]);
		if (res != Integer.MIN_VALUE) return res - a[size-1];
		return Integer.MIN_VALUE;
	}
	private static String mathOpsDriver(int[] a, int sum) {
		int[] ops = new int[a.length];
		int res =  mathOps(a, a.length, ops, sum);
		String sol = "";
		if (res == sum) { // found
			if (ops[0] == -1) sol = "-" + a[0];
			else sol = String.valueOf(a[0]);
			for (int i=1; i<ops.length; i++) {
				if (ops[i] == 1) sol += "+" + a[i]; 
				if (ops[i] == -1) sol += "-" + a[i]; 
			}
			sol += " = " + sum;
		}
		else sol = "NOT FOUND";
		return sol;
	}
	
	@FunctionalInterface
	protected interface IntArrayToStringFunction {
	   String apply(int[] array, int sum) throws Exception;  
	}
	
    protected static void runIntArrayFuncAndCalculateTime(String message, IntArrayToStringFunction intArrFunc, int[] array, int sum) throws Exception {
    	long startTime = System.nanoTime();
    	System.out.printf("%-70s%s\n", message, intArrFunc.apply(array, sum));
    	long endTime = System.nanoTime();
    	long totalTime = new Long(TimeUnit.MICROSECONDS.convert(endTime - startTime, TimeUnit.NANOSECONDS));
    	DecimalFormat formatter = new DecimalFormat("#,###");
    	System.out.printf("%-70s%s\n\n", "Function execution time in micro-seconds: ", formatter.format(totalTime));
    }
	
	public static void main(String[] args) {
		//int[] intArray = genRanIntArr(30, 1, 9);
		int[] intArray = {2, 1, 7, 2, 6, 3, 7, 4, 2, 3, 6, 2, 3, 4, 8, 6, 5, 8, 8, 6, 5, 8, 1, 5, 4, 5, 5, 7, 1, 3};
		int sum = 25;
		System.out.println("Welcome to the rabbit hole of math operations for fixed sum!\n"
				+ "The integer array is \n" + Arrays.toString(intArray) + "\n");
		
		try {
			runIntArrayFuncAndCalculateTime("[Random]                    For sum " + sum + " the solution is:", (int[] a, int v) -> mathOpsDriver(a, v), intArray, sum);
			sum = 137; 
			runIntArrayFuncAndCalculateTime("[Linear in best case]       For sum " + sum + " the solution is:", (int[] a, int v) -> mathOpsDriver(a, v), intArray, sum);
			sum = -137;
			runIntArrayFuncAndCalculateTime("[Exponential in worst case] For sum " + sum + " the solution is:", (int[] a, int v) -> mathOpsDriver(a, v), intArray, sum);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("All rabbits gone.");
	}
}
