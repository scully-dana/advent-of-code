data class Point(val x: Int, val y: Int, val z: Int)

class UnionFind(count: Int) {
    private val parent = IntArray(count) { it }
    private val size = IntArray(count) { 1 }
    var components = count
        private set

    fun find(a: Int): Int =
        if (parent[a] == a) a else find(parent[a]).also { parent[a] = it }

    fun union(a: Int, b: Int): Boolean {
        var rootA = find(a)
        var rootB = find(b)
        if (rootA == rootB) return false

        if (size[rootA] < size[rootB]) rootA = rootB.also { rootB = rootA }
        parent[rootB] = rootA
        size[rootA] += size[rootB]
        components--
        return true
    }

    fun rootSizes(): List<Int> =
        size.filterIndexed { index, _ -> parent[index] == index }
}

private fun sq(v: Int) = v.toLong() * v

val points = readInput("day_8")
    .filter { it.isNotBlank() }
    .map { line ->
        val (x, y, z) = line.split(",").map(String::toInt)
        Point(x, y, z)
    }

val n = points.size

lateinit var uf1: UnionFind
lateinit var uf2: UnionFind
var lastMergeA = 0
var lastMergeB = 0

fun solution() {
    val edges = buildList {
        for (i in 0 until n)
            for (j in i + 1 until n) {
                val a = points[i]
                val b = points[j]
                val distSq = sq(a.x - b.x) + sq(a.y - b.y) + sq(a.z - b.z)
                add(Triple(distSq, i, j))
            }
    }.sortedBy { it.first }

    uf1 = UnionFind(n)
    var attemptedEdges = 0

    for ((_, a, b) in edges) {
        attemptedEdges++
        uf1.union(a, b)
        if (attemptedEdges == 1000) break
    }

    uf2 = UnionFind(n)

    for ((_, a, b) in edges) {
        if (uf2.union(a, b)) {
            lastMergeA = a
            lastMergeB = b
            if (uf2.components == 1) break
        }
    }
}

fun main() {
    solution()

    val part1 = uf1.rootSizes()
        .sortedDescending()
        .take(3)
        .fold(1L) { acc, size -> acc * size }

    val part2 = points[lastMergeA].x.toLong() * points[lastMergeB].x

    println("Part 1 = $part1")
    println("Part 2 = $part2")
}
