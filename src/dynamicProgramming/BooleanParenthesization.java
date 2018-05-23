package dynamicProgramming;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/**
 * Given a boolean expression of 'T' and 'F' symbols filled with below logic operators 
 * in between, count number of ways we can parenthesize (prioritize logic operations) the 
 * expression such that the expression evaluates to true. 
 * 
 *     &  --- AND
 *     |  --- OR
 *     ^  --- XOR
 *  
 * E.g. given symbol array of {T, T, F, T}
 *      and operator array of {|, &, ^},
 *      the expression is T | T & F ^ T and there 4 ways for it to evaluate to true. 
 *      ((T|T)&(F^T))
 *      (T|(T&(F^T)))
 *      (((T|T)&F)^T)
 *      (T|((T&F)^T))
 * 
 * Note that the number of possible parenthesizations grows very fast with the size of the input. Hence to 
 * make the program robust, integer value overflow should be taken care of wherever arithmetic operations are involved. 
 * 
 * https://www.geeksforgeeks.org/dynamic-programming-set-37-boolean-parenthesization-problem/
 * Solution submission on GFG: https://ide.geeksforgeeks.org/KfZDE4
 * 
 * @author ruifengm
 * @since 2018-May-5
 *
 */

public class BooleanParenthesization {
	
	private static final int[] BOOL_SYM = {1, 0}; 
	private static final char[] BOOL_OPT = {'|', '&', '^'}; 
	
	private static int[] symArr; 
	private static char[] optArr; 
	
	private static void genSymAndOptArr(int size) {
		symArr = new int[size];
		optArr = new char[size-1]; 
		for (int i=0; i<size-1; i++) {
			symArr[i] = BOOL_SYM[ThreadLocalRandom.current().nextInt(0, BOOL_SYM.length)];
			optArr[i] = BOOL_OPT[ThreadLocalRandom.current().nextInt(0, BOOL_OPT.length)];
		}
		symArr[size-1] = BOOL_SYM[ThreadLocalRandom.current().nextInt(0, BOOL_SYM.length)];
	}
	
	/**
	 * We try to observe the sub-problem pattern. 
	 * Let S(n) be the boolean symbol array, and O(n-1) be the boolean operator array. 
	 * Then we know that the total number of possible parenthesizations 
	 *     = number of parenthesizations that evaluate to true   +   number of parenthesizations that evaluate to false
	 * Let SolT(S(0..n-1), O(0..n-2)) be the count of parenthesizations which make the expression evaluate to true, 
	 * and SolF(S(0..n-1), O(0..n-2)) to false. 
	 * Since every boolean operator can possibly be the last to be evaluated, SolT(S(0..n-1), O(0..n-2)) is the sum of 
	 * solutions for which each operator is the last to be evaluated.  
	 * Let O[K] be the last operator to be evaluated, then 0 <= K <= n-2. 
	 *     SolT(S(0..n-1), O(0..n-2)) =  
	 *       SUM(  SolT(S(0..K), O(0..K)) * SolT(S(K+1..n-1), O(K+1..n-2))                                                                         
	 *                                   --> when O[K] == '&'
	 *     		   SolT(S(0..K), O(0..K)) * SolF(S(K+1..n-1), O(K+1..n-2)) + SolF(S(0..K), O(0..K)) * SolT(S(K+1..n-1), O(K+1..n-2)) 
	 *                                   --> when O[K] == '^'
	 *             SolT(S(0..K), O(0..K)) * SolT(S(K+1..n-1), O(K+1..n-2) + SolT(S(0..K), O(0..K)) * SolF(S(K+1..n-1), O(K+1..n-2)) 
	 *                  + SolF(S(0..K), O(0..K)) * SolT(S(K+1..n-1), O(K+1..n-2)
	 *                                   --> when O[K] == '|'
	 *                                                                        )
	 * For simplicity, we can represent the symbols using 1 and 0 so as to utilize bit-wise operators. 
	 */
	private static long recursiveCountTrue(int[] symArr, char[] optArr, int start, int end) {
		if (start > end) return symArr[start];
		long sol = 0; 
		for (int i=start; i<=end; i++) {
			long leftTrue = recursiveCountTrue(symArr, optArr, start, i-1); 
			long rightTrue = recursiveCountTrue(symArr, optArr, i+1, end);
			long leftFalse = recursiveCountFalse(symArr, optArr, start, i-1); 
			long rightFalse = recursiveCountFalse(symArr, optArr, i+1, end);
			if (optArr[i] == '&') sol += leftTrue * rightTrue;
			if (optArr[i] == '^') sol += leftTrue * rightFalse + rightTrue * leftFalse;
			if (optArr[i] == '|') sol += leftTrue * rightTrue + leftTrue * rightFalse + rightTrue * leftFalse;
		}
		return sol;
	}
	private static long recursiveCountFalse(int[] symArr, char[] optArr, int start, int end) {
		if (start > end) return symArr[start] ^ 1;
		long sol = 0; 
		for (int i=start; i<=end; i++) {
			long leftTrue = recursiveCountTrue(symArr, optArr, start, i-1); 
			long rightTrue = recursiveCountTrue(symArr, optArr, i+1, end);
			long leftFalse = recursiveCountFalse(symArr, optArr, start, i-1); 
			long rightFalse = recursiveCountFalse(symArr, optArr, i+1, end);
			if (optArr[i] == '&') sol += leftFalse * rightFalse + leftTrue * rightFalse + rightTrue * leftFalse;
			if (optArr[i] == '^') sol += leftTrue * rightTrue + leftFalse * rightFalse;
			if (optArr[i] == '|') sol += leftFalse * rightFalse;
		}
		return sol;
	}
	private static long recursiveCountTrueDriver(int[] symArr, char[] optArr) {
		return recursiveCountTrue(symArr, optArr, 0, optArr.length-1);
	}
	
