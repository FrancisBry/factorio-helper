package factorio.helper.entity.graph

import kotlin.Pair
import kotlin.collections.emptyMap

class DirectedAcyclicGraph<T : Any>(private val verticies: Map<T, DirectedAcyclicVertex<T>>) {
    constructor() : this(emptyMap())

    fun getVertex(value: T): DirectedAcyclicVertex<T>? {
        return verticies.get(value)
    }

    fun forceGetVertex(value: T): DirectedAcyclicVertex<T> {
        return requireNotNull(getVertex(value))
    }

    fun addValue(
        value: T,
        edges: Collection<T>,
    ): DirectedAcyclicGraph<T> {
        return addVertex(verticies.get(value)?.addEdges(toVerticies(edges)) ?: DirectedAcyclicVertex(value, toVerticies(edges)))
    }

    fun asBreadthFirstSequence(): Sequence<T> {
        return verticies.values.fold(
            emptySequence(),
            { sequence, vertex -> vertex.accumulateBreadthFirstSequence(sequence) },
        )
    }

    private fun addVertex(vertex: DirectedAcyclicVertex<T>): DirectedAcyclicGraph<T> {
        return DirectedAcyclicGraph(verticies + vertex.getTraversableVerticies().map { Pair(it.value, it) })
    }

    private fun toVerticies(edges: Collection<T>): Collection<DirectedAcyclicVertex<T>> {
        return edges.asSequence().map(this::toVertex).toList()
    }

    private fun toVertex(edge: T): DirectedAcyclicVertex<T> {
        return verticies.get(edge) ?: DirectedAcyclicVertex(edge, emptyList())
    }
}
