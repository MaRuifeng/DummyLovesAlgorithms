package dynamicProgramming;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;

import utils.FunIntAlgorithm;

/**
 * A coin keeper has three types of coins whose values are 1, 2, 5, 10 respectively, and he possesses infinite number of 
 * such coins (rich as the person who has infinite number of dollars). 
 * 
 * One day he gets really fed up with the numerous coins he has, and decides to change them into higher value
 * paper notes. With a finite amount of paper notes offered by the bank, he is faced with two problems: 
 * 
 * 1) How many ways are there to pick up coins that can sum up to the total value of the paper notes? 
 * 2) Can all the possible combinations be listed out?
 * 3) Out of them which is the best combination that involves the least number of coins to be picked up?
 * 
 * 
 * This is a very typical coin-change problem discussed everywhere on DP. 
 * https://www.geeksforgeeks.org/dynamic-programming-set-7-coin-change/
 * 
 * Similar problem
 * https://stackoverflow.com/questions/4632322/finding-all-possible-combinations-of-numbers-to-reach-a-given-sum
 *  
 * @author ruifengm
 * @since 2015-Mar-17
 */
public class CoinKeeper extends FunIntAlgorithm {
	// private static final int[] coinArr = {5, 2, 1, 10, 25, 50};
	private static final int[] coinArr = {5, 2, 1, 10};
	private static final int NIL = -1;

	/**
	 * It's not easy but still possible to spot below two properties that call for recursion. 
	 * 1) The total set of solutions (coin combinations) can be divided into below two sets
	 *    a) A solution set whose members do not contain the last coin in the coin type array
	 *    b) A solution set whose members contain at least one piece of the last coin in the coin type array
	 * 2) The base cases are
	 *    a) Solution set has only one member when the given sum is 0, i.e. zero coins to be picked (0*1 + 0*2 + 0*5 + 0*10 + 0*25 + 0*50 = 0) 
	 *    b) Solution set is empty when the given sum is less than 0, i.e. no coin pick is applicable
	 *    c) Solution set is empty when the coin array is empty, i.e. no coin pick is applicable
	 *
	 * Defining below variables,  
	 * 		CS - count of solutions
	 *		m - number of coins
	 * 		Cm - value of m'th coin
	 * 		n - sum to be reached
	 * 		C[] - coin array
	 * and the problem can be formulated as below.
	 * 		CS(C[], m, n) = CS(C[], m-1, n) + CS(C[], m, n-Cm)
	 * @param coinCount, sum
	 * @return
	 */
	private static long recursivePick(int coinCount, int sum) {
		// base cases
		if (sum == 0) return 1; 
		if (sum < 0) return 0; 
		if (coinCount <= 0) return 0; 
		// recursion
		return recursivePick(coinCount - 1, sum) + recursivePick(coinCount, sum - coinArr[coinCount - 1]); 
	}
	
	private static long recursivePickDriver(int sum) {
		return recursivePick(coinArr.length, sum);
	}
	
	/**
	 * The pure recursive call involves sub-problems that are computed repeatedly. 
	 * We can use the DP strategy to store the sub-problem results. 
	 * Since there are two dimensions involved in recursion (coin type count and money sum), a 2-D array is needed.
	 * This method builds it in a top-down manner using memoization, and brings the complexity down to N*k. 
	 * @param coinCount
	 * @param sum
	 * @return
	 */
	private static long recursivePickDPMemoIn2DArray(long[][] table, int coinCount, int sum) {
		// base cases
		if (sum == 0) {
			for (int j=0; j<table[0].length; j++) table[0][j] = 1;
			return 1;
		}
		if (sum < 0) return 0; 
		if (coinCount <= 0) return 0; 
		// recursion
		if ((coinCount >= 2) && table[sum][coinCount - 2] > 0 && (sum - coinArr[coinCount - 1]) >= 0 && table[sum - coinArr[coinCount - 1]][coinCount - 1] > 0)
			table[sum][coinCount - 1] = table[sum][coinCount - 2] + table[sum - coinArr[coinCount - 1]][coinCount - 1]; 
		else table[sum][coinCount - 1] = recursivePickDPMemoIn2DArray(table, coinCount - 1, sum) + recursivePickDPMemoIn2DArray(table, coinCount, sum - coinArr[coinCount - 1]);
		// System.out.println("Solution for coin count " + coinCount + " and sum " + sum + ": " + table[sum][coinCount - 1]);
		return table[sum][coinCount - 1];
	}
	private static long recursivePickDPMemoIn2DArrayDriver(int sum) {
		long[][] table = new long[sum+1][coinArr.length]; // one more row to cater for sum = 0
			                                              // storing number of solutions for {sum = i, coinCount = j}
		long result = recursivePickDPMemoIn2DArray(table, coinArr.length, sum);
		// for(long[] item: table) System.out.println(Arrays.toString(item)); // print out the solution array for inspection                      
		return result;
	}
	
