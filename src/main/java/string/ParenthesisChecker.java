package string;

import java.util.HashMap;
import java.util.Stack;

import utils.FunStringAlgorithm;

/**
 * Check whether parentheses are properly matched in a given String.
 * 
 * We can assume the parentheses are '()', '{}', '[]' and '<>' only. 
 * 
 * This is useful is code lint tools. 
 * 
 * @author ruifengm
 * @since 2018-May-11
 *
 * YiTu tech interview round 2
 */
public class ParenthesisChecker extends FunStringAlgorithm {
	
	/**
	 * A very typical use case of the stack data structure
	 */
	public static boolean checkParenMatched(String s) {
		// Store all parenthesis matches into a hashmap
		HashMap<Character, Character> hash = new HashMap<>();
		hash.put(')', '('); 
		hash.put(']', '[');
		hash.put('}', '{');
		hash.put('>', '<');
		// Check
		Stack<Character> stack = new Stack<>(); 
		for (char c: s.toCharArray()) {
			Character cObj = new Character(c);
			if (hash.values().contains(cObj)) stack.push(cObj);
			if (hash.keySet().contains(cObj)) {
				if(!stack.isEmpty() && stack.pop().equals(hash.get(cObj)));
				else return false;
			}
		}
		if (stack.isEmpty()) return true;
		else return false;
	}

	public static void main(String[] args) {
//		String testStr = "";
//		String testStr = ")]}";
//		String testStr = "()]}";
//		String testStr = ")]{}";
//		String testStr = "{[";
//		String testStr = "()";
		String testStr = "{this[is(a<test>)]}";
		System.out.println("Welcome to the rabbit hole of parenthesis checkers! \n"
				+ "The test string is: \n" + testStr+ "\n"); 
		
		System.out.println("Parentheses matched? " + checkParenMatched(testStr));
		
		System.out.println("All rabbits gone.");
	}
}
