package dynamicProgramming;

import java.util.ArrayList;
import java.util.Arrays;

import utils.FunIntAlgorithm;

/**
 * Given a row of even number of coins of different values, a user plays a game against an 
 * equally-clever opponent by alternating turns. In each turn, a player selects either the first
 * or the last coin from the row, removes it permanently and receives the value of the coin. 
 * 
 * Determine the maximum possible amount of money the user gets to receive it he/she moves first,
 * also in which case the user wins the game. 
 * 
 * Example: an easy case would be that the coins are sorted in ascending order so the user just needs 
 *          to choose the last element in each turn. 
 *          for {1, 2, 3, 4}, the result is 6.
 *          
 * When the number of coins is odd, the user might not get the privilege to win the game by starting first. 
 * 
 * @author ruifengm
 * @since 2018-Apr-29
 * 
 * https://www.geeksforgeeks.org/dynamic-programming-set-31-optimal-strategy-for-a-game/
 *
 */
public class CoinGameStrategy extends FunIntAlgorithm {
	
	/**
	 * Let A(n) be an array of coin values, and n is even. Let Sol(A(n), (0..n-1)) be the maximum gain the user could get, and 
	 * S be the total sum of all coin values.
	 * We try to address the problem via recursion by finding the sub-problem pattern. 
	 * The game takes n/2 turns to complete, and Sol(A(n-2)) is the maximum gain the user is able to attain in the 
	 * following (n/2 - 1) turns, then
	 * for Sol(A(n), (0..n-1)), there are 4 possibilities. 
	 * 		Possibility 1: Sol(A(n-2), (1..n-2)) + A[n-1]    // user selects A[n-1], and opponent selects A[0]
	 * 		Possibility 2: Sol(A(n-2), (0..n-3)) + A[n-1]    // user selects A[n-1], and opponent selects A[n-2]
	 * 		Possibility 3: Sol(A(n-2), (1..n-2)) + A[0]      // user selects A[0], and opponent selects A[n-1]
	 * 		Possibility 4: Sol(A(n-2), (2..n-1)) + A[0]      // user selects A[0], and opponent selects A[1]
	 * 
	 * Note that the opponent also gets to choose to maximize his/her gain after the user moves. So there are conditions for each option to occur. 
	 * 
	 * Condition for Possibility 1 to occur: S - Sol(A(n-2), (1..n-2))     >       S - Sol(A(n-2), (0..n-3))  <==>  Sol(A(n-2), (1..n-2)) <= Sol(A(n-2), (0..n-3))
	 * Condition for Possibility 2 to occur: S - Sol(A(n-2), (1..n-2))     <=      S - Sol(A(n-2), (0..n-3))  <==>  Sol(A(n-2), (1..n-2)) >  Sol(A(n-2), (0..n-3))
	 * Condition for Possibility 3 to occur: S - Sol(A(n-2), (1..n-2))     >       S - Sol(A(n-2), (2..n-1))  <==>  Sol(A(n-2), (1..n-2)) <= Sol(A(n-2), (2..n-1))
	 * Condition for Possibility 4 to occur: S - Sol(A(n-2), (1..n-2))     <=      S - Sol(A(n-2), (2..n-1))  <==>  Sol(A(n-2), (1..n-2)) >  Sol(A(n-2), (2..n-1))
	 * 
	 * So we just need to get MAX(occurrence of either Possibility 1 or 2, occurrence of either Possibility 3 or 4) based on the condition checks.
	 */
	private static long recursiveMaxGain(int[] a, int start, int end) throws Exception {
		if (end <= start) throw new Exception("Array start position must be smaller than end position.");
		if ((end-start) == 1) return Math.max(a[end], a[start]); // last turn to end of game
		long curTurnResOpt1, curTurnResOpt2; // option 1 for selecting the current last coin, option 2 for selecting the current first coin
		long nextTurnResOpt1 = recursiveMaxGain(a, start + 1, end - 1); 
		long nextTurnResOpt2 = recursiveMaxGain(a, start, end - 2); 
		long nextTurnResOpt3 = recursiveMaxGain(a, start + 2, end); 
		if (nextTurnResOpt1 <= nextTurnResOpt2) curTurnResOpt1 = nextTurnResOpt1 + a[end]; 
		else curTurnResOpt1 = nextTurnResOpt2 + a[end];
		if (nextTurnResOpt1 <= nextTurnResOpt3) curTurnResOpt2 = nextTurnResOpt1 + a[start];
		else curTurnResOpt2 = nextTurnResOpt3 + a[start];
		return Math.max(curTurnResOpt1, curTurnResOpt2);
	}
	private static long recursiveMaxGainDriver(int[] a) throws Exception {
		if (a.length % 2 != 0) throw new Exception ("The coin array must contain even number of coins.");
		return recursiveMaxGain(a, 0, a.length - 1);
	}
	
