package day17

import util.printTimedOutput
import java.io.File
import kotlin.math.min

data class Coordinate(
    val x: Long,
    val y: Long,
)

typealias Rock = List<Coordinate>

fun Rock.height(): Long = this.maxOf { it.y } - this.minOf { it.y }
fun Rock.width(): Long = this.maxOf { it.x } - this.minOf { it.x }

typealias World = MutableMap<Coordinate, Char>

fun World.highestPoint(): Long = if (this.isNotEmpty()) this.keys.maxOf{ it.y } else 0

fun World.shapeFitsAtPosition(rock: Rock, position: Coordinate): Boolean {
    for (point in rock) {
        val absolutePoint = Coordinate(position.x + point.x, position.y + point.y)
        if (absolutePoint.x < 0 || absolutePoint.x > 6) {
            return false
        }
        if (absolutePoint.y <= 0) {
            return false
        }
        if (this[absolutePoint] != null) {
            return false
        }
    }
    return true
}

fun World.addRockAtPosition(rock: Rock, position: Coordinate) {
    for (point in rock) {
        val absolutePoint = Coordinate(position.x + point.x, position.y + point.y)
        this[absolutePoint] = '#'
    }
}

fun World.trim() {
    val columnMaxes = (0L..6L)
        .mapNotNull{ x ->
            this.keys
                .filter { it.x == x }
                .maxOfOrNull { it.y }
        }

    if (columnMaxes.size == 7) {
        val cutoff = min(columnMaxes.min(), highestPoint() - 20L)
        this.keys.filter { it.y < cutoff }.forEach { this.remove(it) }
    }
}

fun World.state(): String {
    val highestPoint = highestPoint()
    return (highestPoint-19..highestPoint).reversed().map { y ->
        (0L..6).map { x ->
            this[Coordinate(x, y)] ?: ' '
        }.joinToString("")
    }.joinToString("\n")
}

fun createWorld(): World = mutableMapOf()

class rockProducer(
    val shapes: Array<Rock> = arrayOf(
        listOf(Coordinate(0, 0), Coordinate(1, 0), Coordinate(2, 0), Coordinate(3, 0)),
        listOf(Coordinate(1, 2), Coordinate(0, 1), Coordinate(1, 1), Coordinate(2, 1), Coordinate(1, 0)),
        listOf(Coordinate(2, 2), Coordinate(2, 1), Coordinate(0, 0), Coordinate(1, 0), Coordinate(2, 0)),
        listOf(Coordinate(0, 3), Coordinate(0, 2), Coordinate(0, 1), Coordinate(0, 0)),
        listOf(Coordinate(0, 1), Coordinate(1, 1), Coordinate(0, 0), Coordinate(1, 0)),
    ),
    var currentShape: Int = 0,
) {
    fun get(): Rock = shapes[currentShape].also { currentShape = (currentShape + 1) % shapes.size }
}

class JetProducer(
    val fileName: String,
    val jets: CharArray = loadJets(fileName),
    var currentJet: Int = 0
) {
    fun get(): Char = peek().also { currentJet = (currentJet + 1) % jets.size }
    fun peek(): Char = jets[currentJet]

    companion object {
        fun loadJets(fileName: String): CharArray {
            return File(fileName).readLines()[0].toCharArray()
        }
    }
}

fun solver(filename: String, rockCount: Long): Long {
    val rocks = rockProducer()
    val jets = JetProducer(filename)
    val world = createWorld()

    val states: MutableMap<String, Long> = mutableMapOf()
    val heights: MutableMap<Long, Long> = mutableMapOf()

    for (rockNumber in 0 until rockCount) {
        val shape = rocks.get()
        var position = Coordinate(2, world.highestPoint() + 4)

        // For the second puzzle, we can't actually iterate 1 trillion times; look for a repeating pattern, and once
        // it's found, we can solve the problem with maths rather than iteration. (multiply the height of that pattern
        // by the number of times it must occur, and add in the height reached before the pattern was detected and the
        // height of the last incomplete instance of the pattern).
        // Note that the "detection of a repeating pattern" here feels pretty weak (it's a case of have we seen this
        // state before? If so, assume that point in history until now is a repeating pattern.), but it does work for
        // my input. The jet direction doesn't seem to be needed, but I'm including because it feels more correct.
        heights[rockNumber] = world.highestPoint()
        val fullState = "${rocks.currentShape}-${jets.peek()}-${world.state()}"
        val previous = states[fullState]
        if (previous != null) {
            val heightBeforeLoop = heights.getValue(previous)
            val loopHeight = heights.getValue(rockNumber) - heightBeforeLoop
            val loopLength = rockNumber - previous
            val numberOfLoops = (rockCount - previous) / loopLength
            val lengthOfLastPartialLoop = (rockCount - previous) % loopLength
            val heightOfLastPartialLoop = heights.getValue(previous + lengthOfLastPartialLoop) - heights.getValue(previous)
            return heightBeforeLoop + (numberOfLoops * loopHeight) + heightOfLastPartialLoop
        } else {
            states[fullState] = rockNumber
        }

        while (true) {
            jets.get().let {
                val newPosition = when (it) {
                    '>' -> position.copy(x = position.x + 1)
                    '<' -> position.copy(x = position.x - 1)
                    else -> position
                }

                if (world.shapeFitsAtPosition(shape, newPosition)) {
                    position = newPosition
                }
            }
            val newPosition = position.copy(y = position.y - 1)
            if (world.shapeFitsAtPosition(shape, newPosition)) {
                position = newPosition
            } else {
                world.addRockAtPosition(shape, position)
                world.trim()
                break
            }
        }
    }

    // Well, we never found a repeating pattern, but we did drop out of the loop, so ... yay?
    return world.highestPoint()
}

fun main() {
    printTimedOutput("Puzzle 1 test") { solver("input/day17-test.txt", 2022) }
    printTimedOutput("Puzzle 1     ") { solver("input/day17.txt", 2022) }
    printTimedOutput("Puzzle 2 test") { solver("input/day17-test.txt", 1000000000000) }
    printTimedOutput("Puzzle 2     ") { solver("input/day17.txt", 1000000000000) }
}
