package integerArray;

import java.util.Arrays;
import java.util.Stack;

/**
 * A recap on bitwise operations. 
 * 
 * &  --> AND operator which copies a bit to the result if it exists in both operands
 * |  --> OR operator which copies a bit to the result if it exists in either operand
 * ^  --> XOR operator which copies a bit to the result if it is set in one operand but not both
 * ~  --> 1's complement operator which flips all bits
 * << --> Left shift operator, the value of the left operand is moved left by the number of bits specified by the right operand
 * >> --> Right shift operator, the value of the left operand is moved right by the number of bits specified by the right operand
 * @author ruifengm
 * @since 2018-Apr-18
 */

public class BitOperators {
	
	// convert a positive decimal integer to its binary format
	private static char[] decimalToBinary(int n) {
		String bi = "";
		while (n>0) {
			bi += n%2; 
			n = n/2; 
		}
		char[] res = new char[bi.length()];
		int j=0; 
		for (int i=bi.length()-1; i>=0; i--) {
			res[j] = bi.charAt(i);
			j++;
		}
		return res;
	}
	
	// convert a positive decimal integer to its binary format using bitwise operation
	private static char[] decimalToBinaryBitOps(int n) {
		Stack<Character> bitStack = new Stack<Character>();
		int checker = 1; 
		while(checker != 0) {
			if ((n&checker) != 0) bitStack.push('1');
			else bitStack.push('0');
			checker = (checker << 1);
		}
		// remove leading zeros
		while(bitStack.peek() == '0') bitStack.pop();
		int size = bitStack.size();
		char[] res = new char[size]; 
		for (int i=0; i<size; i++) res[i] = bitStack.pop().charValue();
		return res;
	}
	
	// Given an integer y, turn off its xth bit (i.e. 1 --> 0, 0 --> 0), counting from right to left.
	private static int turnOffBit(int y, int x) {
		// perform & operation with a number whose xth bit is 0 and all others are 1
		int checker = ~(1 << (x-1)); 
		return (checker & y);
	}
	
	// Mathematical addition without using '+' operator
	private static int recursiveAdd(int a, int b) {
		if ((a&b) == 0) return a^b; // no more carry-over, return result
		return recursiveAdd((a&b)<<1, a^b); // carry-over found, add again
	}
	private static int iterativeAdd(int a, int b) {
		int carryOver = a&b;
		while (carryOver != 0) {
			b = a^b;
			a = carryOver<<1;
			carryOver = a&b;
		}
		return a^b;
	}
	
	public static void main(String args[]) {
		//int num = Integer.MAX_VALUE; 
		int num = 7896; 
		int pos = 4; 
		System.out.println("Binary conversion by short division: " + Arrays.toString(decimalToBinary(num)));
		System.out.println("Binary conversion by bit operaion: " + Arrays.toString(decimalToBinaryBitOps(num)));
		System.out.println("Turning off the " + pos + "'th bit...");
		System.out.println(Arrays.toString(decimalToBinaryBitOps(turnOffBit(num, pos))));
		int a = -5, b = 8; 
		System.out.println("[Recursive] Adding integer " + a + " and " + b + " by bit operation: " + recursiveAdd(a, b));
		System.out.println("[Iterative] Adding integer " + a + " and " + b + " by bit operation: " + iterativeAdd(a, b));
	}

}
