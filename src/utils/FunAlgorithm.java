package utils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.function.IntFunction;
import java.util.function.IntToLongFunction;
import java.util.function.IntUnaryOperator;

/**
 * Parent class for all algorithm classes 
 * @author ruifengm
 * @since 2018-Mar-30
 */


public class FunAlgorithm {
	
	@FunctionalInterface
	protected interface IntArrayToIntItemFunction {
	   int apply(int[] array, int location) throws Exception;  
	}
	
	
	@FunctionalInterface
	protected interface IntArrayToLongFunction {
	   long apply(int[] array);  
	}
	
	@FunctionalInterface
	protected interface IntSubArrayToLongFunction {
	   long apply(int[] array, int size);  
	}
	
	protected static int[] genRanIntArr(int size) {
		int arr[] = new int[size];
		for (int i=0; i<size; i++) {
			// arr[i] = ThreadLocalRandom.current().nextInt(Integer.MIN_VALUE, Integer.MAX_VALUE);
			arr[i] = ThreadLocalRandom.current().nextInt(-5, 10);
		}
		return arr;
	}
	
	protected static int[] genRanUniqueIntArr(int size) {
		ArrayList<Integer> arrList = new ArrayList<Integer>();
		for (int i=0; i<size * 5; i++) {
			arrList.add(new Integer(i - size * 5 / 2));
		}
		Collections.shuffle(arrList);
		int[] newArr = new int[size]; 
		for (int i=0; i<newArr.length; i++) newArr[i] = arrList.get(i);
		return newArr;
	}
	
    protected static void runIntFuncAndCalculateTime(String message, IntUnaryOperator intFunc, int value) throws Exception {
    	long startTime = System.nanoTime();
    	System.out.printf("%-70s%d\n", message, intFunc.applyAsInt(value));
    	long endTime   = System.nanoTime();
    	long totalTime = new Long(TimeUnit.MICROSECONDS.convert(endTime - startTime, TimeUnit.NANOSECONDS));
    	DecimalFormat formatter = new DecimalFormat("#,###");
    	System.out.printf("%-70s%s\n\n", "Function execution time in micro-seconds: ", formatter.format(totalTime));
    }
    
    protected static void runIntFuncAndCalculateTime(String message, IntFunction<HashSet<ArrayList<Integer>>> intFunc, int value) throws Exception {
    	System.out.printf("%-70s\n", message);
    	long startTime = System.nanoTime();
    	HashSet<ArrayList<Integer>> resultSet = intFunc.apply(value);
    	long endTime   = System.nanoTime();
    	long totalTime = new Long(TimeUnit.MICROSECONDS.convert(endTime - startTime, TimeUnit.NANOSECONDS));
		for(ArrayList<Integer> item: resultSet) System.out.println(item.toString());
    	DecimalFormat formatter = new DecimalFormat("#,###");
    	System.out.printf("%-70s%s\n\n", "Function execution time in micro-seconds: ", formatter.format(totalTime));
    }
    
    protected static void runIntFuncAndCalculateTime(String message, IntToLongFunction intFunc, int value) throws Exception {
    	long startTime = System.nanoTime();
    	System.out.printf("%-70s%d\n", message, intFunc.applyAsLong(value));
    	long endTime   = System.nanoTime();
    	long totalTime = new Long(TimeUnit.MICROSECONDS.convert(endTime - startTime, TimeUnit.NANOSECONDS));
    	DecimalFormat formatter = new DecimalFormat("#,###");
    	System.out.printf("%-70s%s\n\n", "Function execution time in micro-seconds: ", formatter.format(totalTime));
    }
	
    protected static void runIntArrayFuncAndCalculateTime(String message, IntArrayToLongFunction intArrayFunc, int[] array) throws Exception {
    	long startTime = System.nanoTime();
    	System.out.printf("%-70s%d\n", message, intArrayFunc.apply(array));
    	long endTime   = System.nanoTime();
    	long totalTime = new Long(TimeUnit.MICROSECONDS.convert(endTime - startTime, TimeUnit.NANOSECONDS));
    	DecimalFormat formatter = new DecimalFormat("#,###");
    	System.out.printf("%-70s%s\n\n", "Function execution time in micro-seconds: ", formatter.format(totalTime));
    }
    
    protected static void runIntArrayFuncAndCalculateTime(String message, IntSubArrayToLongFunction intArrayFunc, int[] array, int size) throws Exception {
    	long startTime = System.nanoTime();
    	System.out.printf("%-70s%d\n", message, intArrayFunc.apply(array, size));
    	long endTime   = System.nanoTime();
    	long totalTime = new Long(TimeUnit.MICROSECONDS.convert(endTime - startTime, TimeUnit.NANOSECONDS));
    	DecimalFormat formatter = new DecimalFormat("#,###");
    	System.out.printf("%-70s%s\n\n", "Function execution time in micro-seconds: ", formatter.format(totalTime));
    }
    
    protected static void runIntArrayFuncAndCalculateTime(String message, IntArrayToIntItemFunction intArrayFunc, int[] array, int location) throws Exception {
    	long startTime = System.nanoTime();
    	System.out.printf("%-70s%d\n", message, intArrayFunc.apply(array, location));
    	long endTime   = System.nanoTime();
    	long totalTime = new Long(TimeUnit.MICROSECONDS.convert(endTime - startTime, TimeUnit.NANOSECONDS));
    	DecimalFormat formatter = new DecimalFormat("#,###");
    	System.out.printf("%-70s%s\n\n", "Function execution time in micro-seconds: ", formatter.format(totalTime));
    }
    
    protected static int[] mergeSort(int[] a, int start, int end) {
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

}