	/**
	 * Avoid repeated computations with via a look up table constructed via DP memoization.
	 */
	private static long recursiveMaxGainDPMemo(int[] a, int start, int end, long[][] table) throws Exception {
		if (table[start][end] != -1) return table[start][end];
		else {
			if (end <= start) throw new Exception("Array start position must be smaller than end position.");
			if ((end-start) == 1) table[start][end] = Math.max(a[end], a[start]); // last turn to end of game
			else {
				long curTurnResOpt1, curTurnResOpt2; // option 1 for selecting the current last coin, option 2 for selecting the current first coin
				long nextTurnResOpt1 = recursiveMaxGainDPMemo(a, start + 1, end - 1, table); 
				long nextTurnResOpt2 = recursiveMaxGainDPMemo(a, start, end - 2, table); 
				long nextTurnResOpt3 = recursiveMaxGainDPMemo(a, start + 2, end, table); 
				if (nextTurnResOpt1 <= nextTurnResOpt2) curTurnResOpt1 = nextTurnResOpt1 + a[end]; 
				else curTurnResOpt1 = nextTurnResOpt2 + a[end];
				if (nextTurnResOpt1 <= nextTurnResOpt3) curTurnResOpt2 = nextTurnResOpt1 + a[start];
				else curTurnResOpt2 = nextTurnResOpt3 + a[start];
				table[start][end] = Math.max(curTurnResOpt1, curTurnResOpt2);
			}
			return table[start][end];
		}
	}
	private static long recursiveMaxGainDPMemoDriver(int[] a) throws Exception {
		if (a.length % 2 != 0) throw new Exception ("The coin array must contain even number of coins.");
		long[][] DPLookUp = new long[a.length][a.length];
		for (long[] row: DPLookUp) Arrays.fill(row, -1);
		long res = recursiveMaxGainDPMemo(a, 0, a.length - 1, DPLookUp);
		//for (long[] row: DPLookUp) System.out.println(Arrays.toString(row));
		return res;
	}
	
	/**
	 * Avoid repeated computations with via a look up table constructed via DP tabulation.
	 */
	private static long iterativeMaxGainDPTabu(int[] a) throws Exception {
		if (a.length == 0 || a.length % 2 != 0) throw new Exception ("The coin array must NOT be empty and must contain even number of coins.");
		long[][] DPLookUp = new long[a.length][a.length]; 
		for (int j=1; j<=a.length-1; j++) { 
			for (int i=j-1; i>=0; i-=2) { // check all valid sub-arrays
				if ((j-i) == 1) DPLookUp[i][j] = Math.max(a[i], a[j]);
				else {
					long opt1, opt2; 
					if (DPLookUp[i+1][j-1] <= DPLookUp[i][j-2]) opt1 = DPLookUp[i+1][j-1] + a[j]; 
					else opt1 = DPLookUp[i][j-2] + a[j];
					if (DPLookUp[i+1][j-1] <= DPLookUp[i+2][j]) opt2 = DPLookUp[i+1][j-1] + a[i];
					else opt2 = DPLookUp[i+2][j] + a[i];
					DPLookUp[i][j] = Math.max(opt1, opt2);
				}
			}
		}
		//for (long[] row: DPLookUp) System.out.println(Arrays.toString(row));
		return DPLookUp[0][a.length-1];
	}
	
