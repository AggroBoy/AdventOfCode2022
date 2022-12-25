package util

import kotlin.time.ExperimentalTime
import kotlin.time.TimeSource


@OptIn(ExperimentalTime::class)
fun <T>printTimedOutput(title: String, lambda: () -> T) {
    val timeStamp = TimeSource.Monotonic.markNow()
    val result = lambda()
    println("%-20s %10s".format("$title : $result", "(${timeStamp.elapsedNow().inWholeMilliseconds}ms)"))
}