	/** 
	 * We can also recursively count the total number of possible parenthesizations and use that to calculate
	 * the number of those that evaluate to true. 
	 */
	private static long recursiveCountTrueViaTotal(int[] symArr, char[] optArr, int start, int end) {
		if (start > end) return symArr[start];
		long sol = 0; 
		for (int i=start; i<=end; i++) {
			long left = recursiveCountTrueViaTotal(symArr, optArr, start, i-1); 
			long right = recursiveCountTrueViaTotal(symArr, optArr, i+1, end);
			long leftTotal = recursiveCountAll(start, i-1); 
			long rightTotal = recursiveCountAll(i+1, end);
			if (optArr[i] == '&') sol += left * right;
			if (optArr[i] == '^') sol += left * (rightTotal - right) + right * (leftTotal - left);
			if (optArr[i] == '|') sol += leftTotal * rightTotal - (leftTotal - left) * (rightTotal - right);
		}
		return sol;
	}
	// Though recursion is used here to compute the total number of possible parenthesizations, it's worth noting that such 
	// numbers might form a mathematical number sequence whose n'th value can be calculated from n via an analytical formula.
	// A short list of such values will be printed at the end of the program to see whether that is possible. 
	// [May-23-2018] These numbers are found to be the Catalan numbers and they do have an analytical formula! Check out
	//               the Catalan number problem for details. 
	private static long recursiveCountAll(int start, int end) {
		if (start > end) return 1; // edge encountered to put parentheses
		long sol = 0; 
		for (int i=start; i<=end; i++) {
			long left = recursiveCountAll(start, i-1); 
			long right = recursiveCountAll(i+1, end);
			sol = Math.addExact(sol, Math.multiplyExact(left, right)); // to avoid long value overflow, available in Java 8
		}
		return sol;
	}
	private static long recursiveCountTrueViaTotalDriver(int[] symArr, char[] optArr) {
		return recursiveCountTrueViaTotal(symArr, optArr, 0, optArr.length-1);
	}
	
