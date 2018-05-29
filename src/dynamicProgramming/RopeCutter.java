package dynamicProgramming;

import java.util.ArrayList;
import java.util.Arrays;

import utils.FunIntAlgorithm;

/**
 * Given a rope of n meters long, cut the rope in different parts of integer lengths
 * such that the product of the lengths is maximized. Minimum one cut must be performed. 
 * 
 * The problem is analogous to the rod cutting problem. 
 * 
 * @author ruifengm
 * @since 2018-May-29 
 */


public class RopeCutter extends FunIntAlgorithm {
	
	/**
	 * Since the rope has to be cut at least once, the integer length that a sub piece can have ranges 
	 * from 1 to n-1. For each length, we try to find its maximum product, and then compare them to find
	 * the global maximum. 
	 * Let Sol(n) be the max product that can be obtained by cutting a rope of length n, the maximum product at each 
	 * sub-length i can be found as 
	 *        MAX ( i * n-i,               --> do not cut remaining rope
	 *              i * Sol(n-i) )         --> cut remaining rope
	 *        for i in [1..n-1]
	 * Since sub-problem pattern has been identified, we can solve the problem via recursion. 
	 */
	private static long recursiveGetMaxProduct(int len) {
		if (len == 1) return 1; 
		long max = Long.MIN_VALUE; 
		for (int i=1; i<len; i++) {
			max = Math.max(max, Math.multiplyExact(i, 
					Math.max(len-i, recursiveGetMaxProduct(len-i)))); 
		}
		return max;
	}
	
	/** 
	 * We try to optimize the above recursive solution with both DP 
	 * memoization and tabulation. 
	 */
	private static long recursiveGetMaxProductDPMemo(int len, long[] table) {
		if (table[len] != -1) return table[len];
		else {
			if (len == 1) table[len] = 1; 
			else {
				long max = Long.MIN_VALUE;
				for (int i=1; i<len; i++) {
					max = Math.max(max, Math.multiplyExact(i, 
							Math.max(len-i, recursiveGetMaxProductDPMemo(len-i, table))));
				}
				table[len] = max;
			}
			return table[len];
		}
	}
	private static long recursiveGetMaxProductDPMemo(int len) {
		long[] DPLookUp = new long[len+1];
		Arrays.fill(DPLookUp, -1);
		return recursiveGetMaxProductDPMemo(len, DPLookUp);
	}
	
