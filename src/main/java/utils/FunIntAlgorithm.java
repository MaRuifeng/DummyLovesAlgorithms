package utils;

import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.function.IntFunction;
import java.util.function.IntToLongFunction;
import java.util.function.IntUnaryOperator;

/**
 * Parent class for all integer related algorithm classes 
 * @author ruifengm
 * @since 2018-Mar-30
 */


public class FunIntAlgorithm extends FunAlgorithm {

	@FunctionalInterface
	protected interface IntArrayToIntItemFunction {
	   int apply(int[] array, int location) throws Exception;  
	}
	
	@FunctionalInterface
	protected interface IntArrayToHashSetFunction {
		HashSet<ArrayList<Integer>> apply(int[] array, int sum);  
	}
	
	@FunctionalInterface
	protected interface IntArrayAndIntToArrayListFunction {
		ArrayList<ArrayList<Integer>> apply(int[] array, int sum);  
	}
	
	@FunctionalInterface
	protected interface IntArrayToArrayListFunction {
		ArrayList<String> apply(int[] array);  
	}
	
	@FunctionalInterface
	protected interface IntArrayToLongFunction {
	   long apply(int[] array) throws Exception;  
	}
	
	@FunctionalInterface
	protected interface IntSubArrayToLongFunction {
	   long apply(int[] array, int size);  
	}
	
	@FunctionalInterface
	protected interface TwoIntToLongFunction {
	   long apply(int a, int b);  
	}
	
	@FunctionalInterface
	protected interface TwoIntToIntFunction {
	   int apply(int a, int b);  
	}
	
	@FunctionalInterface
	protected interface IntMatrixToIntArrayFunction {
		int[] apply(int[][] matrix) throws Exception;
	}

	@FunctionalInterface
	protected interface IntListToIntFunction {
		int apply(List<Integer> list);
	}

	public static int genRanInt(int start, int end) {
		return ThreadLocalRandom.current().nextInt(start, end);
	}
	
	public static int[] genRanIntArr(int size, int start, int end) {
		int arr[] = new int[size];
		for (int i=0; i<size; i++) {
			// arr[i] = ThreadLocalRandom.current().nextInt(Integer.MIN_VALUE, Integer.MAX_VALUE);
			arr[i] = ThreadLocalRandom.current().nextInt(start, end);
		}
		return arr;
	}
	
	public static int[] genRanUniqueIntArr(int size) {
		ArrayList<Integer> arrList = new ArrayList<Integer>();
		for (int i=0; i<size * 10; i++) {
			arrList.add(new Integer(i - size * 10 / 2));
		}
		Collections.shuffle(arrList);
		int[] newArr = new int[size]; 
		for (int i=0; i<newArr.length; i++) newArr[i] = arrList.get(i);
		return newArr;
	}
	
    protected static void runIntFuncAndCalculateTime(String message, IntUnaryOperator intFunc, int value) throws Exception {
    	long startTime = System.nanoTime();
    	System.out.printf("%-70s%d\n", message, intFunc.applyAsInt(value));
    	long endTime = System.nanoTime();
    	long totalTime = new Long(TimeUnit.MICROSECONDS.convert(endTime - startTime, TimeUnit.NANOSECONDS));
    	DecimalFormat formatter = new DecimalFormat("#,###");
    	System.out.printf("%-70s%s\n\n", "Function execution time in micro-seconds: ", formatter.format(totalTime));
    }
    
    protected static void runIntFuncAndCalculateTime(String message, IntFunction<HashSet<ArrayList<Integer>>> intFunc, int value) throws Exception {
    	System.out.printf("%-70s\n", message);
    	long startTime = System.nanoTime();
    	HashSet<ArrayList<Integer>> resultSet = intFunc.apply(value);
    	long endTime = System.nanoTime();
    	long totalTime = new Long(TimeUnit.MICROSECONDS.convert(endTime - startTime, TimeUnit.NANOSECONDS));
		for(ArrayList<Integer> item: resultSet) System.out.println(item.toString());
    	DecimalFormat formatter = new DecimalFormat("#,###");
    	System.out.printf("%-70s%s\n\n", "Function execution time in micro-seconds: ", formatter.format(totalTime));
    }
    
