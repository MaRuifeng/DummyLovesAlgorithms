package maxSubsequenceDiff;

import java.util.Arrays;

import utils.FunAlgorithm;

/**
 * [Lintcode]http://www.lintcode.com/en/problem/maximum-subarray-difference/
 * 
 * Given an array with integers. Find two non-overlapping subarrays A and B, which |SUM(A) - SUM(B)| is the largest.
 * Return the largest difference.
 * Note: The subarray should contain at least one number
 * Challenge: O(n) time and O(n) space
 * 
 * Analysis: Let's ignore the requirement of non-overlapping first, since the question is asking for the largest absolute 
 *           value of SUM(A) - SUM(B), it can be 
 *           interpreted as [maximum subsequence sum] - [minimum subsequence sum]. Hence the problem is easily 
 *           translated to the max(min) subsequence sum problem. 
 *           [Case 1]: all numbers are positive => [maximum subsequence sum] - [minimum value in array] (since subarray size > 0)
 *           [Case 2]: all numbers are negative => [maximum value in array] - [minimum subsequence sum] (since subarrar size > 0)
 *           [case 3]: mixed positive and negative numbers => [maximum subsequence sum] - [minimum subsequence sum]
 *           
 *           To take care of the requirement of non-overlapping, the question can be interpreted as 
 *           1) Traverse the whole array
 *           2) At every i'th location (1 to size-2, because no empty subarray allowed), find 
 *           	a) the difference between the maximum subsequence sum of left side and the minimum subsequence sum of right side
 *              b) the difference between the minimum subsequence sum of left side and the maximum subsequence sum of right side
 *              and then take the difference that has a larger absolute value
 *           3) Find the maximum from the absolute values of all such differences
 * @author ruifengm
 * @date 2018-Feb-16
 */
public class MaxSubseqDiff extends FunAlgorithm {
	
	private static int max(int[] arr) {
		int max = arr[0];
		for (int i=0; i<arr.length; i++) {
			if (arr[i] > max) max = arr[i];
		}
		return max;
	}
	
	private static int min(int[] arr) {
		int min = arr[0];
		for (int i=0; i<arr.length; i++) {
			if (arr[i] < min) min = arr[i];
		}
		return min;
	}
	
	private static int maxSubseqSum(int[] arr) {
		int curSum = 0; 
		int maxSum = max(arr);
		for (int i=0; i<arr.length; i++) {
			curSum += arr[i];
			if (curSum > maxSum) maxSum = curSum; 
			if (curSum < 0) curSum = 0;
		}	
		return maxSum;
	}
	
	private static int minSubseqSum(int[] arr) {
		int curSum = 0;
		int minSum = min(arr);
		for (int i=0; i<arr.length; i++) {
			curSum += arr[i];
			if (curSum < minSum) minSum = curSum; 
			if (curSum > 0) curSum = 0;
		}
		return minSum;
	}
	
	/**
	 * Apply the linear max/min subsequence sum algorithm to solve the problem. 
	 * Note: Though this method is very easy to read, its complexity is still O(N2) because of the nested loops...
	 *       Actually this is expected because each divided sub-array is sent to the algorithm, hence resulting in a lot
	 *       of repeated computations. 
	 *       The linear max/min subsequence sum algorithm is an "on-line algorithm", which means that at any point of time, the 
	 *       algorithm can correctly give an answer to the "partial parent array" it has processed. 
	 *       We can utilize that characteristic to bring the complexity down to linear. See runCalculation_On(). 
	 * @param intArray
	 * @return
	 */
	private static int runCalculation_On2(int[] intArray) {
		if (intArray == null || intArray.length == 0) return 0;
		int maxAbsDiff = 0; 
		int[] absDiffArr = new int[intArray.length - 1]; 
		
		for (int i=1; i<=intArray.length-1; i++) {
			int leftMax = maxSubseqSum(Arrays.copyOfRange(intArray, 0, i));
			int leftMin = minSubseqSum(Arrays.copyOfRange(intArray, 0, i));
			int rightMax = maxSubseqSum(Arrays.copyOfRange(intArray, i, intArray.length));
			int rightMin = minSubseqSum(Arrays.copyOfRange(intArray, i, intArray.length));
			absDiffArr[i-1] = Math.max(Math.abs(leftMax - rightMin), Math.abs(leftMin - rightMax)); 
			if (absDiffArr[i-1] > maxAbsDiff) maxAbsDiff = absDiffArr[i-1];	
		}
		return maxAbsDiff;
	}
	
