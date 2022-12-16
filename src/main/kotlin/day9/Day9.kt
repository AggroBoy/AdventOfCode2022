package day9

import java.io.File
import kotlin.math.absoluteValue

private fun Int.oneCloserTo(other: Int): Int =
     this + (if (other > this) 1 else -1)

private fun Pair<Int, Int>.moveCloserTo(other: Pair<Int, Int>): Pair<Int, Int> {
    val delta = (other.first - this.first to other.second - this.second)
    if (delta.first.absoluteValue < 2 && delta.second.absoluteValue < 2) {
        return this
    } else if (delta.first == 0) {
        return (this.first to this.second.oneCloserTo(other.second))
    } else if (delta.second == 0) {
        return (this.first.oneCloserTo(other.first) to this.second)
    } else {
        return (this.first.oneCloserTo(other.first) to this.second.oneCloserTo(other.second))
    }
}

private fun loadMoves(): List<Pair<Int, Int>> {
    val lines = File("input/day9.txt").readLines()
    val testLines: List<String> = listOf(
        "R 4",
        "U 4",
        "L 3",
        "D 1",
        "R 4",
        "D 1",
        "L 5",
        "R 2"
    )
    return lines.flatMap {
        val (direction, distance) = it.split(' ')
        List(distance.toInt()) {
            when (direction) {
                "U" -> (0 to 1)
                "L" -> (-1 to 0)
                "R" -> (1 to 0)
                "D" -> (0 to -1)
                else -> (0 to 0)
            }
        }
    }
}

fun puzzle1(): Int {
    val visited: MutableSet<Pair<Int, Int>> = mutableSetOf()
    val moves = loadMoves()

    var h = (0 to 0)
    var t = (0 to 0)
    visited.add((0 to 0))

    for (move in moves) {
        h = (h.first + move.first to h.second + move.second)
        t = t.moveCloserTo(h)

        visited.add(t)
    }

    return visited.size
}

fun puzzle2(): Int {
    val visited: MutableSet<Pair<Int, Int>> = mutableSetOf()
    val moves = loadMoves()

    val rope = List(10) {(0 to 0)}.toMutableList()
    visited.add((0 to 0))

    for (move in moves) {
        rope[0] = (rope[0].first + move.first to rope[0].second + move.second)

        for (knot in 1..rope.lastIndex) {
             rope[knot] = rope[knot].moveCloserTo(rope[knot - 1])
        }

        visited.add(rope.last())
    }

    return visited.size
}

fun main(args: Array<String>) {
    println("Puzzle 1: ${puzzle1()}")
    println("Puzzle 2: ${puzzle2()}")
}