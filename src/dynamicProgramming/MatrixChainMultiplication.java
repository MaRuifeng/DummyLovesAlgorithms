package dynamicProgramming;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import utils.FunIntAlgorithm;

/**
 * Given a sequence of matrices, find the most efficient way to multiply these matrices together. 
 * 
 * Matrix multiplication is associative, so there are many options to multiply a chain of matrices, each of which 
 * involves different number of arithmetic operations to be performed. 
 * 
 * E.g. suppose A is a 10 X 30 matrix, B is a 30 X 5 matrix, C is a 5 X 60 matrix and D is a 60 X 10 matrix then
 *      (((AB)C)D) = ((AB)(CD)) = (A((BC)D)) = ((A(BC))D) = ..., and the total number of arithmetic operations needed are 
 *      (((AB)C)D) = 10*30*5 + 10*5*60 + 10*60*10 = 10500
 *      ((AB)(CD)) = 10*30*5 + 5*60*10 + 10*5*10  = 5000
 *      ...
 *      hence there must be a parenthesization that yields the least number of arithmetic operations. 
 *
 * The problem can be stated as an array that represent a chain of matrices such that the dimensions of the i'th matrix
 * are stored in the (i-1)'th and i'th element of the array. 
 * 
 * For above example, the array is [10, 30, 5, 60, 10]. 
 * 
 * The solution to this problem is analogous to that of the rod cutting problem.
 * 
 * @author ruifengm
 * @since 2018-May-25
 * 
 * https://www.geeksforgeeks.org/dynamic-programming-set-8-matrix-chain-multiplication/
 */

public class MatrixChainMultiplication extends FunIntAlgorithm {
	
	private static char[] matrixList; 
	private static int[] dimensionList; 
	
	private static void genMatrixAndDimenList(int size) {
		matrixList = new char[size];
		dimensionList = new int[size+1]; 
		for (int i=0; i<size; i++) {
			matrixList[i] = (char)(65 + i); 
			dimensionList[i] = ThreadLocalRandom.current().nextInt(1, 9) * 10;
		}
		dimensionList[size] = ThreadLocalRandom.current().nextInt(1, 9) * 10;
	}
	
	/**
	 * A brute force method is to find all possible parenthesizations and then find the one 
	 * that yields the least number of arithmetic operations. 
	 * Refer to the Boolean Parenthesization problem on how all possible parenthesizations can be found. 
	 * This solution will soon face difficulties with a relatively small input size as the numbers of possible
	 * parenthesizations form the Catalan Number series which grows very exponentially. 
	 */
	static class Solution {
		long num;      // number of arithmetic operations needed
		String solStr; // parenthesization string
		public Solution (long num, String solStr) {
			this.num = num;
			this.solStr = solStr;
		}
	}
	private static ArrayList<Solution> recursiveFindAll(char[] matrixList, int[] dimensionList, int start, int end) {
		if (start > end) {
			Solution sol = new Solution(0, String.valueOf(matrixList[start])); 
			ArrayList<Solution> solList = new ArrayList<>(); 
			solList.add(sol);
			return solList;
		}
		ArrayList<Solution> solList = new ArrayList<>();
		for (int i=start; i<=end; i++) {
			ArrayList<Solution> leftSolList = recursiveFindAll(matrixList, dimensionList, start, i-1);
			ArrayList<Solution> rightSolList = recursiveFindAll(matrixList, dimensionList, i+1, end); 
			for (Solution l: leftSolList) 
				for (Solution r: rightSolList) {
					Solution sol = new Solution(l.num + r.num + dimensionList[start] * dimensionList[i+1] * dimensionList[end+2], 
							"(" + l.solStr + r.solStr + ")");
					solList.add(sol);
				}
		}
		return solList;
	}
	private static Solution findSolByBruteForce(char[] matrixList, int[] dimensionList) {
		ArrayList<Solution> solList = recursiveFindAll(matrixList, dimensionList, 0, matrixList.length-2); 
		Solution minSol = new Solution(Long.MAX_VALUE, "");
		for (Solution sol: solList) {
			if (sol.num < minSol.num) minSol = sol; 
		}
		return minSol;
	}
	
