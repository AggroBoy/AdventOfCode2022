package day06

import java.io.File

private fun findUniqueSequence(length: Int): Int {
    val buffer = File("input/day6.txt").readLines().first()
    val packetbuffer = Array<Char>(length, { buffer[0] })

    buffer.forEachIndexed { i, c ->
        packetbuffer[i % length] = c
        if (packetbuffer.toSet().size == length) {
            return i + 1
        }
    }

    return 0
}

fun puzzle1(): Int {
    return findUniqueSequence(4)
}

fun puzzle2(): Int {
    return findUniqueSequence(14)
}

fun main(args: Array<String>) {
    println("Puzzle 1: ${puzzle1()}")
    println("Puzzle 2: ${puzzle2()}")
}