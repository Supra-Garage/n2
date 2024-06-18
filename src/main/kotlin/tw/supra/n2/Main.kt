package tw.supra.w2

fun main() {
//    println(Pair(1, 2) == Pair(1, 2))
    Nature(9).start()
}

class Position<DimensionType>(dimensions: Set<String>) {
val dimensions :LinkedHashSet<String> by lazy {  LinkedHashSet(dimensions.sorted())}
    val key by lazy { dimensions }
}

