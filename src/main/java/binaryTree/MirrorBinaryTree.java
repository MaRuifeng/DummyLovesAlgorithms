package binaryTree;

import binaryTree.entities.BinaryTree;
import binaryTree.entities.TreeNode;

/**
 * Given a binary tree, produce its mirror image.
 *
 * E.g. for
 *         1
  *       / \
 *       2  3
 *      / \
 *     4  5
 * the mirror image is
 *        1
 *       / \
 *      3  2
 *        / \
 *       5  4
 *
 * The solution is pretty trivial: walk through the tree using pre-order traversal and swap child nodes.
 *
 * Time complexity is linear and space complexity is in average log(n) for balanced trees and n for worst case scenario where
 * the tree is essentially a linked list.
 */

public class MirrorBinaryTree {
    /**
     * Recursively mirror a binary tree in place.
     * @param node Root node of tree
     */
    public static void recursiveInPlaceMirror(TreeNode node) {
        if (node.getLeft() == null && node.getRight() == null) return; // fewer recursive stacks than simply if (node == null) return;

        TreeNode temp = node.getLeft();
        node.setLeft(node.getRight());
        node.setRight(temp);

        recursiveInPlaceMirror(node.getLeft());
        recursiveInPlaceMirror(node.getRight());
    }


    public static void main( String[] args ) {
        System.out.println("Welcome to the rabbit hole of binary tree mirror images.");
        TreeNode root = new TreeNode(1);
        root.setLeft(new TreeNode(2));
        root.setRight(new TreeNode(3));
        root.getLeft().setLeft(new TreeNode(4));
        root.getLeft().setRight(new TreeNode(5));

        BinaryTree bt = new BinaryTree(root);
        bt.levelOrderTraverse();

        recursiveInPlaceMirror(root);
        bt.levelOrderTraverse();


        System.out.println("All rabbits gone.");
    }
}
