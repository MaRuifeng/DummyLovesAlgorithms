package dynamicProgramming;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import org.junit.Test;
import utils.FunStringAlgorithm;

/**
 * Given an input string and a dictionary of words, determine if the string can be segmented 
 * into a space separated sequence of words that can be found in the dictionary. 
 * 
 * Assume that words in the dictionary are all in lower case. 
 * 
 * E.g. for input string "writeabug" and dictionary {"code", "compile", "write", "test", "a", "run", "bug"}, 
 *      such a sequence can be found as "write a bug". 
 *      
 * The solution to this problem is analogous to the neater solution of the rod cutting problem. 
 *      
 * @author ruifengm
 * @since 2018-May-27
 * 
 * https://www.geeksforgeeks.org/dynamic-programming-set-32-word-break-problem/
 *
 */

public class StringResolver extends FunStringAlgorithm {
	
	private static Random random = new Random(); 
	private static boolean randBool() { return random.nextBoolean(); }
	private static HashSet<String> dictionary;
	static {
		// take note of the word-in-word patterns
		dictionary = new HashSet<>();
		dictionary.add("compile"); 
		dictionary.add("com"); 
		dictionary.add("pile"); 
		dictionary.add("a");
		dictionary.add("recursion");
		dictionary.add("recur"); 
		dictionary.add("java");
		dictionary.add("program");
		dictionary.add("pro"); 
		dictionary.add("gram"); 
		dictionary.add("without");
		dictionary.add("with"); 
		dictionary.add("out");
		dictionary.add("bug"); 
		dictionary.add("in");
		dictionary.add("thinkpad");
		dictionary.add("think");
		dictionary.add("pad");
		dictionary.add("and");
		dictionary.add("that");
		dictionary.add("compiles");
		dictionary.add("s");
	}
	
	/**
	 * The problem can be solved in a similar recursive way as the rod cutting problem. 
	 * Let S(0..n-1) denote an input string of length n, and then a sub-string of it can be of length from 1 to n. 
	 * Let D denote the dictionary and Sol(S(0..n-1)) denote the boolean value indicating whether S(0..n-1) can be successfully resolved into 
	 * dictionary words. 
	 * 
	 *    Sol(S(0..n-1)) = D.contains(S(i..n-1) && Sol(S(0..i-1)), for i ranging from n-1 to 0.
	 */
	private static boolean recursiveCheckRtoL(String str) {
		int size = str.length();
		if (size == 0) return true; 
		for (int i=size-1; i>=0; i--) {
			// if (dictionary.contains(str.substring(i, size).toLowerCase())) System.out.println(str.substring(i, size));
			// if (recursiveCheckRtoL(str.substring(0, i)) && dictionary.contains(str.substring(i, size).toLowerCase()))
			if (dictionary.contains(str.substring(i, size).toLowerCase()) 
					&& recursiveCheckRtoL(str.substring(0, i)))
					return true; 
		}
		return false;
	}
	/**
	 * We can check it from left to right by checking "prefix" of the string first. 
	 * Note how these two solutions behave in the opposite manner in their best/worst case scenarios. 
	 *                        Worst Case                                    Best Case
	 *   R to L        non-dictionay word at string start            non-dictionay word at string end
	 *   L to R        non-dictionay word at string end              non-dictionay word at string start
	 */
	private static boolean recursiveCheckLtoR(String str) {
	    int size = str.length();
	    if (size == 0)  return true;
	    for (int i=1; i<=size; i++) {
	    	// if (dictionary.contains(str.substring(0, i).toLowerCase())) System.out.println(str.substring(0, i));
	        if (dictionary.contains(str.substring(0, i).toLowerCase()) 
	        		&& recursiveCheckLtoR(str.substring(i, size)))
	        	return true;
	    }
	    return false;
	}
	
