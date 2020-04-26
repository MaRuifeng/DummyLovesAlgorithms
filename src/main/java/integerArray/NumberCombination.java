package integerArray;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Given a set of unique integers and a number N between 0 and size of the set, find 
 * all combinations of N integers from the set. 
 * 
 * E.g. given {1, 2, 3} and N = 2, the combinations are 
 *      {1, 2}, {1, 3} and {2, 3}
 *      
 * Mathematically the number of combinations is equal to S!/((S-N)!*N!), where 
 * S is the size of the set.  
 * 
 * @author ruifengm
 * @since 2018-May-26
 */

public class NumberCombination {
	
	/** 
	 * The subset can either contains the last element of the set or not, which forms sub-problems
	 * for the recursive pattern. 
	 */
	private static void printNumberCombinations(ArrayList<Integer> sol, int[] a, int size, int n, int N) {
		if (sol.size() == N) System.out.println(sol.toString());
		else {
			ArrayList<Integer> newSol = new ArrayList<>(sol); 
			newSol.add(a[size-1]); 
			// last element included
			printNumberCombinations(newSol, a, size-1, n-1, N);
			// last element excluded
			if (size-1 >= n) printNumberCombinations(sol, a, size-1, n, N);
		}
	}
	private static void printNumberCombination(int[] a, int n) {
		ArrayList<Integer> sol = new ArrayList<>(); 
		printNumberCombinations(sol, a, a.length, n, n);
	}
	
	/**
	 * Store the combinations into a list and utilize the property of C(n, m) = C(n, n-m)
	 * to reduce computations. 
	 */
	private static void findNumberCombinations(ArrayList<Integer> sol, int[] a, int size, int n, int N, 
			ArrayList<ArrayList<Integer>> solList) {
		if (sol.size() == N) solList.add(sol);
		else {
			ArrayList<Integer> newSol = new ArrayList<>(sol); 
			newSol.add(a[size-1]); 
			// last element included
			findNumberCombinations(newSol, a, size-1, n-1, N, solList);
			// last element excluded
			if (size-1 >= n) findNumberCombinations(sol, a, size-1, n, N, solList);
		}
	}
	private static ArrayList<ArrayList<Integer>> findNumberCombinations(int[] a, int n) {
		int size = a.length;
		ArrayList<Integer> sol = new ArrayList<>(); 
		ArrayList<ArrayList<Integer>> solList = new ArrayList<>(); 
		if (n < size - n) findNumberCombinations(sol, a, a.length, n, n, solList);
		else {
			findNumberCombinations(sol, a, a.length, size-n, size-n, solList);
			ArrayList<Integer> arrList = new ArrayList<>(); 
			for (int i: a) arrList.add(i);
			for (ArrayList<Integer> s: solList) {
				ArrayList<Integer> temp = new ArrayList<>(arrList); 
				temp.removeAll(s);
				s.clear();
				s.addAll(temp);
			}
		}
		return solList;
	}
	
	public static void main(String[] args) {
		int[] intArray = {1, 2, 3, 4, 5};
		int n = 3;
		System.out.println("Welcome to the rabbit hole of number combinations!\n"
				+ "The integer set is \n" + Arrays.toString(intArray) + "\n"
				+ "The number of integers to choose is " + n + "."); 
		
		
		System.out.println("\n/* Recursively print combinations */");
		printNumberCombination(intArray, n);
		
		System.out.println("\n/* Recursively find combinations and store them into a list */");
		ArrayList<ArrayList<Integer>> combList = findNumberCombinations(intArray, n);
		System.out.println("Total number of combinations: " + combList.size());
		for (ArrayList<Integer> comb: combList) System.out.println(comb.toString());

		System.out.println("\nAll rabbits gone.");
	}

}
