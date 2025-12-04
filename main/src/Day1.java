import java.util.*;

public class Day1 {
    public static void solve(List<String> inputLines) {
        System.out.println("--- Day 1 ---");

        List<Integer> list1 = new ArrayList<>();
        List<Integer> list2 = new ArrayList<>();

        for (String s: inputLines) {
            list1.add(parseLineToList(s).get(0));
            list2.add(parseLineToList(s).get(1));
        }

        // Part 1
        System.out.println("Part 1: Total distance: " + calculateTotalDistance(list1, list2));

        // Part 2
        System.out.println("Part 2: Similarity score: " + calculateSimilarityScore(list1, list2));
    }

    private static List<Integer> parseLineToList(String line) {
        String[] parts = line.split("\\s+");
        List<Integer> result = new ArrayList<>();
        for (String part : parts) {
            result.add(Integer.parseInt(part));
        }
        return result;
    }

    private static int calculateTotalDistance(List<Integer> list1, List<Integer> list2) {
        Collections.sort(list1);
        Collections.sort(list2);
        int totalDistance = 0;
        for (int i = 0; i < list1.size(); i++) {
            totalDistance += Math.abs(list1.get(i) - list2.get(i));
        }
        return totalDistance;
    }

    private static int calculateSimilarityScore(List<Integer> list1, List<Integer> list2) {
        Map<Integer, Integer> frequencyMap = new HashMap<>();
        for (int num : list2) {
            frequencyMap.put(num, frequencyMap.getOrDefault(num, 0) + 1);
        }
        int similarityScore = 0;
        for (int num : list1) {
            similarityScore += num * frequencyMap.getOrDefault(num, 0);
        }
        return similarityScore;
    }
}
