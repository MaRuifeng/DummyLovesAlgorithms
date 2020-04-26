package integerArray;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Given an array of integers, the majority number is the number that occurs more than half of the size of the array. Find it.
 * 
 * Challenge: O(n) time and O(1) extra space
 * 
 * This is a voting problem. Refer to the Boyer-Moore majority vote algorithm for details.
 * 
 * [Variation II]
 * Given an array of integers, a majority number is the number that occurs more than 1/3 of the size of the array. Return all such
 * numbers.
 * Challenge: O(n) time and O(1) extra space
 * 
 * [Variation III]
 * Given an array of integers and a number k, a majority number is the number that occurs more than 1/k of the size of the array. Return
 * all such numbers.
 * 
 * Challenge: O(n) time and O(k) extra space
 * 
 * @author ruifengm
 * @since 2018-Jun-24
 * 
 * https://www.lintcode.com/problem/majority-element/
 */
public class MajorityNumber {
	
	/**
	 * Use a customized data structure Vote which contains a candidate attribute and its count. 
	 * Traverse through the array. When encountering a different candidate, reduce the count by 1, otherwise increase by
	 * 1; when the count becomes 0, replace the candidate. The more-than-half majority candidate's count will never become 0 
	 * hence it will eventually stand out. 
	 */
	private static class Vote {
		int candidate;
		int count;
		public Vote(int candidate, int count) {
			this.candidate = candidate;
			this.count = count;
		}
	}
	private static int findMoreThanHalfMajorNum(int[] a) {
		Vote winner = new Vote(a[0], 1);
		for (int i=1; i<a.length; i++) {
			if (winner.count == 0) {
				winner = new Vote(a[i], 1);
				continue;
			}
			if (winner.candidate == a[i]) winner.count++;
			else winner.count--; 
		}
		return winner.candidate;
	}
	
	/**
	 * [Variation II]
	 * It can be easily deduced that at most there will be 2 candidates with more than 1/3 votes. 
	 * We look at them as a bundle and try to find them using the Boyer-Moore majority vote algorithm.
	 * And then traverse the array once more to verify.
	 */
	private static List<Integer> findMoreThanOneThirdMajorNum(int[] a) {
		Vote winnerOne = new Vote(a[0], 1); 
		Vote winnerTwo = new Vote(a[0], 1); 
		for (int i=1; i<a.length; i++) {
			if (winnerOne.candidate == a[i]) winnerOne.count++;
			else if (winnerTwo.candidate == a[i]) winnerTwo.count++;
			else if (winnerOne.count == 0) {
				winnerOne.candidate = a[i]; 
				winnerOne.count = 1;
			}
			else if (winnerTwo.count == 0) {
				winnerTwo.candidate = a[i]; 
				winnerTwo.count = 1;
			}
			else {
				winnerOne.count--;
				winnerTwo.count--;
			}
		}
		List<Integer> resList = new LinkedList<>(); 
		winnerOne.count = 0;
		winnerTwo.count = 0;
		for (int i=0; i<a.length; i++) {
			if (winnerOne.candidate == a[i]) winnerOne.count++;
			if (winnerTwo.candidate == a[i]) winnerTwo.count++; 
		}
		if (winnerOne.count > a.length/3) resList.add(winnerOne.candidate);
		if (winnerTwo.count > a.length/3) resList.add(winnerTwo.candidate);
		return resList;
	}
	
	/**
	 * [Variation III]
	 * It can be easily deduced that at most there will be (k-1) candidates with more than 1/k votes. 
	 * Proof by Contradiction: 
	 * Given an candidate array of size n, suppose there are m candidates with more than 1/k votes and m >= k, 
	 * then the total count of such candidates is m * (n/k) > n, which isn't possible. 
	 * We look at all such candidates as a bundle and try to find them using the Boyer-Moore majority vote algorithm.
	 * And then traverse the array once more to verify.
	 * 
	 * Time complexity: O(n*k)
	 * Extra space: O(k)
	 */
	private static List<Integer> findMoreThanOneKthMajorNum(int[] a, int k) {
		Vote[] voteArr = new Vote[k-1];
		for (int j=0; j<k-1; j++) voteArr[j] = new Vote(a[0], 1); // initialization
		for (int i=1; i<a.length; i++) {
			boolean next = false;
			
			// check for equal element
			for (int j=0; j<k-1; j++) {
				if (voteArr[j].candidate == a[i]) {
					voteArr[j].count++; 
					next = true; 
					break;
				}
			}
			if (next) continue;
			
			// check for empty count
			for (int j=0; j<k-1; j++) {
				if (voteArr[j].count == 0) {
					voteArr[j].candidate = a[i];
					voteArr[j].count = 1;
					next = true; 
					break;
				}
			}
			if (next) continue;
			
			// count reduction
			for (int j=0; j<k-1; j++) voteArr[j].count--;
		}
		
		List<Integer> resList = new LinkedList<>(); 
		for (int j=0; j<k-1; j++) voteArr[j].count = 0;
		for (int i=0; i<a.length; i++) {
			for (int j=0; j<k-1; j++) {
				if (voteArr[j].candidate == a[i]) voteArr[j].count++;
			}
		}
		for (int j=0; j<k-1; j++) {
			if (voteArr[j].count > a.length/k) resList.add(voteArr[j].candidate);
		}
		return resList;
	}
	
