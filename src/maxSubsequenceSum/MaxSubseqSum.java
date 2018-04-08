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
	
	/**
	 * The linear method is actually a greedy algorithm. 
	 * Though the idea is similar, we write it in the Dynamic Programming way which has a more rigorous proof. 
	 * 
	 * Let A be an array whose size is n, and for any given position k we define a new operation called positiveSum(k), which denotes
	 * summing up from element 1 to k and setting the sum to 0 whenever it becomes negative. 
	 * 	   postiveSum(k) = max(positiveSum(k-1) + A[k] , 0)
	 * So the positiveSum value is actually the sum of the longest sub-array that ends at k
	 * whose sub-sum value at each element is non-negative when adding up from left to right. 
	 * Based on the definition we know that at each position of that sub-array, the positiveSum value is always non-negative. 
	 * Let Sol(A(k)) denote the maximum subsequence sum at position k, and we can deduce that 
	 *     Sol(A(k)) = max( Sol(A(k-1)), positiveSum(k) )
	 * This can be proved with a series of conclusions that contradict each other. 
	 * Suppose the formula is not true, then there exists another maximum subsequence sum for array A(k) and its value neither equals to the maximum subsequence sum 
	 * of array A(k-1), nor equals to the positiveSum value at k. 
	 * Not maximum subsequence sum of array A(k-1) ==> the subsequence ends with A[k]
	 * Not positiveSum value at k                  ==> the subsequence does not end with A[k]
	 * So the contradiction voids the assumption and the formula is true. 
	 * 
	 * DP can be implemented in either the bottom up tabulation manner or the top down memoization manner.
	 */
	
	// Usage of results from sub-recursive calls makes this method not tail recursive, hence new stack frame 
	// is generated for every recursive call and StackOverflowError is easily
	// encountered when the array size is relatively large. 
	private static int recursiveMaxSubseqSumDP(int[] array, int size, int[] positiveSum) {
		if (size == 1) return Math.max(0, array[size - 1]);
		else return  Math.max(recursiveMaxSubseqSumDP(array, size - 1, positiveSum), positiveSum[size - 1]);
	}
	private static int recursiveMaxSubseqSumDPDriver(int[] array) {
		int[] positiveSum = new int[array.length];
		positiveSum[0] = Math.max(0, array[0]);
		for (int i=1; i<array.length; i++) positiveSum[i] = Math.max(positiveSum[i-1] + array[i], 0);
		return recursiveMaxSubseqSumDP(array, array.length, positiveSum);
	}
	
	// Make it tail recursive by performing calculations first. The compiler will reuse the current stack frame by replacing 
	// the caller with the callee so as to achieve constant space complexity.
	// However... this is useless in Java as its compiler doesn't do tail recursive optimization. 
	// While functional languages support tail recursive optimization by nature (code body repetitions can only be done via recursion), 
	// most of the OOP language compilers do not. 
	// https://softwareengineering.stackexchange.com/questions/272061/why-doesnt-java-have-optimization-for-tail-recursion-at-all
	private static int tailRecursiveMaxSubseqSumDP(int[] array, int size, int positiveSum, int pos, int res) {
		if (size == 1) return res;
		else {
			positiveSum = Math.max(positiveSum + array[pos + 1], 0);
			res = Math.max(positiveSum, res);
			return tailRecursiveMaxSubseqSumDP(array, size - 1, positiveSum, pos + 1, res);
		}
	}
	private static int tailRecursiveMaxSubseqSumDPDriver(int[] array) {
		return tailRecursiveMaxSubseqSumDP(array, array.length, Math.max(0, array[0]), 0, Math.max(0, array[0]));
	}
	
	// The tail recursive method can be easily converted to an iterative one
	private static int iterativeMaxSubseqSumDP(int[] array) {
		int positiveSum = Math.max(0, array[0]);
		int[] resArr = new int[array.length]; 
		resArr[0] = Math.max(0, array[0]); 
		for (int i=1; i<array.length; i++) {
			positiveSum = Math.max(positiveSum + array[i], 0);
			resArr[i] = Math.max(resArr[i-1], positiveSum);
		}
		return resArr[array.length - 1];
	}
	

	public static void main(String[] args) {
		int[] intArray = genRanIntArr(3000, -5, 10);
		//int[] intArray = {1, -5, -1, 6, -3, 6, -3, -1, -4, -3};
		//System.out.println("Integer array:" + Arrays.toString(intArray));
		System.out.println("Welcome to the rabbit hole of maximum sugsequence sums!\n"
				+ "The randomly generated integer array is of size " + intArray.length + ".\n"); 
		
		try {
			runIntArrayFuncAndCalculateTime("[Cubic]                      Maximum subsequence sum:", (int[] a) -> cubicMaxSubseqSum(a), intArray);
			runIntArrayFuncAndCalculateTime("[Quadratic]                  Maximum subsequence sum:", (int[] a) -> quadraticMaxSubseqSum(a), intArray);
			runIntArrayFuncAndCalculateTime("[N*Logrithmic]               Maximum subsequence sum:", (int[] a) -> recursiveMaxSubseqSumDriver(a), intArray);
			runIntArrayFuncAndCalculateTime("[Linear]                     Maximum subsequence sum:", (int[] a) -> linearMaxSubseqSum(a), intArray);
			runIntArrayFuncAndCalculateTime("[Linear][DP][Recursive]      Maximum subsequence sum:", (int[] a) -> recursiveMaxSubseqSumDPDriver(a), intArray);
			runIntArrayFuncAndCalculateTime("[Linear][DP][Tail Recursive] Maximum subsequence sum:", (int[] a) -> tailRecursiveMaxSubseqSumDPDriver(a), intArray);
			runIntArrayFuncAndCalculateTime("[Linear][DP][Iterative]      Maximum subsequence sum:", (int[] a) -> iterativeMaxSubseqSumDP(a), intArray);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("All rabbits gone.");
	}

}
