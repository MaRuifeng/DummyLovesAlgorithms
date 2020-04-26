package integerArray;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

/**
 * While attempting a series of DP problems, a bunch of them have been found related to 
 * the problem of finding all subsets of a set. Most of them do not explicitly ask to find
 * all subsets exhaustively but only require some min/max conclusions, which makes them DP solvable. 
 * 
 * Yet the problem itself is pretty interesting as a set of size N has 2^N subsets (null set included). 
 * Hence merely a set of size 32 would yield 2^32 subsets, and that is the number of possible addresses of 
 * a 32-bit machine. An individual subset consumes more than one byte, hence such a solution 
 * set can't even be stored into a 32-bit addressable memory of the maximum value of 4GB. 
 * 
 * In this post, a number of possible ways are explored to print all subsets of an integer array.
 * 
 * Below paper discusses a recursive way of generating a sequence of subsets where the 
 * number of elements increases monotonically, which is pretty interesting. 
 * http://applied-math.org/subset.pdf
 * 
 * https://www.lintcode.com/problem/subsets/note/145899
 * https://www.lintcode.com/problem/subsets-ii/note/145909
 * 
 * @author ruifengm
 * @since 2018-Apr-24
 *
 */

public class SubsetSeeker {
	
	/**
	 * Let A(n) be an integer array of size n, and its subsets can be found via below recursive pattern.
	 * To print all subsets of A(n) ==> 
	 * 		              Include element A[n-1]
	 *                        Print all subsets of A(n-1) with A[n-1] appended (with null set included)
	 *                    Exclude element A[n-1]
	 *                        Print all subsets of A(n-1)
	 *                        
	 * Time complexity (by iterations in the print loop): 
	 * 	      0*C(n,0) + 1*C(n,1) + 2*C(n,2) + ... + n*C(n,n) = (n/2)*2^n = n*2^(n-1)  <-- Binomial Theorem
	 *                        
	 * The interesting part of a recursive algorithm is the tricky assumption that the algorithm already 
	 * works well for sub-problems of smaller size. Keep the reasoning simple and don't even try to deduce 
	 * how the compiler is going to unravel the recursive stack calls. 
	 * The important thing is always to properly define a base case and make sure recursion converges to it. 
	 */
	private static void recursivePrintSubset(ArrayList<Integer> solution, ArrayList<Integer> set) {
		if (set.size() == 0) {
			// set is depleted, print solution
			for (Integer i: solution) System.out.print(i + " ");
			System.out.println("");
		} else {
			ArrayList<Integer> newSolution = new ArrayList<Integer>();
			newSolution.addAll(solution);
			newSolution.add(set.get(set.size()-1));
			ArrayList<Integer> newSet = new ArrayList<Integer>();
			newSet.addAll(set);
			newSet.remove(set.get(set.size()-1));
			// include last element of set and recur
			recursivePrintSubset(newSolution, newSet);
			// exclude last element of set and recur
			recursivePrintSubset(solution, newSet);
		}
	}
	private static void recursivePrintSubsetDriver(int[] a) {
		ArrayList<Integer> solution = new ArrayList<Integer>(); // convert to ArrayList for its elasticity
		ArrayList<Integer> set = new ArrayList<Integer>(); // convert to ArrayList for its elasticity
		for (int i: a) set.add(i);
		recursivePrintSubset(solution, set);
	}
	
	/**
	 * Recursion assisted by a boolean operation array which denotes whether a certain element should be printed or not. 
	 * 
	 * Time complexity (by iterations in the print loop): 
	 * 	      n*C(n,0) + n*C(n,1) + n*C(n,2) + ... + n*C(n,n) = n*2^n = n*2^n  <-- Binomial Theorem
	 */
	private static void recursivePrintSubsetWithOpsArray(int[] a, int size, boolean[] ops) {
		if (size == 0) return; // nothing to print
		// including a[size-1]
		ops[size-1] = true;
		recursivePrintSubsetWithOpsArray(a, size - 1, ops);
		//System.out.println(Arrays.toString(ops));
		for (int i=0; i<ops.length; i++) {
			if (ops[i] == true) System.out.print(a[i] + " ");
		}
		System.out.println("");
		// excluding a[size-1]
		ops[size-1] = false;
		recursivePrintSubsetWithOpsArray(a, size - 1, ops);
	}
	private static void recursivePrintSubsetWithOpsArrayDriver(int[] a) {
		int size = a.length;
		boolean[] ops = new boolean[size];
		Arrays.fill(ops, false);
		recursivePrintSubsetWithOpsArray(a, size, ops);
	}
	
