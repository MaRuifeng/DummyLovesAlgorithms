package binaryTree;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

import binaryTree.entities.BinarySearchTree;
import binaryTree.entities.TreeNode;
import utils.FunIntAlgorithm;

/**
 * Parse BST from a given pre-order traversal. 
 * 
 * Since the traversal array is in pre-order, we can just construct back the BST by inserting the element into 
 * an empty BST from left to right. The time complexity of this method has an upper bound of n*H, where 
 * H is the height of the original BST. 
 * 
 * @author ruifengm
 * @since 2018-May-14 
 * 
 * https://www.geeksforgeeks.org/construct-bst-from-given-preorder-traversal-set-2/
 */
public class BSTParserFromPreorderTraversal {
	
	/**
	 * Initiate an empty stack, push the first of the array as root and traverse the remaining array
	 * 1) while node is smaller than stack top, make it the left child to current stack top and push into stack
	 * 2) when encountering node larger than current, pop from stack till the top node is larger, make the encountered 
	 *    node the right child to the last popped node and push it into stack
	 * 3) repeat until end of array
	 * 
	 * Time complexity: O(N)  <-- since every node is at most pushed and popped once by the stack
	 */
	private static BinarySearchTree iterativeParseViaStack(int[] a) {
		if (a.length == 0) return new BinarySearchTree();
		Stack<TreeNode> s = new Stack<>(); 
		TreeNode root = new TreeNode(a[0]), node;
		s.push(root);
		for (int i=1; i<a.length; i++) {
			node = new TreeNode(a[i]);
			if (node.getKey() < s.peek().getKey()) {
				s.peek().setLeft(node);
				s.push(node);
			}
			else {
				TreeNode temp = s.pop();
				while (!s.isEmpty() && s.peek().getKey() < node.getKey()) temp = s.pop();
				temp.setRight(node);
				s.push(node);
			}
		}
		return new BinarySearchTree(root);
	}
	
	/**
	 * Parse by recursion. 
	 * Since for any given node in a BST, all nodes in its left sub-tree are smaller than the node, and 
	 * all nodes in its right sub-tree are larger, we can traverse the array and look for such partition 
	 * points, and then partition the array and parse sub-trees. 
	 * 
	 * Time complexity: upper bound O(N2), in case of complete trees, O(NLogN)
	 * Note that the time complexity of this method is no better than directly inserting into an empty BST.
	 */
	private static TreeNode recursiveParse(int[] a, int start, int end) {
		if (start > end) return null;
		if (start == end) return new TreeNode(a[start]); // not necessary but good to keep so as to reduce call recursive call stacks
		TreeNode root = new TreeNode(a[start]);
		// look for end of left sub-tree and start of right sub-tree
		int partition = start+1; 
		while (partition <= end && a[partition] < a[start]) partition++;
		root.setLeft(recursiveParse(a, start+1, partition-1));
		root.setRight(recursiveParse(a, partition, end));
		return root;
	}
	private static BinarySearchTree recursiveParseDriver(int[] a) {
		return new BinarySearchTree(recursiveParse(a, 0, a.length-1));
	}
	
