package day08

import java.io.File

fun puzzle1(): Int {
    var totalVisible = 0

    val grid = loadGrid()
    for (x in 0..grid[0].lastIndex) {
        for (y in 0..grid.lastIndex) {
            if (!isHidden(grid, x, y)) {
                totalVisible++
            }
        }
    }

    return totalVisible
}

private fun loadGrid(): List<List<Int>> {
    val lines = File("input/day8.txt").readLines()
    return lines.map { it.chunked(1).map { it.toInt() } }
}

fun isHiddenNorth(grid: List<List<Int>>, x: Int, y: Int): Boolean =
    grid.subList(0, y).any { it[x] >= grid[y][x]}

fun isHiddenSouth(grid: List<List<Int>>, x: Int, y: Int): Boolean =
    grid.subList(y+1, grid.size).any { it[x] >= grid[y][x]}

fun isHiddenWest(grid: List<List<Int>>, x: Int, y: Int): Boolean =
    grid[y].subList(0, x).any { it >= grid[y][x] }

fun isHiddenEast(grid: List<List<Int>>, x: Int, y: Int): Boolean =
    grid[y].subList(x+1, grid[y].size).any { it >= grid[y][x] }

fun isHidden(grid: List<List<Int>>, x: Int, y: Int): Boolean {
    val hiddenNorth = isHiddenNorth(grid, x, y)
    val hiddenSouth = isHiddenSouth(grid, x, y)
    val hiddenWest = isHiddenWest(grid, x, y)
    val hiddenEast = isHiddenEast(grid, x, y)

    return hiddenNorth && hiddenSouth && hiddenWest && hiddenEast
}

fun puzzle2(): Int {
    val grid = loadGrid()

    return (0..grid.lastIndex).map { y ->
        (0..grid[y].lastIndex).map { x ->
            calculateScenery(grid, x, y)
        }
    }.flatten().max()
}


fun calculateScenery(grid: List<List<Int>>, x: Int, y: Int): Int {
    val sceneryNorth = sceneryNorth(grid, x, y)
    val scenerySouth = scenerySouth(grid, x, y)
    val sceneryWest = sceneryWest(grid, x, y)
    val sceneryEast = sceneryEast(grid, x, y)
    return sceneryNorth * scenerySouth * sceneryWest * sceneryEast
}


fun sceneryNorth(grid: List<List<Int>>, x: Int, y: Int): Int {
    if (y == 0) return 0
    val blockedAt = grid.subList(0, y).indexOfLast { it[x] >= grid[y][x]}
    return when (blockedAt) {
        -1 -> y
        else -> y - blockedAt
    }
}

fun scenerySouth(grid: List<List<Int>>, x: Int, y: Int): Int {
    if (y == grid.lastIndex) return 0
    val blockedAt = grid.subList(y + 1, grid.size).indexOfFirst { it[x] >= grid[y][x]}
    return when (blockedAt) {
        -1 -> grid.lastIndex - y
        else -> blockedAt + 1
    }
}

fun sceneryWest(grid: List<List<Int>>, x: Int, y: Int): Int {
    if (x == 0) return 0
    val blockedAt = grid[y].subList(0, x).indexOfLast { it >= grid[y][x]}
    return when (blockedAt) {
        -1 -> x
        else -> x - blockedAt
    }
}

fun sceneryEast(grid: List<List<Int>>, x: Int, y: Int): Int {
    if (x == grid[y].lastIndex) return 0
    val blockedAt = grid[y].subList(x + 1, grid[y].size).indexOfFirst { it >= grid[y][x]}
    return when (blockedAt) {
        -1 -> grid[y].lastIndex - x
        else -> blockedAt + 1
    }
}

fun main(args: Array<String>) {
    println("Puzzle 1: ${puzzle1()}")
    println("Puzzle 2: ${puzzle2()}")
}