	/**
	 * The DP memoization in 2D array is not good enough as it gives O(N*k) space complexity.
	 * If we print out the 2D array once the recursive calls are completed, we can see that a lot of fields are 0 indicating
	 * that the solution count for those cases were not computed at all since they were not needed for the final solution count. 
	 * We try whether we can reduce the space complexity down to O(N) by using 1-D array to store the intermediate results. 
	 * 
	 * This will require the DP model to be formed in a bottom up manner using tabulation because recursion requires both the 
	 * coinCount dimension and the sum dimension to check whether to go for next recursive calls or get result from the lookup table. 
	 * 
	 * @param table, coinCount, sum
	 * @return
	 */
	private static long iterativePickDPTabuIn1DArray(long[] table, int sum) {
		// base state when sum is zero
		table[0] = 1;
		// bottom up
		for (int i=0; i<coinArr.length; i++) { // starting coin count from one type (first element in coinArray) available onwards
			for (int j=0; j<=sum; j++) {  
				if (j >= coinArr[i]) table[j] = table[j] + table[j-coinArr[i]]; 
				// solution count for sum value of j at current 
				// coin count (i'th iteration)  =  solution count for sum value of j at previous coin count + 
			    //                                 solution count for sum value of j-coinArr[i] at current coin count 
				// (because j-coinArr[i] < j, so table[j-coinArr[i]] at current coin count has already been computed when referenced here)
			}
		}
		return table[sum];
	}
	private static long iterativePickDPTabuIn1DArray_optimized(long[] table, int sum) {
		// base state when sum is zero
		table[0] = 1;
		// bottom up
		for (int i=0; i<coinArr.length; i++) // starting coin count from one type (first element in coinArray) available onwards
			for (int j=coinArr[i]; j<=sum; j++)  
				table[j] = table[j] + table[j-coinArr[i]]; 
		return table[sum];
	}
	private static long iterativePickDPTabuIn1DArrayDriver(int sum) {
		long[] table = new long[sum+1]; // one more row to cater for sum = 0
		                                // storing number of solutions for {sum = i, coinCount = coinArr.length}
		Arrays.fill(table, 0); // not necessary though as Java pre-fills any initialized no-value array with zero
		return iterativePickDPTabuIn1DArray(table, sum);
	}
	
	/**
	 * It's also possible to form the tabulation in a 2-D array, with a bit lengthy code and more space complexity. 
	 * But the code itself is much easier to understand. 
	 * 
	 * This time the 2-D array will be fully populated. 
	 * 
	 * @param table, sum
	 * @return
	 */
	private static long iterativePickDPTabuIn2DArray(long[][] table, int sum) {
		// base state when sum is zero 
		Arrays.fill(table[0], 1);
		
		// base state when coin array size is 1
		for (int j=0; j<=sum; j++) {
			if (j >= coinArr[0] &&  (j % coinArr[0]) == 0) table[j][0] = 1;
		}
		
		// bottom up
		for (int i=1; i<coinArr.length; i++) {
			for (int j=0; j<=sum; j++) {
				if (j >= coinArr[i]) table[j][i] = table[j][i-1] + table[j-coinArr[i]][i];
				else table[j][i] = table[j][i-1] + 0;
				//System.out.println("Solution for coin count " + (i + 1) + " and sum " + j + ": " + table[j][i]);
			}
		}
		// for(long[] item: table) System.out.println(Arrays.toString(item)); // print out the solution array for inspection 
		return table[sum][coinArr.length - 1];
		
	}
	private static long iterativePickDPTabuIn2DArrayDriver(int sum) {
		long[][] table = new long[sum+1][coinArr.length]; // one more row to cater for sum = 0
		                                // storing number of solutions for {sum = i, coinCount = coinArr.length}
		for (long[] row: table) Arrays.fill(row, 0);  // not necessary though as Java pre-fills any initialized no-value array with zero
		return iterativePickDPTabuIn2DArray(table, sum);
	}
	
