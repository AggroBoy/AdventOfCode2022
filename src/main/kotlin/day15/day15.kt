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

    fun lineTo(other: Coordinate): List<Coordinate> {
        // Hacky - only works for precisely diagonal lines, but that's all that's needed
        val distance = abs(x - other.x)
        val xStep = if (other.x > x) 1 else -1
        val yStep = if (other.y > y) 1 else -1
        return (0..distance).map {
            Coordinate(x + (it * xStep), y + (it * yStep))
        }
    }
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

private fun puzzle2(fileName: String): Long {
    val sensors = parseInput(fileName)

    for (sensor in sensors) {
        val left = Coordinate(sensor.position.x - sensor.distanceToClosestBeacon - 1, sensor.position.y)
        val top = Coordinate(sensor.position.x, sensor.position.y - sensor.distanceToClosestBeacon - 1)
        val right = Coordinate(sensor.position.x + sensor.distanceToClosestBeacon + 1, sensor.position.y)
        val bottom = Coordinate(sensor.position.x, sensor.position.y + sensor.distanceToClosestBeacon + 1)


        val toConsider = listOf(
            left.lineTo(top),
            top.lineTo(right),
            right.lineTo(bottom),
            bottom.lineTo(left),
        )
            .flatten()
            .filter {
                it.x >= 0 && it.x <= 4000000 && it.y >= 0 && it.y <= 4000000
            }

        for (position in toConsider) {
            if ( sensors.none { it.position.distanceFrom(position) <= it.distanceToClosestBeacon } ) {
                return (position.x.toLong() * 4000000L) + position.y.toLong()
            }
        }
    }

    return 0
}


fun main(args: Array<String>) {
    println("Puzzle 1 (test): ${puzzle1("input/day15-test.txt", 10)}")
    println("Puzzle 1: ${puzzle1("input/day15.txt", 2000000)}")
    println("Puzzle 2: ${puzzle2("input/day15.txt")}")
}
