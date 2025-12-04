import java.util.*;

public class Day5 {
    public static void solve1(List<String> input) {
        int splitIndex = input.indexOf("");
        List<String> rules = input.subList(0, splitIndex);
        List<String> updates = input.subList(splitIndex + 1, input.size());

        // Parse the rules into a map
        Map<Integer, Set<Integer>> ruleMap = parseRules1(rules);

        // Process each update and find valid ones
        int sumOfMiddles = 0;
        for (String update : updates) {
            List<Integer> pages = parseUpdate(update);
            if (isValidUpdate1(pages, ruleMap)) {
                sumOfMiddles += findMiddlePage(pages);
            }
        }

        // Output the result
        System.out.println("Sum of middle page numbers: " + sumOfMiddles);
    }
    // Parses the rules into a map where key -> must be printed before set
    private static Map<Integer, Set<Integer>> parseRules1(List<String> rules) {
        Map<Integer, Set<Integer>> ruleMap = new HashMap<>();
        for (String rule : rules) {
            String[] parts = rule.split("\\|");
            int before = Integer.parseInt(parts[0]);
            int after = Integer.parseInt(parts[1]);
            ruleMap.putIfAbsent(before, new HashSet<>());
            ruleMap.get(before).add(after);
        }
        return ruleMap;
    }

    // Parses an update into a list of integers
    private static List<Integer> parseUpdate(String update) {
        String[] parts = update.split(",");
        List<Integer> pages = new ArrayList<>();
        for (String part : parts) {
            pages.add(Integer.parseInt(part));
        }
        return pages;
    }

    // Checks if the update respects all rules
    private static boolean isValidUpdate1(List<Integer> pages, Map<Integer, Set<Integer>> ruleMap) {
        Map<Integer, Integer> positionMap = new HashMap<>();
        for (int i = 0; i < pages.size(); i++) {
            positionMap.put(pages.get(i), i);
        }

        for (Map.Entry<Integer, Set<Integer>> entry : ruleMap.entrySet()) {
            int before = entry.getKey();
            for (int after : entry.getValue()) {
                if (positionMap.containsKey(before) && positionMap.containsKey(after)) {
                    if (positionMap.get(before) >= positionMap.get(after)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    // Finds the middle page in the update
    private static int findMiddlePage(List<Integer> pages) {
        return pages.get(pages.size() / 2);
    }

    public static int solve(List<String> lines) {
        int blankLineIndex = lines.indexOf("");

        List<String> ruleLines = lines.subList(0, blankLineIndex);
        List<String> updateLines = lines.subList(blankLineIndex + 1, lines.size());

        List<Rule> rules = parseRules(ruleLines);
        List<List<String>> updates = parseUpdates(updateLines);

        int total = 0;

        for (List<String> update : updates) {
            if (!isValidUpdate(update, rules)) {
                reorderUpdate(update, rules);
                total += Integer.parseInt(update.get(update.size() / 2));
            }
        }

        return total;
    }

    private static List<Rule> parseRules(List<String> ruleLines) {
        List<Rule> rules = new ArrayList<>();
        for (String rule : ruleLines) {
            String[] parts = rule.split("\\|");
            rules.add(new Rule(parts[0], parts[1]));
        }
        return rules;
    }

    private static List<List<String>> parseUpdates(List<String> updateLines) {
        List<List<String>> updates = new ArrayList<>();
        for (String line : updateLines) {
            updates.add(Arrays.asList(line.split(",")));
        }
        return updates;
    }

    private static boolean isValidUpdate(List<String> update, List<Rule> rules) {
        for (Rule rule : rules) {
            if (update.contains(rule.X) && update.contains(rule.Y)) {
                if (update.indexOf(rule.X) > update.indexOf(rule.Y)) {
                    return false;
                }
            }
        }
        return true;
    }

    private static void reorderUpdate(List<String> update, List<Rule> rules) {
        update.sort((x, y) -> {
            for (Rule rule : rules) {
                if (rule.X.equals(x) && rule.Y.equals(y)) return -1;
                if (rule.X.equals(y) && rule.Y.equals(x)) return 1;
            }
            return 0;
        });
    }

    private static class Rule {
        String X, Y;

        Rule(String X, String Y) {
            this.X = X;
            this.Y = Y;
        }
    }

}
