package dynamicProgramming;

import java.util.Arrays;

import utils.FunStringAlgorithm;

/**
 * Given two strings, 
 * 
 * 1) find the length of the longest common substring (LCstr).
 * 2) print out the longest common substring. 
 * 
 * https://www.geeksforgeeks.org/longest-common-substring/
 * 
 * @author ruifengm
 * @since 2018-Apr-11
 */

public class LongestCommonSubstring extends FunStringAlgorithm {
	
	/**
	 * Use dynamic programming, the idea is to find the lengths of the longest common suffix of all substrings of the two given strings 
	 * that starts at 0. Out of them, the largest value is the length of longest common substring. 
	 * 
	 * The longest common suffix has following optimal substructure property
	 * 		LCSuff(X, Y, m, n) = LCSuff(X, Y, m-1, n-1) + 1       if X[m-1] = Y[n-1]
     *                           0                                if X[m-1] != Y[n-1]
     *      LCSubStr(X, Y, m, n)  = Max(LCSuff(X, Y, i, j)) where 1 <= i <m and 1 <= j <= n
	 */
	private static int findLengthOfLCstr(char[] a, char[] b) {
		if (a == null || b == null) return 0; 
		int[][] table = new int[a.length+1][b.length+1]; // storing longest common suffix lengths
		int len = 0; // result
		
		table[0][0] = 0; // empty string a and string b
		for (int i=1; i<=a.length; i++) {
			for (int j=1; j<b.length; j++) {
				if (a[i-1] == b[j-1]) table[i][j] = table[i-1][j-1] + 1; 
				else table[i][j] = 0;
				len = Math.max(len, table[i][j]); 
			}
		}
	
		return len;
	}
	
	/**
	 * Use the DP table to store intermediary longest common suffixes
	 */
	private static String printLCstr(char[] a, char[] b) {
		if (a == null || b == null) return ""; 
		String[][] table = new String[a.length+1][b.length+1]; // storing longest common suffixes
		String lcstr = "";
		
		for (String[] arr: table) Arrays.fill(arr, ""); // base state

		for (int i=1; i<=a.length; i++) {
			for (int j=1; j<=b.length; j++) {
				if (a[i-1] == b[j-1]) table[i][j] = table[i-1][j-1] + a[i-1];
				else table[i][j] = "";
				if (table[i][j].length() > lcstr.length()) lcstr = table[i][j];
			}
		}
		
		return lcstr;
	}
	
	/**
	 * Optimize space usage by reducing the 2-D table to a single array
	 */
	private static String printLCstrOptimized(char[] a, char[] b) {
		if (a == null || b == null) return ""; 
		String[] temp = new String[b.length+1];
		Arrays.fill(temp, "");
		String preLCsuff = "";
		String curLCsuff = "";
		String lcstr = "";

		for (int i=1; i<=a.length; i++) {
			curLCsuff = "";
			for (int j=1; j<=b.length; j++) {
				preLCsuff = curLCsuff;
				if (a[i-1] == b[j-1]) curLCsuff = temp[j-1] + a[i-1];
				else curLCsuff = "";
				temp[j-1] = preLCsuff; 
				preLCsuff = curLCsuff;
				if (curLCsuff.length() > lcstr.length()) lcstr = curLCsuff;
			}
			temp[b.length] = curLCsuff;
		}
		
		return lcstr;
	}
	
	public static void main(String[] args) {
//		String str1 = "wholovesdummycode"; 
//		String str2 = "dummyloves";
		String str1 = genRanStr(200);
		String str2 = genRanStr(120);
		System.out.println("Welcome to the rabbit hole of longest common substrings! \n"
				+ "The test strings are: \n" + str1 + "\n" + str2 + "\n"); 
		
		try {
			runStringFuncAndCalculateTime("[Iterative][O(n*m)]                 "
					+ "The length of the LCstr is:", (char[] a, char[] b) -> findLengthOfLCstr(a, b), str1.toCharArray(), str2.toCharArray());
			runStringFuncAndCalculateTime("[Iterative][O(n*m)]                 "
					+ "The LCstr is:", (char[] a, char[] b) -> printLCstr(a, b), str1.toCharArray(), str2.toCharArray());
			runStringFuncAndCalculateTime("[Iterative][O(n*m)][Space optimized]"
					+ "The LCstr is:", (char[] a, char[] b) -> printLCstrOptimized(a, b), str1.toCharArray(), str2.toCharArray());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("All rabbits gone.");
	}
	

}