	/**
	 * We try to void repeated computations via DP memoization.
	 */
	private static long recursiveCountTrueViaTotalDPMemo(int[] symArr, char[] optArr, int start, int end, long[][] trueTable, long[][] totalTable) {
		if (trueTable[start][end+1] != -1) return trueTable[start][end+1];
		else {
			if (start > end) trueTable[start][end+1] = symArr[start];
			else {
				long sol = 0; 
				for (int i=start; i<=end; i++) {
					long left = recursiveCountTrueViaTotalDPMemo(symArr, optArr, start, i-1, trueTable, totalTable); 
					long right = recursiveCountTrueViaTotalDPMemo(symArr, optArr, i+1, end, trueTable, totalTable);
					long leftTotal = totalTable[start][i];
					long rightTotal = totalTable[i+1][end+1];
					// long leftTotal = recursiveCountAllDPMemo(start, i-1, totalTable); 
					// long rightTotal = recursiveCountAllDPMemo(i+1, end, totalTable);
					if (optArr[i] == '&') sol += left * right;
					if (optArr[i] == '^') sol += left * (rightTotal - right) + right * (leftTotal - left);
					if (optArr[i] == '|') sol += leftTotal * rightTotal - (leftTotal - left) * (rightTotal - right);
				}
				trueTable[start][end+1] = sol;
			}
			return trueTable[start][end+1];
		}
	}
	private static long recursiveCountAllDPMemo(int start, int end, long[][] table) {
		if (table[start][end+1] != -1) return table[start][end+1]; 
		else {		
			if (start > end) table[start][end+1] = 1; // edge encountered to put parentheses
			else {
				long sol = 0; 
				for (int i=start; i<=end; i++) {
					long left = recursiveCountAllDPMemo(start, i-1, table); 
					long right = recursiveCountAllDPMemo(i+1, end, table);
					// to avoid long value overflow, available in Java 8
					// sol = Math.addExact(sol, Math.multiplyExact(left, right)); 
					// manually avoid long value overflow
					long product;
					if (left != 0 && right > Long.MAX_VALUE / left) throw new ArithmeticException("Long.MAX_VALUE exceeded!");
					else product = left * right;
					if (sol > Long.MAX_VALUE - product) throw new ArithmeticException("Long.MAX_VALUE exceeded!");
					else sol += product;
				}
				table[start][end+1] = sol;
			}
			return table[start][end+1];
		}
	}
	private static long recursiveCountAllDPMemoDriver(int start, int end) {
		long[][] DPLookUp = new long[end+2][end+2]; 
		for (long[] row: DPLookUp) Arrays.fill(row, -1);
        return recursiveCountAllDPMemo(start, end, DPLookUp);
	}
	private static long recursiveCountTrueViaTotalDPMemoDriver(int[] symArr, char[] optArr) {
		long[][] allCountDPLookUp = new long[optArr.length+1][optArr.length+1]; 
		long[][] trueCountDPLookUp = new long[optArr.length+1][optArr.length+1];
		for (long[] row: allCountDPLookUp) Arrays.fill(row, -1);
		for (long[] row: trueCountDPLookUp) Arrays.fill(row, -1);
        recursiveCountAllDPMemo(0, optArr.length-1, allCountDPLookUp); // calculate total counts first, but not necessary
		long res = recursiveCountTrueViaTotalDPMemo(symArr, optArr, 0, optArr.length-1, trueCountDPLookUp, allCountDPLookUp);
//		for (long[] row: allCountDPLookUp) System.out.println(Arrays.toString(row));
//		for (long[] row: trueCountDPLookUp) System.out.println(Arrays.toString(row));
		return res;
	}

	
	/**
	 * We try to void repeated computations via DP tabulation.
	 */
	private static long iterativeCountTrueViaTotalDPTabu(int[] symArr, char[] optArr) {
		int size = optArr.length;
		long[][] table = new long[size+1][size+1]; // DP lookup table for true count
		long[][] totalTable = iterativeCountAllDPTabu(0, size-1);
		table[0][0] = symArr[0]; // base state
		// fill up the lookup table in a diagonal way
		for (int j=0; j<=size-1; j++)
			for (int i=j+1; i>=0; i--) {
				if (i>j) table[i][j+1] = symArr[i]; 
				else {
					table[i][j+1] = 0; 
					for (int k=i; k<=j; k++) {
						long left = table[i][k], right = table[k+1][j+1];
						long leftTotal = totalTable[i][k], rightTotal = totalTable[k+1][j+1]; 
						if (optArr[k] == '&') table[i][j+1] += left * right;
						if (optArr[k] == '^') table[i][j+1] += left * (rightTotal - right) + right * (leftTotal - left);
						if (optArr[k] == '|') table[i][j+1] += leftTotal * rightTotal - (leftTotal - left) * (rightTotal - right);
					}
				}
			}
		//for (long[] row: table) System.out.println(Arrays.toString(row));
		return table[0][size];
	}
	private static long[][] iterativeCountAllDPTabu(int start, int end) {
		int size = end + 1;
		long[][] table = new long[size+1][size+1]; // DP lookup table
		table[0][0] = 1; // base state
		// fill the DP lookup table in a diagonal way
		for (int j=0; j<=size-1; j++) 
			for (int i=j+1; i>=0; i--) {
				if (i>j) table[i][j+1] = 1; 
				else {
					table[i][j+1] = 0; 
					for (int k=i; k<=j; k++)
						table[i][j+1] += table[i][k] * table[k+1][j+1];
				}
			}
		//for (long[] row: table) System.out.println(Arrays.toString(row));
		//return table[start][end+1];
		return table;
	}
	
