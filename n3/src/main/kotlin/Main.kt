package tw.supra.n2

import java.util.*
import kotlin.math.abs
import kotlin.random.Random
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel


fun main() = runBlocking {
    println("Hello World!")
//    println("${Double.MAX_VALUE}")
//    println("${Double.MAX_VALUE.toInt()}")
//    return

    val pattern = { input: Double -> 2 * input + 3 }
    val input = 1.0
    val output = pattern(input)
    val inputGenerator = { input }
    val outputLoss = { a: Double, b: Double -> ((a - b) / (a + b)) }
    val inputToNeurons = { input: Double, inputNeurons: Array<InputNeuron> ->
        inputNeurons.forEach {
            it.impulse(input)
        }
    }
    val outputFromNeurons = { outputNeurons: Array<OutputNeuron> ->
        outputNeurons[0].outputs.values.sortedBy { outputLoss(it, pattern(it)) }[0]
    }
    val machine = Machine(
        10, 5.0,
        onInput = inputGenerator,
        onOutput = {
            println("outPut $it")
        },
        inputToNeurons,
        outputFromNeurons
    )

    learn(
        inputGenerator,
        outputLoss,
        pattern,
        machine
    )
}

fun <inpuT, outpuT> learn(
    inputGenerator: () -> inpuT,
    outputLoss: (outpuT, outpuT) -> Double,
    pattern: (inpuT) -> outpuT,
    machine: Machine<inpuT, outpuT>
) {
}

interface MachineContext {
    val scope: CoroutineScope
}

class Machine<inpuT, outpuT>(
    neuronCount: Int,
    spaceRange: Double,
    onInput: () -> inpuT,
    onOutput: (Array<outpuT>) -> Unit,
    inputToNeurons: (inpuT, Array<InputNeuron>) -> Unit,
    outputFromNeurons: (Array<OutputNeuron>) -> outpuT,
    override val scope: CoroutineScope = CoroutineScope(Dispatchers.Default)
) : MachineContext {


    val inputNeurons = arrayOf(InputNeuron(this, 0.0))
    val outputNeurons = arrayOf(OutputNeuron(this, spaceRange))

    val network = Network(this, neuronCount, spaceRange, inputNeurons, outputNeurons)

    fun start() {
        TODO()
        next()
    }

    fun stop() {
        TODO()
    }

    fun next() {}
    fun nextWithLoss(loss: Double) {
        TODO()
        next()
    }
}

/** 神经元网络 */
class Network(
    machineContext: MachineContext,
    neuronCount: Int,
    val spaceRange: Double,
    inputNeurons: Array<InputNeuron>,
    outputNeurons: Array<OutputNeuron>
) : MachineContext by machineContext {

    /** 神经元 */
    val neurons: SortedMap<Double, Neuron> = DoubleArray(neuronCount) { Random.nextDouble(spaceRange) }.toSortedSet()
        .associateWithTo(sortedMapOf()) { Neuron(this, it) }.apply {
            inputNeurons.forEach { put(it.position, it) }
            outputNeurons.forEach { put(it.position, it) }
        }

    /** 查询给定点和范围内神经元 */
    fun queryNeurons(axonTerminalPosition: Double, influenceRange: Double): List<Pair<Double, Neuron>> {
        val fromKey = axonTerminalPosition - influenceRange
        val toKey = axonTerminalPosition + influenceRange
        return neurons.subMap(fromKey, toKey).values.map {
            Pair<Double, Neuron>(abs(it.position - axonTerminalPosition), it)
        }.sortedBy { it.first }
    }
}

open class Neuron(
    val machineContext: MachineContext,
    open val position: Double,
    open val dendritesConnectionCapacity: Int = 100,
    var axonLength: Double = 0.0,
    val axonInfluence: Double = 1.0
) : MachineContext by machineContext {


    private val connectionsBy = mutableSetOf<Connection>()

    private val connectionsTo = mutableSetOf<Connection>()


    /** 兴奋激活偏差 */
    private var excitationBias: Double = 0.0

    /** 活性 */
    private var activity: Double = 0.0

    /** 树突兴奋累计 */
    private var dendritesExcitation: Double = 0.0

    /** 树突刺激 */
    protected open fun dendritesStimulate(connection: Connection, excitationStrength: Double) {
        dendritesExcitation += excitationStrength
    }

    private fun active() {

    }

    /**
     * 被链接
     */
    fun connectFrom(from: Neuron): Connection? =
        Connection(from).takeIf { connectionsBy.size > dendritesConnectionCapacity }?.also { connectionsBy.add(it) }


    /** 冲动 */
    fun impulse(strength: Double) {
    }

    /** 连接到神经元 */
    open fun connectTo(to: Neuron) {
        if (axonInfluence > 0.0) {
            TODO()
        }
    }

    /** 链接 */
    inner class Connection(from: Neuron) {
        val channel = Channel<Double>(Channel.UNLIMITED)
        /** 权重 */
        var weight: Double = 0.0

        /** 耦合强度 */
        var couplingStrength: Double = 0.0

        /** 链接强度 */
        val connectionStrength = weight * couplingStrength

        /** 冲动 */
        fun impulse(strength: Double) {
            dendritesStimulate(this, weight * strength)
        }
    }

}

class InputNeuron(
    machineContext: MachineContext,
    override val position: Double
) :
    Neuron(machineContext, position, 0) {

    fun input() {
//            impulse()
    }
}


class OutputNeuron(
    machineContext: MachineContext,
    override val position: Double
) : Neuron(machineContext, position, axonInfluence = 0.0) {
    val outputs = mutableMapOf<Connection, Double>()

    override fun connectTo(to: Neuron) {}
    override fun dendritesStimulate(connection: Connection, excitationStrength: Double) {
        outputs.put(connection, excitationStrength)
    }
}

