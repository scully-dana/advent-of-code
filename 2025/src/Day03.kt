val input = readInput("day_3")
    .filterNot { it.isBlank() }

fun String.maxTwoDigit(): Int {
    var best = 0
    var maxRight = -1
    this.reversed().forEach { ch ->
        val d = ch - '0'
        if (maxRight >= 0) best = maxOf(best, d * 10 + maxRight)
        maxRight = maxOf(maxRight, d)
    }
    return best
}

val part1 = input
    .sumOf { it.maxTwoDigit().toLong() }

fun String.pick12() =
    (0 until 12).fold(Pair(0, StringBuilder())) { (start, out), chosen ->
        val end = length - (12 - chosen)
        (start..end).maxBy { this[it] }.let { idx ->
            idx + 1 to out.append(this[idx])
        }
    }.second.toString()

val part2 = input
    .sumOf { it.pick12().toLong() }

fun main() {
    println("Part 1: $part1")
    println("Part 2: $part2")
}