    protected static void runIntFuncAndCalculateTime(String message, IntToLongFunction intFunc, int value) throws Exception {
    	long startTime = System.nanoTime();
    	System.out.printf("%-70s%d\n", message, intFunc.applyAsLong(value));
    	long endTime = System.nanoTime();
    	long totalTime = new Long(TimeUnit.MICROSECONDS.convert(endTime - startTime, TimeUnit.NANOSECONDS));
    	DecimalFormat formatter = new DecimalFormat("#,###");
    	System.out.printf("%-70s%s\n\n", "Function execution time in micro-seconds: ", formatter.format(totalTime));
    }
	
    protected static void runIntFuncAndCalculateTime(String message, TwoIntToLongFunction intFunc, int a, int b) throws Exception {
    	long startTime = System.nanoTime();
    	System.out.printf("%-70s%d\n", message, intFunc.apply(a, b));
    	long endTime = System.nanoTime();
    	long totalTime = new Long(TimeUnit.MICROSECONDS.convert(endTime - startTime, TimeUnit.NANOSECONDS));
    	DecimalFormat formatter = new DecimalFormat("#,###");
    	System.out.printf("%-70s%s\n\n", "Function execution time in micro-seconds: ", formatter.format(totalTime));
    }
    
    protected static void runIntFuncAndCalculateTime(String message, TwoIntToIntFunction intFunc, int a, int b) throws Exception {
    	long startTime = System.nanoTime();
    	System.out.printf("%-70s%d\n", message, intFunc.apply(a, b));
    	long endTime = System.nanoTime();
    	long totalTime = new Long(TimeUnit.MICROSECONDS.convert(endTime - startTime, TimeUnit.NANOSECONDS));
    	DecimalFormat formatter = new DecimalFormat("#,###");
    	System.out.printf("%-70s%s\n\n", "Function execution time in micro-seconds: ", formatter.format(totalTime));
    }
    
    protected static void runIntArrayFuncAndCalculateTime(String message, IntArrayToLongFunction intArrayFunc, int[] array) throws Exception {
    	long startTime = System.nanoTime();
    	System.out.printf("%-70s%d\n", message, intArrayFunc.apply(array));
    	long endTime = System.nanoTime();
    	long totalTime = new Long(TimeUnit.MICROSECONDS.convert(endTime - startTime, TimeUnit.NANOSECONDS));
    	DecimalFormat formatter = new DecimalFormat("#,###");
    	System.out.printf("%-70s%s\n\n", "Function execution time in micro-seconds: ", formatter.format(totalTime));
    }
    
    protected static void runIntArrayFuncAndCalculateTime(String message, IntSubArrayToLongFunction intArrayFunc, int[] array, int size) throws Exception {
    	long startTime = System.nanoTime();
    	System.out.printf("%-70s%d\n", message, intArrayFunc.apply(array, size));
    	long endTime = System.nanoTime();
    	long totalTime = new Long(TimeUnit.MICROSECONDS.convert(endTime - startTime, TimeUnit.NANOSECONDS));
    	DecimalFormat formatter = new DecimalFormat("#,###");
    	System.out.printf("%-70s%s\n\n", "Function execution time in micro-seconds: ", formatter.format(totalTime));
    }
    
    protected static void runIntArrayFuncAndCalculateTime(String message, IntArrayToIntItemFunction intArrayFunc, int[] array, int location) throws Exception {
    	long startTime = System.nanoTime();
    	System.out.printf("%-70s%d\n", message, intArrayFunc.apply(array, location));
    	long endTime = System.nanoTime();
    	long totalTime = new Long(TimeUnit.MICROSECONDS.convert(endTime - startTime, TimeUnit.NANOSECONDS));
    	DecimalFormat formatter = new DecimalFormat("#,###");
    	System.out.printf("%-70s%s\n\n", "Function execution time in micro-seconds: ", formatter.format(totalTime));
    }
    
