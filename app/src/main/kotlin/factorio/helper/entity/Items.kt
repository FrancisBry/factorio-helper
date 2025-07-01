package factorio.helper.entity

import java.util.function.BiFunction
import kotlin.collections.listOf

data class Items(private val items: Collection<Item>) {
    private val values: Map<String, Item>

    init {
        values = items.associateBy({ it.name }, { it })
    }

    fun getByName(name: String): Item? {
        return values.get(name)
    }

    fun getByNames(names: Collection<String>): Items {
        return Items(names.map(this::getByName).filterNotNull())
    }

    fun asCollection(): Collection<Item> {
        return values.values
    }

    fun asSequence(): Sequence<Item> {
        return values.values.asSequence()
    }

    fun withItem(item: Item): Items {
        return Items(values.values + listOf(item))
    }

    fun <T> fold(
        initial: T,
        operation: BiFunction<T, Item, T>,
    ): T {
        return asSequence().fold(initial, { acc, item -> operation.apply(acc, item) })
    }
}
