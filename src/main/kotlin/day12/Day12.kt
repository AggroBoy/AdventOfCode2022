package day12

import java.io.File

private data class Coordinate(
    val x: Int,
    val y: Int,
) {
    val north get() = Coordinate(x = x, y = y - 1)
    val east  get() = Coordinate(x = x + 1, y = y)
    val south get() = Coordinate(x = x, y = y + 1)
    val west  get() = Coordinate(x = x - 1, y = y)
}

private data class Position(
    val coordinate: Coordinate,
    val height: Char,
)

private interface Problem {
    var startPosition: Position
    fun getPossibleSteps(position: Position): List<Position>
    fun positionWins(position: Position): Boolean
}

private class Problem1(val fileName: String): Problem {
    val heightMap: Map<Coordinate, Char> = buildHeightMap()
    override lateinit var startPosition: Position
    lateinit var targetPosition: Position

    private fun buildHeightMap(): Map<Coordinate, Char> {
        val lines = File(fileName).readLines()

        val map = lines.mapIndexed { y, line ->
            line.mapIndexed { x, char ->
                val position = Position(coordinate = Coordinate(x, y), height = char)
                Coordinate(x, y) to char
            }
        }
            .flatten()
            .toMap().toMutableMap()

        map.entries.find { it.value == 'S' }?.let {
            startPosition = Position(coordinate = it.key, height = 'a')
            map[it.key] = 'a'
        }
        map.entries.find { it.value == 'E' }?.let {
            targetPosition = Position(coordinate = it.key, height = 'z')
            map[it.key] = 'z'
        }

        return map
    }

    override fun getPossibleSteps(position: Position): List<Position> {
        val posibleCoordinates = listOf(
            position.coordinate.north,
            position.coordinate.south,
            position.coordinate.east,
            position.coordinate.west,
        )

        val possiblePositions = posibleCoordinates.mapNotNull{
            val height = heightMap.get(it)
            if (height == null)
                null
            else
                Position(coordinate = it, height = height)
        }

        return possiblePositions.filter { (it.height <= position.height + 1) }
    }

    override fun positionWins(position: Position): Boolean {
        return position == targetPosition
    }
}

private class Problem2(val fileName: String): Problem {
    val heightMap: Map<Coordinate, Char> = buildHeightMap()
    override lateinit var startPosition: Position

    private fun buildHeightMap(): Map<Coordinate, Char> {
        val lines = File(fileName).readLines()

        val map = lines.mapIndexed { y, line ->
            line.mapIndexed { x, char ->
                val position = Position(coordinate = Coordinate(x, y), height = char)
                Coordinate(x, y) to char
            }
        }
            .flatten()
            .toMap().toMutableMap()

        map.entries.find { it.value == 'E' }?.let {
            startPosition = Position(coordinate = it.key, height = 'z')
            map[it.key] = 'z'
        }

        return map
    }

    override fun getPossibleSteps(position: Position): List<Position> {
        val posibleCoordinates = listOf(
            position.coordinate.north,
            position.coordinate.south,
            position.coordinate.east,
            position.coordinate.west,
        )

        val possiblePositions = posibleCoordinates.mapNotNull{
            val height = heightMap.get(it)
            if (height == null)
                null
            else
                Position(coordinate = it, height = height)
        }

        return possiblePositions.filter { (it.height >= position.height - 1) }
    }

    override fun positionWins(position: Position): Boolean {
        return position.height == 'a'
    }
}

private class Solver(val map: Problem) {
    val alreadyConsidered: MutableSet<Position> = mutableSetOf(map.startPosition)
    var solution: List<Position>? = null

    private class Node(val parent: Node?, val position: Position)

    fun solve(): List<Position> {
        val root = Node(parent = null, position = map.startPosition)
        return recursiveWalk(listOf(root)).map { it.position }
    }

    private fun recursiveWalk(parentNodes: List<Node>): List<Node> {
        val newNodes = parentNodes.map { parent ->
            map.getPossibleSteps(parent.position).map {
                position -> Node(parent=parent, position = position)
            }
        }
            .flatten()
            .filter { firstTimeSeen(it.position) }

        for (node in newNodes) {
            if (map.positionWins(node.position)) {
                return solutionFromWinningNode(node)
            }
        }

        if (newNodes.isEmpty()) {
            return emptyList()
        }

        return recursiveWalk(newNodes)
    }

    private fun firstTimeSeen(position: Position): Boolean {
        val result = !alreadyConsidered.contains(position)
        alreadyConsidered.add(position)
        return result
    }

    private fun solutionFromWinningNode(node: Node): MutableList<Node> {
        val result = mutableListOf<Node>()
        var walkNode = node
        while (true) {
            result.add(walkNode)
            val parent = walkNode.parent
            if (parent == null)
                return result.asReversed()
            walkNode = parent
        }
    }
}

private fun puzzle1(): Int {
    return Solver(Problem1("input/Day12.txt")).solve().size - 1
}

private fun puzzle2(): Int {
    return Solver(Problem2("input/Day12.txt")).solve().reversed().size - 1
}

fun main(args: Array<String>) {
    println("Puzzle 1: ${puzzle1()}")
    println("Puzzle 2: ${puzzle2()}")
}
