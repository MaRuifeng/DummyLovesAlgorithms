package integerArray;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

/**
 * Given a non-overlapping interval list which is sorted by start point.
 * Insert a new interval into it, make sure the list is still in order and non-overlapping (merge intervals if necessary).
 * 
 * Insert (2, 5) into [(1,2), (5,9)], we get [(1,9)].
 * Insert (3, 4) into [(1,2), (5,9)], we get [(1,2), (3,4), (5,9)].
 * 
 * @author ruifengm
 * @since 2018-Jun-16
 *
 * https://www.lintcode.com/problem/insert-interval/description
 */

public class Intervals {
	
	private static class Interval {
		int start, end; 
		public Interval (int start, int end) {
			this.start = start;
			this.end = end;
		}
		public String toString() {
			return "[" + this.start + ", " + this.end + "]";
		}
	}
	
	private static void insert(Interval interval, List<Interval> list) {
		int i = 0, size = list.size();
		// looking for left inserting position
		while (i < size && interval.start > list.get(i).end) i++; 
		if (i == size) {
			list.add(interval); 
			return;
		}
		// update interval start
		if (interval.start > list.get(i).start) interval.start = list.get(i).start;
		// look for right inserting position
		while (!list.isEmpty() && i < list.size() && interval.end > list.get(i).end) list.remove(i);
		if (list.isEmpty() || i == list.size()) {
			list.add(interval);
			return;
		}
		if (interval.end < list.get(i).start) list.add(i, interval);
		else list.get(i).start = interval.start;
	}
	
	/**
	 * Optimized the linear search with binary search.
	 */
	private static void insertViaBinarySearch(Interval interval, List<Interval> list) {
		int i = 0, size = list.size(), left, right;
		// looking for left inserting position via binary search
		left = 0; 
		right = size-1; 
		while (left <= right) {
			int mid = left + (right-left)/2; 
			i = mid;
			if (interval.start > list.get(mid).end) {
				left = mid + 1; 
				i++;
			}
			else if (interval.start < list.get(mid).start) right = mid - 1; 
			else break;
		}
	
		if (i == size) {
			list.add(interval); 
			return;
		}
		// update interval start
		if (interval.start > list.get(i).start) interval.start = list.get(i).start;
		// look for right inserting position
		while (!list.isEmpty() && i < list.size() && interval.end > list.get(i).end) list.remove(i);
		if (list.isEmpty() || i == list.size()) {
			list.add(interval);
			return;
		}
		if (interval.end < list.get(i).start) list.add(i, interval);
		else list.get(i).start = interval.start;
	}
	
