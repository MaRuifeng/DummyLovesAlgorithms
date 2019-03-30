package string;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class IPv4AddressMatcherTest {
    private String inputString;
    private List<String> expectedIPList = new ArrayList<>();

    private final void test(String inputString, List<String> expectedIPList) {
        System.out.println("\n/******** TEST START ********/");
        System.out.println("<<< Input string\n" + inputString);

        List<String> actualIPList = IPv4AddressMatcher.parseIPv4Addresses(inputString);
        System.out.println(">>> Parsed IPs");
        for (String ip : actualIPList) System.out.println(ip);
        Assert.assertEquals(expectedIPList.size(), actualIPList.size());
        Assert.assertEquals(true, expectedIPList.containsAll(actualIPList));
        Assert.assertEquals(true, actualIPList.containsAll(expectedIPList));

        System.out.println("/******** TEST END ********/");
    }

    /* positive cases */
    @Test
    public final void shouldParseTheSimplestIPv4Address() {
        inputString = "0.0.0.0";
        expectedIPList.clear(); // reduce load on GC
        expectedIPList.add("0.0.0.0");
        test(inputString, expectedIPList);
    }

    @Test
    public final void shouldParseIPv4AddressWithMultipleDigitsInAByte() {
        inputString = "127.0.0.1";
        expectedIPList.clear();
        expectedIPList.add("127.0.0.1");
        expectedIPList.add("27.0.0.1");
        expectedIPList.add("7.0.0.1");
        test(inputString, expectedIPList);
    }

    @Test
    public final void shouldParseIPv4AddressWithLeadingZeroInLastByte() {
        inputString = "9.8.1.009";
        expectedIPList.clear();
        expectedIPList.add("9.8.1.0");
        test(inputString, expectedIPList);
    }

    @Test
    public final void shouldParseIntersectedIPv4Addresses() {
        inputString = "0.0.0.1.198.192.3.45.1";
        expectedIPList.clear();
        expectedIPList.add("0.0.0.1");
        expectedIPList.add("0.0.1.1");
        expectedIPList.add("0.0.1.19");
        expectedIPList.add("0.0.1.198");
        expectedIPList.add("0.1.198.1");
        expectedIPList.add("0.1.198.19");
        expectedIPList.add("0.1.198.192");
        expectedIPList.add("1.198.192.3");
        expectedIPList.add("198.192.3.4");
        expectedIPList.add("98.192.3.4");
        expectedIPList.add("8.192.3.4");
        expectedIPList.add("198.192.3.45");
        expectedIPList.add("98.192.3.45");
        expectedIPList.add("8.192.3.45");
        expectedIPList.add("192.3.45.1");
        expectedIPList.add("92.3.45.1");
        expectedIPList.add("2.3.45.1");
        test(inputString, expectedIPList);
    }

    @Test
    public final void shouldParseIPv4AddressesSeparatedByMultipleDots() {
        inputString = "9.0.0.1..8.0.1.7....4.5.";
        expectedIPList.clear();
        expectedIPList.add("9.0.0.1");
        expectedIPList.add("8.0.1.7");
        test(inputString, expectedIPList);
    }

    @Test
    public final void shouldParseIPv4AddressesSeparatedBySpaces() {
        inputString = "91.2.3.1. .8. 0.1.7... .4.5..";
        expectedIPList.clear();
        expectedIPList.add("91.2.3.1");
        expectedIPList.add("1.2.3.1");
        test(inputString, expectedIPList);
    }

    @Test
    public final void shouldParseIPv4AddressesFromAlphaNumericString() {
        inputString = "wifi.sso.jwtauth.8.x.x.191.9.0.8..segmenTFault..1.2.3sshDlinux2.3.4.5...port.";
        expectedIPList.clear();
        expectedIPList.add("191.9.0.8");
        expectedIPList.add("91.9.0.8");
        expectedIPList.add("1.9.0.8");
        expectedIPList.add("2.3.4.5");
        test(inputString, expectedIPList);
    }

    @Test
    public final void shouldParseIPv4AddressesFromStringMixedWithSpecialCharacters() {
        inputString = "//*.!\\b\\s\\d.3.4.8.9..*===_.+.\\/\\/s\\&&^%$#....}{<>?`@`3~`";
        expectedIPList.clear();
        expectedIPList.add("3.4.8.9");
        test(inputString, expectedIPList);
    }

    /* negative cases */
    @Test
    public final void shouldReturnEmptyForNullString() {
        inputString = null;
        expectedIPList.clear();
        test(inputString, expectedIPList);
    }

    @Test
    public final void shouldReturnEmptyForEmptyString() {
        inputString = "";
        expectedIPList.clear();
        test(inputString, expectedIPList);
    }

    @Test
    public final void shouldReturnEmptyForStringContainingOnlyNumbers() {
        inputString = "1981921871561325426";
        expectedIPList.clear();
        test(inputString, expectedIPList);
    }

    @Test
    public final void shouldReturnEmptyForStringContainingOnlyDots() {
        inputString = "............................";
        expectedIPList.clear();
        test(inputString, expectedIPList);
    }


    @Test
    public final void shouldReturnEmptyForStringNotContainingDigit() {
        inputString = "aSimpleAlphabeticalString";
        expectedIPList.clear();
        test(inputString, expectedIPList);
    }

    @Test
    public final void shouldReturnEmptyForStringNotContainingProperIP() {
        inputString = "9string0not8containing1properIP";
        expectedIPList.clear();
        test(inputString, expectedIPList);
    }

    @Test
    public final void shouldReturnEmptyForDecimalIntegerIP() {
        inputString = "3221226219";
        expectedIPList.clear();
        test(inputString, expectedIPList);
    }

    @Test
    public final void shouldReturnEmptyForHexadecimalIntegerIP() {
        inputString = "0xC00002EB";
        expectedIPList.clear();
        test(inputString, expectedIPList);
    }

    @Test
    public final void shouldReturnEmptyForDottedHexIP() {
        inputString = "0xC0.0x00.0x02.0xEB";
        expectedIPList.clear();
        test(inputString, expectedIPList);
    }

    @Test
    public final void shouldReturnEmptyForOctalByteIP() {
        inputString = "0300.0000.0002.0353";
        expectedIPList.clear();
        test(inputString, expectedIPList);
    }

    @Test
    public final void shouldReturnEmptyForPartialIP() {
        inputString = "127.0.0";
        expectedIPList.clear();
        test(inputString, expectedIPList);
    }

    @Test
    public final void shouldReturnEmptyForIllFormattedIP() {
        inputString = "90.09.09.008";
        expectedIPList.clear();
        test(inputString, expectedIPList);
    }
}
