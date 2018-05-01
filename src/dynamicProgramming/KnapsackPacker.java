package dynamicProgramming;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import utils.FunIntAlgorithm;

/**
 * Given weights and values of n items and a knapsack of capacity W, find the maximum total value 
 * that could be obtained by putting items into the knapsack. No item can be broken into parts. An 
 * item can only be picked or not picked, which yields the 0-1 property of the problem. 
 * 
 * It's worth noting that the knapsack problem is NP-complete and the related time complexity is discussed
 * in the subset sum problem. 
 * 
 * @author ruifengm
 * @since 2018-May-1 
 * 
 * https://www.geeksforgeeks.org/knapsack-problem/
 *
 */
public class KnapsackPacker extends FunIntAlgorithm {
	
	/**
	 * We try to observe the sub-problem and optimal substructure properties of the problem. 
	 * Let V(n) be the value array, W(n) be the weight array and C be the capacity of the knapsack. Let Sol(V(n), W(n), C) be the maximum 
	 * total value sum that can be obtained by packing items into the knapsack. 
	 *     Sol(V(n), W(n), C) = MAX( Sol(V(n-1), W(n-1), C),                     last item is not packed into the knapsack
	 *                               Sol(V(n-1), W(n-1), C-W[n-1]) + V[n-1] )    last item is packed into the knapsack
	 */
	private static int recursivePackForMaxValSum(int[] v, int[] w, int size, int cap) {
		if (cap == 0 || size == 0) return 0; // knapsack has no capacity or no items can be packed
		if (cap < w[size-1]) return recursivePackForMaxValSum(v, w, size-1, cap); // exclude last item and recur
		else return Math.max(recursivePackForMaxValSum(v, w, size-1, cap),    // exclude last item and recur
				recursivePackForMaxValSum(v, w, size-1, cap - w[size-1]) + v[size-1]);  // include last item and recur
	}
	private static int recursivePackForMaxValSumDriver(int[] v, int[] w, int cap) throws Exception { 
		if (v.length != w.length) throw new Exception("Value array and weight array must be of the same size!"); 
		return recursivePackForMaxValSum(v, w, v.length, cap);
	}
	
	/** 
	 * Avoid repeated computations via a DP lookup table constructed via memoization. 
	 */
	private static int recursivePackForMaxValSumDPMemo(int[] v, int[] w, int size, int cap, int[][] table) {
		if (table[size][cap] != -1) return table[size][cap]; 
		else {
			if (cap == 0 || size == 0) table[size][cap] = 0; 
			else {
				if (cap < w[size-1]) table[size][cap] = recursivePackForMaxValSumDPMemo(v, w, size-1, cap, table); 
				else table[size][cap] = Math.max(recursivePackForMaxValSumDPMemo(v, w, size-1, cap, table),    
						recursivePackForMaxValSumDPMemo(v, w, size-1, cap - w[size-1], table) + v[size-1]);  
			}
			return table[size][cap];
		}
	}
	private static int recursivePackForMaxValSumDPMemoDriver(int[] v, int[] w, int cap) throws Exception { 
		if (v.length != w.length) throw new Exception("Value array and weight array must be of the same size!"); 
		int[][] DPLookUp = new int[v.length+1][cap+1]; 
		for (int[] row: DPLookUp) Arrays.fill(row, -1);
		return recursivePackForMaxValSumDPMemo(v, w, v.length, cap, DPLookUp);
	}
	
	/** 
	 * Avoid repeated computations via a DP lookup table constructed via tabulation. 
	 */
	private static int iterativePackForMaxValSumDPTabu(int[] v, int[] w, int cap) throws Exception {
		if (v.length != w.length) throw new Exception("Value array and weight array must be of the same size!"); 
		int size = v.length;
		int[][] table = new int[v.length+1][cap+1]; // DP lookup table
		for (int i=1; i<=size; i++) {
			for (int j=1; j<=cap; j++) {
				if (j < w[i-1]) table[i][j] = table[i-1][j]; 
				else table[i][j] = Math.max(table[i-1][j], table[i-1][j-w[i-1]] + v[i-1]); 
			}
		}
		//for (int[] row: table) System.out.println(Arrays.toString(row));
		return table[size][cap];
	}
	
