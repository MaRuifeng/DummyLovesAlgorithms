package string;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.Test;

/**
 * For a given source string and a pattern string, find the first index of the pattern 
 * string in the source string. 
 * 
 * E.g. the first index of "pile" in "compilecode" is 3. 
 * 
 * String pattern search is a very important problem in computer science with many useful applications. 
 * Note: refer to the KMP algorithm.
 * 
 * @author ruifengm
 * @since 2018-may-31
 * 
 * https://www.lintcode.com/problem/implement-strstr/description
 * https://www.geeksforgeeks.org/searching-for-patterns-set-2-kmp-algorithm/
 */

public class StringPatternSearch {
	
	/**
	 * An intuitive method is to use two indices to traverse the two strings and check. 
	 * This method generally works fine but its worst cases involve a lot of redundant checks which
	 * push the time complexity to an upper bound of O(n*m). 
	 * E.g. source = "AAAAAAAAAAB", pattern = "AAB"
	 */
	private static int getPatternFirstIdx(String src, String pat) {
		if (src == null || pat == null) return -1; 
		if (src.isEmpty() && pat.isEmpty()) return 0; 
		int idx = -1, srcIdx = 0, patIdx = 0; 
		while (srcIdx < src.length()) {
			if (pat.isEmpty()) return 0;
			if (src.charAt(srcIdx) == pat.charAt(patIdx)) {
				if(idx == -1) idx = srcIdx; 
				patIdx++; 
			} else {
				idx = -1;
				if (patIdx != 0) {
					srcIdx -= patIdx;
					patIdx = 0; // reset and look up again
					
				}
			}
			if (patIdx == pat.length()) return idx; // pattern found
			srcIdx++;
			if (src.length()-srcIdx < pat.length()-patIdx) return -1; // remaining source not able to cover remaining pattern
		}
		return idx; 
	}

	private static ArrayList<Integer> getPatternAllIndices(String src, String pat) {
		ArrayList<Integer> idxList = new ArrayList<>(); 
		if (src == null || pat == null) return idxList;
		if (src.isEmpty()) {
			if (pat.isEmpty()) idxList.add(0);
			return idxList;
		}
		int idx = -1, srcIdx = 0, patIdx = 0; 
		while (srcIdx < src.length()) {
			if (pat.isEmpty()) idxList.add(srcIdx++); 
			else {
				if (src.charAt(srcIdx) == pat.charAt(patIdx)) {
					if(idx == -1) idx = srcIdx; 
					patIdx++; 
				} else {
					idx = -1;
					if (patIdx != 0) {
						srcIdx -= patIdx;
						patIdx = 0; // reset and look up again
					}
				}
				if (patIdx == pat.length()) {
					idxList.add(idx); // pattern found
				    srcIdx -= (pat.length()-1);
					patIdx = 0; // reset and look up again
					idx = -1;
				}
				srcIdx++;
				if (src.length()-srcIdx < pat.length()-patIdx) break; // remaining source not able to cover remaining pattern
			}
		}
		return idxList; 
	}
	
	/**
	 * The KMP algorithm reduces the worst case scenario time complexity down to O(n) with 
	 * an auxiliary integer array that stores the lengths of the longest proper prefix which 
	 * is also a suffix (the LPS array) at each point in the pattern(pattern) string. 
	 * A detailed explanation is given here. 
	 * https://www.geeksforgeeks.org/searching-for-patterns-set-2-kmp-algorithm/
	 */
	private static ArrayList<Integer> getPatternAllIndicesKMP(String src, String pat) {
		ArrayList<Integer> idxList = new ArrayList<>(); 
		if (src == null || pat == null) return idxList;
		if (src.isEmpty()) {
			if (pat.isEmpty()) idxList.add(0);
			return idxList;
		}
		
		int[] lps = getLPSArray(pat);
		int srcIdx = 0, patIdx = 0; 
		while (srcIdx < src.length()) {
			if (pat.isEmpty()) idxList.add(srcIdx++);
			else {
				if (src.charAt(srcIdx) == pat.charAt(patIdx)) {
					srcIdx++; 
					patIdx++; 
				} else {
					if (patIdx != 0) patIdx = lps[patIdx-1]; // reset pattern index based on the LPS array
					else srcIdx++;
				}
				if (patIdx == pat.length()) {
					idxList.add(srcIdx - patIdx);
					patIdx = lps[patIdx-1];
				}
			}
		}
		return idxList;
	}
	private static int[] getLPSArray(String pat) {
		int size = pat.length(); 
		int[] lps = new int[size];
		if (pat.isEmpty()) return lps;
		int i = 1, len = 0;
		lps[0] = 0; 
		
		while(i < size) {
			if (pat.charAt(i) == pat.charAt(len)) lps[i++] = ++len; 
			else { // no match
				if (len != 0) len = lps[len-1]; 
				else lps[i++] = 0; 
			}
		}
		return lps;
	}
	
