package string;

import java.util.Arrays;

/**
 * Given a string and an offset, rotate string by offset. (rotate from left to right)
 * 
 * E.g. given "abcdefg".
 * offset=0 => "abcdefg"
 * offset=1 => "gabcdef"
 * offset=2 => "fgabcde"
 * offset=3 => "efgabcd"
 * 
 * Requirement: rotate in-place with O(1) extra space complexity. 
 * 
 * @author ruifengm
 * @since 2018-May-17
 *
 */
public class StringRotator {
	
	/**
	 * Given "abcdefg" and offset 3, rotate the string as below:
	 * Step 1: reverse the whole string                                   --> "gfedcba"
	 * Step 2: reverse each sub string partitioned by the offset point    --> "efgabcd"
	 * 
	 * Space complexity: O(1)
	 * Time complexity: O(n) where n is the size of the string
	 */
	private static void rotate(char[] charArr, int offset) {
		if (charArr == null || charArr.length == 0) return;
		offset %= charArr.length;
		reverse(charArr, 0, charArr.length-1);
		reverse(charArr, 0, offset-1);
		reverse(charArr, offset, charArr.length-1);
	}
	private static void reverse(char[] a, int start, int end) {
		if (start >= end) return; 
		while (start < end) {
			char temp = a[start]; 
			a[start++] = a[end];
			a[end--] = temp;
		}
	}
	
	public static void main(String[] args) {
		System.out.println("Welcome to the rabbit hole of string rotators!");
		
		char[] str = "rabbitholeisempty".toCharArray();
		// char[] str = "".toCharArray();
		// int offset = 0;
		// int offset = 1;
		// int offset = 5;
		// int offset = str.length;
		// int offset = Math.multiplyExact(str.length, 3);
		int offset = Math.multiplyExact(str.length, 3) + 5;
		System.out.println("The test string is: \n" + Arrays.toString(str));
		System.out.println("The offset is: \n" + offset);
		
		rotate(str, offset);
		System.out.println("After rotation: \n" + Arrays.toString(str));
		System.out.println("All rabbits gone.");
	}

}