	/**
	 * We try to "optimize" the above method via DP memoization. 
	 * It's worth noting that the AND condition check in the pure recursive solution 
	 * already eliminates a lot of recursive sub-calls, hence there is virtually no efficiency gain via DP. 
	 */
	private static boolean recursiveCheckRtoLDPMemo(String str, Boolean[] table) {
		int size = str.length();
		if (table[size] != null) {
			// System.out.println(table[size]);
			return table[size].booleanValue();
		}
		else {
			if (size == 0) table[size] = new Boolean(true);
			else {
				table[size] = new Boolean(false); 
				for (int i=size-1; i>=0; i--) {
					if (dictionary.contains(str.substring(i, size).toLowerCase()) 
							&& recursiveCheckRtoLDPMemo(str.substring(0, i),table)) {
						table[size] = new Boolean(true); 
						break;
					}
				}
			}
			return table[size].booleanValue();
		}
	}
	private static boolean recursiveCheckRtoLDPMemo(String str) {
		Boolean[] DPLookUp = new Boolean[str.length()+1];
		boolean res = recursiveCheckRtoLDPMemo(str, DPLookUp);
		// System.out.println(Arrays.toString(DPLookUp));
		return res;
	}
	
	private static boolean recursiveCheckLtoRDPMemo(String str, Boolean[] table) {
		int size = str.length();
		if (table[size] != null) {
			// System.out.println(table[size]);
			return table[size].booleanValue();
		}
		else {
			if (size == 0) table[size] = new Boolean(true);
			else {
				table[size] = new Boolean(false); 
				for (int i=1; i<=size; i++) {
//					if (recursiveCheckLtoRDPMemo(str.substring(i, size),table)  
//							&& dictionary.contains(str.substring(0, i).toLowerCase())) {
					if (dictionary.contains(str.substring(0, i).toLowerCase()) 
							&& recursiveCheckLtoRDPMemo(str.substring(i, size), table)) {
						table[size] = new Boolean(true); 
						break;
					}
				}
			}
			return table[size].booleanValue();
		}
	}
	private static boolean recursiveCheckLtoRDPMemo(String str) {
		Boolean[] DPLookUp = new Boolean[str.length()+1];
		boolean res = recursiveCheckLtoRDPMemo(str, DPLookUp);
		//System.out.println(Arrays.toString(DPLookUp));
		return res;
	}

	/**
	 * We try to "optimize" the above method via DP tabulation.
	 * The DP tabulation method is very much less efficient comparatively because the DP lookup table has 
	 * to be completely filled up, whereas in the top down memoization case, it's mostly
	 * empty. 
	 */
	private static boolean iterativeCheckDPTabu(String str) {
		int size = str.length();
		boolean[] table = new boolean[size+1]; // DP lookup table
		table[0] = true; // base state
		for (int i=1; i<=size; i++) {
			table[i] = false;
			for (int j=i-1; j>=0; j--) {
				// if (dictionary.contains(str.substring(j, i).toLowerCase())) System.out.println(str.substring(j, i));
				if (dictionary.contains(str.substring(j, i).toLowerCase()) && table[j]) {
					table[i] = true;
					break;
				}
			}
		}
		//System.out.println(Arrays.toString(table));
		return table[size];
	}
	
	/**
	 * Now we try to find out all possible partitions of the string that yield dictionary words. 
	 * From left to right of the string, we check string prefixes in the dictionary. There will 
	 * be two cases when we encounter a word: 
	 *    --> include the word in the solution, set the prefix to empty and continue search
	 *    --> don't include the word in the solution, keep the prefix and continue search
	 * 
	 * We can develop a recursive solution based on above sub-problem patterns found. 
	 * Look at the subset seeker problem and the math operations for fixed sum problem for a similar approach. 
	 */
	private static void recursivePrintAllResolvablePartitions(String solution, String input, String prefix, int remainStrLen) {
		if (remainStrLen==0) System.out.println(solution.trim()); 
		else {
			int size = input.length(); 
			for (int i=1; i<=size; i++) {
				prefix += input.charAt(i-1); 
				if (dictionary.contains(prefix.toLowerCase())) {
					// include the found word in solution
					recursivePrintAllResolvablePartitions(solution + " " + prefix, input.substring(i, size), "", remainStrLen-i);
					// don't include the found word in solution
					recursivePrintAllResolvablePartitions(solution, input.substring(i, size), prefix, remainStrLen);
				}
			}
		}
	}
	private static void recursivePrintAllResolvablePartitions(String str) {
		recursivePrintAllResolvablePartitions("", str, "", str.length());
	}
	private static int recursiveCountAllResolvablePartitions(String input, String prefix, int remainStrLen) {
		if (remainStrLen==0) return 1;
		else {
			int size = input.length(); 
			int count = 0;
			for (int i=1; i<=size; i++) {
				prefix += input.charAt(i-1);
				if (dictionary.contains(prefix.toLowerCase())) {
					// include the found word in solution
					count += recursiveCountAllResolvablePartitions(input.substring(i, size), "", remainStrLen-i) 
					// don't include the found word in solution
							+ recursiveCountAllResolvablePartitions(input.substring(i, size), prefix, remainStrLen);
				}
			}
			return count;
		}
	}
	private static int recursiveCountAllResolvablePartitions(String str) {
		return recursiveCountAllResolvablePartitions(str, "", str.length());
	}
	
