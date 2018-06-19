package dynamicProgramming;

import utils.FunIntAlgorithm;

/**
 * Given an array of integers and a number k, find k non-overlapping subarrays which have the largest sum.
 * The number in each subarray should be contiguous.
 * The subarray should contain at least one number. 
 * 
 * @author ruifengm
 * @since 2018-Jun-19
 * 
 * https://www.lintcode.com/problem/maximum-subarray-iii/description
 * https://www.lintcode.com/problem/maximum-subarray-iii/note/147076
 * https://pobenliu.gitbooks.io/leetcode/Maximum%20Subarray%20III.html
 */
public class MaxKSubArraySum extends FunIntAlgorithm {
	
	/**
	 * We try to look for sub-problem patterns and solve it in a recursive manner. 
	 * Let A(n) be an integer array of size n, and let k be the number of subarrays to be partitioned. 
	 * Let Sol(A(n), k) be the largest sum of the k non-overlapping subarrays. Then there is 
	 * 
	 * Sol(A(n), k) = MAX( Sol(A(n-1), k-1) + maxSubArraySum(A, m, n),        --> A(n-1) partitioned into k-1 subarrays, where m is 
	 *                                                                            the index right after the last subarray of Sol(A(n-1), k-1)
	 *                     Sol(A(n-1), k) + Math.max(0, SUM(A, m, n)) )       --> A(n-1) partitioned into k subarrays, where m is 
	 *                                                                            the index right after the last subarray of Sol(A(n-1, k)
	 * When A(n-1) gets partitioned into k-1 subarrays to yield the local max sum, we need to find 
	 * one more sub array that produces the max sum in the remaining elements till A[n].
	 * When A(n-1) gets partitioned into k subarrays to yield the local max sum, we need to check 
	 * whether the last subarray can be extended to A[n] to produce a larger sum.
	 */
	
	private static class Result {
		int sum;
		int lastItemIdx; // index of the last item in the last subarray that is part of the solution
		public Result(int sum, int lastItemIdx) {
			this.sum = sum;
			this.lastItemIdx = lastItemIdx;
		}
		public String toString() {
			return "{" + this.sum + ", " + this.lastItemIdx + "}";
		}
	}
	private static Result recursiveMaxKSubArraySum(int[] arr, int size, int k) {
		if (size == k) {
			int sum = 0; 
			for (int i=0; i<size; i++) sum += arr[i]; 
			return new Result(sum, size-1);
		}
		if (k == 0) return new Result(0, -1);
		Result excl = recursiveMaxKSubArraySum(arr, size-1, k-1);
		Result incl = recursiveMaxKSubArraySum(arr, size-1, k); 
		
		// for Sol(A(n-1), k-1), look for the last subarray with max sum
		int maxSum = Integer.MIN_VALUE, curSum = 0, exclIdx = -1;
		for (int i=excl.lastItemIdx+1; i<size; i++) {
			curSum = Math.max(curSum + arr[i], arr[i]);
			if (curSum >= maxSum) {
				maxSum = curSum; 
				exclIdx = i;
			}
		}
		
		// for Sol(A(n-1), k), check whether the last subarray can be extended
		int extraSum = 0, inclIdx = incl.lastItemIdx;
		for (int i=incl.lastItemIdx+1; i<size; i++) extraSum += arr[i];
		if (extraSum >= 0) inclIdx = size-1; 
		
		int exclSum = excl.sum + maxSum;
		int inclSum = incl.sum + Math.max(0, extraSum);
		if (exclSum > inclSum) return new Result(exclSum, exclIdx);
		else return new Result(inclSum, inclIdx);
	}
	private static int recursiveMaxKSubArraySum(int[] arr, int k) {
		int size = arr.length;
		if (k > size) return Integer.MIN_VALUE;
		return recursiveMaxKSubArraySum(arr, size, k).sum;
	}
	
