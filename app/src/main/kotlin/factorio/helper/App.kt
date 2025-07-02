package factorio.helper

import factorio.helper.entity.recipe.RecipeStats
import factorio.helper.service.ItemService

fun main(args: Array<String>) {
    val itemName: String = args[0]
    val itemsPerSecond: Double = args[1].toDouble()
    val itemService = ItemService()
    val recipeStats: RecipeStats = itemService.getCostStatsForItem(itemName, itemsPerSecond)
    recipeStats.components.forEach {
        println("")
        println("Component: " + it.item.name)
        println("Quantity per second: " + it.quantityPerSecond)
        println("Machines required: " + it.machinesRequired)
    }
}