	/**
	 * The above method using an array to store candidates has an upper bound time complexity of O(nk) 
	 * which does not fulfill the requirements. 
	 * We take a loser look at the operations that occur within the outer for loop. 
	 * 1) Look for a candidate and increase its count if matched with current element.
	 * 2) Look for a candidate with zero count
	 * 3) Reduce all candidates' count by 1
	 * 
	 * The first operation can be done in O(1) time with a HashMap. 
	 * The second operation can be done in O(1) time with an auxiliary HashMap that stores all candidates with 
	 * certain count, and the count serves as the key. --> too complex logic, not worth it.
	 * The third operation is a bit tricky. Instead of reducing all counts by 1, we keep track of a BASE_COUNT value that replaces
	 * the reductions in a contrary way. Whenever we need to reduce all counts by 1, we increase the BASE_COUNT by 1. 
	 * 
	 * So we try to optimize it a bit. 
	 */
	private static List<Integer> findMoreThanOneKthMajorNumOptimized(int[] a, int k) {
		HashMap<Integer, Integer> candidateHash = new HashMap<>(); // key - candidate; value - count
		int baseCount = 0;
		for (int i=0; i<a.length; i++) {	
			boolean next = false;
			// check for equal element
			if (candidateHash.containsKey(a[i])) {
				candidateHash.put(a[i], candidateHash.get(a[i]) + 1);
				continue;
			} else if (candidateHash.size() < k-1) {
				candidateHash.put(a[i], baseCount + 1);
				continue;
			}
			
			// check for votes with base count
			for (Map.Entry<Integer, Integer> entry: candidateHash.entrySet()) {
				if (entry.getValue() == baseCount) {
					candidateHash.remove(entry.getKey());
					candidateHash.put(a[i], baseCount + 1);
					next = true; 
					break;
				}
			}
			if (next) continue;
			
			// count reduction
			baseCount++;
		}
		
		List<Integer> resList = new LinkedList<>(); 
		for (Integer key: candidateHash.keySet()) candidateHash.put(key, 0);
		for (int key: a) {
			if (candidateHash.containsKey(key)) candidateHash.put(key, candidateHash.get(key) + 1);
		}
		for (Map.Entry<Integer, Integer> entry: candidateHash.entrySet()) {
			if (entry.getValue() > a.length/k) resList.add(entry.getKey());
		}
		return resList;
	}
	
	public static void main(String[] args) {
		int[] a1 = {4, 5, 1, 1, 3, 1, 1, 2, 1};
		System.out.println("Welcome to the rabbit hole of majority numbers!\n");
		
		/* More-than-half majority */
		System.out.println("\n*****************More-than-half*******************");
		System.out.println("The integer array with one more-than-half majority number is \n" + Arrays.toString(a1) + ".\n");
		System.out.println("The more-than-half winner: " + findMoreThanHalfMajorNum(a1));
		
		/* More-than-1/3 majority */
		int[] a2 = {1, 1, 2, 2, 3, 3};
		System.out.println("\n*****************More-than-1/3*******************");
		System.out.println("The integer array with NONE more-than-1/3 majority number is \n" + Arrays.toString(a2) + ".\n");
		System.out.println("The more-than-1/3 winner(s): " + findMoreThanOneThirdMajorNum(a2).toString());
		
		int[] a3 = {1, 1, 1, 2, 3, 3};
		System.out.println("\n*****************More-than-1/3*******************");
		System.out.println("The integer array with one more-than-1/3 majority number is \n" + Arrays.toString(a3) + ".\n");
		System.out.println("The more-than-1/3 winner(s): " + findMoreThanOneThirdMajorNum(a3).toString());
		
		int[] a4 = {1, 1, 1, 2, 3, 3, 3, 1};
		System.out.println("\n*****************More-than-1/3*******************");
		System.out.println("The integer array with two more-than-1/3 majority number is \n" + Arrays.toString(a4) + ".\n");
		System.out.println("The more-than-1/3 winner(s): " + findMoreThanOneThirdMajorNum(a4).toString());
		
		/* More-than-1/k majority */
		int[] a5 = {1, 1, 1, 2, 3, 3, 3, 1, 2, 4, 2, 2, 2, 3, 4, 4, 4};
		int k = 5;
		System.out.println("\n*****************More-than-1/k*******************");
		System.out.println("The integer array with NONE more-than-1/k majority number is \n" + Arrays.toString(a5) + ".\n");
		System.out.println("The integer k is " + k + ".\n");
		System.out.println("The more-than-1/k winner(s): " + findMoreThanOneKthMajorNum(a5, k).toString());
		System.out.println("[optimized]The more-than-1/k winner(s): " + findMoreThanOneKthMajorNumOptimized(a5, k).toString());
		
		System.out.println("\nAll rabbits gone.");
	}

}
