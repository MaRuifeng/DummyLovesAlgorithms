package dynamicProgramming;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import utils.FunIntAlgorithm;

/**
 * Given a number-to-alphabet map "1->A, 2->B, ... , 26->Z", and a digit sequence (e.g. 122314561),
 * 
 * 1) Count the number of possible decodings of the given digit sequence
 * 2) Print out all such decodings
 * 
 * https://www.geeksforgeeks.org/count-possible-decodings-given-digit-sequence/
 * 
 * For example, for digit sequence 12321, the possible decodings are
 * "ABCBA", "ABCU", "AWBA", "AWU", "LCBA", "LCU"
 * and the count is 6.  
 * 
 * @author ruifengm
 *
 */

public class DigitSequenceDecoder extends FunIntAlgorithm {
	
	private static final Map<Integer, Character> codebook; // a simple array can be used, but we use a HashMap for practice purpose
	static { // static initializer
		codebook = new HashMap<Integer, Character>();
		for (int i=1; i<=26; i++) {
			codebook.put(new Integer(i), new Character((char)(i+64)));
		}
	}
	
	/**
	 * In the codebook, an alphabet is represented with either a single digit or double digits. 
	 * Let A be a digit array of size n, denoted as A(n), and let Sol(A(n)) denote its total decoding count, then we can deduce that 
	 *       if the last two digits of A(n) forms a valid code
	 *          if A[n-1] != 0 AND A[n-2] != 0
	 *       		Sol(A(n)) = Sol(A(n-1) + Sol(A(n-2))    -> append alphabets represented by A[n-1] and A[n-2]A[n-1]
	 *          else if A[n-1] == 0
	 *              Sol(A(n)) = Sol(A(n-2))                 -> append alphabet represented by A[n-2]A[n-1] 
	 *          else if A[n-2] == 0 
	 *              Sol(A(n)) = 0                           -> not able to append anything, decoding process breaks
	 *       else 
	 *       	if A[n-1] == 0
	 *              Sol(A(n)) = 0                           -> not able to append anything, decoding process breaks  
	 *          else                                           
	 *              Sol(A(n)) = Sol(A(n-1)                  -> append alphabet represented by A[n-1]
	 */
	private static long recursiveCountDecodings(int[] digitSeq, int size) {
		if (digitSeq == null) return 0;
		if (size == 0) return 1; // no digit given, considered as 1 decoding
		if (size == 1) return digitSeq[0] == 0 ? 0 : 1;
		Integer code = new Integer(digitSeq[size-2] * 10 + digitSeq[size-1]);
		if (codebook.containsKey(code)) { 
			if (digitSeq[size-1] != 0 && digitSeq[size-2] != 0) return recursiveCountDecodings(digitSeq, size - 1) + recursiveCountDecodings(digitSeq, size - 2);
			else if (digitSeq[size-1] == 0) return recursiveCountDecodings(digitSeq, size - 2);
			else return 0; // standing alone zero stops the decoding process
		}
		else  return digitSeq[size-1] != 0 ? recursiveCountDecodings(digitSeq, size - 1) : 0;
	}
	
	/**
	 * Use DP to reduce the time complexity to linear with memoization.
	 */
	private static long recursiveCountDecodingsDPMemo(int[] digitSeq, int size, long[] table) {
		if (table[size] != Long.MIN_VALUE) return table[size];
		else {
			if (size == 0) table[0] = 1;
			if (size == 1) table[1] = digitSeq[0] == 0 ? 0 : 1;
			Integer code = new Integer(digitSeq[size-2] * 10 + digitSeq[size-1]);
			if (codebook.containsKey(code)) { 
				if (digitSeq[size-1] != 0 && digitSeq[size-2] != 0) table[size] = recursiveCountDecodings(digitSeq, size - 1) + recursiveCountDecodings(digitSeq, size - 2);
				else if (digitSeq[size-1] == 0) table[size] = recursiveCountDecodings(digitSeq, size - 2);
				else table[size] = 0; // standing alone zero stops the decoding process
			}
			else table[size] = digitSeq[size-1] != 0 ? recursiveCountDecodings(digitSeq, size - 1) : 0;
			return table[size];
		}
	}
	private static long recursiveCountDecodingsDPMemoDriver(int[] digitSeq, int size) {
		if (digitSeq == null) return 0;
		long[] DPLookUp = new long[size+1]; // catering for empty digit sequence
		Arrays.fill(DPLookUp, Long.MIN_VALUE);
		return recursiveCountDecodingsDPMemo(digitSeq, size, DPLookUp);
	}
	
