package binaryTree.entities;


import java.util.ArrayList;
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
public class BinarySearchTree extends BinaryTree {
	/* Constructors */
	public BinarySearchTree() {
		super();
	}
	public BinarySearchTree(TreeNode root) {
		super(root);
	}
	
	/* Insert a new key by recursion */
	public void insert(int key) {
		root = insertItem(root, key);
	}
	private TreeNode insertItem(TreeNode node, int key) {
		// return a new node if tree is empty
		if (node == null) return new TreeNode(key); 
		// else recur down the tree
		if (key < node.key) // left side
			node.left = insertItem(node.left, key);
		else if (key > node.key) // right side, and no duplicates allowed in BST
			node.right = insertItem(node.right, key);
		return node;
	}
	
	/* Search a key by recursion */
	public boolean contains(int key) {
		return containsItem(root, key);
	}
	private boolean containsItem(TreeNode node, int key) {
		if (node == null) return false; // empty tree
		if (node.key == key) return true; 
		else if (key < node.key) // recur left
			return containsItem(node.left, key); 
		else // recur right
			return containsItem(node.right, key);
	}
	
	/* Delete a key by recursion */
	public void delete(int key) {
		root = deleteItem(root, key);
	}
	private TreeNode deleteItem(TreeNode node, int key) {
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
	public int minVal(TreeNode node) { // get minimal value by searching from the given node
		int min = node.key;
		while (node.left != null) {
			min = node.left.key;
			node = node.left;
		}
		return min;
	}
	
	/* Traverse BST in order */
	public void inorderTraverse() { inorderTraverse(this.root); System.out.println(); }
	private void inorderTraverse(TreeNode node) {
		if (node != null) {
			inorderTraverse(node.left); // smaller ones
			System.out.print(node.key + " ");
			inorderTraverse(node.right); // larger ones
		}
	}
	
	/* Traverse BST in order and store results to an array */
	public int[] inorderTraversalToArray() {
		ArrayList<Integer> list = new ArrayList<>();
		inorderTraversalToArray(this.root, list);
		return list.stream().mapToInt(i -> i).toArray();
	}
	private void inorderTraversalToArray(TreeNode node, ArrayList<Integer> list) {
		if (node != null) {
			inorderTraversalToArray(node.left, list); // smaller ones
			list.add(node.key);
			inorderTraversalToArray(node.right, list); // larger ones
		}
	}
	
	/* Find tree height (defined as number of nodes from root to farthest leaf) */
	public int recursiveHeight() {
		return recursiveFindHeight(root);
	}
	int recursiveFindHeight(TreeNode node) {
		if (node == null) return 0;  
		return Math.max(recursiveFindHeight(node.left), recursiveFindHeight(node.right)) + 1;
	}
	public int iterativeHeight() {
		return iterativeFindHeight(root);
	}
	int iterativeFindHeight(TreeNode node) {
		if (node == null) return 0;  
		Queue<TreeNode> nodeQ = new LinkedList<TreeNode>();
		nodeQ.add(node);
		int height = 0; 
		
		while(true) {
			int nodeCount = nodeQ.size(); // node count at current level
			if (nodeCount == 0) return height; // no more levels to count nodes
			else height++; 
			// enqueue nodes from next level and dequeue nodes from current level
			while(nodeCount > 0) {
				TreeNode top = nodeQ.peek();
				nodeQ.remove();
				if (top.left != null) nodeQ.add(top.left);
				if (top.right != null) nodeQ.add(top.right);
				nodeCount--;
			}
		}
	}
	
	/* Print out the tree level by level */
	public void treePrint() {
		if (root == null) return;
		treePrintFromNode(root);
	}
	private void treePrintFromNode(TreeNode node) {
		if (node == null) return; 
		Queue<TreeNode> nodeQ = new LinkedList<TreeNode>(); // store nodes from a given level
		nodeQ.add(node);
		int level = 0, count = 0;
		boolean lastLvl = false; 
		
		while(true) {
			if (!lastLvl) count = nodeQ.size();
			if (count == 0) break; // no more nodes to print
			else level++; 
			String nodeQStr = ""; 
			for(TreeNode n: nodeQ) nodeQStr += (n == null ? "#" : n.key) + " ";
			System.out.printf("%-10s%s\n", "Level " + level + ":", nodeQStr);
			lastLvl = true;
			while (count > 0) {
				TreeNode top = nodeQ.peek();
				nodeQ.remove();
				if (top != null) {
					nodeQ.add(top.left);
					nodeQ.add(top.right);
					if (top.left != null || top.right != null) lastLvl = false;
				}
				count--; 
			}
		}
	}
	
	/* Convert a given array of unique integers into a balanced BST */
	public static TreeNode convertArrayToBST(int[] arr) {
		int[] sortedArr = FunIntAlgorithm.mergeSort(arr, 0, arr.length-1);
		return convertSortedArrayToBST(sortedArr, 0, sortedArr.length - 1); 
	}
	private static TreeNode convertSortedArrayToBST(int[] sortedArr, int start, int end) {
		// base case
		if (start > end)  return null; 
		// recursion
		int mid = (start + end) / 2; 
		TreeNode node = new TreeNode(sortedArr[mid]); 
		node.left = convertSortedArrayToBST(sortedArr, start, mid-1);
		node.right = convertSortedArrayToBST(sortedArr, mid+1, end);
		return node;
	}

	/* Serialization/deserialization methods */
	public String serialize() {
		return recursiveSerialize();
	}
	public static BinarySearchTree deserialize(String s) throws Exception {
		return new BinarySearchTree(recursiveDeserialize(s));
	}
	
	/* Check if a given binary tree is a binary search tree */
	public static boolean isBinarySearchTree(BinaryTree bt) {
		int[] nodeArr = bt.inOrderTraversalToArray();
		for (int i=1; i<nodeArr.length; i++) {
			if (nodeArr[i] < nodeArr[i-1]) return false; 
		}
		return true;
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


		System.out.println("/** Print out the tree elements in order. **/");
		bst.inorderTraverse(bst.root);
		System.out.println("\n");
		
		System.out.println("/** Find the height of the tree. **/");
		System.out.println("Recursively: " + bst.recursiveHeight());
		System.out.println("Iteratively: " + bst.iterativeHeight());
		
		System.out.println("\n/** Iteratively print out the tree elements in levels. **/");
		bst.treePrint();
		System.out.println("\n/** Recursively print out the tree elements in levels. **/");
		bst.levelOrderTraverse();
		
		System.out.println("\n/** Checking if a given element exists. **/");
		int key = 3; 
		System.out.println("Element " + key + ": " + bst.contains(key));
		key = 1;
		System.out.println("Element " + key + ": " + bst.contains(key));
		
		System.out.println("\n/** Insert a new eletment " + key + ". **/");
		bst.insert(key);
		bst.inorderTraverse();
		System.out.println("\n");
		bst.treePrint(); 
		System.out.println();
		
		key = 5;
		System.out.println("/** Delete an element " + key + ". **/");
		bst.delete(key);
		bst.inorderTraverse(bst.root);
		System.out.println("\n");
		bst.levelOrderTraverse();
		
		System.out.println("/** Converting a random unique integer array into a balanced BST. **/");
		int[] arr = {2, 4, 6, 3, 5, 8,7};
		//int[] arr = FunIntAlgorithm.genRanUniqueIntArr(10);
		System.out.println("The randomly generated array is \n" + Arrays.toString(arr));
		BinarySearchTree balBST = new BinarySearchTree(BinarySearchTree.convertArrayToBST(arr));
		balBST.treePrint();
		bst.inorderTraverse(balBST.root);
		System.out.println("\n");
		
		System.out.println("/** Serializing a BST. **/");
		String serialized = bst.serialize();
		System.out.println(serialized);
		System.out.println("/** Deserializing the BST that had just been serialized. **/");
		//serialized = "##";
		try {
			BinarySearchTree deserialized = BinarySearchTree.deserialize(serialized);
			deserialized.treePrint();
			deserialized.inorderTraverse(deserialized.root);
			System.out.println("\n");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("All rabbits gone.");
	}

}
