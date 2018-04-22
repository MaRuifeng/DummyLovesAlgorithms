package dynamicProgramming;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Stack;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;
import utils.FunIntAlgorithm;

/** 
 * Given an array of integers, find the longest increasing subsequence where the elements of the 
 * subsequence is sorted in ascending order. Return all if there are multiples. 
 * 
 * A subsequence of an array is a subset of the array's elements kept in original order. 
 * 
 * E.g. Given {3, 5, 2, 8, 4}, the result is
 *            {3, 5, 8}
 *            
 * @author ruifengm
 * @since 2018-Apr-15
 *
 */

public class LongestIncreasingSubsequence extends FunIntAlgorithm {
	
	/**
	 * Let's try to solve the problem using brute-force first.
	 * 1) Find all subsequences 
	 * 2) Filter out those in ascending order
	 * 3) Find the one with the highest length
	 * 
	 * Time complexity: O(2^n) since the total number of subsequences is 2^n.
	 */
	private static ArrayList<Stack<Integer>> bruteForceFindLIS(int[] a) {
		CopyOnWriteArrayList<ArrayList<Integer>> subseqList = recursiveFindSubSeq(a, a.length);
	    // filter out subsequences not in ascending order
	    Iterator<ArrayList<Integer>> iter = subseqList.iterator(); 
	    while (iter.hasNext()) {
	    	ArrayList<Integer> item = iter.next();
	    	for (int i=0; i<=item.size()-2; i++) {
	    		if (item.get(i) > item.get(i+1)) {
	    			subseqList.remove(item);
	    			break;
	    		}
	    	}
	    }
	    // look for those with maximal length (return all ties)
	    ArrayList<Stack<Integer>> resList = new ArrayList<Stack<Integer>>();
	    int maxLen = Integer.MIN_VALUE;
	    for (ArrayList<Integer> item: subseqList) {
	    	if (item.size() > maxLen) {
	    		maxLen = item.size();
	    		resList.removeAll(resList); // reset
	    	}
	    	if (item.size() == maxLen) {
	    		Stack<Integer> res = new Stack<Integer>(); 
	    		res.addAll(item);
	    		resList.add(res);
	    	}
	    }
	    return resList;
	}
	
	private static CopyOnWriteArrayList<ArrayList<Integer>> recursiveFindSubSeq(int[] a, int s) {
		// use a CopyOnWriteArrayList to avoid concurrentModificationException
		if (s == 0) return new CopyOnWriteArrayList<ArrayList<Integer>>();
		CopyOnWriteArrayList<ArrayList<Integer>> resList = recursiveFindSubSeq(a, s-1);
		for (ArrayList<Integer> res: resList) {
			ArrayList<Integer> newRes = new ArrayList<Integer>();
			newRes.addAll(res);
			newRes.add(a[s-1]);
			resList.add(newRes);
		}
		ArrayList<Integer> res = new ArrayList<Integer>();
		res.add(a[s-1]); 
		resList.add(res);
		return resList;
	}

