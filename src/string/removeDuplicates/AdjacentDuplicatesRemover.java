package string.removeDuplicates;

import utils.FunStringAlgorithm;

/**
 * Given a string, remove all adjacent duplicate characters from it (repeatedly, if needed)
 * such that the result string does not contain adjacent duplicates. 
 * 
 * E.g. Input: "azxxzy"    Output: "ay"
 * 
 * @author ruifengm
 * @since 2018-May-4
 * 
 * https://www.geeksforgeeks.org/recursively-remove-adjacent-duplicates-given-string/
 *
 */

public class AdjacentDuplicatesRemover extends FunStringAlgorithm {
	
	/**
	 * The problem statement hints for a recursive solution.
	 */
	private static String recursiveRemoveAdjacentDuplicates(String s, int idx) {
		if (idx <=0) return s; 
		int temp = idx; 
		while (idx > 0 && s.charAt(idx) == s.charAt(idx-1)) idx--; 
		if (temp != idx) s = s.substring(0, idx) + s.substring(temp+1, s.length());
		return recursiveRemoveAdjacentDuplicates(s, idx-1);
	}
	private static String recursiveRemoveAdjacentDuplicatesDriver(String s) {
		while (checkAdjacentDuplicates(s)) s = recursiveRemoveAdjacentDuplicates(s, s.length()-1);
		return s;
	}
	private static boolean checkAdjacentDuplicates(String s) {
		for (int i=1; i<s.length(); i++)
			if (s.charAt(i) == s.charAt(i-1)) return true;
		return false;
	}
	
	/**
	 * Above method isn't optimized because of repeated adjacent duplicate check. 
	 * We try to limit the time complexity to O(n). 
	 */
	private static String recursiveRemoveAdjacentDuplicatesLinear(String s, int idx) {
		if (idx <0) return s; 
		int temp = idx; 
		while (idx > 0 && s.charAt(idx) == s.charAt(idx-1)) idx--; 
		if (temp != idx) {
			s = s.substring(0, idx) + s.substring(temp+1, s.length());
			return recursiveRemoveAdjacentDuplicatesLinear(s, idx-1); // remove all adjacent duplicates from right
		}
		else if (idx < s.length()-1 && s.charAt(idx) == s.charAt(idx+1)) // check new duplicates at concatenation point
			return recursiveRemoveAdjacentDuplicatesLinear(s, idx+1); // recur again with idx increased if found
		else return recursiveRemoveAdjacentDuplicatesLinear(s, idx-1);
	}
	private static String recursiveRemoveAdjacentDuplicatesLinearDriver(String s) {
		return recursiveRemoveAdjacentDuplicatesLinear(s, s.length()-1);
	}
	
	public static void main(String[] args) {
		//String testStr = "azxxzy";
		//String testStr = "whyydooiihhaavveedupplicattess";
		//String testStr = "caaabbbaacdddd";
		String testStr = "acaaabbbacdddd"; 
		//String testStr = "aaaaa";
		//String testStr = "abcddcba";  // palindrome
		//String testStr = "";
//		int size = 3000; 
//		char[] palindrome = new char[size]; 
//		for (int i=0; i<=size/2; i++) palindrome[i] = palindrome[size-1-i] = (char)ThreadLocalRandom.current().nextInt(32, 126); 
//		String testStr = String.valueOf(palindrome);
		System.out.println("Welcome to the rabbit hole of adjacent duplicate characters!");
		System.out.println("The test string is: \n" + testStr+ "\n"); 
		
		try {
			runStringFuncAndCalculateTime("[Recursive]         After removing adjacent duplicates:\n", 
					(String s) -> recursiveRemoveAdjacentDuplicatesDriver(s), testStr);
			runStringFuncAndCalculateTime("[Recursive][Linear] After removing adjacent duplicates:\n", 
					(String s) -> recursiveRemoveAdjacentDuplicatesLinearDriver(s), testStr);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("All rabbits gone.");
	}

}
