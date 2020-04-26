package dynamicProgramming;
import java.util.ArrayList;

import utils.FunIntAlgorithm;

/**
 * Catalan numbers are a sequence of natural numbers that occurs in many intersting counting problems in 
 * combinatorics, e.g. count the number of expressions containing n pairs of parentheses which are correctly matched. For n = 3, 
 * possible expressions are ((())), ()(()), ()()(), (())(), (()()). The number of possible binary search trees that can 
 * be formed with keys from 1 to N is a Catalan number at the N'th position. 
 * 
 * Below link contains references to a few applications of the Catalan numbers. 
 * https://www.geeksforgeeks.org/applications-of-catalan-numbers/
 * 
 * The Catalan numbers have a closed-form mathematical formula in terms of binomial coefficients, and they also
 * satisfy below recurrence relation. (https://brilliant.org/wiki/catalan-numbers/)
 * 
 * C[n+1] = C[0] * C[n] + C[1] * C[n-1] + ... + C[n] * C[0]
 * 
 * The first few Catalan numbers for n = 0, 1, 2, 3, 4, ... are 1, 1, 2, 5, 14, 42, 132, 429, 1430, 4862, ...
 * 
 * Write a program that returns the n'th Catalan number. 
 * 
 * @author ruifengm
 * @since 2018-May-22
 */
public class CatalanNumbers extends FunIntAlgorithm {
	
	/** 
	 * A simple recursive method based on the recurrence relation. 
	 * 
	 * Time complexity: equivalent to the Catalan number at the given position, which is exponential.
	 */
	private static long recursiveCatalan(int n) {
		if (n<=1) return 1; 
		long res = 0; 
		for (int i=0; i<n; i++) res = Math.addExact(res, Math.multiplyExact(recursiveCatalan(i), recursiveCatalan(n-i-1)));
		return res;
	}
	
	/**
	 * Optimized with a DP lookup table constructed via memoization.
	 * 
	 * Time complexity: O(n2)
	 */
	private static long recursiveCatalanDPMemo(int n, long[] table) {
		if (table[n] != 0) return table[n]; 
		else {
			if (n<=1) table[n] = 1; 
			else {
				for (int i=0; i<n; i++) table[n] = Math.addExact(table[n], Math.multiplyExact(recursiveCatalanDPMemo(i, table), 
						recursiveCatalanDPMemo(n-i-1, table)));
			}
			return table[n];
		}
	}
	private static long recursiveCatalanDPMemo(int n) {
		long[] DPLookUp = new long[n+1]; 
		return recursiveCatalanDPMemo(n, DPLookUp);
	}
	
	/**
	 * Optimize with a DP lookup table constructed via tabulation. This solution also comes from the natural iterative 
	 * way of finding the Catalan numbers.
	 * 
	 * Time complexity: O(n2)
	 */
	private static long iterativeCatalanDPTabu(int n) {
		long[] table = new long[n+1]; // DP lookup table
		// base case
		table[0] = 1;
		table[1] = 1;
		// proliferation
		for (int i=2; i<=n; i++) 
			for (int j=0; j<i; j++) 
				table[i] = Math.addExact(table[i], Math.multiplyExact(table[j], table[i-j-1]));
		return table[n];
	}
	
	/**
	 * Compute the Catalan numbers from its closed-form mathematical formula. 
	 * C[n] = comb(2n,n) / (n+1), where comb denotes the binomial coefficient. 
	 * 
	 * Time complexity: O(n)
	 */
	private static long mathematicalCatalan(int n) {
		long val = 1; 
		for (int i=1; i<=n; i++) {
			val = Math.multiplyExact(val, n*2-i+1); 
			val /= i;
		}
		return val/(n+1);
	}
	
	/**
	 * Print all expressions that contain n pairs of balanced parentheses.
	 * This method is trying to push the printing deep into the function call tree. 
	 */
	private static void printBalancedParentheses(char[] sol, int n, int idx, int leftBracketCount, int rightBracketCount) {
		if (rightBracketCount == n) {  // fully matched, print 
			for (char c: sol) System.out.print(c);
			System.out.println();
		}
		else {
			if (leftBracketCount < n) {
				sol[idx] = '('; // left open brackets not depleted, add into the solution
				printBalancedParentheses(sol, n, idx+1, leftBracketCount+1, rightBracketCount);
			}
			if (leftBracketCount > rightBracketCount) {
				sol[idx] = ')'; // left open brackets exceeded, add right open bracket into the solution to match
				printBalancedParentheses(sol, n, idx+1, leftBracketCount, rightBracketCount+1);
			}
		}
	}
	private static void printBalancedParentheses(int n) {
		if (n<=0) return;
		char[] parenArr = new char[n*2]; 
		printBalancedParentheses(parenArr, n, 0, 0, 0);
	}
	
	/**
	 * Find all expressions that contain n pairs of balanced parentheses.
	 * This method is trying to pull the solution to the surface and then print.
	 * We can think of it as enclosing empty strings with open and close brackets.
	 * Refer to the boolean symbol and logic operator parenthesization problem for better understanding.
	 */
	private static ArrayList<String> findBalancedParentheses(int start, int end) {
		if (start > end) { // edge encountered to put parentheses
			ArrayList<String> sol = new ArrayList<>();
			sol.add("");
			return sol;
		}
		ArrayList<String> sol = new ArrayList<>();
		for (int i=start; i<=end; i++) {
			ArrayList<String> left = findBalancedParentheses(start, i-1);
			ArrayList<String> right = findBalancedParentheses(i+1, end);
			for (String l: left)
				for (String r: right)
					sol.add(l + "(" + r + ")");   // use this or use below
					//sol.add("(" +l + ")" + r);
		}
		return sol;
	}
	
	public static void main(String[] args) {
		int pos = 3;
		System.out.println("Welcome to the rabbit hole of Catalan numbers!\n"
				+ "The wanted number should be at position " + pos + ".\n"); 
		
		try {
			runIntFuncAndCalculateTime("[Recursive][Exponential]           Catalan Number at position " + pos + ":" , (int i) -> recursiveCatalan(i), pos);
			runIntFuncAndCalculateTime("[Recursive][DPMemo][Quadratic]     Catalan Number at position " + pos + ":" , (int i) -> recursiveCatalanDPMemo(i), pos);
			runIntFuncAndCalculateTime("[Iterative][DPTabu][Quadratic]     Catalan Number at position " + pos + ":" , (int i) -> iterativeCatalanDPTabu(i), pos);
			runIntFuncAndCalculateTime("[Iterative][Linear]                Catalan Number at position " + pos + ":" , (int i) -> mathematicalCatalan(i), pos);
			System.out.println("\nPrint out all possible balanced parentheses strings:");
			printBalancedParentheses(pos);
			System.out.println("\nFind all possible balanced parentheses strings into a list:");
			ArrayList<String> sol = findBalancedParentheses(0, pos-1); 
			System.out.println("List size: " + sol.size());
			for (String s: sol) System.out.println(s);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("All rabbits gone.");
	}
	
	

}