	/**
	 * The boolean array can be replaced with a binary number representation. 
	 */
	private static void recursivePrintSubsetWithBitOps(int[] a, int size, int bNum) {
		if (size == 0) return; // nothing to print
		// including a[size-1] ==> Turn on bit at position size-1
		bNum = bNum | (1 << (a.length-size));
		recursivePrintSubsetWithBitOps(a, size - 1, bNum);
		//System.out.println("Binary Number: " + bNum);
		for (int i=0; i<a.length; i++) {
			if ((bNum & (1<<i)) != 0) System.out.print(a[i] + " ");
		}
		System.out.println("");
		// excluding a[size-1] ==> Turn off bit at position size-1
		bNum = bNum & ~(1 << (a.length-size));
		recursivePrintSubsetWithBitOps(a, size - 1, bNum);
	}
	private static void recursivePrintSubsetWithBitOpsDriver(int[] a) {
		recursivePrintSubsetWithBitOps(a, a.length, 0);
	}
	
	/** 
	 * While the above recursive methods are trying to push the printing deep into the function call tree, 
	 * below recursive method pulls out the solutions to the surface. 
	 * 
	 * Though it has the benefit of storing all subsets into a list, the benefit itself is a drawback as 
	 * an ordinary laptop memory might not be able to hold that list if the original set is relatively large.
	 * 
	 * And the time complexity is also larger because of the list operations involved. 
	 */
	private static CopyOnWriteArrayList<ArrayList<Integer>> recursiveFindAndPrintSubset(int[] a, int s) {
		if (s == 0) return new CopyOnWriteArrayList<ArrayList<Integer>>();
		CopyOnWriteArrayList<ArrayList<Integer>> resList = recursiveFindAndPrintSubset(a, s-1); 
		for (ArrayList<Integer> res: resList) {
			ArrayList<Integer> newRes = new ArrayList<>(res);
			newRes.add(a[s-1]);
			for (Integer i: newRes) System.out.print(i + " ");
			System.out.println("");
			resList.add(newRes);
		}
		ArrayList<Integer> cur = new ArrayList<Integer>();
		cur.add(a[s-1]);
		System.out.print(a[s-1] + " ");
		System.out.println("");
		resList.add(cur);
		return resList;
	}
	private static void recursiveFindAndPrintSubsetDriver(int[] a) {
		recursiveFindAndPrintSubset(a, a.length);
	}
	
	/**
	 * Iterate from 0 to 2^n-1, where n is the size of the array, and print out 
	 * items in the array according to the binary bits (1 for print, 0 for skip) in each number.
	 * 
	 * E.g. for array {1, 2, 3}, the subsets are
	 *    7     [1 1 1]     {1, 2, 3}
	 *    6     [1 1 0]     {1, 2}
	 *    5     [1 0 1]     {1, 3}
	 *    4     [1 0 0]     {1}
	 *    3     [0 1 1]     {2, 3}
	 *    2     [0 1 0]     {2}
	 *    1     [0 0 1]     {3}
	 *    0     [0 0 0]     {}
	 */
	private static void iterativePrintSubSet(int[] a) {
		for (int i=0; i<=Math.pow(2, a.length)-1; i++) {
			for (int j=0; j<a.length; j++) {
				if ((i&(1<<j)) != 0) System.out.print(a[j] + " ");
			}
			System.out.println("");
		}
	}
	
	/**
	 * What if the integer array contains duplicates? By observing the recursive call stack of the first method, 
	 * we can see that when a duplicate element is added to the base solution to form a new solution, the next recursive
	 * call with that new solution as a base solution will occur as a duplicate, because the same element had been added to the 
	 * same base solution in higher level of the recursive call stack pending for execution. 
	 * 
	 * We need to look for a way to tell our recursion program when such a duplicate occurs. The idea is to establish a map for each base
	 * solution which takes each array element as a key (unique) and a boolean as the value that indicates whether the array element has been 
	 * added to the base solution or not. Then during each recursion, we check current array element against this map, if it's not added, we recur
	 * with both the base solution and the new solution; otherwise we recur with the base solution only. 
	 * 
	 * The map works in a way very much like the DP lookup table. 
	 * 
	 * Note that as opposed to some other solutions which require the array to be sorted as to eliminate duplicates,
	 * this solution does not need so. 
	 */
	private static void recursivePrintSubSetWithDup(ArrayList<Integer> baseSol, int[] arr, int size, HashMap<Integer, Boolean> table) {
		if (size == arr.length) {
			// array is depleted, print solution
			String subset = "[";
			for (Integer i: baseSol) subset += (i + " ");
			subset = subset.trim() + "]";
			System.out.println(subset);
		}
		else {
			if (!table.get(arr[size])) { // element not added to base solution
				ArrayList<Integer> newSol = new ArrayList<>(baseSol);
				newSol.add(arr[size]); 
				table.put(arr[size], true); // set value in lookup table for current base solution
				recursivePrintSubSetWithDup(baseSol, arr, size+1, table);
				table.put(arr[size], false); // recover the lookup table for new base solution
				recursivePrintSubSetWithDup(newSol, arr, size+1, table);
			} else { // element already added to base solution in a previous recursion
				recursivePrintSubSetWithDup(baseSol, arr, size+1, table);
			}
		}	
	}
	private static void recursivePrintSubSetWithDup(int[] arr) {
		HashMap<Integer, Boolean> lookup = new HashMap<>(); 
		for (int i: arr) lookup.put(i, false);
		recursivePrintSubSetWithDup(new ArrayList<>(), arr, 0, lookup);
	}
	
