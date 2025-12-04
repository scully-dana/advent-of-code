import java.math.BigInteger

private fun String.toBI() = BigInteger(this)
private fun pow10(n: Int) = BigInteger.TEN.pow(n)

private fun inRange(x: BigInteger, lo: BigInteger, hi: BigInteger) =
    x >= lo && x <= hi

private fun parseRanges(line: String): List<Pair<BigInteger, BigInteger>> =
    line.split(",")
        .map { it.trim() }
        .filter { it.isNotEmpty() }
        .mapNotNull {
            val parts = it.split("-").map(String::trim)
            if (parts.size != 2) null else Pair(parts[0].toBI(), parts[1].toBI())
        }

// ---------- Part 1: sequence repeated exactly twice (k = 2) ----------
fun part1(line: String): BigInteger {
    var sum = BigInteger.ZERO
    val ranges = parseRanges(line)

    for ((lo, hi) in ranges) {
        val maxHalf = hi.toString().length / 2
        for (halfLen in 1..maxHalf) {
            val start = pow10(halfLen - 1)
            val end = pow10(halfLen).subtract(BigInteger.ONE)

            var x = start
            while (x <= end) {
                val repeated = (x.toString() + x.toString()).toBI()
                if (repeated > hi) break
                if (repeated >= lo) sum = sum.add(repeated)
                x = x.add(BigInteger.ONE)
            }
        }
    }
    return sum
}

// ---------- Part 2: sequence repeated k >= 2 times ----------
fun part2(line: String): BigInteger {
    var sum = BigInteger.ZERO
    val ranges = parseRanges(line)

    for ((lo, hi) in ranges) {
        val hiLen = hi.toString().length
        val seen = hashSetOf<String>()

        val maxSeqLen = hiLen / 2
        for (seqLen in 1..maxSeqLen) {
            val start = pow10(seqLen - 1)
            val end = pow10(seqLen).subtract(BigInteger.ONE)
            var x = start
            while (x <= end) {
                val base = x.toString()
                
                val maxK = hiLen / seqLen
                for (k in 2..maxK) {
                    val s = base.repeat(k)
                    if (s.length > hiLen) break
                    val value = s.toBI()
                    if (value > hi) break
                    if (value < lo) continue
                    if (seen.add(s)) sum = sum.add(value)
                }
                x = x.add(BigInteger.ONE)
            }
        }
    }
    return sum
}

fun main() {
    val inp = readInputAsString("day_2").lineSequence().firstOrNull { it.isNotBlank() }?.trim()
    if (inp.isNullOrBlank()) {
        System.err.println("No input found for day_2")
        return
    }

    println("Part 1: ${part1(inp)}")
    println("Part 2: ${part2(inp)}")
}
