package dynamicProgramming;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import utils.FunStringAlgorithm;

/**
 * Given a text and a wildcard pattern, determine if the entire text matches the pattern. 
 * 
 * The wildcard characters are 
 *     ? - matches any single character
 *     * - matches any character sequence (including the empty sequence) 
 *     
 * E.g. text 'ababc' matches '**b*a?c'. 
 * 
 * @author ruifengm
 * @since 2018-May-2
 *
 */
public class WildcardMatching extends FunStringAlgorithm {
	
	/**
	 * We try to observe the sub-problems. 
	 * Let T(n) be the text string of n characters, and P(m) be the pattern string of m characters. Let Sol(T(n), P(m)) be the 
	 * boolean result indicating whether T matches P. 
	 *     if P[m-1] == '?'
	 *         Sol(T(n), P(m)) = Sol(T(n-1), P(m-1))         --> T[n-1] is ignored
	 *     else if P[m-1] == '*'
	 *         Sol(T(n), P(m)) = Sol(T(n), P(m-1)) ||        --> P[m-1] matches an empty sequence
	 *                           Sol(T(n-1), P(m-1)) ||      --> P[m-1] matches T[n-1]
	 *                           Sol(T(n-2), P(m-1)) ||      --> P[m-1] matches T[n-2]..T[n-1]
	 *                           ...
	 *                           Sol(T(0), P(m-1))           --> P[m-1] matches T[0]..T[n-1]
	 *     else     i.e. P[m-1] != '?' && P[m-1] != '*'
	 *         Sol(T(n), P(m)) = Sol(T(n-1), P(m-1))         --> P[m-1] == T[m-1]
	 *                         = FALSE                       --> p[m-1] != T[m-1]
	 *                         
	 * Note that the case of P[m-1] == '*' can also be formulated as below
	 *         Sol(T(n), P(m)) = Sol(T(n), P(m-1)) ||        --> do not let P[m-1] match anything, hence text remains
	 *                           Sol(T(n-1), P(m))           --> P[m-1] matches T[n-1], hence text is reduced with 1 char, but pattern remains as * can match many 
	 */
	private static boolean recursiveCheckWildcardMatch(char[] t, char[] p, int tL, int pL) {
		if (tL == 0) {
			for (int i=0; i<pL; i++)
				if (p[i] != '*') return false; // check non-wildcard chars in pattern
			return true; 
		}
		else if (pL == 0) return false; // pattern depleted
		else {
			if (p[pL-1] == '?') return recursiveCheckWildcardMatch(t, p, tL-1, pL-1);
			else if (p[pL-1] == '*') {
				boolean res = false;
				for (int i=tL; i>=0; i--) {
					res = recursiveCheckWildcardMatch(t, p, i, pL-1); 
					if (res == true) break;
				}
              return res;				
//			  return recursiveCheckWildcardMatch(t, p, tL, pL-1) || recursiveCheckWildcardMatch(t, p, tL-1, pL);
			} else {
				if (t[tL-1] == p[pL-1]) return recursiveCheckWildcardMatch(t, p, tL-1, pL-1);
				else return false; 
			}
		}
	}
	private static boolean recursiveCheckWildcardMatchDriver(String t, String p) {
		//p = preProcessPattern(p);
		return recursiveCheckWildcardMatch(t.toCharArray(), p.toCharArray(), t.length(), p.length());
	}
	
