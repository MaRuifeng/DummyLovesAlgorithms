package integerArray;

import java.util.Arrays;

/**
 * Given a rotated sorted array, recover it to sorted array in-place.
 * 
 * @author ruifengm
 * @since 2018-Jun-18
 * 
 * https://www.lintcode.com/problem/recover-rotated-sorted-array/description
 */
public class RecoverRotatedSortedArray {
	
	/**
	 * The idea is to use 3-step rotation. 
	 * - Find the rotation pivot. 
	 * - Rotate the left portion. 
	 * - Rotate the right portion.
	 * - Rotate the whole array.
	 */
	
	private static void recover(int[] arr) {
		// int pivot = findPivotLogN(arr, 0, arr.length-1);
		int pivot = findPivot(arr);
		if (pivot == -1) return; 
		rotate(arr, 0, pivot);
		rotate(arr, pivot+1, arr.length-1); 
		rotate(arr, 0, arr.length-1);
	}
	/*
	 * Though in O(LogN) time, below method will fail for arrays with duplicate numbers. 
	 * E.g. {1, 1, 1, 1, 1, 1, 1, 1, 1, -1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}
	 */
	private static int findPivotLogN(int[] arr, int start, int end) { 
		if (end < start) return -1; 
		int mid = start + (end-start)/2;
		if (mid < end && arr[mid] > arr[mid+1]) return mid; 
		if (mid > start && arr[mid-1] > arr[mid]) return mid - 1; 
		if (arr[start] >= arr[mid]) return findPivotLogN(arr, start, mid-1);
		else return findPivotLogN(arr, mid+1, end);
	}
	private static int findPivot(int[] arr) {
		int i = 0; 
		while (i<arr.length-1) {
			if (arr[i] > arr[i+1]) return i;
			i++;
		}
		return -1;
	}
	private static void rotate(int[] arr, int start, int end) {
		int i, j, len = end - start + 1;
		int mid = start + len/2; 
		i = mid - 1;
		if (len%2 == 0) j = mid; 
		else j = mid + 1; 
		while (i >= start) {
			int temp = arr[i]; 
			arr[i] = arr[j];
			arr[j] = temp; 
			i--; j++;
		}
	}
	
	public static void main(String[] args) {
		System.out.println("Welcome to the rabbit hole of integer array rotators!");
//		int[] array = {4, 5, 6, 7, 1, 2, 3}; 
//		int[] array = {7, 1, 2, 3, 4, 5, 6}; 
//		int[] array = {2, 3, 4, 5, 6, 7, 1}; 
//		int[] array = {1, 2, 3, 4, 5, 6, 7}; 
		int[] array = {1, 1, 1, 1, 1, 1, 1, 1, 1, -1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
		System.out.println(Arrays.toString(array));
		recover(array);
		System.out.println(Arrays.toString(array));
		System.out.println("All rabbits gone.");
	}

}
