package dynamicProgramming;

import java.util.ArrayList;
import java.util.Arrays;

import dynamicProgramming.MinimumPalindromeInsertion.PalindromeInsertion.Position;
import utils.FunStringAlgorithm;

/**
 * Given a string, find the minimum characters to be inserted to convert it into a palindrome. 
 * 
 * E.g. given "abc", minimum two insertions are needed to convert it into "abcba". 
 * 
 * @author ruifengm
 * @since 2017-Apr-30
 * 
 * https://www.geeksforgeeks.org/dynamic-programming-set-28-minimum-insertions-to-form-a-palindrome/
 *
 */
public class MinimumPalindromeInsertion extends FunStringAlgorithm {
	
	private static boolean palindromeCheck(String s) {
		int start = 0, end = s.length() - 1;
		char[] charArr = s.toCharArray();
		while (start < end) {
			if (charArr[start] != charArr[end]) return false;
			start++; 
			end--;
		}
		return true;
	}
	
	/**
	 * We try to observe the sub-problem property. 
	 * Let S(n) be a array of n characters, and Sol(S(n), 0..n-1) be the minimum number of characters needed 
	 * to convert it into a palindrome. The recursive pattern would be like
	 * 
	 * 		Sol(S(n), 0..n-1) = Sol(S(n-2), 1..n-2)                                    if S[0] == S[n-1]
	 *                        = MIN(Sol(S(n-1), 0..n-2), Sol(S(n-1), 1..n-1)) + 1      if S[0] != S[n-1]
	 * Conversion is made by either prepending the last element to the front, or appending the first element to the back, whichever yields 
	 * less operations. 
	 * 
	 */
	private static int recursiveMinNumOfCharsForPalin(String s) {
		if (s.length() <= 1) return 0; 
		char[] charArr = s.toCharArray();
		int size = charArr.length;
		if (charArr[0] == charArr[size-1]) return recursiveMinNumOfCharsForPalin(s.substring(1, size-1));
		else return Math.min(recursiveMinNumOfCharsForPalin(s.substring(0, size-1)), recursiveMinNumOfCharsForPalin(s.substring(1, size))) + 1;
	}
	
	/**
	 * Avoid repeated computations with a lookup table constructed via DP memoization.
	 */
	private static int recursiveMinNumOfCharsForPalinDPMemo(char[] charArr, int start, int end, int[][] table) {
		if (table[start][end] != -1) return table[start][end];
		else {
			if (start == end) table[start][end] = 0; 
			else {
				if (charArr[start] == charArr[end]) {
					if ((end-start) == 1) table[start][end] = 0; 
					else table[start][end] = recursiveMinNumOfCharsForPalinDPMemo(charArr, start + 1, end - 1, table);
				}
				else table[start][end] = Math.min(recursiveMinNumOfCharsForPalinDPMemo(charArr, start, end - 1, table), 
						recursiveMinNumOfCharsForPalinDPMemo(charArr, start + 1, end, table)) + 1;
			}
			return table[start][end];
		}
	}
	private static int recursiveMinNumOfCharsForPalinDPMemoDriver(String s) {
		if (s.length() == 0) return 0;
		int[][] DPLookUp = new int[s.length()][s.length()];
		for (int[] row: DPLookUp) Arrays.fill(row, -1);
		return recursiveMinNumOfCharsForPalinDPMemo(s.toCharArray(), 0, s.length() - 1, DPLookUp);
	}
	
	/**
	 * Avoid repeated computations with a lookup table constructed via DP tabulation. 
	 */
	
