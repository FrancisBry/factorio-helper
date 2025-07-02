package factorio.helper.entity.graph

data class DirectedAcyclicVertex<T : Any>(val value: T, val edges: Collection<DirectedAcyclicVertex<T>>) {
    fun addEdges(edgesToAdd: Collection<DirectedAcyclicVertex<T>>): DirectedAcyclicVertex<T> {
        val newValues: Collection<T> = edgesToAdd.map { it.value }
        val newEdges: Collection<DirectedAcyclicVertex<T>> = edges.filter { !newValues.contains(it.value) } + edgesToAdd
        if (newEdges.any { it.isTraversableTo(value) }) {
            throw CyclicEdgeAddedException()
        }
        return DirectedAcyclicVertex(value, newEdges)
    }

    fun isTraversableTo(search: T): Boolean {
        return value == search || edges.any { it.isTraversableTo(search) }
    }

    fun getTraversableVertices(): Sequence<DirectedAcyclicVertex<T>> {
        return sequenceOf(this) + edges.fold(emptySequence()) { acc, edge -> acc + edge.getTraversableVertices() }
    }

    fun accumulateBreadthFirstSequence(accumulator: Sequence<T>): Sequence<T> {
        if (accumulator.contains(value)) {
            return accumulator
        }
        val edgeSequence: Sequence<T> = edges.fold(accumulator) { a, v -> v.accumulateBreadthFirstSequence(a) }
        return edgeSequence + value
    }
}
