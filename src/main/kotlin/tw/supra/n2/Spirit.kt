package tw.supra.w2

import kotlin.properties.Delegates
import kotlin.random.Random

class Spirit(nature: Nature, name: String = ('a'..'z').random().toString()) : Looping {
    override var inertness = 1000L
    val nature = nature
    var name: String
    var energy = Random.nextInt(5)
    var loopCount = 0
    var x = Random.nextInt(nature.space.w, nature.space.e)

    var y = Random.nextInt(nature.space.s, nature.space.n)
    init {
        this.name = name
        energy = Random.nextInt(5)
        nature.space.reg(this)
    }

    override fun onLoop() {
        println("${toString()}: hasDied[${hasDied()}] loop[${++loopCount}]")
        if(!hasDied() && (--energy) < 0){
            dead()
        }
    }

    fun hasDied() = inertness < 0

    private fun dead() {
        inertness = -1
        nature.space.del(this)
    }

}