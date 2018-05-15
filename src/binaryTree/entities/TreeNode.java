package binaryTree.entities;

/**
 * Tree node class
 * 
 * @author ruifengm
 * @since 2018-May-14
 */

public class TreeNode {
	int key; 
	TreeNode left, right;
	
	public TreeNode(int item) {
		key = item;
		left = right = null;
	}

	public int getKey() {
		return key;
	}

	public void setKey(int key) {
		this.key = key;
	}

	public TreeNode getLeft() {
		return left;
	}

	public void setLeft(TreeNode left) {
		this.left = left;
	}

	public TreeNode getRight() {
		return right;
	}

	public void setRight(TreeNode right) {
		this.right = right;
	}
}
