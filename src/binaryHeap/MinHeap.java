package binaryHeap;

import java.util.Arrays;

/**
 * A binary heap is a complete binary tree data structure that satisfies the heap ordering property. 
 * 
 * 1) Min Heap: the value of each node is greater than or equal to the value of its 
 *              parent, with the minimum at the root
 * 2) Max Heap: the value of each node is smaller than or equal to the value of its 
 *              parent, with the maximum at the root 
 *              
 * Following the level order traversal of a complete binary tree, marking the root element as the 
 * 1st, then we can easily deduce that at the k-th element, 
 * 		1) its left child is the (2*k)-th element
 * 		2) its right child is the (2*k+1)-th element
 * 		3) its parent is the (k/2)-th element
 * 
 * With above properties, a complete binary tree can be uniquely represented in an array. 
 * 
 * https://www.cs.cmu.edu/~adamchik/15-121/lectures/Binary%20Heaps/heaps.html
 * 
 * @author ruifengm
 * @since 2018-Apr-21
 */

public class MinHeap {
	
	protected int[] heapArray; // array storing heap elements
	protected int capacity;    // maximum heap size
	protected int heapSize;    // current heap size
	
	public MinHeap(int capacity) { // constructor
		this.heapSize = 0; 
		this.capacity = capacity; 
		this.heapArray = new int[capacity];
	}
	
	protected int getParentIdx(int idx) {
		return (idx-1)/2;
	}
	
	protected int getLeftIdx(int idx) {
		return idx*2 + 1;
	}
	
	protected int getRightIdx(int idx) {
		return idx*2 + 2;
	}
	
	protected void swap(int[] a, int p1, int p2) {
		if (p1<0 || p1>a.length-1 || p2<0 || p2>a.length-1) return;
		int temp = a[p1];
		a[p1] = a[p2];
		a[p2] = temp;
	}
	
	public String toString() {
		return Arrays.toString(Arrays.copyOfRange(this.heapArray, 0, this.heapSize));
	}
	/**
	 * Insert a new element into the heap
	 * --> append to the end as a leaf and repair the heap by "percolation up" if needed
	 */
	public void insert(int e) {
		if (this.heapSize == this.capacity) {
			System.out.println("\nHeap capacity reached! No more elements allowed.\n");
			return;
		}
		heapArray[heapSize++] = e;
		int curPos = heapSize - 1; 
		int parPos = getParentIdx(curPos);
		while (heapArray[parPos] > heapArray[curPos] && curPos > 0) {
			swap(heapArray, curPos, parPos);
			curPos = parPos;
			parPos = getParentIdx(curPos);
		}
	}
	
	/**
	 * Get minimum element
	 */
	public int getMin() {
		return this.heapArray[0];
	}
	
	/**
	 * Extract minimum element
	 * --> return the min element and remove it from the heap
	 */
	public int extractMin() {
		int min = getMin();
		swap(this.heapArray, 0, heapSize-1);
		this.heapSize--; 
		if (0 < this.heapSize) minHeapify(0);
		return min;
	}
	
	/**
	 * Delete an element stored at given index
	 */
	public void delete(int idx) {
		if (idx >= this.heapSize) {
			System.out.println("\nNo element found or index out of heap capacity!\n");
			return;
		}
		swap(this.heapArray, idx, heapSize-1);
		this.heapSize--;
		if (idx < this.heapSize) minHeapify(idx);
	}
	
	/**
	 * Repair a heap with root at given index
	 */
	public void minHeapify(int idx) {
		if (idx >= this.heapSize) {
			System.out.println("\nNo element found or index out of heap capacity!\n");
			return;
		}
		int curPos = idx;
		int leftPos = getLeftIdx(curPos), rightPos = getRightIdx(curPos);
		while (true) {
			if (leftPos < heapSize && heapArray[curPos] > heapArray[leftPos] && (rightPos >= heapSize || heapArray[rightPos] >= heapArray[leftPos])) {
				swap(this.heapArray, curPos, leftPos);
				curPos = leftPos;
			} else if (rightPos < heapSize && heapArray[curPos] > heapArray[rightPos] && heapArray[leftPos] > heapArray[rightPos]) {
				swap(this.heapArray, curPos, rightPos);
				curPos = rightPos;
			} else break;
			leftPos = getLeftIdx(curPos);
			rightPos = getRightIdx(curPos);
		}
	}
	
	public static void main(String[] args) {
		MinHeap heap = new MinHeap(10); 
		System.out.println("Welcome to the rabbit hole of min heaps!\n");
		System.out.println("[O(logN)] Inserting new elements...");
		heap.insert(4);
		heap.insert(5);
		heap.insert(2);
		heap.insert(8);
		heap.insert(3);
		heap.insert(7);
		heap.insert(6);
		
		/**
		 * After above insertion, the min heap should look like
		 * 
		 *           2
		 *         /   \
		 *        3     4
		 *       / \   / \
		 *      8   5 7   6 
		 */
		System.out.println("Printing Min Heap as array: " + heap.toString());
		System.out.println("[O(1)] Get min: " + heap.getMin());
		System.out.println("[O(logN)] Extract min: " + heap.extractMin());
		System.out.println("Printing Min Heap as array: " + heap.toString());
		System.out.println("[O(logN)] Extract min: " + heap.extractMin());
		System.out.println("Printing Min Heap as array: " + heap.toString());
		System.out.println("[O(logN)] Inserting new elements...");
		heap.insert(9);
		heap.insert(1);
		heap.insert(11);
		heap.insert(15);
		heap.insert(10);
		System.out.println("Printing Min Heap as array: " + heap.toString());
		int idx = 4;
		System.out.println("[O(logN)] Delete element at index " + idx + "...");
		heap.delete(idx);
		System.out.println("Printing Min Heap as array: " + heap.toString());
		idx = 1;
		System.out.println("[O(logN)] Delete element at index " + idx + "...");
		heap.delete(idx);
		System.out.println("Printing Min Heap as array: " + heap.toString());
		System.out.println("\nAll rabbits gone.");
	}

}
