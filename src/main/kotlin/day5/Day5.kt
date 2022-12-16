package day5

import java.io.File

fun puzzle1(): String {
    val regex = Regex("move (\\d+) from (\\d+) to (\\d+)")

    val lines = File("input/day5.txt").readLines()
    val initial = lines.subList(0, lines.indexOf("") - 1).reversed()
    val instructions = lines.subList(lines.indexOf("") + 1, lines.size)

    val stacks = prepareStacks(initial)

    for (instruction in instructions) {
        val result = regex.matchEntire(instruction) ?: continue
        val size = result.groupValues.get(1).toInt()
        val source = stacks[result.groupValues[2].toInt()] ?: continue
        val target = stacks[result.groupValues[3].toInt()] ?: continue

        repeat(size) {
            target.addLast(source.removeLast() ?: ' ')
        }
    }

    return stacks.toSortedMap().map {
        it.value.last()
    }.joinToString(separator = "")
}

private fun prepareStacks(initial: List<String>): HashMap<Int, ArrayDeque<Char>> {
    val stackCount = (initial.first().length / 4) + 1
    val stacks = HashMap<Int, ArrayDeque<Char>>()

    for (i in 1..stackCount) {
        stacks[i] = ArrayDeque()
    }

    for (row in initial) {
        for (col in 0..stackCount - 1) {
            val crate = row[col * 4 + 1]
            if (crate != ' ') {
                stacks[col + 1]?.addLast(crate)
            }
        }
    }

    return stacks
}


fun puzzle2(): String {
    val regex = Regex("move (\\d+) from (\\d+) to (\\d+)")

    val lines = File("input/day5.txt").readLines()
    val initial = lines.subList(0, lines.indexOf("") - 1).reversed()
    val instructions = lines.subList(lines.indexOf("") + 1, lines.size)

    val stacks = prepareStacks(initial)

    for (instruction in instructions) {
        val result = regex.matchEntire(instruction) ?: continue
        val number = result.groupValues.get(1).toInt()
        val source = stacks[result.groupValues[2].toInt()] ?: continue
        val target = stacks[result.groupValues[3].toInt()] ?: continue

        val addAt = target.size
        repeat(number) {
            target.add(addAt, source.removeLast() ?: ' ')
        }
    }

    return stacks.toSortedMap().map {
        it.value.last()
    }.joinToString(separator = "")
}

fun main(args: Array<String>) {
    println("Puzzle 1: ${puzzle1()}")
    println("Puzzle 2: ${puzzle2()}")
}