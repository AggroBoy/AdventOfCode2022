package day14.puzzle2

import java.io.File
import java.lang.Integer.max
import java.lang.Integer.min

private data class Coordinate(
    val x: Int,
    val y: Int,
) {
    val down get() = this.copy(y = y + 1)
    val left get() = this.copy(x = x - 1)
    val right get() = this.copy(x = x + 1)
}

private fun String.toCoordinate(): Coordinate {
    val (x, y) = this.split(",").map { it.toInt() }
    return Coordinate(x = x, y = y)
}

private val world:MutableMap<Coordinate, Char> = mutableMapOf()
private var floorLevel = 0

private fun loadWorld(filename: String) {
    File(filename).readLines().forEach {
        val coords = ArrayDeque(it.split(Regex(" -> ")).map{ it. toCoordinate()  })

        var position = coords.removeFirst()
        while (coords.isNotEmpty()) {
            floorLevel = max(position.y + 2, floorLevel)
            drawLine(position, coords.first())
            position = coords.removeFirst()
        }
    }
}

private fun drawLine(start: Coordinate, end: Coordinate) {
    val xMin = min(start.x, end.x)
    val xMax = max(start.x, end.x)
    val yMin = min(start.y, end.y)
    val yMax = max(start.y, end.y)

    if (xMin == xMax) {
        for (y in yMin..yMax) {
            world[Coordinate(xMin, y)] = '#'
        }
    } else {
        for (x in xMin..xMax) {
            world[Coordinate(x, yMin)] = '#'
        }
    }
}

private fun emptySpace(position: Coordinate) = position.y != floorLevel && world.get(position) == null

private fun findRestingPlace(entry: Coordinate): Coordinate {
    var currentPos = entry

    while (true) {
        currentPos = when {
            emptySpace(currentPos.down) -> currentPos.down
            emptySpace(currentPos.down.left) -> currentPos.down.left
            emptySpace(currentPos.down.right) -> currentPos.down.right
            else -> return currentPos
        }
    }
}

private fun puzzle2(): Int {
    loadWorld("input/day14.txt")
    val entry = Coordinate(500, 0)

    var count = 0

    while (true) {
        val finalPosition = findRestingPlace(entry)
        world[finalPosition] = 'o'
        count++
        if (entry == finalPosition) {
            return count
        }
    }
}

fun main(args: Array<String>) {
    println("Puzzle 2: ${puzzle2()}")
}
