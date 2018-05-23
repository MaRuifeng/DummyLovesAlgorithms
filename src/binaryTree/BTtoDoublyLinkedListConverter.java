package binaryTree;

import binaryTree.entities.BinarySearchTree;
import binaryTree.entities.BinaryTree;
import binaryTree.entities.TreeNode;

/**
 * Convert a binary tree to a doubly linked list with in-order sequence maintained. 
 * 
 * @author ruifengm
 * @since 2018-May-23
 * 
 * https://www.geeksforgeeks.org/in-place-convert-a-given-binary-tree-to-doubly-linked-list/
 */
public class BTtoDoublyLinkedListConverter {
	
	/** 
	 * Recursively convert the left subtree into a doubly linked list, and the right
	 * subtree into another, and concatenate them together with the root. 
	 */
	static class DoublyLinkedList {
		TreeNode head;
		public DoublyLinkedList(TreeNode head)  {
			this.head = head;
		}
		public void inorderTraverse() {
			TreeNode node = this.head;
			while (node != null) {
				System.out.print(node.getKey() + " ");
				node = node.getRight();
			}
			System.out.println();
		}
		public void recursiveReverseOrderTraverse(TreeNode head) {
			if (head == null) return; 
			recursiveReverseOrderTraverse(head.getRight());
			System.out.print(head.getKey() + " ");
		}
		public void reverseOrderTraverse() {
			TreeNode node = this.head;
			while (node.getRight() != null) node = node.getRight();
			while (node != null) {
				System.out.print(node.getKey() + " ");
				node = node.getLeft();
			}
			System.out.println();
		}
	}
	private static DoublyLinkedList convertBTtoDLL(BinaryTree bt) {
		TreeNode head = convertBTtoDLL(bt.getRoot());
		while (head.getLeft() != null) head = head.getLeft();
		return new DoublyLinkedList(head);
	}
	private static TreeNode convertBTtoDLL(TreeNode root) {
		if (root != null) {
			if (root.getLeft() != null) { // convert left sub-tree
				TreeNode left = convertBTtoDLL(root.getLeft());
				while (left.getRight() != null) left = left.getRight();
				left.setRight(root);
				root.setLeft(left);
			}
			if (root.getRight() != null) { // convert right sub-tree
				TreeNode right = convertBTtoDLL(root.getRight());
				while (right.getLeft() != null) right = right.getLeft();
				right.setLeft(root);
				root.setRight(right);
			}
			return root;
		}
		return null;
	}
	
	public static void main(String[] args) {
		System.out.println("/** Converting a binary tree to a doubly linked list in O(n) time. **/");
		BinarySearchTree bst = new BinarySearchTree(); 
		bst.insert(8);
		bst.insert(4);
		bst.insert(12);
		bst.insert(2);
		bst.insert(6);
		bst.insert(10);
		bst.insert(14);
		bst.insert(5);
		bst.insert(1);
		bst.insert(7);
		
		System.out.println("The original BST is: \n");
		bst.levelOrderTraverse();
		DoublyLinkedList dll = convertBTtoDLL(bst);
		System.out.println("The inorder traversal of the doubly linked list is: \n");
		dll.inorderTraverse();
		System.out.println("The reverse order traversal of the doubly linked list is: \n");
		dll.reverseOrderTraverse();
		dll.recursiveReverseOrderTraverse(dll.head);
		
		System.out.println("\n\nAll rabbits gone.");
	}

}