	/**
	 * Recursively find all possible parenthesizations.
	 */
	private static ArrayList<String> recursiveFindAll(int[] symArr, char[] optArr, int start, int end) {
		if (start > end) { // edge encountered to put parentheses
			ArrayList<String> sol = new ArrayList<String>();
			sol.add(String.valueOf(symArr[start]));
			return sol;
		}
		ArrayList<String> sol = new ArrayList<String>();
		for (int i=start; i<=end; i++) {
			ArrayList<String> left = recursiveFindAll(symArr, optArr, start, i-1); 
			ArrayList<String> right = recursiveFindAll(symArr, optArr, i+1, end);
			for (String l: left) 
				for (String r: right) 
					sol.add("(" + l + optArr[i] + r + ")"); 
		}
		return sol;
	}
	private static ArrayList<String> recursiveFindAllDriver(int[] symArr, char[] optArr) {
		return recursiveFindAll(symArr, optArr, 0, optArr.length-1);
	}
	
	/**
	 * Recursively find all possible parenthesizations that evaluate to true.
	 */
	static class SolutionSet {
		ArrayList<String> trueList; 
		ArrayList<String> falseList;
		public SolutionSet(ArrayList<String> trueList, ArrayList<String> falseList) {
			this.trueList = trueList; 
			this.falseList = falseList;
		}
	}
	private static SolutionSet recursiveFindTrueAndFalse(int[] symArr, char[] optArr, int start, int end) {
		if (start > end) { // edge encountered to put parentheses
			ArrayList<String> tList = new ArrayList<String>();
			ArrayList<String> fList = new ArrayList<String>();
			if (symArr[start] == 1) tList.add(String.valueOf(symArr[start]));
			if (symArr[start] == 0) fList.add(String.valueOf(symArr[start]));
			return new SolutionSet(tList, fList);
		}
		ArrayList<String> tList = new ArrayList<String>();
		ArrayList<String> fList = new ArrayList<String>();
		for (int i=start; i<=end; i++) {
			ArrayList<String> leftTrue = recursiveFindTrueAndFalse(symArr, optArr, start, i-1).trueList; 
			ArrayList<String> rightTrue = recursiveFindTrueAndFalse(symArr, optArr, i+1, end).trueList;
			ArrayList<String> leftFalse = recursiveFindTrueAndFalse(symArr, optArr, start, i-1).falseList; 
			ArrayList<String> rightFalse = recursiveFindTrueAndFalse(symArr, optArr, i+1, end).falseList;
			// get sub solution sets
			ArrayList<String> lTrT = new ArrayList<String>();
			for (String l: leftTrue) 
				for (String r: rightTrue) 
					lTrT.add("(" + l + optArr[i] + r + ")");
			
			ArrayList<String> lTrF = new ArrayList<String>();
			for (String l: leftTrue) 
				for (String r: rightFalse) 
					lTrF.add("(" + l + optArr[i] + r + ")");
			
			ArrayList<String> lFrT = new ArrayList<String>();
			for (String l: leftFalse) 
				for (String r: rightTrue) 
					lFrT.add("(" + l + optArr[i] + r + ")");
			
			ArrayList<String> lFrF = new ArrayList<String>();
			for (String l: leftFalse) 
				for (String r: rightFalse) 
					lFrF.add("(" + l + optArr[i] + r + ")");
			// combine
			if (optArr[i] == '&') {
				tList.addAll(lTrT);
				fList.addAll(lFrF);
				fList.addAll(lFrT);
				fList.addAll(lTrF);
			}
			if (optArr[i] == '^') {
				tList.addAll(lFrT);
				tList.addAll(lTrF);
				fList.addAll(lFrF);
				fList.addAll(lTrT);
			}
			if (optArr[i] == '|') {
				tList.addAll(lTrT);
				tList.addAll(lFrT);
				tList.addAll(lTrF);
				fList.addAll(lFrF);
			}
		}
		return new SolutionSet(tList, fList);
	}
	private static ArrayList<String> recursiveFindTrueDriver(int[] symArr, char[] optArr) {
		return recursiveFindTrueAndFalse(symArr, optArr, 0, optArr.length-1).trueList;
	}
	
