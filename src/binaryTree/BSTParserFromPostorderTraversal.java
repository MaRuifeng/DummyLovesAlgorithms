package binaryTree;

import java.util.Arrays;
import java.util.Stack;

import binaryTree.entities.BinarySearchTree;
import binaryTree.entities.TreeNode;
import utils.FunIntAlgorithm;

/**
 * Parse BST from a given post-order traversal. 
 * 
 * @author ruifengm
 * @since 2018-May-14 
 * 
 * https://www.geeksforgeeks.org/construct-a-binary-search-tree-from-given-postorder/
 */

public class BSTParserFromPostorderTraversal {
	
	/**
	 * From end to start, recursively traverse the array by looking for partition points
	 * for right and left sub-trees. 
	 * 
	 * Time complexity: upper bound O(N2), in case of complete trees, O(NlogN)
	 */
	private static TreeNode recursiveParse(int[] a, int end, int start) {
		if (end < start) return null; 
		if (end == start) return new TreeNode(a[end]); 
		TreeNode root = new TreeNode(a[end]); 
		int partition = end-1; 
		while (partition >= start && a[partition] > a[end]) partition--; 
		root.setRight(recursiveParse(a, end-1, partition+1));
		root.setLeft(recursiveParse(a, partition, start));
		return root;
	}
	private static BinarySearchTree recursiveParse(int[] a) {
		return new BinarySearchTree(recursiveParse(a, a.length-1, 0));
	}
	
	/**
	 * We try to optimize above recursive method to bring down its time complexity to O(N).
	 */
	private static TreeNode recursiveParseOptimized(int[] a, Stack<TreeNode> s, int min, int max) {
		if (!s.isEmpty() && s.peek().getKey() > min && s.peek().getKey() < max) {
			TreeNode root = s.pop();
			if (!s.isEmpty()) {
				root.setRight(recursiveParseOptimized(a, s, root.getKey(), max));
				root.setLeft(recursiveParseOptimized(a, s, min, root.getKey()));
			}
			return root;
		}
		return null; 
	}
	private static BinarySearchTree recursiveParseOptimized(int[] a) {
		Stack<TreeNode> nodeStack = new Stack<>();
		for (int i: a) nodeStack.push(new TreeNode(i));
		return new BinarySearchTree(recursiveParseOptimized(a, nodeStack, Integer.MIN_VALUE, Integer.MAX_VALUE));
	}
	
	/**
	 * Iterative O(N) method via a stack.
	 */
	private static BinarySearchTree iterativeParseViaStack(int[] a) {
		Stack<TreeNode> s = new Stack<>();
		TreeNode root = new TreeNode(a[a.length-1]);
		s.push(root);
		for (int i=a.length-2; i>=0; i--) {
			TreeNode node = new TreeNode(a[i]);
			if (a[i] > a[i+1]) {
				s.peek().setRight(node);
				s.push(node);
			}
			else {
				TreeNode temp = s.pop();
				while (!s.isEmpty() && s.peek().getKey() > node.getKey()) temp = s.pop();
				temp.setLeft(node);
				s.push(node);
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
		int[] fooPostOrderTraversal = foo.postOrderTraversalToArray();
		System.out.println("Its post-order traversal is: \n" + Arrays.toString(fooPostOrderTraversal) + "\n");
		System.out.println("[Iterative via Stack][O(N)] Parsing BST from the post-order traversal:");
		bst = iterativeParseViaStack(fooPostOrderTraversal);
		bst.levelOrderTraverse();
		bst.inorderTraverse();
		System.out.println("[Recursive][O(N2)] Parsing BST from the post-order traversal:");
		bst = recursiveParse(fooPostOrderTraversal);
		bst.levelOrderTraverse();
		bst.inorderTraverse();
		System.out.println("[Recursive][O(N)] Parsing BST from the post-order traversal:");
		bst = recursiveParseOptimized(fooPostOrderTraversal);
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
		System.out.println("********** Randome BST **********");
		bar.levelOrderTraverse();
		int[] barPostOrderTraversal = bar.postOrderTraversalToArray();
		System.out.println("Its post-order traversal is: \n" + Arrays.toString(barPostOrderTraversal) + "\n");
		System.out.println("[Iterative via Stack] Parsing BST from the post-order traversal:");
		bst = iterativeParseViaStack(barPostOrderTraversal);
		bst.levelOrderTraverse();
		bst.inorderTraverse();
		System.out.println("[Recursive] Parsing BST from the post-order traversal:");
		bst = recursiveParse(barPostOrderTraversal);
		bst.levelOrderTraverse();
		bst.inorderTraverse();
		System.out.println("[Recursive][O(N)] Parsing BST from the post-order traversal:");
		bst = recursiveParseOptimized(barPostOrderTraversal);
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
		int[] leftSkewPostOrderTraversal = leftSkew.postOrderTraversalToArray();
		System.out.println("Its post-order traversal is: \n" + Arrays.toString(leftSkewPostOrderTraversal) + "\n");
		System.out.println("[Iterative via Stack] Parsing BST from the post-order traversal:");
		bst = iterativeParseViaStack(leftSkewPostOrderTraversal);
		bst.levelOrderTraverse();
		bst.inorderTraverse();
		System.out.println("[Recursive] Parsing BST from the post-order traversal:");
		bst = recursiveParse(leftSkewPostOrderTraversal);
		bst.levelOrderTraverse();
		bst.inorderTraverse();
		System.out.println("[Recursive][O(N)] Parsing BST from the post-order traversal:");
		bst = recursiveParseOptimized(leftSkewPostOrderTraversal);
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
		int[] rightSkewPostOrderTraversal = rightSkew.postOrderTraversalToArray();
		System.out.println("Its post-order traversal is: \n" + Arrays.toString(rightSkewPostOrderTraversal) + "\n");
		System.out.println("[Iterative via Stack] Parsing BST from the post-order traversal:");
		bst = iterativeParseViaStack(rightSkewPostOrderTraversal);
		bst.levelOrderTraverse();
		bst.inorderTraverse();
		System.out.println("[Recursive] Parsing BST from the post-order traversal:");
		bst = recursiveParse(rightSkewPostOrderTraversal);
		bst.levelOrderTraverse();
		bst.inorderTraverse();
		System.out.println("[Recursive][O(N)] Parsing BST from the post-order traversal:");
		bst = recursiveParseOptimized(rightSkewPostOrderTraversal);
		bst.levelOrderTraverse();
		bst.inorderTraverse();
		
		System.out.println("All rabbits gone.");
	}

}