	private static int iterativeMinNumOfCharsForPalinDPTabu(String s) {
		if (s.length() == 0) return 0;
		int size = s.length(); 
		char[] charArr = s.toCharArray();
		int[][] table = new int[size][size]; // DP lookup table
		// populate the lookup table in a diagonal manner
		for (int j=0; j<=size-1; j++) {
			for (int i=j; i>=0; i--) {
				if (i == j) table[i][j] = 0; 
				else {
					if (charArr[i] == charArr[j]) {
						if ((j-i)==1) table[i][j] = 0; 
						else table[i][j] = table[i+1][j-1];
					}
					else table[i][j] = Math.min(table[i][j-1], table[i+1][j]) + 1; 
				}
			}
		}
		for (int[] row: table) System.out.println(Arrays.toString(row));
		return table[0][size-1];
	}
	
	/**
	 * It's worth nothing that at most only half of the DP lookup table is used.
	 * We try to eliminate the claim of the unused space. 
	 */
	private static int iterativeMinNumOfCharsForPalinDPTabuSpaceOptimized(String s) {
		if (s.length() == 0) return 0;
		int size = s.length(); 
		char[] charArr = s.toCharArray();
		int[][] table = new int[size][]; // DP lookup table
		for (int i=0; i<size; i++) table[i] = new int[size-i];
		// populate the lookup table in a diagonal manner
		for (int j=0; j<=size-1; j++) {
			for (int i=j; i>=0; i--) {
				if (i == j) table[i][size-1-j] = 0; 
				else {
					if (charArr[i] == charArr[j]) {
						if ((j-i)==1) table[i][size-1-j] = 0; 
						else table[i][size-1-j] = table[i+1][size-j];
					}
					else table[i][size-1-j] = Math.min(table[i][size-j], table[i+1][size-1-j]) + 1; 
				}
			}
		}
		for (int[] row: table) System.out.println(Arrays.toString(row));
		return table[0][0];
	}
	
	/**
	 * Use a customized data object to get palindromization steps. 
	 */
	static class PalindromeInsertion {
		public enum Position { BEFORE, AFTER };
		char value; 
		int location; // insert location with respect to the original string
		Position pos;
		public PalindromeInsertion(char value, int location, Position pos) {
			this.value = value;
			this.location = location;
			this.pos = pos;
		}
		public String toString() {
			return "Insert " + this.value + " " + pos + " index " + this.location + "."; 
		}
	}
	@SuppressWarnings("unchecked")
	private static String palindromizeStrDPTabu(String s) {
		if (s.length() == 0) return "";
		int size = s.length(); 
		char[] charArr = s.toCharArray();
		ArrayList<PalindromeInsertion>[][] table = new ArrayList[size][size]; // DP lookup table
		for (int i=0; i<table.length; i++) 
			for (int j=i; j<table.length; j++) table[i][j] = new ArrayList<PalindromeInsertion>();
		// populate the lookup table in a diagonal manner
		for (int j=0; j<=size-1; j++) {
			for (int i=j; i>=0; i--) {
				if (i != j) {
					if (charArr[i] == charArr[j]) {
						if ((j-i)==1) table[i][j] = new ArrayList<PalindromeInsertion>();
						else table[i][j] = table[i+1][j-1];
					}
					else {
						if (table[i][j-1].size() < table[i+1][j].size()) { // insert the j'th element before the i'th element
							table[i][j].addAll(table[i][j-1]); 
							table[i][j].add(new PalindromeInsertion(charArr[j], i, Position.BEFORE));
						} else { // insert the i'th element after the j'th element
							table[i][j].addAll(table[i+1][j]); 
							table[i][j].add(new PalindromeInsertion(charArr[i], j, Position.AFTER));
						}
					}
				}
			}
		}
		// generate palindrome string with the help of ADTs
		class FixedHeadLinkedList { // Method-local inner class
			class Node {
				char val; 
				Node prev, next; 
				public Node(char val) {
					this.val = val; 
					prev = next = null; 
				}
			}
			Node head; 
			public FixedHeadLinkedList() { this.head = null; }
			public void setHead(char val) { this.head = new Node(val); }
			public void insertBeforeHead(char val) {
				Node node = new Node(val);
				if (head.prev != null) head.prev.next = node;
				node.prev = head.prev;
				node.next = head;
				head.prev = node;
			}
			public void insertAfterHead(char val) {
				Node node = new Node(val);
				if (head.next != null) head.next.prev = node;
				node.next = head.next;
				node.prev = head;
				head.next = node;
			}
			public String toString() {
				String s = ""; 
				Node node = head; 
				while (node.prev != null) node = node.prev;
				while (node != null) {
					s += node.val; 
					node = node.next;
				}
				return s; 
			}
		}
		ArrayList<FixedHeadLinkedList> palindrome = new ArrayList<FixedHeadLinkedList>(); 
		for (int i=0; i<size; i++) {
			FixedHeadLinkedList item = new FixedHeadLinkedList();
			item.setHead(charArr[i]);
			palindrome.add(item);
		}
		for (int i=table[0][size-1].size()-1; i>=0; i--) {
			PalindromeInsertion item = table[0][size-1].get(i);
			if (item.pos == Position.BEFORE)
				palindrome.get(item.location).insertBeforeHead(item.value);
			if (item.pos == Position.AFTER)
				palindrome.get(item.location).insertAfterHead(item.value);
		}
		String palinStr = ""; 
		for (FixedHeadLinkedList list: palindrome) palinStr += list.toString();
		
		// generate palindrome string plainly
		String res = s;
		for (int i=table[0][size-1].size()-1; i>=0; i--) {
			PalindromeInsertion item = table[0][size-1].get(i);
			//System.out.println(item.toString());
			if (item.pos == Position.BEFORE)
				res = res.substring(0, item.location) + item.value + res.substring(item.location, res.length());
			if (item.pos == Position.AFTER)
				res = res.substring(0, item.location + 1) + item.value + res.substring(item.location + 1, res.length());
			// refresh location values in the palindrome insertion list
			for (int j=0; j<i; j++) {
				PalindromeInsertion pi = table[0][size-1].get(j); 
				if (item.pos == Position.BEFORE) if (pi.location >= item.location) pi.location += 1;
				if (item.pos == Position.AFTER) if (pi.location > item.location) pi.location += 1;
			}
		}
		//System.out.println(res);
		
		return palinStr;
	}
	
