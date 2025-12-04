class Day04 {
    fun part1(input: String): Int =
        input.lines().filter { it.isNotEmpty() }
            .map { it.toCharArray() }
            .let { grid ->
                val h = grid.size
                val w = grid[0].size
                val neighbors = listOf(-1 to -1, 0 to -1, 1 to -1, -1 to 0, 1 to 0, -1 to 1, 0 to 1, 1 to 1)
                (0 until h).flatMap { y -> (0 until w).map { x -> x to y } }
                    .count { (x, y) ->
                        grid[y][x] == '@' &&
                                neighbors.count { (dx, dy) ->
                                    val nx = x + dx
                                    val ny = y + dy
                                    nx in 0 until w && ny in 0 until h && grid[ny][nx] == '@'
                                } < 4
                    }
            }

    fun part2(input: String): Int {
        data class P(val x: Int, val y: Int)

        val g = input
            .lineSequence()
            .filter { it.isNotEmpty() }
            .map { it.toCharArray() }
            .toList()

        val h = g.size
        val w = g[0].size

        val dirs = listOf(
            P(-1, -1), P(0, -1), P(1, -1),
            P(-1, 0), P(1, 0),
            P(-1, 1), P(0, 1), P(1, 1)
        )

        fun P.neigh() = dirs.map { P(x + it.x, y + it.y) }
            .filter { it.x in 0 until w && it.y in 0 until h }

        val deg = Array(h) { IntArray(w) }
        val q = ArrayDeque<P>()

        (0 until h).asSequence().flatMap { y ->
            (0 until w).asSequence().map { x -> P(x, y) }
        }.forEach { p ->
            if (g[p.y][p.x] == '@') {
                deg[p.y][p.x] = p.neigh().count { (nx, ny) -> g[ny][nx] == '@' }
                if (deg[p.y][p.x] < 4) q += p
            }
        }

        var removed = 0

        while (q.isNotEmpty()) {
            val p = q.removeFirst()
            if (g[p.y][p.x] != '@' || deg[p.y][p.x] >= 4) continue

            g[p.y][p.x] = '.'
            removed++

            p.neigh().forEach { n ->
                if (g[n.y][n.x] == '@' && --deg[n.y][n.x] == 3)
                    q += n
            }
        }

        return removed
    }
}

fun main() {
    val input = readInputAsString("day_4")
    println("Part 1: ${Day04().part1(input)}")
    println("Part 2: ${Day04().part2(input)}")
}