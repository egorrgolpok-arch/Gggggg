package com.example.game

data class ProceduralTalent(
    val id: String,
    val title: String,
    val description: String,
    val maxLevel: Int = 5000,
    val baseCostDiamonds: Int,
    val baseCostGold: Int,
    val iconEmoji: String,
    val statBonusPerLevel: Float
)

object ProceduralTalentGenerator {

    private val categories = listOf(
        Pair("Алмазная Кольчуга", "💎"),
        Pair("Платиновый Автотюн", "🎙"),
        Pair("Неоновый Рывок", "⚡"),
        Pair("Хайп-Турбо", "🏎"),
        Pair("Золотой Дождь", "💰"),
        Pair("Гламурный Вампиризм", "🍷"),
        Pair("Квантовый Резонанс", "🔮"),
        Pair("Титановый Пресс", "🛡"),
        Pair("Сабвуфер Олимпа", "🔊"),
        Pair("Лавовый Шок", "♨")
    )

    fun getTalent(index: Int): ProceduralTalent {
        val catIndex = (index - 1) % categories.size
        val cat = categories[catIndex]
        val tierNum = ((index - 1) / categories.size) + 1
        
        val title = "${cat.first} Mk.$tierNum"
        val desc = "Улучшение #${index} (Макс. 5000 ур.): усиливает параметры Владика на +${(tierNum * 0.4f)}%."
        val diamonds = 10 + (index % 40)
        val gold = 50 + (index % 200)

        return ProceduralTalent(
            id = "talent_$index",
            title = title,
            description = desc,
            maxLevel = 5000,
            baseCostDiamonds = diamonds,
            baseCostGold = gold,
            iconEmoji = cat.second,
            statBonusPerLevel = 0.5f * tierNum
        )
    }

    fun getAllTalents(): List<ProceduralTalent> {
        // Returns first 1000 talents for UI listing / iteration
        return (1..1000).map { getTalent(it) }
    }
}
