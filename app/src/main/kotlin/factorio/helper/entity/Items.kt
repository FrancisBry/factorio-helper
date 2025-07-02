package factorio.helper.entity

import java.util.function.BiFunction

data class Items(private val items: Collection<Item>) {
    private val values: Map<String, Item> = items.associateBy({ it.name }, { it })

    fun getByName(name: String): Item? {
        return values[name]
    }

    fun getByNames(names: Collection<String>): Items {
        return Items(names.mapNotNull(this::getByName))
    }

    fun asCollection(): Collection<Item> {
        return values.values
    }

    fun asSequence(): Sequence<Item> {
        return values.values.asSequence()
    }

    fun <T> fold(
        initial: T,
        operation: BiFunction<T, Item, T>,
    ): T {
        return asSequence().fold(initial) { acc, item -> operation.apply(acc, item) }
    }
}
