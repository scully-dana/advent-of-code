import java.io.File
import java.math.BigInteger
import java.util.ArrayDeque

fun main() {
    val input = readInput("day_7")

    println("Part 1 = ${solvePart1(input)}")
    println("Part 2 = ${solvePart2(input)}")
}

fun solvePart1(grid: List<String>): Int {
    val rowCount = grid.size
    val colCount = grid[0].length

    val start = grid.indices
        .firstNotNullOf { r ->
            grid[r].indexOf('S').takeIf { it != -1 }?.let { r to it }
        }

    val visitedCells = HashSet<Pair<Int, Int>>()
    val queue = ArrayDeque<Pair<Int, Int>>().apply { add(start) }

    var splitCount = 0

    while (queue.isNotEmpty()) {
        var (row, col) = queue.removeFirst()

        while (++row < rowCount) {
            val cell = row to col

            if (!visitedCells.add(cell)) break

            if (grid[row][col] == '^') {
                splitCount++
                if (col > 0) queue += (row to col - 1)
                if (col + 1 < colCount) queue += (row to col + 1)
                break
            }
        }
    }

    return splitCount
}

fun solvePart2(grid: List<String>): BigInteger {
    val rowCount = grid.size
    val colCount = grid[0].length

    val start = grid.indices
        .firstNotNullOf { r ->
            grid[r].indexOf('S').takeIf { it != -1 }?.let { r to it }
        }

    val memo = HashMap<Pair<Int, Int>, BigInteger>()

    fun countTimelinesFrom(position: Pair<Int, Int>): BigInteger =
        memo.getOrPut(position) {
            var (row, col) = position

            while (++row < rowCount) {
                if (grid[row][col] == '^') {
                    var total = BigInteger.ZERO
                    if (col > 0) total += countTimelinesFrom(row to col - 1)
                    if (col + 1 < colCount) total += countTimelinesFrom(row to col + 1)
                    return@getOrPut total
                }
            }

            BigInteger.ONE
        }

    return countTimelinesFrom(start)
}
