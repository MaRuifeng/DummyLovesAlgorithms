package string;

/**
 * Given a string with unique characters, find all its permutations. 
 * 
 * E.g. for string "ABC", the permutations are
 *      "ABC", "ACB", "BAC", "BCA", "CAB", "CBA"
 *      
 * @author ruifengm
 * @since 2018-May-26
 * 
 * https://www.geeksforgeeks.org/write-a-c-program-to-print-all-permutations-of-a-given-string/
 */

public class StringPermutation {
	
	/**
	 * Similar to the number permutation problem, the string permutations can 
	 * be found recursively by inserting a character to all possible locations. 
	 */
	private static void printStringPermutations(String sol, String str, int idx) {
		if (sol.length() == str.length()) System.out.println(sol);
		else {
			for (int i=0; i<=sol.length(); i++) 
				printStringPermutations(sol.substring(0, i) + str.charAt(idx-1) + sol.substring(i), str, idx-1);
		}
	}
	private static void printStringPermutations(String str) {
		printStringPermutations("", str, str.length());
	}
	
	/**
	 * Recursively print permutations by backtracking in-place.  
	 */
	private static void printStringPermutationsByBacktracking(char[] charArr, int start, int end) {
		if (start == end) System.out.println(String.valueOf(charArr));
		else {
			for (int i=start; i<=end; i++) {
				swap(charArr, start, i); 
				printStringPermutationsByBacktracking(charArr, start+1, end);
				swap(charArr, start, i);
			}
		}
	}
	private static void printStringPermutationsByBacktracking(String str) {
		char[] charArr = str.toCharArray(); 
		printStringPermutationsByBacktracking(charArr, 0, str.length()-1);
		System.out.println("Character array remains unchanged after backtracking swaps:");
		System.out.println(String.valueOf(charArr));
	}
	private static void swap(char[] a, int x, int y) {
		if (x == y) return;
		char temp = a[x];
		a[x] = a[y]; 
		a[y] = temp;
	}
	
	public static void main(String[] args) {
		String str = "ABCDE";
		System.out.println("Welcome to the rabbit hole of string permutations!\n"
				+ "The string is \n" + str + "\n"); 
		
		System.out.println("\n/* Recursively print all permutations */");
		printStringPermutations(str);
		
		System.out.println("\n/* Recursively print all permutations by backtracking in place */");
		printStringPermutationsByBacktracking(str);
		
		System.out.println("\nAll rabbits gone.");
	}

}
