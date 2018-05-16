package binaryHeap;

import java.util.Arrays;

import utils.FunIntAlgorithm;

/**
 * A sorting algorithm with time complexity of O(NlogN) using the Min Heap data structure.
 * 
 * @author ruifengm
 * @since 2018-Apr-22
 */

public class HeapSort extends FunIntAlgorithm {
	
	/* Note that below method is not efficient in space, but gives a good overview on how heapsort works.*/
	/* Actual heap sort should occur in place without extra space used. */
	private static void heapSort(int[] a) {
		// Store the array values into a Min Heap
		MinHeap heap = new MinHeap(a.length);
		for (int i: a) heap.insert(i);
		// Extract the min value successively to build up the sorted array
		for (int i=0; i<a.length; i++) a[i] = heap.extractMin();
	}

	public static void main(String[] args) {
		int[] intArray = genRanIntArr(20, -50, 50);
		//int[] intArray = {-48, -22, 17, -32, -22, -21, 4, -26, -48, 27, 21, -5, -33, -24, -46, -24, 47, -20, -26, 0};
		System.out.println("Welcome to the rabbit hole of heap sorting!\n"
				+ "The integer array is \n" + Arrays.toString(intArray) + "\n");
		
		heapSort(intArray);
		System.out.println("[O(NlogN) After heap sort: " + Arrays.toString(intArray));

		
		System.out.println("All rabbits gone.");
	}
}
