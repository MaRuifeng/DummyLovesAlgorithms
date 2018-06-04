package dynamicProgramming;

import java.util.Arrays;
import java.util.LinkedList;

import utils.FunIntAlgorithm;

/**
 * The famous egg dropping puzzle requires the highest floor number
 * to be found out when dropped from which an egg does not break. 
 * 
 * When there is only one egg, the only solution is to start from the first floor onwards. Hence
 * the worst case involves number of droppings as large as the total number of floors. 
 * 
 * What if there are multiple eggs provided? Given n eggs and m floors, 
 * find the least number of egg droppings that is guaranteed to work for all cases. 
 * 
 * An analytical soltuion is given at https://www.geeksforgeeks.org/puzzle-set-35-2-eggs-and-100-floors/
 * 
 * @author ruifengm
 * @since 2018-Jun-4
 * 
 * https://www.geeksforgeeks.org/dynamic-programming-set-11-egg-dropping-puzzle/
 * 
 */
public class EggDropper extends FunIntAlgorithm {
	
	/**
	 * Given n eggs and m floors, we can start dropping the first egg from floor i, where 1 <= i <= m. There will be two cases. 
	 * [Case 1] The egg breaks           -->      We try the remaining floors from 0 to i-1 with remaining n-1 eggs
	 * [Case 2] The egg does not break   -->      We try the remaining floors from i+1 to m with n eggs
	 * We have identified the sub-problem patterns. 
	 * Let Sol(n, m) be the least number of trials needed. At the i'th floor, since we need to take care of the possible worst
	 * case scenario, we take the larger value between Sol(n-1, i-1) and Sol(n, m-i), add 1 to it to form
	 * the Sol(n, i). After we find out such solutions corresponding to every floor, we take minimum as 
	 * the final solution, as we have completed trying all possible ways of dropping eggs. 
	 * 
	 *     Sol(n, m) = MIN (1 + MAX(Sol(n-1, i-1), Sol(n, m-i)) ) for i ranging from 1 to m
	 */
	private static int recursiveGetMinDrops(int n, int m) {
		if (n==1 && m > 0) return m; // need to drop for all floors in worst case
		if (m == 0) return 0; // no need to drop eggs if there is no floor
		int minDrops = Integer.MAX_VALUE;
		for (int i=1; i<=m; i++) {
			int curDrops = 1 + Math.max(recursiveGetMinDrops(n-1, i-1), // egg breaks
					recursiveGetMinDrops(n, m-i));                      // egg does not break
			if (minDrops > curDrops) minDrops = curDrops; 
		}
		return minDrops;
	}
	
	/**
	 * We try to optimize above method with DP memoization and tabulation. 
	 */
	private static int recursiveGetMinDropsDPMemo(int n, int m, int[][] table) {
		if (table[n-1][m] != -1) return table[n-1][m]; 
		else {
			if (m==0) table[n-1][m] = 0; // no need to drop eggs if there is no floor
			else if (n==1) table[n-1][m] = m; // need to drop for all floors in worst case
			else {
				int minDrops = Integer.MAX_VALUE;
				for (int i=1; i<=m; i++) {
					int curDrops = 1 + Math.max(recursiveGetMinDropsDPMemo(n-1, i-1, table), // egg breaks
							recursiveGetMinDropsDPMemo(n, m-i, table));                      // egg does not break
					if (minDrops > curDrops) minDrops = curDrops; 
				}
				table[n-1][m] = minDrops; 
			}
			return table[n-1][m];
		}
	}
	private static int recursiveGetMinDropsDPMemo(int n, int m) {
		int[][] DPLookUp = new int[n][m+1]; 
		for (int[] row: DPLookUp) Arrays.fill(row, -1);
		int res = recursiveGetMinDropsDPMemo(n, m, DPLookUp);
		//for (int[] row: DPLookUp) System.out.println(Arrays.toString(row));
		return res;
	}
	
