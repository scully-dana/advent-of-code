fun main() {
    fun part1(input: List<String>): Int {
        var dialAt = 50
        var zeros = 0
        
        for (line in input) {
            if (line.isBlank()) continue
            val direction = line[0]
            val distance = line.substring(1).trim().toInt()

            dialAt = when (direction) {
                'L' -> (dialAt - distance).mod(100)
                'R' -> (dialAt + distance).mod(100)
                else -> error("Invalid: $direction")
            }
            if (dialAt == 0) zeros++
        }

        return zeros
    }

    fun part2(input: List<String>): Int {
        var dialAt = 50
        var zeros = 0
        
        fun norm(a: Int): Int = ((a % 100) + 100) % 100
        
        for (line in input) {
            if (line.isBlank()) continue
            val direction = line[0]
            val distance = line.substring(1).trim().toInt()
            val firstK = when (direction) {
                'L' -> {
                    var k = dialAt % 100
                    if (k == 0) k = 100
                    k
                }
                'R' -> {
                    var k = (100 - dialAt) % 100
                    if (k == 0) k = 100
                    k
                }
                else -> error("Invalid: $direction")
            }

            if (distance >= firstK) {
                zeros += ( (distance - firstK) / 100 ) + 1
            }

            dialAt = when (direction) {
                'R' -> norm(dialAt + distance)
                'L' -> norm(dialAt - distance)
                else -> dialAt
            }
        }
        return zeros
    }

    // Read the input from the `src/Day01.txt` file.
    val input = readInput("day_1")
    part1(input).println()
    part2(input).println()
}
