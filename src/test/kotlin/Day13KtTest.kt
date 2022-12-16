import day13.parsePacket
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

class Day13KtTest {

    @Test
    fun parseList() {
        assertEquals(
            emptyList<Any>(),
            parsePacket("[]")
        )

        assertEquals(
            listOf<Any>(2, 5, 7),
            parsePacket("[2,5,7]")
        )

        assertEquals(
            listOf<Any>(listOf(2, 2), 5, 7),
            parsePacket("[[2,2],5,7]")
        )

        assertEquals(
            listOf<Any>(listOf(2, 2), listOf(5), listOf(7, listOf( 6, 4, 2))),
            parsePacket("[[2,2],[5],[7,[6,4,2]]]")
        )

        assertEquals(
            listOf<Any>(listOf(2, 2), listOf(5), emptyList<Any>(), listOf(7, listOf( 6, emptyList<Any>(), 4, 2))),
            parsePacket("[[2,2],[5],[],[7,[6,[],4,2]]]")
        )

    }
}