	/**
	 * We try to avoid repeated computations via DP memoization.
	 */
	private static SolutionSet recursiveFindTrueAndFalseDPMemo(int[] symArr, char[] optArr, int start, int end, SolutionSet[][] table) {
		if (table[start][end+1] != null) return table[start][end+1]; 
		else {
			if (start > end) { // edge encountered to put parentheses
				ArrayList<String> tList = new ArrayList<String>();
				ArrayList<String> fList = new ArrayList<String>();
				if (symArr[start] == 1) tList.add(String.valueOf(symArr[start]));
				if (symArr[start] == 0) fList.add(String.valueOf(symArr[start]));
				table[start][end+1] = new SolutionSet(tList, fList);
			} else {
				ArrayList<String> tList = new ArrayList<String>();
				ArrayList<String> fList = new ArrayList<String>();
				for (int i=start; i<=end; i++) {
					ArrayList<String> leftTrue = recursiveFindTrueAndFalseDPMemo(symArr, optArr, start, i-1, table).trueList; 
					ArrayList<String> rightTrue = recursiveFindTrueAndFalseDPMemo(symArr, optArr, i+1, end, table).trueList;
					ArrayList<String> leftFalse = recursiveFindTrueAndFalseDPMemo(symArr, optArr, start, i-1, table).falseList; 
					ArrayList<String> rightFalse = recursiveFindTrueAndFalseDPMemo(symArr, optArr, i+1, end, table).falseList;
					// get sub solution sets
					ArrayList<String> lTrT = new ArrayList<String>();
					for (String l: leftTrue) 
						for (String r: rightTrue) 
							lTrT.add("(" + l + optArr[i] + r + ")");
					
					ArrayList<String> lTrF = new ArrayList<String>();
					for (String l: leftTrue) 
						for (String r: rightFalse) 
							lTrF.add("(" + l + optArr[i] + r + ")");
					
					ArrayList<String> lFrT = new ArrayList<String>();
					for (String l: leftFalse) 
						for (String r: rightTrue) 
							lFrT.add("(" + l + optArr[i] + r + ")");
					
					ArrayList<String> lFrF = new ArrayList<String>();
					for (String l: leftFalse) 
						for (String r: rightFalse) 
							lFrF.add("(" + l + optArr[i] + r + ")");
					// combine
					if (optArr[i] == '&') {
						tList.addAll(lTrT);
						fList.addAll(lFrF);
						fList.addAll(lFrT);
						fList.addAll(lTrF);
					}
					if (optArr[i] == '^') {
						tList.addAll(lFrT);
						tList.addAll(lTrF);
						fList.addAll(lFrF);
						fList.addAll(lTrT);
					}
					if (optArr[i] == '|') {
						tList.addAll(lTrT);
						tList.addAll(lFrT);
						tList.addAll(lTrF);
						fList.addAll(lFrF);
					}
				}
				table[start][end+1] = new SolutionSet(tList, fList);
			}
			return table[start][end+1];
		}
	}
	private static ArrayList<String> recursiveFindTrueDPMemoDriver(int[] symArr, char[] optArr) {
		SolutionSet[][] DPLookUp = new SolutionSet[optArr.length+1][optArr.length+1];
		return recursiveFindTrueAndFalseDPMemo(symArr, optArr, 0, optArr.length-1, DPLookUp).trueList;
	}
	
