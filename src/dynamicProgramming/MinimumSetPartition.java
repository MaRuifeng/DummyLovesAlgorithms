package dynamicProgramming;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

import utils.FunIntAlgorithm;

/**
 * Partition a given integer set into two subsets such that the difference of their sums is the smallest. 
 * 
 * Mathematical rephrasing: given a set S with n integers, find two subsets S1 and S2 such that 
 *                          1) S = S1 U S2
 *                          2) ABS(SUM(S1) - SUM(S2)) is minimum
 * 
 * https://www.geeksforgeeks.org/partition-a-set-into-two-subsets-such-that-the-difference-of-subset-sums-is-minimum/
 * 
 * @author ruifengm
 * @since 2018-Apr-22
 */
public class MinimumSetPartition extends FunIntAlgorithm {
	
	private static void recursivePrintSubset(int[] a, int size, boolean[] ops) {
		if (size == 0) return; // nothing to print
		// including a[size-1]
		ops[size-1] = true;
		recursivePrintSubset(a, size - 1, ops);
		//System.out.println(Arrays.toString(ops));
		for (int i=0; i<ops.length; i++) {
			if (ops[i] == true) System.out.print(a[i] + " ");
		}
		System.out.println("");
		// excluding a[size-1]
		ops[size-1] = false;
		recursivePrintSubset(a, size - 1, ops);
	}
	private static void recursivePrintSubsetDriver(int[] a) {
		int size = a.length;
		boolean[] ops = new boolean[size];
		Arrays.fill(ops, false);
		recursivePrintSubset(a, size, ops);
	}
	
	/**
	 * Let S(n) denote the total sum of all elements, and S(i) the sum of a subset, 
	 * then we know 1 <= i <= 2^n. 
	 * The absolute difference can be expressed as ABS((S(n) - S(i)) - S(i)), i.e. ABS(S(n) - 2*S(i)). 
	 * We need to find all subsets that match MIN(ABS(S(n) - 2*S(i))). 
	 */
	private static ArrayList<ArrayList<Integer>> bruteForcePartition(int[] a) {
		int totalSum = 0, subSum = 0, minAbs = Integer.MAX_VALUE;
		ArrayList<Integer> intList = new ArrayList<Integer>();
		for (int i: a) {
			totalSum += i;
			intList.add(i);
		}
		ArrayList<ArrayList<Integer>> resList = new ArrayList<ArrayList<Integer>>();
		// get all subsets that meet the requirement
		CopyOnWriteArrayList<ArrayList<Integer>> subsetList = recursiveFindSubset(a, a.length);
		for (ArrayList<Integer> subset: subsetList) {
			subSum = 0;
			for (Integer i: subset) subSum += i; 
			int curAbs = Math.abs(totalSum - 2*subSum);
			if (minAbs > curAbs) {
				minAbs = curAbs;
				resList.removeAll(subsetList); // reset
			}
			if (curAbs == minAbs) resList.add(subset);
		}
		System.out.println("Minimum sum difference is :" + minAbs);
		return resList;
	}
	private static CopyOnWriteArrayList<ArrayList<Integer>> recursiveFindSubset(int[] a, int s) {
		if (s == 0) return new CopyOnWriteArrayList<ArrayList<Integer>>();
		CopyOnWriteArrayList<ArrayList<Integer>> resList = recursiveFindSubset(a, s-1); 
		for (ArrayList<Integer> res: resList) {
			ArrayList<Integer> newRes = new ArrayList<>(res);
			newRes.add(a[s-1]);
			resList.add(newRes);
		}
		ArrayList<Integer> cur = new ArrayList<Integer>();
		cur.add(a[s-1]);
		resList.add(cur);
		return resList;
	}
	
