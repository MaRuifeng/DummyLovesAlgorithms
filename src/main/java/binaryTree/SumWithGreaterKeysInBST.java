package binaryTree;

import binaryTree.entities.BinarySearchTree;
import binaryTree.entities.BinaryTree;
import binaryTree.entities.TreeNode;

/**
 * Given a Binary Search Tree (BST), convert it to a Binary Tree such that every key 
 * of the original BST is changed to key plus sum of all greater keys in BST.
 * 
 * E.g.      5                    18
 *         /   \       -->      /    \
 *        2    13              20    13
 *        
 * @author ruifengm
 * @since 2018-May-19
 * 
 * https://www.geeksforgeeks.org/convert-bst-to-a-binary-tree/
 */
public class SumWithGreaterKeysInBST {
	
	static class Sum { // wrapper class for primitive int since Java does not pass by reference (address)
		int val = 0; 
	}
	
	/**
	 * Traverse the BST in reverse order and sum up along the way. 
	 */
	private static BinaryTree sumWithGreaterKeys(BinarySearchTree bst) {
		sumWithGreaterKeys(bst.getRoot(), new Sum());
		return new BinaryTree(bst.getRoot());
	}
	private static void sumWithGreaterKeys(TreeNode root, Sum sum) {
		if (root != null) {
			sumWithGreaterKeys(root.getRight(), sum);
			sum.val += root.getKey();
			root.setKey(sum.val);
			sumWithGreaterKeys(root.getLeft(), sum);
		}
	}
	
	public static void main(String[] args) {
		BinarySearchTree bst = new BinarySearchTree(); 
		bst.insert(5);
		bst.insert(2);
		bst.insert(13);
		bst.insert(1);
		bst.insert(4);
		bst.insert(11);
		bst.insert(15);
		bst.levelOrderTraverse();
		
		BinaryTree bt = sumWithGreaterKeys(bst);
		System.out.println("After summing up greater keys...\n");
		bt.levelOrderTraverse();
	}

}
