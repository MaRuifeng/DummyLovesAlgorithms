package dynamicProgramming;

import utils.FunAlgorithm;

/**
 * Below algorithms to find the n'th Fibonacci number are discussed. 
 * 
 * 1) Recursive
 * 2) Iterative
 * 3) Recursive with dynamic programming strategy
 * 4) Iterative with dynamic programming strategy 
 * 
 * The focus should be on how DP can greatly reduce the time complexity of the recursive method by avoiding repeated computations, which 
 * abides by the compound interest rule of recursive functions. 
 * 
 * Dynamic Programming is an algorithmic paradigm where a complex problem is broken into sub-problems, whose results are stored to eventually 
 * render the result of the problem itself, instead of computing them again and again. Below two properties are indicators on whether a problem is
 * DP-solvable. 
 * 
 * 1) Overlapping (common) sub-problems: two techniques can be used to store the sub-solutions
 *    a) Memoization (Top Down) --> in recursive method
 *    b) Tabulation (Bottom Up) --> in iterative method
 * 2) Optimal substructure: optimal solutions to sub-problems can be used to obtain the solution to the parent problem
 * 
 * https://www.geeksforgeeks.org/solve-dynamic-programming-problem/
 * @author ruifengm
 * @since 2018-Mar-15
 */

public class FibNumbers extends FunAlgorithm {
	
	private static final int MAX = 200; 
	private static final long NIL = -1; 
	
	private static long[] DPLookUp = new long[MAX];
	
	private static void initDPLookUpArray() {
		for (int i=0; i<DPLookUp.length; i++) DPLookUp[i] = NIL;
	}
	
	
	/**
	 * Some involved mathematical deductions can show that the time complexity of this method is exponential. (p.37 of Mark's book)
	 * @param an integer n indicating position of wanted Fibonacci number
	 * @return the Fibonacci number at position n
	 */
	protected static long recursiveFib(int n) {
		if (n <= 1) return n;
		else return recursiveFib(n - 1) + recursiveFib(n - 2);
	}
	
	/**
	 * Most intuitive method. Time complexity O(N).
	 * @param an integer n indicating position of wanted Fibonacci number
	 * @return the Fibonacci number at position n
	 */
    protected static long iterativeFib(int n) {
    	long pre_1 = 0;
    	long pre_2 = 1; 
    	long cur = 0;
		for (int i=0; i<n; i++) {
			cur = pre_1 + pre_2; 
			pre_2 = pre_1; 
			pre_1 = cur;
		}
		return cur;
	}
    
	/**
	 * Use memoization to store the calculated results in an array to avoid repeated computations. 
	 * No mathematical deductions done, but a guessed time complexity is O(N). Use the running time to verify.
	 * @param an integer n indicating position of wanted Fibonacci number
	 * @return the Fibonacci number at position n
	 */
	protected static long recursiveFibWithDP(int n) {
		if (DPLookUp[n] != NIL) return DPLookUp[n];
		else {
			if (n <= 1) DPLookUp[n] = n;
			// Build the solution store from top down
			else DPLookUp[n] = recursiveFibWithDP(n - 1) + recursiveFibWithDP(n - 2);
			return DPLookUp[n];
		}
	}
	
	/**
	 * Time complexity O(N).
	 * @param an integer n indicating position of wanted Fibonacci number
	 * @return the Fibonacci number at position n
	 */
    protected static long iterativeFibWithDP(int n) {
    	// Build the solution store from bottom up
    	DPLookUp[0] = 0;
    	DPLookUp[1] = 1; 
		for (int i=2; i<=n; i++) DPLookUp[i] = DPLookUp[i - 1] + DPLookUp[i - 2];
		return DPLookUp[n];
	}
	
	public static void main(String[] args) {
		int pos = 40;
		System.out.println("Welcome to the rabbit hole of Fibonacci numbers!\n"
				+ "The randomly wanted number should be at position " + pos + ".\n"); 
		
		try {
			// Find the max subsequence sum with cubic complexity
			runIntFuncAndCalculateTime("[Recursive][Exponential]  Fibonacci Number at position " + pos + ":" , (int i) -> recursiveFib(i), pos);
			runIntFuncAndCalculateTime("[Iterative][Linear]       Fibonacci Number at position " + pos + ":" , (int i) -> iterativeFib(i), pos);
			initDPLookUpArray();
			runIntFuncAndCalculateTime("[RecursiveDP][Linear]     Fibonacci Number at position " + pos + ":" , (int i) -> recursiveFibWithDP(i), pos);
			initDPLookUpArray();
			runIntFuncAndCalculateTime("[IterativeDP][Linear]     Fibonacci Number at position " + pos + ":" , (int i) -> iterativeFibWithDP(i), pos);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("All rabbits gone.");
	}

}
