package day11

import java.io.File
import java.lang.IllegalArgumentException

data class Monkey(
    val id: Int,
    val items: MutableList<Long>,
    val operation: String,
    val testDivisibleBy: Long,
    val trueTargetMonkey: Int,
    val falseTargetMonkey: Int,
    var inspections: Long = 0,
)

val MONKEY_NUMBER_REGEX = Regex("day11.Monkey (\\d+):")
val STARTING_ITEMS_REGEX = Regex("\\s*Starting items: (.*)")
val OPERATION_REGEX = Regex("\\s*Operation: new = (.*)")
val DIVISIBLE_BY_REGEX = Regex("\\s*Test: divisible by (\\d+)")
val TRUE_TARGET_REGEX = Regex("\\s*If true: throw to monkey (\\d+)")
val FALSE_TARGET_REGEX = Regex("\\s*If false: throw to monkey (\\d+)")

private fun parseMonkey(input: List<String>): Monkey {
    return Monkey(
        id = MONKEY_NUMBER_REGEX.matchEntire(input[0])?.groupValues?.get(1)?.toInt() ?: 0,
        items = STARTING_ITEMS_REGEX.matchEntire(input[1])?.groupValues?.get(1)?.split(", ")?.map { it.toLong() }?.toMutableList() ?: mutableListOf(),
        operation = OPERATION_REGEX.matchEntire(input[2])?.groupValues?.get(1) ?: "",
        testDivisibleBy = DIVISIBLE_BY_REGEX.matchEntire(input[3])?.groupValues?.get(1)?.toLong() ?: 0,
        trueTargetMonkey = TRUE_TARGET_REGEX.matchEntire(input[4])?.groupValues?.get(1)?.toInt() ?: 0,
        falseTargetMonkey = FALSE_TARGET_REGEX.matchEntire(input[5])?.groupValues?.get(1)?.toInt() ?: 0,
    )
}

private fun puzzle1(): Long {
    val lines = File("input/day11.txt").readLines()

    val monkies = lines.filter { it.isNotBlank() }
        .chunked(6)
        .map { parseMonkey(it) }

    for (round in 0 until 20) {
        for (monkey in monkies) {
            while (monkey.items.isNotEmpty()) {
                var item = monkey.items.removeFirst()
                item = applyOperation(item, monkey.operation)
                monkey.inspections++
                item = item / 3
                if (item % monkey.testDivisibleBy == 0L) {
                    monkies[monkey.trueTargetMonkey].items.add(item)
                } else {
                    monkies[monkey.falseTargetMonkey].items.add(item)
                }
            }
        }
    }

    val sortedMonkies = monkies
        .sortedByDescending { it.inspections }
    return sortedMonkies[0].inspections * sortedMonkies[1].inspections
}

private fun applyOperation(item: Long, operation: String): Long {
    val(one, op, two) = operation.replace("old", item.toString()).split(' ')
    val valOne = one.toLong()
    val valTwo = two.toLong()

    return when(op) {
        "+" -> valOne + valTwo
        "*" -> valOne * valTwo
        else -> throw IllegalArgumentException()
    }
}

private fun puzzle2(): Long {
    val lines = File("input/day11.txt").readLines()

    val monkies = lines.filter { it.isNotBlank() }
        .chunked(6)
        .map { parseMonkey(it) }

    val neededWorryAccuracy = monkies.map { it.testDivisibleBy }.reduce { acc, i -> acc * i }

    for (round in 0 until 10000) {
        for (monkey in monkies) {
            while (monkey.items.isNotEmpty()) {
                var item = monkey.items.removeFirst()
                item = applyOperation(item, monkey.operation) % neededWorryAccuracy
                monkey.inspections++
                if (item % monkey.testDivisibleBy == 0L) {
                    monkies[monkey.trueTargetMonkey].items.add(item)
                } else {
                    monkies[monkey.falseTargetMonkey].items.add(item)
                }
            }
        }
    }

    val sortedMonkies = monkies
        .sortedByDescending { it.inspections }
    return sortedMonkies[0].inspections * sortedMonkies[1].inspections

}

fun main(args: Array<String>) {
    println("Puzzle 1: ${puzzle1()}")
    println("Puzzle 2: ${puzzle2()}")
}
