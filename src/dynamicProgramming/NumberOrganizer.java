package dynamicProgramming;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;

import utils.FunAlgorithm;

/**
 * Given a series of seed numbers, say 1, 3, and 5, find the number of all possible arrangements that sum up to a given value A. 
 * Repetitions are allowed, and the order of seed numbers matter. 
 * 
 * https://www.geeksforgeeks.org/solve-dynamic-programming-problem/
 * 
 * E.g. Let A = 6, the possible arrangements are
 * A = 1 + 1 + 1 + 1 + 1 + 1
 * A = 1 + 1 + 1 + 3
 * A = 1 + 1 + 3 + 1
 * A = 1 + 3 + 1 + 1
 * A = 3 + 1 + 1 + 1
 * A = 3 + 3
 * A = 1 + 5
 * A = 5 + 1
 * 
 * @author ruifengm
 * @since 2018-Mar-31
 */

public class NumberOrganizer extends FunAlgorithm {

	private static final int[] NUM_ARR = {1, 3, 5};
	private static final long NIL = -1; 
	
	/**
	 * This problem can be solved using recursion.
	 * Let A denote the given sum, let Sol(A) denote the solution set of all arrangements of the seed numbers, each of which sums up to A, and let U denote the 
	 * set union operation.
	 * Since we only have a finite set of seed numbers, a member of Sol(A) must ends with one of them.  
	 * Let's first convince ourselves this case that if the last element of all members of Sol(A) that end with 3 is removed (i.e. last element is 3), those members
	 * will form the complete solution set for A-3, which is Sol(A-3). 
	 * 
	 * [Proof by Contradiction]
	 * Suppose those members do not form the complete solution set for A-3, then there will be at least one more solution. If we attach number 3 to that 
	 * solution, it will form a new arrangement as a new solution for A, which contradicts the fact that the whole solution set of A is already known. 
	 * 
	 * Then we have below formula
	 * 			Sol(A) = Sol(A-1) with 1 attached to all members  U  Sol(A-3) with 3 attached to all members  U  Sol(A-5) with 5 attached to all members
	 * 
	 * And since each sub solution set contains members that ends with a distinct number (1, 3, and 5 respectively), we are save to deduce that 
	 *          Sol(A).size =  Sol(A-1).size + Sol(A-3).size + Sol(A-5).size
	 *
	 * The time complexity of this algorithm is exponential because of the looped recursive calls. 
	 * 
	 * @param sum
	 * @return
	 */
	private static long recursiveArrange(int sum) {
		if (sum == 0 ) return 1; // only one arrangement which is 0*1 + 0*3 + 0*5 = 0
		long count = 0;
		for (int n: NUM_ARR) {
			if (sum >= n) count += recursiveArrange(sum - n);
		}
		return count;
	}
	
	
	/**
	 * Use DP memoization to reduce time complexity of O(N*k) as overlapping sub-problems are easily observed in the pure recursive technique 
	 * 
	 * @param table
	 * @param sum
	 * @return
	 */
	private static long recursiveArrangeDPMemo(long[] table, int sum) {
		if (table[sum] != NIL) return table[sum]; 
		else {
			if (sum == 0 ) table[sum] = 1; // only one arrangement which is 0*1 + 0*3 + 0*5 = 0
			else {
				table[sum] = 0;
				for (int n: NUM_ARR) {
					if (sum >= n) table[sum] += recursiveArrangeDPMemo(table, sum - n);
				}
			}
			return table[sum];
		}
	}
	private static long recursiveArrangeDPMemoDriver(int sum) {
		long[] DPLookUP = new long[sum + 1];
		Arrays.fill(DPLookUP, NIL);
		return recursiveArrangeDPMemo(DPLookUP, sum);
	}
	
	/**
	 * We convert the DP memoization flow into a bottom-up manner tabulation flow.
	 * 
	 * @param table
	 * @param sum
	 * @return
	 */
	private static long iterativeArrangeDPTabu(long[] table, int sum) {
		table[0] = 1; 
		for (int i=0; i<=sum; i++) 
			for (int n: NUM_ARR) if (i >= n) table[i] += table[i - n];
		return table[sum];
	}
	private static long iterativeArrangeDPTabuDriver(int sum) {
		long[] DPLookUP = new long[sum + 1];
		return iterativeArrangeDPTabu(DPLookUP, sum);
	}


	
	public static void main(String[] args) {
		int sum = 42;
		System.out.println("Welcome to the rabbit hole of number arrangements!\n"
				+ "The available seed numbers are " + Arrays.toString(NUM_ARR) + ".\n"
				+ "The sum value is " + sum + ".\n"); 
		
		try {
			runIntFuncAndCalculateTime("[Recursive][Exponential]     Count of number arrangements for sum " + sum + ":" , (int i) -> recursiveArrange(i), sum);
			runIntFuncAndCalculateTime("[Recursive][O(N*k)][Memo]    Count of number arrangements for sum " + sum + ":" , (int i) -> recursiveArrangeDPMemoDriver(i), sum);
			runIntFuncAndCalculateTime("[Iterative][O(N*k)][Tabu]    Count of number arrangements for sum " + sum + ":" , (int i) -> iterativeArrangeDPTabuDriver(i), sum);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("All rabbits gone.");
	}
}
