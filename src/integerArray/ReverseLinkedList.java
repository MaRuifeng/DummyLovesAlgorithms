package integerArray;

/**
 * Reverse a linked list in-place and in one pass. 
 * 
 * For linked list 1->2->3, the reversed linked list is 3->2->1. 
 * 
 * @author ruifengm
 * @since 2018-Jun-17
 * 
 * https://www.lintcode.com/problem/reverse-linked-list/description
 *
 */

public class ReverseLinkedList {
	
	private static class ListNode {
		int key;
		ListNode next; 
		public ListNode(int key) {
			this.key = key; 
			this.next = null; 
		}
	}
	
	/**
	 * Reverse the linked list recursively.
	 */
	public static ListNode newHead;
	private static ListNode recursiveReverse(ListNode node) {
		if (node.next == null) {
			newHead = node;
			return node;
		}
		ListNode tail = recursiveReverse(node.next);
		tail.next = node; // reverse
		node.next = null; 
		return node;
	}
	
	/**
	 * Reverse the linked list iteratively.
	 */
	private static ListNode iterativeReverse(ListNode head) {
		if (head == null) return null;
		ListNode pre = head;
		ListNode cur = pre.next;
		ListNode temp = null;
		head.next = null;
		while (cur != null) {
			temp = cur.next;
			cur.next = pre;
			pre = cur;
			cur = temp;
		}
		return pre;
	}
	
	private static void print(ListNode head) {
		if (head == null) return;
		while (true) {
			System.out.print(head.key);
			head = head.next;
			if (head == null) break;
			System.out.print("->");
		}
		System.out.println();
	}
	
	public static void main(String[] args) {
		System.out.println("Welcome to the rabbit hole of reversed singly linked list.");
		
		ListNode head = new ListNode(1);
		head.next = new ListNode(2);
		head.next.next = new ListNode(3);
		
		print(head);
		
//		recursiveReverse(head);
//		print(newHead);
		
		ListNode newHead = iterativeReverse(head);
		print(newHead);
		
		System.out.println("All rabbits gone.");
	}

}
