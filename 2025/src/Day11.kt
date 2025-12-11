import java.io.BufferedReader
import java.io.File
import java.io.Reader
import java.math.BigInteger
import java.util.ArrayDeque

fun main(args: Array<String>) {
    val dayNumber = args.firstOrNull()?.toIntOrNull() ?: 11
    if (dayNumber != 11) {
        error("This combined file only contains Day 11. Run with no args or with 11.")
    }

    val solver = Day11()

    val inputPath = "input.txt"

    val is1 = solver::class.java.getResourceAsStream("/$inputPath")
        ?: File(inputPath).takeIf { it.exists() }?.inputStream()
        ?: error("Input file not found: $inputPath")

    val part1Reader = is1.reader()
    val part1Result = solver.part1(part1Reader)
    println("Part 1: $part1Result")

    val is2 = solver::class.java.getResourceAsStream("/$inputPath")
        ?: File(inputPath).takeIf { it.exists() }?.inputStream()
        ?: error("Input file not found: $inputPath")

    val part2Reader = is2.reader()
    val part2Result = solver.part2(part2Reader)
    println("Part 2: $part2Result")
}

class Day11 {

    fun part1(input: Reader): Any {
        val graph = parseGraph(input)
        val canReachOut = computeCanReachTarget(graph, "out")
        return countAllPaths(graph, start = "you", target = "out", canReachOut = canReachOut)
    }

    fun part2(input: Reader): Any {
        val graph = parseGraph(input)
        val mustPass = listOf("dac", "fft")

        val canReachOut = computeCanReachTarget(graph, "out")
        val reverseReachForRequired = computeReverseReachForTargets(graph, mustPass)

        return countAllPathsPassingThrough(
            graph = graph,
            start = "svr",
            target = "out",
            mustPass = mustPass,
            canReachOut = canReachOut,
            reverseReachForRequired = reverseReachForRequired
        )
    }

    private fun parseGraph(reader: Reader): Map<String, Set<String>> {
        val graph = mutableMapOf<String, MutableSet<String>>()
        val br = BufferedReader(reader)

        br.lineSequence().forEach { line ->
            val trimmed = line.trim()
            if (trimmed.isEmpty()) return@forEach

            val (from, toList) = parseConnection(trimmed)
            val neighbors = graph.getOrPut(from) { mutableSetOf() }
            neighbors.addAll(toList)
        }

        return graph
    }

    private fun parseConnection(line: String): Pair<String, List<String>> {
        val parts = line.split("\\s+".toRegex()).filter { it.isNotEmpty() }
        require(parts.isNotEmpty()) { "Empty line in parseConnection" }
        val from = parts[0].removeSuffix(":")
        val toList = if (parts.size > 1) parts.subList(1, parts.size) else emptyList()
        return from to toList
    }

    private fun computeCanReachTarget(graph: Map<String, Set<String>>, target: String): Set<String> {
        val rev = mutableMapOf<String, MutableList<String>>()
        graph.forEach { (u, nbrs) ->
            nbrs.forEach { v ->
                rev.getOrPut(v) { mutableListOf() }.add(u)
            }
        }

        val canReach = mutableSetOf<String>()
        val dq = ArrayDeque<String>()
        dq.add(target)
        while (dq.isNotEmpty()) {
            val u = dq.removeFirst()
            if (!canReach.add(u)) continue
            val parents = rev[u] ?: continue
            for (p in parents) {
                if (p !in canReach) dq.add(p)
            }
        }
        return canReach
    }

    private fun computeReverseReachForTargets(
        graph: Map<String, Set<String>>,
        targets: List<String>
    ): Map<String, Set<String>> {
        val rev = mutableMapOf<String, MutableList<String>>()
        graph.forEach { (u, nbrs) ->
            nbrs.forEach { v ->
                rev.getOrPut(v) { mutableListOf() }.add(u)
            }
        }

        val map = mutableMapOf<String, Set<String>>()
        for (t in targets) {
            val canReach = mutableSetOf<String>()
            val dq = ArrayDeque<String>()
            dq.add(t)
            while (dq.isNotEmpty()) {
                val u = dq.removeFirst()
                if (!canReach.add(u)) continue
                val parents = rev[u] ?: continue
                for (p in parents) {
                    if (p !in canReach) dq.add(p)
                }
            }
            map[t] = canReach
        }
        return map
    }

