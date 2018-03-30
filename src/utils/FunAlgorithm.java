package utils;

import java.text.DecimalFormat;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/**
 * Parent class for all algorithm classes 
 * @author ruifengm
 * @since 2018-Mar-30
 */


public class FunAlgorithm {
	
	@FunctionalInterface
	protected interface IntArrayToIntFunction {
	   int apply(int[] array);  
	}
	
	protected static int[] genRanIntArr(int size) {
		int arr[] = new int[size];
		for (int i=0; i<size; i++) {
			// arr[i] = ThreadLocalRandom.current().nextInt(Integer.MIN_VALUE, Integer.MAX_VALUE);
			arr[i] = ThreadLocalRandom.current().nextInt(-5, 10);
		}
		return arr;
	}
	
    protected static void runFuncAndCalculateTime(String message, IntArrayToIntFunction maxSumFunc, int[] array) throws Exception {
    	long startTime = System.nanoTime();
    	System.out.printf("%-60s%d\n", message, maxSumFunc.apply(array));
    	long endTime   = System.nanoTime();
    	long totalTime = new Long(TimeUnit.MICROSECONDS.convert(endTime - startTime, TimeUnit.NANOSECONDS));
    	DecimalFormat formatter = new DecimalFormat("#,###");
    	System.out.printf("%-60s%s\n\n", "Function execution time in micro-seconds: ", formatter.format(totalTime));
    }

}
