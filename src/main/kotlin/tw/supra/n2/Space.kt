package tw.supra.w2

import kotlin.math.abs

class Space(nature: Nature) : Looping {
    override var inertness = 100L
    val nature = nature
    val e = nature.maxAbsoluteDistance
    val n = nature.maxAbsoluteDistance
    val w = -e
    val s = -n
    val c = 0
    val cycleDistance = nature.maxAbsoluteDistance * 2 + 1
    val indicesX by lazy { mutableMapOf<Int, MutableMap<Int, Spirit>>() }
    val indicesY by lazy { mutableMapOf<Int, MutableMap<Int, Spirit>>() }

    fun requireIndex(dimension: Dimension, location: Int): MutableMap<Int, Spirit> {
        val indices = when (dimension) {
            Dimension.X -> indicesX
            Dimension.Y -> indicesY
        }
        var index = indices[location]
        if (null == index) {
            index = mutableMapOf()
            indices[location] = index
        }
        return index
    }

    fun index(spirit: Spirit) {
        requireIndex(Dimension.X, spirit.x)[spirit.y] = spirit
        requireIndex(Dimension.Y, spirit.y)[spirit.x] = spirit
    }

    fun indexRemove(spirit: Spirit) {
        requireIndex(Dimension.X, spirit.x).remove(spirit.y)
        requireIndex(Dimension.Y, spirit.y).remove(spirit.x)
    }

    val reportors: MutableSet<Space.() -> Unit> by lazy { mutableSetOf(DEFAULT_REPORTOR) }

    val spirits = mutableSetOf<Spirit>()

    override fun onLoop() {
        report()
    }

    fun reg(spirit: Spirit) {
        synchronized(spirits) {
            spirits.add(spirit)
            index(spirit)
        }
    }

    fun del(spirit: Spirit) {
        synchronized(spirits) {
            spirits.remove(spirit)
            indexRemove(spirit)
            if (spirits.isEmpty()) {
                inertness = -1
            }
        }
    }

    fun toMonoArt(): String {
        //          println("  +${" — ".repeat(cycleDistance)}+")

        val bodyLineBodyIndex: String by lazy {
            var line = ""
            for (x in w..e) {
                line += " ${abs(x)} "
            }
            line
        }

        var monoArt = ""
        monoArt += "\n  -${bodyLineBodyIndex}+"
        monoArt += "\n+  ${" N ".repeat(cycleDistance)}  +"
        monoArt += "${buildMonoArtBody()}"
        monoArt += "\n-  ${" S ".repeat(cycleDistance)}  -"
        monoArt += "\n  -${bodyLineBodyIndex}+"
        return monoArt
    }

    fun buildMonoArtBodyLineBody(y: Int): String {
        val index = requireIndex(Dimension.Y, y)
        var line = ""
        for (x in w..e) {
            val spirit = index[x]
            line += " ${when {
                null != spirit -> spirit.name
                c == x && c == y -> "+"
                c == y -> "—"
                c == x -> "|"
                else -> "."
            }} "
        }
        return line
    }

    fun buildMonoArtBody(): String {
        var body = ""
        for (y in n downTo s) {
            val absY = abs(y)
            body += "\n$absY W${buildMonoArtBodyLineBody(y)}E $absY"
        }
        return body
    }

    private fun report() {
        reportors.forEach({
            println("report by $it:")
            it.invoke(this)
        })
    }

    companion object {
        val DEFAULT_REPORTOR: Space.() -> Unit = {
            println(toMonoArt())
        }
    }

    enum class Dimension {
        X, Y
    }

}