package maxSubsequenceSum;

import java.util.Arrays;
import utils.FunAlgorithm;

/**
 * Four algorithms that find the maximum subsequence sum of a given array
 * 1) Complexity of O(N3)
 * 2) Complexity of O(N2)
 * 3) Complexity of O(NlogN)
 * 4) Complexity of O(N)
 * 
 * Note: in current context a subsequence has the same definition as a sub-array does, though in general
 *       a subsequence does not have to be contiguous. 
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
	 * Recursive maximum contiguous subsequence sum using the "divide and conquer" strategy
	 * Inception: divide the array into two, the max sum is either in the left side or the right side or spans over
	 *            divide until the array becomes singular
	 * The complexity of this algorithm is O(NlogN)
	 * @param array
	 * @return
	 */
	private static int recursiveMaxSubseqSum(int[] array, int left, int right) {
		if (array == null || array.length == 0) return 0;
		if (left == right) {
			if (array[left] > 0) return array[left];
			else return 0;
		}
		
		int center = (left + right) / 2; 
		int leftMaxSum = recursiveMaxSubseqSum(array, left, center);
		int rightMaxSum = recursiveMaxSubseqSum(array, center + 1, right);
		// calculate max subsequence sum that spans over the left and right side
		int leftBorderSum = 0, leftBorderMaxSum = 0, rightBorderSum = 0, rightBorderMaxSum = 0;
		for (int i=center; i>=left; i--) {
			leftBorderSum += array[i];
			leftBorderMaxSum = Math.max(leftBorderSum, leftBorderMaxSum);
		}
		for (int i=center+1; i<=right; i++) {
			rightBorderSum += array[i];
			rightBorderMaxSum = Math.max(rightBorderSum, rightBorderMaxSum);
		}
		
		return Math.max(Math.max(leftMaxSum, rightMaxSum), leftBorderMaxSum + rightBorderMaxSum);
	}
	
	private static int recursiveMaxSubseqSumDriver(int[] array) {
		return recursiveMaxSubseqSum(array, 0, array.length - 1);
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
		int[] intArray = genRanIntArr(1000);
		//System.out.println("Integer array:" + Arrays.toString(intArray));
		System.out.println("Welcome to the rabbit hole of maximum sugsequence sums!\n"
				+ "The randomly generated integer array is of size " + intArray.length + ".\n"); 
		
		try {
			runIntArrayFuncAndCalculateTime("[Cubic]     Maximum subsequence sum:", (int[] a) -> cubicMaxSubseqSum(a), intArray);
			runIntArrayFuncAndCalculateTime("[Quadratic] Maximum subsequence sum:", (int[] a) -> quadraticMaxSubseqSum(a), intArray);
			runIntArrayFuncAndCalculateTime("[N*Logrithmic] Maximum subsequence sum:", (int[] a) -> recursiveMaxSubseqSumDriver(a), intArray);
			runIntArrayFuncAndCalculateTime("[Linear]    Maximum subsequence sum:", (int[] a) -> linearMaxSubseqSum(a), intArray);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("All rabbits gone.");
	}

}
