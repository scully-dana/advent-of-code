import java.util.*;
import java.util.regex.*;

public class Day3 {

    public static void solve1(String input) {
        int totalSum = 0;

        // Regex to match the pattern mul(X,Y), where X and Y are 1-3 digit numbers
        Pattern pattern = Pattern.compile("mul\\((\\d{1,3}),(\\d{1,3})\\)");

        // Match all valid mul instructions in the input
        Matcher matcher = pattern.matcher(input);

        while (matcher.find()) {
            int x = Integer.parseInt(matcher.group(1)); // Extract X
            int y = Integer.parseInt(matcher.group(2)); // Extract Y
            totalSum += x * y; // Multiply and add to the total sum
        }

        System.out.println("Total sum of multiplications: " + totalSum);
    }

    public static void solve(String input) {
        List<Object> instructions = parser2(input);

        boolean active = true;
        int sum = 0;

        for (Object instr : instructions) {
            if (active && instr instanceof Integer) {
                sum += (Integer) instr;
            }
            if (active && instr.equals("don't()")) {
                active = false;
            }
            if (!active && instr.equals("do()")) {
                active = true;
            }
        }

        System.out.println("Sum: " + sum);
    }

    public static List<Object> parser2(String input) {
        List<Object> result = new ArrayList<>();
        Pattern pattern = Pattern.compile("mul\\((\\d+),(\\d+)\\)|do\\(\\)|don't\\(\\)");
        Matcher matcher = pattern.matcher(input);

        while (matcher.find()) {
            String match = matcher.group();
            if (matcher.group(1) != null && matcher.group(2) != null) {
                // If mul(x, y) found, calculate the multiplication
                int a = Integer.parseInt(matcher.group(1));
                int b = Integer.parseInt(matcher.group(2));
                result.add(a * b);
            } else {
                // If it's a do() or don't() statement
                result.add(match);
            }
        }
        return result;
    }
}
