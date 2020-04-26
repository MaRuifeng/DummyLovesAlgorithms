package integerArray.maxSubarraySumOfSizeK;

import java.util.Arrays;

import utils.FunIntAlgorithm;

/**
 * Given an integer array of size n, find the maximum sum of a sub-array with given size of k where k<=n. 
 * @author ruifengm
 * @since 2018-Feb-23
 */

public class MaxSubArraySumOfSizeK extends FunIntAlgorithm {
	
	/**
	 * The most intuitive solution is to find all possible sub-arrays of size k, sum up the elements for each and then find the max. 
	 * This gives a time complexity upper bound of O(n*k). 
	 * @param a
	 * @param k
	 * @return
	 */
	private static long maxSubArraySum_Onk(int[] a, int k) {
		if (a == null || a.length == 0) return 0; 
		
		long maxSum = Integer.MIN_VALUE;
		for (int i=0; i<=a.length-k; i++) {
			long curSum = 0;
			for (int j=i; j<k+i; j++) curSum += a[j]; 
			maxSum = Math.max(maxSum, curSum);
		}
		return maxSum;
	}
	

	/**
	 * The O(N*k) algorithm is not good enough because of the repeated addition operations when performing the sub-array sums freshly. 
	 * Actually a previously calculated sum can be used to obtain the new sum by simply subtracting the first element and adding 
	 * a new one right after the last. 
	 * This could bring the time complexity down to O(N).
	 * @param a
	 * @param k
	 * @return
	 */
	private static long maxSubArraySum_On(int[] a, int k) {
		if (a == null || a.length == 0) return 0;
		long maxSum = Integer.MIN_VALUE, curSum = 0; 
		// Calculating the starting sub-array sum
		for (int i=0; i<k; i++) curSum += a[i];
		maxSum = Math.max(maxSum, curSum);
		for (int i=1; i<=a.length-k; i++) {
			curSum = curSum - a[i-1] + a[i+k-1];
			maxSum = Math.max(maxSum, curSum);
		}
		return maxSum;
	}
	
	public static void main(String[] args) {
		int[] intArray = genRanIntArr(1000000000, -5, 10);
		int subArraySize = 100;
		//System.out.println("Integer array:" + Arrays.toString(intArray));
		System.out.println("Welcome to the rabbit hole of maximum sum of sub-array of size K!\n"
				+ "The randomly generated integer array is of size " + intArray.length + ".\n"
				+ "And the sub-array should have a size of " + subArraySize + ".\n"); 
		
		try {
			runIntArrayFuncAndCalculateTime("[N*k]    Maximum sub-array sum:", (int[] a, int k) -> maxSubArraySum_Onk(a, k), intArray, subArraySize);
			runIntArrayFuncAndCalculateTime("[Linear] Maximum sub-array sum:", (int[] a, int k) -> maxSubArraySum_On(a, k), intArray, subArraySize);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("All rabbits gone.");
	}

}