	/**
	 * We try to avoid repeated computations via DP tabulation.
	 */
	private static SolutionSet iterativeFindTrueAndFalseDPTabu(int[] symArr, char[] optArr) {
		int size = optArr.length;
		SolutionSet[][] table = new SolutionSet[size+1][size+1]; // DP lookup table
		ArrayList<String> tList, fList;
		// base state
		for (int i=0; i<=size; i++) {
			tList = new ArrayList<String>();
			fList = new ArrayList<String>();
			if (symArr[i] == 1) tList.add(String.valueOf(symArr[i]));
			if (symArr[i] == 0) fList.add(String.valueOf(symArr[i]));
			table[i][i] = new SolutionSet(tList, fList); 
		}
		// fill up the DP lookup table in a diagonal manner
		for (int j=0; j<=size-1; j++)
			for (int i=j; i>=0; i--) {
				if (i>j) {
					tList = new ArrayList<String>();
					fList = new ArrayList<String>();
					if (symArr[i] == 1) tList.add(String.valueOf(symArr[i]));
					if (symArr[i] == 0) fList.add(String.valueOf(symArr[i]));
					table[i][j+1] = new SolutionSet(tList, fList);
				} else {
					tList = new ArrayList<String>();
					fList = new ArrayList<String>();
					for (int k=i; k<=j; k++) {
						ArrayList<String> leftTrue = table[i][k].trueList; 
						ArrayList<String> rightTrue = table[k+1][j+1].trueList;
						ArrayList<String> leftFalse = table[i][k].falseList; 
						ArrayList<String> rightFalse = table[k+1][j+1].falseList;
						// get sub solution sets
						ArrayList<String> lTrT = new ArrayList<String>();
						for (String l: leftTrue) 
							for (String r: rightTrue) 
								lTrT.add("(" + l + optArr[k] + r + ")");
						
						ArrayList<String> lTrF = new ArrayList<String>();
						for (String l: leftTrue) 
							for (String r: rightFalse) 
								lTrF.add("(" + l + optArr[k] + r + ")");
						
						ArrayList<String> lFrT = new ArrayList<String>();
						for (String l: leftFalse) 
							for (String r: rightTrue) 
								lFrT.add("(" + l + optArr[k] + r + ")");
						
						ArrayList<String> lFrF = new ArrayList<String>();
						for (String l: leftFalse) 
							for (String r: rightFalse) 
								lFrF.add("(" + l + optArr[k] + r + ")");
						// combine
						if (optArr[k] == '&') {
							tList.addAll(lTrT);
							fList.addAll(lFrF);
							fList.addAll(lFrT);
							fList.addAll(lTrF);
						}
						if (optArr[k] == '^') {
							tList.addAll(lFrT);
							tList.addAll(lTrF);
							fList.addAll(lFrF);
							fList.addAll(lTrT);
						}
						if (optArr[k] == '|') {
							tList.addAll(lTrT);
							tList.addAll(lFrT);
							tList.addAll(lTrF);
							fList.addAll(lFrF);
						}
					}
					table[i][j+1] = new SolutionSet(tList, fList);
				}
			}
		return table[0][size];
	}
	private static ArrayList<String> iterativeFindTrueDPTabu(int[] symArr, char[] optArr) {
		return iterativeFindTrueAndFalseDPTabu(symArr, optArr).trueList;
	}
	
	@FunctionalInterface
	protected interface ArrayToLongFunction {
	   long apply(int[] a, char[] b) throws Exception;  
	}
	
    protected static void runFuncAndCalculateTime(String message, ArrayToLongFunction func, int[] a, char[] b) throws Exception {
    	long startTime = System.nanoTime();
    	System.out.printf("%-80s%s\n", message, func.apply(a, b));
    	long endTime = System.nanoTime();
    	long totalTime = new Long(TimeUnit.MICROSECONDS.convert(endTime - startTime, TimeUnit.NANOSECONDS));
    	DecimalFormat formatter = new DecimalFormat("#,###");
    	System.out.printf("%-70s%s\n\n", "Function execution time in micro-seconds: ", formatter.format(totalTime));
    }

	@FunctionalInterface
	protected interface ArrayToArrayListFunction {
	   ArrayList<String> apply(int[] a, char[] b) throws Exception;  
	}
	
