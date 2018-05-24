package binaryTree.entities;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Parent class for all binary tree data structures
 * 
 * @author ruifengm
 * @since 2018-May-14
 */
public class BinaryTree {
	
	TreeNode root;

	public BinaryTree(TreeNode root) { // constructor
		this.root = root;
	}
	
	public BinaryTree() {
		this.root = null;
	}
	
	public BinaryTree(BinaryTree bt) { // copy constructor
		this.root = cloneViaPreOrderTraversal(bt.root);
	}
	
	public TreeNode getRoot() {
		return root;
	}

	public void setRoot(TreeNode root) {
		this.root = root;
	}

	/* Recursive level order traversal */
	public void levelOrderTraverse() {
		List<TreeNode> list = new ArrayList<>(); 
		list.add(this.root);
		levelOrderTraverse(list, 1);
		System.out.println();
	}
	private void levelOrderTraverse(List<TreeNode> list, int level) {
		List<TreeNode> next = new ArrayList<>();
		System.out.print("Level " + level + ": ");
		boolean lastLvl = true;
		for (TreeNode n: list) {
			if (n != null ) {
				System.out.print(n.key + " "); 
				next.add(n.left);
				next.add(n.right);
				if (n.left != null || n.right != null) lastLvl = false;
			} else System.out.print("# ");
		}
		System.out.println();
		if (!next.isEmpty() && !lastLvl) levelOrderTraverse(next, ++level);
	}
	
	/* Preorder traversal */
	// When visiting a tree, meet roots before leaves
	public void preorderTraverse() { preorderTraverse(this.root); System.out.println("\n"); };
	private void preorderTraverse(TreeNode node) {
		if (node != null) {
			System.out.print(node.key + " ");
			preorderTraverse(node.left);
			preorderTraverse(node.right);
		}
	}
	
	/* Postorder traversal */
	// When visiting a tree, meet leaves before roots
	public void postorderTraverse() { postorderTraverse(this.root); System.out.println("\n"); };
	private void postorderTraverse(TreeNode node) {
		if (node != null) {
			postorderTraverse(node.left);
			postorderTraverse(node.right);
			System.out.print(node.key + " ");
		}
	}
	
	/* Preorder traversal and store results to an array */
	public int[] preOrderTraversalToArray() {
		ArrayList<Integer> list = new ArrayList<>();
		preOrderTraversalToArray(this.root, list);
		return list.stream().mapToInt(i -> i).toArray();
	}
	private void preOrderTraversalToArray(TreeNode node, ArrayList<Integer> list) {
		if (node != null) {
			list.add(node.key);
			preOrderTraversalToArray(node.left, list); // smaller ones
			preOrderTraversalToArray(node.right, list); // larger ones
		}
	}
	
	/* Postorder traversal and store results to an array */
	public int[] postOrderTraversalToArray() {
		ArrayList<Integer> list = new ArrayList<>();
		postOrderTraversalToArray(this.root, list);
		return list.stream().mapToInt(i -> i).toArray();
	}
	private void postOrderTraversalToArray(TreeNode node, ArrayList<Integer> list) {
		if (node != null) {
			postOrderTraversalToArray(node.left, list); // smaller ones
			postOrderTraversalToArray(node.right, list); // larger ones
			list.add(node.key);
		}
	}
	
	/* Inorder traversal and store results to an array */
	public int[] inOrderTraversalToArray() {
		ArrayList<Integer> list = new ArrayList<>();
		inOrderTraversalToArray(this.root, list);
		return list.stream().mapToInt(i -> i).toArray();
	}
	private void inOrderTraversalToArray(TreeNode node, ArrayList<Integer> list) {
		if (node != null) {
			inOrderTraversalToArray(node.left, list); // smaller ones
			list.add(node.key);
			inOrderTraversalToArray(node.right, list); // larger ones
		}
	}
	
	/* Generate a clone of current tree via preorder traversal */
	//	@Override
	//	public BinaryTree clone() {
	//		return new BinaryTree(cloneViaPreOrderTraversal(this.root));
	//	}
	public <T extends BinaryTree> T clone(Class<T> type) throws Exception {
		Constructor<? extends BinaryTree> constructor;
		try {
			constructor = this.getClass().getConstructor(TreeNode.class);
		} catch (NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
			throw new Exception("Could not get a proper constructor for " + this.getClass().getName() + ".");
		}
		return type.cast(constructor.newInstance(cloneViaPreOrderTraversal(this.root)));
	}
	protected TreeNode cloneViaPreOrderTraversal(TreeNode ori) {
		if (ori != null) {
			TreeNode cln = new TreeNode(ori.key);
			cln.left = cloneViaPreOrderTraversal(ori.left);
			cln.right = cloneViaPreOrderTraversal(ori.right);
			return cln;
		}
		return null;
	}
	
	/* Serialization Method 1 */
	public String recursiveSerialize() {
		return recursiveSerialize(root);
	}
	private String recursiveSerialize(TreeNode node) {
		if (node == null) return "#"; 
		return String.valueOf(node.key) + "," + recursiveSerialize(node.left) + "," + recursiveSerialize(node.right);
	}
	