    protected static void runIntArrayFuncAndCalculateTime(String message, IntArrayToHashSetFunction intArrayFunc, int[] array, int sum) throws Exception {
    	long startTime = System.nanoTime();
    	System.out.printf("%-80s\n%s\n", message, intArrayFunc.apply(array, sum).toString());
    	long endTime = System.nanoTime();
    	long totalTime = new Long(TimeUnit.MICROSECONDS.convert(endTime - startTime, TimeUnit.NANOSECONDS));
    	DecimalFormat formatter = new DecimalFormat("#,###");
    	System.out.printf("%-80s%s\n\n", "Function execution time in micro-seconds: ", formatter.format(totalTime));
    }
    
    protected static void runIntArrayFuncAndCalculateTime(String message, IntArrayAndIntToArrayListFunction intArrayFunc, int[] array, int sum) throws Exception {
    	long startTime = System.nanoTime();
    	System.out.printf("%-80s\n%s\n", message, intArrayFunc.apply(array, sum).toString());
    	long endTime = System.nanoTime();
    	long totalTime = new Long(TimeUnit.MICROSECONDS.convert(endTime - startTime, TimeUnit.NANOSECONDS));
    	DecimalFormat formatter = new DecimalFormat("#,###");
    	System.out.printf("%-80s%s\n\n", "Function execution time in micro-seconds: ", formatter.format(totalTime));
    }
    
    protected static void runIntArrayFuncAndCalculateTime(String message, IntArrayToArrayListFunction intArrayFunc, int[] array) throws Exception {
    	long startTime = System.nanoTime();
    	System.out.printf("%-80s\n", message);
    	for (String s: intArrayFunc.apply(array)) System.out.println(s);
    	long endTime = System.nanoTime();
    	long totalTime = new Long(TimeUnit.MICROSECONDS.convert(endTime - startTime, TimeUnit.NANOSECONDS));
    	DecimalFormat formatter = new DecimalFormat("#,###");
    	System.out.printf("%-80s%s\n\n", "Function execution time in micro-seconds: ", formatter.format(totalTime));
    }
    
    protected static void runIntMatrixFuncAndCalculateTime(String message, IntMatrixToIntArrayFunction intArrayFunc, int[][] matrix) throws Exception {
    	long startTime = System.nanoTime();
    	System.out.printf("%-70s%s\n", message, Arrays.toString(intArrayFunc.apply(matrix)));
    	long endTime = System.nanoTime();
    	long totalTime = new Long(TimeUnit.MICROSECONDS.convert(endTime - startTime, TimeUnit.NANOSECONDS));
    	DecimalFormat formatter = new DecimalFormat("#,###");
    	System.out.printf("%-70s%s\n\n", "Function execution time in micro-seconds: ", formatter.format(totalTime));
    }

	protected static void runIntListFuncAndCalculateTime(String message, IntListToIntFunction intFunc, List<Integer> list) throws Exception {
		long startTime = System.nanoTime();
		System.out.printf("%-70s%s\n", message, intFunc.apply(list));
		long endTime = System.nanoTime();
		long totalTime = new Long(TimeUnit.MICROSECONDS.convert(endTime - startTime, TimeUnit.NANOSECONDS));
		DecimalFormat formatter = new DecimalFormat("#,###");
		System.out.printf("%-70s%s\n\n", "Function execution time in micro-seconds: ", formatter.format(totalTime));
	}
	
    
    public static int[] mergeSort(int[] a, int start, int end) {
    	if (start == end) return new int[]{a[start]};
    	
    	int mid = (start + end) / 2; 
    	int[] leftSortedArr = mergeSort(a, start, mid);
    	int[] rightSortedArr = mergeSort(a, mid + 1, end);
    	// merge
    	int[] sortedArr = new int[leftSortedArr.length + rightSortedArr.length];
    	int i = leftSortedArr.length - 1, j = rightSortedArr.length - 1, k = sortedArr.length - 1;
    	while (k>=0) {
    		if (i<0) {
    			sortedArr[k--] = rightSortedArr[j--]; 
    			continue;
    		}
    		if (j<0) {
    			sortedArr[k--] = leftSortedArr[i--]; 
    			continue;
    		}
    		if (leftSortedArr[i] > rightSortedArr[j]) sortedArr[k--] = leftSortedArr[i--];
    		else sortedArr[k--] = rightSortedArr[j--];
    	}
    	return sortedArr;
    }

