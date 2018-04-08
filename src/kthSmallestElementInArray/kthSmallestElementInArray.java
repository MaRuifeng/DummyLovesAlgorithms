package kthSmallestElementInArray;

import java.util.ArrayList;
import java.util.Arrays;

import utils.FunAlgorithm;

/**
 * Find the k'th smallest element in an unsorted array of distinct integers, where k is smaller than the size of the array. 
 * 
 * Only two simple and intuitive methods are discussed here for now. Below link contains some other algorithms involving 
 * more profound data structures like heap, which the author has no knowledge of yet ...
 * 
 * And other randomized algorithm like quickselect that gives an O(N) in average and O(N2) in worst case scenario, and 
 * introselect that further optimizes the worst case scenario are only documented here for reference purpose. 
 * 
 * https://www.geeksforgeeks.org/kth-smallestlargest-element-unsorted-array/
 * @author ruifengm
 * @since 2018-Apr-01
 */
public class kthSmallestElementInArray extends FunAlgorithm {
	
	/**
	 * As the name suggests, the complexity is clearly with an upper bound of O(N*k). In practice, if k > n/2, the problem can be converted into 
	 * finding the (n-k+1)'th largest element problem, so as to reduce operations. 
	 * 
	 * Although not necessary, the method uses ADT and recursion just for practice purpose. 
	 * @param a
	 * @param k
	 * @return
	 * @throws Exception 
	 */
	private static ArrayList<Integer> maxList = new ArrayList<Integer>();
	private static void findKthSmallestByBruteForce(ArrayList<Integer> a, int k) throws Exception {
		if (a == null || a.size() == 0) throw new Exception("Given array is either null or empty.");
		if (k > a.size()) throw new Exception("Given index exceeds array bound");
		
		int result = Integer.MIN_VALUE; 
		for (Integer i: a) result = Math.max(result, i.intValue());
		if (a.size() > k)  {	
			a.remove(new Integer(result));
			findKthSmallestByBruteForce(a, k);
		}
		maxList.add(new Integer(result));
	}
	
	private static int findKthSmallestByBruteForceDriver(int[] a, int k) throws Exception {
		ArrayList<Integer> aList = new ArrayList<Integer>();
		for (int i: a) aList.add(new Integer(i));
		findKthSmallestByBruteForce(aList, k);
		return maxList.get(0);
	}
	
	/** 
	 * Sort the array using an efficient sorting algorithm like merge sort, and then return the (n-k+1)'th element.
	 * The time complexity follows that of the sorting algorithm, which is O(NLogN) in this case. 
	 * @param a
	 * @param k
	 * @return
	 */
	private static int findKthSmallestBySorting(int[] a, int k) throws Exception {
		if (a == null || a.length == 0) throw new Exception("Given array is either null or empty.");
		int[] sortedArr = mergeSort(a, 0, a.length - 1);
		return sortedArr[k - 1];
//		quickSort(a, 0, a.length - 1);
//		return a[k-1];
	}
	
	/**
	 * During quick sort for ascending order, if the found correct position of a pivot happens to be k - 1, 
	 * then the pivot is the the k'th smallest number. Besides, the recursive quick sort can be limited to the section
	 * where k is included.
	 * 
	 * This algorithm works in O(N) time complexity on average.
	 */
	private static int findkthSmallestByQuickSelecting(int[] a, int k) throws Exception {
		if (a == null || a.length == 0) throw new Exception("Given array is either null or empty.");
		quickSelect(a, 0, a.length - 1, k);
		return a[k-1];
	}
	
    private static void quickSelect(int[] a, int start, int end, int k) {
    	if (start <= end) {
//    		System.out.println("Start: " + start);
//    		System.out.println("End: " + end);
    		int pivotPos = partition(a, start, end);
//    		System.out.println("Pivot pos: " + pivotPos);
//    		System.out.println("Pivot value: " + a[pivotPos]);
//    		System.out.println(Arrays.toString(a));
    		if (pivotPos == (k - 1)) {
    			System.out.println("Pivot position: " + pivotPos);
    			System.out.println("Pivot value: " + a[pivotPos]);
    			return; 
    		}
    		else if (pivotPos > (k-1)) quickSelect(a, start, pivotPos - 1, k); // select from items before the pivot
    		else quickSelect(a, pivotPos + 1, end, k); // select from items after the pivot
    	}
    }
    
    private static int partition(int[] a, int start, int end) {
    	int pivotVal = a[end];
    	int temp;
    	int i = start - 1; // index pointing to elements smaller than the pivot
    	for (int j=start; j<end; j++) {
    		if (a[j] <= pivotVal) {
    			// moving the smaller elements before the larger ones
    			i++; 
    			temp = a[j];
    			a[j] = a[i];
    			a[i] = temp;
    		}
    	}
    	// All elements smaller than the pivot are now placed before those larger than the pivot, and
    	// i is pointing to the last element smaller than the pivot. 
    	// Now move the pivot to its correct position, which is just in between.
    	temp = a[i+1]; 
    	a[i+1] = a[end]; 
    	a[end] = temp;
    	return i+1; // correct position of the pivot in the sorted array
    }
	
	public static void main(String[] args) {
		int[] intArray = genRanUniqueIntArr(10000);
		//int[] intArray = {-36, -92, 90, 40, -51, 63, 49, -71, -64, -52, 82, -32, 31, -28, 65, 81, 92, -98, -44, -81};
		int location = 8000; 
		//System.out.println("Integer array:" + Arrays.toString(intArray));
		System.out.println("Welcome to the rabbit hole of k'th smallest element in an array of distinct integers!\n"
				+ "The randomly generated integer array is of size " + intArray.length + ".\n"); 
		
		try {
			runIntArrayFuncAndCalculateTime("[O(N*k)]   k'th smallest element (k=" + location + "):", (int[] a, int k) -> findKthSmallestByBruteForceDriver(a, k), intArray, location);
			runIntArrayFuncAndCalculateTime("[O(NlogN)] k'th smallest element (k=" + location + "):", (int[] a, int k) -> findKthSmallestBySorting(a, k), intArray, location);
			runIntArrayFuncAndCalculateTime("[O(N)]     k'th smallest element (k=" + location + "):", (int[] a, int k) -> findkthSmallestByQuickSelecting(a, k), intArray, location);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("All rabbits gone.");
	}

	

}
