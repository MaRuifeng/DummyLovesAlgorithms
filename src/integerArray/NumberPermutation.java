package integerArray;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Given a set of unique integers, find all permutations. 
 * 
 * E.g. given {1, 2, 3}, the permutations are 
 *      1 2 3
 *      1 3 2
 *      2 3 1
 *      2 1 3
 *      3 1 2
 *      3 2 1
 * 
 * Mathematically the number of permutations is equal to the factorial of the 
 * size of the set. For above example, 3! = 6.
 * 
 * @author ruifengm
 * @since 2018-May-26
 */

public class NumberPermutation {
	
	/** 
	 * Let array A(n) denote the set of integers, a simple recursive solution is to insert 
	 * A[n-1] into all possible positions in the permutations of sub-array A(n-1). 
	 */
	private static ArrayList<ArrayList<Integer>> recursiveGetPermutations(int[] a, int size) {
		if (size == 1) {
			ArrayList<Integer> perm = new ArrayList<>();
			perm.add(a[size-1]);
			ArrayList<ArrayList<Integer>> sol = new ArrayList<>();
			sol.add(perm);
			return sol;
		}
		ArrayList<ArrayList<Integer>> prev = recursiveGetPermutations(a, size-1); 
		ArrayList<ArrayList<Integer>> cur = new ArrayList<>();
		for (ArrayList<Integer> perm: prev) 
			for (int i=0; i<=perm.size(); i++) {
				ArrayList<Integer> newPerm = new ArrayList<>(perm);
				newPerm.add(i, a[size-1]);
				cur.add(newPerm);
			}
		return cur;
	}
	private static ArrayList<ArrayList<Integer>> recursiveGetPermutations(int[] a) {
		return recursiveGetPermutations(a, a.length);
	}
	
	/**
	 * Above method pulls the solution to the surface out of the recursive function 
	 * call stack. We try to push the printing deep into the function call stack. 
	 */
	private static void recursivePrintPermutations(ArrayList<Integer> solution, int[] a, int size) {
		if (solution.size() == a.length) System.out.println(solution.toString());
		else {
			for (int i=0; i<=solution.size(); i++) {
				ArrayList<Integer> newSol = new ArrayList<>(solution);
				newSol.add(i, a[size-1]); 
				recursivePrintPermutations(newSol, a, size-1);
			}
		}
	}
	private static void recursivePrintPermutations(int[] a) {
		ArrayList<Integer> solution = new ArrayList<>(); 
		recursivePrintPermutations(solution, a, a.length);
	}

	public static void main(String[] args) {
		int[] intArray = {1, 2, 3, 4, 5};
		System.out.println("Welcome to the rabbit hole of number permutations!\n"
				+ "The integer set is \n" + Arrays.toString(intArray) + "\n"); 
		
		System.out.println("\n/* Find all permutations via simple recursion */");
		ArrayList<ArrayList<Integer>> permList = recursiveGetPermutations(intArray);
		System.out.println("Total number of permutations: " + permList.size());
		for (ArrayList<Integer> perm: permList) System.out.println(perm.toString());
		
		System.out.println("\n/* Recursively print all permutations */");
		recursivePrintPermutations(intArray);
		
		System.out.println("\nAll rabbits gone.");
	}
}