	public static int[] mergeSort2(int[] arr, int start, int end) {
		if (start == end) return new int[]{arr[start]}; // base state

		int middle = start + (end - start) / 2;

		// divide and conquer
		int[] sortedLeft = mergeSort(arr, start, middle);
		int[] sortedRight = mergeSort(arr, middle + 1, end);

		// merge sorted parts
		int mergedSize = sortedLeft.length + sortedRight.length;
		int[] merged = new int[mergedSize];
		int i = 0, j = 0, k = 0;
		while (k < mergedSize) {
			if (i == sortedLeft.length) merged[k++] = sortedRight[j++];
			else if (j == sortedRight.length) merged[k++] = sortedLeft[i++];
			else {
				if (sortedLeft[i] < sortedRight[j]) merged[k++] = sortedLeft[i++];
				else merged[k++] = sortedRight[j++];
			}
		}

		return merged;
	}
    
    /**
     * Since quickSort is a randomized algorithm, its time complexity depends on how the pivot gets picked up. 
     * 1) Worst case: for the given partition strategy (last element), pivot is always chosen as the largest/smallest number in the array . This 
     *                occurs when the array is already in sorted order. Time complexity is O(N^2). 
     * 2) Best case: for the given partition strategy (last element), pivot is always chosen as the middle large number in the array. Time complexity 
     *               is O(NLogN). 
     * 3) Average case: time complexity is O(NLogN). 
     * 
     * In general quickSort is considered to be better than mergeSort because of its space efficiency, good cache locality, and the fact that the worst case
     * scenario can be easily avoided by randomizing the pivot selection process. 
     * When input data set is huge with external storage involved, mergeSort is the one that can do it better because it's a stable sort that does not 
     * reorder equal elements. This gives better performance when slow-to-access media like disk storage is involved. 
     * 
     * https://www.geeksforgeeks.org/quicksort-better-mergesort/
     */
    public static void quickSort(int[] a, int start, int end) {
    	if (start < end) {
    		int pivotPos = partition(a, start, end);
			//System.out.println("Pivot position: " + pivotPos);
			//System.out.println("Pivot value: " + a[pivotPos]);
    		quickSort(a, start, pivotPos - 1); // sort items before the pivot
    		quickSort(a, pivotPos + 1, end); // sort items after the pivot
    	}
    }
    
    /**
     * The partition method for the quick sort algorithm. Its purpose is to put all items smaller than the pivot before it, 
     * and all items larger after it. After such operations, the pivot will be in its correct position in the final sorted array, and 
     * the method will return that position in order for the quick sort algorithm to further partition the array and sort (divide and conquer). 
     * The pivot can be chosen randomly (which makes the quick sort algorithm a randomized algorithm). 
     * Over here we always choose the last element as the partitioning pivot. 
     * 
     */
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

	private static int partition2(int[] arr, int start, int end) {
		int pivotValue = arr[end]; // randomly chosen pivot value

		int temp;
		int index = start; // index pointing to elements smaller than pivot
		for (int i=start; i<=end; i++) {
			// increase "smaller-than-pivot-element" index until it reaches the immediate smaller one
			if (arr[i] < pivotValue) {
				temp = arr[index];
				arr[index] = arr[i];
				arr[i] = temp;
				index++;
			}
		}

		// now idx is pointing to the pivot position
		temp = arr[index];
		arr[index] = pivotValue;
		arr[end] = temp;

		return index;
	}

}
