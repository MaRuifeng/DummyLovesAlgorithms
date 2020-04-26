package dynamicProgramming;

import java.util.Arrays;

import utils.FunStringAlgorithm;

/**
 * Given two strings str1 and str2 with below character operation allowed, 
 * 
 * 1) insert
 * 2) remove
 * 3) replace
 * 
 * find the minimum number of such operations needed to convert str1 to str2. 
 * 
 * E.g. str1 = "hlloworlde"
 *      str2 = "helloworld" 
 * Two operations needed, insert first 'e' and remove second 'e'. 
 * 
 * https://www.geeksforgeeks.org/dynamic-programming-set-5-edit-distance/
 * 
 * This is also referred to as the "edit distance" algorithm, which has a lot of practical applications, e.g. display
 * words that are near proximity to a given incorrectly spelled word. 
 * 
 * @author ruifengm
 * @since 2018-Apr-15
 */
public class MinStringEdits extends FunStringAlgorithm {
	
	/**
	 * Similar to the LCS problem, the problem can be attempted via a DP technique. 
	 * Let A(n) be a string of size n and B be a string of size m, let Sol(A(n), B(m)) denote the number of minimum char-level edits
	 * needed to convert A to B. 
	 *      if A[n-1] = B[m-1]
	 *			Sol(A(n), B(m)) = Sol(A(n-1), B(m-1))          -->  no edits needed
	 *      else            
	 *          Sol(A(n), B(m)) = MIN(Sol(A(n), B(m-1)) + 1,   -->  convert A(n) to B(m-1) and insert B[m-1]
	 *                                Sol(A(n-1), B(m)) + 1,   -->  convert A(n-1) to B(m) and remove A[n-1]
	 *                                Sol(A(n-1), B(m-1)) + 1) -->  convert A(n-1) to B(m-1) and replace A[n-1] with B[m-1]
	 */
	private static int recursiveFindMinEdits(char[] a, int aL, char[] b, int bL) {
		if (a == null || b == null) return 0; 
		if (aL == 0) return bL; // insert all elements in b
		if (bL == 0) return aL; // remove all elements in a
		if (a[aL-1] == b[bL-1]) return recursiveFindMinEdits(a, aL-1, b, bL-1);
		else return Math.min(Math.min(recursiveFindMinEdits(a, aL-1, b, bL)+1, recursiveFindMinEdits(a, aL, b, bL-1)+1),
				recursiveFindMinEdits(a, aL-1, b, bL-1)+1);
	}
	private static int recursiveFindMinEditsDriver(char[] a, char[] b) {
		return recursiveFindMinEdits(a, a.length, b, b.length);
	}
	
	private static int recursiveFindMinEditsDPMemo(char[] a, int aL, char[] b, int bL, int[][] table) {
		if (table[aL][bL] != -1) return table[aL][bL]; 
		else {		
			if (aL == 0) table[aL][bL] = bL; // insert all elements in b
			else if (bL == 0) table[aL][bL] = aL; // remove all elements in a
			else if (a[aL-1] == b[bL-1]) table[aL][bL] = recursiveFindMinEditsDPMemo(a, aL-1, b, bL-1, table);
			else table[aL][bL] = Math.min(Math.min(recursiveFindMinEditsDPMemo(a, aL-1, b, bL, table)+1, recursiveFindMinEditsDPMemo(a, aL, b, bL-1, table)+1),
				recursiveFindMinEditsDPMemo(a, aL-1, b, bL-1, table)+1);
			return table[aL][bL];
		}

	}
	private static int recursiveFindMinEditsDPMemoDriver(char[] a, char[] b) {
		if (a == null || b == null) return 0; 
		int[][] DPLookUp = new int[a.length+1][b.length+1]; 
		for (int[] row: DPLookUp) Arrays.fill(row, -1);
		int res = recursiveFindMinEditsDPMemo(a, a.length, b, b.length, DPLookUp);
		//for(int[] row: DPLookUp) System.out.println(Arrays.toString(row));
		return res;
	}
	
	private static int iterativeFindMinEditsDPTabu(char[] a, char[] b) {
		if (a == null || b == null) return 0;
		int[][] table = new int[a.length+1][b.length+1]; // DP lookup table
		// base state
		int aL = a.length, bL = b.length;
		for (int i=0; i<=bL; i++) table[0][i] = i;
		for (int i=0; i<=aL; i++) table[i][0] = i;
		// proliferation
		for (int i=1; i<=aL; i++) {
			for (int j=1; j<=bL; j++) {
				if (a[i-1] == b[j-1]) table[i][j] = table[i-1][j-1];
				else table[i][j] = Math.min(Math.min(table[i-1][j]+1, table[i][j-1]+1), table[i-1][j-1]+1);
			}
		}
		for(int[] row: table) System.out.println(Arrays.toString(row));
		return table[aL][bL];
	}

	
	public static void main(String[] args) {
//		String str1 = "hlloworlde"; 
//		String str2 = "helloworld";
//		String str1 = "wholovesdummycode"; 
//		String str2 = "dummyloves";
		String str1 = genRanStr(15);
		String str2 = genRanStr(8);
		System.out.println("Welcome to the rabbit hole of minimum string edits! \n"
				+ "The test strings are: \n" + str1 + "\n" + str2 + "\n"); 
		
		try {
			runStringFuncAndCalculateTime("[Recursive][Very Exponential]  "
					+ "The minimum number of string edits required is:", (char[] a, char[] b) -> recursiveFindMinEditsDriver(a, b), str1.toCharArray(), str2.toCharArray());
			runStringFuncAndCalculateTime("[Recursive][O(n*m)]            "
					+ "The minimum number of string edits required is:", (char[] a, char[] b) -> recursiveFindMinEditsDPMemoDriver(a, b), str1.toCharArray(), str2.toCharArray());
			runStringFuncAndCalculateTime("[Iterative][O(n*m)]            "
					+ "The minimum number of string edits required is:", (char[] a, char[] b) -> iterativeFindMinEditsDPTabu(a, b), str1.toCharArray(), str2.toCharArray());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("All rabbits gone.");
	}
}