	/* Deserialization Method 1 (only applicable to BTs serialized via the Serialization Method 1 implemented in this class) */
	public static TreeNode recursiveDeserialize(String s) throws Exception {
		if (s.isEmpty() || s.equals("#")) return null;
		Queue<String> list = new LinkedList<>();
		for (String str: s.split(",")) list.add(str);
		TreeNode node; 
		try {
			node = recursiveDeserialize(list); 
		} catch (Exception e) {
			throw new Exception("The string to be deserialized is not well formed.");
		}
		return node;
	}
	private static TreeNode recursiveDeserialize(Queue<String> list) throws Exception {
		TreeNode node = new TreeNode(Integer.parseInt(String.valueOf(list.poll())));
		if (!list.peek().equals("#")) node.left = recursiveDeserialize(list); 
		else {
			node.left = null;
			list.poll();
		}
		if (!list.peek().equals("#")) node.right = recursiveDeserialize(list);
		else {
			node.right = null; 
			list.poll();
		}
		return node;
	}
	
	/* Serialization Method 2 */
	public String iterativeSerialize() {
		if (this.root == null) return "";
		Queue<TreeNode> nodeQ = new LinkedList<>();
		String serialized = "";
		boolean lastLvl = false;
		int count = 0;
		nodeQ.add(this.root);
		while(true) {
			if (!lastLvl) count = nodeQ.size();
			if (count == 0) break; // no more nodes to serialize
			for (TreeNode n: nodeQ) serialized += (n == null ? "#" : n.key) + ",";
			lastLvl = true;
			while (count > 0) {
				TreeNode top = nodeQ.peek();
				nodeQ.remove();
				if (top != null) {
					nodeQ.add(top.left);
					nodeQ.add(top.right);
					if (top.left != null || top.right != null) lastLvl = false;
				}
				count --; 
			}
		}
		return serialized.substring(0, serialized.length()-1); // remove last ','
	}
	
	/* Deserialization Method 2 (only applicable to BTs serialized via the Serialization Method 2 implemented in this class) */
	public static TreeNode iterativeDeserialize(String s) {
		if (s.isEmpty()) return null;
		ArrayList<TreeNode> list = new ArrayList<>();
		for (String str: s.split(",")) list.add(str.equals("#") ? null : new TreeNode(Integer.parseInt(str)));
		Queue<TreeNode> curLvl = new LinkedList<>(); 
		curLvl.add(list.get(0));
		int i = 1; 
		while (true) {
			Queue<TreeNode> nextLvl = new LinkedList<>();
			Iterator<TreeNode> iter = curLvl.iterator();
			int nextLvlsize = 0; 
			while (iter.hasNext()) if (iter.next() != null) nextLvlsize += 2;
			while (i <= list.size()-1 && nextLvl.size() < nextLvlsize) nextLvl.add(list.get(i++));
			iter = nextLvl.iterator();
			for (TreeNode node: curLvl) {
				if (node != null) {
					if (iter.hasNext()) node.left = iter.next();
					if (iter.hasNext()) node.right = iter.next();
				}
			}
			if (i >= list.size()-1) break;
			curLvl = nextLvl;
		}
		return list.get(0);
	}

	/**
	 * Tests
	 */
	public static void main (String[] args) {
		TreeNode root = new TreeNode(0); 
		root.left = new TreeNode(8); 
		root.right = new TreeNode(4); 
		root.left.left = new TreeNode(3);
		root.left.left.left = new TreeNode(5);
		root.right.right = new TreeNode(6);
		root.left.left.right = new TreeNode(1); 
		
		BinaryTree bt = new BinaryTree(root);
		
		System.out.println("Level-order traversal:");
		bt.levelOrderTraverse();
		System.out.println("Pre-order traversal:");
		bt.preorderTraverse();
		System.out.println("Post-order traversal:");
		bt.postorderTraverse();
		System.out.println("Clone 1 via pre-order traversal clone:");
		BinaryTree btClone1 = new BinaryTree();
		try {
			btClone1 = bt.clone(BinaryTree.class);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		btClone1.levelOrderTraverse();

		
		System.out.println("Clone 2 via copy contructor (utilizing the pre-order traversal method):");
		BinaryTree btClone2 = new BinaryTree(bt);
		btClone2.levelOrderTraverse();
		System.out.println("Original identity in memory:                       " + System.identityHashCode(bt)); 
		// not using the object's hashCode() method as it's subject to override
		System.out.println("Clone 1 identity (via the clone method) in memory: " + System.identityHashCode(btClone1));
		System.out.println("Clone 2 identity (via copy constructor) in memory: " + System.identityHashCode(btClone2));
		
		System.out.println("[Recursive] Serialization:");
		String serialized = bt.recursiveSerialize();
		System.out.println(serialized);
		System.out.println();
		System.out.println("[Recursive] Deserialization:"); 
		TreeNode deserialized;
		try {
			deserialized = BinaryTree.recursiveDeserialize(serialized);
			(new BinaryTree(deserialized)).levelOrderTraverse();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("[Iterative] Serialization:");
		serialized = bt.iterativeSerialize();
		System.out.println(serialized);
		System.out.println();
		System.out.println("[Iterative] Deserialization:"); 
		try {
			deserialized = BinaryTree.iterativeDeserialize(serialized);
			(new BinaryTree(deserialized)).levelOrderTraverse();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("All rabbits gone.");
	}
}
