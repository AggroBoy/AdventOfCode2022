package day13

import java.io.File
import java.io.StringReader
import java.lang.IllegalArgumentException
import java.lang.Integer.min


private fun puzzle1(): Int {
    val pairs = File("input/day13.txt")
        .readLines()
        .chunked(3)
        .map { it.subList(0, 2)}

    var result = 0

    pairs.forEachIndexed { i, pair ->
        val packet1 = parsePacket(pair[0])
        val packet2 = parsePacket(pair[1])

        if (compareLists(packet1, packet2) <= 0) {
            result += i + 1
        }
    }

    return result
}

private fun compareLists(one: List<Any>, two: List<Any>): Int {
    for (i in 0..min(one.lastIndex, two.lastIndex)) {
        val elementOne = one[i]
        val elementTwo = two[i]
        if (elementOne is Int && elementTwo is Int) {
            if (elementOne < elementTwo) {
                return -1
            } else if (elementTwo < elementOne) {
                return 1
            }
        } else if (elementOne is Int) {
            val c = compareLists(listOf(elementOne), elementTwo as List<Any>)
            if (c != 0) return c
        } else if (elementTwo is Int) {
            val c = compareLists(elementOne as List<Any>, listOf( elementTwo))
            if (c != 0) return c
        } else {
            val c = compareLists(elementOne as List<Any>, elementTwo as List<Any>)
            if (c != 0) return c
        }
    }

    if (one.size < two.size) return -1
    if (two.size < one.size) return 1

    return 0
}

private fun MutableList<Any>.addAsListOrInt(item: Any) {
    if (item is String) {
        this.add(item.toInt())
    } else {
        this.add(item)
    }
}

fun parsePacket(packet: String): List<Any> {
    return parseList(packet.substring(1).reader())
}

fun parseList(reader: StringReader): List<Any> {
    val output = mutableListOf<Any>()
    var currentItem: Any? = null

    while (reader.ready())
    {
        val v = reader.read()
        if (v == -1)
            return output

        val char = v.toChar()
        when (char) {
            // start of a new list; recursively call this function and hang on to the returned list as the current item
            '[' -> {
                currentItem = parseList(reader)
            }

            // End of list, if we have an item add it and then return the whole current list
            ']' -> {
                if (currentItem != null)
                    output.addAsListOrInt(currentItem)
                return output
            }

            // It's a comma; currentItem is complete so add it to the list
            ',' -> {
                output.addAsListOrInt(currentItem ?: throw IllegalArgumentException())
                currentItem = null
            }

            // Else, it's a character, which we can use to build a string
            else -> {
                if (currentItem == null)
                    currentItem = "$char"
                else
                    currentItem = (currentItem as String) + char
            }
        }
    }

    return output
}

private fun puzzle2(): Int {
    val packets = File("input/day13.txt")
        .readLines()
        .chunked(3)
        .flatMap { it.subList(0, 2)}

    val dividerOne = listOf(listOf(2))
    val dividerTwo = listOf(listOf(6))
    val withDividers = packets.map { parsePacket(it) } + listOf(dividerOne, dividerTwo)

    val sorted = withDividers.sortedWith(Comparator {a, b ->
        compareLists(a, b)
    })

    return (sorted.indexOf(dividerOne) + 1) * (sorted.indexOf(dividerTwo) + 1)
}

fun main(args: Array<String>) {
    println("Puzzle 1: ${puzzle1()}")
    println("Puzzle 2: ${puzzle2()}")
}
