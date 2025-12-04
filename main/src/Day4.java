import java.util.List;

public class Day4 {
    // Method to count XMAS patterns for Part 1
    public static int countXmasPartOne(String[][] crossword) {
        int count = 0;
        for (int i = 0; i < crossword.length; i++) {
            for (int j = 0; j < crossword[i].length; j++) {
                if (crossword[i][j].equals("X")) {
                    count += search(crossword, i, j, "XMAS");
                }
            }
        }
        return count;
    }

    // Method to count XMAS patterns for Part 2
    public static int countXmasPartTwo(String[][] crossword) {
        int count = 0;
        for (int i = 1; i < crossword.length - 1; i++) {
            for (int j = 1; j < crossword[i].length - 1; j++) {
                if (crossword[i][j].equals("A")) {
                    String topLeftBottomRight = crossword[i - 1][j - 1] + crossword[i][j] + crossword[i + 1][j + 1];
                    String topRightBottomLeft = crossword[i - 1][j + 1] + crossword[i][j] + crossword[i + 1][j - 1];

                    if (topLeftBottomRight.equals("SAM") || topLeftBottomRight.equals("MAS")) {
                        if (topRightBottomLeft.equals("SAM") || topRightBottomLeft.equals("MAS")) {
                            count++;
                        }
                    }
                }
            }
        }
        return count;
    }

    // Helper method to search for the word in all directions
    public static int search(String[][] crossword, int startX, int startY, String target) {
        int count = 0;
        // Directions: Right, Left, Down, Up, Down-Right, Up-Right, Up-Left, Down-Left
        int[][] directions = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}, {1, 1}, {-1, 1}, {-1, -1}, {1, -1}};

        for (int[] direction : directions) {
            count += searchDirection(crossword, startX, startY, direction[0], direction[1], target);
        }
        return count;
    }

    // Helper method to search in a specific direction
    private static int searchDirection(String[][] crossword, int startX, int startY, int dx, int dy, String target) {
        StringBuilder word = new StringBuilder();
        for (int i = 0; i < target.length(); i++) {
            int newX = startX + i * dx;
            int newY = startY + i * dy;

            // Check bounds
            if (newX < 0 || newY < 0 || newX >= crossword.length || newY >= crossword[0].length) {
                return 0;
            }
            word.append(crossword[newX][newY]);
            // Check if the current part of the word matches
            if (!target.startsWith(word.toString())) {
                return 0;
            }
            // If the word is found, return 1
            if (word.toString().equals(target)) {
                return 1;
            }
        }
        return 0;
    }

    // Main method for testing
    public static void solve(List<String> lines) {

        int rows = lines.size();
        int cols = lines.getFirst().length(); // Assuming all strings have the same length
        String[][] crossword = new String[rows][cols];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                crossword[i][j] = String.valueOf(lines.get(i).charAt(j)); // Fill array with characters
            }
        }

        // Part 1: Count XMAS patterns
        int part1Count = countXmasPartOne(crossword);
        System.out.println("Part 1: " + part1Count);  // Output result for part 1

        // Part 2: Count X-MAS shapes
        int part2Count = countXmasPartTwo(crossword);
        System.out.println("Part 2: " + part2Count);  // Output result for part 2
    }
}


