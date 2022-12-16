package day10

import java.io.File
import java.lang.IllegalArgumentException
import kotlin.math.absoluteValue

private data class Instruction(
    val command: String,
    val argument: Int?,
    val cycles: Int,
)

private fun parseInstruction(instruction: String): Instruction {
    return if (instruction.startsWith("addx")) {
        val (c, a) = instruction.split(' ')
        Instruction(command = c, argument = a.toInt(), cycles = 2)
    } else {
        Instruction("noop", null, cycles = 1)
    }
}

private fun loadInstructions(): ArrayDeque<Instruction> {
    val lines = File("input/day10.txt").readLines()
    return ArrayDeque(lines.map {
        parseInstruction(it)
    })
}

private fun puzzle1(): Int {
    val instructions = loadInstructions()

    var cycle = 1
    var currentInstruction: Instruction? = null
    var remainingCycles: Int = 0
    var registerX = 1
    var output = 0

    while (true) {
        if (currentInstruction == null) {
            if (instructions.isEmpty())
                break
            currentInstruction = instructions.removeFirst()
            remainingCycles = currentInstruction.cycles
        }

        if (isInterestingCycle(cycle)) {
            output += registerX * cycle
        }

        remainingCycles--
        cycle++

        if (remainingCycles == 0) {
            if (currentInstruction.command == "addx") {
                registerX += currentInstruction.argument ?: throw IllegalArgumentException()
            }
            currentInstruction = null
        }
    }

    return output
}

private fun isInterestingCycle(cycle: Int): Boolean {
    if (cycle == 20) return true
    if ((cycle - 20) % 40 == 0) return true
    return false
}

private fun puzzle2(): Int {
    val instructions = loadInstructions()

    var cycle = 1
    var currentInstruction: Instruction? = null
    var remainingCycles: Int = 0
    var registerX = 1
    var output = 0

    while (true) {
        if (currentInstruction == null) {
            if (instructions.isEmpty())
                break
            currentInstruction = instructions.removeFirst()
            remainingCycles = currentInstruction.cycles
        }

        val x = ((cycle - 1) % 40)
        if ((x - registerX).absoluteValue < 2) {
            print('#')
        } else {
            print(' ')
        }

        if (cycle % 40 == 0) {
            println()
        }

        remainingCycles--
        cycle++

        if (remainingCycles == 0) {
            if (currentInstruction.command == "addx") {
                registerX += currentInstruction.argument ?: throw IllegalArgumentException()
            }
            currentInstruction = null
        }
    }

    return output
}

fun main(args: Array<String>) {
    println("Puzzle 1: ${puzzle1()}")
    println("Puzzle 2: ${puzzle2()}")
}
