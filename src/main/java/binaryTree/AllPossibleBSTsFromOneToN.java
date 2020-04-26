package binaryTree;

import java.util.ArrayList;

import binaryTree.entities.BinarySearchTree;
import binaryTree.entities.TreeNode;
import utils.FunIntAlgorithm;

/**
 * Given a positive integer N, find all structurally unique binary search trees that can be constructed with 
 * node keys from 1 to N. 
 * 
 * 
 * @author ruifengm
 * @since 2018-May-20
 * 
 * https://www.geeksforgeeks.org/construct-all-possible-bsts-for-keys-1-to-n/
 *
 */

public class AllPossibleBSTsFromOneToN extends FunIntAlgorithm {
	
	/**
	 * Let Sol(1..n) be the count of possible BSTs that can be formed from 1 to n, and since any number in between 
	 * 1 and n can be the tree root, for a particular number i as root, the count of possible BSTs is 
	 * 
	 *         Sol(1..i-1) * Sol(i+1..n)    <-- 1 to i-1 form the left sub-tree, and i+1 to n form the right sub-tree
	 *         
	 * We need to sum these counts up for 1 <= i <= n, so as to obtain Sol(1..n). 
	 * 
	 * Actually the BST counts form the Catalan numbers (https://brilliant.org/wiki/catalan-numbers/).
	 * 
	 */
	private static long recursiveCountOfBSTs(int n) {
		if (n <= 1) return 1; 
		int count = 0; 
		for (int i=1; i<=n; i++) count += Math.multiplyExact(recursiveCountOfBSTs(i-1), recursiveCountOfBSTs(n-i)); 
		return count;
	}
	
	/**
	 * Get all possible BSTs.
	 */
	private static ArrayList<BinarySearchTree> getBSTs(int start, int end) {
		ArrayList<BinarySearchTree> bstList = new ArrayList<>(); 
		if (start > end) {
			bstList.add(new BinarySearchTree()); // an empty BST
			return bstList;
		}
		for (int i=start; i<=end; i++) {
			ArrayList<BinarySearchTree> leftSubTreeList = getBSTs(start, i-1);
			ArrayList<BinarySearchTree> rightSubTreeList = getBSTs(i+1, end);
			for (BinarySearchTree leftSubTree: leftSubTreeList) {
				for (BinarySearchTree rightSubTree: rightSubTreeList) {
					BinarySearchTree bst = new BinarySearchTree(new TreeNode(i));
					bst.getRoot().setLeft(leftSubTree.getRoot());
					bst.getRoot().setRight(rightSubTree.getRoot());
					bstList.add(bst);
				}
			}
		}
		return bstList;
	}

	
	public static void main(String[] args) {
		int n = 5;
		System.out.println("Welcome to the rabbit hole of possible BSTs from 1 to N!\n"
				+ "The value of N is : " + n + ".\n"); 
		
		try {
			runIntFuncAndCalculateTime("[Recursive][Exponential]  Count of possible BSTs: " , (int i) -> recursiveCountOfBSTs(i), n);
			System.out.println("[Recursive] Print all possible BSTs:\n");
			ArrayList<BinarySearchTree> list = getBSTs(1, n);
			System.out.println("BST count: " + list.size());
			for (BinarySearchTree bst: list) bst.levelOrderTraverse();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("All rabbits gone.");
	}

}
