package string;

import org.junit.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * String to int converter.
 * <p>
 * Allowed characters in the string: '+', '-', '.' and [0-9]
 *
 * @author sg.ruifeng.ma
 * @since 2019-06-18
 */

public class StringToIntConverter {
    public static final Pattern regex = Pattern.compile("^([+-]?)(\\d{1,10})(\\.?)(0*)$");

    private static int strToInt(String s) {
        if (s == null || s.isEmpty()) return 0;

        Matcher matcher = regex.matcher(s);
        if (!matcher.find()) return 0;

        int sign = (matcher.group(1) != null && matcher.group(1).equals("-")) ? -1 : 1;
        String intStr = matcher.group(2);

        // check for Integer max
        String maxIntStr = String.valueOf(Integer.MAX_VALUE);
        String minIntStr = String.valueOf(Integer.MIN_VALUE).replace("-", "");
        if (intStr.length() == 10) {
            for (int i = 0; i < 10; i++) {
                if (sign == 1) {
                    if (intStr.charAt(i) > maxIntStr.charAt(i)) return Integer.MAX_VALUE;
                    if (intStr.charAt(i) < maxIntStr.charAt(i)) break;
                }

                if (sign == -1) {
                    if (intStr.charAt(i) > minIntStr.charAt(i)) return Integer.MIN_VALUE;
                    if (intStr.charAt(i) < minIntStr.charAt(i)) break;
                }
            }
        }

        // parse value;
        int intVal = 0, base = 1;
        for (int i = intStr.length() - 1; i >= 0; i--) {
            intVal += (intStr.charAt(i) - '0') * base;
            base *= 10;
        }

        return intVal * sign;
    }

    private static class TestData {
        String s;
        int value;

        public TestData(String s, int value) {
            this.s = s;
            this.value = value;
        }
    }

    private static void test(List<TestData> testDataList) {
        testDataList.forEach(data -> Assert.assertEquals(data.value, strToInt(data.s)));
    }

    public static void main(String[] args) {
        List<TestData> testDataList = new ArrayList<>();

        testDataList.add(new TestData("1234", 1234));
        testDataList.add(new TestData("-100", -100));
        testDataList.add(new TestData("+2569", 2569));
        testDataList.add(new TestData("0", 0));
        testDataList.add(new TestData("00000", 0));
        testDataList.add(new TestData("125.0", 125));
        testDataList.add(new TestData("000987", 987));
        testDataList.add(new TestData("6987.000", 6987));
        testDataList.add(new TestData("0076534.00", 76534));
        testDataList.add(new TestData("+1987.00", 1987));
        testDataList.add(new TestData("-9854344.00", -9854344));
        testDataList.add(new TestData("1245.", 1245));
        testDataList.add(new TestData("-9763.", -9763));
        testDataList.add(new TestData(String.valueOf(Integer.MAX_VALUE - 1), Integer.MAX_VALUE - 1));
        testDataList.add(new TestData(String.valueOf(Integer.MAX_VALUE), Integer.MAX_VALUE));
        testDataList.add(new TestData(String.valueOf(Integer.MIN_VALUE + 1), Integer.MIN_VALUE + 1));
        testDataList.add(new TestData(String.valueOf(Integer.MIN_VALUE), Integer.MIN_VALUE));

        testDataList.add(new TestData(null, 0));
        testDataList.add(new TestData("", 0));
        testDataList.add(new TestData("9879..", 0));
        testDataList.add(new TestData("98k876", 0));
        testDataList.add(new TestData("++984.0", 0));
        testDataList.add(new TestData("--9873.0", 0));
        testDataList.add(new TestData("+-1234", 0));
        testDataList.add(new TestData("9865.89", 0));
        testDataList.add(new TestData("NotANumber", 0));
        testDataList.add(new TestData("2147483648", Integer.MAX_VALUE));
        testDataList.add(new TestData("-2147483649", Integer.MIN_VALUE));
        testDataList.add(new TestData("+3147483648", Integer.MAX_VALUE));
        testDataList.add(new TestData("-9000000000", Integer.MIN_VALUE));

        test(testDataList);
    }
}
