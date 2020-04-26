package binaryTree;

import binaryTree.entities.BinarySearchTree;
import binaryTree.entities.BinaryTree;
import binaryTree.entities.TreeNode;

/**
 * Convert a binary search tree to a greater sum tree where each node contains 
 * sum of nodes greater than itself. 
 * 
 * E.g.      5                    13
 *         /   \       -->      /    \
 *        2    13              18     0
 *        
 * @author ruifengm
 * @since 2018-May-20
 *
 * https://www.geeksforgeeks.org/transform-bst-sum-tree/
 */

public class BSTtoGreaterSumTreeConverter {
	
	/**
	 * Traverse the BST in reverse order and sum up along the way.
	 */
	static class Sum {
		int val = 0; 
	}
	public static BinaryTree convertToGreaterSumTree(BinarySearchTree bst) {
		sumUpGreaterKeys(bst.getRoot(), new Sum());
		return new BinaryTree(bst.getRoot());
	}
	public static void sumUpGreaterKeys(TreeNode root, Sum sum) {
		if (root != null) {
			sumUpGreaterKeys(root.getRight(), sum);
			int temp = root.getKey();
			root.setKey(sum.val);
			sum.val = Math.addExact(temp, sum.val);
			sumUpGreaterKeys(root.getLeft(), sum);
		}
	}
	
	public static void main(String[] args) {
		System.out.println("/** Converting a BST to a greater sum tree. **/");
		BinarySearchTree bst = new BinarySearchTree(); 
		bst.insert(11);
		bst.insert(2);
		bst.insert(29);
		bst.insert(1);
		bst.insert(7);
		bst.insert(15);
		bst.insert(40);
		bst.insert(35);
		
		System.out.println("The original BST is: \n");
		bst.levelOrderTraverse();
		BinaryTree bt = convertToGreaterSumTree(bst);
		System.out.println("The greater sum tree is: \n");
		bt.levelOrderTraverse();
		System.out.println("All rabbits gone.");
	}

}