	public static void main(String[] args) {
		String str = "abcdcbefg";
		//String str = "";
		//String str = "a";
		//String str = "aabbccc";
		//String str = "12343561";
		//String str = genRanStr(24);
		System.out.println("Welcome to the rabbit hole to palindromize strings! \n"
				+ "The test string is: \n" + str + "\n");
		System.out.println("Palindrome check: " + palindromeCheck(str) + "\n");
		
		try {
			runStringFuncAndCalculateTime("[Recursive][Exponential]                "
					+ "The minimum number of char insertions needed to palindrome: ", (String s) -> recursiveMinNumOfCharsForPalin(s), str);
			runStringFuncAndCalculateTime("[Recursive][DP Memo]                    "
					+ "The minimum number of char insertions needed to palindrome: ", (String s) -> recursiveMinNumOfCharsForPalinDPMemoDriver(s), str);
			runStringFuncAndCalculateTime("[Recursive][DP Tabu]                     "
					+ "The minimum number of char insertions needed to palindrome: ", (String s) -> iterativeMinNumOfCharsForPalinDPTabu(s), str);
			runStringFuncAndCalculateTime("[Recursive][DP Tabu][Half Lookup Table]  "
					+ "The minimum number of char insertions needed to palindrome: ", (String s) -> iterativeMinNumOfCharsForPalinDPTabuSpaceOptimized(s), str);
			String palindromizedStr = palindromizeStrDPTabu(str);
			System.out.println("After minimum palindromization, the string is: \n" + palindromizedStr + "\n");
			System.out.println("Palindrome check: " + palindromeCheck(palindromizedStr) + "\n");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("All rabbits gone.");
	}

}
