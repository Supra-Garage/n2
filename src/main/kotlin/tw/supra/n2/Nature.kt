package tw.supra.w2

import java.util.concurrent.*
import kotlin.math.abs

class Nature(maxDistance: Int) {
    val maxAbsoluteDistance = abs(maxDistance)
    val space = Space(this)
    val executorService by lazy { Executors.newScheduledThreadPool(1) }

    fun createSpirit(count: Int) {
        for (i in 0..count) {
            run(Spirit(this))
        }
    }

    fun start() {
        run(space)
        createSpirit(5)
    }

    private fun run(looping: Looping) {
        loop(looping, true)
    }

    private fun loop(looping: Looping, forcePost: Boolean = false) {
        synchronized(looping) {
            fun status() = "${Thread.currentThread()} for $looping of inertness[$looping.inertness}]"
            println("loop: check on ${status()}")
            if (looping.inertness < 0) {
                return@synchronized
            }

            if (!forcePost) {
                println("loop: exec on ${status()}")
                do {
                    looping.onLoop()
                } while (looping.inertness == 0L)
            } else if (looping.inertness == 0L) {
                println("loop: post on ${status()}")
                executorService.execute { loop(looping) }
            }

            if (looping.inertness > 0) {
                println("loop: post delay ${looping.inertness}ms on ${status()}")
                executorService.schedule({ loop(looping) }, looping.inertness, TimeUnit.MILLISECONDS)
            }
        }
    }
}