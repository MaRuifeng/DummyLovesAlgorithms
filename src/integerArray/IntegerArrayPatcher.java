package integerArray;

import java.util.ArrayList;
import java.util.Arrays;

import utils.FunIntAlgorithm;

/**
 * Given an array of positive integers and fixed number N, determine the minimum number of 
 * elements to be patched to the array, such that every number from 1 to N can be formed by
 * summing up some elements in the Array. 
 *  
 * @author ruifengm
 * @since 2018-May-8
 * 
 * Leetcode link:
 * https://leetcode.com/problems/patching-array/description/
 * 
 * List of possible solutions: 
 * https://blog.csdn.net/qq508618087/article/details/51042428
 * http://www.zrzahid.com/patching-array/
 */
public class IntegerArrayPatcher extends FunIntAlgorithm {
	
	/**
	 * We can try to get some idea from the DP tabulation method of subset sum check. 
	 * Given an array of {1, 2} and a fixed sum of 7, to determine whether there is a subset
	 * whose sum is 7, the DP lookup table looks like below. 
	 *             0     1     2      3      4       5      6      7       <-- sum
	 *  {}    0 [true, false, false, false, false, false, false, false]
     *  {1}   1 [true, true, false, false, false, false, false, false]
     *  {1,2} 2 [true, true, true, true, false, false, false, false]
     * The last row of the table indicates whether the sum values from 0 to 7 can be formed 
     * by summing up a subset of {1, 2}. 
     * Hence the solution to the original problem is to find the minimum number of new elements to be added
     * to {1, 2} such that the last row of the table becomes all true. 
     * We can iteratively look for the longest leftmost true list and take its size as a patch item.  
	 */
	private static int getMinPatchSizeDP(int[] a, int sum) {
		boolean[][] DPLookUp = new boolean[a.length+1][sum+1];
		// base state
		for (int i=0; i<=a.length; i++) DPLookUp[i][0] = true; 
		for (int i=1; i<=sum; i++) DPLookUp[0][i] = false; 
		// proliferation
		for (int i=1; i<=a.length; i++) {
			for (int j=1; j<=sum; j++) {
				if (j < a[i-1]) DPLookUp[i][j] = DPLookUp[i-1][j]; 
				else DPLookUp[i][j] = DPLookUp[i-1][j] || DPLookUp[i-1][j-a[i-1]];
			}
		}
//		for (boolean[] row: DPLookUp) System.out.println(Arrays.toString(row));
//		System.out.println();
		ArrayList<Integer> patch = new ArrayList<Integer>();
		int k = 1; 
		while (k<=sum) {
			if (DPLookUp[a.length][k] == false) {
				// patch item found
				if (sum+1 - k < k) patch.add(sum+1 - k);
				else patch.add(k);
				// apply patch item
				ArrayList<Integer> idxList = new ArrayList<Integer>();
				for (int m=k; m<=sum; m++) 
					if (DPLookUp[a.length][m] == false && DPLookUp[a.length][m-k] == true)
						idxList.add(m); // lookup
				for (Integer idx: idxList) DPLookUp[a.length][idx.intValue()] = true; // update
				//System.out.println(Arrays.toString(DPLookUp[a.length]));
			}
			k++;
		}
		System.out.println("A possible patch: " + patch.toString());
		return patch.size();
	}
	
