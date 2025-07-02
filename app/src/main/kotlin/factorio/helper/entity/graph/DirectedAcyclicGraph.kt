package factorio.helper.entity.graph

import kotlin.Pair
import kotlin.collections.emptyMap

class DirectedAcyclicGraph<T : Any>(private val vertices: Map<T, DirectedAcyclicVertex<T>>) {
    constructor() : this(emptyMap())

    fun getVertex(value: T): DirectedAcyclicVertex<T>? {
        return vertices[value]
    }

    fun forceGetVertex(value: T): DirectedAcyclicVertex<T> {
        return requireNotNull(getVertex(value))
    }

    fun addValue(
        value: T,
        edges: Collection<T>,
    ): DirectedAcyclicGraph<T> {
        return addVertex(vertices[value]?.addEdges(toVertices(edges)) ?: DirectedAcyclicVertex(value, toVertices(edges)))
    }

    fun asBreadthFirstSequence(): Sequence<T> {
        return vertices.values.fold(
            emptySequence())
            { sequence, vertex -> vertex.accumulateBreadthFirstSequence(sequence) }
    }

    private fun addVertex(vertex: DirectedAcyclicVertex<T>): DirectedAcyclicGraph<T> {
        return DirectedAcyclicGraph(vertices + vertex.getTraversableVertices().map { Pair(it.value, it) })
    }

    private fun toVertices(edges: Collection<T>): Collection<DirectedAcyclicVertex<T>> {
        return edges.asSequence().map(this::toVertex).toList()
    }

    private fun toVertex(edge: T): DirectedAcyclicVertex<T> {
        return vertices[edge] ?: DirectedAcyclicVertex(edge, emptyList())
    }
}
