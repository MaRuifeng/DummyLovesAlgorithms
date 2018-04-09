package dynamicProgramming;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

import utils.FunIntAlgorithm;

/**
 * Given a array of numbers which represents prices of a stock over a range of consecutive days, if it's your first time 
 * entering a stock market and you are only allowed to perform one transaction (i.e. buy and sell one share of the stock), 
 * what is the maximum profit you can get?
 * 
 * This can be generalized as finding MAX(A[j] - A[i]) where j > i for a given array A of non-negative numbers.
 * 
 * Variant 1: if the number of transactions is not limited but you are only allowed to be engaged in one transaction 
 *            at a time (must sell before buy again), what's the maximum profit you can get?
 *            
 * Variant 2: if the number of transactions is limited to 2 and you are only allowed to be engaged in one transaction
 *            at a time (must sell before buy again), what's the maximum profit you can get?
 * 
 * @author ruifengm
 * @since 2018-Apr-4
 */

public class StockProfit extends FunIntAlgorithm {
	
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
		if (a == null || a.length == 0) return 0; 
		int max = a[1] - a[0]; // starting profit
		for (int i=2; i<a.length; i++) 
			for (int j=0; j<i; j++) 
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
		if (a == null || a.length == 0) return 0; 
		int maxDiff = Integer.MIN_VALUE;
		int curMaxDiff = 0; // starting profit 
		for (int i=1; i<a.length; i++) {
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
		if (a == null || a.length == 0) return 0; 
		int minPrice = a[0]; 
		int maxDiff = Integer.MIN_VALUE;
		for (int i=1; i<a.length; i++) {
			minPrice = Math.min(minPrice, a[i-1]);
			maxDiff = Math.max(maxDiff, a[i] - minPrice);
		}
		return maxDiff;
	}
	
	/**
	 * If unlimited transactions are allowed, we look for the maximum profit that can be gained until a day when the stock price starts to fall, and start 
	 * a new search in the following days. The sum of such maximum profits will be the total maximum profit we can get. 
	 * 		SUM(MAX(A[j] - A[i])) where 
	 *      1) j > i and 
	 *      2) all such pairs should not overlap (i.e. for a new pair, starting i should be larger than previous ending j) and 
	 *      3) start to look for a new pair when A[k] > A[k+1]
	 */
	private static int findMaxProfitVar1(int[] a) {
		if (a == null || a.length == 0) return 0; 
		int totalProfit = 0; 
		int localMax = Integer.MIN_VALUE;
		int minPrice = a[0]; 
		for (int i=1; i<a.length; i++) {
			minPrice = Math.min(minPrice, a[i-1]);
			localMax = Math.max(localMax, a[i] - minPrice);
			if (i < a.length - 1 && a[i] > a[i+1]) {
				// sum up and start looking for a new pair
				totalProfit += Math.max(0, localMax);
				localMax = Integer.MIN_VALUE;
				minPrice = a[i+1]; 
			}
		}
		totalProfit += Math.max(0, localMax); // add result of last pair
		return totalProfit;
	}
	
	/**
	 * Problem variant 1 can also be solved via below greedy way, but it requires the most number of transactions, while above method requires the least.
	 */
	private static int findMaxProfitVar1Greedy(int[] a) {
		if (a == null || a.length == 0) return 0; 
		int totalProfit = 0; 
		for (int i=1; i<a.length; i++)
			totalProfit += a[i] > a[i-1] ? a[i] - a[i-1] : 0;
		return totalProfit;
	}
	
	/**
	 * Traverse the price array from left to right, and record down the maximum profit that can be gained by ONE transaction before each day (included).
	 * Traverse the price array from right to left, and record down the maximum profit that can be gained by ONE transaction after each day (included).
	 * 
	 * Find the maximum out of the sums of such two profits. 
	 * 
	 */
	private static int findMaxProfitVar2(int[] a) {
		if (a == null || a.length == 0) return 0; 
		int totalProfit = 0;  
		int[] leftMax = new int[a.length];
		int[] rightMax = new int[a.length];
		int localMax = 0;
		
		int minPrice = a[0];
		leftMax[0] = 0; // no transaction, or buy and sell again on the first day
		for (int i=1; i<a.length; i++) {
			minPrice = Math.min(minPrice, a[i-1]);
			localMax = Math.max(localMax, a[i] - minPrice);
			leftMax[i] = localMax;
		}
		
		int maxPrice = a[a.length - 1];
		rightMax[a.length - 1] = 0; // no transaction, or buy and sell again on the last day
		localMax = 0;
		for (int i=a.length - 2; i>=0; i--) {
			maxPrice = Math.max(maxPrice, a[i+1]);
			localMax = Math.max(localMax, maxPrice - a[i]);
			rightMax[i] = localMax;
		}
		
		for (int i=0; i<a.length; i++) {
			totalProfit = Math.max(leftMax[i] + rightMax[i], totalProfit);
		}
		return totalProfit;
	}
	
	public static void main(String[] args) {
		int[] intArray = genRanIntArr(15, 0, 15);
		//int[] intArray = {2, 8, 2, 5, 9, 3, 4, 2, 1, 7, 9, 8}; // Normal stock price
		//int[] intArray = {1, -5, -1, 6, -3, 6, -3, -1, -4, -3}; // Crazy stock price with negative values! The company pays you for choosing their stock!
		//int[] intArray = {-3, -4, -6, -8, -9, -12, -14, -24, -26, -30}; // Even crazier stock price with all descending negative values! The company is doing ultimate charity!
		System.out.println("Integer array:" + Arrays.toString(intArray));
		System.out.println("Welcome to the rabbit hole of maximum stock profits!\n"
				+ "The randomly generated integer array is of size " + intArray.length + ".\n"); 
		
		try {
			runIntArrayFuncAndCalculateTime("[Quadratic]        Maximum stock profit of single transaction:", (int[] a) -> findMaxProfitDumb(a), intArray);
			runIntArrayFuncAndCalculateTime("[Linear][DP]       Maximum stock profit of single transaction:", (int[] a) -> findMaxProfitDP(a), intArray);
			runIntArrayFuncAndCalculateTime("[Linear][Greedy]   Maximum stock profit of single transaction:", (int[] a) -> findMaxProfitGreedy(a), intArray);
			runIntArrayFuncAndCalculateTime("[Linear]           Maximum stock profit of multiple transactions:", (int[] a) -> findMaxProfitVar1(a), intArray);
			runIntArrayFuncAndCalculateTime("[Linear][Greedy]   Maximum stock profit of multiple transactions:", (int[] a) -> findMaxProfitVar1Greedy(a), intArray);
			runIntArrayFuncAndCalculateTime("[Linear]          Maximum stock profit of at most 2 transactions:", (int[] a) -> findMaxProfitVar2(a), intArray);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("All rabbits gone.");
	}


}