	/**
	 * If the array is sorted, we can just use a boolean value to indicate
	 * whether the new solution formation should be skipped or not for current base solution.
	 */
	private static void recursivePrintSubSetWithDupAndSortedArr(ArrayList<Integer> baseSol, int[] arr, int size, boolean skip) {
		if (size == arr.length) {
			// array is depleted, print solution
			String subset = "[";
			for (Integer i: baseSol) subset += (i + " ");
			subset = subset.trim() + "]";
			System.out.println(subset);
		}
		else {
			if (skip) { // skip new solution formation due to duplicate
				if (!(size < arr.length-1 && arr[size+1] == arr[size])) skip = false; // stop skipping
				recursivePrintSubSetWithDupAndSortedArr(baseSol, arr, size+1, skip);
			}
			else {
				ArrayList<Integer> newSol = new ArrayList<>(baseSol);
				newSol.add(arr[size]); 
				if (size < arr.length-1 && arr[size+1] == arr[size]) skip = true;
				recursivePrintSubSetWithDupAndSortedArr(baseSol, arr, size+1, skip);
				recursivePrintSubSetWithDupAndSortedArr(newSol, arr, size+1, false); // skip reset for new base solution
			}
		}	
	}
	private static void recursivePrintSubSetWithDupAndSortedArr(int[] arr) {
		Arrays.sort(arr);
		recursivePrintSubSetWithDupAndSortedArr(new ArrayList<>(), arr, 0, false);
	}
	
	@FunctionalInterface
	protected interface IntArrayToVoidFunction {
		void apply(int[] array) throws Exception;  
	}
	
    protected static void runIntArrayFuncAndCalculateTime(String message, IntArrayToVoidFunction intArrFunc, int[] array) throws Exception {
    	long startTime = System.nanoTime();
    	System.out.printf("%-70s\n", message);
    	intArrFunc.apply(array);
    	long endTime = System.nanoTime();
    	long totalTime = new Long(TimeUnit.MICROSECONDS.convert(endTime - startTime, TimeUnit.NANOSECONDS));
    	DecimalFormat formatter = new DecimalFormat("#,###");
    	System.out.printf("%-70s%s\n\n", "Function execution time in micro-seconds: ", formatter.format(totalTime));
    }
	
	public static void main(String[] args) {
		//int[] intArray = {1, 2, 3, 4, 5, 6, 7, 8, 9, 0};
		int[] intArray = {1, 2, 3, 4};
		int[] intArrayWithDuplicates = {1, 2, 1, 1, 2};
		System.out.println("Welcome to the rabbit hole of subset seekers!\n"
				+ "The integer set is \n" + Arrays.toString(intArray) + "\n"); 
		
		try {
			/**
			 * It's worth noting the outputs form some very different shapes yet with mathematical beauty inside. 
			 */
			runIntArrayFuncAndCalculateTime("[Exponential][Simple Recursion][O(n*2^(n-1)]  The subsets are:", 
					(int[] a) -> recursivePrintSubsetDriver(a), intArray);
			runIntArrayFuncAndCalculateTime("[Exponential][Recursion with Boolean Array][O(n*2^n]       The subsets are:", 
					(int[] a) -> recursivePrintSubsetWithOpsArrayDriver(a), intArray);
			runIntArrayFuncAndCalculateTime("[Exponential][Recursion with Bit Operations][O(n*2^n]       The subsets are:", 
					(int[] a) -> recursivePrintSubsetWithBitOpsDriver(a), intArray);
			runIntArrayFuncAndCalculateTime("[Exponential][Recursion with ArrayList]       The subsets are:", 
					(int[] a) -> recursiveFindAndPrintSubsetDriver(a), intArray);
			runIntArrayFuncAndCalculateTime("[Exponential][Iteration with Bit Operations][O(n*2^n]       The subsets are:", 
					(int[] a) -> iterativePrintSubSet(a), intArray);
			runIntArrayFuncAndCalculateTime("[Exponential][Simple Recursion][With Duplicates]       The subsets are:", 
					(int[] a) -> recursivePrintSubSetWithDup(a), intArrayWithDuplicates);
			runIntArrayFuncAndCalculateTime("[Exponential][Simple Recursion][With Duplicates and Sorted Array]   The subsets are:", 
					(int[] a) -> recursivePrintSubSetWithDupAndSortedArr(a), intArrayWithDuplicates);

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("All rabbits gone.");
	}
	
	

}