	/**
	 * Inspired by the DP method, we try to find a simpler implementation. 
	 * Given N = 8, and an empty initial array, the patch can be found as below with the assistance of 
	 * a boolean array. 
	 *              0     1     2      3      4       5      6      7      8        <-- N
	 * {}        [true, false, false, false, false, false, false, false, false]      // empty result array
	 * {1}       [true, true,  false, false, false, false, false, false, false]      // add element 1, bringing coverage to the 2nd place
	 * {1,2}     [true, true,  true,  true,  false, false, false, false, false]      // add element 2, bringing coverage to the 4th place
	 * {1,2,4}   [true, true,  true,  true,  true,  true,  true,  true,  false]      // add element 4, bringing coverage to the 8th place
	 * {1,2,4,1} [true, true,  true,  true,  true,  true,  true,  true,  true ]      // number of remaining 'false' less than the number of 
	 *                                                                               // 'true', so we just wield the power as necessary by adding 
	 *                                                                               // element 1, which is equal to the number of remaining 'false'
	 * Result array: {1,2,4,1}
	 * 
	 * Now with a non-empty initial array, we need to consider its current smallest number during each iteration. 
	 * If the number is smaller than or equal to the element that is supposed to be patched, we need to take that number instead of patching. 
	 * So for the same case above, suppose we have an initial array of {3}, the iterations will be like
	 *              0     1     2      3      4       5      6      7      8        <-- N
	 * {}        [true, false, false, false, false, false, false, false, false]      // empty result array
	 * {1}       [true, true,  false, false, false, false, false, false, false]      // add element 1, bringing coverage to the 2nd place
	 * {1,2}     [true, true,  true,  true,  false, false, false, false, false]      // add element 2, bringing coverage to the 4th place
	 * {1,2,3}   [true, true,  true,  true,  true,  true,  true,  false, false]      // supposed to add element 4, but the initial array contains 3, 
	 *                                                                               // so add 3, bringing coverage to the 7th place
	 * {1,2,3,2} [true, true,  true,  true,  true,  true,  true,  true,  true ]      // number of remaining 'false' less than the number of 
     *                                                                               // 'true', so we just wield the power as necessary by adding 
	 *                                                                               // element 2, which is equal to the number of remaining 'false'
	 * 
	 * Result array: {1,2,3,2}
	 */
	private static int getMinPatchSize(int[] a, int sum) {
		// sort the array for ease of access
		quickSort(a, 0, a.length-1);
		// find patch
		ArrayList<Integer> patch = new ArrayList<Integer>();
		int i = 0; // initial array index
		int maxCoverage = sum + 1;
		int coverage = 1; // the case where sum == 0 is covered by an empty solution set
		while (coverage < maxCoverage) {
			if (i<a.length && coverage >= a[i]) coverage += a[i++];
			else {
				if (maxCoverage - coverage < coverage) {
						patch.add(maxCoverage - coverage); 
						coverage += (maxCoverage - coverage);
				} else {
						patch.add(coverage); 
						//coverage += coverage;
						coverage<<=1; // use bitwise operation to perform multiplication by 2, which is the base of the binary number system
				}
			}
		}
		System.out.println("A possible patch: " + patch.toString());
		return patch.size();
	}
	/**
	 * Modify above method to calculate patch size only. 
	 */
	private static int getMinPatchSizeOnly(int[] a, int sum) {
		// sort the array for ease of access
		quickSort(a, 0, a.length-1);
		// find patch
		int count = 0; 
		int i = 0; // initial array index
		int coverage = 1; // the case where sum == 0 is covered by an empty solution set
		while (coverage < sum) {
			if (i<a.length && coverage >= a[i]) coverage += a[i++];
			else {
				coverage<<=1; // use bitwise operation to perform multiplication by 2, which is the base of the binary number system
				count++;
			}
		}
		return count;
	}

	
	public static void main(String[] args) {
		//int[] intArray = genRanIntArr(5, 120, 300);
		int[] intArray = {9, 2, 15, 19, 20, 21, 24, 86, 98, 73, 56, 18, 10, 63, 120, 156, 148, 206, 293, 178, 146};
		//int[] intArray = {};
		int sum = 270; 
//		int[] intArray = {3}; 
//		int sum = 8; 
//		int[] intArray = {}; 
//		int sum = 6;
//		int[] intArray = {100}; 
//		int sum = 7;
//		int[] intArray = {}; 
//		int sum = 1;
//		int[] intArray = {}; 
//		int sum = 0;
//		int[] intArray = {1, 5, 10}; 
//		int sum = 20;
//		int[] intArray = {3, 7}; 
//		int sum = 14;
//		int[] intArray = {1, 4, 10}; 
//		int sum = 50;
		System.out.println("Welcome to the rabbit hole of subset sums!\n"
				+ "The integer set is \n" + Arrays.toString(intArray) + "\n"
				+ "The sum value is " + sum + ".\n"); 
		
		try {
			runIntArrayFuncAndCalculateTime("[DP]          Size of the patch: ", (int[] a, int s) -> getMinPatchSizeDP(a, s), intArray, sum);
			runIntArrayFuncAndCalculateTime("[DP Inspired] Size of the patch: ", (int[] a, int s) -> getMinPatchSize(a, s), intArray, sum);
			runIntArrayFuncAndCalculateTime("[Greedy]      Size of the patch: ", (int[] a, int s) -> getMinPatchSizeOnly(a, s), intArray, sum);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("All rabbits gone.");
	}
}
