package dynamicProgramming;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;


/**
 * Given n dices each with m faces (i.e. the numbers on each dice range from 1 to m), count the 
 * number of situations where the top face values sum up to a fixed value when the dices are 
 * rolled together.
 * 
 * The problem is analogous to the math operations for fixed sum problem.
 * 
 * @author ruifengm
 * @since 2018-May-29
 */
public class DiceRoller {
	
	/**
	 * After rolling, we choose a dice, take its value (m choices), and then subtract that value from 
	 * the sum and continue looking in the remaining dices. 
	 */
	private static long recursiveCountAllDiceCombinations(int n, int m, int sum) {
		if (n == 0) {
			if (sum == 0) return 1; // sum reached, return 1 solution count
			else return 0;
		}
		long count = 0; 
		for (int i=1; i<=m; i++) {
			// no need to check for negative sum
			if (sum >= i) count += recursiveCountAllDiceCombinations(n-1, m, sum-i); 
		}
		return count;
	}
	/**
	 * We try to optimize the above solution via DP memoization and DP tabulation. 
	 */
	private static long recursiveCountAllDiceCombinationsDPMemo(int n, int m, int sum, long[][] table) {
		if (table[n][sum] != -1) return table[n][sum]; 
		else {
			if (n == 0) {
				if (sum == 0) table[n][sum] = 1; 
				else table[n][sum] = 0; 
			} else {
				long count = 0; 
				for (int i=1; i<=m; i++) {
					if (sum >= i) count += recursiveCountAllDiceCombinationsDPMemo(n-1, m, sum-i, table);
				} 
				table[n][sum] = count;
			}
			return table[n][sum];
		}
	}
	private static long recursiveCountAllDiceCombinationsDPMemo(int n, int m, int sum) {
		long[][] DPLookUp = new long[n+1][sum+1]; 
		for (long[] row: DPLookUp) Arrays.fill(row, -1);
		long res = recursiveCountAllDiceCombinationsDPMemo(n, m, sum, DPLookUp); 
		// for (long[] row: DPLookUp) System.out.println(Arrays.toString(row));
		return res;
	}
	
	private static long iterativeCountAllDiceCombinationsDPTabu(int n, int m, int sum) {
		long[][] table = new long[n+1][sum+1]; // DP lookup table
		// base state
		table[0][0] = 1; 
		for (int i=1; i<=n; i++) table[i][0] = 0; // no solution when there are dices but sum is zero
		// fill up the DP lookup table
		for (int i=1; i<=n; i++) {
			for (int j=1; j<=sum; j++) {
				long count = 0; 
				for (int k=1; k<=m; k++) {
					if (j>=k) count += table[i-1][j-k]; 
				}
				table[i][j] = count; 
			}
		}
		// for (long[] row: table) System.out.println(Arrays.toString(row));
		return table[n][sum];
	}
	
	/**
	 * The DP lookup table generated via the tabulation method can be used to 
	 * calculate all possible sum values and their probabilities. In the implementation, 
	 * the DP table is reduced to two arrays for space optimization. 
	 */
	private static void iterativeGetSumValAndProbDPTabu(int n, int m) {
		int largestSum = n*m; 
		long[] table = new long[largestSum+1]; // DP lookup table
		table[0] = 1; 
		for (int i=1; i<=n; i++) {
			long[] temp = new long[largestSum+1];
			for (int j=1; j<=largestSum; j++) {
				long count = 0; 
				for (int k=1; k<=m; k++) {
					if (j>=k) count += table[j-k];
				}
				temp[j] = count;
			}
			table = temp; 
		}
		long totalCount = 0; 
		for (int i=n; i<=largestSum; i++) totalCount += table[i];
		
		System.out.println("Total count: " + totalCount);
		DecimalFormat foramt = new DecimalFormat("#0.000000");
		//List<Map.Entry<Integer, Double>> list = new ArrayList<>();
		for (int i=n; i<=largestSum; i++) {
			System.out.printf("Sum: %-20d Count: %-20d Probability: %s\n", i, table[i],
					foramt.format((double)table[i]/totalCount));
			//list.add(new java.util.AbstractMap.SimpleEntry<Integer, Double>(i, (double)table[i]/totalCount));
		}
	}
	
	/**
	 * We try to print out all possible situations.
	 */
	private static void recursivePrintAllDiceCombinations(String solution, int n, int m, int sum) {
		if (n == 0) {
			if (sum == 0) System.out.println(solution); // sum reached, print solution
		} else {
			for (int i=1; i<=m; i++) {
				// no need to check for negative sum
				if (sum >= i) recursivePrintAllDiceCombinations(solution + i + " ", n-1, m, sum-i); 
			}
		}
	}
	private static void recursivePrintAllDiceCombinations(int n, int m, int sum) {
		recursivePrintAllDiceCombinations("", n, m, sum);
	}
	
	private static boolean recursivePrintAllDiceCombinationsDPMemo(String solution, int n, int m, int sum, Boolean[][] table) {
		if (table[n][sum] != null && table[n][sum] == false) return table[n][sum]; // no solution, just return boolean check
		else {
			if (n == 0) {
				if (sum == 0) {
					System.out.println(solution); // sum reached, print solution
					table[n][sum] = true; 
				}
				else table[n][sum] = false; 
			} else { 
				boolean hasSol = false; 
				for (int i=1; i<=m; i++) {
					if (sum >= i) hasSol = recursivePrintAllDiceCombinationsDPMemo(solution + i + " ", n-1, m, sum-i, table) || hasSol; 
				} 
				table[n][sum] = hasSol;
			}
			return table[n][sum];
		}
	}
	private static void recursivePrintAllDiceCombinationsDPMemo(int n, int m, int sum) {
		Boolean[][] DPLookUp = new Boolean[n+1][sum+1]; 
		recursivePrintAllDiceCombinationsDPMemo("", n, m, sum, DPLookUp); 
	}
	
