package day14.puzzle1

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
private var yBound = 0

private fun loadWorld(filename: String) {
    File(filename).readLines().forEach {
        val coords = ArrayDeque(it.split(Regex(" -> ")).map{ it.toCoordinate() })

        var position = coords.removeFirst()
        while (coords.isNotEmpty()) {
            yBound = max(position.y, yBound)
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

private fun withinBounds(position: Coordinate) = position.y < yBound

private fun findRestingPlace(entry: Coordinate): Coordinate? {
    var currentPos = entry

    while (withinBounds(currentPos)) {
        currentPos = when {
            world.get(currentPos.down) == null -> currentPos.down
            world.get(currentPos.down.left) == null -> currentPos.down.left
            world.get(currentPos.down.right) == null -> currentPos.down.right
            else -> return currentPos
        }
    }

    return null
}

private fun puzzle1(): Int {
    loadWorld("input/day14.txt")
    val entry = Coordinate(500, 0)

    var count = 0

    while (true) {
        val finalPosition = findRestingPlace(entry)
        if (finalPosition != null) {
            world[finalPosition] = 'o'
            count++
        } else {
            return count
        }
    }
}

fun main(args: Array<String>) {
    println("Puzzle 1: ${puzzle1()}")
}
