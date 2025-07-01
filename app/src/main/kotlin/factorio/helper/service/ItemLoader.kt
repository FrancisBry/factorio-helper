package factorio.helper.service

import factorio.helper.entity.Item
import factorio.helper.entity.Items
import factorio.helper.entity.recipe.Recipe
import factorio.helper.entity.recipe.RecipeComponent
import org.apache.commons.lang3.StringUtils
import java.time.Duration

class ItemLoader {
    fun load(): Items {
        return Items(loadLines().asSequence().drop(1).map(this::parseLine).toList())
    }

    private fun loadLines(): Collection<String> {
        return javaClass.getResourceAsStream("/items.csv")?.bufferedReader()?.readLines() ?: emptyList()
    }

    private fun parseLine(line: String): Item {
        val parts: List<String> = line.split(",").filter(StringUtils::isNotBlank)
        val name: String = parts.get(0)
        return Item(name, parseRecipe(parts))
    }

    private fun parseRecipe(parts: List<String>): Recipe? {
        if (parts.size == 1) {
            return null
        }
        val recipeComponents: Collection<RecipeComponent> =
            parts.subList(3, parts.size).chunked(2).asSequence()
                .map({ componentParts -> parseRecipeComponent(componentParts.get(0), componentParts.get(1)) })
                .toList()
        return Recipe(Duration.ofMillis(parts.get(1).toLong()), parts.get(2).toDouble(), recipeComponents)
    }

    private fun parseRecipeComponent(
        name: String,
        quantity: String,
    ): RecipeComponent {
        return RecipeComponent(
            name,
            quantity.toDouble(),
        )
    }
}
