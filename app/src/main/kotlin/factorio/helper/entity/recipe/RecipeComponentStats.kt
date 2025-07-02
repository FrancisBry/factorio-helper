package factorio.helper.entity.recipe

import factorio.helper.entity.Item

data class RecipeComponentStats(val item: Item, val quantityPerSecond: Double, val machinesRequired: Double? = null) {
    fun multiplyQuantityPerSecond(by: Double): RecipeComponentStats {
        return RecipeComponentStats(item, quantityPerSecond * by, machinesRequired?.times(by))
    }

    fun combine(other: RecipeComponentStats): RecipeComponentStats {
        return RecipeComponentStats(
            item,
            quantityPerSecond + other.quantityPerSecond,
            machinesRequired?.plus(other.machinesRequired ?: 0.0),
        )
    }
}
