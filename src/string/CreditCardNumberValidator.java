package string;

import org.junit.Assert;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Luhn check is a defined checksum formula that can be used to validate a variety of ID numbers.
 * It consists of following steps.
 *
 * 1. From the right most digit, double every second digit. If the result is greater than 9,
 *    add both digits to make a new single digit number. E.g. (3 -> 3*2=6; 5 -> 5*2=10, 1+0=1; 9 -> 9*2=18, 1+8=9).
 * 2. Sum up the unchanged and changed digits.
 * 3. If the sum is a multiple of 10, the ID number is valid; else not.
 *
 * [Example]
 * Given credit card number 12345678903555
 * Digits are : 1,2,3,4,5,6,7,8,9,0,3,5,5,5
 * After doubling : 2,2,6,4,1,6,5,8,9,0,6,5,1,5
 * Sum of digits : 2+2+6+4+1+6+5+8+9+0+6+5+1+5 = 60 = 6*10 and hence it is a valid credit card number.
 *
 * [TakeAway]
 * With introduction of Lambda Expressions in Java 8 to facilitate functional programming,
 * try to use it as much as possible for more concise code.
 *
 * @author Ruifeng Ma
 * @since 2019-Apr-06
 */
public class CreditCardNumberValidator {

    private final static Pattern NUMBER_PATTERN = Pattern.compile("^\\d+$");

    public static boolean isCreditCardNumberValid(String ccNumStr) {
        if (ccNumStr == null) return false;
        if (!isNumberString(ccNumStr)) return false;
        // Use lambda expression instead of loop iteration
        int[] ccNumArr = ccNumStr.chars().mapToObj(c -> (char)c).mapToInt(Character::getNumericValue).toArray();

//        int[] ccNumArr = new int[ccNumStr.length()];
//        for (int i = 0; i < ccNumStr.length(); i++) {
//            ccNumArr[i] = Character.getNumericValue(ccNumStr.toCharArray()[i]);
//        }

        doubleSecondDigit(ccNumArr);
        int sum = sumAll(ccNumArr);
        return sum % 10 == 0;
    }

    private static boolean isNumberString(String str) {
        Matcher matcher = NUMBER_PATTERN.matcher(str);
        return matcher.find();
    }

    private static void doubleSecondDigit(int[] arr) {
        for (int i = 0; i < arr.length; i += 2) {
            int result = Math.multiplyExact(arr[i], 2);
            if (result > 9) {
                arr[i] = result / 10 + result % 10;
            } else arr[i] = result;
        }
    }

    private static int sumAll(int[] arr) {
        // Use lambda expression instead of loop iteration
        return Arrays.stream(arr).reduce(0, (a, b) -> a + b);
//        int sum = 0;
//        for (int element : arr) {
//            sum += element;
//        }
//        return sum;
    }

    public static void main(String[] args) {
        System.out.println("Welcome to the rabbit hole of credit card number validators.");

        /* positive cases */
        Assert.assertEquals(true, isCreditCardNumberValid("0"));
        Assert.assertEquals(true, isCreditCardNumberValid("34"));
        Assert.assertEquals(true, isCreditCardNumberValid("59"));
        Assert.assertEquals(true, isCreditCardNumberValid("12345678903555"));

        /* negative cases */
        Assert.assertEquals(false, isCreditCardNumberValid(null));
        Assert.assertEquals(false, isCreditCardNumberValid(""));
        Assert.assertEquals(false, isCreditCardNumberValid("1"));
        Assert.assertEquals(false, isCreditCardNumberValid("35"));
        Assert.assertEquals(false, isCreditCardNumberValid("58"));
        Assert.assertEquals(false, isCreditCardNumberValid("12chia"));
        Assert.assertEquals(false, isCreditCardNumberValid("creditcard"));
        Assert.assertEquals(false, isCreditCardNumberValid("&^%&*)-=s12dis"));

        System.out.println("All rabbits gone.");
    }
}
