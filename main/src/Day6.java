import java.util.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Day6 {
    public static void solve1(List<String> input) {

        String[] map = input.toArray(new String[0]);
        System.out.println("Distinct Positions Visited: " + simulateGuardPath(map));
    }

    public static int simulateGuardPath(String[] map) {
        int rows = map.length;
        int cols = map[0].length();

        int[] start = findStart(map);
        int x = start[0], y = start[1];
        char direction = start[2] == 0 ? '^' : (start[2] == 1 ? '>' : (start[2] == 2 ? 'v' : '<'));

        Set<String> visited = new HashSet<>();
        visited.add(x + "," + y);

        int[][] deltas = {{-1, 0}, {0, 1}, {1, 0}, {0, -1}}; // Up, Right, Down, Left
        int dirIndex = start[2];

        while (true) {
            int nextX = x + deltas[dirIndex][0];
            int nextY = y + deltas[dirIndex][1];

            if (nextX < 0 || nextX >= rows || nextY < 0 || nextY >= cols) {
                // Guard has left the map
                break;
            }

            if (map[nextX].charAt(nextY) == '#') {
                // Turn right
                dirIndex = (dirIndex + 1) % 4;
            } else {
                // Move forward
                x = nextX;
                y = nextY;
                visited.add(x + "," + y);
            }
        }

        return visited.size();
    }

    private static int[] findStart(String[] map) {
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length(); j++) {
                char c = map[i].charAt(j);
                if (c == '^') return new int[]{i, j, 0};
                if (c == '>') return new int[]{i, j, 1};
                if (c == 'v') return new int[]{i, j, 2};
                if (c == '<') return new int[]{i, j, 3};
            }
        }
        throw new IllegalArgumentException("No starting position found");
    }


    public static void solve(List<String> grid) throws IOException {
        int p1 = 0;
        int p2 = 0;

        int R = grid.size();
        int C = grid.getFirst().length();

        int sr = -1, sc = -1; // Starting row and column for the guard
        // Find the starting position of the guard ('^')
        for (int r = 0; r < R; r++) {
            for (int c = 0; c < C; c++) {
                if (grid.get(r).charAt(c) == '^') {
                    sr = r;
                    sc = c;
                    break;
                }
            }
        }

        // Process for each possible origin position ('#')
        for (int o_r = 0; o_r < R; o_r++) {
            for (int o_c = 0; o_c < C; o_c++) {
                int r = sr, c = sc;
                int d = 0; // 0=up, 1=right, 2=down, 3=left
                Set<String> SEEN = new HashSet<>();
                Set<String> SEEN_RC = new HashSet<>();

                while (true) {
                    String state = r + "," + c + "," + d;
                    if (SEEN.contains(state)) {
                        p2++;
                        break;
                    }
                    SEEN.add(state);
                    SEEN_RC.add(r + "," + c);
                    int[] direction = new int[]{-1, 0, 0, 1, 1, 0, 0, -1}; // up, right, down, left
                    int dr = direction[d * 2];
                    int dc = direction[d * 2 + 1];
                    int rr = r + dr;
                    int cc = c + dc;

                    if (rr < 0 || rr >= R || cc < 0 || cc >= C) {
                        if (grid.get(o_r).charAt(o_c) == '#') {
                            p1 = SEEN_RC.size();
                        }
                        break;
                    }

                    if (grid.get(rr).charAt(cc) == '#' || (rr == o_r && cc == o_c)) {
                        d = (d + 1) % 4; // Turn right (clockwise)
                    } else {
                        r = rr;
                        c = cc;
                    }
                }
            }
        }

        // Print results for both parts
        System.out.println(p1);
        System.out.println(p2);
    }
}
