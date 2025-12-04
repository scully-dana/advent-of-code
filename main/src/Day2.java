import java.util.*;

public class Day2 {

    public static void solve(List<String> inputLines) {
        int safeReportCount = 0;

        for (String line : inputLines) {
            List<Integer> levels = parseLevels(line);
            if (isSafe(levels) || canBeMadeSafe(levels)) {
                safeReportCount++;
            }
        }

        System.out.println("Number of safe reports: " + safeReportCount);
    }

    private static List<Integer> parseLevels(String line) {
        String[] parts = line.split(" ");
        List<Integer> levels = new ArrayList<>();
        for (String part : parts) {
            levels.add(Integer.parseInt(part));
        }
        return levels;
    }

    private static boolean isSafe(List<Integer> levels) {
        // Check that all adjacent differences are between 1 and 3 (inclusive)
        for (int i = 1; i < levels.size(); i++) {
            int difference = Math.abs(levels.get(i) - levels.get(i - 1));
            if (difference < 1 || difference > 3) {
                return false;
            }
        }

        // Check if the levels are either all increasing or all decreasing
        boolean isIncreasing = true;
        boolean isDecreasing = true;

        for (int i = 1; i < levels.size(); i++) {
            if (levels.get(i) < levels.get(i - 1)) {
                isIncreasing = false;
            }
            if (levels.get(i) > levels.get(i - 1)) {
                isDecreasing = false;
            }
        }

        return isIncreasing || isDecreasing;
    }

    private static boolean canBeMadeSafe(List<Integer> levels) {
        // Try removing each level and check if the report becomes safe
        for (int i = 0; i < levels.size(); i++) {
            List<Integer> newLevels = new ArrayList<>(levels);
            newLevels.remove(i); // Remove one level at a time
            if (isSafe(newLevels)) {
                return true; // If removing this level makes it safe, return true
            }
        }
        return false; // If no level removal makes it safe, return false
    }
}
