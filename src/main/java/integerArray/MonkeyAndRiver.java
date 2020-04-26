package integerArray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

/**
 * A monkey needs to cross a river that has a width of N. At most the monkey is able to jump over a 
 * distance of D. Given an array A of N integers that indicate the time at which a stone will surface
 * out from water at the indexed position across the river, determine the earliest time at which 
 * the monkey can reach the other bank of the river. In the array, number 0 means the stone is already at 
 * the surface and number -1 means the stone will never surface. Return -1 if the monkey is never going 
 * to be able to cross the river. 
 * 
 * E.g. 
 * N = 3, D = 1, A = [3, 2, 1], the result is 3 as the monkey needs to wait for the first stone to surface and jump 1 at a time. 
 * N = 4, D = 3, A = [1, 2, 3, 4], the result is 2, as the monkey will jump onto the first 2 stones
 *                                 when they surface and then reach the other bank in one more jump. 
 * N = 6, D = 2, A = [1, 2, 2, -1, -1, -1], the result is -1.
 * N = 2, D = 3, A = [1, 2], the result is 0. 
 * 
 * Expected time complexity: O(N + Max(A))
 * Challenge: how can we ensure the monkey performs the least jumps as well?
 * 
 * @author ruifengm
 * @since 2018-Jun-26
 * 
 */
public class MonkeyAndRiver {
	
	/**
	 * An intuitive greedy solution is to set a time counter, and at each time point, let the monkey jump over to the 
	 * farthest stone it can reach, until it reaches the other bank of the river. 
	 */
	private static int earliestTime(int[] A, int D) {
		int start = -1, target = A.length;
		int time = 0, maxTime = maxVal(A);
		while (time <= maxTime) {
			start = jumpTo(start, A, D, time);
			if (start == target) return time; 
			time++;
		}
		return -1;
	}
	private static int jumpTo(int start, int[] A, int D, int time) { // farthest position the monkey can reach at given time
		int dest = start;
		while (true) {
			int maxPos = start + D;
			if (maxPos > A.length-1) return A.length;
			for (int i=maxPos; i>=Math.max(start, 0); i--) {
				if (A[i] <= time && A[i] > -1) {
					dest = i;
					break;
				}
			}
			if (dest == start) break; 
			else start = dest;
		}
		return dest == A.length-1 ? A.length : dest; // if monkey can reach the last stone then it just needs to make one more jump to reach the other bank
	}
	private static int maxVal(int[] arr) {
		int max = Integer.MIN_VALUE;
		for (int i: arr) if (max < i) max = i; 
		return max;
	}
	
	/* Test */
	static class TestData {
		int[] A; 
		int D;
		int expectedTime;
		public TestData(int[] A, int D, int expectedTime) {
			this.A = A; 
			this.D = D;
			this.expectedTime = expectedTime;
		}
	}	
	@Test
	public static void test(TestData data) {
		System.out.println("\n/******** TEST START ********/");
		
		System.out.println("Time array: " + Arrays.toString(data.A)); 
		System.out.println("Maximum jump distance: " + data.D); 
		System.out.println("Expected earliest time: " + data.expectedTime); 
		int actualTime = earliestTime(data.A, data.D);
		System.out.println("Actual earliest time: " + actualTime); 
		Assert.assertEquals(data.expectedTime, actualTime);
		
		System.out.println("/******** TEST End ********/");
	}
	
	

	public static void main(String[] args) {
		System.out.println("Welcome to the rabbit hole of monkeys crossing a river!\n");

		List<TestData> testDataList = new ArrayList<>(); 
		testDataList.add(new TestData(new int[] {1}, 0, -1));
		testDataList.add(new TestData(new int[] {1}, 1, 1));
		testDataList.add(new TestData(new int[] {-1}, 1, -1));
		testDataList.add(new TestData(new int[] {0}, 1, 0));
		testDataList.add(new TestData(new int[] {1, 2}, 3, 0));
		testDataList.add(new TestData(new int[] {1, 2, 2, -1, -1, -1}, 2, -1));
		testDataList.add(new TestData(new int[] {1, 2, 2, -1, -1, -1}, 3, -1));
		testDataList.add(new TestData(new int[] {1, 2, 2, -1, 0, -1}, 3, 2));
		testDataList.add(new TestData(new int[] {-1, -1, 0, 3, 2, 1}, 3, 1));
		testDataList.add(new TestData(new int[] {1, 2, 3, 4}, 3, 2));
		testDataList.add(new TestData(new int[] {3, 2, 1}, 1, 3));
		testDataList.add(new TestData(new int[] {2, 1, -1, 0}, 2, 1));
		testDataList.add(new TestData(new int[] {2, 1, 4, 3, 0, -1, 1}, 2, 3));

		
		for (TestData data: testDataList) test(data);
		System.out.println("\nAll rabbits gone and monkeys stayed.");
	}
	
}
