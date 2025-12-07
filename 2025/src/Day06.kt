import java.math.BigInteger

fun main() {
    val lines = readInput("day_6")
    val numberLines = lines.dropLast(1)
    val operationsLine = lines.last()

    val maxWidth = lines.maxOf { it.length }
    val paddedNumbers = numberLines.map { it.padEnd(maxWidth) }
    val paddedOperations = operationsLine.padEnd(maxWidth)

    val isSeparatorColumn = (0 until maxWidth).map { columnIndex ->
        paddedNumbers.all { it[columnIndex] == ' ' } && paddedOperations[columnIndex] == ' '
    }

    val columnSpans = buildList {
        var currentColumn = 0
        while (currentColumn < maxWidth) {
            if (isSeparatorColumn[currentColumn]) {
                currentColumn++; continue
            }
            val startColumn = currentColumn
            while (currentColumn < maxWidth && !isSeparatorColumn[currentColumn]) currentColumn++
            add(startColumn until currentColumn)
        }
    }

    fun part1(): BigInteger =
        columnSpans.sumOf { span ->
            val operator = paddedOperations.substring(span).trim().firstOrNull() ?: '+'
            val numbers = paddedNumbers.map { it.substring(span).trim() }
                .filter { it.isNotEmpty() }
                .flatMap { it.split(Regex("\\s+")) }
                .map { BigInteger(it) }

            when (operator) {
                '+' -> numbers.fold(BigInteger.ZERO, BigInteger::add)
                '*' -> numbers.fold(BigInteger.ONE, BigInteger::multiply)
                else -> BigInteger.ZERO
            }
        }

    fun part2(): BigInteger =
        columnSpans.sumOf { span ->
            val operator = paddedOperations.substring(span).trim().firstOrNull() ?: '+'
            val verticalNumbers = span.map { columnIndex ->
                paddedNumbers.map { it[columnIndex] }
                    .joinToString("") { if (it.isDigit()) it.toString() else "" }
            }.filter { it.isNotEmpty() }
                .map { BigInteger(it) }

            when (operator) {
                '+' -> verticalNumbers.fold(BigInteger.ZERO, BigInteger::add)
                '*' -> verticalNumbers.fold(BigInteger.ONE, BigInteger::multiply)
                else -> BigInteger.ZERO
            }
        }

    println("Part 1 = ${part1()}")
    println("Part 2 = ${part2()}")
}