	/**
	 * The possible coin combinations of a given sum can be obtained recursively. 
	 * 
	 * Let's reduce the coin keeper's coin array to {1, 2, 5} only. 
	 * Let A denote the given sum, let Sol(A) denote the set of all combinations of coin 1, 2 and 5, each of which sums up to A, and let U denote the set union symbol.
	 * Let's first convince ourselves this case that if one coin 2 is taken out from members of Sol(A) that contain coin 2, those members
	 * will form the complete solution set for A-2, which is Sol(A-2). 
	 * 
	 * [Proof by Contradiction]
	 * Suppose those members do not form the complete solution set for A-2, then there will be at least one more solution. If we add 2 to that solution, it will form
	 * a new solution for A, which contradicts the fact the whole solution set of A is already known. 
	 * 
	 * Then we have below formula
	 *      Sol(A) = Sol(A-1) with 1 added to all members  U  Sol(A-2) with 2 added to all members  U  Sol(A-5) with 5 added to all members
	 *      
	 * Java HashSet data structure can be used to carry out the set union operations shown above so as to filter out duplicates when encountered. 
	 * 
	 * The time complexity of this method is expected to be very high due to NP-Complete nature of the problem. 
	 * 
	 * @param sum
	 * @return
	 */
	private static HashSet<ArrayList<Integer>> recursiveGetCoinCombinations(int sum) {
		HashSet<ArrayList<Integer>> solSet = new HashSet<ArrayList<Integer>>();
		// base cases
		if (sum == 0) {
			ArrayList<Integer> sol = new ArrayList<Integer>();
			solSet.add(sol);
			return solSet;
		}
		// recursion
		for (int c: coinArr) {
			if (sum>=c) {
				HashSet<ArrayList<Integer>> sol_c_Set = recursiveGetCoinCombinations(sum - c);
				// Add new coin
				Iterator<ArrayList<Integer>> iter = sol_c_Set.iterator();
				while (iter.hasNext()) {
					ArrayList<Integer> sol_c = iter.next();
					sol_c.add(c);
					Collections.sort(sol_c); // sort the solution array so as to filter out duplicates when adding to HashSet
				}
				// Set union
				for (ArrayList<Integer> sol: sol_c_Set) solSet.add(sol);
			}
		}
		return solSet;
	}

	/**
	 * The least number of coins that can add up to the given sum can be found in a recursive manner. 
	 * 
	 * Let's reduce the coin keeper's coin array to {1, 2, 5} only.
	 * Let A denote the given sum, Sol(A) denote the least number of coins needed for A, and Min denote the 'find minimum' operation. 
	 * For any given coin value of n, Sol(A-n) + 1 will be a possible value for Sol(A), as adding n to the sum of coins in Sol(A-n) gives A. 
	 * So we can easily get below formula
	 *       Sol(A) = Min( Sol(A-1)+1, Sol(A-2)+1, Sol(A-5)+1 )
	 * 
	 * @param sum
	 * @return
	 */
	private static int recursiveGetLeastNumberOfCoins(int sum) {
		if (sum == 0) return 0;
		int sol = Integer.MAX_VALUE;
		boolean sol_found = false;
		for (int c: coinArr) {
			if (sum >= c) {
				int sol_c = recursiveGetLeastNumberOfCoins(sum - c);
				// System.out.println("Solution for sum value " + (sum-c) + ": " + sol_c);
				if (sol >= (sol_c + 1)) {
					sol_found = true;
					sol = sol_c + 1;
				}
			}
		}
		if (!sol_found) sol = 0;
		return sol;
	}
	
	/**
	 * Due to the loop in the pure recursive method, we can easily see that a lot of sub-problems are re-computed. 
	 * 
	 * In this method we use DP memoization to store those values to avoid repeated computations so as to achieve O(N*k).
	 * 
	 * @param sum
	 * @return
	 */
	private static int recursiveGetLeastNumberOfCoinsDPMemo(int[] table, int sum) {
		if (table[sum] != NIL) return table[sum];
		else {
			if (sum == 0) table[sum] = 0;
			else {
				int sol = Integer.MAX_VALUE;
				boolean sol_found = false;
				for (int c: coinArr) {
					if (sum >= c) {
						int sol_c = recursiveGetLeastNumberOfCoinsDPMemo(table, sum - c);
						if (sol >= (sol_c + 1)) {
							sol_found = true;
							sol = sol_c + 1;
						}
					}
				}
				if (!sol_found) sol = 0;
				table[sum] = sol;
			}
			return table[sum];
		}
	}
	private static int recursiveGetLeastNumberOfCoinsDPMemoDriver(int sum) {
		int[] DPLookUp = new int[sum + 1];
		Arrays.fill(DPLookUp, NIL);
		return recursiveGetLeastNumberOfCoinsDPMemo(DPLookUp, sum);
	}
	
