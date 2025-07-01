package factorio.helper.service

import factorio.helper.entity.Item
import factorio.helper.entity.Items
import factorio.helper.entity.graph.DirectedAcyclicGraph
import factorio.helper.entity.recipe.RecipeComponent
import factorio.helper.entity.recipe.RecipeComponentStats
import factorio.helper.entity.recipe.RecipeStats
import kotlin.requireNotNull

class RecipeStatsCalculator {
    fun getCostStatsForItem(
        items: Items,
        focusedItem: Item,
        itemsPerSecond: Double,
    ): RecipeStats {
        return getBaseStatsForItem(items, focusedItem).scaleStats(
            itemsPerSecond / (focusedItem.recipe?.getQuantityPerSecond() ?: 1.0),
        )
    }

    private fun getBaseStatsForItem(
        items: Items,
        focusedItem: Item,
    ): RecipeStats {
        val recipeGraph: DirectedAcyclicGraph<Item> =
            items.fold(
                DirectedAcyclicGraph(),
                {
                        graph: DirectedAcyclicGraph<Item>,
                        item,
                    ->
                    graph.addValue(item, items.getByNames(item.getRecipeComponentItemNames()).asCollection())
                },
            )

        val recipeStatsMap: Map<Item, RecipeStats> =
            recipeGraph.asBreadthFirstSequence().fold(
                emptyMap(),
                { accumulator, item -> addRecipeStatsToMap(accumulator, item, recipeGraph) },
            )

        return requireNotNull(recipeStatsMap.get(focusedItem))
    }

    private fun addRecipeStatsToMap(
        map: Map<Item, RecipeStats>,
        item: Item,
        recipeGraph: DirectedAcyclicGraph<Item>,
    ): Map<Item, RecipeStats> {
        val componentStats: RecipeStats =
            recipeGraph.forceGetVertex(item)
                .edges
                .map { it.value }
                .map({
                    scaleComponentStatsToRecipe(
                        item,
                        item.forceGetRecipeComponentByItemName(it.name),
                        it,
                        requireNotNull(map.get(it)),
                    )
                })
                .fold(RecipeStats(), { acc, stats -> acc.combine(stats) })

        return map + Pair(item, calculateRecipeStatsGivenComponentStats(item, componentStats))
    }

    private fun scaleComponentStatsToRecipe(
        item: Item,
        recipeComponent: RecipeComponent,
        component: Item,
        componentStats: RecipeStats,
    ): RecipeStats {
        val recipeQuantity: Double = recipeComponent.quantity
        val recipeDuration: Long = if (component.recipe == null) 1000 else component.recipe.duration.toMillis()
        val componentQuantity: Double = component.recipe?.quantity ?: 1.0
        val componentDuration: Long = if (item.recipe == null) 1000 else item.recipe.duration.toMillis()

        // scalar = quantity * sink produced per second / source produced per second
        // Warning: messing with this calculation too much will result in floating point precision errors
        return componentStats.scaleStats(recipeQuantity * recipeDuration / (componentQuantity * componentDuration))
    }

    private fun calculateRecipeStatsGivenComponentStats(
        item: Item,
        componentStats: RecipeStats,
    ): RecipeStats {
        val itemQuantityPerSecond: Double = item.recipe?.getQuantityPerSecond() ?: 1.0
        val machinesRequired: Double? = if (item.isBaseItem()) null else 1.0
        val itemAsComponent: Collection<RecipeComponentStats> = listOf(RecipeComponentStats(item, itemQuantityPerSecond, machinesRequired))
        return RecipeStats(item.recipe?.duration, itemAsComponent).combine(componentStats)
    }
}
