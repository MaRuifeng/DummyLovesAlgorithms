package string;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Test;

/**
 * Given three strings: s1, s2, s3, determine whether s3 is formed by the interleaving of s1 and s2.
 * 
 * E.g. 
 * For s1 = "aabcc", s2 = "dbbca"
 * When s3 = "aadbbcbcac", return true.
 * When s3 = "aadbbbaccc", return false.
 * 
 * @author ruifengm
 * @since 2018-Jun-10
 * 
 * https://www.lintcode.com/problem/interleaving-string/description
 *
 */

public class InterleavedString {
	
	/**
	 * Use recursion.
	 */
	private static boolean isInterleaved(String s1, String s2, String s3) {
		if (s3.length() == s1.length() + s2.length()) { // check lengths first
			return recursiveIsInterleaved(s1.toCharArray(), s1.length(),
					s2.toCharArray(), s2.length(), s3.toCharArray(), s3.length());
		} else return false; 
	}
	private static boolean recursiveIsInterleaved(char[] a1, int size1, 
			char[] a2, int size2, char[] a3, int size3) {
		if (size1 == 0 && size2 == 0) {
			if (size3 == 0) return true; 
			else return false;
		} else if (size3 > 0) {
			if (size1 > 0 && a1[size1-1] == a3[size3-1]) {
				if (size2 > 0 && a2[size2-1] == a3[size3-1]) { // found a tie, recur in both cases
					return recursiveIsInterleaved(a1, size1-1, a2, size2, a3, size3-1) ||
							recursiveIsInterleaved(a1, size1, a2, size2-1, a3, size3-1);
				} else {
					return recursiveIsInterleaved(a1, size1-1, a2, size2, a3, size3-1); 
				}
			} else if (size2 > 0 && a2[size2-1] == a3[size3-1]) {
				return recursiveIsInterleaved(a1, size1, a2, size2-1, a3, size3-1);
			} else return false;
		} else return false;
	}
	
	/**
	 * Let's try to improve above method with DP memoization and tabulation.
	 */
	private static boolean isInterleavedDPMemo(String s1, String s2, String s3) {
		if (s3.length() == s1.length() + s2.length()) { // check lengths first
			Boolean[][] DPLookUP = new Boolean[s1.length()+1][s2.length()+1];
			return recursiveIsInterleavedDPMemo(s1, s2, s3, DPLookUP);
		} else return false; 
	}
	private static boolean recursiveIsInterleavedDPMemo(String s1, String s2, String s3, Boolean[][] table) {
		int size1 = s1.length(), size2 = s2.length(), size3 = s3.length();
		if (table[size1][size2] != null) return table[size1][size2]; 
		else {
			if (size1 == 0) {
				if (s2.equals(s3)) table[size1][size2] = true; 
				else table[size1][size2] = false;
			} else if (size2 == 0) {
				if (s1.equals(s3)) table[size1][size2] = true;
				else table[size1][size2] = false;
			} else if (!s3.isEmpty()) {
				boolean bool1 = false, bool2 = false;
				if (s1.charAt(size1-1) == s3.charAt(size3-1)) {
					bool1 = recursiveIsInterleavedDPMemo(s1.substring(0, size1-1), s2, s3.substring(0, size3-1), table);
				}
				if (s2.charAt(size2-1) == s3.charAt(size3-1)) {
					bool2 = recursiveIsInterleavedDPMemo(s1, s2.substring(0, size2-1), s3.substring(0, size3-1), table);
				}
				table[size1][size2] = bool1 || bool2;
			} else table[size1][size2] = false;
			return table[size1][size2]; 
		}
	}
	
	private static boolean isInterleavedDPTabu(String s1, String s2, String s3) {
		if (s3.length() == s1.length() + s2.length()) { // check lengths first
			int size1 = s1.length(), size2 = s2.length();
			boolean[][] table = new boolean[size1+1][size2+1]; // DP lookup table
			// base state
			table[0][0] = true;
			for (int i=1; i<=size1; i++) if (s1.charAt(i-1) == s3.charAt(i-1)) table[i][0] = true; 
			for (int j=1; j<=size2; j++) if (s2.charAt(j-1) == s3.charAt(j-1)) table[0][j] = true; 
//			for (int i=0; i<=size1; i++) if (s1.substring(0, i).equals(s3.substring(0, i))) table[i][0] = true; 
//			for (int j=0; j<=size2; j++) if (s2.substring(0, j).equals(s3.substring(0, j))) table[0][j] = true; 
			// proliferation
			for (int i=1; i<=size1; i++) {
				for (int j=1; j<=size2; j++) {
					boolean bool1 = false, bool2 = false; 
					if (s1.charAt(i-1) == s3.charAt(i+j-1)) bool1 = table[i-1][j]; 
					if (s2.charAt(j-1) == s3.charAt(i+j-1)) bool2 = table[i][j-1]; 
					table[i][j] = bool1 || bool2; 
				}
			}
			// for (boolean[] row: table) System.out.println(Arrays.toString(row));
			return table[size1][size2];
		} else return false;
	}
	
	
	/* Test */
	static class TestData {
		String s1, s2, s3;
		boolean interleaved;
		public TestData(String s1, String s2, String s3, boolean interleaved) {
			this.s1 = s1;
			this.s2 = s2;
			this.s3 = s3; 
			this.interleaved = interleaved;
		}
	}
	
