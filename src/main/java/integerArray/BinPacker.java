package integerArray;

import java.util.Arrays;

import utils.FunIntAlgorithm;

/**
 * Given n items of different weights and bins each of capacity c, assign each item to a 
 * bin such that number of total used bins is minimized. It may be assumed that all 
 * items have weights smaller than bin capacity.
 * 
 * The bin packing problem is an NP-Hard problem whose exact solution takes exponential time 
 * to find. Here we present a few approximation techniques. 
 * 
 * @author ruifengm
 * @since 2018-Jun-21
 */

public class BinPacker extends FunIntAlgorithm {
	
	/* Online algorithms where item comes in one after one during run time. */
	/**
	 * The solution obtained by the next fit approximation technique has an upper bound of twice the optimal value. 
	 */
	private static int nextFit(int[] weightArr, int capacity) {
		int binCount = 1, binRemCap = capacity; 
		for (int weight: weightArr) {
			if (weight <= binRemCap) binRemCap -= weight;
			else { // use a new bin
				binCount++; 
				binRemCap = capacity - weight;
			}
		}
		return binCount;
	}
	
	/**
	 * The solution obtained by the first fit approximation technique has an upper bound of 1.7 times the optimal value.
	 */
	private static int firstFit(int[] weightArr, int capacity) {
		int binCount = 1; 
		int[] binRemCap = new int[weightArr.length]; // array of bin remaining capacities
		Arrays.fill(binRemCap, capacity); // initialize all bins with a start capacity, but think as if they were all closed except first one
		for (int weight: weightArr) {
			// look for first bin that can accommodate the item
			int i;
			for (i=0; i<binCount; i++) {
				if (binRemCap[i] >= weight) {
					binRemCap[i] -= weight; 
					break;
				}
			}
			if (i == binCount) { // no open bin can take the item, open a new one
				binCount++; 
				binRemCap[binCount-1] -= weight;
			}
		}
		return binCount;
	}
	
	/**
	 * The solution obtained by the best fit approximation technique also has an upper bound of 1.7 times the optimal value.
	 */
	
	private static int bestFit(int[] weightArr, int capacity) {
		int binCount = 1; 
		int[] binRemCap = new int[weightArr.length]; // array of bin remaining capacities
		Arrays.fill(binRemCap, capacity); // initialize all bins with a start capacity, but think as if they were all closed except first one
		for (int weight: weightArr) {
			// look for best (tightest) bin that can accommodate the item
			int minCap = Integer.MAX_VALUE, bestBinIdx = -1; 
			for (int i=0; i<binCount; i++) {
				if (binRemCap[i] >= weight && binRemCap[i]-weight < minCap) {
					bestBinIdx = i; 
					minCap = binRemCap[i]-weight; 
				}
			}
			if (minCap == Integer.MAX_VALUE) { // no open bin can take the item, open a new one
				binCount++; 
				binRemCap[binCount-1] -= weight;
			} else binRemCap[bestBinIdx] -= weight;
		}
		return binCount;
	}
	
	/* Offline algorithms where all items are presented together. */
	/**
	 * We need to sort the weight array and start placing from the largest item using either first fit or 
	 * best fit technique. 
	 * First fit decreasing technique has an upper bound of (4M + 1)/3 bins if the optimal is M.
	 */
	private static int firstFitDecreasing(int[] weightArr, int capacity) {
		quickSort(weightArr, 0, weightArr.length-1);
		int binCount = 1; 
		int[] binRemCap = new int[weightArr.length]; // array of bin remaining capacities
		Arrays.fill(binRemCap, capacity); // initialize all bins with a start capacity, but think as if they were all closed except first one
		for (int j=weightArr.length-1; j>=0; j--) {
			// look for first bin that can accommodate the item
			int i;
			for (i=0; i<binCount; i++) {
				if (binRemCap[i] >= weightArr[j]) {
					binRemCap[i] -= weightArr[j]; 
					break;
				}
			}
			if (i == binCount) { // no open bin can take the item, open a new one
				binCount++; 
				binRemCap[binCount-1] -= weightArr[j];
			}
		}
		return binCount;
	}
	
	public static void main(String[] args) {
		System.out.println("Welcome to the rabbit hole of bin packers!\n");
		
		//int[] weightArray = {2, 8, 4, 3, 7, 5, 1, 2}; 
		int[] weightArray = genRanIntArr(100, 1, 99);
		int capacity = 100; 
		System.out.println("Weight array: ");
		System.out.println(Arrays.toString(weightArray));
		System.out.println("Bin capacity: " + capacity + "\n");
		
		System.out.println("[On-line][Next Fit]        Approximate minimum number of bins needed: " + nextFit(weightArray, capacity));
		System.out.println("[On-line][First Fit]       Approximate minimum number of bins needed: " + firstFit(weightArray, capacity));
		System.out.println("[On-line][Best Fit]        Approximate minimum number of bins needed: " + bestFit(weightArray, capacity));
		
		System.out.println("[Off-line][First Fit Decreasing]        "
				+ "Approximate minimum number of bins needed: " + firstFitDecreasing(weightArray, capacity));
		
		System.out.println("\nAll rabbits gone.");
	}

}
