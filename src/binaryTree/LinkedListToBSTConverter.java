package binaryTree;

import java.util.Iterator;
import java.util.LinkedList;

import binaryTree.entities.BinarySearchTree;
import binaryTree.entities.TreeNode;

/**
 * Convert a singly linked list of sorted unique integers in ascending order
 * into a balanced binary search tree. 
 * 
 * @author ruifengm
 * @since 2018-May-20
 * 
 */

public class LinkedListToBSTConverter {
	
	/**
	 * Construct the BST from root to leaves via binary division. 
	 * Time complexity: O(nlogn)
	 */
	private static BinarySearchTree convertSortedLinkedListToBSTfromRootToLeaf(LinkedList<Integer> list) {
		return new BinarySearchTree(convertSortedLinkedListToBSTfromRootToLeaf(list, 0, list.size()-1));
	}
	private static TreeNode convertSortedLinkedListToBSTfromRootToLeaf(LinkedList<Integer> list, int start, int end) {
		if (start > end) return null;
		int rootPos = start + (end-start)/2;
		//int rootPos = (start + end) >> 1;
		TreeNode root = new TreeNode(list.get(rootPos)); 
		root.setLeft(convertSortedLinkedListToBSTfromRootToLeaf(list, start, rootPos-1));
		root.setRight(convertSortedLinkedListToBSTfromRootToLeaf(list, rootPos + 1, end));
		return root;
	}
	
	/**
	 * Construct the BST from leaves to root. 
	 * Time complexity: O(n)
	 */
	private static BinarySearchTree convertSortedLinkedListToBSTfromLeafToRoot(LinkedList<Integer> list) {
		return new BinarySearchTree(convertSortedLinkedListToBSTfromLeafToRoot(list, list.iterator(), list.size()));
	}
	private static TreeNode convertSortedLinkedListToBSTfromLeafToRoot(LinkedList<Integer> list, Iterator<Integer> iter, int size) {
		if (size <= 0) return null; 
		TreeNode left = convertSortedLinkedListToBSTfromLeafToRoot(list, iter, size/2);  // left sub-tree size: size/2
		TreeNode root = new TreeNode(list.get(iter.next()));
		root.setLeft(left);
		TreeNode right = convertSortedLinkedListToBSTfromLeafToRoot(list, iter, size - size/2 - 1); // right sub-tree size: size - size/2 -1
		root.setRight(right);
		return root;
	}
	
	public static void main(String[] args) {
		System.out.println("/** Converting a random unique integer singly linked list into a balanced BST. **/");
		LinkedList<Integer> list = new LinkedList<>();
		for (int i=0; i<20; i++) list.add(i);
		System.out.println("The randomly generated list is \n" + list.toString());
		System.out.println("\n/** From root to leaf [O(nlogn)] **/\n");
		BinarySearchTree balBST = convertSortedLinkedListToBSTfromRootToLeaf(list);
		balBST.treePrint();
		balBST.inorderTraverse();
		System.out.println("\n/** From leaf to root [O(n)] **/\n");
		balBST = convertSortedLinkedListToBSTfromLeafToRoot(list); 
		balBST.treePrint();
		balBST.inorderTraverse();
		System.out.println("All rabbits gone.");
	}

}