	@Test
	private static void test(TestData data) {
		System.out.println("\n/******** TEST START ********/\n");
		System.out.println("The test strings are:" + "\nS1: " + data.s1 + "\nS2: " + data.s2 + "\nS3: " + data.s3 + "\n");
		
		boolean actual = isInterleaved(data.s1, data.s2, data.s3);
		System.out.println("S3 formed by interleaving S1 and S2 (Recursion)? " + actual);
		assertEquals(data.interleaved, actual);
		
		actual = isInterleavedDPMemo(data.s1, data.s2, data.s3);
		System.out.println("S3 formed by interleaving S1 and S2 (DP Memo)? " + actual);
		assertEquals(data.interleaved, actual);
		
		actual = isInterleavedDPTabu(data.s1, data.s2, data.s3);
		System.out.println("S3 formed by interleaving S1 and S2 (DP Tabu)? " + actual);
		assertEquals(data.interleaved, actual);
		
		System.out.println("\n/******** TEST END ********/\n");
	}
	
	public static void main(String[] args) {
		ArrayList<TestData> dataList = new ArrayList<>(); 
		dataList.add(new TestData("aba", "a", "aaba", true));
		dataList.add(new TestData("aabcc", "dbbca", "aadbbcbcac", true));
		dataList.add(new TestData("aabcc", "dbbca", "aadbbbaccc", false));
		dataList.add(new TestData("aabcc", "dbbca", "aadbbbcace", false));
		dataList.add(new TestData("aabcc", "dbbca", "eaadbbbcac", false));
		dataList.add(new TestData("aabcce", "dbbca", "aadbbcbcac", false));
		dataList.add(new TestData("aabcc", "dbbca", "", false));
		dataList.add(new TestData("aabcc", "", "aadbbcbcac", false));
		dataList.add(new TestData("", "", "aadbbcbcac", false));
		dataList.add(new TestData("", "", "", true));
		dataList.add(new TestData("asdfjopufaaf;lasnopfodifa;djfal;sfjdsf", 
				"adlkjfoqweijzdkj;ljfeowfjl;djfnd", 
				"asdfjopufaaf;lasnopfodadlkjfoqweijzdkj;ljfeowfjl;djfneifa;djfal;sfjdsf", 
				false));
		dataList.add(new TestData("sdfjas;dfjoisdufzjkndfasdkfja;sdfa;dfa;dfaskdjhfasdhjdfakhdgfkajdfasdjfgajksdfgaksdhfasdkbfjkdsfbajksdfhakjsdfbajkdfbakdjsfgaksdhgfjkdsghfkdsfgadsjfgkajsdgfkjasdfh", 
				"dfnakdjnfjkzghdufguweygfasjkdfgb2gf8asf7tgbgasjkdfgasodf7asdgfajksdfguayfgaogfsdkagfsdhfajksdvfbgkadsghfakdsfgasduyfgajsdkfgajkdghfaksdgfuyadgfasjkdvfjsdkvfakfgauyksgfajkefgjkdasgfdjksfgadjkghfajksdfgaskdjfgasjkdgfuyaegfasdjkfgajkdfygadjskfgjkadfg", 
				"sdfjas;dfjoisdfnakdjnfjkzghdufguwdufzjkeygfasjkdfgb2gf8asf7ndtgbgasjkdfgasodf7asdfgfajkasdksdfguayfgaogfsdkagfsfjadhfajksdvfbgkadsghfa;sdkdsfgasduyfgajsdkfgafajkdghfaksdgfuyadgfas;dfjkdvfjsdkvfakfgauyksa;dgfajkefgjkdasgfdjksffaskdjhfasdhjdfakhdgadjkghfajgfkajdfksdfgaskdjfgasjkdgfuasdjfgajksdfgaksdhfasdkbfjkdsfbajksdfyaegfasdjkfgajkdfygadjskfgjkadfghakjsdfbajkdfbakdjsfgaksdhgfjkdsghfkdsfgadsjfgkajsdgfkjasdfh", 
				true));
		dataList.add(new TestData("aacaac", "aacaaeaac", "aacaaeaaeaacaac", false));
		System.out.println("Welcome to the rabbit hole of interleaved strings! \n");
		
		for (TestData data: dataList) test(data);
		
		System.out.println("All rabbits gone.");
	}

}
