package utils;

import java.text.DecimalFormat;
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
	protected interface CharArrayFunction {
		char[] apply(char[] charA);
	}
	
    protected static void runStringFuncAndCalculateTime(String message, CharArrayToStringFunction strFunc, char[] charA) throws Exception {
    	long startTime = System.nanoTime();
    	System.out.printf("%s%s\n", message, strFunc.apply(charA));
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

}