package factorio.helper.service

import factorio.helper.entity.Items
import factorio.helper.entity.recipe.RecipeStats
import kotlin.requireNotNull

class ItemService {
    private val itemLoader: ItemLoader
    private val statsCalculator: RecipeStatsCalculator

    init {
        itemLoader = ItemLoader()
        statsCalculator = RecipeStatsCalculator()
    }

    fun getCostStatsForItem(
        name: String,
        itemsPerSecond: Double,
    ): RecipeStats {
        val items: Items = itemLoader.load()
        return statsCalculator.getCostStatsForItem(items, requireNotNull(items.getByName(name)), itemsPerSecond)
    }
}