	/**
	 * We convert the memoization flow to a bottom-up manner using tabulation.
	 * 
	 * @param table
	 * @param sum
	 * @return
	 */
	private static int iterativeGetLeastNumberOfCoinsDPTabu(int[] table, int sum) {
		table[0] = 0; // base state
		for (int i=0; i<=sum; i++) {
			boolean sol_found = false;
			for (int c: coinArr)  {
				if (i >=c && table[i] >= (table[i-c] + 1)) { 
					table[i] = table[i-c] + 1;
					sol_found = true;
				}
			}
			if (!sol_found) table[i] = 0; 
		}
		return table[sum];
	}
	private static int iterativeGetLeastNumberOfCoinsDPTabuDriver(int sum) {
		int[] DPLookUp = new int[sum + 1];
		Arrays.fill(DPLookUp, Integer.MAX_VALUE);
		return iterativeGetLeastNumberOfCoinsDPTabu(DPLookUp, sum);
	}
	
	/**
	 * An intuitive method to get the least number of coins that add up to the given sum. 
	 * Time complexity is O(klogk + k) because of the merge sort performed on the coin array.
	 * 
	 * 1) Sort the coin array
	 * 2) Start from the largest and calculate number of coins needed
	 * @param sum
	 * @return
	 */
	private static int getLeastNumberOfCoins(int sum) {
		int[] sortedCoinArr = mergeSort(Arrays.copyOf(coinArr, coinArr.length), 0, coinArr.length - 1);
		int count = 0;
		for (int i=sortedCoinArr.length-1; i>=0; i--) {
			count += sum / sortedCoinArr[i];
			sum = sum % sortedCoinArr[i]; // remainder
		}
		return count;
	}
	
	
	
	public static void main(String[] args) {
		int sum = 27;
		System.out.println("Welcome to the rabbit hole of coins!\n"
				+ "The available coin types are " + Arrays.toString(coinArr) + ".\n"
				+ "The amount offered is " + sum + ".\n"); 
		
		try {
			runIntFuncAndCalculateTime("[Recursive][Exponential]     Number of coin combinations for sum " + sum + ":" , (int i) -> recursivePickDriver(i), sum);
			runIntFuncAndCalculateTime("[Recursive][O(N*k)][2D-Memo] Number of coin combinations for sum " + sum + ":" , (int i) -> recursivePickDPMemoIn2DArrayDriver(i), sum);
			runIntFuncAndCalculateTime("[Iterative][O(N*k)][1D-Tabu] Number of coin combinations for sum " + sum + ":" , (int i) -> iterativePickDPTabuIn1DArrayDriver(i), sum);
			runIntFuncAndCalculateTime("[Iterative][O(N*k)][2D-Tabu] Number of coin combinations for sum " + sum + ":" , (int i) -> iterativePickDPTabuIn2DArrayDriver(i), sum);
			runIntFuncAndCalculateTime("[Recursive][Exponential]     Coin combinations for sum " + sum + ":" , (int i) -> recursiveGetCoinCombinations(i), sum);
			runIntFuncAndCalculateTime("[Recursive][Exponential]     Least number of coins for sum " + sum + ":" , (int i) -> recursiveGetLeastNumberOfCoins(i), sum);
			runIntFuncAndCalculateTime("[Recursive][O(N*k)][Memo]    Least number of coins for sum " + sum + ":" , (int i) -> recursiveGetLeastNumberOfCoinsDPMemoDriver(i), sum);
			runIntFuncAndCalculateTime("[Iterative][O(N*k)][Tabu]    Least number of coins for sum " + sum + ":" , (int i) -> iterativeGetLeastNumberOfCoinsDPTabuDriver(i), sum);
			runIntFuncAndCalculateTime("[Iterative][O(klogk + k)]    Least number of coins for sum " + sum + ":" , (int i) -> getLeastNumberOfCoins(i), sum);

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("All rabbits gone.");
	}

}