	/* Test */
	static class TestData {
		String str;
		int wordCount;
		boolean resolvable;
		String description;
		public TestData(String str, boolean resolvable, int wordCount, String description) {
			this.str = str;
			this.resolvable = resolvable;
			this.wordCount = wordCount;
			this.description = description;
		}
	}
	
	@Test
	public static void test(TestData data) {
		try {
			System.out.println("\n/******** TEST START ********/\n");
			System.out.println("The input string consists of " + data.wordCount + " words and has a length of " + data.str.length() + "."); 
			System.out.println("Test Scenario: " + data.description + "\n");
			boolean result; 
			
			result = runStringFuncAndCalculateTime("[Recursive][Right->Left]           Resolvable? " , 
					(String s) -> recursiveCheckRtoL(s), data.str);
			assertEquals(data.resolvable, result);
			
			result = runStringFuncAndCalculateTime("[Recursive][Right->Left][DP Memo]  Resolvable? " , 
					(String s) -> recursiveCheckRtoLDPMemo(s), data.str);
			assertEquals(data.resolvable, result);
			
			result = runStringFuncAndCalculateTime("[Recursive][Left->Right]           Resolvable? " , 
					(String s) -> recursiveCheckLtoR(s), data.str);
			assertEquals(data.resolvable, result);
			
			result = runStringFuncAndCalculateTime("[Recursive][Left->Right][DP Memo]  Resolvable? " , 
					(String s) -> recursiveCheckLtoRDPMemo(s), data.str);
			assertEquals(data.resolvable, result);
			
			result = runStringFuncAndCalculateTime("[Iterative][DP Tabu]  Resolvable? " , 
					(String s) -> iterativeCheckDPTabu(s), data.str);
			assertEquals(data.resolvable, result);
			System.out.println("\n/******** TEST END ********/\n");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		System.out.println("Welcome to the rabbit hole of string breakers!\n"
				+ "Determine if a given string can be resolved into words found in the dictionary.\n"); 
		
		String dataStr1 = "", dataStr2 = "";
		int base = 50; 
		String perturbation = "troublemaker";
		
		/* Test false */
		int count = base;
		for (int i=0; i<base/2; i++) {
			dataStr1 += dictionary.toArray()[ThreadLocalRandom.current().nextInt(0, dictionary.size()-1)];
			if (randBool()) {
				dataStr1 += perturbation;
				count += 2;
			}
		}
		TestData data1 = new TestData(dataStr1, false, count, "Non-dictionary words are randomly scattered.");
		test(data1);
		
		/* Test true */
		for (int i=0; i<base; i++) dataStr2 += dictionary.toArray()[ThreadLocalRandom.current().nextInt(0, dictionary.size()-1)];
		TestData data2 = new TestData(dataStr2, true, base, "All words are in dictionary.");
		test(data2);
		
		/* Test string with non-dictionary words at head */
		TestData data3 = new TestData("prefix" + dataStr2, false, base + 1, "Single non-dictionary word at string head."); // 'prefix' not in dictionary
		test(data3);
		
		/* Test string with non-dictionary words at end */
		TestData data4 = new TestData(dataStr2 + "suffix", false, base + 1, "Single non-dictionary word at string end."); // 'suffix' not in dictionary
		test(data4);
		
		/* Test string with words contained in words */
		String str = "compilearecursionjavaprogramwithoutbugandarecursionjavaprogramwithbuginacompilethinkpadthatcompilesrecursionjavaprogram"; 
		TestData data5 = new TestData(str, true, 23, "Word contains other words."); 
		test(data5);
		System.out.println("List all possible string partitions that are resolvable to dictionary words.\n");
		try {
			runStringFuncAndCalculateTime("[Recursive]  Number of possible partitions: ", 
					(String s) -> recursiveCountAllResolvablePartitions(s), str);
			runStringFuncAndCalculateTime("[Recursive]  All possible partitions: ", 
					(String s) -> recursivePrintAllResolvablePartitions(s), str);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("All rabbits gone.");
	}

}