    protected static void runFuncAndCalculateTime(String message, ArrayToArrayListFunction func, int[] a, char[] b) throws Exception {
    	long startTime = System.nanoTime();
    	System.out.printf("%-80s\n", message);
    	ArrayList<String> res = func.apply(a, b);
    	for (String s: res) System.out.println(s);
    	System.out.println("Size of list: " + res.size());
    	long endTime = System.nanoTime();
    	long totalTime = new Long(TimeUnit.MICROSECONDS.convert(endTime - startTime, TimeUnit.NANOSECONDS));
    	DecimalFormat formatter = new DecimalFormat("#,###");
    	System.out.printf("%-70s%s\n\n", "Function execution time in micro-seconds: ", formatter.format(totalTime));
    }
	
	public static void main(String[] args) {
//		int[] symArr = {1, 1, 0, 1}; 
//		char[] optArr = {'|', '&', '^'}; 
//		int[] symArr = {1, 0}; 
//		char[] optArr = {'|'}; 
//		int[] symArr = {1, 0, 1}; 
//		char[] optArr = {'^', '&'}; 
//		int[] symArr = {1, 0, 0}; 
//		char[] optArr = {'^', '|'}; 
//		int[] symArr = {1, 1, 1}; 
//		char[] optArr = {'^', '^'};	
//		int[] symArr = {1, 1, 1}; 
//		char[] optArr = {'&', '&'};	
//		int[] symArr = {0, 0, 0}; 
//		char[] optArr = {'^', '^'};	
//		int[] symArr = {1, 1, 0, 1, 1, 1, 0, 1, 0}; 
//		char[] optArr = {'|', '&', '^', '|', '&', '^', '|', '&'}; 
//		int[] symArr = {1, 1, 0, 1, 1}; 
//		char[] optArr = {'|', '&', '^', '|'}; 
		
		genSymAndOptArr(6);

		System.out.println("Welcome to the rabbit hole of boolean parenthesizations!\n"
				+ "The boolean symbol array denoted by 1 and 0 is " + Arrays.toString(symArr) + ".\n"
				+ "The boolean operator array is " + Arrays.toString(optArr) + ".\n"); 
		
		try {
			runFuncAndCalculateTime("[Recursive][by false count]     Count of possible parenthesizations to true:" , 
					(int[] a, char[] b) -> recursiveCountTrueDriver(a, b), symArr, optArr);
			runFuncAndCalculateTime("[Recursive][by total count]     Count of possible parenthesizations to true:" , 
					(int[] a, char[] b) -> recursiveCountTrueViaTotalDriver(a, b), symArr, optArr);
			runFuncAndCalculateTime("[Recursive][DP Memo]            Count of possible parenthesizations to true:" , 
					(int[] a, char[] b) -> recursiveCountTrueViaTotalDPMemoDriver(a, b), symArr, optArr);
			runFuncAndCalculateTime("[Iterative][DP Tabu]            Count of possible parenthesizations to true:" , 
					(int[] a, char[] b) -> iterativeCountTrueViaTotalDPTabu(a, b), symArr, optArr);
			runFuncAndCalculateTime("[Recursive]                     List of all possible parenthesizations:" , 
					(int[] a, char[] b) -> recursiveFindAllDriver(a, b), symArr, optArr);
			runFuncAndCalculateTime("[Recursive]                     List of all parenthesizations to true:" , 
					(int[] a, char[] b) -> recursiveFindTrueDriver(a, b), symArr, optArr);
			runFuncAndCalculateTime("[Recursive][DP Memo]            List of all parenthesizations to true:" , 
					(int[] a, char[] b) -> recursiveFindTrueDPMemoDriver(a, b), symArr, optArr);
			runFuncAndCalculateTime("[Iterative][DP Tabu]            List of all parenthesizations to true:" , 
					(int[] a, char[] b) -> iterativeFindTrueDPTabu(a, b), symArr, optArr);
			
			System.out.println("Let's check for number of possible parenthesizations with operator array size from 0 to 99...");
			try {
				for (int i=0; i<100; i++) System.out.printf("%s%-4d%s%d\n", "Size: ", i, " --> ", recursiveCountAllDPMemoDriver(0, i));
			} catch (ArithmeticException e) {
				e.printStackTrace();
				System.out.println("The number grows so fast that it exceeds the max value of the long integer type with a mere array size of 35...\n");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("All rabbits gone.");
	}
	

}
