package day18

import util.printTimedOutput
import java.io.File
import kotlin.math.max
import kotlin.math.min

data class Coordinate(
    val x: Long,
    val y: Long,
    val z: Long,
)

fun loadVoxels(filename: String): Set<Coordinate> {
    return File(filename).readLines().map {
        val (x, y, z) = it.split(',').map{ it.toLong()}
        Coordinate(x, y, z)
    }.toSet()
}
fun difference(a: Long, b: Long): Long =
    max(a, b) - min(a, b)

fun puzzle1(filename: String): Long {
    val voxels = loadVoxels(filename)

    return calculateSurfaceArea(voxels)
}

private fun calculateSurfaceArea(voxels: Set<Coordinate>): Long {
    return voxels.map { voxel ->
        6L - setOf(
            Coordinate(voxel.x, voxel.y, voxel.z + 1),
            Coordinate(voxel.x, voxel.y, voxel.z - 1),
            Coordinate(voxel.x, voxel.y + 1, voxel.z),
            Coordinate(voxel.x, voxel.y - 1, voxel.z),
            Coordinate(voxel.x + 1, voxel.y, voxel.z),
            Coordinate(voxel.x - 1, voxel.y, voxel.z),
        ).filter {
            voxels.contains(it)
        }.count()
    }.sum()
}

fun fill(coordinate: Coordinate, mold: MutableSet<Coordinate>, droplet: Set<Coordinate>) {
    val toVisit: MutableSet<Coordinate> = mutableSetOf(coordinate)

    while (toVisit.isNotEmpty()) {
        val fill = toVisit.first()
        toVisit.remove(fill)
        mold.add(fill)

        toVisit.addAll(
            listOf(
                Coordinate(fill.x, fill.y, fill.z + 1),
                Coordinate(fill.x, fill.y, fill.z - 1),
                Coordinate(fill.x, fill.y + 1, fill.z),
                Coordinate(fill.x, fill.y - 1, fill.z),
                Coordinate(fill.x + 1, fill.y, fill.z),
                Coordinate(fill.x - 1, fill.y, fill.z),
            ).filterNot { mold.contains(it) || droplet.contains(it) }
                .filter {
                    (it.x >= 0 && it.x <= droplet.maxOf { it.x } + 1) &&
                    (it.y >= 0 && it.y <= droplet.maxOf { it.y } + 1) &&
                    (it.z >= 0 && it.z <= droplet.maxOf { it.z } + 1)
                }
        )
    }
}

fun puzzle2(filename: String): Long {
    val rawVoxels = loadVoxels(filename)
    val mold: MutableSet<Coordinate> = mutableSetOf()

    fill(Coordinate(0, 0, 0), mold, rawVoxels)
    val voxels = invertVoxels(mold)

    return calculateSurfaceArea(voxels)
}

fun invertVoxels(mold: MutableSet<Coordinate>): Set<Coordinate> {
    val result: MutableSet<Coordinate> = mutableSetOf()

    for (x in mold.minOf { it.x }..mold.maxOf { it.x }) {
        for (y in mold.minOf { it.y }..mold.maxOf { it.y }) {
            for (z in mold.minOf { it.z }..mold.maxOf { it.z }) {
                if (!mold.contains(Coordinate(x, y, z))) {
                    result.add(Coordinate(x, y, z))
                }
            }
        }
    }

    return result
}

fun main() {
    printTimedOutput("Puzzle 1 test") { puzzle1("input/day18-test.txt") }
    printTimedOutput("Puzzle 1     ") { puzzle1("input/day18.txt") }
    printTimedOutput("Puzzle 2 test") { puzzle2("input/day18-test.txt") }
    printTimedOutput("Puzzle 2     ") { puzzle2("input/day18.txt") }
}