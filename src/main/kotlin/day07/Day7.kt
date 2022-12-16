package day07

import java.io.File

val dirs: MutableList<String> = mutableListOf("/")

private data class Command(
    val command: String,
    val output: MutableList<String>
)


private fun parseInput(input: List<String>): List<Command> {
    val commands = mutableListOf<Command>()
    input.forEach {
        if (it.startsWith("$")) {
            commands.add(Command(it.substring(2), mutableListOf()))
        } else {
            commands.last().output.add(it)
        }
    }

    return commands
}

private fun buildFileList(commands: List<Command>): Map<String, Int> {
    val files: MutableMap<String, Int> = mutableMapOf()
    var cwd: String = "/"

    for (command in commands) {
        if (command.command == "ls") {
            for (line in command.output) {
                val (size, name) = line.split(' ')
                if (size == "dir") {
                    dirs.add(cwd + name + "/")
                } else {
                    files[cwd + name] = size.toInt()
                }
            }

        } else if (command.command.startsWith("cd")) {
            val (_, dir) = command.command.split(' ')
            if (dir == "/") {
                cwd = "/"

            } else if (dir == "..") {
                cwd = cwd.substring(0, cwd.lastIndexOf('/', startIndex = cwd.lastIndex - 1) + 1)

            } else {
                cwd += dir + "/"
            }
        }
    }

    return files.toMap()
}

fun puzzle1(): Int {
    val lines = File("input/day7.txt").readLines()

    val commands = parseInput(lines)
    val files = buildFileList(commands)

    return dirs.map { dir ->
        files.filter { file ->
            file.key.startsWith(dir)
        }.values.sum()
    }.filter { it <= 100000}.sum()
}

fun puzzle2(): Int {
    val lines = File("input/day7.txt").readLines()

    val commands = parseInput(lines)
    val files = buildFileList(commands)

    val dir_sizes = dirs.map { dir ->
        dir to files.filter { file ->
            file.key.startsWith(dir)
        }.values.sum()
    }.toMap()

    val usedSpace = dir_sizes["/"] ?: 0
    val needToFree = 30000000 - (70000000 - usedSpace)
    return dir_sizes.values.sorted().find { it > needToFree } ?: 0
}

fun main(args: Array<String>) {
    println("Puzzle 1: ${puzzle1()}")
    println("Puzzle 2: ${puzzle2()}")
}