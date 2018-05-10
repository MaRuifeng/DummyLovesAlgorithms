package utils;

import java.text.DecimalFormat;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/**
 * Parent class for all string related algorithm classes 
 * @author ruifengm
 * @since 2018-Apr-10
 */

public class FunStringAlgorithm extends FunAlgorithm {
	
	@FunctionalInterface
	protected interface CharArrayToStringFunction {
	   String apply(char[] charA);  
	}
	
	@FunctionalInterface
	protected interface StringToStringFunction {
	   String apply(String s);  
	}
	
	@FunctionalInterface
	protected interface CharArrayFunction {
		char[] apply(char[] charA);
	}
	
	@FunctionalInterface
	protected interface StringToIntFunction {
		int apply(String s);
	}
	
	@FunctionalInterface
	protected interface DoubleCharArrayToIntFunction {
		int apply(char[] a, char[] b);
	}
	
	@FunctionalInterface
	protected interface DoubleCharArrayToStringFunction {
		String apply(char[] a, char[] b);
	}
	
    protected static void runStringFuncAndCalculateTime(String message, CharArrayToStringFunction strFunc, char[] charA) throws Exception {
    	long startTime = System.nanoTime();
    	System.out.printf("%s%s\n", message, strFunc.apply(charA));
    	long endTime = System.nanoTime();
    	long totalTime = new Long(TimeUnit.MICROSECONDS.convert(endTime - startTime, TimeUnit.NANOSECONDS));
    	DecimalFormat formatter = new DecimalFormat("#,###");
    	System.out.printf("%-70s%s\n\n", "Function execution time in micro-seconds: ", formatter.format(totalTime));
    }
    
    protected static void runStringFuncAndCalculateTime(String message, StringToStringFunction strFunc, String s) throws Exception {
    	long startTime = System.nanoTime();
    	System.out.printf("%s%s\n", message, strFunc.apply(s));
    	long endTime = System.nanoTime();
    	long totalTime = new Long(TimeUnit.MICROSECONDS.convert(endTime - startTime, TimeUnit.NANOSECONDS));
    	DecimalFormat formatter = new DecimalFormat("#,###");
    	System.out.printf("%-70s%s\n\n", "Function execution time in micro-seconds: ", formatter.format(totalTime));
    }
    
    protected static void runStringFuncAndCalculateTime(String message, CharArrayFunction strFunc, char[] charA) throws Exception {
    	long startTime = System.nanoTime();
    	System.out.printf("%s\n", message);
    	for (char c: strFunc.apply(charA)) System.out.print(c != 0 ? c : "");
    	long endTime = System.nanoTime();
    	long totalTime = new Long(TimeUnit.MICROSECONDS.convert(endTime - startTime, TimeUnit.NANOSECONDS));
    	DecimalFormat formatter = new DecimalFormat("#,###");
    	System.out.printf("%-70s%s\n\n", "\nFunction execution time in micro-seconds: ", formatter.format(totalTime));
    }
    
    protected static void runStringFuncAndCalculateTime(String message, StringToIntFunction strFunc, String s) throws Exception {
    	long startTime = System.nanoTime();
    	System.out.printf("%-70s%s\n", message, strFunc.apply(s));
    	long endTime = System.nanoTime();
    	long totalTime = new Long(TimeUnit.MICROSECONDS.convert(endTime - startTime, TimeUnit.NANOSECONDS));
    	DecimalFormat formatter = new DecimalFormat("#,###");
    	System.out.printf("%-70s%s\n\n", "Function execution time in micro-seconds: ", formatter.format(totalTime));
    }
    
    protected static int runStringFuncAndCalculateTime(String message, DoubleCharArrayToIntFunction strFunc, char[] a, char[] b) throws Exception {
    	long startTime = System.nanoTime();
    	int result = strFunc.apply(a, b);
    	System.out.printf("%-70s%s\n", message, result);
    	long endTime = System.nanoTime();
    	long totalTime = new Long(TimeUnit.MICROSECONDS.convert(endTime - startTime, TimeUnit.NANOSECONDS));
    	DecimalFormat formatter = new DecimalFormat("#,###");
    	System.out.printf("%-70s%s\n\n", "Function execution time in micro-seconds: ", formatter.format(totalTime));
    	return result;
    }
    
    protected static String runStringFuncAndCalculateTime(String message, DoubleCharArrayToStringFunction strFunc, char[] a, char[] b) throws Exception {
    	long startTime = System.nanoTime();
    	String result = strFunc.apply(a, b);
    	System.out.printf("%-70s%s\n", message, result);
    	long endTime = System.nanoTime();
    	long totalTime = new Long(TimeUnit.MICROSECONDS.convert(endTime - startTime, TimeUnit.NANOSECONDS));
    	DecimalFormat formatter = new DecimalFormat("#,###");
    	System.out.printf("%-70s%s\n\n", "Function execution time in micro-seconds: ", formatter.format(totalTime));
    	return result;
    }
    
    /**
     * Generate a printable string of given size. 
     * @param size
     * @return String
     */
    protected static String genRanStr(int size) {
    	StringBuilder builder= new StringBuilder();
    	while (size-- != 0) builder.append((char)ThreadLocalRandom.current().nextInt(32, 126));
    	return builder.toString();
    }

}