	/**
	 * We try to optimize the recursive method via DP memoization. 
	 */
	private static Result recursiveMaxKSubArraySumDPMemo(int[] arr, int size, int k, Result[][] table) {
		if (table[size][k] != null) return table[size][k]; 
		else {
			if (size == k) {
				int sum = 0; 
				for (int i=0; i<size; i++) sum += arr[i]; 
				table[size][k] = new Result(sum, size-1);
			} else if (k == 0) table[size][k] = new Result(0, -1);
			else {
				Result excl = recursiveMaxKSubArraySumDPMemo(arr, size-1, k-1, table);
				Result incl = recursiveMaxKSubArraySumDPMemo(arr, size-1, k, table); 
				
				// for Sol(A(n-1), k-1), look for the last subarray with max sum
				int maxSum = Integer.MIN_VALUE, curSum = 0, exclIdx = -1;
				for (int i=excl.lastItemIdx+1; i<size; i++) {
					curSum = Math.max(curSum + arr[i], arr[i]);
					if (curSum >= maxSum) {
						maxSum = curSum; 
						exclIdx = i;
					}
				}
				
				// for Sol(A(n-1), k), check whether the last subarray can be extended
				int extraSum = 0, inclIdx = incl.lastItemIdx;
				for (int i=incl.lastItemIdx+1; i<size; i++) extraSum += arr[i];
				if (extraSum >= 0) inclIdx = size-1; 
				
				int exclSum = excl.sum + maxSum;
				int inclSum = incl.sum + Math.max(0, extraSum);
				if (exclSum > inclSum) table[size][k] = new Result(exclSum, exclIdx);
				else table[size][k] = new Result(inclSum, inclIdx);
			}
			return table[size][k];
		}
	}
	private static int recursiveMaxKSubArraySumDPMemo(int[] arr, int k) {
		int size = arr.length; 
		if (k > size) return Integer.MIN_VALUE;
		Result[][] DPLookUp = new Result[size+1][k+1];
		int sum = recursiveMaxKSubArraySumDPMemo(arr, size, k, DPLookUp).sum;
//		for (Result[] resArr: DPLookUp) {
//			System.out.print("[ ");
//			for (Result res: resArr) {
//				if (res == null) System.out.print("{null} ");
//				else System.out.print(res.toString() + " ");
//			}
//			System.out.print("]");
//			System.out.println();
//		}
		return sum;
	}
	
	/**
	 * We try to optimize the recursive method via DP tabulation.
	 */
	private static int iterativeMaxKSubArraySumDPTabu(int[] arr, int k) {
		int size = arr.length; 
		if (k > size) return Integer.MIN_VALUE;
		Result[][] table = new Result[size+1][k+1]; // DP lookup table
		// base state
		for (int i=0; i<=size; i++) table[i][0] = new Result(0, -1);
		// proliferation
		for (int j=1; j<=k; j++) {
			for (int i=j; i<=size; i++) {
				if (i==j) table[i][j] = new Result(table[i-1][j-1].sum + arr[i-1], i-1);
				else {
					int maxSum = Integer.MIN_VALUE, curSum = 0, exclIdx = -1;
					for (int m=table[i-1][j-1].lastItemIdx+1; m<i; m++) {
						curSum = Math.max(curSum + arr[m], arr[m]);
						if (curSum >= maxSum) {
							maxSum = curSum; 
							exclIdx = m;
						}
					}
					
					int extraSum = 0, inclIdx = table[i-1][j].lastItemIdx;
					for (int m=table[i-1][j].lastItemIdx+1; m<i; m++) extraSum += arr[m]; 
					if (extraSum >= 0) inclIdx = i-1; 
					
					int exclSum = table[i-1][j-1].sum + maxSum;
					int inclSum = table[i-1][j].sum + Math.max(0, extraSum);
					
					if (exclSum > inclSum) table[i][j] = new Result(exclSum, exclIdx);
					else table[i][j] = new Result(inclSum, inclIdx);
				}
			}
		}
		
//		for (Result[] resArr: table) {
//			System.out.print("[ ");
//			for (Result res: resArr) {
//				if (res == null) System.out.print("{null} ");
//				else System.out.print(res.toString() + " ");
//			}
//			System.out.print("]");
//			System.out.println();
//		}
		return table[size][k].sum;
	}
	
	public static void main(String[] args) {
		int[] intArray = genRanIntArr(200, -99, 99);
	
		//int[] intArray = {1, -5, -1, 6, -3, 6, -3, -1, -4, -3};
		//int[] intArray = {-1, 2, 3, -2, 4};
		//int[] intArray = {-1, 4, -2, 3, -2, 3};		
		int k = 47;
		// System.out.println("Integer array:" + Arrays.toString(intArray));
		System.out.println("Welcome to the rabbit hole of maximum subarray sums!\n"
				+ "The randomly generated integer array is of size " + intArray.length + ".\n"
				+ "The number of subarray partitions is " + k + ".\n"); 
		
		try {
//			runIntArrayFuncAndCalculateTime("[Recursive][Exponential]      Maximum k subarray sum:", 
//					(int[] a, int b) -> recursiveMaxKSubArraySum(a, b), intArray, k);
			runIntArrayFuncAndCalculateTime("[Recursive][DP Memo][]        Maximum k subarray sum:", 
					(int[] a, int b) -> recursiveMaxKSubArraySumDPMemo(a, b), intArray, k);
			runIntArrayFuncAndCalculateTime("[Iterative][DP Tabu][]        Maximum k subarray sum:", 
					(int[] a, int b) -> iterativeMaxKSubArraySumDPTabu(a, b), intArray, k);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("All rabbits gone.");
	}

}
