package dynamicProgramming;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

import utils.FunStringAlgorithm;

/**
 * Given two strings, find the shortest string that has both strings as subsequences. 
 * 
 * E.g. given "rabbit" and "rattle", the result is "rabbittle". 
 * 
 * @author ruifengm
 * @since 2018-May-10
 * 
 * https://www.geeksforgeeks.org/shortest-common-supersequence/
 */

public class ShortestCommonSupersequence extends FunStringAlgorithm {
	
	/**
	 * We try to look for the length of the shortest common supersequence (SCS) first. 
	 * Let A(n) be a string of size n, and B(m) be a string of size m, and let Sol(A(n), B(m)) denote the 
	 * length of their SCS. 
	 *     Sol(A(n), B(m)) = Sol(A(n-1), B(m-1)) + 1                          if A[n-1] == B[m-1]
	 *                       MIN(Sol(A(n-1), B(m)), Sol(A(n), B(m-1)) + 1     else
	 *                       
	 * It's also worth noting that
	 *     length of SCS = (length of A + length of B) - length of LCS
	 */
	private static int recursiveGetLengthOfSCS(char[] a, char[] b, int aL, int bL) {
		if (aL == 0) return bL;
		if (bL == 0) return aL; 
		if (a[aL-1] == b[bL-1]) return recursiveGetLengthOfSCS(a, b, aL-1, bL-1) + 1; 
		else return Math.min(recursiveGetLengthOfSCS(a, b, aL, bL-1), recursiveGetLengthOfSCS(a, b, aL-1, bL)) + 1; 
	}
	private static int recursiveGetLengthOfSCSDriver(char[] a, char[] b) {
		return recursiveGetLengthOfSCS(a, b, a.length, b.length);
	}
	
	/**
	 * Avoid repeated computations through DP memoization
	 */
	private static int recursiveGetLengthOfSCSDPMemo(char[] a, char[] b, int aL, int bL, int[][] table) {
		if (table[aL][bL] != 0) return table[aL][bL]; 
		else {
			if (aL == 0) table[aL][bL] = bL;
			else if (bL == 0) table[aL][bL] = aL; 
			else {
				if (a[aL-1] == b[bL-1]) table[aL][bL] = recursiveGetLengthOfSCS(a, b, aL-1, bL-1) + 1; 
				else table[aL][bL] = Math.min(recursiveGetLengthOfSCSDPMemo(a, b, aL, bL-1, table), 
						recursiveGetLengthOfSCSDPMemo(a, b, aL-1, bL, table)) + 1;
			}
			return table[aL][bL];
		}
	}
	private static int recursiveGetLengthOfSCSDPMemoDriver(char[] a, char[] b) {
		int[][] DPLookUp = new int[a.length+1][b.length+1];
		return recursiveGetLengthOfSCSDPMemo(a, b, a.length, b.length, DPLookUp);
	}
	
	/**
	 * Avoid repeated computations through DP tabulation
	 */
	private static int iterativeGetLengthOfSCSDPTabu(char[] a, char[] b) {
		int aL = a.length, bL = b.length;
		int[][] table = new int[aL+1][bL+1]; // DP lookup table
		// base state
		for (int i=0; i<=aL; i++) table[i][0] = i; 
		for (int i=0; i<=bL; i++) table[0][i] = i;
		for (int i=1; i<=aL; i++) 
			for (int j=1; j<=bL; j++) {
				if (a[i-1] == b[j-1]) table[i][j] = table[i-1][j-1] + 1; 
				else table[i][j] = Math.min(table[i][j-1], table[i-1][j]) + 1;
			}
		return table[aL][bL];
	}
	
	/**
	 * Get one shortest common supersequence through DP tabulation
	 */
	private static String iterativeGetSCSDPTabu(char[] a, char[] b) {
		int aL = a.length, bL = b.length;
		String[][] table = new String[aL+1][bL+1]; // DP lookup table
		// base state
		table[0][0] = "";
		for (int i=1; i<=aL; i++) table[i][0] = table[i-1][0] + a[i-1];
		for (int i=1; i<=bL; i++) table[0][i] = table[0][i-1] + b[i-1];
		// proliferation
		for (int i=1; i<=aL; i++) 
			for (int j=1; j<=bL; j++) {
				if (a[i-1] == b[j-1]) table[i][j] = table[i-1][j-1] + a[i-1]; 
				else {
					if (table[i][j-1].length() <= table[i-1][j].length()) table[i][j] = table[i][j-1] + b[j-1];
					else table[i][j] = table[i-1][j] + a[i-1];
				}
			}
		return table[aL][bL];
	}
	
