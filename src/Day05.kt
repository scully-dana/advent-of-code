import java.math.BigInteger

typealias R = Pair<BigInteger, BigInteger>

val p: (String) -> List<R> = { s ->
    s.lineSequence()
        .map { it.split("-").map(String::trim).map(::BigInteger) }
        .map { (a, b) -> a to b }.toList()
}

fun part1(i: String) = i.trim().split("\n\n").let { (r, ids) ->
    val rs = p(r)
    ids.lineSequence().filter { it.isNotBlank() }
        .map { BigInteger(it) }.count { x -> rs.any { (a, b) -> x in a..b } }
}

fun part2(i: String): BigInteger = p(i.substringBefore("\n\n"))
    .sortedBy { it.first }
    .fold(listOf<R>()) { a, (x, y) ->
        if (a.isEmpty()) listOf(x to y) else
            a.last().let { (u, v) ->
                if (x <= v + BigInteger.ONE) a.dropLast(1) + (u to maxOf(v, y))
                else a + (x to y)
            }
    }.fold(BigInteger.ZERO) { s, (x, y) -> s + (y - x + BigInteger.ONE) }


fun main() {
    val input = readInputAsString("day_5")
    println(part1(input))
    println(part2(input))
}