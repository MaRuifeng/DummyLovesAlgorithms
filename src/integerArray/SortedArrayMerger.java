package integerArray;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import utils.FunIntAlgorithm;

/**
 * Given K sorted integer arrays in ascending order, merge them into one.
 * 
 * @author ruifengm
 * @since 2018-Apr-22
 */

public class SortedArrayMerger extends FunIntAlgorithm {
	
	/**
	 * Put all elements into a single array and sort the array.
	 * Suppose the size of each array is denoted as S(i), where 1 <= i <= K, 
	 * the size of the merged single array is S = SUM(S(1), S(2), ... , S(K)).
	 * The time complexity is O(SLogS).
	 */
	private static int[] mergeBySorting(int[][] matrix) { // an integer matrix with K rows, each row is a sorted array
		int size = 0; 
		for (int[] a: matrix) size += a.length;
		int[] resArr = new int[size];
		int offset = 0;
		for (int[] a: matrix) {
			System.arraycopy(a, 0, resArr, offset, a.length); // use system arraycopy for efficiency
			offset += a.length;
		}
		quickSort(resArr, 0, resArr.length - 1);
		return resArr;
	}
	
	/**
	 * Insert the first element of every array into a min heap of size K, and then 
	 * extract the minimum to the output array, and then 
	 * insert the next element from the array where the extracted minimum comes from, and then 
	 * repeat until all rest elements have been processed as such. 
	 * 
	 * Suppose the size of each  array is denoted as S(i), where 1 <= i <= K, 
	 * the size of the merged single array is S = SUM(S(1), S(2), ... , S(K)).
	 * The time complexity is O(SLogK).
	 * 
	 */
	static class HeapNode { // a customized data type to assist the min heap merger
		                    // made as static here, which can also be declared as a 
		                    // method-local innner class within the method where it is used.
		int key; 
		int source; // index of the array where the key is from in the matrix
		int index; // index of the key in the array
		int size; // size of the array
		
		public HeapNode(int key, int source, int index, int size) {
			this.key = key; 
			this.source = source;
			this.index = index;
			this.size = size;
		}
	}
	private static int[] mergeByMinHeapWithComparator(int[][] matrix) {
		int k = matrix.length;
		int size = 0; 
		for (int[] a: matrix) size += a.length;
		int[] resArr = new int[size];
		// store first elements into a min heap
		PriorityQueue<HeapNode> minHeap = new PriorityQueue<HeapNode>(new Comparator<HeapNode>() {
			@Override
			public int compare(HeapNode a, HeapNode b) {
				return a.key < b.key ? -1 : a.key == b.key ? 0 : 1;
			}		
		});
		for (int i=0; i<k; i++) minHeap.add(new HeapNode(matrix[i][0], i, 0, matrix[i].length));
		for (int i=0; i<size; i++) {
			HeapNode node = minHeap.poll();
			resArr[i] = node.key;
			if (node.index < node.size-1) minHeap.add(new HeapNode(matrix[node.source][node.index+1], node.source, node.index+1, node.size));
		}
		return resArr;
	}
	private static int[] mergeByMinHeapWithLambdaExp(int[][] matrix) { // in Java 8, use Lambda Expression to shorten the code
		int k = matrix.length;
		int size = 0; 
		for (int[] a: matrix) size += a.length;
		int[] resArr = new int[size];
		// store first elements into a min heap
		PriorityQueue<HeapNode> minHeap = new PriorityQueue<HeapNode>((a, b) -> a.key < b.key ? -1 : a.key == b.key ? 0 : 1);
		// Do not use the comparison-by-subtraction trick (a.key - b.key) as it is easily broken by integer overflow. 
		// https://stackoverflow.com/questions/2728793/java-integer-compareto-why-use-comparison-vs-subtraction
		for (int i=0; i<k; i++) minHeap.add(new HeapNode(matrix[i][0], i, 0, matrix[i].length));
		for (int i=0; i<size; i++) {
			HeapNode node = minHeap.poll();
			resArr[i] = node.key;
			if (node.index < node.size-1) minHeap.add(new HeapNode(matrix[node.source][node.index+1], node.source, node.index+1, node.size));
		}
		return resArr;
	}
	
	@FunctionalInterface
	protected interface IntMatrixToIntArrayFunction {
		int[] apply(int[][] matrix) throws Exception;
	}
	
    protected static void runIntArrayFuncAndCalculateTime(String message, IntMatrixToIntArrayFunction intArrayFunc, int[][] matrix) throws Exception {
    	long startTime = System.nanoTime();
    	System.out.printf("%-70s%s\n", message, Arrays.toString(intArrayFunc.apply(matrix)));
    	long endTime = System.nanoTime();
    	long totalTime = new Long(TimeUnit.MICROSECONDS.convert(endTime - startTime, TimeUnit.NANOSECONDS));
    	DecimalFormat formatter = new DecimalFormat("#,###");
    	System.out.printf("%-70s%s\n\n", "Function execution time in micro-seconds: ", formatter.format(totalTime));
    }
	
	public static void main(String[] args) {
		int k = 4;
		System.out.println("Welcome to the rabbit hole of sorted array mergers!\n"
				+ "The number of randomly generated integer arrays is " + k + ".\n");
		int[][] kSortedArrays = new int[k][];
		for (int i=0; i<k; i++) {
			int arraySize = ThreadLocalRandom.current().nextInt(10, 100);
			kSortedArrays[i] = mergeSort(genRanIntArr(arraySize, -10*(i+1), 10*(i+1)), 0, arraySize-1);
		}
		System.out.println("The sorted arrays are:\n");
		for (int[] a: kSortedArrays) System.out.println(Arrays.toString(a));
		System.out.println("");
		
		try {
			runIntArrayFuncAndCalculateTime("[O(nkLog(nk))]   Merged array is", (int[][] m) -> mergeBySorting(m), kSortedArrays);
			runIntArrayFuncAndCalculateTime("[O(nkLogk)][Comparator]   Merged array is", (int[][] m) -> mergeByMinHeapWithComparator(m), kSortedArrays);
			runIntArrayFuncAndCalculateTime("[O(nkLogk)][Lambda]   Merged array is", (int[][] m) -> mergeByMinHeapWithLambdaExp(m), kSortedArrays);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("All rabbits gone.");
	}

}
