package day2

import java.io.File
import java.lang.IllegalArgumentException

fun scoreLine(line: String): Int = when(line) {
    "A X" -> 4
    "B X" -> 1
    "C X" -> 7

    "A Y" -> 8
    "B Y" -> 5
    "C Y" -> 2

    "A Z" -> 3
    "B Z" -> 9
    "C Z" -> 6
    else -> throw IllegalArgumentException()
}

fun scoreLine2(line: String): Int = when(line) {
    "A X" -> 3
    "B X" -> 1
    "C X" -> 2

    "A Y" -> 4
    "B Y" -> 5
    "C Y" -> 6

    "A Z" -> 8
    "B Z" -> 9
    "C Z" -> 7
    else -> throw IllegalArgumentException()
}


fun main(args: Array<String>) {
    var totalScore = 0
    var totalScore2 = 0
    File("input/day2.txt").forEachLine {
        totalScore += scoreLine(it)
        totalScore2 += scoreLine2(it)
    }

    println("Puzzle 1: $totalScore")
    println("Puzzle 2: $totalScore2")
}