package day16

import java.io.File
import java.lang.IllegalArgumentException

data class Valve(
    val name: String,
    val rate: Int,
    var tunnels: Set<String>,
    val distances: MutableMap<String, Int> = mutableMapOf(),
)

class World {
    val valveMap: MutableMap<String, Valve> = mutableMapOf()
    val valves: Set<Valve> get() = valveMap.values.toSet()

    override fun toString(): String {
        return "World: valves: $valveMap"
    }

    fun addValve(name: String, flowRate: Int, tunnels: Set<String>) {
        val valve = Valve(name = name, rate = flowRate, tunnels = tunnels)
        valveMap[name] = valve
    }

    fun propagateDistances(origin: Valve, tunnels: Set<String>, currentDistance: Int) {
        val next = tunnels
            .mapNotNull { valveMap.get(it) }
            .filter {
                val distance = it.distances.get(origin.name)
                it != origin && (distance == null || distance == 1 || distance > currentDistance)
            }

        next.forEach {
            origin.distances[it.name] = currentDistance
            it.distances[origin.name] = currentDistance
            propagateDistances(
                origin=origin,
                tunnels=it.tunnels,
                currentDistance = currentDistance + 1
            )
        }
    }

    fun fillInDistances(origin: Valve) {
        valves.filterNot { it == origin }.forEach {
            val distance = measureDistance(origin, it) ?: throw IllegalArgumentException()
            origin.distances[it.name] = distance
            it.distances[origin.name] = distance
        }
    }

    fun measureDistance(origin: Valve, target: Valve, currentDistance: Int = 0, visited: List<Valve> = emptyList()): Int? {
        if (origin.tunnels.contains(target.name)) {
            return currentDistance + 1
        }

        if (visited.contains(origin)) {
            return null
        }

        return origin.tunnels.mapNotNull {
            valveMap[it]
        }.mapNotNull {
            measureDistance(it, target, currentDistance + 1, visited + origin)
        }.minOrNull()
    }

    companion object {
        fun loadFromFile(filename: String): World {
            val regex = Regex("Valve (.*) has flow rate=(\\d+); tunnels? leads? to valves? (.*)")
            val world = World()

            for (line in File(filename).readLines()) {
                val matches = regex.matchEntire(line)?.groupValues ?: throw IllegalArgumentException()
                val name = matches[1]
                val flowRate = matches[2].toInt()
                val tunnels = matches[3].split(", ")

                world.addValve(name, flowRate, tunnels.toSet())
            }

            for (valve in world.valves) {
                world.fillInDistances(valve)
            }

            return world
        }
    }
}

fun walk(position: Valve, unopened: List<Valve>, open: List<Valve>, timeRemaining: Int, pressureReleased: Int): Int {
    return when {
        timeRemaining <= 0 -> pressureReleased
        unopened.none { it != position && it.rate > 0 } -> pressureReleased
        else ->
            unopened.filter { it != position }.map {
                val newRemaining = timeRemaining - (position.distances[it.name] ?: throw IllegalArgumentException()) - 1
                walk(
                    it,
                    unopened - it,
                    open + it,
                    newRemaining,
                    pressureReleased + (it.rate * newRemaining),
                )
            }.max()
    }
}

fun doubleWalk(
    position1: Valve,
    position2: Valve,
    unopened: List<Valve>,
    opened1: List<Valve>,
    opened2: List<Valve>,
    timeRemaining1: Int,
    timeRemaining2: Int,
    pressureReleased: Int)
: Int {
    return when {
        timeRemaining1 <= 1 || timeRemaining2 <= 1 -> pressureReleased
        unopened.none { it.rate > 0 } -> pressureReleased
        else -> {
            unopened.filter { !listOf(position1, position2).contains(it) }.map {
                if (timeRemaining1 >= timeRemaining2)
                    doubleWalk(
                        it,
                        position2,
                        unopened - it,
                        opened1 + it,
                        opened2,
                        timeRemaining1 - (position1.distances[it.name] ?: throw IllegalArgumentException()) - 1,
                        timeRemaining2,
                        pressureReleased + (it.rate * (timeRemaining1 - (position1.distances[it.name] ?: throw IllegalArgumentException()) - 1))
                    )
                else
                    doubleWalk(
                        position1,
                        it,
                        unopened - it,
                        opened1,
                        opened2 + it,
                        timeRemaining1,
                        timeRemaining2 - (position2.distances[it.name] ?: throw IllegalArgumentException()) - 1,
                        pressureReleased + (it.rate * (timeRemaining2 - (position2.distances[it.name] ?: throw IllegalArgumentException()) - 1))
                    )
            }.max()
        }
    }
}

fun puzzle1(filename: String): Int {
    val world = World.loadFromFile(filename)

    return walk(world.valveMap["AA"] ?: throw IllegalArgumentException(), world.valves.filter { it.rate > 0 }.toList(), emptyList(), 30, 0)
}

fun puzzle2(filename: String): Int {
    val world = World.loadFromFile(filename)

    return doubleWalk(
        world.valveMap["AA"] ?: throw IllegalArgumentException(),
        world.valveMap["AA"] ?: throw IllegalArgumentException(),
        world.valves.filter { it.rate > 0 }.toList(),
        emptyList(),
        emptyList(),
        26,
        26,
        0,
    )
}

fun main() {
    println("Puzzle 1: ${puzzle1("input/day16.txt")}")
    println("Puzzle 2: ${puzzle2("input/day16.txt")}")
}