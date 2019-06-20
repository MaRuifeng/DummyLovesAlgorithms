package integerArray;

/**
 * Reverse a linked list in-place and in one pass. 
 * 
 * For linked list 1->2->3, the reversed linked list is 3->2->1. 
 * 
 * Variation: reverse a portion only.
 * 
 * @author ruifengm
 * @since 2018-Jun-17
 * 
 * https://www.lintcode.com/problem/reverse-linked-list/description
 * https://www.lintcode.com/problem/reverse-linked-list-ii/description
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
		ListNode prev = null, next;
		while(head != null) {
			next = head.next;
			head.next = prev;
			prev = head;
			head = next;
		}
		return prev;
	}
	
	/**
	 * Reverse the linked list from position m to n iteratively.
	 */
	private static ListNode iterativePartialReverse(ListNode head, int m, int n) {
		if (head == null) return null;
		int pos = 1; 
		ListNode pre = head, cur = head.next, temp = null, subTail = null, subHead = null;
		// look for entry position
		while (pos < m) {
			subHead = pre; // used to connect the portion ahead of reversal
			pre = pre.next;
			cur = pre.next;
			pos++;
		}
		subTail = pre; // used to connect to the portion behind reversal
		// perform reverse
		while (pos < n && cur != null) {
			temp = cur.next;
			cur.next = pre;
			pre = cur;
			cur = temp;
			pos++; 
		}
		subTail.next = cur;
		if (subHead == null) {
			head.next = cur; 
			head = pre;  
		} else subHead.next = pre;
		return head;
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
		
		/* Full reversal */
		ListNode head = new ListNode(1);
		head.next = new ListNode(2);
		head.next.next = new ListNode(3);
		
		print(head);
		
//		recursiveReverse(head);
//		print(newHead);
		
		ListNode newHead = iterativeReverse(head);
		print(newHead);
		
		/* Partial reversal */
		ListNode head2 = new ListNode(1);
		head2.next = new ListNode(2);
		head2.next.next = new ListNode(3);
		head2.next.next.next = new ListNode(4);
		head2.next.next.next.next = new ListNode(5);
		head2.next.next.next.next.next = new ListNode(6);
		
		print(head2);
		ListNode newHead2 = iterativePartialReverse(head2, 2, 4);
//		ListNode newHead2 = iterativePartialReverse(head2, 1, 6);
//		ListNode newHead2 = iterativePartialReverse(head2, 1, 5);
//		ListNode newHead2 = iterativePartialReverse(head2, 2, 6);
		print(newHead2);
		
		System.out.println("All rabbits gone.");
	}

}
