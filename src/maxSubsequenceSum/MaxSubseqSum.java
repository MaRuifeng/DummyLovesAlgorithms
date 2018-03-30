package maxSubsequenceSum;

import java.util.Arrays;
import utils.FunAlgorithm;

/**
 * Four algorithms that find the maximum subsequence sum of a given array
 * 1) Complexity of O(N3)
 * 2) Complexity of O(N2)
 * 3) Complexity of O(NlogN)
 * 4) Complexity of O(N)
 * @author ruifengm
 * @since 2018-Jan-23
 */

public class MaxSubseqSum extends FunAlgorithm {
	
	/**
	 * Cubic maximum contiguous subsequence sum
	 * Inception: traverse through all possible subsequence sums and find the max
	 * 
	 * @param array
	 * @return int
	 */
	private static int cubicMaxSubseqSum(int[] array) {
		int maxSum = 0;  // assuming no max subseq sum applicable if all negative
		for (int i = 0; i < array.length; i++) {
			for (int j = i; j < array.length; j++) {
				int curSum = 0;
				for (int k = i; k <= j; k++) {
					curSum += array[k]; 
				}
				if (curSum > maxSum) maxSum = curSum;
			}
		}
		return maxSum;
	}
	
	/**
	 * Quadratic maximum contiguous subsequence sum
	 * Inception: improve the cubic version of the algorithm by reducing one loop cycle
	 * 
	 * @param array
	 * @return
	 */
	private static int quadraticMaxSubseqSum(int[] array) {
		int maxSum = 0;  // assuming no max subseq sum applicable if all negative
		for (int i = 0; i < array.length; i++) {
			int curSum = 0; 
			for (int j = i; j < array.length; j++) {
				curSum += array[j];
				if (curSum > maxSum) maxSum = curSum;
			}
		}
		return maxSum;
	}
	
	/**
	 * Linear maximum contiguous subsequence sum
	 * Inception: no negative number should lead the optimal sequence, neither a negative sum; set to zero when encounter
	 * 
	 * @param array
	 * @return int
	 */
	private static int linearMaxSubseqSum(int[] array) {
		int curSum = 0, maxSum = 0;  // assuming no max subseq sum applicable if all negative
		for (int i=0; i<array.length; i++) {
			curSum += array[i];
			if (curSum > maxSum) maxSum = curSum;
			else if (curSum < 0) curSum = 0; 
		}
		return maxSum;
	}

	public static void main(String[] args) {
		int[] intArray = genRanIntArr(10);
		System.out.println("Integer array:" + Arrays.toString(intArray));
		System.out.println("Welcome to rabbit hole of maximum sugsequence sums!\n"
				+ "The randomly generated integer array is of size " + intArray.length + ".\n"); 
		
		try {
			// Find the max subsequence sum with cubic complexity
			runFuncAndCalculateTime("[Cubic]     Maximum subsequence sum:", (int[] a) -> cubicMaxSubseqSum(a), intArray);
			runFuncAndCalculateTime("[Quadratic] Maximum subsequence sum:", (int[] a) -> quadraticMaxSubseqSum(a), intArray);
			runFuncAndCalculateTime("[Linear]    Maximum subsequence sum:", (int[] a) -> linearMaxSubseqSum(a), intArray);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("All rabbits gone.");
	}

}
