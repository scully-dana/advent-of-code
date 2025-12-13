fun main() {
    val input = readInput("input2")

    var fit = 0

    for (line in input) {
        if ('x' in line) {
            val (dimensions, boxesPart) = line.split(":", limit = 2)
            val (w, h) = dimensions.split("x").map { it.toInt() }

            val area = (w / 3) * (h / 3)

            val totalBoxes = boxesPart.trim().split(Regex("\\s+")).sumOf { it.toInt() }

            if (totalBoxes <= area) {
                fit++
            }
        }
    }

    println(fit)
}
