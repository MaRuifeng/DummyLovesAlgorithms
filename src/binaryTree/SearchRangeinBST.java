package binaryTree;

import java.util.ArrayList;
import java.util.List;

import binaryTree.entities.BinarySearchTree;
import binaryTree.entities.TreeNode;

/**
 * Given two values k1 and k2 (where k1 < k2) and a root pointer to a Binary Search Tree. Find all the keys of tree in 
 * range k1 to k2. i.e. print all x such that k1 <= x <= k2 and x is a key of given BST. Return all the keys in ascending order.
 * 
 * @author ruifengm
 * @since 2018-May-19
 * 
 * https://www.lintcode.com/problem/search-range-in-binary-search-tree/
 */
public class SearchRangeInBST {
	
	/**
	 * Run inorder traversal of the BST and find items in range.
	 */
	private static List<Integer> getRangeInBSTviaInOrderTraversal(BinarySearchTree bst, int left, int right) {
		ArrayList<Integer> range = new ArrayList<>(); 
		getRangeInBSTviaInOrderTraversal(bst.getRoot(), left, right, range);
		return range;
	}
	private static void getRangeInBSTviaInOrderTraversal(TreeNode root, int l, int r, List<Integer> list) {
		if (root != null) {
			getRangeInBSTviaInOrderTraversal(root.getLeft(), l, r, list);
			if (root.getKey() <= r && root.getKey() >= l) list.add(root.getKey()); 
			else if (root.getKey() > r) return;
			getRangeInBSTviaInOrderTraversal(root.getRight(), l, r, list);
		}
	}
	
	/**
	 * A more efficient way is to divide and conquer. 
	 */
	private static List<Integer> getRangeInBSTviaDivideAndConquer(TreeNode root, int l, int r) {
		if (root == null) return new ArrayList<>();
		if (root.getKey() < l) return getRangeInBST(root.getRight(), l, r); // range in right sub-tree
		if (root.getKey() > r) return getRangeInBST(root.getLeft(), l, r);  // range in left sub-tree
		// range spans across
		List<Integer> list = getRangeInBST(root.getLeft(), l, root.getKey());
		list.add(root.getKey());
		list.addAll(getRangeInBST(root.getRight(), root.getKey(), r));
		return list;
	}
	
	/**
	 * More elegantly written...
	 */
	private static List<Integer> getRangeInBST(TreeNode root, int l, int r) {
		List<Integer> list = new ArrayList<>(); 
		getRangeInBST(root, l, r, list);
		return list;
	}
	private static void getRangeInBST(TreeNode root, int l, int r, List<Integer> list) {
		if (root == null) return;
		if (root.getKey() > l) getRangeInBST(root.getLeft(), l, r, list);  // range extends over to left-subtree
		if (root.getKey() >= l && root.getKey() <= r) list.add(root.getKey()); // item found in range, collect
		if (root.getKey() < r) getRangeInBST(root.getRight(), l, r, list); // range extends over to right sub-tree
	}

	public static void main(String[] args) {
		BinarySearchTree bst = new BinarySearchTree(); 
		bst.insert(20);
		bst.insert(8);
		bst.insert(22);
		bst.insert(4);
		bst.insert(12);
		
		bst.levelOrderTraverse();
		
		/* In-order traversal */
		List<Integer> range1 = getRangeInBSTviaInOrderTraversal(bst, 10, 22);
		System.out.println(range1.toString());
		List<Integer> range2 = getRangeInBSTviaInOrderTraversal(bst, 0, 2);
		System.out.println(range2.toString());
		List<Integer> range3 = getRangeInBSTviaInOrderTraversal(bst, 50, 100);
		System.out.println(range3.toString());
		List<Integer> range4 = getRangeInBSTviaInOrderTraversal(bst, 4, 22);
		System.out.println(range4.toString());
		List<Integer> range5 = getRangeInBSTviaInOrderTraversal(bst, 0, 13);
		System.out.println(range5.toString());
		
		/* Divide and conquer */
		List<Integer> range6 = getRangeInBST(bst.getRoot(), 8, 22);
		System.out.println(range6.toString());
	}
}
