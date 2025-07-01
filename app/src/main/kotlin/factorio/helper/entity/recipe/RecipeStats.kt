package factorio.helper.entity.recipe

import java.time.Duration

data class RecipeStats(val duration: Duration?, val components: Collection<RecipeComponentStats>) {
    constructor() : this(null, emptyList())

    fun combine(other: RecipeStats): RecipeStats {
        return RecipeStats(duration?.plus(other.duration ?: Duration.ofMillis(0)), combineComponents(components, other.components))
    }

    fun scaleStats(by: Double): RecipeStats {
        return RecipeStats(duration, components.map { it.multiplyQuantityPerSecond(by) })
    }

    private fun combineComponents(
        componentsA: Collection<RecipeComponentStats>,
        componentsB: Collection<RecipeComponentStats>,
    ): Collection<RecipeComponentStats> {
        return (componentsA + componentsB).groupBy(RecipeComponentStats::item).values.map({
            it.reduce({ componentA, componentB -> componentA.combine(componentB) })
        })
    }
}
