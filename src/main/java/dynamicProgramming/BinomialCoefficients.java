package dynamicProgramming;
import java.util.Arrays;

import utils.FunIntAlgorithm;

/**
 * In mathematics, a binomial coefficient C(n, k) is the coefficient of the x^k term in the expansion of (1+x)^n in 
 * the binomial theorem (https://en.wikipedia.org/wiki/Binomial_theorem). 
 * 
 * It can also be plainly expressed as the number of combinations of choosing k elements from an n-element set, where 0 <= k <= n. 
 * 
 * Write a program that finds the value the binomial coefficient C(n, k). 
 * 
 * @author ruifengm
 * @since 2018-May-22
 * 
 */

public class BinomialCoefficients extends FunIntAlgorithm {
	
	/**
	 * A recursive pattern can be found as below: 
	 *     Number of ways to choose k elements out of n = Number of ways to choose (k-1) elements out of (n-1)     <-- last element chosen
	 *                                                  + Number of ways to choose k elements out of (n-1)         <-- last element not chosen
	 * The mathematical relation is expressed as C(n, k) = C(n-1, k-1) + C(n-1, k) which can also be rigorously 
	 * proved using the mathematical definitions of combination. 
	 * And for edge cases there are C(n, 0) = 1, C(n, n) = 1.
	 */
	private static long recursiveCombinations(int k, int n) {
		if (k==0 || k==n) return 1; 
		return Math.addExact(recursiveCombinations(k-1, n-1), recursiveCombinations(k, n-1));
	}
	
	/** 
	 * Optimized using a DP lookup table constructed via DP memoization. 
	 * Time complexity: O(n*k)
	 */
	private static long recursiveCombinationsDPMemo(int k, int n, long[][] table) {
		if (table[k][n] != 0) return table[k][n]; 
		else {
			if (k==0 || k==n) table[k][n] = 1; 
			else {
				table[k][n] = Math.addExact(recursiveCombinationsDPMemo(k-1, n-1, table), 
						recursiveCombinationsDPMemo(k, n-1, table));
			}
			return table[k][n];
		}
	}
	private static long recursiveCombinationsDPMemo(int k, int n) {
		long[][] DPLookUp= new long[k+1][n+1]; 
		return recursiveCombinationsDPMemo(k, n, DPLookUp);
	}
	
	
	/** 
	 * Optimized using a DP lookup table constructed via DP tabulation. 
	 * Time complexity: O(n*k)
	 */
	private static long iterativeCombinationsDPTabu(int k, int n) {
		long[][] table = new long[k+1][n+1]; // DP lookup table
		// base case
		Arrays.fill(table[0], 1);
		// proliferation
		for (int i=1; i<=k; i++) 
			for (int j=1; j<=n; j++) table[i][j] = Math.addExact(table[i-1][j-1], table[i][j-1]);
		// for (long[] row: table) System.out.println(Arrays.toString(row));
		return table[k][n];
	}
	
	/**
	 * Optimize space usage on the DP tabulation method.
	 * Time complexity: O(n*k)
	 */
	private static long iterativeCombinationsDPTabuSpaceOptimized(int k, int n) {
		long[] table = new long[k+1]; // DP lookup table
		// base case
		table[0] = 1;
		// proliferation
		for (int i=1; i<=n; i++) {
			// compute the next row of the Pascal triangle
			for (int j=Math.min(i, k); j>0; j--) table[j] = Math.addExact(table[j], table[j-1]);
			// System.out.println(Arrays.toString(table));
		}

		return table[k];
	}
	
	/**
	 * Compute the binomial coefficient from its mathematical formula. 
	 * C(n, k) = [n * (n-1) * .... * (n-k+1)] / [k * (k-1) * .... * 1]
	 * C(n, k) = C(n, n-k)
	 * Time complexity: O(n)
	 */
	private static long mathematicalCombinations(int k, int n) {
		if (k > n-k) k = n-k; 
		long val = 1; 
		for (int i=1; i<=k; i++) {
			val = Math.multiplyExact(val, n-i+1); 
			val /= i;
		}
		return val; 
	}
	
	public static void main(String[] args) {
		int k = 10, n= 40;
		System.out.println("Welcome to the rabbit hole of Binomial Coefficients!\n"
				+ "Choose " + k + " elements out of " + n + ".\n"); 
		try {
			runIntFuncAndCalculateTime("[Recursive][Exponential]     Binomial Coefficient C(" + n + "," + k + ") = " , (int a, int b) -> recursiveCombinations(a, b), k, n);
			runIntFuncAndCalculateTime("[Recursive][DPMemo][O(n*k)]  Binomial Coefficient C(" + n + "," + k + ") = " , (int a, int b) -> recursiveCombinationsDPMemo(a, b), k, n);
			runIntFuncAndCalculateTime("[Iterative][DPTabu][O(n*k)]  Binomial Coefficient C(" + n + "," + k + ") = " , (int a, int b) -> iterativeCombinationsDPTabu(a, b), k, n);
			runIntFuncAndCalculateTime("[Iterative][O(n*k)]          Binomial Coefficient C(" + n + "," + k + ") = " , (int a, int b) -> iterativeCombinationsDPTabuSpaceOptimized(a, b), k, n);
			runIntFuncAndCalculateTime("[Iterative][O(n)]            Binomial Coefficient C(" + n + "," + k + ") = " , (int a, int b) -> mathematicalCombinations(a, b), k, n);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("All rabbits gone.");
	}

}