	/**  
	 * Avoid repeated computations with a DP lookup table constructed via memoization.
	 */
	private static boolean recursiveCheckWildcardMatchDPMemo(char[] t, char[] p, int tL, int pL, Boolean[][] table) {
		if (table[tL][pL] != null) return table[tL][pL].booleanValue();
		else {
			if (tL == 0) {
				table[tL][pL] = new Boolean(true);
				for (int i=0; i<pL; i++)
					if (p[i] != '*') {
						table[tL][pL] = new Boolean(false); // check non-wildcard chars left in pattern
						break;
					}	
			}
			else if (pL == 0) table[tL][pL] = new Boolean(false); // pattern depleted
			else {
				if (p[pL-1] == '?') table[tL][pL] = recursiveCheckWildcardMatchDPMemo(t, p, tL-1, pL-1, table);
				else if (p[pL-1] == '*') {
					boolean res = false;
					for (int i=tL; i>=0; i--) {
						res = recursiveCheckWildcardMatchDPMemo(t, p, i, pL-1, table); 
						if (res == true) break;
					}
			      table[tL][pL] = new Boolean(res);				
//				  table[tL][pL] = recursiveCheckWildcardMatchDPMemo(t, p, tL, pL-1, table) || recursiveCheckWildcardMatchDPMemo(t, p, tL-1, pL, table);
				} else {
					if (t[tL-1] == p[pL-1]) table[tL][pL] = recursiveCheckWildcardMatchDPMemo(t, p, tL-1, pL-1, table);
					else table[tL][pL] = new Boolean(false); 
				}
			}
			return table[tL][pL];
		}
	}
	private static boolean recursiveCheckWildcardMatchDPMemoDriver(String t, String p) {
		Boolean[][] DPLookUp = new Boolean[t.length()+1][p.length()+1];
		boolean res =  recursiveCheckWildcardMatchDPMemo(t.toCharArray(), p.toCharArray(), t.length(), p.length(), DPLookUp);
		// for (Boolean[] row: DPLookUp) System.out.println(Arrays.toString(row));
		return res;
	}
	
	/**  
	 * Avoid repeated computations with a DP lookup table constructed via tabulation.
	 */
	private static boolean iterativeCheckWildcardMatchDPTabu(String t, String p) {
		int tL = t.length(), pL = p.length();
		boolean[][] table = new boolean[tL+1][pL+1]; // DP lookup table
		// base state
		table[0][0] = true;
		int k;
		for (k=0; k<pL; k++) 
			if (p.charAt(k) != '*') break; 
		for (int j=1; j<=pL; j++) {
			if (j<k+1) table[0][j] = true;
			else table[0][j] = false; 
		}
		for (int i=1; i<=tL; i++) table[i][0] = false;
		// proliferation
		for (int i=1; i<=tL; i++) {
			for (int j=1; j<=pL; j++) {
				if (p.charAt(j-1) == '?') table[i][j] = table[i-1][j-1];
				else if (p.charAt(j-1) == '*') table[i][j] = table[i][j-1] || table[i-1][j];
				else {
					if (p.charAt(j-1) == t.charAt(i-1)) table[i][j] = table[i-1][j-1];
					else table[i][j] = false;
				}
			}
		}
		// for (boolean[] row: table) System.out.println(Arrays.toString(row));
		return table[tL][pL];
	}
	
	/**  
	 * We can observed from the tabulation method that we don't need a 2-D array to store the 
	 * intermediary results. We can use a 1-D array and two temp variables to replace it. 
	 */
	private static boolean iterativeCheckWildcardMatchDPTabuSpaceOptimized(String t, String p) {
		int tL = t.length(), pL = p.length();
		boolean[] table = new boolean[pL+1]; // DP lookup table
		// base state
		table[0] = true;
		int k;
		for (k=0; k<pL; k++) 
			if (p.charAt(k) != '*') break; 
		for (int j=1; j<=pL; j++) {
			if (j<k+1) table[j] = true;
			else table[j] = false; 
		}
		boolean pre, cur;
		// proliferation
		for (int i=1; i<=tL; i++) {
			cur = false;
			for (int j=1; j<=pL; j++) {
				pre = cur;
				if (p.charAt(j-1) == '?') cur = table[j-1];
				else if (p.charAt(j-1) == '*') cur = table[j] || pre;
				else {
					if (p.charAt(j-1) == t.charAt(i-1)) cur = table[j-1];
					else cur = false;
				}
				table[j-1] = pre;
				pre = cur;
			}
			table[pL] = cur;
		}
		//for (boolean[] row: table) System.out.println(Arrays.toString(row));
		return table[pL];
	}
	
