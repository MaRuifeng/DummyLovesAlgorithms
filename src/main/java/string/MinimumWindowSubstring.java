package string;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Given a string source and a string target, find the minimum window in source which will contain all the characters in target.
 * If there is no such window in source that covers all characters in target, return the empty string "".
 * If there are multiple such windows, you are guaranteed that there will always be only one unique minimum window in source.
 * 
 * Should the characters in minimum window has the same order in target? No.
 * 
 * @author ruifengm
 * @since 2018-Jun-16
 * 
 * https://www.lintcode.com/problem/minimum-window-substring/description
 * https://www.geeksforgeeks.org/find-the-smallest-window-in-a-string-containing-all-characters-of-another-string/
 *
 */
public class MinimumWindowSubstring {
	
	/**
	 * The idea is to keep two hashes denote character occurrences in the source and target strings and use
	 * them to find the minimum window substring.
	 */
	private static String minWinSubstr(String src, String tar) {
		int srcLen = src.length(), tarLen = tar.length(); 
		if (srcLen < tarLen) return "";
		
		int[] srcHash = new int[256], tarHash = new int[256]; // ASCII table size
		for (char c: tar.toCharArray()) tarHash[c]++; // populate target string hash
		
		int windowStart = 0, minWindowStart = -1, minWindowLen = Integer.MAX_VALUE;
		int count = 0; // count of char matches
		for (int i=0; i<srcLen; i++) {
			srcHash[src.charAt(i)]++; // populate source string hash
			if (tarHash[src.charAt(i)] != 0 &&
					srcHash[src.charAt(i)] <= tarHash[src.charAt(i)])
				count++; // matched character found
			
			if (count == tarLen) { // all target string characters matched, try to minimize the found window
				while (srcHash[src.charAt(windowStart)] > tarHash[src.charAt(windowStart)] 
						|| tarHash[src.charAt(windowStart)] == 0) {
					if (srcHash[src.charAt(windowStart)] > tarHash[src.charAt(windowStart)])
						srcHash[src.charAt(windowStart)]--;
					windowStart++; 
				}
				int windowLen = i - windowStart + 1; 
				if (minWindowLen > windowLen) {
					minWindowLen = windowLen;
					minWindowStart = windowStart;
				}
			}
		}
		
		if (minWindowStart == -1) return ""; 
		return src.substring(minWindowStart, minWindowStart + minWindowLen);
	}
	
	public static void main(String[] args) {
		String src = "wholovesdummycode"; 
		String tar = "loe";
		System.out.println("Welcome to the rabbit hole of minimum window substrings! \n"
				+ "The test strings are: \n" + src + "\n" + tar + "\n"); 
		
		System.out.println("Minimum window substring: " + minWinSubstr(src, tar));
		
		System.out.println("All rabbits gone.");
	}
}