	/**
	 * Find only the least number of arithmetic operations involved. 
	 * For the given dimension array, say [10, 30, 5, 60, 10], we try to find the least number of operations needed
	 * when the final multiplication occurs at 30, 5, and 60 respectively, and then find the least among them. 
	 * This is a greedy strategy from local optimal to global optimal. 
	 */
	private static long recursiveFindLeastNumOfOps(int[] dimensionList, int start, int end) {
		if (end-start == 1) return 0; 
		long min = Long.MAX_VALUE;
		for (int i=start+1; i<=end-1; i++) {
			long left = recursiveFindLeastNumOfOps(dimensionList, start, i); 
			long right = recursiveFindLeastNumOfOps(dimensionList, i, end);
			long val = left + right + dimensionList[start] * dimensionList[i] * dimensionList[end]; 
			if (val < min) min = val;
		}
		return min;	
	}
	private static long recursiveFindLeastNumOfOps(int[] dimensionList) {
		return recursiveFindLeastNumOfOps(dimensionList, 0, dimensionList.length-1);
	}
	
	/**
	 * We try optimize the above method with DP memoization.
	 */
	private static long recursiveFindLeastNumOfOpsDPMemo(int[] dimensionList, int start, int end, long[][] table) {
		if (table[start][end-1] != -1) return table[start][end-1];
		else {
			if (end-start == 1) table[start][end-1] = 0; 
			else {
				long min = Long.MAX_VALUE; 
				for (int i=start+1; i<=end-1; i++) {
					long left = recursiveFindLeastNumOfOpsDPMemo(dimensionList, start, i, table); 
					long right = recursiveFindLeastNumOfOpsDPMemo(dimensionList, i, end, table);
					long val = (left + right + dimensionList[start] * dimensionList[i] * dimensionList[end]);
					if (val < min) min = val;
				}
				table[start][end-1] = min;
			}
			return table[start][end-1];
		}
	}
	private static long recursiveFindLeastNumOfOpsDPMemo(int[] dimensionList) {
		long[][] DPLookUp = new long[dimensionList.length-1][dimensionList.length-1]; 
		for (long[] row: DPLookUp) Arrays.fill(row, -1);
		long sol = recursiveFindLeastNumOfOpsDPMemo(dimensionList, 0, dimensionList.length-1, DPLookUp);
		// for (long[] row: DPLookUp) System.out.println(Arrays.toString(row));
		return sol;
	}
	
	/**
	 * We try to optimize the above method with DP tabulation. 
	 */
	private static long iterativeFindLeastNumOfOpsDPTabu(int[] dimensionList) {
		int size = dimensionList.length;
		long[][] table = new long[size-1][size-1]; // DP lookup table
		// fill up the DP table in a diagonal way
		for (int j=1; j<=size-1; j++) {
			for (int i=j-1; i>=0; i--) {
				if (j-i == 1) table[i][j-1] = 0; 
				else {
					long min = Long.MAX_VALUE;
					for (int k=i+1; k<=j-1; k++) {
						long left = table[i][k-1]; 
						long right = table[k][j-1];
						long val = left + right + dimensionList[i] * dimensionList[k] * dimensionList[j]; 
						if (val < min) min = val;
					}
					table[i][j-1] = min;
				}
			}
		}
		// for (long[] row: table) System.out.println(Arrays.toString(row));
		return table[0][size-2];
	}
	