	/**
	 * Get all shortest common supersequences through DP tabulation
	 */
	@SuppressWarnings("unchecked")
	private static ArrayList<String> iterativeGetAllSCSDPTabu(char[] a, char[] b) {
		int aL = a.length, bL = b.length;
		ArrayList<String>[][] table = new ArrayList[aL+1][bL+1]; // DP lookup table
		// base state
		table[0][0] = new ArrayList<String>();
		table[0][0].add("");
		for (int i=1; i<=aL; i++) {
			table[i][0] = new ArrayList<String>();
			for (String str: table[i-1][0]) table[i][0].add(str + a[i-1]);
		}
		for (int i=1; i<=bL; i++) {
			table[0][i] = new ArrayList<String>();
			for (String str: table[0][i-1]) table[0][i].add(str + b[i-1]);
		}
		// proliferation
		for (int i=1; i<=aL; i++) 
			for (int j=1; j<=bL; j++) {
				table[i][j] = new ArrayList<String>();
				if (a[i-1] == b[j-1]) for (String str: table[i-1][j-1]) table[i][j].add(str + a[i-1]); 
				else {
					if (table[i][j-1].get(0).length() <= table[i-1][j].get(0).length()) 
						for (String str: table[i][j-1]) table[i][j].add(str + b[j-1]);
					if (table[i][j-1].get(0).length() >= table[i-1][j].get(0).length())
						for (String str: table[i-1][j]) table[i][j].add(str + a[i-1]);
				}
			}
		return table[aL][bL];
	}
	
	/**
	 * Get all shortest common supersequences through DP tabulation with space optimized
	 */
	@SuppressWarnings("unchecked")
	private static ArrayList<String> iterativeGetAllSCSDPTabuSpaceOptimized(char[] a, char[] b) {
		int aL = a.length, bL = b.length;
		ArrayList<String>[] table = new ArrayList[bL+1]; // DP lookup table
		// base state
		table[0]= new ArrayList<String>();
		table[0].add("");
		for (int i=1; i<=bL; i++) {
			table[i] = new ArrayList<String>();
			for (String str: table[i-1]) table[i].add(str + b[i-1]);
		}
		// proliferation
		ArrayList<String> pre = new ArrayList<>();
		ArrayList<String> cur = new ArrayList<>();
		cur.add(String.valueOf(b));
		for (int i=1; i<=aL; i++) {
			cur = new ArrayList<String>();
			String str = "";
			for (int k=0; k<i; k++) str += a[k]; 
			cur.add(str);
			for (int j=1; j<=bL; j++) {
				pre = new ArrayList<String>();
				pre.addAll(cur);
				cur = new ArrayList<>();
				if (a[i-1] == b[j-1]) for (String s: table[j-1]) cur.add(s + a[i-1]); 
				else {
					if (pre.get(0).length() <= table[j].get(0).length()) 
						for (String s: pre) cur.add(s + b[j-1]);
					if (pre.get(0).length() >= table[j].get(0).length())
						for (String s: table[j]) cur.add(s + a[i-1]);
				}
				table[j-1] = new ArrayList<>();  
				table[j-1].addAll(pre); // store result
			}
			table[bL] = new ArrayList<>();
			table[bL].addAll(cur);
		}
		return cur;
	}
	
	@FunctionalInterface
	protected interface DoubleCharArrayToStringListFunction {
		ArrayList<String> apply(char[] a, char[] b);
	}
	