	/**
	 * The DP lookup table formed via tabulation can be examined to list out items
	 * that should be picked to yield the maximum value sum.
	 */
	private static ArrayList<Integer> iterativeFindItemsToPackForMaxValSum(int[] v, int[] w, int cap)  throws Exception {
		if (v.length != w.length) throw new Exception("Value array and weight array must be of the same size!"); 
		int size = v.length;
		int[][] table = new int[v.length+1][cap+1]; // DP lookup table
		for (int i=1; i<=size; i++) {
			for (int j=1; j<=cap; j++) {
				if (j < w[i-1]) table[i][j] = table[i-1][j]; 
				else table[i][j] = Math.max(table[i-1][j], table[i-1][j-w[i-1]] + v[i-1]); 
			}
		}
		
		ArrayList<Integer> itemIdxList = new ArrayList<>();
		int i = size, j = cap; 
		while (i>0 && j>0) {
			if (j >= w[i-1] && v[i-1] > 0 && table[i][j] == table[i-1][j-w[i-1]] + v[i-1]) { // last item selected if value is positive
				itemIdxList.add(i-1);
				j -= w[i-1]; 
			}
			i--; 
		}
		return itemIdxList;
	}
	
	@FunctionalInterface
	protected interface DoubleIntArrayToLongFunction {
	   int apply(int[] a, int[] b, int cap) throws Exception;  
	}
	
    protected static void runIntArrayFuncAndCalculateTime(String message, DoubleIntArrayToLongFunction intArrayFunc, int[] a, int[] b, int cap) throws Exception {
    	long startTime = System.nanoTime();
    	System.out.printf("%-70s%d\n", message, intArrayFunc.apply(a, b, cap));
    	long endTime = System.nanoTime();
    	long totalTime = new Long(TimeUnit.MICROSECONDS.convert(endTime - startTime, TimeUnit.NANOSECONDS));
    	DecimalFormat formatter = new DecimalFormat("#,###");
    	System.out.printf("%-70s%s\n\n", "Function execution time in micro-seconds: ", formatter.format(totalTime));
    }
	
	public static void main(String[] args) {
		int[] valArr = genRanIntArr(28, 100, 150);
		//int[] wtArr = genRanIntArr(28, 5, 600);
		int[] wtArr = genRanIntArr(28, 5, 100);
		int capacity = 1000;
		
//		int[] valArr = {6, 10, 12};
//		int[] wtArr = {1, 2, 3};
//		int capacity = 7; 
		
//		int[] valArr = {2, 2, 2, 2, 2};
//		int[] wtArr = {1, 1, 1, 1, 1};
//		int capacity = 5; 
		
//		int[] valArr = {8, 2, 1};
//		int[] wtArr = {10, 3, 4};
//		int capacity = 10; 
		
//		int[] valArr = {0, 0, 0};
//		int[] wtArr = {2, 3, 4};
//		int capacity = 10; 
		
//		int[] valArr = {2, 3, 4};
//		int[] wtArr = {0, 0, 0};
//		int capacity = 10;
		
//		int[] valArr = {0, 0, 0};
//		int[] wtArr = {0, 0, 0};
//		int capacity = 10;
		System.out.println("Welcome to the rabbit hole of knapsack packers!\n"
				+ "The value set is \n" + Arrays.toString(valArr) + "\n"
				+ "The weight set is \n" + Arrays.toString(wtArr) + "\n"
				+ "The knapsack capacity is " + capacity + ".\n"); 
		
		try {
			runIntArrayFuncAndCalculateTime("[Recursion][NP-complete]            Max total value: ", 
					(int[] a, int[] b, int c) -> recursivePackForMaxValSumDriver(a, b, c), valArr, wtArr, capacity);
			runIntArrayFuncAndCalculateTime("[Recursion][DP Memo]                Max total value: ", 
					(int[] a, int[] b, int c) -> recursivePackForMaxValSumDPMemoDriver(a, b, c), valArr, wtArr, capacity);
			runIntArrayFuncAndCalculateTime("[Iteration][DP Tabu][O(n*capacity)] Max total value: ", 
					(int[] a, int[] b, int c) -> iterativePackForMaxValSumDPTabu(a, b, c), valArr, wtArr, capacity);
			ArrayList<Integer> itemsToBePicked = iterativeFindItemsToPackForMaxValSum(valArr, wtArr, capacity);
			System.out.println("The " + itemsToBePicked.size() + " item(s) to be picked are:");
			int totalVal = 0, totalWt = 0; 
			for (Integer idx: itemsToBePicked) {
				System.out.printf("Value: %-6dWeight: %-6d\n", valArr[idx.intValue()], wtArr[idx.intValue()]);
				totalVal += valArr[idx.intValue()]; 
				totalWt += wtArr[idx.intValue()]; 
			}
			System.out.println("Total value: " + totalVal);
			System.out.println("Total weight: " + totalWt);
			System.out.println();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("All rabbits gone.");
	}

}
