package factorio.helper.entity.recipe

import java.time.Duration

data class Recipe(val duration: Duration, val quantity: Double, val components: Collection<RecipeComponent>) {
    fun hasNoComponents(): Boolean {
        return components.isEmpty()
    }

    fun getComponentItemNames(): Collection<String> {
        return components.map { it.itemName }
    }

    fun getComponentByItemName(name: String): RecipeComponent? {
        return components.find { it.itemName == name }
    }

    fun getQuantityPerSecond(): Double {
        return quantity * getProductionsPerSecond()
    }

    fun getProductionsPerSecond(): Double {
        return 1000.0 / duration.toMillis()
    }
}
