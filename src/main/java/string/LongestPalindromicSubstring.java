package string;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

/**
 * Find longest palindromic substring.
 *
 * @author Ruifeng Ma
 * @since 2022-Aug-09
 */

public class LongestPalindromicSubstring {

    /**
     * Dynamic programming.
     * See this lovely solution with nice comments.
     * https://leetcode.com/problems/longest-palindromic-substring/discuss/2921/Share-my-Java-solution-using-dynamic-programming/3570
     */

    /**
     * Greedy.
     * Iterate through the character array of the string, use two indices to keep to the head and tail of each
     * local palindrome, and look for the local maximum. Compare all local maxima and find the global maximum.
     */

    private static class LocalLongestPalindrome {
        int head;
        int len;

        private LocalLongestPalindrome( int head, int len ) {
            this.head = head;
            this.len = len;
        }
    }

    private static String longestPalindromicSubstring( String s ) {
        LocalLongestPalindrome localMax = new LocalLongestPalindrome(0, 1);

        for (int i = 0; i < s.length() - 1; i++) {
            // Look for local longest palindrome with odd length, e.g. "cabac"
            LocalLongestPalindrome odd = findLocalLongestPalindrome(s, i, i);
            // Look for local longest palindrome with even length, e.g. "caac"
            LocalLongestPalindrome even = findLocalLongestPalindrome(s, i, i + 1);

            if (odd.len > localMax.len) localMax = odd;
            if (even.len > localMax.len) localMax = even;
        }

        return s.substring(localMax.head, localMax.head + localMax.len);
    }

    private static LocalLongestPalindrome findLocalLongestPalindrome( String s, int head, int tail ) {
        int maxHead = 0, maxTail = 0;
        while (tail < s.length() && head >= 0) {
            if (s.charAt(head) == s.charAt(tail)) {
                // set local max positions
                maxHead = head;
                maxTail = tail;
                // extend palindrome check by one character towards both directions
                head--;
                tail++;
            } else break;
        }

        return new LocalLongestPalindrome(maxHead, maxTail - maxHead + 1);
    }

    /**
     * Brute force.
     * Find all substrings, and then look for the longest palindrome.
     */
    private static String longestPalindromicSubstringBruteForce( String s ) {
        List<String> subStrList = getAllSubstrings(s);

        int longestLen = 0;
        String longestPalindrome = null;

        for (String subStr : subStrList) {
            if (isPalindrome(subStr) && subStr.length() > longestLen) {
                longestLen = subStr.length();
                longestPalindrome = subStr;
            }
        }

        return longestPalindrome;
    }

    private static List<String> getAllSubstrings( String s ) {
        List<String> subStrList = new ArrayList<>();

        for (int i = 0; i < s.length(); i++) { // starting index
            for (int j = i; j < s.length(); j++) { // ending index
                subStrList.add(s.substring(i, j + 1));
            }
        }

        return Collections.unmodifiableList(subStrList);
    }

    private static boolean isPalindrome( String s ) {
        int len = s.length(), middleIdx = -1;
        if (len % 2 != 0) middleIdx = len / 2;

        Stack<Character> stack = new Stack<>();
        for (int i = 0; i < len; i++) {
            if (i < len / 2) stack.push(s.charAt(i));
            else {
                if (i != middleIdx && !stack.isEmpty() && stack.peek().equals(s.charAt(i))) {
                    stack.pop();
                }
            }
        }

        return stack.isEmpty();
    }

    @FunctionalInterface
    private interface LongestPalindrome {
        String apply( String s );
    }

    private static void execute( String s, LongestPalindrome func, String expected ) {
        System.out.println("Given string: " + s);
        String actual = func.apply(s);

        assert ( actual.equals(expected) ) : "Failed: expected " + expected + ", but received " + actual;
        System.out.println("Result: " + actual);
    }

    public static void main( String[] args ) {
        System.out.println("Welcome to the rabbit hole of longest palindromic substrings.");

        System.out.println("# Test case 1: ");
        System.out.println("Brute force method >");
        execute("babad", LongestPalindromicSubstring::longestPalindromicSubstringBruteForce, "bab");
        System.out.println("Greedy method >");
        execute("babad", LongestPalindromicSubstring::longestPalindromicSubstring, "bab");

        System.out.println("# Test case 2: ");
        System.out.println("Brute force method >");
        execute("cbbd", LongestPalindromicSubstring::longestPalindromicSubstringBruteForce, "bb");
        System.out.println("Greedy method >");
        execute("cbbd", LongestPalindromicSubstring::longestPalindromicSubstring, "bb");

        System.out.println("# Test case 3: ");
        System.out.println("Brute force method >");
        execute("ccc", LongestPalindromicSubstring::longestPalindromicSubstringBruteForce, "ccc");
        System.out.println("Greedy method >");
        execute("ccc", LongestPalindromicSubstring::longestPalindromicSubstring, "ccc");

        System.out.println("# Test case 4: ");
        System.out.println("Brute force method >");
        execute("abbcccbbbcaaccbababcbcabca", LongestPalindromicSubstring::longestPalindromicSubstringBruteForce, "bbcccbb");
        System.out.println("Greedy method >");
        execute("abbcccbbbcaaccbababcbcabca", LongestPalindromicSubstring::longestPalindromicSubstring, "bbcccbb");

        System.out.println("# Test case 5: ");
        System.out.println("Brute force method >");
        execute("bananas", LongestPalindromicSubstring::longestPalindromicSubstringBruteForce, "anana");
        System.out.println("Greedy method >");
        execute("bananas", LongestPalindromicSubstring::longestPalindromicSubstring, "anana");

        System.out.println("# Test case 6: ");
        System.out.println("Brute force method >");
        execute("aaabaaaa", LongestPalindromicSubstring::longestPalindromicSubstringBruteForce, "aaabaaa");
        System.out.println("Greedy method >");
        execute("aaabaaaa", LongestPalindromicSubstring::longestPalindromicSubstring, "aaabaaa");

        System.out.println("All rabbits gone");
    }
}