	private static class TestData {
		List<Interval> intervalList; 
		Interval toBeInserted;
		List<Interval> expected;
		public TestData(List<Interval> list, Interval interval, List<Interval> expected) {
			this.intervalList = list;
			this.toBeInserted = interval;
			this.expected = expected;
		}
		public TestData(TestData data) { // copy constructor, but not deeply copied
			this.intervalList = new ArrayList<>(data.intervalList);
			this.expected = new ArrayList<>(data.expected);
			this.toBeInserted = data.toBeInserted;
		}
	}
	@Test
	private static void test(TestData data) {
		System.out.println("\n/******** TEST START ********/\n");
		System.out.println("Original interval list:\n" + data.intervalList.toString());
		System.out.println("Interval to be inserted:\n" + data.toBeInserted.toString());
		System.out.println("Expected:\n" + data.expected.toString());
		TestData dataCopy = new TestData(data);
		insert(data.toBeInserted, data.intervalList);
		System.out.println("After insertion(linear):\n" + data.intervalList.toString());
		assertTrue(data.intervalList.toString().equals(data.expected.toString()));
		
		insertViaBinarySearch(dataCopy.toBeInserted, dataCopy.intervalList);
		System.out.println("After insertion(binary search):\n" + dataCopy.intervalList.toString());
		assertTrue(dataCopy.intervalList.toString().equals(dataCopy.expected.toString()));
		
		System.out.println("\n/******** TEST END ********/\n");
	}
	public static void main(String[] args) {
		System.out.println("Welcome to the rabbit hole of integer intervals!\n");
		List<Interval> intveralList, expected;
		Interval interval;
		List<TestData> dataList = new ArrayList<>();
		
		/* Test Data */
		intveralList = new ArrayList<>(); 
		expected = new ArrayList<>(); 
		intveralList.add(new Interval(1, 3));
		intveralList.add(new Interval(6, 9));
		expected = new ArrayList<>(intveralList);
		interval = new Interval(4, 5);
		expected.add(1, interval);
		dataList.add(new TestData(intveralList, interval, expected));
		
		/* Test Data */
		intveralList = new ArrayList<>(); 
		expected = new ArrayList<>(); 
		intveralList.add(new Interval(1, 3));
		intveralList.add(new Interval(6, 9));
		expected = new ArrayList<>(intveralList);
		interval = new Interval(-1, 0);
		expected.add(0, interval);
		dataList.add(new TestData(intveralList, interval, expected));
		
		/* Test Data */
		intveralList = new ArrayList<>(); 
		expected = new ArrayList<>(); 
		intveralList.add(new Interval(1, 3));
		intveralList.add(new Interval(6, 9));
		expected = new ArrayList<>(intveralList);
		interval = new Interval(10, 11);
		expected.add(interval);
		dataList.add(new TestData(intveralList, interval, expected));
		
		/* Test Data */
		intveralList = new ArrayList<>(); 
		expected = new ArrayList<>(); 
		intveralList.add(new Interval(1, 3));
		intveralList.add(new Interval(6, 9));
		expected = new ArrayList<>();
		interval = new Interval(2, 7);
		expected.add(new Interval(1, 9));
		dataList.add(new TestData(intveralList, interval, expected));
		
		/* Test Data */
		intveralList = new ArrayList<>(); 
		expected = new ArrayList<>(); 
		intveralList.add(new Interval(1, 3));
		intveralList.add(new Interval(6, 9));
		expected = new ArrayList<>();
		interval = new Interval(3, 6);
		expected.add(new Interval(1, 9));
		dataList.add(new TestData(intveralList, interval, expected));
		
		/* Test Data */
		intveralList = new ArrayList<>(); 
		expected = new ArrayList<>(); 
		intveralList.add(new Interval(1, 3));
		intveralList.add(new Interval(6, 9));
		expected = new ArrayList<>();
		interval = new Interval(1, 6);
		expected.add(new Interval(1, 9));
		dataList.add(new TestData(intveralList, interval, expected));
		
		/* Test Data */
		intveralList = new ArrayList<>(); 
		expected = new ArrayList<>(); 
		intveralList.add(new Interval(1, 3));
		intveralList.add(new Interval(6, 9));
		expected = new ArrayList<>();
		interval = new Interval(1, 9);
		expected.add(new Interval(1, 9));
		dataList.add(new TestData(intveralList, interval, expected));
		
		/* Test Data */
		intveralList = new ArrayList<>(); 
		expected = new ArrayList<>(); 
		intveralList.add(new Interval(1, 3));
		intveralList.add(new Interval(6, 9));
		expected = new ArrayList<>();
		interval = new Interval(3, 9);
		expected.add(new Interval(1, 9));
		dataList.add(new TestData(intveralList, interval, expected));
		
		/* Test Data */
		intveralList = new ArrayList<>(); 
		expected = new ArrayList<>(); 
		intveralList.add(new Interval(1, 3));
		intveralList.add(new Interval(6, 9));
		expected = new ArrayList<>();
		interval = new Interval(0, 10);
		expected.add(interval);
		dataList.add(new TestData(intveralList, interval, expected));
		
		/* Test Data */
		intveralList = new ArrayList<>(); 
		expected = new ArrayList<>(); 
		intveralList.add(new Interval(1, 3));
		intveralList.add(new Interval(6, 9));
		expected = new ArrayList<>();
		interval = new Interval(5, 10);
		expected.add(new Interval(1, 3));
		expected.add(interval);
		dataList.add(new TestData(intveralList, interval, expected));
		
		for (TestData data: dataList) test(data);
		
		System.out.println("\nAll rabbits gone.");
	}
	
	

}

