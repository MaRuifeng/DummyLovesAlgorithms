package dynamicProgramming;

import java.util.Arrays;

import utils.FunAlgorithm;

/**
 * Given a array of numbers which represents prices of a stock over a range of consecutive days, if it's your first time 
 * entering a stock market and you are only allowed to perform one transaction (i.e. buy and sell one share of the stock), 
 * what is the maximum profit you can get?
 * 
 * This can be generalized as finding MAX(A[j] - A[i]) where j > i for a given array. 
 * 
 * @author ruifengm
 * @since 2018-Apr-4
 */

public class StockProfit extends FunAlgorithm {
	
	/**
	 * An intuitive way that can be quickly thought of is to iterate over the array, at i'th element, find all differences
	 * between the i'th element and its previous elements, compare them with the previously found result at (i-1)'th element
	 * and pick the maximum as current result. 
	 * 
	 * This is no different from finding all results of any element subtracting its previous elements and then find the maximum
	 * value, but such implementation leads to DP way of thinking. 
	 * 
	 * Anyway, a brute-force method without much algorithmic thinking is always dumb. 
	 * 
	 * Time complexity: O(N^2)
	 */
	private static int findMaxProfitDumb(int[] a) {
		int max = a[1] - a[0]; // starting profit
		for (int i=2; i<a.length; i++) 
			for (int j=0; j<=i; j++)
				max = Math.max(max, a[i] - a[j]);
		return max;
	}
	
	/**
	 * Above method can be refined done to O(N) with below DP formula. 
	 * 
	 * We keep track of the maximum difference for each element of the array A from left to right. Let Sol(i) denote the result at the i'th element, then
	 * 		Sol(i) = MAX(A[i] - A[i-1] + Sol(i-1), A[i] - A[i-1])
	 * Reasoning: subtractions of A[i-1] by its all previous elements have been performed in order to find Sol(i-1), suppose the result 
	 *            is found at A[i-1] - A[k], then we know A[k] is the smallest among all previous elements of A[i-1]; to find Sol(i), we don't need to subtract 
	 *            them again as we know for them the max difference must be A[i] - A[k] (i.e. A[i] - A[i-1] + Sol(i-1)), and we just need to compare this value 
	 *            with the new subtraction A[i] - A[i-1] to get Sol(i). Note the greedy nature where a global optimal is expected from local optimals. 
	 *            
	 * The final result if the largest one among the maximum differences we have tracked. 
	 */
	private static int findMaxProfitDP(int[] a) {
		int maxDiff = Integer.MIN_VALUE;
		int curMaxDiff = a[1] - a[0]; // starting profit 
		for (int i=2; i<a.length; i++) {
			curMaxDiff = Math.max(a[i] - a[i-1] + curMaxDiff, a[i] - a[i-1]);
			maxDiff = Math.max(maxDiff, curMaxDiff);
		}
		return maxDiff;
	}
	
	/**
	 * Another method that can be cleverly thought of is to track the current minimum price on previous days for any given day, then the maximum 
	 * profit for that day would be to just subtract the current minimum price from the price of that day. Then we find the largest
	 * from the maximum profits of all days. 
	 * 
	 * Though no DP reasoning is done, the working principle is no much different in being greedy. 
	 */
	private static int findMaxProfitGreedy(int[] a) {
		int minPrice = a[0]; 
		int maxDiff = Integer.MIN_VALUE;
		for (int i=1; i<a.length; i++) {
			minPrice = Math.min(minPrice, a[i-1]);
			maxDiff = Math.max(maxDiff, a[i] - minPrice);
		}
		return maxDiff;
	}
	
	public static void main(String[] args) {
		int[] intArray = genRanIntArr(15);
		//int[] intArray = {2, 8, 2, 5, 9, 3, 4, 2, 1, 7, 9, 8}; // Normal stock price
		//int[] intArray = {1, -5, -1, 6, -3, 6, -3, -1, -4, -3}; // Crazy stock price with negative values! The company pays you for choosing their stock!
		System.out.println("Integer array:" + Arrays.toString(intArray));
		System.out.println("Welcome to the rabbit hole of maximum stock profits!\n"
				+ "The randomly generated integer array is of size " + intArray.length + ".\n"); 
		
		try {
			runIntArrayFuncAndCalculateTime("[Quadratic]              Maximum stock profit:", (int[] a) -> findMaxProfitDumb(a), intArray);
			runIntArrayFuncAndCalculateTime("[Linear][DP]             Maximum stock profit:", (int[] a) -> findMaxProfitDP(a), intArray);
			runIntArrayFuncAndCalculateTime("[Linear][Greedy]         Maximum stock profit:", (int[] a) -> findMaxProfitGreedy(a), intArray);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("All rabbits gone.");
	}


}
