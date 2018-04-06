package dynamicProgramming;

import utils.FunAlgorithm;

/**
 * Ugly numbers are defined as numbers whose prime factors are 2, 3, and 5 only. Below sequence shows the first 11 ugly numbers
 * 		1, 2, 3, 4, 5, 6, 8, 9, 10, 12, 15 ...
 * 1 is included by convention. 
 * 
 * What is the n'th ugly number with n given as a positive integer? 
 * 
 * https://www.geeksforgeeks.org/?p=753
 * @author ruifengm
 * @since 2018-Apr-6
 */


public class UglyNumbers extends FunAlgorithm {
	
	/**
	 * Trivial method.
	 * Increment from 1 and check whether the number is ugly. If yes, increase counter. Stop until counter is equal to n. 
	 * Ugly check on number k: keep dividing k with 2, 3 or 5 if any of the modulus is 0; 
	 * stop when a modulus is 0 and the quotient is 1, k is an ugly number; if all modulus values become 
	 * non-zero, number k is not an ugly number.
	 *  
	 */
	private static int findUglyNumber(int n) {
		int counter = 1; 
		int num = 1; // starting position
		int res = 1; // first ugly number
		
		
		while (counter<n) {
			// ugly check
			int temp = num;
			int mod2 = 0, mod3 = 0, mod5 = 0; 
			while (mod2 == 0 || mod3 == 0 || mod5 == 0) {
				mod2 = temp % 2; 
				if (mod2 == 0) {
					if (temp/2 == 1) {
						counter++;
						res = num;
						break;
					} else temp = temp/2;
				} 
				
				mod3 = temp % 3;
				if (mod3 == 0) {
					if (temp/3 == 1) {
						counter++; 
						res = num;
						break;
					} else temp = temp/3;
				} 
					
				mod5 = temp % 5;
				if (mod5 == 0) {
					if (temp/5 == 1) {
						counter++; 
						res = num;
						break;
					} else temp = temp/5;
				} 
			}
			
			num++;
		}
		return res;
	}
	
	
	/** 
	 * Ugly numbers are all multiplications of the seed numbers 2, 3, and 5.  
	 * It's not difficult to observe and deduce that larger ugly numbers can be obtained by multiplying a smaller ugly number with 2, 3 and 5. 
	 * Starting from the smallest, we'll be able to cover all. The "overlapping sub-problems" and "optimal sub-structure" properties
	 * of a DP problem are identified. (A bit greedy, isn't it?)
	 * Observer below pattern, 
	 * 1*2, 1*3, 1*5
	 * 2*2, 2*3, 2*5
	 * 3*2, 3*3, 3*5
     * 4*2, 4*3, 4*5
     * 5*2, 5*3, 5*5
     * 6*2, 6*3, 6*5
     * 8*2, 8*3, 8*5
     * ...
     * 
     * We just need to put the found numbers into ascending order and filter out duplicates so as to form the ugly number array.
     * Think of three naughty elves who keep polling numbers from above three queues, and we choose the smallest and add to the result array 
     * and decline the other 2. And the elves extend their queues by multiplying with the newest result ...
	 */
	private static int iterativeFindUglyNumberDP(int n) {
		int[] numList = new int[n];
		numList[0] = 1;
		int twoMul = 2, thrMul = 3, fivMul = 5; // multiplication of the ugly numbers by the seed numbers, starting from the first ugly number 1
		int q2Idx = 1, q3Idx = 1, q5Idx = 1; // index to indicate multiplication point on the ugly number array
		int i = 1;
		while (i<n) {
			numList[i] = Math.min(Math.min(twoMul, thrMul), fivMul);
			if (twoMul == numList[i]) twoMul = 2 * numList[q2Idx++];
			if (thrMul == numList[i]) thrMul = 3 * numList[q3Idx++];
			if (fivMul == numList[i]) fivMul = 5 * numList[q5Idx++];
			i++;
		}
		return numList[n-1];
	}
	
	public static void main(String[] args) {
		int pos = 150;
		System.out.println("Welcome to the rabbit hole of ugly numbers!\n"
				+ "The wanted number should be at position " + pos + ".\n"); 
		
		try {
			runIntFuncAndCalculateTime("[Iterative]        Ugly number at position " + pos + ":" , (int i) -> findUglyNumber(i), pos);
			runIntFuncAndCalculateTime("[Iterative][DP]    Ugly number at position " + pos + ":" , (int i) -> iterativeFindUglyNumberDP(i), pos);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("All rabbits gone.");
	}

}
