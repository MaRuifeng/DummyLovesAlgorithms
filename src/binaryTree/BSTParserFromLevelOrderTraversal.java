package binaryTree;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

import binaryTree.entities.BinarySearchTree;
import binaryTree.entities.TreeNode;
import utils.FunIntAlgorithm;

/**
 * Parse BST from a given level order traversal. 
 * 
 * An intuitive but inefficient solution is to declare an empty binary search tree and insert the 
 * keys into it one by one. The time complexity of this method has an upper bound of n*H, where 
 * H is the height of the original BST. 
 * 
 * @author ruifengm
 * @since 2018-May-24
 * 
 * https://www.geeksforgeeks.org/construct-bst-given-level-order-traversal/
 */

public class BSTParserFromLevelOrderTraversal {
	
	/**
	 * Use a queue to keep track of tree nodes that have already been formed from the 
	 * level order traversal array. Some additional data will also be needed to keep the information
	 * regarding lower bounds for left sub-tree nodes and upper bounds for right sub-tree nodes. 
	 */
	static class NodeInfo {
		TreeNode node; 
		int min = Integer.MIN_VALUE, max = Integer.MAX_VALUE; // bounds for left sub-tree and right sub-tree
		public NodeInfo(TreeNode node) {
			this.node = node;
		}
	}
	private static BinarySearchTree parseViaQueue(int[] arr) {
		if (arr == null || arr.length == 0) return new BinarySearchTree();
		TreeNode root = new TreeNode(arr[0]);
		Queue<NodeInfo> nodeInfoQ = new LinkedList<>();
		NodeInfo rootInfo = new NodeInfo(root);
		nodeInfoQ.add(rootInfo);
		int i = 1;
		while (i < arr.length) {
			int count = nodeInfoQ.size(); 
			while (count > 0) {
				NodeInfo topNodeInfo = nodeInfoQ.remove();
				if (arr[i] < topNodeInfo.node.getKey() && arr[i] > topNodeInfo.min) {
					NodeInfo leftChildInfo = new NodeInfo(new TreeNode(arr[i++]));
					leftChildInfo.max = topNodeInfo.node.getKey();
					leftChildInfo.min = topNodeInfo.min;
					topNodeInfo.node.setLeft(leftChildInfo.node);
					if (i == arr.length) break;
					nodeInfoQ.add(leftChildInfo);
				}
				if (arr[i] > topNodeInfo.node.getKey() && arr[i] < topNodeInfo.max) {
					NodeInfo rightChildInfo = new NodeInfo(new TreeNode(arr[i++]));
					rightChildInfo.min = topNodeInfo.node.getKey();
					rightChildInfo.max = topNodeInfo.max;
					topNodeInfo.node.setRight(rightChildInfo.node);
					if (i == arr.length) break;
					nodeInfoQ.add(rightChildInfo);
				}
				count--; 
			}
		}
		return new BinarySearchTree(root);
	}
	
	public static void main(String[] args) {
		System.out.println("Welcome to the rabbit hole of BST parsers!\n"); 
		BinarySearchTree bst = new BinarySearchTree(); // result
		
		/* Balanced BST */
		int[] intArr = FunIntAlgorithm.genRanUniqueIntArr(8);
		FunIntAlgorithm.quickSort(intArr, 0, intArr.length-1);
		BinarySearchTree foo = new BinarySearchTree(BinarySearchTree.convertArrayToBST(intArr)); 
		System.out.println("********** Automatically generated balanced BST **********");
		foo.levelOrderTraverse();
		int[] fooLevelOrderTraversal = foo.levelOrderTraversalToArray();
		System.out.println("Its level-order traversal is: \n" + Arrays.toString(fooLevelOrderTraversal) + "\n");
		System.out.println("[Iterative via Queue][O(N)] Parsing BST from the level-order traversal:");
		bst = parseViaQueue(fooLevelOrderTraversal);
		bst.levelOrderTraverse();
		bst.inorderTraverse();
		
		/* Random BST */
		BinarySearchTree bar = new BinarySearchTree(); 
		bar.insert(10);
		bar.insert(5);
		bar.insert(15);
		bar.insert(4);
		bar.insert(7);
		bar.insert(14);
		bar.insert(18);
		bar.insert(6);
		bar.insert(9);
		bar.insert(16);
		bar.insert(2);
		bar.insert(19);
		bar.insert(20);
		System.out.println("********** Randome BST **********");
		bar.levelOrderTraverse();
		int[] barLevelOrderTraversal = bar.levelOrderTraversalToArray();
		System.out.println("Its level-order traversal is: \n" + Arrays.toString(barLevelOrderTraversal) + "\n");
		System.out.println("[Iterative via Queue] Parsing BST from the level-order traversal:");
		bst = parseViaQueue(barLevelOrderTraversal);
		bst.levelOrderTraverse();
		bst.inorderTraverse();
		
		
		/* Left skewed BST */
		BinarySearchTree leftSkew = new BinarySearchTree(); 
		leftSkew.insert(10);
		leftSkew.insert(9);
		leftSkew.insert(8);
		leftSkew.insert(7);
		System.out.println("********** Left skewed BST **********");
		leftSkew.levelOrderTraverse();
		int[] leftSkewLevelOrderTraversal = leftSkew.levelOrderTraversalToArray();
		System.out.println("Its level-order traversal is: \n" + Arrays.toString(leftSkewLevelOrderTraversal) + "\n");
		System.out.println("[Iterative via Queue] Parsing BST from the level-order traversal:");
		bst = parseViaQueue(leftSkewLevelOrderTraversal);
		bst.levelOrderTraverse();
		bst.inorderTraverse();
		
		/* Right skewed BST */
		BinarySearchTree rightSkew = new BinarySearchTree();
		rightSkew.insert(1);
		rightSkew.insert(2);
		rightSkew.insert(3);
		rightSkew.insert(4);
		System.out.println("********** Right skewed BST **********");
		rightSkew.levelOrderTraverse();
		int[] rightSkewLevelOrderTraversal = rightSkew.levelOrderTraversalToArray();
		System.out.println("Its level-order traversal is: \n" + Arrays.toString(rightSkewLevelOrderTraversal) + "\n");
		System.out.println("[Iterative via Queue] Parsing BST from the level-order traversal:");
		bst = parseViaQueue(rightSkewLevelOrderTraversal);
		bst.levelOrderTraverse();
		bst.inorderTraverse();
		
		System.out.println("All rabbits gone.");
	}
}
