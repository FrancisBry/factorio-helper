package factorio.helper.entity

import factorio.helper.entity.recipe.Recipe
import factorio.helper.entity.recipe.RecipeComponent
import kotlin.collections.emptyList

data class Item(val name: String, val recipe: Recipe?) {
    fun isBaseItem(): Boolean {
        return recipe?.hasNoComponents() ?: true
    }

    fun getRecipeComponentItemNames(): Collection<String> {
        return recipe?.getComponentItemNames() ?: emptyList()
    }

    fun getRecipeComponentByItemName(name: String): RecipeComponent? {
        return recipe?.getComponentByItemName(name)
    }

    fun forceGetRecipeComponentByItemName(name: String): RecipeComponent {
        return requireNotNull(getRecipeComponentByItemName(name))
    }
}
