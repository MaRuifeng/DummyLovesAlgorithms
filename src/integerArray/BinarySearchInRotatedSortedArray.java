package integerArray;

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

import utils.FunIntAlgorithm;

/**
 * Given an array of unique integers sorted in ascending order, which is also rotated at 
 * an unkown pivot, find the position of a given target value. Return -1 if not found. 
 * 
 * E.g. [0,1,2,4,5,6,7] might become [4,5,6,7,0,1,2]
 * 
 * @author ruifengm
 * @since 2018-May-9
 * 
 * Leetcode link:
 * https://leetcode.com/problems/search-in-rotated-sorted-array/description/
 */

public class BinarySearchInRotatedSortedArray extends FunIntAlgorithm {
	
	/**
	 * Apply normal binary search in both halves if needed until the rotation pivot is encountered.
	 */
	private static int recursiveBinarySearch(int[] a, int t, int start, int end) {
		if (end<start) return -1; 
		int middle = (start+end) >> 1; 
		if (a[middle] == t) return middle; 
		else if (t < a[middle]) return recursiveBinarySearch(a, t, start, middle-1);
		else return recursiveBinarySearch(a, t, middle+1, end);
	}
	
	/**
	 * Apply normal binary search in both halves if needed until the rotation pivot is encountered.
	 */
	private static int recursivePivotBinarySearch(int[] a, int t, int start, int end, boolean pivotFound) {
		if (end<start) return -1; 
		int middle = (start+end) >> 1; 
		boolean nextPivotFound = false; 
		if (pivotFound == false && middle < a.length-1 && a[middle] > a[middle+1]) nextPivotFound = true;
		if (a[middle] == t) return middle; 
		else if (t < a[middle]) {
			int idx = recursivePivotBinarySearch(a, t, start, middle-1, nextPivotFound);
			if (idx == -1 && !pivotFound) 
				return recursivePivotBinarySearch(a, t, middle+1, end, nextPivotFound);
			else return idx;
		}
		else {
			int idx = recursivePivotBinarySearch(a, t, middle+1, end, nextPivotFound);
			if (idx == -1 && !pivotFound) 
				return recursivePivotBinarySearch(a, t, start, middle-1, nextPivotFound);
			else return idx;
		}
	}
	private static int recursivePivotBinarySearchDriver(int[] a, int t) {
		return recursivePivotBinarySearch(a, t, 0, a.length-1, false);
	}
	
	/**
	 * A clearer implementation with pivot searched first. 
	 */
	private static int recursivePivotBinarySearch2 (int[] a, int t) {
		int pivot = findPivot(a, 0, a.length-1); 
		if (pivot == -1) return recursiveBinarySearch(a, t, 0, a.length-1);
		if (t == a[pivot]) return pivot; 
		if (a[0] <= t && t <= a[pivot]) return recursiveBinarySearch(a, t, 0, pivot); 
		else return recursiveBinarySearch(a, t, pivot+1, a.length-1);
	}
	private static int findPivot(int[] a, int start, int end) {
		if (end < start) return -1; // no rotation pivot, array is sorted
		int middle = (start + end) >> 1; 
		if (middle < a.length-1 && a[middle] > a[middle+1]) return middle;
		if (0 < middle && a[middle-1] > a[middle]) return middle-1; 
		else if (a[start] >= a[middle]) return findPivot(a, start, middle-1); 
		else return findPivot(a, middle+1, end);
	}
	
	/**
	 * Pivot binary search in one shot.
	 */
	private static int recursivePivotBinarySearch3(int[] a, int t, int start, int end) {
		if (end<start) return -1; 
		int middle = (start+end) >> 1; 
		if (a[middle] == t) return middle; 
		if (a[start] <= a[middle]) { // left half sorted
			if (a[middle] >= t && a[start] <= t) return recursivePivotBinarySearch3(a, t, start, middle-1);
			else return recursivePivotBinarySearch3(a, t, middle+1, end);
		}
		else { // right half sorted
			if (a[middle] <= t && a[end] >= t) return recursivePivotBinarySearch3(a, t, middle+1, end);
			else return recursivePivotBinarySearch3(a, t, start, middle-1);
		}
	}
	private static int recursivePivotBinarySearch3Driver(int[] a, int t) {
		return recursivePivotBinarySearch3(a, t, 0, a.length-1);
	}
	
	/**
	 * Iterative pivot binary search
	 */
	private static int iterativePivotBinarySearch(int[] a, int t) {
		int start = 0, end = a.length-1;
		while (start <= end) {
			int middle = (start+end) / 2; 
			if (a[middle] == t) return middle; 
			if (a[start] <= a[middle]) { // left half sorted
				if (t <= a[middle] && t >= a[start]) end = middle-1; 
				else start = middle+1; 
			} else { // right half sorted
				if (t >= a[middle] && t <= a[end]) start = middle+1; 
				else end = middle-1;
			}
		}
		return -1;
	}
	
	public static void main(String[] args) {
		int[] intArray = new int[1000]; 
		for (int i=0; i<1000; i++) intArray[i] = i;
		quickSort(intArray, 0, intArray.length-1);
		int pivot = ThreadLocalRandom.current().nextInt(0, intArray.length-1);
		int[] arr1 = Arrays.copyOfRange(intArray, 0, pivot);
		int[] arr2 = Arrays.copyOfRange(intArray, pivot, intArray.length);
		System.arraycopy(arr2, 0, intArray, 0, arr2.length);
		System.arraycopy(arr1, 0, intArray, arr2.length, arr1.length);
		//int[] intArray = {3, 4, 1, 2};
		//int[] intArray = {4, 5, 6, 7, 0, 1, 2};
		//int[] intArray = {9, 10, 11, 12, 0, 1, 2, 3, 4, 5, 6, 7, 8};
		//int[] intArray = {12, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11};
		int target = intArray[pivot];
		System.out.println("Welcome to the rabbit hole of binary searches!\n"
				+ "The integer set is \n" + Arrays.toString(intArray) + "\n"
				+ "The target value is " + target + ".\n"); 
		
		try {
			runIntArrayFuncAndCalculateTime("[Recursive][O(logN)     Index of target value: ", (int[] a, int s) -> recursivePivotBinarySearchDriver(a, s), intArray, target);
			runIntArrayFuncAndCalculateTime("[Recursive][O(logN)     Index of target value: ", (int[] a, int s) -> recursivePivotBinarySearch2(a, s), intArray, target);
			runIntArrayFuncAndCalculateTime("[Recursive][O(logN)     Index of target value: ", (int[] a, int s) -> recursivePivotBinarySearch3Driver(a, s), intArray, target);
			runIntArrayFuncAndCalculateTime("[Iterative][O(logN)     Index of target value: ", (int[] a, int s) -> iterativePivotBinarySearch(a, s), intArray, target);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("All rabbits gone.");
	}

}
