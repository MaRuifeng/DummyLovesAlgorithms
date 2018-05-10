package binarySearchTree;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

import utils.FunIntAlgorithm;

/**
 * Implement a binary search tree of integers that supports below functions using linked nodes. 
 * 
 * 1) search
 * 2) insert
 * 3) delete
 * 
 * Based on materials read from geeksforgeeks.org
 * 
 * @author ruifengm
 * @since 2018-Apr-13
 *
 */
public class BinarySearchTree {
	// Node class in BST
	class Node {
		int key; 
		Node left, right;
		
		public Node(int item) {
			key = item;
			left = right = null;
		}
	}
	
	Node root; // attribute of the BST

	public BinarySearchTree() { // constructor
		root = null;
	}
	public BinarySearchTree(Node node) {
		root = node;
	}
	
	/* Insert a new key by recursion */
	void insert(int key) {
		root = insertItem(root, key);
	}
	Node insertItem(Node node, int key) {
		// return a new node if tree is empty
		if (node == null) return new Node(key); 
		// else recur down the tree
		if (key < node.key) // left side
			node.left = insertItem(node.left, key);
		else if (key > node.key) // right side, and no duplicates allowed in BST
			node.right = insertItem(node.right, key);
		return node;
	}
	
	/* Search a key by recursion */
	boolean contains(int key) {
		return containsItem(root, key);
	}
	boolean containsItem(Node node, int key) {
		if (node == null) return false; // empty tree
		if (node.key == key) return true; 
		else if (key < node.key) // recur left
			return containsItem(node.left, key); 
		else // recur right
			return containsItem(node.right, key);
	}
	
	/* Delete a key by recursion */
	void delete(int key) {
		root = deleteItem(root, key);
	}
	Node deleteItem(Node node, int key) {
		// return null if tree is empty
		if (node == null) return node; 
		// else recur down the tree
		if (key < node.key) node.left = deleteItem(node.left, key);
		else if (key > node.key) node.right = deleteItem(node.right, key);
		else { // key found
			// node with only one child or no child
			if (node.left == null) return node.right;
			if (node.right == null) return node.left;
			// node with two children, look for the inorder successor
			node.key = minVal(node.right); // set current node value to the value of the inorder sucessor
			node.right = deleteItem(node.right, node.key); // delete the successor
		}
		return node;
	}
	int minVal(Node node) { // get minimal value by searching from the given node
		int min = node.key;
		while (node.left != null) {
			min = node.left.key;
			node = node.left;
		}
		return min;
	}
	
	/* Traverse BST in order from the given node */
	void inorderTraverse(Node node) {
		if (node != null) {
			inorderTraverse(node.left); // smaller ones
			System.out.print(node.key + " ");
			inorderTraverse(node.right); // larger ones
		}
	}
	
	/* Find tree height (defined as number of nodes from root to farthest leaf) */
	int recursiveHeight() {
		return recursiveFindHeight(root);
	}
	int recursiveFindHeight(Node node) {
		if (node == null) return 0;  
		int leftH = recursiveFindHeight(node.left);
		int rightH = recursiveFindHeight(node.right);
		if (leftH > rightH) return leftH + 1; 
		else return rightH + 1;
	}
	int iterativeHeight() {
		return iterativeFindHeight(root);
	}
	int iterativeFindHeight(Node node) {
		if (node == null) return 0;  
		Queue<Node> nodeQ = new LinkedList<Node>();
		nodeQ.add(node);
		int height = 0; 
		
		while(true) {
			int nodeCount = nodeQ.size(); // node count at current level
			if (nodeCount == 0) return height; // no more levels to count nodes
			else height++; 
			// enqueue nodes from next level and dequeue nodes from current level
			while(nodeCount > 0) {
				Node top = nodeQ.peek();
				nodeQ.remove();
				if (top.left != null) nodeQ.add(top.left);
				if (top.right != null) nodeQ.add(top.right);
				nodeCount--;
			}
		}
	}
	
