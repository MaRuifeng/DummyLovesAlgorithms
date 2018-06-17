package integerArray;

/**
 * Reverse a multi-digit integer.
 * 
 * E.g.
 * Reverse 123 you will get 321.
 * Reverse 900 you will get 9.	
 *
 * 
 * @author ruifengm
 * @since 2018-Jun-17
 * 
 * https://www.lintcode.com/problem/reverse-3-digit-integer/description
 */

public class ReverseInteger {
	
	/**
	 * Naively reverse ...
	 */
	private static int naiveReverse(int num) {
		char[] digSeq = String.valueOf(num).toCharArray();
		int len = digSeq.length;
		// reverse
		int i, j; 
		int mid = len / 2; 
		i = mid - 1;
		if (len % 2 == 0) j = mid; 
		else j = mid + 1; 
		while (i > -1 && j < len) {
			char temp = digSeq[i];
			digSeq[i] = digSeq[j];
			digSeq[j] = temp;
			i--; j++;
		}
		// parse
		int base = 1, res = 0;
		i = len - 1; 
		while (i > -1) {
			res += (digSeq[i--] - '0') * base; 
			base *= 10;
		}
		return res; 
	}
	
	/**
	 * After completing above method, realized we don't need to reverse the char array at all ...
	 */
	private static int reverse(int num) {
		int size = (int)(Math.log10(num)) + 1; // not applicable to num = 0
		int rightBase = 1, leftBase = (int) Math.pow(10, size-1);
		int res = 0, dig;
		while (leftBase > 0) {
			dig = num / leftBase; 
			res += dig * rightBase; 
			num = num % leftBase; 
			leftBase /= 10; 
			rightBase *= 10;
		}
		return res; 
	}
	
	public static void main(String[] args) {
		System.out.println("Welcome to the rabbit hole of reversed singly linked list.");
		
//		int num = 123; 
//		int num = 900; 
//		int num = 102; 
//		int num = 0;
//		int num = 12580; 
		int num = 2014503010;
		System.out.println("Input: " + num);
//		System.out.println("Output: " + naiveReverse(num));
		System.out.println("Output: " + reverse(num));
		
		System.out.println("All rabbits gone.");
	}

}
