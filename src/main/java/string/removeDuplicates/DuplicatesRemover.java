package string.removeDuplicates;

import utils.FunStringAlgorithm;

/**
 * Given a string can be read in the English language, remove all duplicate characters from it.
 * 
 * Time complexity: O(n)
 * Space complexity: O(1)
 * 
 * For example,
 * Input:     "AaBabcCCDeEcFE"
 * Output:    "AaBbcCDeEF"
 * 
 * @author ruifengm
 * @since 2018-Apr-9
 */

public class DuplicatesRemover extends FunStringAlgorithm {
	
	/**
	 * Printable characters in the ASCII table forms a finite direct access table where character occurrences can be recorded. 
	 */
	private static char[] removeDuplicate(char[] charA) {
		boolean[] charOcc = new boolean[95]; // denote character occurrence
		for (int i=0; i<charOcc.length; i++) charOcc[i] = false; 
		for (int i=0; i<charA.length; i++) {
			if (charOcc[charA[i]-32] == false) charOcc[charA[i]-32] = true; 
			else charA[i] = 0; // set to null char
		}
		return charA;
	}
	
	public static void main(String[] args) {
		String testStr = "AaBabcCCDeEcFE";
		System.out.println("Welcome to the rabbit hole of duplicate characters! \n"
				+ "The test string is: \n" + testStr+ "\n"); 
		
		try {
			runStringFuncAndCalculateTime("[O(n)Time][O(1)Space]        After removing duplicates:\n", (char[] a) -> removeDuplicate(a), testStr.toCharArray());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("All rabbits gone.");
	}

}