	/**
	 * 1) Traverse the given array from left to right and store all left side max/min subsequence sums found after each element excluding the last one
	 * 2) Traverse the given array from right to left and store all right side max/min subsequence sums found after each element excluding the first one
	 * 3) Calculate max(abs(left_max - right_min) - abs(left_min - right_max)) across the sums found at each location
	 * 4) Return the largest
	 * @param intArray
	 * @return
	 */
	private static int runCalculation_On(int[] intArray) {
		if (intArray == null || intArray.length == 0) return 0;
		int maxAbsDiff = 0; 
		int[] leftMax = new int[intArray.length - 1]; // Array storing max subsequence sums for array [0, n], where n <= intArray.length-2
		int[] leftMin = new int[intArray.length - 1]; // Array storing min subsequence sums for array [0, n], where n <= intArray.length-2
		int[] rightMax = new int[intArray.length - 1]; // Array storing max subsequence sums for array [n, intArray.length-1], where n >= 1
		int[] rightMin = new int[intArray.length - 1]; // Array storing max subsequence sums for array [n, intArray.length-1], where n >= 1
		
		int curSumMax = 0;
		int curSumMin = 0;
		int maxSum = intArray[0];
		int minSum = intArray[0];
		for (int i=0; i<intArray.length-1; i++) {
			// populate leftMax array
			curSumMax += intArray[i];
			if (maxSum < curSumMax) maxSum = curSumMax;
			leftMax[i] = maxSum;
			if (curSumMax < 0) curSumMax = 0;
			
			// populate leftMin array 
			curSumMin += intArray[i];
			if (minSum > curSumMin) minSum = curSumMin;
			leftMin[i] = minSum;
			if (curSumMin > 0) curSumMin = 0;
		}
		
		curSumMax = 0;
		curSumMin = 0;
		maxSum = intArray[intArray.length - 1];
		minSum = intArray[intArray.length - 1];
		for (int i=intArray.length - 1; i>=1; i--) {
			// populate the rightMax array
			curSumMax += intArray[i];
			if (maxSum < curSumMax) maxSum = curSumMax;
			rightMax[i-1] = maxSum;
			if (curSumMax < 0) curSumMax = 0;
			
			// populate rightMin array
			curSumMin += intArray[i];
			if (minSum > curSumMin) minSum = curSumMin;
			rightMin[i-1] = minSum;
			if (curSumMin > 0) curSumMin = 0;
		}
		
		for (int i=0; i<intArray.length-1; i++) {
			int absDiff = Math.max(Math.abs(leftMax[i] - rightMin[i]), Math.abs(leftMin[i] - rightMax[i]));
			if (absDiff > maxAbsDiff) maxAbsDiff = absDiff; 
		}
		return maxAbsDiff;
	}
	
