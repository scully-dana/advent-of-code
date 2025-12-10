import java.io.File
import java.util.ArrayDeque
import kotlin.system.exitProcess

data class Machine(
    val lightsPattern: String,         // e.g. "..##."
    val buttonsAsSets: List<Set<Int>>, // list of sets of indices each button toggles / increments
    val joltageTargets: IntArray       // target counters for part2
)

fun parseLine(line: String): Machine {
    // Line format: [PATTERN] (a,b,...) (c,...) ... {n,n,...}
    val patternRegex = "\\[([^\\]]*)\\]".toRegex()
    val parenRegex = "\\(([^)]*)\\)".toRegex()
    val curlRegex = "\\{([^}]*)\\}".toRegex()

    val pat = patternRegex.find(line)?.groups?.get(1)?.value
        ?: throw IllegalArgumentException("No pattern in line: $line")
    val parenMatches = parenRegex.findAll(line).map { it.groups[1]!!.value.trim() }.toList()
    val curl = curlRegex.find(line)?.groups?.get(1)?.value
        ?: throw IllegalArgumentException("No curly braces in line: $line")

    val buttons = parenMatches.map { s ->
        if (s.isEmpty()) emptySet()
        else s.split(',').map { it.trim().toInt() }.toSet()
    }

    val jolts = curl.split(',').map { it.trim().toInt() }.toIntArray()

    return Machine(pat, buttons, jolts)
}

/** Part 1: compute minimal number of button presses to match the light pattern.
 *  Each button toggles listed lights; pressing twice cancels -> each button pressed 0/1 times only.
 *  We'll represent light states as bitmask (LSB = index 0).
 */
fun solvePart1(machine: Machine): Int {
    val nButtons = machine.buttonsAsSets.size
    // build bitmask of each button
    val buttonMasks = IntArray(nButtons)
    for (i in 0 until nButtons) {
        var m = 0
        for (idx in machine.buttonsAsSets[i]) {
            require(idx >= 0) { "negative index" }
            m = m or (1 shl idx)
        }
        buttonMasks[i] = m
    }
    // target mask from lightsPattern: pattern string is e.g. "[..##.]" but we stored inner already
    val pattern = machine.lightsPattern
    var targetMask = 0
    // pattern index 0 -> light 0, etc. We set bit idx if char == '#'
    for ((idx, ch) in pattern.withIndex()) {
        if (ch == '#') targetMask = targetMask or (1 shl idx)
    }

    // enumerate subsets of buttons (2^nButtons). For each subset compute xor of buttonMasks,
    // track minimal popcount achieving target mask.
    var best = Int.MAX_VALUE
    val total = 1 shl nButtons
    for (mask in 0 until total) {
        var cur = 0
        var cnt = 0
        var m = mask
        var bi = 0
        while (m != 0) {
            if ((m and 1) == 1) {
                cur = cur xor buttonMasks[bi]
                cnt++
            }
            bi++
            m = m ushr 1
            // small optimization: if cnt >= best break
            if (cnt >= best) break
        }
        if (cur == targetMask) {
            if (cnt < best) best = cnt
        }
    }
    if (best == Int.MAX_VALUE) {
        // If no subset can produce target, problem statement implies always possible, but handle gracefully:
        return Int.MAX_VALUE
    }
    return best
}

/** Part 2: BFS on counter vectors.
 *  Start from 0-vector. Each button press adds a 1 to specified indices.
 *  We explore states (vectors) with coordinates bounded by target vector.
 *  BFS level == total presses. Return minimal presses to reach target.
 */
fun solvePart2(machine: Machine): Int {
    val targets = machine.joltageTargets
    val dims = targets.size
    val buttonsVecs = machine.buttonsAsSets.map { set ->
        IntArray(dims) { i -> if (i in set) 1 else 0 }
    }
    // Quick check: if any target index has all buttons not affecting it but target > 0 => impossible
    for (i in 0 until dims) {
        var affects = 0
        for (b in buttonsVecs) if (b[i] != 0) affects++
        if (affects == 0 && targets[i] != 0) return Int.MAX_VALUE
    }

    // represent state as IntArray dims; but for hashing in visited set we use string or packed key.
    fun pack(state: IntArray): String {
        // small pack: join with ','
        return state.joinToString(",")
    }

    val start = IntArray(dims) { 0 }
    val q = ArrayDeque<Pair<IntArray, Int>>()
    q.add(start.copyOf() to 0)
    val seen = HashSet<String>()
    seen.add(pack(start))
    val targetKey = pack(targets)
    while (q.isNotEmpty()) {
        val (curState, steps) = q.removeFirst()
        if (pack(curState) == targetKey) return steps
        // try pressing each button once (one more press)
        for (b in buttonsVecs) {
            val next = curState.copyOf()
            var ok = true
            for (i in 0 until dims) {
                next[i] += b[i]
                if (next[i] > targets[i]) { ok = false; break } // prune overshoot
            }
            if (!ok) continue
            val key = pack(next)
            if (key !in seen) {
                seen.add(key)
                q.add(next to steps + 1)
            }
        }
    }
    return Int.MAX_VALUE // unreachable
}

fun main() {
    val lines = readInput("day_10").map { it.trim() }.filter { it.isNotEmpty() }
    val machines = lines.map { parseLine(it) }

    // For part1 we need to pass the pattern into Machine; parseLine stored it already.
    // Compute part1 sum and part2 sum
    var p1Sum = 0L
    var p2Sum = 0L
    for (m in machines) {
        val p1 = solvePart1(m)
        if (p1 == Int.MAX_VALUE) {
            System.err.println("Machine cannot be configured (part1) for pattern: ${m.lightsPattern}")
            // treat as 0 or error — choose to exit
            exitProcess(3)
        }
        p1Sum += p1.toLong()

        val p2 = solvePart2(m)
        if (p2 == Int.MAX_VALUE) {
            System.err.println("Machine cannot be configured (part2) for joltage targets: ${m.joltageTargets.joinToString(",")}")
            exitProcess(4)
        }
        p2Sum += p2.toLong()
    }

    println(p1Sum)
    println(p2Sum)
}