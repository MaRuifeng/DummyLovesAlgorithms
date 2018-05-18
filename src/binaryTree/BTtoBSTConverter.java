package binaryTree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import binaryTree.entities.BinarySearchTree;
import binaryTree.entities.BinaryTree;
import binaryTree.entities.TreeNode;
import utils.FunIntAlgorithm;

/**
 * Convert a binary tree into a binary search tree with original tree structure maintained. 
 * 
 * 
 * @author ruifengm
 * @since 2018-May-18
 * 
 * https://www.geeksforgeeks.org/binary-tree-to-binary-search-tree-conversion/
 */

public class BTtoBSTConverter {
	
	/**
	 * Step 1: store all node value into a list, and replace the value of each node with its index in the in-order traversal
	 * Step 2: sort the list
	 * Step 3: assign the node values back into the tree based on the sorted order
	 */
	private static void convert(TreeNode root) {
		List<Integer> nodeKeyList = new ArrayList<>(); 
		countAndStoreNodeIntoList(root, nodeKeyList);
		int[] nodeKeyArr = nodeKeyList.stream().mapToInt(i -> i).toArray(); // not really necessary, but primitive is the lover
		FunIntAlgorithm.quickSort(nodeKeyArr, 0, nodeKeyArr.length-1);
		formBST(root, nodeKeyArr);
	}
	/**
	 * In-order traverse the tree, store node value into a list and replace node value
	 * with its sequence in the traversal.
	 * 
	 */
	private static void countAndStoreNodeIntoList(TreeNode root, List<Integer> nodeKeyList) {
		if (root != null) {
			countAndStoreNodeIntoList(root.getLeft(), nodeKeyList); 
			nodeKeyList.add(root.getKey());
			root.setKey(nodeKeyList.size()-1);
			countAndStoreNodeIntoList(root.getRight(), nodeKeyList);
		}
	}
	/**
	 * Assign sorted tree node values back into the tree to form the BST. 
	 */
	private static void formBST(TreeNode root, int[] nodeKeyArr) {
		if (root != null) {
			formBST(root.getLeft(), nodeKeyArr);
			root.setKey(nodeKeyArr[root.getKey()]);
			formBST(root.getRight(), nodeKeyArr);
		}
	}
	
	/* Note if we use inorder traversal as well to form the BST, then there is no need to record node sequence. */
	private static void convertViaQueue(TreeNode root) {
		List<Integer> nodeKeyList = new ArrayList<>(); 
		countAndStoreNodeIntoList(root, nodeKeyList);
		Collections.sort(nodeKeyList);
		formBST(root, new LinkedList<>(nodeKeyList));
	}
	private static void formBST(TreeNode root, Queue<Integer> nodeQ) {
		if (root != null) {
			formBST(root.getLeft(), nodeQ);
			root.setKey(nodeQ.poll().intValue());
			formBST(root.getRight(), nodeQ);
		}
	}
	
	public static void main(String[] args) {
		TreeNode root = new TreeNode(10); 
		root.setLeft(new TreeNode(2));
		root.setRight(new TreeNode(7));
		root.getLeft().setLeft(new TreeNode(8));
		root.getLeft().setRight(new TreeNode(4));
		
		BinaryTree bt = new BinaryTree(root); 
		BinaryTree btClone = bt.clone();
		
		bt.levelOrderTraverse();
		System.out.println("Is BST? " + BinarySearchTree.isBinarySearchTree(bt) + "\n");
		convert(bt.getRoot());
		bt.levelOrderTraverse();
		System.out.println("Is BST? " + BinarySearchTree.isBinarySearchTree(bt) + "\n");
		
		btClone.levelOrderTraverse();
		System.out.println("Is BST? " + BinarySearchTree.isBinarySearchTree(btClone) + "\n");
		convertViaQueue(btClone.getRoot());
		btClone.levelOrderTraverse();
		System.out.println("Is BST? " + BinarySearchTree.isBinarySearchTree(btClone) + "\n");
		
		root = new TreeNode(10);
		root.setLeft(new TreeNode(30));
		root.setRight(new TreeNode(15));
		root.getLeft().setLeft(new TreeNode(20));
		root.getRight().setRight(new TreeNode(5));
		
		bt = new BinaryTree(root);
		bt.levelOrderTraverse();
		System.out.println("Is BST? " + BinarySearchTree.isBinarySearchTree(bt) + "\n");
		convertViaQueue(bt.getRoot());
		bt.levelOrderTraverse();
		System.out.println("Is BST? " + BinarySearchTree.isBinarySearchTree(bt) + "\n");
	}

}
