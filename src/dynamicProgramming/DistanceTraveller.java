package dynamicProgramming;

import java.util.Arrays;

import utils.FunIntAlgorithm;

/**
 * Given a distance, count the number of ways through which it can be covered with 1, 2 and 3 steps. 
 * 
 * E.g. Given a distance of 3, the ways to cover it are
 *     1 step, 1 step, 1 step
 *     1 step, 2 steps
 *     2 steps, 1 step
 *     3 steps
 * So the total number of ways is 4. 
 * 
 * This question is analogous to the number organization question.
 * 
 * @author ruifengm
 * @since 2018-Apr-26
 * 
 * https://www.geeksforgeeks.org/count-number-of-ways-to-cover-a-distance/
 */
public class DistanceTraveller extends FunIntAlgorithm {
	private static final int[] steps = {1, 2, 3};
	
	/**
	 * Given a distance n, let Sol(n) be the number of ways to cover it with the manageable steps. 
	 * Below recursive pattern can be easily identified, since the possible steps are limited to 1, 2 and 3. 
	 * 		Sol(n) = Sol(n-1) + Sol(n-2) + Sol(n-3)
	 */
	private static long recursiveCount(int dist) {
		if (dist == 0) return 1;
		if (dist < 0) return 0; 
		long sol = 0; 
		for (int i: steps) sol += recursiveCount(dist - i); 
		return sol;
	}
	
	/**
	 * Optimize the recursive solution with DP top down memoization.
	 */
	private static long recursiveCountDPMemo(int dist, long[] table) {
		if (table[dist] != 0) return table[dist];
		else {
			if (dist == 0) table[dist] = 1;
			else {
				for (int i: steps) {
					if (dist >= i) table[dist] += recursiveCountDPMemo(dist - i, table);
				}
			}
			return table[dist];
		}
	}
	private static long recursiveCountDPMemoDriver(int dist) {
		long[] DPLookUp = new long[dist+1];
		return recursiveCountDPMemo(dist, DPLookUp);
	}
	
	/**
	 * Optimize the recursive solution with DP bottom up tabulation.
	 */
	private static long iterativeCountDPTabu(int dist, long[] table) {
		table[0] = 1; 
		for (int i=1; i<=dist; i++) {
			for (int s: steps) {
				if (i >= s) table[i] += table[i-s];
			}
		}
		return table[dist];
	}
	private static long iterativeCountDPTabuDriver(int dist) {
		long[] DPLookUp = new long[dist+1];
		return iterativeCountDPTabu(dist, DPLookUp);
	}
	
	public static void main(String[] args) {
		int dist = 14;
		System.out.println("Welcome to the rabbit hole of distance and steps!\n"
				+ "The available step counts are " + Arrays.toString(steps) + ".\n"
				+ "The distance is " + dist + ".\n"); 
		
		try {
			runIntFuncAndCalculateTime("[Recursive][Exponential]     Count of ways to cover distance " + dist + ":" , (int i) -> recursiveCount(i), dist);
			runIntFuncAndCalculateTime("[Recursive][O(N*k)][Memo]    Count of ways to cover distance " + dist + ":" , (int i) -> recursiveCountDPMemoDriver(i), dist);
			runIntFuncAndCalculateTime("[Iterative][O(N*k)][Tabu]    Count of ways to cover distance " + dist + ":" , (int i) -> iterativeCountDPTabuDriver(i), dist);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("All rabbits gone.");
	}

}
