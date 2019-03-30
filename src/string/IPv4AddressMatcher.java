package string;

import org.junit.runner.JUnitCore;
import org.junit.runner.notification.Failure;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * IPv4 uses 32-bit addresses which limits the address space to 4,294,967,296 addresses.
 * IPv4 address can be represented in any notation expressing a 32-bit integer number. They are most often written
 * in the dot-decimal notation, which consists of 4 octets expressed individually in decimal numbers and separated by periods.
 * <p>
 * Given an input string, find all substrings that match an IPv4 address in dot-decimal notation.
 * <p>
 * [In Scope]
 * Dot-decimal format should be matched.
 * E.g. 192.178.4.235, 127.0.0.1 and 0.0.0.0
 * <p>
 * [Out of Scope]
 * Other representations like below should be ignored.
 * Decimal integer format: 3221226219
 * Hexadecimal integer format: 0xC00002EB
 * Dotted hex format: 0xC0.0x00.0x02.0xEB
 * Octal byte value format: 0300.0000.0002.0353
 * <p>
 * [Example]
 * Input: this is a list of intersected IP addresses 127.0.0.1.0.2. and 620.0.0.2 is not IP
 * Output: 127.0.0.1, 27.0.0.1, 7.0.0.1, 0.0.1.0, 0.1.0.2, 20.0.0.2, 0.0.0.2
 * <p>
 * [Time complexity analysis of the Regex based approach]
 * Let n be the size of the string, and m be the size of the matching regex.
 * Since this is a partial match, the ultimate time complexity is roughly at O(m*n)
 * in the worst case where the whole string consists of valid IPv4 addresses.
 *
 * @author Ruifeng Ma
 * @since 2019-Mar-30
 */

public class IPv4AddressMatcher {

    // In IPv4 address, each dot separated decimal number section (an IP byte) ranges from 0 to 255.
    // Non-capturing groups by (?: re) are to avoid unwanted sub-matches.
    private static final String IPV4_BYTE_REGEX = "25[0-5]|2[0-4][0-9]|1[0-9][0-9]|[1-9][0-9]|[0-9]";
    private static final Pattern IPV4_PATTERN =
            Pattern.compile("(?:(?:" + IPV4_BYTE_REGEX + ")\\.){3}" + "((" + IPV4_BYTE_REGEX + "))");
    private static final Pattern DIGIT_PATTERN = Pattern.compile("\\d");

    private static final int MIN_IPv4_ADDRESS_LENGTH = 7; // e.g. 0.0.0.0

    public static List<String> parseIPv4Addresses(String inputString) {
        List<String> matches = new ArrayList<>();
        if (inputString == null) return matches;

        int i = 0;
        while (i <= inputString.length() - MIN_IPv4_ADDRESS_LENGTH) {
            Matcher digitMater = DIGIT_PATTERN.matcher(inputString.substring(i));
            if (digitMater.find()) {
                i += digitMater.start(); // index pointing to first matched digit

                Matcher iPv4Matcher = IPV4_PATTERN.matcher(inputString.substring(i));

                if (iPv4Matcher.find()) {
                    String match = iPv4Matcher.group(); // greedily obtain match like 127.0.0.198
                    matches.add(match);

                    String lastByte = iPv4Matcher.group(1); // backtracking for matches like 127.0.0.1 and 127.0.0.19
                    for (int j = 1; j<lastByte.length(); j++) matches.add(match.substring(0, match.length() - j));

                    i = i + iPv4Matcher.start() + 1; // move index to next match check point
                } else break;

            } else break;
        }
        return matches;
    }

    public static void main(String[] args) {
        System.out.println("Welcome to IPv4 address matcher!\n");

        System.out.println("Demonstrating with below sample string ...");
        String inputString = "127.0.0.1.198.192.3.45.123.4.5.6.7";
        System.out.println(inputString);
        List<String> parsedIPs = parseIPv4Addresses(inputString);
        System.out.println("Parsed IPs: ");
        for (String ip : parsedIPs) System.out.println(ip);

        System.out.println("Running unit tests ...");
        for (Failure failure : JUnitCore.runClasses(IPv4AddressMatcherTest.class).getFailures()) {
            System.out.println(failure.toString());
        }

        System.out.println("\nEnd of program.");
    }
}
