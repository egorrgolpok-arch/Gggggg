package com.example.game

data class ProceduralQuest(
    val id: String,
    val giverNpcId: String,
    val giverName: String,
    val title: String,
    val description: String,
    val type: QuestType,
    val targetAmount: Int,
    var currentProgress: Int = 0,
    val rewardDiamonds: Int,
    val rewardGold: Int,
    val unlocksEndingId: Int? = null,
    val isDaily: Boolean = false,
    val cooldownHours: Int = 3,
    var lastCompletedTimestamp: Long = 0L,
    var isCompleted: Boolean = false,
    var isClaimed: Boolean = false
)

object ProceduralQuestGenerator {

    private val questTitles = listOf(
        "Зачистка этажа от цензоров", "Поиск алмазов в каньонах", "Сбор батарей для гиперкаров",
        "Уничтожение босса цензуры", "Рейв на 800-м этаже", "Защита золотых колонн",
        "Парижский дефиле-рейд", "Тюремный прорыв Бати", "Автотюновый штурм", "Неоновый дозор"
    )

    private val npcNames = listOf(
        Pair("npc_julien", "Жульен «Французский Люкс»"),
        Pair("npc_batya", "Батя «Титановый Поршень»"),
        Pair("npc_marcus", "Маркус «Стальной Бицепс»"),
        Pair("npc_malik", "DJ Малик «Ритм Марса»"),
        Pair("npc_antoine", "Антуан «Высокий Кутюр»")
    )

    fun getQuest(index: Int): ProceduralQuest {
        val titleBase = questTitles[(index - 1) % questTitles.size]
        val npcPair = npcNames[(index - 1) % npcNames.size]
        val isDaily = index > 10000 // 1 to 10000 are normal quests, 10001 to 10500 are daily rotating quests
        
        val type = when (index % 5) {
            0 -> QuestType.KILL_ENEMIES
            1 -> QuestType.COLLECT_ARTIFACTS
            2 -> QuestType.COLLECT_BATTERIES
            3 -> QuestType.KILL_BOSS
            else -> QuestType.COMPLETE_ENDINGS
        }

        val target = 5 + (index % 20)
        val diamonds = 20 + (index % 80)
        val gold = 100 + (index % 400)
        val endingId = if (index % 10 == 0) ((index % 100) + 1) else null

        return ProceduralQuest(
            id = if (isDaily) "daily_quest_${index - 10000}" else "quest_$index",
            giverNpcId = npcPair.first,
            giverName = npcPair.second,
            title = "$titleBase #${index}",
            description = "Срочное задание от ${npcPair.second}: выполнить цель (${target} единиц) для укрепления позиций Владика на Марсе.",
            type = type,
            targetAmount = target,
            rewardDiamonds = diamonds,
            rewardGold = gold,
            unlocksEndingId = endingId,
            isDaily = isDaily,
            cooldownHours = if (isDaily) 0 else 3
        )
    }

    // Returns active list of quests (some standard + 500 daily rotating based on current 24h block)
    fun getActiveQuests(): List<ProceduralQuest> {
        val list = mutableListOf<ProceduralQuest>()
        // Add 20 standard sample quests for quick display and progress
        for (i in 1..25) {
            list.add(getQuest(i))
        }
        // Add 10 daily rotating quests based on current 24h block
        val dayBlock = System.currentTimeMillis() / (24 * 3600 * 1000L)
        val dailySeedOffset = (dayBlock % 50).toInt() * 10
        for (i in 1..10) {
            list.add(getQuest(10001 + dailySeedOffset + i))
        }
        return list
    }
}
