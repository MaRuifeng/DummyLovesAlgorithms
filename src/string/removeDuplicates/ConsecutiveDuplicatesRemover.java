package string.removeDuplicates;

import utils.FunStringAlgorithm;

/**
 * Given a string, remove all consecutive duplicate characters from it
 * such that the result string does not contain consecutive duplicates. 
 * 
 * E.g. Input: "abbbcc"    Output: "abc"
 * 
 * @author ruifengm
 * @since 2018-May-5
 * 
 * https://www.geeksforgeeks.org/remove-consecutive-duplicates-string/
 *
 */

public class ConsecutiveDuplicatesRemover extends FunStringAlgorithm {
	
	/**
	 * Recursive method.
	 * Let A(n) be the char array, check if A[n-1] == A[n-2]. 
	 * If yes, remove A[n-1] and recur with A(n-1); 
	 * else, keep A[n-1] and recur with A(n-1).
	 */
	private static String recursiveRemoveConsecutiveDuplicates(String s, int idx) {
		if (idx <= 0) return s; 
		if (s.charAt(idx) == s.charAt(idx-1)) 
			return recursiveRemoveConsecutiveDuplicates(s.substring(0, idx) + s.substring(idx+1, s.length()), idx-1); 
		else return recursiveRemoveConsecutiveDuplicates(s, idx-1);
	}
	private static String recursiveRemoveConsecutiveDuplicatesDriver(String s) {
		return recursiveRemoveConsecutiveDuplicates(s, s.length()-1);
	}
	
	/** 
	 * Iterative method. 
	 */
	private static String iterativeRemoveConsecutiveDuplicates(String s) {
		int i = s.length() - 1; 
		while (i>0) {
			if (s.charAt(i) == s.charAt(i-1)) s = s.substring(0, i) + s.substring(i+1, s.length()); 
			i--; 
		}
		return s;
	}
	
	public static void main(String[] args) {
		//String testStr = "azxxzy";
		//String testStr = "whyydooiihhaavveeddupplicattess";
		//String testStr = "caaabbbaacdddd";
		String testStr = "acaaabbbacdddd"; 
		//String testStr = "aaaaa";
		//String testStr = "aaaabbbbc";
		//String testStr = "";
		System.out.println("Welcome to the rabbit hole of consecutive duplicate characters!");
		System.out.println("The test string is: \n" + testStr+ "\n"); 
		
		try {
			runStringFuncAndCalculateTime("[Recursive]         After removing consecutive duplicates:\n", 
					(String s) -> recursiveRemoveConsecutiveDuplicatesDriver(s), testStr);
			runStringFuncAndCalculateTime("[Iterative]         After removing consecutive duplicates:\n", 
					(String s) -> iterativeRemoveConsecutiveDuplicates(s), testStr);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("All rabbits gone.");
	}

}
