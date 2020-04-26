package string.removeDuplicates;

import utils.FunStringAlgorithm;

/**
 * Given a char array that can be read in the English language, remove 
 * alternate duplicate characters in place without considering case sensitivity.
 * 
 * Time complexity: O(n)
 * Space complexity: O(1)
 * 
 * For example,
 * Input:     "I like dummy's code"
 * Output:    "I lkedumy's co"
 * 
 * @author ruifengm
 * @since 2018-Apr-9
 */
public class AlternateDuplicatesRemover extends FunStringAlgorithm {
	
	/**
	 * We assume that the characters are only those printable in the ASCII table, ranging from 32 ~ 126. 
	 * 
	 * We create an integer array A of size 95 (covering the range of the printable characters), then traverse the 
	 * given string, and use A[char-32] to store the number of occurrences of char. 
	 * 
	 * To just denote whether a character should appear or not, a boolean array can be used. 
	 * 
	 * This is based on the fact that the character set is finite, so we are actually constructing a direct access table
	 * using character's ASCII code as indices, which has the capability of doing item querying/updating in O(1) time complexity.
	 * 
	 */
	private static String removeAltDup(char[] charA) {
		boolean[] charApp = new boolean[95]; // denote appearance of printable chars in the ASCII table
		for (int i=0; i<charApp.length; i++) charApp[i] = true; 
		for (int i=0; i<charA.length; i++) {
			if (charApp[charA[i]-32] == true) { // alternate by setting to true/false repeatedly
				charApp[charA[i]-32] = false;
				if (charA[i]>='A' && charA[i]<='Z') charApp[charA[i]-32 + ('a'-'A')] = false; // set lower-case counterpart
				if (charA[i]>='a' && charA[i]<='z') charApp[charA[i]-32 - ('a'-'A')] = false;
			} else {
				charApp[charA[i]-32] = true;
				if (charA[i]>='A' && charA[i]<='Z') charApp[charA[i]-32 + ('a'-'A')] = true; // set lower-case counterpart
				if (charA[i]>='a' && charA[i]<='z') charApp[charA[i]-32 - ('a'-'A')] = true;
				charA[i] = 0; // remove by setting to null char
			}
		}
		return String.copyValueOf(charA).replaceAll("\0", ""); // in fact we should just use a loop to print the char array to make it really in-place, 
		                                                     // but just let the String object do us a favor.
	}
	
	public static void main(String[] args) {
//		String testStr = "FAR out in the ocean, where the water is as blue as the prettiest cornflower, and as clear as crystal, " + 
//	          "it is very, very deep; so deep, indeed, that no cable could fathom it: many church steeples, "
//	          + "piled one upon another, would not reach from the ground beneath to the surface of the water above.";
		String testStr = "A ab; bcd; bCDE, cde.FE";
		System.out.println("Welcome to the rabbit hole of alternate duplicate characters! \n"
				+ "The test string is: \n" + testStr+ "\n"); 
		
		try {
			runStringFuncAndCalculateTime("[O(n)Time][O(1)Space]        After removing alternate duplicates:\n", (char[] a) -> removeAltDup(a), testStr.toCharArray());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("All rabbits gone.");
	}


}
