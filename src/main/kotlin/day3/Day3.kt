package day3

import java.io.File

val priorities = HashMap<Char, Int>()

fun findSinlgeCommonChar(input: Iterable<String>): Char =
    input.map{ it.toSet() }.reduce{ acc, it -> acc.intersect(it) }.first()

fun puzzle1() =
    File("input/day3.txt").readLines()
        .map { it.chunked(it.length / 2) }
        .mapNotNull { priorities[findSinlgeCommonChar(it)] }
        .sum()

fun puzzle2() =
    File("input/day3.txt").readLines()
        .chunked(3)
        .mapNotNull { priorities[findSinlgeCommonChar(it)] }
        .sum()

fun main(args: Array<String>) {
    "abcdefghijklmnopqrstuvwxyz".forEachIndexed{i, char -> priorities[char] = i+1}
    "ABCDEFGHIJKLMNOPQRSTUVWXYZ".forEachIndexed{i, char -> priorities[char] = i+27}

    println("Puzzle 1: ${puzzle1()}")
    println("Puzzle 2: ${puzzle2()}")
}