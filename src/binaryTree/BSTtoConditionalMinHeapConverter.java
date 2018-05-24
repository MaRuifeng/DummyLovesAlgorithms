package binaryTree;

import binaryTree.entities.BinarySearchTree;
import binaryTree.entities.TreeNode;

/**
 * Convert a complete binary search tree to a min-heap with the condition that 
 * all left sub-tree values of a node should be smaller than any value in the right 
 * sub-tree. 
 * 
 * @author ruifengm
 * @since 2018-May-24
 * 
 * https://www.geeksforgeeks.org/convert-bst-min-heap/
 *
 */

public class BSTtoConditionalMinHeapConverter {
	
	/**
	 * Step 1): take out smallest node as root of the final min-heap, substitute subsequent values inorder, and then substitute 
	 *          the rightmost node in left sub-tree with root, so as to maintain left sub-tree completeness
	 * Step 2): convert the left sub-tree and right sub-tree into min-heaps (recursively)
	 * Step 3): connect them to the root of the final min-heap
	 */
	private static void inplaceConvertToConditionalMinHeap(BinarySearchTree bst) {
		bst.setRoot(inplaceConvertToConditionalMinHeap(bst.getRoot()));
	}
	private static TreeNode prev;
	private static TreeNode heapNode;
	private static TreeNode inplaceConvertToConditionalMinHeap(TreeNode root) {
		if (root == null || root.getLeft() == null && root.getRight() == null) return root;
		// get smallest node value as heap root and maintain left sub-tree completeness
		prev = null;
		if (root.getLeft() != null) maintainCompleteBST(root.getLeft()); 
		prev.setKey(root.getKey()); // substitute the rightmost node in left sub-tree with root
		TreeNode heapRoot = heapNode;
		TreeNode leftHeap = null, rightHeap = null;
		// convert left sub-tree
		if (root.getLeft() != null) leftHeap = inplaceConvertToConditionalMinHeap(root.getLeft());
		// convert right sub-tree
		if (root.getRight() != null) rightHeap = inplaceConvertToConditionalMinHeap(root.getRight());
		heapRoot.setLeft(leftHeap);
		heapRoot.setRight(rightHeap);
		return heapRoot;
	}
	private static void maintainCompleteBST(TreeNode root) {
		if (root != null) {
			maintainCompleteBST(root.getLeft());
			if (prev == null) heapNode = new TreeNode(root.getKey());
			else prev.setKey(root.getKey());
			prev = root;
			maintainCompleteBST(root.getRight());
		}
	}
	
	/**
	 * Convert via an auxiliary helper array. 
	 * Step 1): run invorder traversal to store all node keys into an array in ascending order. 
	 * Step 2): run pre-order traversal to put the node keys back into the tree from the array.
	 */
	static class Index {
		int val = 0; 
	}
	private static void ConvertToConditionalMinHeapViaArray(BinarySearchTree bst) {
		int[] inorderArr = bst.inorderTraversalToArray();
		getConditionalMinHeapFromArray(bst.getRoot(), inorderArr, new Index());
	}
	private static void getConditionalMinHeapFromArray(TreeNode root, int[] arr, Index idx) {
		if (root != null) {
			// set root
			root.setKey(arr[idx.val++]);
			// set left sub-tree (depleting smaller keys from the array)
			getConditionalMinHeapFromArray(root.getLeft(), arr, idx);
			// set right sub-tree (depleting remaining keys from the array)
			getConditionalMinHeapFromArray(root.getRight(), arr, idx);
		}
	}
	
	public static void main(String[] args) {
		System.out.println("/** Converting a complete BST to the conditional min-heap. **/");
		BinarySearchTree bst = new BinarySearchTree(); 
		bst.insert(8);
		bst.insert(4);
		bst.insert(12);
		bst.insert(2);
		bst.insert(6);
		bst.insert(10);
		bst.insert(14);
		BinarySearchTree bstCopy = null;
		try {
			bstCopy = bst.clone(BinarySearchTree.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		System.out.println("The original BST is: \n");
		bst.levelOrderTraverse();
		
		inplaceConvertToConditionalMinHeap(bst);
		System.out.println("After in-place conversion, the conditional min-heap tree is: \n");
		bst.levelOrderTraverse();
		
		ConvertToConditionalMinHeapViaArray(bstCopy);
		System.out.println("After conversion via an helper array, the conditional min-heap tree is: \n");
		bstCopy.levelOrderTraverse();
		
		System.out.println("All rabbits gone.");
	}
}