	/**
	 * Since '*' matches a sequence, consecutive wildcard character sequence including '*' like 
	 * '***', '?*' and '*?' have the same effect as '*' does and hence can all be reduced to '*'. 
	 */
	private static String preProcessPattern(String p) { 
		String[] strList = p.split("\\*", -1); 
		if (strList.length == 1) return p;
		strList[0] = removeTrailingChars(strList[0], '?'); // remove trailing ?'s from the first sequence
		strList[strList.length-1] = removeLeadingChars(strList[strList.length-1], '?'); // remove leading ?'s from the last sequence
		String res = strList[0] + '*';
		for (int i=1; i<=strList.length-2; i++) {
			strList[i] = removeTrailingChars(strList[i], '?');
			strList[i] = removeLeadingChars(strList[i], '?');
			if (!strList[i].isEmpty()) res = res + strList[i] + '*';
		}
		res = res + strList[strList.length-1];
		return res; 
	}
	private static String removeLeadingChars(String s, char c) {
		int start = 0; 
		while (start <= s.length() - 1 && s.charAt(start) == c) start++; 
		return s.substring(start, s.length());
	}
	private static String removeTrailingChars(String s, char c) {
		int end = s.length() - 1; 
		while (end >= 0 && s.charAt(end) == c) end--; 
		return s.substring(0, end+1);
	}
	
	
	@FunctionalInterface
	protected interface DoubleCharArrayToBooleanFunction {
		boolean apply(String a, String b);
	}
	
    protected static void runStringFuncAndCalculateTime(String message, DoubleCharArrayToBooleanFunction strFunc, String a, String b) throws Exception {
    	long startTime = System.nanoTime();
    	System.out.printf("%s%s\n", message, strFunc.apply(a, b));
    	long endTime = System.nanoTime();
    	long totalTime = new Long(TimeUnit.MICROSECONDS.convert(endTime - startTime, TimeUnit.NANOSECONDS));
    	DecimalFormat formatter = new DecimalFormat("#,###");
    	System.out.printf("%-70s%s\n\n", "Function execution time in micro-seconds: ", formatter.format(totalTime));
    }
    
	public static void main(String[] args) {
		String text = "abbabbabbabbabbabbabbabbabbabbabbabbabbabbabbabbabbabb"; 
		String pattern = "***abbc*******";
		// String text = "baaabab";
        // String pattern = "*****ba*****ab";
        // String pattern = "ba*****ab";
        // String pattern = "ba*ab";
        // String pattern = "a*ab";
        // String pattern = "a*****ab";
        // String pattern = "*a*****ab";
        // String pattern = "ba*ab****";
        // String pattern = "****";
        // String pattern = "*";
        // String pattern = "aa?ab";
        // String pattern = "b*b";
        // String pattern = "a*a";
        // String pattern = "baaabab";
        // String pattern = "?baaabab";
        // String pattern = "baaabab?";
        // String pattern = "*baaaba*";
		//String pattern = "**?ab***cd*?*e?*?f*??*?g??";
		//String pattern = "?a?*b***c*?d*?*e?f*?g*??*?h??*";
		
		System.out.println("Welcome to the rabbit hole of wildcard matches! \n"
				+ "The text string is: \n" + text 
				+ "\nThe pattern string is: \n" + pattern + "\n"); 
		
		try {
			runStringFuncAndCalculateTime("[Recursive]              Matched: ", (String a, String b) -> recursiveCheckWildcardMatchDriver(a, b), text, pattern);
			runStringFuncAndCalculateTime("[Recursive][DP Memo]     Matched: ", (String a, String b) -> recursiveCheckWildcardMatchDPMemoDriver(a, b), text, pattern);
			runStringFuncAndCalculateTime("[Iterative][DP Tabu]     Matched: ", (String a, String b) -> iterativeCheckWildcardMatchDPTabu(a, b), text, pattern);
			runStringFuncAndCalculateTime("[Iterative][DP Tabu]     Matched: ", (String a, String b) -> iterativeCheckWildcardMatchDPTabuSpaceOptimized(a, b), text, pattern);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("All rabbits gone.");
	}

}
