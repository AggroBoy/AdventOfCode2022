package day4

import java.io.File

fun puzzle1(): Int {
    val regex = Regex("(\\d+)-(\\d+),(\\d+)-(\\d+)")

    return File("input/day4.txt").readLines()
        .mapNotNull { regex.matchEntire(it) }
        .map {
            val bounds = it.groupValues
                .subList(1, it.groupValues.size)
                .map { bound -> bound.toInt() }

            when {
                (bounds[0] <= bounds[2] && bounds[3] <= bounds[1]) -> 1
                (bounds[2] <= bounds[0] && bounds[1] <= bounds[3]) -> 1
                else -> 0
            }
        }
        .sum()
}


fun puzzle2(): Int {
    val regex = Regex("(\\d+)-(\\d+),(\\d+)-(\\d+)")

    return File("input/day4.txt").readLines()
        .mapNotNull { regex.matchEntire(it) }
        .map {
            val bounds = it.groupValues
                .subList(1, it.groupValues.size)
                .map { bound -> bound.toInt() }

            when {
                (bounds[0] <= bounds[2] && bounds[2] <= bounds[1]) -> 1
                (bounds[0] <= bounds[3] && bounds[3] <= bounds[1]) -> 1
                (bounds[2] <= bounds[0] && bounds[0] <= bounds[3]) -> 1
                (bounds[2] <= bounds[1] && bounds[1] <= bounds[3]) -> 1
                else -> 0
            }
        }
        .sum()
}

fun main(args: Array<String>) {
    println("Puzzle 1: ${puzzle1()}")
    println("Puzzle 2: ${puzzle2()}")
}