	/**
	 * The problem can be better approached with DP. 
	 * We keep track of the LIS that can be found at each item of the array. 
	 * Let A(n) denote an integer array of size n, and LIS(A(k)) denote the collection of its longest increasing 
	 * subsequences (ties might exist) that end at a[k-1]. All items of LIS(A(k)) would have the same length. 
	 * 
	 * 		LIS(A(k)).item.size = MAX(LIS(A(j)).item.size) + 1         where 1 <= j < k, and A[j-1] < A[k-1]
	 *      LIS(A(k)).item.size = 1                                    if no such j can be found
	 *
	 * We need to find MAX(LIS(A(k)).item.size) for 1 <= k <= n
	 *
	 */
	private static void recursiveFindLISdP(int[] a, int s, ArrayList<ArrayList<Stack<Integer>>> table) {
		if (s == 1) {
			Stack<Integer> res = new Stack<Integer>();
			res.push(a[s-1]);
			ArrayList<Stack<Integer>> resList = new ArrayList<Stack<Integer>>();
			resList.add(res);
			table.add(resList);
			return; 
		}
		recursiveFindLISdP(a, s-1, table);
		// find matching max
		ArrayList<Stack<Integer>> resList = new ArrayList<Stack<Integer>>();
		int maxLen = Integer.MIN_VALUE;
		ArrayList<Integer> idxList = new ArrayList<Integer>();
		for (int i=0; i<s-1; i++) {
			Stack<Integer> item = table.get(i).get(0);
			if (item.get(item.size()-1) <= a[s-1]) {
				if (maxLen < item.size()) {
					maxLen = item.size();
					idxList.removeAll(idxList); // reset
				} 
				if (maxLen == item.size()) idxList.add(i);
			}
		}
		// populate new results
		if (maxLen != Integer.MIN_VALUE) {
			for (int idx: idxList) {
				for (Stack<Integer> item: table.get(idx)) {
					Stack<Integer> newItem = new Stack<Integer>();
					newItem.addAll(item);
					newItem.push(a[s-1]);
					resList.add(newItem);
				}
			}
		}else {
			Stack<Integer> newItem = new Stack<Integer>();
			newItem.push(a[s-1]);
			resList.add(newItem);
		}
		table.add(resList);
	}
	private static ArrayList<Stack<Integer>> recursiveFindLISdPDriver(int[] a) {
		ArrayList<ArrayList<Stack<Integer>>> table = new ArrayList<ArrayList<Stack<Integer>>>();
		recursiveFindLISdP(a, a.length, table);
		ArrayList<Stack<Integer>> res = new ArrayList<Stack<Integer>>();
		int maxLen = Integer.MIN_VALUE;
		for (ArrayList<Stack<Integer>> item: table) {
			//System.out.println(item);
			if (maxLen < item.get(0).size()) {
				maxLen = item.get(0).size();
				res.removeAll(res); // reset
			}
			if (maxLen == item.get(0).size()) res.addAll(item);
		}
		return res;
	}
	
	@FunctionalInterface
	protected interface IntArrayToArrayListFunction {
		ArrayList<Stack<Integer>> apply(int[] array) throws Exception;  
	}
	
    protected static void runIntArrayFuncAndCalculateTime(String message, IntArrayToArrayListFunction intArrFunc, int[] array) throws Exception {
    	long startTime = System.nanoTime();
    	System.out.printf("%-70s\n", message);
    	for (Stack<Integer> item: intArrFunc.apply(array)) System.out.println(item);
    	long endTime = System.nanoTime();
    	long totalTime = new Long(TimeUnit.MICROSECONDS.convert(endTime - startTime, TimeUnit.NANOSECONDS));
    	DecimalFormat formatter = new DecimalFormat("#,###");
    	System.out.printf("%-70s%s\n\n", "Function execution time in micro-seconds: ", formatter.format(totalTime));
    }
	
	public static void main(String[] args) {
		int[] intArray = genRanIntArr(15, 0, 10);
		//int[] intArray = {9, 4, 8, 6, 3, 5, 0, 3, 4, 9, 10, 7, 12, 5, 2, 12};
		//int[] intArray = {8, 3, 6, 0, 2, 7, 4, 7, 9, 8, 2, 5, 4, 0, 5};
		System.out.println("Welcome to the rabbit hole of longest increasing subsequences!\n"
				+ "The integer array is \n" + Arrays.toString(intArray) + "\n"); 
		
		try {
			runIntArrayFuncAndCalculateTime("[Exponential]                  Longest increasing subsequences:", (int[] a) -> bruteForceFindLIS(a), intArray);
			runIntArrayFuncAndCalculateTime("[Quadratic][Recursive]         Longest increasing subsequences:", (int[] a) -> recursiveFindLISdPDriver(a), intArray);		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("All rabbits gone.");
	}
	
	

}
