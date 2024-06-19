package tw.supra.n2

fun main() {
    println("Hello World!")
    learn(
        inputGenerator = { 0.1 },
        pattern = { 2 * it + 3 },
        outputLoss = { a, b -> ((a - b) / (a + b)) })x
}

fun <inpuT, outpuT> learn(
    inputGenerator: () -> inpuT,
    pattern: (inpuT) -> outpuT,
    outputLoss: (outpuT, outpuT) -> Double
) {
    Machine(
        onInput = inputGenerator
                outputLoss = {
            outputLoss
        }
    )
}

class Machine<inpuT, outpuT>(onInput: () -> inpuT, onOutput: (outpuT) -> Unit) {
    fun start() {
        TODO()
        next()
    }
    fun next() {}
    fun nextWithLoss(loss: Double) {
        TODO()
        next()
    }
}
