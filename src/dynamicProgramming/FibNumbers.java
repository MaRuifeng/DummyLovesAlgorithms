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
 * 1) Overlapping (common) sub-problems: the same sub-problem is computed more than once. Hence their results can be stored for future usage. 
 *    a) Memoization (Top Down) --> in recursive method
 *    b) Tabulation (Bottom Up) --> in iterative method
 * 2) Optimal substructure: optimal solutions to sub-problems can be used to obtain the solution to the parent problem.
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
	 * Tail recursion that produces Fibonacci Numbers. 
	 * In functional programming languages, tail recursive optimization is by default hence this recursive method utilizes constant space 
	 * as the iterative method. 
	 * The "tail" property of a tail recursion is that the return value of any given recursive step is always equal to the 
	 * return value of the next recursive call. 
	 * @param an integer n indicating position of wanted Fibonacci number
	 * @return the Fibonacci number at position n
	 */
	protected static long tailRecursiveFib(int n, int a, int b) {
		if (n == 0) return 0;
		if (n == 1) return b;
		else return tailRecursiveFib(n-1, b, a+b); // run calculation first and then recur
	}
	protected static long tailRecursiveFibDriver(int n) {
		return tailRecursiveFib(n, 0, 1);
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
	 * It's worth mentioning that this bottom-up DP solution looks so intuitive. 
	 * This is true because the "overlapping subproblems" and the "optimal substructure" properties
	 * are well described in the definition of the Fibonacci Numbers itself. In other DP-solvable problems, these properties may not be 
	 * so easy to spot. E.g. the coin change problem requires quite some smart thinking to identify the patterns. 
	 * 
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
			runIntFuncAndCalculateTime("[Recursive][Exponential]  Fibonacci Number at position " + pos + ":" , (int i) -> recursiveFib(i), pos);
			runIntFuncAndCalculateTime("[Recursive][Linear]       Fibonacci Number at position " + pos + ":" , (int i) -> tailRecursiveFibDriver(i), pos);
			runIntFuncAndCalculateTime("[Iterative][Linear]       Fibonacci Number at position " + pos + ":" , (int i) -> iterativeFib(i), pos);
			initDPLookUpArray();
			runIntFuncAndCalculateTime("[RecursiveDP][Linear]     Fibonacci Number at position " + pos + ":" , (int i) -> recursiveFibWithDP(i), pos);
			initDPLookUpArray();
			runIntFuncAndCalculateTime("[IterativeDP][Linear]     Fibonacci Number at position " + pos + ":" , (int i) -> iterativeFibWithDP(i), pos);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("All rabbits gone.");
	}

}