	private static long iterativeGetMaxProductDPTabu(int len) {
		long[] table = new long[len+1];
		table[1] = 1; 
		for (int i=2; i<=len; i++) {
			long max = Long.MIN_VALUE;
			for (int j=1; j<i; j++)
				max = Math.max(max, Math.multiplyExact(j, 
						Math.max(i-j, table[i-j])));
			table[i] = max; 
		}
		return table[len];
	}
	
	
	/** 
	 * We try to find a cutting solution with minimum cuts that would yield the maximum product
	 * via the DP tabulation method. 
	 */
	static class Solution {
		String cutPoints; 
		int numOfCuts;
		long maxPoduct; 
		public Solution(String cutPoints, int numOfCuts, long maxPoduct) {
			this.cutPoints = cutPoints; 
			this.numOfCuts = numOfCuts;
			this.maxPoduct = maxPoduct;
		}
	}
	private static void iterativeGetMaxProductSolutionDPTabu(int len) {
		Solution[] table = new Solution[len+1];
		table[1] = new Solution("", 0, 1);
		for (int i=2; i<=len; i++) {
			Solution optimal = new Solution("", 0, Long.MIN_VALUE);
			for (int j=1; j<i; j++) {
				String solStrLeft = " ", solStrRight = " ", solStr;
				for (int k=0; k<j; k++) solStrLeft += (i-k) + " ";
				for (int k=j; k<i; k++) solStrRight += (i-k) + " ";
				int solNumOfCuts = 1; 
				long rightMaxProdct = i-j;
				if (table[i-j].maxPoduct > i-j) {
					solNumOfCuts += table[i-j].numOfCuts;
					rightMaxProdct = table[i-j].maxPoduct;
					solStr = solStrLeft + "|" + table[i-j].cutPoints;
				} else solStr = solStrLeft + "|" + solStrRight;
				Solution sol = new Solution(solStr, solNumOfCuts, Math.multiplyExact(j, rightMaxProdct));
				if (sol.maxPoduct > optimal.maxPoduct) optimal = sol;
				if (sol.maxPoduct == optimal.maxPoduct && sol.numOfCuts < optimal.numOfCuts) optimal = sol; 
			}
			table[i] = optimal; 
		}
		System.out.println("\n/************* Cutting solution with minimum cuts that yields the max product *************/");
    	System.out.println("Cutting points: " + table[len].cutPoints);
    	System.out.println("Number of cuts: " + table[len].numOfCuts);
    	System.out.println("Max product: " + table[len].maxPoduct);
    	System.out.println();
	}
	/** 
	 * We try to find all cutting solutions that would yield the maximum product
	 * via the DP tabulation method. 
	 */
	@SuppressWarnings("unchecked")
	private static void iterativeGetAllMaxProductSolutionsDPTabu(int len) {
		ArrayList<Solution>[] table = new ArrayList[len+1];
		table[1] = new ArrayList<>();
		table[1].add(new Solution("", 0, 1));
		for (int i=2; i<=len; i++) {
			Solution optimal = new Solution("", 0, Long.MIN_VALUE);
			table[i] = new ArrayList<>();
			for (int j=1; j<i; j++) {
				for (Solution preSol: table[i-j]) {
					String solStrLeft = " ", solStrRight = " ", solStr;
					for (int k=0; k<j; k++) solStrLeft += (i-k) + " ";
					for (int k=j; k<i; k++) solStrRight += (i-k) + " ";
					int solNumOfCuts = 1; 
					long rightMaxProdct = i-j;
					if (preSol.maxPoduct > i-j) {
						solNumOfCuts += preSol.numOfCuts;
						rightMaxProdct = preSol.maxPoduct;
						solStr = solStrLeft + "|" + preSol.cutPoints;
					} else solStr = solStrLeft + "|" + solStrRight;
					Solution sol = new Solution(solStr, solNumOfCuts, Math.multiplyExact(j, rightMaxProdct));
					if (sol.maxPoduct > optimal.maxPoduct) {
						optimal = sol;
						table[i].clear(); // reset
					}
					if (sol.maxPoduct == optimal.maxPoduct) {
						if (table[i].isEmpty() || !table[i].get(table[i].size()-1).cutPoints.equals(sol.cutPoints))
							table[i].add(sol); 
					}
				}
			}
		}
		System.out.println("\n/************* All cutting solutions that yield the max product *************/");
		System.out.println("Count of solutions: " + table[len].size());
		if (table[len].size() < 40) {
			for (Solution sol: table[len]) {
		    	System.out.println("Cutting points: " + sol.cutPoints);
		    	System.out.println("Number of cuts: " + sol.numOfCuts);
		    	System.out.println("Max product: " + sol.maxPoduct);
		    	System.out.println();
			}
		}
	}
	
	/**
	 * After observing a few examples of this problem, we can observe that the maximum 
	 * product can be obtained by repeatedly cutting out a sub-piece of size 3 while the size is greater than
	 * 4, keeping the last part of size 2, 3, or 4. 
	 * Though there isn't a rigorous mathematical correctness proof given, we try to implement this approach. 
	 */
	private static long empiricalGetMaxProduct(int len) {
		if (len == 2 || len == 3) return len-1; 
		long res = 1; 
		while (len > 4) {
			len -= 3; 
			res = Math.multiplyExact(3, res);
		}
		return Math.multiplyExact(len, res);
	}

	public static void main(String[] args) {
		int ropeLen = 7; 

		System.out.println("Welcome to the rabbit hole of rope cutters!\n"
				+ "The length of the rope is " + ropeLen + ".\n"
				+ "Find the maximum length product by cutting the rope into pieces.\n"); 
		
		try {
			runIntFuncAndCalculateTime("[Recursive]               Max product:" , 
					(int i) -> recursiveGetMaxProduct(i), ropeLen);
			runIntFuncAndCalculateTime("[Recursive][DP Memo]      Max product:" , 
					(int i) -> recursiveGetMaxProductDPMemo(i), ropeLen);
			runIntFuncAndCalculateTime("[Iterative][DP Tabu]      Max product:" , 
					(int i) -> iterativeGetMaxProductDPTabu(i), ropeLen);
			runIntFuncAndCalculateTime("[Empirical]               Max product:" , 
					(int i) -> empiricalGetMaxProduct(i), ropeLen);
			
			iterativeGetMaxProductSolutionDPTabu(ropeLen);
			iterativeGetAllMaxProductSolutionsDPTabu(ropeLen);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("All rabbits gone.");
	}
}
