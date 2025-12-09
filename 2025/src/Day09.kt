import java.io.File
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

data class Point(val x: Int, val y: Int)
data class Segment(val a: Point, val b: Point)

fun main() {
    val points = readInput("day_9")
        .filter { it.isNotBlank() }
        .map { line ->
            val (x, y) = line.split(",").map(String::toInt)
            Point(x, y)
        }

    println("Part 1 = ${largestRectanglePart1(points)}")
    println("Part 2 = ${largestRectanglePart2(points)}")
}

fun largestRectanglePart1(points: List<Point>): Long {
    var maxArea = 0L

    for (i in 0 until points.size) {
        val p1 = points[i]
        for (j in i + 1 until points.size) {
            val p2 = points[j]

            val width = abs(p1.x - p2.x) + 1L
            val height = abs(p1.y - p2.y) + 1L
            val area = width * height

            if (area > maxArea) maxArea = area
        }
    }

    return maxArea
}

fun largestRectanglePart2(points: List<Point>): Long {
    val boundary = buildBoundarySegments(points)
    val rectangleCandidates = generateRectangleCandidates(points)

    var bestArea = 0L

    for (candidate in rectangleCandidates) {
        if (candidate.area <= bestArea) break

        val cornerA = points[candidate.i]
        val cornerB = points[candidate.j]

        val xmin = min(cornerA.x, cornerB.x)
        val xmax = max(cornerA.x, cornerB.x)
        val ymin = min(cornerA.y, cornerB.y)
        val ymax = max(cornerA.y, cornerB.y)

        val centerX = xmin + 0.5
        val centerY = ymin + 0.5
        if (!isInsidePolygon(centerX, centerY, boundary)) continue

        if (boundaryIntersectsInterior(boundary, xmin, xmax, ymin, ymax)) continue

        bestArea = candidate.area
        break
    }

    return bestArea
}

fun buildBoundarySegments(points: List<Point>): List<Segment> =
    points.indices.map { i ->
        val a = points[i]
        val b = points[(i + 1) % points.size]
        Segment(a, b)
    }

data class RectangleCandidate(val area: Long, val i: Int, val j: Int)

fun generateRectangleCandidates(points: List<Point>): List<RectangleCandidate> {
    val candidates = ArrayList<RectangleCandidate>()

    for (i in points.indices) {
        for (j in i + 1 until points.size) {
            val p1 = points[i]
            val p2 = points[j]
            val area = (abs(p1.x - p2.x) + 1L) * (abs(p1.y - p2.y) + 1L)
            candidates.add(RectangleCandidate(area, i, j))
        }
    }
    return candidates.sortedByDescending { it.area }
}

fun isInsidePolygon(px: Double, py: Double, edges: List<Segment>): Boolean {
    var inside = false

    for (edge in edges) {
        val (x1, y1) = edge.a
        val (x2, y2) = edge.b

        if (py == y1.toDouble() && py == y2.toDouble()) {
            if (px in min(x1, x2).toDouble()..max(x1, x2).toDouble()) return true
        }

        val ymin = min(y1, y2)
        val ymax = max(y1, y2)

        if (py < ymin || py >= ymax) continue

        val intersectionX = x1.toDouble()

        if (px == intersectionX) return true

        if (intersectionX > px) inside = !inside
    }

    return inside
}

fun boundaryIntersectsInterior(
    edges: List<Segment>,
    xmin: Int, xmax: Int,
    ymin: Int, ymax: Int
): Boolean {
    for (seg in edges) {
        val (x1, y1) = seg.a
        val (x2, y2) = seg.b

        if (y1 == y2) {
            val y = y1
            if (y in (ymin + 1) until ymax) {
                val left = min(x1, x2)
                val right = max(x1, x2)
                if (left < xmax && right > xmin) return true
            }
        } else {
            val x = x1
            if (x in (xmin + 1) until xmax) {
                val bottom = min(y1, y2)
                val top = max(y1, y2)
                if (bottom < ymax && top > ymin) return true
            }
        }
    }
    return false
}
