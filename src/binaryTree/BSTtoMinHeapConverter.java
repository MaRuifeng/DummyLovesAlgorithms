package binaryTree;
import java.util.ArrayList;

import binaryTree.entities.BinarySearchTree;
import binaryTree.entities.TreeNode;

/** 
 * Given a binary search tree, convert it into a min heap containing the same 
 * elements. 
 * 
 * @author ruifengm
 * @since 2018-May-23
 */

public class BSTtoMinHeapConverter {
	
	/**
	 * Get inorder traversal array of the BST and construct the min heap (a complete binary tree) accordingly. 
	 */
	private static void convertToMinHeap(BinarySearchTree bst) {
		ArrayList<TreeNode> list = new ArrayList<TreeNode>(); 
		getInorderTraversalList(bst.getRoot(), list);
		int levelSize = 1, coverage = 0, remain = list.size();
		while (remain > 0) {
			int i = coverage;
			coverage += Math.min(levelSize, remain);
			for (;i<coverage;i++) {
				TreeNode parent = list.get(i);
				int left = getLeftChildKey(i), right = getRightChildKey(i);
				if (left < list.size()) parent.setLeft(list.get(left));
				else parent.setLeft(null);
				if (right < list.size()) parent.setRight(list.get(right));
				else parent.setRight(null);
			}
			remain -= levelSize;
			levelSize *= 2;
		}
		bst.setRoot(list.get(0));
	}
	private static void getInorderTraversalList(TreeNode node, ArrayList<TreeNode> list) {
		if (node != null) {
			getInorderTraversalList(node.getLeft(), list);
			list.add(node);
			getInorderTraversalList(node.getRight(), list);
		}
	}
	private static int getLeftChildKey(int i) { return 2*i + 1; }
	private static int getRightChildKey(int i) { return 2*i + 2; }
	
	
	public static void main(String[] args) {
		System.out.println("/** Converting a BST to a min-heap in O(n) time. **/");
		BinarySearchTree bst = new BinarySearchTree(); 
		bst.insert(8);
		bst.insert(4);
		bst.insert(12);
		bst.insert(2);
		bst.insert(6);
		bst.insert(10);
		bst.insert(14);
		
		System.out.println("The original BST is: \n");
		bst.levelOrderTraverse();
		convertToMinHeap(bst);
		System.out.println("The min-heap tree is: \n");
		bst.levelOrderTraverse();
		
		System.out.println("All rabbits gone.");
	}

}