    protected static ArrayList<String> runStringFuncAndCalculateTime(String message, DoubleCharArrayToStringListFunction strFunc, char[] a, char[] b) throws Exception {
    	long startTime = System.nanoTime();
    	ArrayList<String> result = strFunc.apply(a, b);
    	System.out.printf("%-70s\n", message);
    	for (String str: result) System.out.println(str);
    	long endTime = System.nanoTime();
    	long totalTime = new Long(TimeUnit.MICROSECONDS.convert(endTime - startTime, TimeUnit.NANOSECONDS));
    	DecimalFormat formatter = new DecimalFormat("#,###");
    	System.out.printf("%-70s%s\n\n", "Function execution time in micro-seconds: ", formatter.format(totalTime));
    	return result;
    }
	
	/* Test */
	static class TestData {
		String str1, str2; 
		int expectedLen; 
		String expectedScs;
		public TestData(String a, String b, int size, String str) {
			this.str1 = a; 
			this.str2 = b; 
			this.expectedLen = size;
			this.expectedScs = str;
		}
	}
	
	@Test
	public static void test(TestData data) {
		try {
			int actualLen;
			ArrayList<String> actualSCSList;
			System.out.println("The test strings are: \n" + data.str1 + "\n" + data.str2); 
			actualLen = runStringFuncAndCalculateTime("[Recursive][Exponential]           "
					+ "The length of the SCS is:", (char[] a, char[] b) -> recursiveGetLengthOfSCSDriver(a, b), data.str1.toCharArray(), data.str2.toCharArray());
			assertEquals(data.expectedLen, actualLen);
			
			actualLen = runStringFuncAndCalculateTime("[Recursive][O(n*m)]                "
					+ "The length of the LCS is:", (char[] a, char[] b) -> recursiveGetLengthOfSCSDPMemoDriver(a, b), data.str1.toCharArray(), data.str2.toCharArray());
			assertEquals(data.expectedLen, actualLen);
			
			actualLen = runStringFuncAndCalculateTime("[Iterative][O(n*m)]                "
					+ "The length of the LCS is:", (char[] a, char[] b) -> iterativeGetLengthOfSCSDPTabu(a, b), data.str1.toCharArray(), data.str2.toCharArray());
			assertEquals(data.expectedLen, actualLen);
			
			runStringFuncAndCalculateTime("[Iterative][O(n*m)]                "
					+ "A possible SCS is: ", (char[] a, char[] b) -> iterativeGetSCSDPTabu(a, b), data.str1.toCharArray(), data.str2.toCharArray());
			
			actualSCSList = runStringFuncAndCalculateTime("[Iterative][O(n*m)]                "
					+ "List of SCSs: ", (char[] a, char[] b) -> iterativeGetAllSCSDPTabu(a, b), data.str1.toCharArray(), data.str2.toCharArray());
			assertTrue(actualSCSList.contains(data.expectedScs));
			
			actualSCSList = runStringFuncAndCalculateTime("[Iterative][O(n*m)][Space Optimized]  "
					+ "List of SCSs: ", (char[] a, char[] b) -> iterativeGetAllSCSDPTabuSpaceOptimized(a, b), data.str1.toCharArray(), data.str2.toCharArray());
			assertTrue(actualSCSList.contains(data.expectedScs));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		System.out.println("Welcome to the rabbit hole of shortest common supersequences! \n");
		ArrayList<TestData> dataList = new ArrayList<>(); 
		dataList.add(new TestData("", "", 0, ""));
		dataList.add(new TestData("a", "", 1, "a"));
		dataList.add(new TestData("", "a", 1, "a"));
		dataList.add(new TestData("a", "a", 1, "a"));
		dataList.add(new TestData("a", "b", 2, "ab"));
		dataList.add(new TestData("abb", "b", 3, "abb"));
		dataList.add(new TestData("rabbit", "rattle", 9, "rabbittle"));
		dataList.add(new TestData("AGGTAB", "GXTXAYB", 9, "AGXGTXAYB"));
		dataList.add(new TestData("aabbccdaddbddeedaceaad", "aabccdabbadec", 24, "aabbccdaddbdbadeedaceaad"));
		//dataList.add(new TestData(genRanStr(16), genRanStr(15), 9));
		
		for (TestData data: dataList) test(data);
		System.out.println("All rabbits gone.");
	}

}
