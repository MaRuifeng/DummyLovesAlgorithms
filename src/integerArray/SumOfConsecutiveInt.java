package integerArray;

import utils.FunIntAlgorithm;

/**
 * Given a number N, find the number of ways to represent this number as a sum of 2 or more consecutive positive integers.
 * 
 * Below post on GFG provides a solution with more mathematical involvement. 
 * https://www.geeksforgeeks.org/count-ways-express-number-sum-consecutive-numbers/
 * 
 * Post on SO. 
 * https://stackoverflow.com/questions/4536877/given-a-number-n-find-the-number-of-ways-to-write-it-as-a-sum-of-two-or-more-co/51125171
 * @author ruifengm
 * @since 2018-Jul-1st
 *
 */
public class SumOfConsecutiveInt extends FunIntAlgorithm {
	
	/**
	 * A brute force method is to check all possible consecutive sums. 
	 * 
	 * Note that the outer loop has an upper limit of N/2, as it's obvious that we don't need to 
	 * search in the range N/2 to N. 
	 */
	private static int countByBruteForce(int n) {
		int count = 0; 
		for (int i=1; i<=n/2; i++) {
			int sum = 0; 
			for (int j=i; j<n; j++) {
				sum += j; 
				if (sum == n) {
					count++; 
					break;
				}
				if (sum > n) break; 
			}
		}
		return count; 
	}
	
	/**
	 * Dynamic programming with a memoization array storing all intermediate number series sums 
	 * from 1 to N. 
	 */
	private static int countByDP(int n) {
		int[] sumArr = new int[n+1];
		sumArr[0] = 0; 
		for (int i=1; i<n+1; i++) sumArr[i] = sumArr[i-1] + i; 
		int i=1, j=2, count = 0, sum = 0; 
		while (j<n) {
			sum = sumArr[j] - sumArr[i] + i; 
			if (sum == n) {
//				System.out.println("i: " + i);
//				System.out.println("j: " + j);
				count++; i++; j++;
			} else if (sum > n) i++; 
			else j++; 
		}
		return count; 
	}
	
	/**
	 * Optimized the DP solution by eliminating the memoization array since the intermediate sub-array sums can 
	 * be computed on the fly. Refer to the MaxSubArraySumOfSizeK problem for hint. 
	 */
	
	private static int count(int n) {
		int i = 1, j = 1, count = 0, sum = 1; 
		while (j<n) {
			if (sum == n) { // matched, move sub-array section forward by 1
//				System.out.println("i: " + i);
//				System.out.println("j: " + j);
				count++; 
				sum -= i; 
				i++;
				j++;
				sum +=j; 
			} else if (sum < n) { // not matched yet, extend sub-array at end
				j++; 
				sum += j; 
			} else { // exceeded, reduce sub-array at start
				sum -= i; 
				i++; 
			}
		}
		return count;
	}
	
	public static void main(String[] args) {
		int sum = 59832475;
		//int sum = 21;
		System.out.println("Welcome to the rabbit hole of sum of consecutive positive integers!\n");
		try {
			runIntFuncAndCalculateTime("[BruteForce]     Number of ways to get sum " +  sum + ":" , 
					(int i) -> countByBruteForce(i), sum);
			runIntFuncAndCalculateTime("[DP]             Number of ways to get sum " +  sum + ":" , 
					(int i) -> countByDP(i), sum);
			runIntFuncAndCalculateTime("[Optimized]      Number of ways to get sum " +  sum + ":" , 
					(int i) -> count(i), sum);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("\nAll rabbits gone.");
	}

}
