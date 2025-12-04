import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class Day7 {
    public static long calculateTotalCalibration1(String[] input) {
        long totalCalibration = 0;

        for (String line : input) {
            String[] parts = line.split(": ");
            long target = Long.parseLong(parts[0]);
            String[] numbers = parts[1].split(" ");

            if (isValidEquation1(target, numbers)) {
                totalCalibration += target;
            }
        }

        return totalCalibration;
    }

    private static boolean isValidEquation1(long target, String[] numbers) {
        List<String[]> operatorCombinations = generateOperatorCombinations(numbers.length - 1);

        for (String[] operators : operatorCombinations) {
            if (evaluateExpression1(numbers, operators) == target) {
                return true;
            }
        }

        return false;
    }

    private static List<String[]> generateOperatorCombinations(long operatorCount) {
        List<String[]> combinations = new ArrayList<>();
        long totalCombinations = (long) Math.pow(2, operatorCount);

        for (int i = 0; i < totalCombinations; i++) {
            String[] operators = new String[(int) operatorCount];
            for (int j = 0; j < operatorCount; j++) {
                operators[j] = (i & (1 << j)) == 0 ? "+" : "*";
            }
            combinations.add(operators);
        }

        return combinations;
    }

    private static long evaluateExpression1(String[] numbers, String[] operators) {
        long result = Long.parseLong(numbers[0]);

        for (int i = 0; i < operators.length; i++) {
            long nextNumber = Long.parseLong(numbers[i + 1]);

            if (operators[i].equals("+")) {
                result += nextNumber;
            } else {
                result *= nextNumber;
            }
        }

        return result;
    }

    public static void solve1(List<String> input) {
        String[] calibs = input.toArray(new String[0]);
        long result = calculateTotalCalibration1(calibs);
        System.out.println("Total Calibration Result: " + result);
    }

    public static BigInteger calculateTotalCalibration(String[] input) {
        BigInteger totalCalibration = BigInteger.ZERO;

        for (String line : input) {
            String[] parts = line.split(": ");
            BigInteger target = new BigInteger(parts[0]);
            String[] numbers = parts[1].split(" ");

            if (isValidEquation(target, numbers)) {
                totalCalibration = totalCalibration.add(target);
            }
        }

        return totalCalibration;
    }

    private static boolean isValidEquation(BigInteger target, String[] numbers) { // 2 + 3 + 6 * 8
        List<String[]> operatorCombinations = generateOperatorCombinations(numbers.length - 1);

        for (String[] operators : operatorCombinations) {
            if (evaluateExpression(numbers, operators).equals(target)) {
                return true;
            }
        }

        return false;
    }

    private static List<String[]> generateOperatorCombinations(int operatorCount) {
        List<String[]> combinations = new ArrayList<>();
        int totalCombinations = (int) Math.pow(3, operatorCount); // 3 operators: +, *, ||

        for (int i = 0; i < totalCombinations; i++) {
            String[] operators = new String[operatorCount];
            int temp = i;
            for (int j = 0; j < operatorCount; j++) {
                int op = temp % 3;
                operators[j] = op == 0 ? "+" : op == 1 ? "*" : "||";
                temp /= 3;
            }
            combinations.add(operators);
        }

        return combinations;
    }

    private static BigInteger evaluateExpression(String[] numbers, String[] operators) {
        BigInteger result = new BigInteger(numbers[0]);

        for (int i = 0; i < operators.length; i++) {
            BigInteger nextNumber = new BigInteger(numbers[i + 1]);
            String operator = operators[i];

            result = switch (operator) {
                case "+" -> result.add(nextNumber);
                case "*" -> result.multiply(nextNumber);
                case "||" -> new BigInteger(result + nextNumber.toString());
                default -> result;
            };
        }

        return result;
    }

    public static void solve(List<String> input) {
        String[] calibs = input.toArray(new String[0]);
        BigInteger result = calculateTotalCalibration(calibs);
        System.out.println("Total Calibration Result: " + result);
    }
}