	/**
	 * The problem can be better approached with DP.
	 * Minimum sum difference can be found by a recursive pattern: either include an element in one subset or not.
	 * Let A(n) denote an array of integers from the set, starting from A[n-1], look for a subset that could yield the 
	 * minimum sum difference with its compensating partner. If A[n-1] is included, mark subset sum as A[n-1] and continue looking
	 * in array A(n-1); if A[n-1] is not included, mark subset sum as 0 and continue looking in array A(n-1); repeat until the array
	 * is depleted. 
	 */
	private static int recursiveFindMinDiff(int[] a, int i, int subsetSum, int totalSum) {
		// reached last element, return result
		if (i==0) return Math.abs((totalSum - subsetSum) - subsetSum);
		// recur by including an element in subset sum or not
		else return Math.min(recursiveFindMinDiff(a, i-1, subsetSum + a[i-1], totalSum), 
				recursiveFindMinDiff(a, i-1, subsetSum, totalSum));
	}
	private static int recursiveFindMinDiffDriver(int[] a) {
		int totalSum = 0; 
		for (int i: a) totalSum += i;
		return recursiveFindMinDiff(a, a.length, 0, totalSum);
	}
	private static int recursiveFindMinDiffDPMemo(int[] a, int i, int subsetSum, int totalSum, int[][] table, int negativeSum) {
		if (table[i][subsetSum] != Integer.MIN_VALUE) return table[i][subsetSum]; 
		else {
			// reached last element, return result
			if (i==0) table[i][subsetSum] = Math.abs((totalSum - (subsetSum + negativeSum)) - (subsetSum + negativeSum));
			// recur by including an element in subset sum or not
			else table[i][subsetSum] = Math.min(recursiveFindMinDiffDPMemo(a, i-1, subsetSum + a[i-1], totalSum, table, negativeSum), 
						recursiveFindMinDiffDPMemo(a, i-1, subsetSum, totalSum, table, negativeSum));
			return table[i][subsetSum];
		}

	}
	private static int recursiveFindMinDiffDPMemoDriver(int[] a) {
		int totalSum = 0; 
		int negativeSum = 0;
		int dpLookUpTableRowSize = 0;
		for (int i: a) {
			totalSum += i;
			dpLookUpTableRowSize += Math.abs(i);
			if (i<0) negativeSum += i;
		}
		int[][] DPLookUp = new int[a.length+1][dpLookUpTableRowSize+1]; 
		for (int[] item: DPLookUp) Arrays.fill(item, Integer.MIN_VALUE);
		return recursiveFindMinDiffDPMemo(a, a.length, 0 - negativeSum, totalSum, DPLookUp, negativeSum); // shift right by negative sum
	}
	
	
	@FunctionalInterface
	protected interface IntArrayToArrayListFunction {
		ArrayList<ArrayList<Integer>> apply(int[] array) throws Exception;  
	}
	
    protected static void runIntArrayFuncAndCalculateTime(String message, IntArrayToArrayListFunction intArrFunc, int[] array) throws Exception {
    	long startTime = System.nanoTime();
    	System.out.printf("%-70s\n", message);
    	ArrayList<ArrayList<Integer>> resList = intArrFunc.apply(array);
    	for (int i=0; i<resList.size()/2; i++) {
    		System.out.println(resList.get(i));
    		System.out.println(resList.get(resList.size()-i-1));
    		System.out.println("");
    	}
    	long endTime = System.nanoTime();
    	long totalTime = new Long(TimeUnit.MICROSECONDS.convert(endTime - startTime, TimeUnit.NANOSECONDS));
    	DecimalFormat formatter = new DecimalFormat("#,###");
    	System.out.printf("%-70s%s\n\n", "Function execution time in micro-seconds: ", formatter.format(totalTime));
    }
	
	public static void main(String[] args) {
		int[] intArray = genRanIntArr(15, -20, 20);
		//int[] intArray = {1, 2, 3, 4};
		//int[] intArray = {1, 2, 3, 4, 5, 6};

		System.out.println("Welcome to the rabbit hole of minimum set partitions!\n"
				+ "The integer set is \n" + Arrays.toString(intArray) + "\n"); 
		
		try {
			//recursivePrintSubsetDriver(intArray);
			runIntArrayFuncAndCalculateTime("[Exponential]                  The possible partitions are:", (int[] a) -> bruteForcePartition(a), intArray);
			runIntArrayFuncAndCalculateTime("[Exponential][Recursive]       The minimum sum difference is:", (int[] a) -> recursiveFindMinDiffDriver(a), intArray);	
			runIntArrayFuncAndCalculateTime("[O(n*sum)][Recursive]          The minimum sum difference is:", (int[] a) -> recursiveFindMinDiffDPMemoDriver(a), intArray);		
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("All rabbits gone.");
	}

}