    private fun countAllPaths(
        graph: Map<String, Set<String>>,
        start: String,
        target: String,
        canReachOut: Set<String>
    ): BigInteger {
        if (start !in canReachOut) return BigInteger.ZERO
        return dfsSimple(start, target, graph, visited = mutableSetOf(), canReachOut = canReachOut)
    }

    private fun dfsSimple(
        current: String,
        target: String,
        graph: Map<String, Set<String>>,
        visited: MutableSet<String>,
        canReachOut: Set<String>
    ): BigInteger {
        if (current in visited) return BigInteger.ZERO
        if (current == target) return BigInteger.ONE

        if (current !in canReachOut) return BigInteger.ZERO

        visited += current
        var total = BigInteger.ZERO

        val neighbors = graph[current]
        if (neighbors != null) {
            for (next in neighbors) {
                if (next in visited) continue
                if (next !in canReachOut) continue
                val sub = dfsSimple(next, target, graph, visited, canReachOut)
                total = total.add(sub)
            }
        }

        visited -= current
        return total
    }

    private data class MemoKey(val node: String, val mask: Int)

    private fun countAllPathsPassingThrough(
        graph: Map<String, Set<String>>,
        start: String,
        target: String,
        mustPass: List<String>,
        canReachOut: Set<String>,
        reverseReachForRequired: Map<String, Set<String>>
    ): BigInteger {
        for (req in mustPass) {
            if (!reverseReachForRequired.containsKey(req)) return BigInteger.ZERO
            if (req !in canReachOut) return BigInteger.ZERO
        }

        val requiredBits = mustPass.withIndex().associate { (i, dev) -> dev to i }
        val targetMask = (1 shl mustPass.size) - 1

        if (start !in canReachOut) return BigInteger.ZERO

        val memo = mutableMapOf<MemoKey, BigInteger>()
        return dfsMemo(
            current = start,
            target = target,
            graph = graph,
            requiredBits = requiredBits,
            currentMask = 0,
            targetMask = targetMask,
            visited = mutableSetOf(),
            memo = memo,
            canReachOut = canReachOut,
            reverseReachForRequired = reverseReachForRequired
        )
    }

    private fun dfsMemo(
        current: String,
        target: String,
        graph: Map<String, Set<String>>,
        requiredBits: Map<String, Int>,
        currentMask: Int,
        targetMask: Int,
        visited: MutableSet<String>,
        memo: MutableMap<MemoKey, BigInteger>,
        canReachOut: Set<String>,
        reverseReachForRequired: Map<String, Set<String>>
    ): BigInteger {
        if (current in visited) return BigInteger.ZERO

        if (current !in canReachOut) return BigInteger.ZERO

        var newMask = currentMask
        requiredBits[current]?.let { pos -> newMask = currentMask or (1 shl pos) }

        val missingBits = targetMask and newMask.inv()
        if (missingBits != 0) {
            for ((dev, pos) in requiredBits) {
                val bit = 1 shl pos
                if ((newMask and bit) == 0) {
                    val reachableSet = reverseReachForRequired[dev]
                    if (reachableSet == null || current !in reachableSet) {
                        return BigInteger.ZERO
                    }
                }
            }
        }

        if (current == target) {
            return if (newMask == targetMask) BigInteger.ONE else BigInteger.ZERO
        }

        val key = MemoKey(current, newMask)
        memo[key]?.let { return it }

        visited += current
        var total = BigInteger.ZERO

        val neighbors = graph[current]
        if (neighbors != null) {
            for (next in neighbors) {
                if (next in visited) continue
                if (next !in canReachOut) continue
                var skip = false
                if (newMask != targetMask) {
                    for ((dev, pos) in requiredBits) {
                        val bit = 1 shl pos
                        if ((newMask and bit) == 0) {
                            val reachableSet = reverseReachForRequired[dev]
                            if (reachableSet == null || next !in reachableSet) {
                                skip = true
                                break
                            }
                        }
                    }
                }
                if (skip) continue

                val sub = dfsMemo(
                    current = next,
                    target = target,
                    graph = graph,
                    requiredBits = requiredBits,
                    currentMask = newMask,
                    targetMask = targetMask,
                    visited = visited,
                    memo = memo,
                    canReachOut = canReachOut,
                    reverseReachForRequired = reverseReachForRequired
                )
                total = total.add(sub)
            }
        }

        visited -= current
        memo[key] = total
        return total
    }
}