	@SuppressWarnings("unchecked")
	private static void iterativeListCoinsForMaxGainDPTabu(int[] a) throws Exception {
		if (a.length == 0 || a.length % 2 != 0) throw new Exception ("The coin array must NOT be empty and must contain even number of coins.");
		ArrayList<Integer>[][] DPLookUp = new ArrayList[a.length][a.length];
		for (int i=0; i<a.length; i++) {
			for (int j=0; j<a.length; j++) DPLookUp[i][j] = new ArrayList<Integer>();
		}
		for (int j=1; j<=a.length-1; j++) { 
			for (int i=j-1; i>=0; i-=2) { // check all valid sub-arrays
				if ((j-i) == 1) DPLookUp[i][j].add(Math.max(a[i], a[j]));
				else {
					long opt1, opt2; 
					long sum1 = sumArrayList(DPLookUp[i+1][j-1]);
					long sum2 = sumArrayList(DPLookUp[i][j-2]);
					long sum3 = sumArrayList(DPLookUp[i+2][j]);
					ArrayList<Integer> opt1List = new ArrayList<Integer>();
					ArrayList<Integer> opt2List = new ArrayList<Integer>();
					if (sum1 <= sum2) {
						opt1 = sum1 + a[j]; 
						opt1List.addAll(DPLookUp[i+1][j-1]);
						opt1List.add(a[j]);
					}
					else {
						opt1 = sum2 + a[j];
						opt1List.addAll(DPLookUp[i][j-2]);
						opt1List.add(a[j]);
					}
					if (sum1 <= sum3) {
						opt2 = sum1 + a[i];
						opt2List.addAll(DPLookUp[i+1][j-1]);
						opt2List.add(a[i]);
					}
					else {
						opt2 = sum3 + a[i];
						opt2List.addAll(DPLookUp[i+2][j]);
						opt2List.add(a[i]);
					}
					if (opt1 > opt2) DPLookUp[i][j].addAll(opt1List);
					else DPLookUp[i][j].addAll(opt2List);
				}
			}
		}
		System.out.println("The winning selections of the user are:");
		ArrayList<Integer> winList = DPLookUp[0][a.length-1];
		long userSum = sumArrayList(winList); 
		long opponentSum = sumArray(a) - userSum;
		System.out.println(winList.toString());
		System.out.println("User gains " + userSum);
		System.out.println("Opponent gains " + opponentSum);
		System.out.println("### Showing the game rounds ### ");
		int start = 0, end = a.length - 1, round = 1, i = winList.size()-1;
		while (i>=0) {
			System.out.println("[Round " + round + "]");
			System.out.println("User selects " + winList.get(i));
			if (winList.get(i).intValue() == a[start]) {
				start++; 
				if (i==0) {
					System.out.println("Opponent selects " + a[end]); 
					i--; 
				} else {
					i--;
					if (winList.get(i).intValue() == a[end-1] || winList.get(i).intValue() == a[start]) System.out.println("Opponent selects " + a[end--]);
					else if (winList.get(i).intValue() == a[start+1] || winList.get(i).intValue() == a[end]) System.out.println("Opponent selects " + a[start++]);
				}
			} else if (winList.get(i).intValue() == a[end]) {
				end--;
				if (i==0) {
					System.out.println("Opponent selects " + a[start]); 
					i--; 
				}
				else {
					i--; 
					if (winList.get(i).intValue() == a[start+1] || winList.get(i).intValue() == a[end]) System.out.println("Opponent selects " + a[start++]);
					else if (winList.get(i).intValue() == a[end-1] || winList.get(i).intValue() == a[start]) System.out.println("Opponent selects " + a[end--]);
				}
			}
			round++; 
		}
		System.out.println();
	}
	private static long sumArrayList(ArrayList<Integer> list) {
		long res = 0;
		for (Integer i: list) res += i;
		return res;
	}
	private static long sumArray(int[] a) {
		long res = 0; 
		for (int i: a) res += i;
		return res;
	}
	
	public static void main(String[] args) {
		int[] intArray = genRanIntArr(38, 0, 19);
		//int[] intArray = {2, 1, 3, 4, 7, 5};
		//int[] intArray = {1, 2, 3, 4, 5, 7};
		//int[] intArray = {2, 2, 2, 2, 2, 2};
		//int[] intArray = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		System.out.println("Welcome to the rabbit hole to play the coin collecting game!\n"
				+ "The coin set is \n" + Arrays.toString(intArray) + "\n"); 
		
		try {
			runIntArrayFuncAndCalculateTime("[Recursion][Exponential]          Max gain for moving first: ", (int[] a) -> recursiveMaxGainDriver(a), intArray);
			runIntArrayFuncAndCalculateTime("[Recursion][DP Memo]              Max gain for moving first: ", (int[] a) -> recursiveMaxGainDPMemoDriver(a), intArray);
			runIntArrayFuncAndCalculateTime("[Iteration][DP Tabu]              Max gain for moving first: ", (int[] a) -> iterativeMaxGainDPTabu(a), intArray);
		    iterativeListCoinsForMaxGainDPTabu(intArray);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("All rabbits gone.");
	}

}