	/* Test */
	static class TestData {
		String src, pat;
		int idx;
		public TestData(String src, String pat, int idx) {
			this.src = src;
			this.pat = pat; 
			this.idx = idx;
		}
	}
	@Test
	public static void test(TestData data) {
		System.out.println("\n/******** TEST START ********/");
		System.out.println("Source string: " + data.src); 
		System.out.println("Pattern string: " + data.pat); 
		int actualIdx; 
		
		actualIdx = getPatternFirstIdx(data.src, data.pat);
		System.out.println("The first index is at " + actualIdx);
		assertEquals(data.idx, actualIdx);
		
		System.out.println("/******** TEST End ********/");
	}
	
	public static void printAllIndices(String source, String pattern) {
		System.out.println("##### Intuitive Approach #####");
		System.out.println("Source string(" + source.length() + "): " + source);
		System.out.println("Pattern string(" + pattern.length() + "): " + pattern);
		ArrayList<Integer> idxList = getPatternAllIndices(source, pattern); 
		System.out.println("Posistions: " + idxList.toString() + "\n");
		
		System.out.println("##### KMP Approach #####");
		System.out.println("Source string(" + source.length() + "): " + source);
		System.out.println("Pattern string(" + pattern.length() + "): " + pattern);
		idxList = getPatternAllIndicesKMP(source, pattern); 
		System.out.println("Posistions: " + idxList.toString() + "\n");
	}

	public static void main(String[] args) {
		System.out.println("Welcome to the rabbit hole of strStr() implementations!\n");
		
		ArrayList<TestData> testDataList = new ArrayList<>(); 
		testDataList.add(new TestData("compilecode", "pile", 3));
		testDataList.add(new TestData("abcdefghijkabc", "def", 3));
		testDataList.add(new TestData("abcabcabc", "bca", 1));
		testDataList.add(new TestData("veryshortSource", "veryveryverylongtarget", -1));
		testDataList.add(new TestData("abc", "abc", 0));
		testDataList.add(new TestData("abc", "abcd", -1));
		testDataList.add(new TestData("abcabcde", "abcde", 3));
		testDataList.add(new TestData("aaaaab", "aab", 3));
		testDataList.add(new TestData("gabcabcg", "abc", 1));
		testDataList.add(new TestData("gabcabcg", "abcabc", 1));
		testDataList.add(new TestData("abc", "c", 2));
		testDataList.add(new TestData("abc", "a", 0));
		testDataList.add(new TestData("aaaaaaaa", "a", 0));
		testDataList.add(new TestData("", "", 0));
		testDataList.add(new TestData("abc", "", 0));
		testDataList.add(new TestData("", "abc", -1));
		
		for (TestData d: testDataList) test(d);
		
		System.out.println("\n/* Find all occurrences of the pattern string and print out their starting index */");
		String source = "AABAACAADAABAABA", pattern = "AABA";
		printAllIndices(source, pattern);
		
		source = "AAABCABAAAAAABCABAAAABCABAAA";
		pattern = "AAABCAB";
		printAllIndices(source, pattern);
		
		source = "AAAAAAAAAAAAAAAAAA";
		pattern = "AAAAA";
		printAllIndices(source, pattern);
		
		source = "AAAAAAAAAAAAAAAAAA";
		pattern = "A";
		printAllIndices(source, pattern);
		
		source = "AAAAAAAAAAAAAAAAAB";
		pattern = "A";
		printAllIndices(source, pattern);
		
		source = "AAAAAAAAAAAAAAAAAB";
		pattern = "";
		printAllIndices(source, pattern);
		
		source = "AAAABAAB";
		pattern = "AAB";
		printAllIndices(source, pattern);
		
		source = "";
		pattern = "";
		printAllIndices(source, pattern);
		
		source = "";
		pattern = "AA";
		printAllIndices(source, pattern);
		
		System.out.println("\nAll rabbits gone.");
	}

}
