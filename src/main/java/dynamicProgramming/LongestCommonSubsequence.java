package dynamicProgramming;

import java.util.Arrays;

import utils.FunStringAlgorithm;

/**
 * A subsequence of an array is a sequence of items that appear in order, but not necessarily contiguously. 
 * 
 * Given two strings, 
 * 
 * 1) Find the length of the longest common subsequence (LCS)
 * 2) Print out the longest common subsequence
 * 
 * The solution has very practical applications like the diff utility used in Git to compare the difference between two files, 
 * and bioinformatic analysis in DNA sequencing. 
 * 
 * https://www.geeksforgeeks.org/printing-longest-common-subsequence/
 * 
 * @author ruifengm
 * @since 2018-Apr-10
 */

public class LongestCommonSubsequence extends FunStringAlgorithm {
	
	/**
	 * We look for the sub-problems. 
	 * Let A be a string of size n, and B be a string of size m, and we denote them as A(n) and B(m). 
	 * Let L(A(n), B(m)) be the length of the longest common subsequence, and we look at the last character of A(n) and B(m). 
	 *      if A[n-1] == B[m-1]
	 *      	L(A(n), B(m)) = L(A(n-1), B(m-1)) + 1                   -> the LCS can be extended by one more char
	 *      else 
	 *      	L(A(n), B(m)) = MAX(L(A(n-1), B(m)), L(A(n), B(m-1)))   -> the LCS must occur either between A(n-1) and B(m), or A(n) and B(m-1)
	 *      
	 * The recursive pattern has been found. 
	 */
	private static int recursiveFindLengthOfLCS(char[] a, int aL, char[] b, int bL) {
		// base
		if (a == null || b == null) return 0;
		if (aL == 0 || bL == 0) return 0; 
		// recursion
		if (a[aL-1] == b[bL-1]) return recursiveFindLengthOfLCS(a, aL - 1, b, bL - 1) + 1; 
		else return Math.max(recursiveFindLengthOfLCS(a, aL - 1, b, bL), recursiveFindLengthOfLCS(a, aL, b, bL - 1));
	}
	private static int recursiveFindLengthOfLCSDriver(char[] a, char[] b) {
		return recursiveFindLengthOfLCS(a, a.length, b, b.length);
	}
	
	/**
	 * We do some bookkeeping via DP to avoid the repeated computations so as to reduce the time complexity.
	 * 
	 * A 2-D lookup table of size (n+1)*(m+1) can be used to store the intermediary results. 
	 */
	private static int recursiveFindLengthOfLCSDPMemo(char[] a, int aL, char[] b, int bL, int[][] table) {
		if (table[aL][bL] != Integer.MIN_VALUE) return table[aL][bL];
		else {		
			// base
			if (aL == 0 || bL == 0) table[aL][bL] = 0;
			else {
				// recursion
				if (a[aL-1] == b[bL-1]) table[aL][bL] = recursiveFindLengthOfLCSDPMemo(a, aL - 1, b, bL - 1, table) + 1; 
				else table[aL][bL] = Math.max(recursiveFindLengthOfLCSDPMemo(a, aL - 1, b, bL, table), recursiveFindLengthOfLCSDPMemo(a, aL, b, bL - 1, table));
			}
			return table[aL][bL];
		}
	}
	private static int recursiveFindLengthOfLCSDPMemoDriver(char[] a, char[] b) {
		if (a == null || b == null) return 0;
		int[][] DPLookUp = new int[a.length+1][b.length+1]; // one more row and column to cater for empty char arrays
		for (int i=0; i<DPLookUp.length; i++) Arrays.fill(DPLookUp[i], Integer.MIN_VALUE);
		return recursiveFindLengthOfLCSDPMemo(a, a.length, b, b.length, DPLookUp);
	}
	
	private static int iterativeFindLengthOfLCSDPTabu(char[] a, char[] b) {
		if (a == null || b == null) return 0;
		int aL = a.length, bL = b.length;
		int[][] table = new int[aL+1][bL+1]; // DP lookup table
		
		table[0][0] = 0; // base state
		for (int i=1; i<=aL; i++) {
			for (int j=1; j<=bL; j++) {
				if (a[i-1] == b[j-1]) table[i][j] = table[i-1][j-1] + 1;
				else table[i][j] = Math.max(table[i-1][j], table[i][j-1]);
			}
		}
		// for (int[] arr: table) System.out.println(Arrays.toString(arr));
		return table[aL][bL];
	}
	