	/**
	 * Use DP to reduce the time complexity to linear with tabulation.
	 */
	private static long iterativeCountDecodingsDPTabu(int[] digitSeq, long[] table) {
		table[0] = 1;
		table[1] = digitSeq[0] == 0 ? 0 : 1;
		
		for (int i=2; i<=digitSeq.length; i++) {
			Integer code = new Integer(digitSeq[i-2] * 10 + digitSeq[i-1]);
			if (codebook.containsKey(code)) { 
				if (digitSeq[i-1] != 0 && digitSeq[i-2] != 0) table[i] = table[i-1] + table[i-2];
				else if (digitSeq[i-1] == 0) table[i] = table[i-2];
				else table[i] = 0; // standing alone zero stops the decoding process
			}
			else table[i] = digitSeq[i-1] != 0 ? table[i-1] : 0;
		}
	    return table[digitSeq.length];
	}
	private static long iterativeCountDecodingsDPTabuDriver(int[] digitSeq) {
		if (digitSeq == null) return 0;
		long[] DPLookUp = new long[digitSeq.length+1]; // catering for empty digit sequence
		return iterativeCountDecodingsDPTabu(digitSeq, DPLookUp);
	}
	
	/**
	 * Find the possible decodings using DP and store them to an ArrayList.
	 */
	private static ArrayList<String> iterativeFindDecodingsDP(int[] digitSeq) {
		ArrayList<String> pre2 = new ArrayList<String>();
		ArrayList<String> pre1 = new ArrayList<String>();
		ArrayList<String> cur = new ArrayList<String>();
		
		// starting state
		pre2.add("");
		if (digitSeq[0] != 0) pre1.add(String.valueOf(codebook.get(digitSeq[0])));
		// proliferation
		for (int i=1; i<digitSeq.length; i++) {
			cur = new ArrayList<String>();
			Integer code = new Integer(digitSeq[i-1] * 10 + digitSeq[i]);
			if (codebook.containsKey(code)) {
				if (digitSeq[i] != 0 && digitSeq[i-1] != 0) {
					for (String s: pre2) cur.add(s + String.valueOf(codebook.get(code)));
					for (String s: pre1) cur.add(s + String.valueOf(codebook.get(digitSeq[i])));
				}
				else if (digitSeq[i] == 0) for (String s: pre2) cur.add(s + String.valueOf(codebook.get(code)));
			}
			else {
				if (digitSeq[i] != 0) for (String s: pre1) cur.add(s + String.valueOf(codebook.get(digitSeq[i])));
			}
			pre2 = pre1; 
			pre1 = cur;
		}
		return cur;
	}
	

	public static void main(String[] args) {
		//int[] digitSeq = {1, 2, 3, 2, 1};
		//int[] digitSeq = {9, 1, 1, 3, 8, 5, 1, 8, 5}; // encoded sequence for "I AM HERE" without spaces
		int[] digitSeq = genRanIntArr(15, 0, 9);
		System.out.println("Welcome to the rabbit of digit sequence decodings!\n"
				+ "The digit sequence is "); 
		for(int d: digitSeq) System.out.print(d);
		System.out.println("\nAnd the code book is ");
		System.out.println(codebook.toString() + "\n");
		
		
		try {
			runIntArrayFuncAndCalculateTime("[Recursive][Exponential]     Number of possible decodings: " , (int[] a, int s) -> recursiveCountDecodings(a, s), digitSeq, digitSeq.length);
			runIntArrayFuncAndCalculateTime("[Recursive][Linear]          Number of possible decodings: " , (int[] a, int s) -> recursiveCountDecodingsDPMemoDriver(a, s), digitSeq, digitSeq.length);
			runIntArrayFuncAndCalculateTime("[Iterative][Linear]          Number of possible decodings: " , (int[] a) -> iterativeCountDecodingsDPTabuDriver(a), digitSeq);
			runIntArrayFuncAndCalculateTime("[Iterative][Linear]          List of possible decodings: " , (int[] a) -> iterativeFindDecodingsDP(a), digitSeq);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("All rabbits gone.");
	}


}
