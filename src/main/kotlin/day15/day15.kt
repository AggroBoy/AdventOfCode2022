package day15

import java.io.File
import java.io.StringReader
import java.lang.IllegalArgumentException
import java.lang.Integer.max
import java.lang.Integer.min
import kotlin.math.abs

private data class Coordinate(
    val x: Int,
    val y: Int,
) {
    fun distanceFrom(other: Coordinate): Int =
        abs(x - other.x) + abs(y - other.y)
}

private data class Sensor(
    val position: Coordinate,
    val closestBeacon: Coordinate,
) {
    val distanceToClosestBeacon get(): Int = position.distanceFrom(closestBeacon)
}

private fun parseInput(fileName: String): List<Sensor> {
    val regex = Regex("Sensor at x=(-?\\d+), y=(-?\\d+): closest beacon is at x=(-?\\d+), y=(-?\\d+)")
    return File(fileName).readLines().map {
        val (sensorX, sensorY, beaconX, beaconY) = regex.matchEntire(it)?.groupValues?.subList(1, 5)?.map { it.toInt() } ?: throw IllegalArgumentException()

        Sensor(position = Coordinate(sensorX, sensorY), closestBeacon = Coordinate(beaconX, beaconY))
    }
}

private fun puzzle1(fileName: String, row: Int): Int {
    val sensors = parseInput(fileName)

    val xMin = sensors.map { it.position.x - it.distanceToClosestBeacon }.min()
    val xMax = sensors.map { it.position.x + it.distanceToClosestBeacon }.max()

    var impossible = 0

    for (x in xMin..xMax) {
        val test = Coordinate(x, row)
        if (sensors.any { it.closestBeacon != test && it.position.distanceFrom(test) <= it.distanceToClosestBeacon } ) {
            impossible++
        }
    }

    return impossible
}

private fun puzzle2(): Int {
    return 0
}


fun main(args: Array<String>) {
    println("Puzzle 1 (test): ${puzzle1("input/day15-test.txt", 10)}")
    println("Puzzle 1: ${puzzle1("input/day15.txt", 2000000)}")
    println("Puzzle 2: ${puzzle2()}")
}
