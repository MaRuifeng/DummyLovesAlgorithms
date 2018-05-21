package binaryTree;
import java.util.Arrays;

import binaryTree.entities.BinarySearchTree;
import binaryTree.entities.TreeNode;
import utils.FunIntAlgorithm;

/**
 * Convert a given array of unique integers into a balanced BST. 
 * 
 * @author ruifengm
 * @since 2018-May-20
 */

public class ArrayToBSTConverter {
	
	/**
	 * From root to leaves (pre-order traversal).
	 */
	private static TreeNode convertArrayToBSTfromRootToLeaf(int[] arr) {
		int[] sortedArr = FunIntAlgorithm.mergeSort(arr, 0, arr.length-1);
		return convertSortedArrayToBSTfromRootToLeaf(sortedArr, 0, sortedArr.length - 1); 
	}
	private static TreeNode convertSortedArrayToBSTfromRootToLeaf(int[] sortedArr, int start, int end) {
		// base case
		if (start > end)  return null; 
		// recursion
		int mid = (start + end) / 2; 
		TreeNode node = new TreeNode(sortedArr[mid]); 
		node.setLeft(convertSortedArrayToBSTfromRootToLeaf(sortedArr, start, mid-1));
		node.setRight(convertSortedArrayToBSTfromRootToLeaf(sortedArr, mid+1, end));
		return node;
	}
	
	/**
	 * From leaves to root (in-order traversal).
	 */
	static class Index {
		int val = 0;
	}
	private static TreeNode convertArrayToBSTfromLeafToRoot(int[] arr) {
		int[] sortedArr = FunIntAlgorithm.mergeSort(arr, 0, arr.length-1);
		return convertSortedArrayToBSTfromLeafToRoot(sortedArr, new Index(), sortedArr.length); 
	}
	private static TreeNode convertSortedArrayToBSTfromLeafToRoot(int[] sortedArr, Index idx, int size) {
		if (size <= 0) return null; 
		TreeNode left = convertSortedArrayToBSTfromLeafToRoot(sortedArr, idx, size/2); 
		TreeNode root = new TreeNode(sortedArr[idx.val++]);
		root.setLeft(left);
		TreeNode right = convertSortedArrayToBSTfromLeafToRoot(sortedArr, idx, size - size/2 -1);
		root.setRight(right);
		return root;
	}
	
	public static void main(String[] args) {
		System.out.println("/** Converting a random unique integer array into a balanced BST. **/");
		// int[] arr = {2, 4, 6, 3, 5, 8,7};
		int[] arr = FunIntAlgorithm.genRanUniqueIntArr(7);
		System.out.println("The randomly generated array is \n" + Arrays.toString(arr));
		
		System.out.println("\n/** From root to leaf [O(n)] **/\n");
		BinarySearchTree balBST = new BinarySearchTree(convertArrayToBSTfromRootToLeaf(arr));
		balBST.treePrint();
		balBST.inorderTraverse();
		
		System.out.println("\n/** From leaf to root [O(n)] **/\n");
		balBST = new BinarySearchTree(convertArrayToBSTfromLeafToRoot(arr));
		balBST.treePrint();
		balBST.inorderTraverse();
		System.out.println("All rabbits gone.");
	}

}