	/**
	 * We modify the tabulation method above to return a complete solution. 
	 * Note that there might be multiple solutions, instead of collecting all, we just return one. 
	 */
	private static Solution iterativeFindSolutionDPTabu(char[] matrixList, int[] dimensionList) {
		int size = dimensionList.length;
		Solution[][] table = new Solution[size-1][size-1]; // DP lookup table
		// fill up the DP table in a diagonal way
		for (int j=1; j<=size-1; j++) {
			for (int i=j-1; i>=0; i--) {
				if (j-i == 1) table[i][j-1] = new Solution(0, String.valueOf(matrixList[i]));
				else {
					Solution min = new Solution(Long.MAX_VALUE, "");
					for (int k=i+1; k<=j-1; k++) {
						Solution left = table[i][k-1]; 
						Solution right = table[k][j-1];
						Solution sol = new Solution(left.num + right.num + dimensionList[i] * dimensionList[k] * dimensionList[j], 
								"(" + left.solStr + right.solStr + ")");
						if (sol.num < min.num) min = sol;
					}
					table[i][j-1] = min;
				}
			}
		}
//		for (Solution[] row: table) {
//			for (Solution sol: row) {
//				if (sol == null) System.out.print("null | ");
//				else {
//					System.out.print(sol.solStr + " ");
//					System.out.print(sol.num + " | ");
//				}
//			}
//			System.out.println();
//		}
		return table[0][size-2];
	}
	
	@FunctionalInterface
	protected interface ArrayToSolutionFunction {
	   Solution apply(char[] a, int[] b) throws Exception;  
	}
	
    protected static void runFuncAndCalculateTime(String message, ArrayToSolutionFunction func, char[] a, int[] b) throws Exception {
    	long startTime = System.nanoTime();
    	Solution sol = func.apply(a, b);
    	System.out.printf("%-50s%-60s%-20d\n", message, sol.solStr, sol.num);
    	long endTime = System.nanoTime();
    	long totalTime = new Long(TimeUnit.MICROSECONDS.convert(endTime - startTime, TimeUnit.NANOSECONDS));
    	DecimalFormat formatter = new DecimalFormat("#,###");
    	System.out.printf("%-70s%s\n\n", "Function execution time in micro-seconds: ", formatter.format(totalTime));
    }
	
	public static void main(String[] args) {
//		char[] matrixList = {'A'}; 
//		int[] dimensionList = {40, 20}; 
		
//		char[] matrixList = {'A', 'B'}; 
//		int[] dimensionList = {40, 20, 30}; 
		
//		char[] matrixList = {'A', 'B', 'C'}; 
//		int[] dimensionList = {40, 20, 30, 10}; 
		
//		char[] matrixList = {'A', 'B', 'C', 'D'}; 
//		int[] dimensionList = {40, 20, 30, 10, 30}; 
		
//		char[] matrixList = {'A', 'B', 'C', 'D'}; 
//		int[] dimensionList = {10, 20, 30, 40, 30};
		
//		char[] matrixList = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H'}; 
//		int[] dimensionList = {10, 20, 30, 40, 30, 20, 10, 40, 80}; 

//		char[] matrixList = {'A', 'B', 'C'}; 
//		int[] dimensionList = {10, 10, 10, 10}; 
		
		genMatrixAndDimenList(15);


		System.out.println("Welcome to the rabbit hole of matrix chain multiplications!\n"
				+ "The matrix symbol list is " + Arrays.toString(matrixList) + ".\n"
				+ "And their dimention list is " + Arrays.toString(dimensionList) + ".\n"
				+ "Find the chain multiplication with the least number of arithmetic operations.\n"); 
		
		try {
			runFuncAndCalculateTime("[Recursive][BruteForce]      Solution:" , 
					(char[] a, int[] b) -> findSolByBruteForce(a, b), matrixList, dimensionList);
			runIntArrayFuncAndCalculateTime("[Recursive]           Least number of arithmetic operations:" , 
					(int[] b) -> recursiveFindLeastNumOfOps(b), dimensionList);
			runIntArrayFuncAndCalculateTime("[Recursive][DP Memo]  Least number of arithmetic operations:" , 
					(int[] b) -> recursiveFindLeastNumOfOpsDPMemo(b), dimensionList);
			runIntArrayFuncAndCalculateTime("[Recursive][DP Tabu]  Least number of arithmetic operations:" , 
					(int[] b) -> iterativeFindLeastNumOfOpsDPTabu(b), dimensionList);
			runFuncAndCalculateTime("[Iterative][DP Tabu]  Soltuion:" , 
					(char[] a, int[] b) -> iterativeFindSolutionDPTabu(a, b), matrixList, dimensionList);		
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("All rabbits gone.");
	}
	
	

}