	/**
	 * We try to optimize the recursive method to reduce the time complexity. 
	 * A dynamic integer range can be used to decide whether to continue parsing or not. 
	 * Look at the recursiveDeserialize(Queue<String> list) method in the BinaryTree class for reference. 
	 * Time complexity: O(N)
	 */
	private static TreeNode recursiveParseOptimized(int[] a, Queue<TreeNode> q, int min, int max) {
		if (!q.isEmpty() && q.peek().getKey() > min && q.peek().getKey() < max) {
			TreeNode node = q.poll();
			if (!q.isEmpty()) {
				node.setLeft(recursiveParseOptimized(a, q, min, node.getKey()));
				node.setRight(recursiveParseOptimized(a, q, node.getKey(), max));
			}
			return node;
		}
		return null;
	}
	private static BinarySearchTree recursiveParseOptimized(int[] a) {
		Queue<TreeNode> nodeQ = new LinkedList<>();
		for (int i: a) nodeQ.add(new TreeNode(i));
		return new BinarySearchTree(recursiveParseOptimized(a, nodeQ, Integer.MIN_VALUE, Integer.MAX_VALUE));
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
		int[] fooPreOrderTraversal = foo.postOrderTraversalToArray();
		System.out.println("Its pre-order traversal is: \n" + Arrays.toString(fooPreOrderTraversal) + "\n");
		System.out.println("[Iterative via Stack][O(N)] Parsing BST from the pre-order traversal:");
		bst = iterativeParseViaStack(fooPreOrderTraversal);
		bst.levelOrderTraverse();
		bst.inorderTraverse();
		System.out.println("[Recursive][O(N2)] Parsing BST from the pre-order traversal:");
		bst = recursiveParseDriver(fooPreOrderTraversal);
		bst.levelOrderTraverse();
		bst.inorderTraverse();
		System.out.println("[Recursive][O(N)] Parsing BST from the pre-order traversal:");
		bst = recursiveParseOptimized(fooPreOrderTraversal);
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
		int[] barPreOrderTraversal = bar.postOrderTraversalToArray();
		System.out.println("Its pre-order traversal is: \n" + Arrays.toString(barPreOrderTraversal) + "\n");
		System.out.println("[Iterative via Stack] Parsing BST from the pre-order traversal:");
		bst = iterativeParseViaStack(barPreOrderTraversal);
		bst.levelOrderTraverse();
		bst.inorderTraverse();
		System.out.println("[Recursive] Parsing BST from the pre-order traversal:");
		bst = recursiveParseDriver(barPreOrderTraversal);
		bst.levelOrderTraverse();
		bst.inorderTraverse();
		System.out.println("[Recursive][O(N)] Parsing BST from the pre-order traversal:");
		bst = recursiveParseOptimized(barPreOrderTraversal);
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
		int[] leftSkewPreOrderTraversal = leftSkew.postOrderTraversalToArray();
		System.out.println("Its pre-order traversal is: \n" + Arrays.toString(leftSkewPreOrderTraversal) + "\n");
		System.out.println("[Iterative via Stack] Parsing BST from the pre-order traversal:");
		bst = iterativeParseViaStack(leftSkewPreOrderTraversal);
		bst.levelOrderTraverse();
		bst.inorderTraverse();
		System.out.println("[Recursive] Parsing BST from the pre-order traversal:");
		bst = recursiveParseDriver(leftSkewPreOrderTraversal);
		bst.levelOrderTraverse();
		bst.inorderTraverse();
		System.out.println("[Recursive][O(N)] Parsing BST from the pre-order traversal:");
		bst = recursiveParseOptimized(leftSkewPreOrderTraversal);
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
		int[] rightSkewPreOrderTraversal = rightSkew.postOrderTraversalToArray();
		System.out.println("Its pre-order traversal is: \n" + Arrays.toString(rightSkewPreOrderTraversal) + "\n");
		System.out.println("[Iterative via Stack] Parsing BST from the pre-order traversal:");
		bst = iterativeParseViaStack(rightSkewPreOrderTraversal);
		bst.levelOrderTraverse();
		bst.inorderTraverse();
		System.out.println("[Recursive] Parsing BST from the pre-order traversal:");
		bst = recursiveParseDriver(rightSkewPreOrderTraversal);
		bst.levelOrderTraverse();
		bst.inorderTraverse();
		System.out.println("[Recursive][O(N)] Parsing BST from the pre-order traversal:");
		bst = recursiveParseOptimized(rightSkewPreOrderTraversal);
		bst.levelOrderTraverse();
		bst.inorderTraverse();
		
		System.out.println("All rabbits gone.");
	}
}