	/* Print out the tree with different levels */
	void treePrint() {
		treePrintFromNode(root);
	}
	void treePrintFromNode(Node node) {
		if (node == null) return; 
		Queue<Node> nodeQ = new LinkedList<Node>(); // store nodes from a given level
		nodeQ.add(node);
		int level = 0; 
		
		while(true) {
			int count = nodeQ.size();
			if (count == 0) break; // no more nodes to print
			else level++; 
			String nodeQStr = ""; 
			for(Node n: nodeQ) nodeQStr += n.key + " ";
			System.out.printf("%-10s%s\n", "Level " + level + ":", nodeQStr);
			while (count > 0) {
				Node top = nodeQ.peek();
				nodeQ.remove();
				if (top.left != null) nodeQ.add(top.left);
				if (top.right != null) nodeQ.add(top.right);
				count--; 
			}
		}
	}
	
	/* Convert a given array of unique integers into a balanced BST */
	Node convertArrayToBST(int[] arr) {
		int[] sortedArr = FunIntAlgorithm.mergeSort(arr, 0, arr.length-1);
		return convertSortdArrayToBST(sortedArr, 0, sortedArr.length - 1); 
	}
	Node convertSortdArrayToBST(int[] sortedArr, int start, int end) {
		// base case
		if (start > end)  return null; 
		// recursion
		int mid = (start + end) / 2; 
		Node node = new Node(sortedArr[mid]); 
		node.left = convertSortdArrayToBST(sortedArr, start, mid-1);
		node.right = convertSortdArrayToBST(sortedArr, mid+1, end);
		return node;
	}
	
	
	
	/**
	 * Test functionalities of the implemented BST
	 */
	public static void main(String[] args) {
		BinarySearchTree bst = new BinarySearchTree(); 
		
		/**
		 * Example tree: 
		 *           5
		 *         /   \
		 *        3     7
		 *       / \   / \
		 *      2   4 6   8
		 */
		// insert elements in a way to keep the tree balanced
		bst.insert(5);
		bst.insert(3);
		bst.insert(7);
		bst.insert(2);
		bst.insert(4);
		bst.insert(6);
		bst.insert(8);

		// insert elements in a way to keep the tree unbalanced
//		bst.insert(4);
//		bst.insert(5);
//		bst.insert(2);
//		bst.insert(3);
//		bst.insert(8);
//		bst.insert(7);
//		bst.insert(6);


		
		System.out.println("Print out the tree elements in order.");
		bst.inorderTraverse(bst.root);
		System.out.println("\n");
		
		System.out.println("Find the height of the tree.");
		System.out.println(bst.recursiveHeight());
		System.out.println(bst.iterativeHeight());
		
		System.out.println("\nPrint out the tree elements in levels.");
		bst.treePrint();
		
		System.out.println("\nChecking if a given element exists.");
		int key = 3; 
		System.out.println("Element " + key + ": " + bst.contains(key));
		key = 1;
		System.out.println("Element " + key + ": " + bst.contains(key));
		
		System.out.println("\nInsert a new eletment " + key + ".");
		bst.insert(key);
		bst.inorderTraverse(bst.root);
		System.out.println("\n");
		bst.treePrint(); 
		
		key = 5;
		System.out.println("Delete an element " + key + ".");
		bst.delete(key);
		bst.inorderTraverse(bst.root);
		System.out.println("\n");
		bst.treePrint();
		
		System.out.println("Converting a random unique integer array into a balanced BST.");
		int[] arr = {2, 4, 6, 3, 5, 8,7};
		//int[] arr = FunIntAlgorithm.genRanUniqueIntArr(10);
		System.out.println("The randomly generated array is \n" + Arrays.toString(arr));
		BinarySearchTree balBST = new BinarySearchTree(bst.convertArrayToBST(arr));
		bst.inorderTraverse(balBST.root);
		System.out.println("\n");
		balBST.treePrint();
		
		System.out.println("All rabbits gone.");
	}

}