	/**
	 * Instead of a 2-D array storing the LCS length, we can make a 2-D array that stores the LCS itself 
	 * so as to print it out eventually.
	 */
	private static String iterativeFindLCSDPTabu(char[] a, char[] b) {
		if (a == null || b == null) return "";
		int aL = a.length, bL = b.length;
		String[][] table = new String[aL+1][bL+1]; // DP lookup table
		
		for (String[] arr: table) Arrays.fill(arr, ""); // base state
		for (int i=1; i<=aL; i++) {
			for (int j=1; j<=bL; j++) {
				if (a[i-1] == b[j-1]) table[i][j] = table[i-1][j-1] + a[i-1];
				else table[i][j] = table[i-1][j].length() > table[i][j-1].length() ? table[i-1][j] : table[i][j-1];
			}
		}
		// for (String[] arr: table) System.out.println(Arrays.toString(arr));
		return table[aL][bL];
	}
	
	/**
	 * Above method lacks a lot in space efficiency. Observing the code tells us that we don't need to 
	 * store all intermediary results. We can use two string arrays and rotate them over.
	 */
	private static String iterativeFindLCSDPTabuOptimized(char[] a, char[] b) {
		if (a == null || b == null) return "";
		int aL = a.length, bL = b.length;
		String[] pre = new String[bL+1]; 
		Arrays.fill(pre, ""); // first row the DP lookup table
		String[] cur = new String[bL+1];
		
		for (int i=1; i<=aL; i++) {
			cur = new String[bL+1];
			Arrays.fill(cur, "");
			for (int j=1; j<=bL; j++) {
				if (a[i-1] == b[j-1]) cur[j] = pre[j-1] + a[i-1];
				else cur[j] = pre[j].length() > cur[j-1].length() ? pre[j] : cur[j-1];
			}
			// rotate
			pre = cur;
			//System.out.println(Arrays.toString(pre));
		}
		return cur[bL];
	}
	
	/**
	 * Further space optimized to use a single string array only by replacing the other array with 2 variables.
	 */
	private static String iterativeFindLCSDPTabuOptimized2(char[] a, char[] b) {
		if (a == null || b == null) return "";
		int aL = a.length, bL = b.length;
		String[] temp = new String[bL+1]; // storing LCSs up to every char in b for each char in a
		Arrays.fill(temp, ""); // first row the DP lookup table
		String curLCS = "";
		String preLCS = "";
		
		for (int i=1; i<=aL; i++) {
			curLCS = "";
			for (int j=1; j<=bL; j++) {
				preLCS = curLCS; // rotate
				if (a[i-1] == b[j-1]) curLCS = temp[j-1] + a[i-1];
				else curLCS = temp[j].length() > preLCS.length() ? temp[j] : preLCS;
				temp[j-1] = preLCS; // store result for next run
			}
			temp[bL] = curLCS; // store result for next run
			//System.out.println(Arrays.toString(temp));
		}
		return curLCS;
	}
	
	
	public static void main(String[] args) {
//		String str1 = "wholovesdummycode"; 
//		String str2 = "dummyloves";
		String str1 = genRanStr(20);
		String str2 = genRanStr(12);
		System.out.println("Welcome to the rabbit hole of longest common subsequences! \n"
				+ "The test strings are: \n" + str1 + "\n" + str2 + "\n"); 
		
		try {
			runStringFuncAndCalculateTime("[Recursive][Exponential]           "
					+ "The length of the LCS is:", (char[] a, char[] b) -> recursiveFindLengthOfLCSDriver(a, b), str1.toCharArray(), str2.toCharArray());
			runStringFuncAndCalculateTime("[Recursive][O(n*m)]                "
					+ "The length of the LCS is:", (char[] a, char[] b) -> recursiveFindLengthOfLCSDPMemoDriver(a, b), str1.toCharArray(), str2.toCharArray());
			runStringFuncAndCalculateTime("[Iterative][O(n*m)]                "
					+ "The length of the LCS is:", (char[] a, char[] b) -> iterativeFindLengthOfLCSDPTabu(a, b), str1.toCharArray(), str2.toCharArray());
			runStringFuncAndCalculateTime("[Iterative][O(n*m)]                "
					+ "The LCS is: ", (char[] a, char[] b) -> iterativeFindLCSDPTabu(a, b), str1.toCharArray(), str2.toCharArray());
			runStringFuncAndCalculateTime("[Iterative][O(n*m)][Space optmized]"
					+ "The LCS is: ", (char[] a, char[] b) -> iterativeFindLCSDPTabuOptimized(a, b), str1.toCharArray(), str2.toCharArray());
			runStringFuncAndCalculateTime("[Iterative][O(n*m)][Space optmized]"
					+ "The LCS is: ", (char[] a, char[] b) -> iterativeFindLCSDPTabuOptimized2(a, b), str1.toCharArray(), str2.toCharArray());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("All rabbits gone.");
	}

}
