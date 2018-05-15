package string.removeDuplicates;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;

/**
 * Given a string containing just the characters '(' and ')', find the 
 * length of the longest valid (well-formed) parentheses substring.
 * 
 * E.g.
 * Input: ")()())"
 * Output: 4
 * Explanation: The longest valid parentheses substring is "()()"
 * 
 * @author ruifengm
 * @since 2018-May-12
 */
public class LongestValidParentheses {
	
	/**
	 * Use the stack data structure
	 */
	public static int stackGetLongestValidParenLen(String s) {
		ArrayList<Boolean> list = new ArrayList<>(); // boolean value list indicating parenthesis match/unmatch in order
		Stack<Integer> stack = new Stack<>(); // storing indices of '(' in the list
		for (char c: s.toCharArray()) {
			if (c=='(') {
				list.add(new Boolean(false)); // '(' not matched
				stack.push(list.size()-1); // current index of '(' to be matched
			}
			if (c==')') {
				if (!stack.isEmpty()) { // able to match
					list.set(stack.pop(), new Boolean(true)); // '(' matched
					list.add(new Boolean(true)); // ')' matched
				} else list.add(new Boolean(false)); // ')' not matched
			}
			//System.out.println(list.toString());
		}
		// Iterate through the list to look for longest continuous 'true' sequence
		int max = 0; 
		int count = 0; 
		for (int i=0; i<list.size(); i++) {
			if (list.get(i).equals(true)) count++;
			else {
				if (count > max) max = count;
				count = 0; 
			}
		}
		if (count > max) max = count;
		return max;
	}
	
	/**
	 * Use a DP lookup array.
	 * Check for patterns like '()()' and '(())' and accumulate the lengths accordingly. 
	 * 
	 * E.g. for string                  "(  )  (  )  (  (  )  )"
	 *      the DP lookup array is      [0, 2, 0, 4, 0, 0, 2, 8]
	 */
	private static int DPGetLongestValidParenLen(String s) {
		int[] dp = new int[s.length()];
		char[] sArr = s.toCharArray(); 
		int maxCount = 0; 
		for (int i=1; i<sArr.length; i++) {
			if (sArr[i] == ')') {
				if (sArr[i-1] == '(') dp[i] = 2 + (i>1 ? dp[i-2] : 0); // check for matches like '()()'
				else if (i>=dp[i-1]+1 && sArr[i-dp[i-1]-1] == '(')     // check for matches like '(())'
					dp[i] = dp[i-1] + 2 + (i>=dp[i-1]+2 ? dp[i-dp[i-1]-2] : 0); 
				maxCount = Math.max(maxCount, dp[i]);
			}
		}
		//System.out.println(Arrays.toString(dp));
		return maxCount;
	}
	
	public static void main(String[] args) {
//		String s = "";
//		String s = "((((((";
//		String s = "))))";
//		String s = "((()())";
//		String s = ")()())";
//		String s = "(This(is(a(test)string)to)use)";
//		String s = "())()";
//		String s = "()(()";
//		String s = "()(())";
//		String s = "()()(())";
		String s = "(()()))(()()()())";
		System.out.println("Length of longest valid parentheses substring: " + stackGetLongestValidParenLen(s));
		System.out.println("Length of longest valid parentheses substring: " + DPGetLongestValidParenLen(s));
	}

}
