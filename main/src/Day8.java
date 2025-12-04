import java.util.*;

public class Day8 {

    public static int part1(List<String> data) {
        char[][] map = parseMap(data);
        int rows = map.length;
        int cols = map[0].length;

        Map<Character, List<int[]>> antennas = new HashMap<>();

        // Collect antenna coordinates
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                if (map[row][col] != '.') {
                    antennas.computeIfAbsent(map[row][col], k -> new ArrayList<>())
                            .add(new int[]{row, col});
                }
            }
        }

        Set<String> antinodes = new HashSet<>();

        // Calculate antinodes
        for (Map.Entry<Character, List<int[]>> entry : antennas.entrySet()) {
            List<int[]> coords = entry.getValue();
            for (int i = 0; i < coords.size(); i++) {
                for (int j = i + 1; j < coords.size(); j++) {
                    int[] diff = {
                            coords.get(j)[0] - coords.get(i)[0],
                            coords.get(j)[1] - coords.get(i)[1]
                    };

                    for (int[] indexAndDirection : new int[][]{{i, -1}, {j, 1}}) {
                        int idx = indexAndDirection[0];
                        int dir = indexAndDirection[1];

                        int[] pos = {
                                coords.get(idx)[0] + diff[0] * dir,
                                coords.get(idx)[1] + diff[1] * dir
                        };

                        if (pos[0] >= 0 && pos[0] < rows && pos[1] >= 0 && pos[1] < cols) {
                            antinodes.add(pos[0] + "," + pos[1]);
                        }
                    }
                }
            }
        }

        return antinodes.size();
    }

    public static int part2(List<String> data) {
        char[][] map = parseMap(data);
        int rows = map.length;
        int cols = map[0].length;

        Map<Character, List<int[]>> antennas = new HashMap<>();

        // Collect antenna coordinates
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                if (map[row][col] != '.') {
                    antennas.computeIfAbsent(map[row][col], k -> new ArrayList<>())
                            .add(new int[]{row, col});
                }
            }
        }

        Set<String> antinodes = new HashSet<>();

        // Calculate antinodes
        for (Map.Entry<Character, List<int[]>> entry : antennas.entrySet()) {
            List<int[]> coords = entry.getValue();
            for (int i = 0; i < coords.size(); i++) {
                for (int j = i + 1; j < coords.size(); j++) {
                    int[] diff = {
                            coords.get(j)[0] - coords.get(i)[0],
                            coords.get(j)[1] - coords.get(i)[1]
                    };

                    for (int[] indexAndDirection : new int[][]{{i, -1}, {j, 1}}) {
                        int idx = indexAndDirection[0];
                        int dir = indexAndDirection[1];

                        int[] pos = Arrays.copyOf(coords.get(idx), 2);

                        while (pos[0] >= 0 && pos[0] < rows && pos[1] >= 0 && pos[1] < cols) {
                            antinodes.add(pos[0] + "," + pos[1]);
                            pos[0] += diff[0] * dir;
                            pos[1] += diff[1] * dir;
                        }
                    }
                }
            }
        }
        return antinodes.size();
    }

    private static char[][] parseMap(List<String> data) {
        int rows = data.size();
        int cols = data.getFirst().length();
        char[][] map = new char[rows][cols];

        for (int i = 0; i < rows; i++) {
            map[i] = data.get(i).toCharArray();
        }

        return map;
    }
}