	private static void iterativePrintAllDiceCombinationsDPTabu(int n, int m, int sum) {
		boolean[][] table = new boolean[n+1][sum+1]; // DP lookup table
		// base state
		table[0][0] = true; 
		for (int i=1; i<=n; i++) table[i][0] = false; // no solution when there are dices but sum is zero
		// fill up the DP lookup table
		for (int i=1; i<=n; i++) {
			for (int j=1; j<=sum; j++) {
				boolean hasSol = false;  
				for (int k=1; k<=m; k++) {
					if (j>=k) hasSol = table[i-1][j-k] || hasSol; 
				}
				table[i][j] = hasSol; 
			}
		}
	    //for (boolean[] row: table) System.out.println(Arrays.toString(row));
		// we can inspect this boolean DP lookup table recursively so as to print out all possible combinations.
		recursiveLookUpAndPrint("", n, m, sum, table);
	}
	private static void recursiveLookUpAndPrint(String solution, int n, int m, int sum, boolean[][] table) {
		if (n == 0) {
			if (sum == 0) System.out.println(solution);
		} else {
			if (table[n][sum] == false) return; // no solution to print
			for (int i=1; i<=m; i++) {
				if (sum >= i) recursiveLookUpAndPrint(solution + i + " ", n-1, m, sum-i, table);
			}
		}
	}
	
	@FunctionalInterface
	protected interface DiceCountFunction {
	   long apply(int n, int m, int sum) throws Exception;  
	}
	
    protected static void runDiceCountFuncAndCalculateTime(String message, DiceCountFunction diceCountFunc, int n, int m, int sum) throws Exception {
    	long startTime = System.nanoTime();
    	System.out.printf("%-70s%s\n", message, diceCountFunc.apply(n, m, sum));
    	long endTime = System.nanoTime();
    	long totalTime = new Long(TimeUnit.MICROSECONDS.convert(endTime - startTime, TimeUnit.NANOSECONDS));
    	DecimalFormat formatter = new DecimalFormat("#,###");
    	System.out.printf("%-70s%s\n\n", "Function execution time in micro-seconds: ", formatter.format(totalTime));
    }
    
	@FunctionalInterface
	protected interface DicePrintFunction {
	   void apply(int n, int m, int sum) throws Exception;  
	}
	
    protected static void runDiceCountFuncAndCalculateTime(String message, DicePrintFunction diceCountFunc, int n, int m, int sum) throws Exception {
    	long startTime = System.nanoTime();
    	System.out.printf("%-70s\n", message);
    	diceCountFunc.apply(n, m, sum);
    	long endTime = System.nanoTime();
    	long totalTime = new Long(TimeUnit.MICROSECONDS.convert(endTime - startTime, TimeUnit.NANOSECONDS));
    	DecimalFormat formatter = new DecimalFormat("#,###");
    	System.out.printf("%-70s%s\n\n", "Function execution time in micro-seconds: ", formatter.format(totalTime));
    }
	
	public static void main(String[] args) {
		int numOfDices = 3, numOfFaces = 6, sum = 10; 
		//int numOfDices = 10, numOfFaces = 6, sum = 59;
		System.out.println("Welcome to the rabbit hole of dice roller!\n"
				+ "There are " + numOfDices + " dices and each has " + numOfFaces + " face(s).\n"
				+ "Count the number of situations that sum up to " + sum + " after rolling.\n"); 
		
		try {
			runDiceCountFuncAndCalculateTime("[Recursive]               Situation count: ", 
					(int a, int b, int c) -> recursiveCountAllDiceCombinations(a, b, c), numOfDices, numOfFaces, sum);
			runDiceCountFuncAndCalculateTime("[Recursive][DP Memo]      Situation count: ", 
					(int a, int b, int c) -> recursiveCountAllDiceCombinationsDPMemo(a, b, c), numOfDices, numOfFaces, sum);
			runDiceCountFuncAndCalculateTime("[Iterative][DP Tabu]      Situation count: ", 
					(int a, int b, int c) -> iterativeCountAllDiceCombinationsDPTabu(a, b, c), numOfDices, numOfFaces, sum);
			runDiceCountFuncAndCalculateTime("[Recursive]               List of all possible dice rolling situations: ", 
					(int a, int b, int c) -> recursivePrintAllDiceCombinations(a, b, c), numOfDices, numOfFaces, sum);
			runDiceCountFuncAndCalculateTime("[Recursive][DP Memo]      List of all possible dice rolling situations: ", 
					(int a, int b, int c) -> recursivePrintAllDiceCombinationsDPMemo(a, b, c), numOfDices, numOfFaces, sum);
			runDiceCountFuncAndCalculateTime("[Iterative][DP Tabu]      List of all possible dice rolling situations: ", 
					(int a, int b, int c) -> iterativePrintAllDiceCombinationsDPTabu(a, b, c), numOfDices, numOfFaces, sum);
			
			System.out.println("List of all possible sum values and their probabilities(DP Tabu):");
			iterativeGetSumValAndProbDPTabu(numOfDices, numOfFaces);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("All rabbits gone.");
	}

}
