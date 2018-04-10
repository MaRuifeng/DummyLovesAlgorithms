package integerArray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;

import utils.FunIntAlgorithm;

/**
 * Given an unsorted array of integers and number K, find all number pairs from the array each of which sums to K; state such if 
 * no solution can be found. 
 * 
 * https://www.geeksforgeeks.org/write-a-c-program-that-given-a-set-a-of-n-numbers-and-another-number-x-determines-whether-or-not-there-exist-two-elements-in-s-whose-sum-is-exactly-x/
 * 
 * @author ruifengm
 * @since 2018-Apr-10
 */
public class NumberPairOfFixedSum extends FunIntAlgorithm {
	
	/**
	 * A simple method is to do it via two nested loops. 
	 * Time complexity: O(N^2) 
	 */
	private static HashSet<ArrayList<Integer>> findNumPairsOfFixedSum(int[] a, int sum) {
		HashSet<ArrayList<Integer>> resList = new HashSet<ArrayList<Integer>>(); // use hashset to avoid duplicates
		
		for (int i=0; i<a.length; i++) {
			for (int j=i+1; j<a.length; j++) {
				if (a[i] + a[j] == sum) {
					ArrayList<Integer> res = new ArrayList<Integer>();
					res.add(a[i]);
					res.add(a[j]);
					Collections.sort(res); // sort so as to let the hashset filter duplicates
					resList.add(res);
				}
			}
		}
		
		return resList;
	}
	
	/**
	 * Traverse the array, and store all numbers into a HashSet, for every number n, check if (K-n) exists in the HashSet. 
	 * A special case that needs extra handling would be when n == K/2. 
	 * 
	 * Time complexity: O(N)
	 */
	private static HashSet<ArrayList<Integer>> findNumPairOfFixedSumViaHash(int[] a, int sum) {
		HashSet<ArrayList<Integer>> resList = new HashSet<ArrayList<Integer>>(); 
		HashSet<Integer> numHash = new HashSet<Integer>(); 
		int halfSumCount = 0; // number of occurrences of sum/2 in the array
		
		for (int n: a) {
			if (halfSumCount < 2 && n == sum/2) halfSumCount++; 
			numHash.add(n);
		}
		Iterator<Integer> iter = numHash.iterator(); 
		while(iter.hasNext()) {
			Integer n = iter.next();
			Integer sumMinusN = new Integer(sum - n.intValue()); 
			if (n.intValue() != sum/2 && numHash.contains(sumMinusN)) {
				ArrayList<Integer> res = new ArrayList<Integer>();
				res.add(n);
				res.add(sumMinusN);
				resList.add(res);
				// remove found entries from the hash set
				iter.remove();
			}
		}
		
		// check for sum/2
		if (halfSumCount >= 2) {
			ArrayList<Integer> res = new ArrayList<Integer>();
			res.add(sum/2);
			res.add(sum/2);
			resList.add(res);
		}
		return resList;
	}
	
	/**
	 * Above method using hashing can be refined by doing it in-line instead of populating the hash set first. 
	 * But it's worth noting that the execution time won't be reduced as the amount of computation still remains. 
	 * It just makes the code shorter. 
	 */
	private static HashSet<ArrayList<Integer>> findNumPairOfFixedSumViaHashRefined(int[] a, int sum) {
		HashSet<ArrayList<Integer>> resList = new HashSet<ArrayList<Integer>>(); 
		HashSet<Integer> numHash = new HashSet<Integer>(); 
		boolean halfSumFound = false; 
		for (int n: a) {
			if (numHash.contains(sum-n)) {
				if (!numHash.contains(n)) { // avoid duplicates
					ArrayList<Integer> res = new ArrayList<Integer>();
					res.add(n);
					res.add(sum-n);
					resList.add(res);
				} else if (n == sum/2 && halfSumFound == false) {
					ArrayList<Integer> res = new ArrayList<Integer>();
					res.add(n);
					res.add(n);
					resList.add(res);
					halfSumFound = true;
				}
			}
			numHash.add(n);
		}
		return resList;
	}
	
	public static void main(String[] args) {
		int[] intArray = genRanIntArr(50000, -298, 301);
		int sum = 298;
		//int[] intArray = {11, 9, 8, 5, 7, 12, 12, 14, 8, 14, 11, 8, 10, 10, 10, 10, 10, 10, 10, 10};
		//System.out.println("Integer array:" + Arrays.toString(intArray));
		System.out.println("Welcome to the rabbit hole of number pairs of fixed sum!\n"
				+ "The randomly generated integer array is of size " + intArray.length + ".\n"
				+ "The fixed sum is " + sum + ".\n"); 
		
		try {
			runIntArrayFuncAndCalculateTime("[Quadratic]              The number pairs are (not found if shown as MAX INT):", (int[] a, int v) -> findNumPairsOfFixedSum(a, v), intArray, sum);
			runIntArrayFuncAndCalculateTime("[Linear][Hashing]        The number pairs are (not found if shown as MAX INT):", (int[] a, int v) -> findNumPairOfFixedSumViaHash(a, v), intArray, sum);
			runIntArrayFuncAndCalculateTime("[Linear][Inline Hashing] The number pairs are (not found if shown as MAX INT):", (int[] a, int v) -> findNumPairOfFixedSumViaHashRefined(a, v), intArray, sum);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("All rabbits gone.");
	}
	

}
