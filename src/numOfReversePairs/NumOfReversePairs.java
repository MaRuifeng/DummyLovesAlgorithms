package numOfReversePairs;

import java.util.Arrays;

import utils.FunAlgorithm;

/**
 * [Lintcode] http://www.lintcode.com/en/problem/reverse-pairs/
 * Given an array A, if for index i < j, there is A[i] > A[j], then (A[i], A[j]) is called a reverse pair. 
 * Find the total number of reverse pairs in the array. 
 * 
 * @author ruifengm
 * @since 2018-Feb-20
 */

public class NumOfReversePairs extends FunAlgorithm {
	
	private static long count;
	
	private static long quadraticMethod(int[] a) {
		if (a == null || a.length == 0) return 0; 
		count = 0;
		for (int i=0; i<a.length; i++) {
			for (int j=0; j<i; j++) if (a[j] > a[i]) count++; 
		}
		return count;
	}
	
	/**
	 * Use the "divide and conquer" strategy of the merge sort algorithm. 
	 * During merge, suppose there are X items in left side array to go beyond the right side array by Y items, count increases by X*Y
	 * @param a
	 * @return
	 */
	private static long recursiveMethod(int[] a) {
		if (a == null || a.length == 0) return 0; 
		count = 0;
		mergeSort(a, 0, a.length -1);
		return count;
	}
	
	// override with counting added
	protected static int[] mergeSort(int[] a, int left, int right) {
		if (left == right) {
			return new int[] {a[left]};
		}
		
		int mid = (left + right) / 2; 
		int[] leftSortedArr = mergeSort(a, left, mid);
		int[] rightSortedArr = mergeSort(a, mid + 1, right); 
		// merging
		int[] sortedArr = new int[right - left + 1]; 
		int i = leftSortedArr.length - 1; 
		int j = rightSortedArr.length - 1;
		int k = sortedArr.length - 1; 
		while (k >= 0) {
			if (i < 0) {
				sortedArr[k--] = rightSortedArr[j--];
				continue;
			}
			if (j < 0) {
				sortedArr[k--] = leftSortedArr[i--];
				continue;
			}
			if (leftSortedArr[i] > rightSortedArr [j]) {
				count += (j + 1);
				sortedArr[k--] = leftSortedArr[i--];
			} else sortedArr[k--] = rightSortedArr[j--];
		}
		return sortedArr;
	}
	
	public static void main(String[] args) {
		int[] intArray = genRanIntArr(1000, -5, 10);
		//System.out.println("Integer array:" + Arrays.toString(intArray));
		System.out.println("Welcome to the rabbit hole of reverse pairs!\n"
				+ "The randomly generated integer array is of size " + intArray.length + ".\n"); 
		
		try {
			runIntArrayFuncAndCalculateTime("[Quadratic] Number of reverse pairs:", (int[] a) -> quadraticMethod(a), intArray);
			runIntArrayFuncAndCalculateTime("[N*Logrithmic] Number of reverse pairs:", (int[] a) -> recursiveMethod(a), intArray);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("All rabbits gone.");
	}

}
