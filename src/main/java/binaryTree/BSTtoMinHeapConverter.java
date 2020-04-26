package binaryTree;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

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
	
	/**
	 * Convert to a min-heap by fixing the pointers in place. 
	 */
	private static TreeNode heapRoot = null; // root in the min-heap
	private static TreeNode heapNode = null; // node in the min-heap
	private static int heapNodeIndex = 0; 
	private static boolean heapRootFound = false; 
	private static void convertToMinHeapByPointerFix(BinarySearchTree bst) {
		fixPointers(bst.getRoot());
		bst.setRoot(heapRoot);
	}
	private static void fixPointers(TreeNode root) {
		if (root != null) {
			fixPointers(root.getLeft());
			root.setLeft(null);
			if (heapNode != null) {
				if (heapNode.getLeft() != null && heapNode.getRight() != null)
					heapNode = getHeapNodeAtLevelOrderIndex(heapRoot, ++heapNodeIndex); // set new heap node
				if (heapNode.getLeft() == null) heapNode.setLeft(root);
				else if (heapNode.getRight() == null) heapNode.setRight(root);
			}
			if (heapRootFound == false) { // found the smallest node in tree as heap root
				heapRoot = heapNode = root;
				heapRootFound = true; 
			}
			fixPointers(root.getRight());
			root.setRight(null);
		}
	}
	private static TreeNode getHeapNodeAtLevelOrderIndex(TreeNode root, int idx) {
		Queue<TreeNode> nodeQ = new LinkedList<>();
		nodeQ.add(root);
		int levelSize = 1, coverage = 1;
		while (idx > 0) {
			int i = coverage;
			coverage += Math.min(levelSize, idx);
			for (;i<coverage;i++) {
				TreeNode top = nodeQ.poll();
				if (top.getLeft() != null) {
					nodeQ.add(top.getLeft());
					if (--idx == 0) break;
				}
				if (top.getRight() != null) {
					nodeQ.add(top.getRight());
					if (--idx == 0) break;
				}
			}
			levelSize *= 2;
		}
		TreeNode node = null; 
		while (!nodeQ.isEmpty()) node = nodeQ.poll();
		return node;
	}
	
	public static void main(String[] args) {
		BinarySearchTree bst = new BinarySearchTree(); 
		bst.insert(8);
		bst.insert(4);
		bst.insert(12);
		bst.insert(2);
		bst.insert(6);
		bst.insert(10);
		bst.insert(14);
		bst.insert(1);
		bst.insert(20);
		bst.insert(7);
		BinarySearchTree bstCopy = null;
		try {
		    bstCopy = bst.clone(BinarySearchTree.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("/** Converting a BST to a min-heap in O(n) time. **/");
		System.out.println("The original BST is: \n");
		bst.levelOrderTraverse();
		convertToMinHeap(bst);
		System.out.println("The min-heap tree is: \n");
		bst.levelOrderTraverse();
		
		System.out.println("/** Converting a BST to a min-heap in place. **/");
		System.out.println("The original BST is: \n");
		bstCopy.levelOrderTraverse();
		convertToMinHeapByPointerFix(bstCopy);
		System.out.println("The min-heap tree is: \n");
		bstCopy.levelOrderTraverse();
		
		System.out.println("All rabbits gone.");
	}

}