	private static int iterativeGetMinDropsDPTabu(int n, int m) {
		int[][] table = new int[n][m+1];  // DP lookup table
		// base state
		for (int i=0; i<n; i++) table[i][0] = 0; // no drops needed for zero floor
		for (int j=1; j<=m; j++) table[0][j] = j; // for worst case scenario, need to drop from all floors with one egg
		// DP proliferation
		for (int i=1; i<n; i++) {
			for (int j=1; j<=m; j++) {
				int min = Integer.MAX_VALUE; 
				for (int k=1; k<=j; k++) {
					int cur = 1 + Math.max(table[i-1][k-1], table[i][j-k]); 
					if (min > cur) min = cur;
				}
				table[i][j] = min; 
			}
		}
		//for (int[] row: table) System.out.println(Arrays.toString(row));
		return table[n-1][m];
	}
	
	/** 
	 * We modify the tabulation method trying to find the actual floors from
	 * which the eggs should be dropped. 
	 */
	@SuppressWarnings("unchecked")
	private static LinkedList<Integer> iterativeGetMinDropFloorsDPTabu(int n, int m) {
		LinkedList<Integer>[][] table = new LinkedList[n][m+1];  // DP lookup table
		// base state
		for (int i=0; i<n; i++) table[i][0] = new LinkedList<>(); // no drops needed for zero floor
		for (int j=1; j<=m; j++) {
			LinkedList<Integer> floorList = new LinkedList<>(); 
			for (int k=1; k<=j; k++) floorList.add(k); // for worst case scenario, need to drop from all floors with one egg
			table[0][j] = floorList; 
		}
		// DP proliferation
		for (int i=1; i<n; i++) {
			for (int j=1; j<=m; j++) {
				LinkedList<Integer> minFloorList = new LinkedList<>(); 
				for (int k=1; k<=m; k++) minFloorList.add(k);
				for (int k=1; k<=j; k++) {
					LinkedList<Integer> curFloorList = new LinkedList<>(); 
					curFloorList.add(k);
					if (table[i-1][k-1].size() > table[i][j-k].size()) { // add floors from lower half
						for (int num: table[i-1][k-1]) curFloorList.addFirst(num);
					} else { // add floors from upper half
						for (int num: table[i][j-k]) curFloorList.addLast(num + k);
					}
					if (minFloorList.size() > curFloorList.size()) minFloorList = curFloorList; 
				}
				table[i][j] = minFloorList; 
			}
		}
		return table[n-1][m];
	}
	
	
	public static void main(String[] args) {
		int numberOfEggs = 2, numberOfFloors = 100; 
		System.out.println("Welcome to the rabbit hole of egg droppers! \n"
				+ "There are " + numberOfEggs + " eggs and " + numberOfFloors + " floors.\n" 
				+ "Find the least number of egg dropping trials that work in all cases to determine the floor "
				+ "when dropped from which an egg does not break."); 
		
		try {
//			runIntFuncAndCalculateTime("[Recursive][Exponential]     Least number of trials: ", 
//					(int a, int b) -> recursiveGetMinDrops(a, b), numberOfEggs, numberOfFloors);
			runIntFuncAndCalculateTime("[Recursive][DP Memo]         Least number of trials: ", 
					(int a, int b) -> recursiveGetMinDropsDPMemo(a, b), numberOfEggs, numberOfFloors);
			runIntFuncAndCalculateTime("[Iterative][DP Tabu]         Least number of trials: ", 
					(int a, int b) -> iterativeGetMinDropsDPTabu(a, b), numberOfEggs, numberOfFloors);
			
			System.out.println("List of floors to be tried for minimum number of trials:");
			LinkedList<Integer> floorList = iterativeGetMinDropFloorsDPTabu(numberOfEggs, numberOfFloors); 
			for (Integer num: floorList) System.out.println(num);
			System.out.println("[Note] In the 2-egg scenario, at each floor in the list, if an egg breaks, "
					+ "\nthe remaining trials in worst case does not exceed the found least number of trials minus 1. "
					+ "\nHence it shows that this solution works in call cases."
					+ "\nQuestion: though DP provides rigorous reasoning, how can we still prove the algorithm is correct?\n");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("All rabbits gone.");
	}
}