	/**
	 * Another method found at https://blog.csdn.net/gqk289/article/details/68958119 to cross check
	 * @param nums
	 * @return
	 */
    public static int check(int[] nums) {  
        // write your code here  
        if (nums == null || nums.length == 0) {  
            return 0;  
        }  
        int n = nums.length;  
        int[] leftMin = new int[n + 1];  
        int[] leftMax = new int[n + 1];  
        leftMin[0] = Integer.MAX_VALUE;  
        leftMax[0] = Integer.MIN_VALUE;  
        int curMin = 0;  
        int curMax = 0;  
        for (int i = 0; i < n; i++) {  
            curMin = Math.min(nums[i], curMin + nums[i]);  
            curMax = Math.max(nums[i], curMax + nums[i]);  
            leftMax[i + 1] = Math.max(leftMax[i], curMax);  
            leftMin[i + 1] = Math.min(leftMin[i], curMin);  
        }  
        int res = 0;  
        curMin = 0;  
        curMax = 0;  
        int max = Integer.MIN_VALUE;  
        int min = Integer.MAX_VALUE;  
        for (int i = n - 1; i > 0; i--) {  
            curMax = Math.max(nums[i], curMax + nums[i]);  
            curMin = Math.min(nums[i], curMin + nums[i]);  
            max = Math.max(max, curMax);  
            min = Math.min(min, curMin);  
            res = Math.max(res, Math.max(Math.abs(max - leftMin[i]), Math.abs(leftMax[i] - min)));  
        }  
        return res;  
    } 
	
	public static void main(String args[]) {
		System.out.println("Welcome to the rabbit hole of maximum sugsequence diffs!\n"); 
		int[] intArray; 
		
		/* All positive numbers */
		System.out.println("# Simple test case 1: all positive numbers");
		intArray = new int[]{1, 7, 2, 3, 4};
		System.out.println("The array is: " + Arrays.toString(intArray) + " and the expected answer is " + check(intArray) + ".");
		System.out.println("The calculated maximum subsequence difference of the positive integer array is: " + runCalculation_On(intArray));
		System.out.println("#############\n");
		
		/* All negative numbers */
		System.out.println("# Simple test case 2: all negative numbers");
		intArray = new int[]{-2, -3, -1, -6, -4};
		System.out.println("The array is: " + Arrays.toString(intArray) + " and the expected answer is " + check(intArray) + ".");
		System.out.println("The calculated maximum subsequence difference of the negative integer array is: " + runCalculation_On(intArray));
		System.out.println("#############\n");
		
		/* Mixed numbers */
		System.out.println("# Simple test case 3: mixed numbers");
		intArray = new int[]{1, 2, -3, -1, 2};
		System.out.println("The array is: " + Arrays.toString(intArray) + " and the expected answer is " + check(intArray) + ".");
		System.out.println("The calculated maximum subsequence difference of the mixed integer array is: " + runCalculation_On(intArray));
		System.out.println("#############\n");
		
		/* Randomly generated numbers */
		try {
		    System.out.println("Randmly generating integer arrays ...");
		    intArray = genRanIntArr(1000, -5, 10);
			System.out.println("\n######\nThe randomly generated integer array is of size " + intArray.length + ".");
			System.out.println("The expected difference is " + check(intArray) + ".");
			runIntArrayFuncAndCalculateTime("[Quadratic]     Maximum subsequence difference:", (int[] a) -> runCalculation_On2(a), intArray);
			runIntArrayFuncAndCalculateTime("[Linear]     Maximum subsequence difference:", (int[] a) -> runCalculation_On(a), intArray);

		    intArray = genRanIntArr(10000, -5, 10);
			System.out.println("\n######\nThe randomly generated integer array is of size " + intArray.length + ".");
			System.out.println("The expected difference is " + check(intArray) + ".");
			runIntArrayFuncAndCalculateTime("[Quadratic]     Maximum subsequence difference:", (int[] a) -> runCalculation_On2(a), intArray);
			runIntArrayFuncAndCalculateTime("[Linear]     Maximum subsequence difference:", (int[] a) -> runCalculation_On(a), intArray);

			
		    intArray = genRanIntArr(100000, -5, 10);
			System.out.println("\n######\nThe randomly generated integer array is of size " + intArray.length + ".");
			System.out.println("The expected difference is " + check(intArray) + ".");
			runIntArrayFuncAndCalculateTime("[Quadratic]     Maximum subsequence difference:", (int[] a) -> runCalculation_On2(a), intArray);
			runIntArrayFuncAndCalculateTime("[Linear]     Maximum subsequence difference:", (int[] a) -> runCalculation_On(a), intArray);

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("\nAll rabbits gone.");
	}

}
