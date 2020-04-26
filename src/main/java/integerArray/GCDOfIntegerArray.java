package integerArray;

import java.util.Arrays;

import utils.FunIntAlgorithm;

/**
 * 
 * Find the Greatest Common Divisor (GCD) of an array of integers. 
 * 
 * @author ruifengm
 * @since 2018-Apr-13
 */

public class GCDOfIntegerArray extends FunIntAlgorithm {
	
	/**
	 * A identity that holds for three numbers is gcd(a, b, c) = gcd(gcd(a,b), c). 
	 * This is true is because GCD is the maximal element of the intersection of the sets of factors of the input numbers. 
	 * In set theory, intersections of sets are commutative and associative. 
	 * 
	 * The identity can be extended to more numbers. 
	 */
	private static int findGCDofArray(int[] a) {
		int res = a[0];
		for (int i=1; i<a.length; i++) res = iterativeGCDBySub(res, a[i]);
		return res;
	}
	
	/**
	 * Euclid's Algorithm
	 *     gcd(a, b) = gcd(b, a%b)     if a%b > 0
	 *               = b               if a%b == 0
	 */
	private static int recursiveGCDByDiv(int a, int b) {
		if (b == 0) return a; 
		return recursiveGCDByDiv(b, a%b);
	}
	private static int iterativeGCDByDiv(int a, int b) {
		while (b != 0) {
			int rem = a%b; 
			a = b; 
			b = rem;
		}
		return a;
	}
	
	/**
	 * Dijkstra's Algorithm
	 *     gcd(a, b) = gcd(a-b, b)    if a > b
	 */
	private static int recursiveGCDBySub(int a, int b) {
		if (a == b) return a; 
		else if (a > b) return recursiveGCDBySub(a-b, b);
		else return recursiveGCDBySub(a, b-a);
	}
	private static int iterativeGCDBySub(int a, int b) {
		while (a != b) {
			if (a > b) a = a - b;
			else b = b - a;
		}
		return a;
	}

	public static void main(String[] args) {
		//int[] intArray = genRanIntArr(15, 2, 6);
		int[] intArray = {4, 8, 12, 24, 28, 36};
		System.out.println("Welcome to the rabbit hole of Greatest Common Divisors!\n"
				+ "The randomly generated integer array is of size " + intArray.length + ".\n"); 
		System.out.println("Integer array:" + Arrays.toString(intArray));
		
		try {
			int a = 121, b =3333; 
			System.out.println("Testing GCD functions for given number " + a + " and " + b + ":");
			System.out.println("[Recursive][Division]    " + recursiveGCDByDiv(a, b));
			System.out.println("[Iterative][Division]    " +iterativeGCDByDiv(a, b));
			System.out.println("[Recursive][Subtraction] " +recursiveGCDBySub(a, b));
			System.out.println("[Iterative][Subtraction] " +iterativeGCDBySub(a, b));
			runIntArrayFuncAndCalculateTime("GCD of the given array of integers:", (int[] arr) -> findGCDofArray(arr), intArray);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("All rabbits gone.");
	}
}
