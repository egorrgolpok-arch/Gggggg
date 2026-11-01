package com.example.game

import androidx.compose.ui.graphics.Color
import kotlin.math.abs
import kotlin.math.floor
import kotlin.math.sin
import kotlin.random.Random

enum class TileType(val isWalkable: Boolean, val color: Color, val detailChar: String = "") {
    PENTHOUSE_MARBLE(true, Color(0xFF2A1348)),
    VELVET_CARPET(true, Color(0xFF7A0938), "═"),
    MARBLE_PILLAR(false, Color(0xFFFAF5FF), "🏛"),
    LAVA_LAMP(false, Color(0xFFFF007F), "♨"),
    NEON_DANCEPAD(true, Color(0xFFD946EF), "♫"),

    MARTIAN_RED_DUNES(true, Color(0xFF962D15)),
    CANYON_EDGE(false, Color(0xFF4A1005), "▼"),
    MARTIAN_CRATER(true, Color(0xFF6B1D0B), "●"),
    MARTIAN_ROCK(false, Color(0xFF380C04), "▲"),
    DIAMOND_CRYSTAL(false, Color(0xFF00F0FF), "◆"),

    GARAGE_ASPHALT(true, Color(0xFF1E222D)),
    GARAGE_SPEEDSTRIPE(true, Color(0xFFFFE600), "═"),
    PARKED_HYPERCAR(false, Color(0xFFFF0055), "🏎"),

    CRYO_FLOOR(true, Color(0xFF0E3247)),
    CRYO_CHAMBER(false, Color(0xFF00B4D8), "❄"),

    SOVIET_CONCRETE(true, Color(0xFF3B0712)),
    SOVIET_STAR(false, Color(0xFFFF3344), "★"),
    TOXIC_MUD(true, Color(0xFF052E16)),
    TOXIC_SPORE(false, Color(0xFF4ADE80), "☣"),
    CRYSTAL_FLOOR(true, Color(0xFF082F49)),
    CRYSTAL_SPIRE(false, Color(0xFF38BDF8), "✧"),
    CASINO_CARPET(true, Color(0xFF1C1917)),
    ROULETTE_TABLE(false, Color(0xFFFBBF24), "🎰"),
    RAVE_STAGE(true, Color(0xFF3B0764)),
    SUBWOOFER_WALL(false, Color(0xFFF43F5E), "🔊"),
    VOLCANO_MAGMA(true, Color(0xFF431407)),
    OBSIDIAN_BOULDER(false, Color(0xFFFB923C), "🌋"),
    GOLDEN_TILES(true, Color(0xFF451A03)),
    GOLDEN_STATUE(false, Color(0xFFFFE066), "👑")
}

object ProceduralWorld {

    const val TILE_SIZE = 64f

    fun getBiomeForPosition(worldX: Float, worldY: Float): BiomeType {
        val regionX = (worldX / (TILE_SIZE * 30)).toInt()
        val regionY = (worldY / (TILE_SIZE * 30)).toInt()
        val biomes = BiomeType.values()
        val hash = abs((regionX * 73856093) xor (regionY * 19349663)) % biomes.size
        return biomes[hash]
    }

    fun getTile(tx: Int, ty: Int): TileType {
        // Safe spawn clearing: Origin and nearby perimeter are always walkable
        if (tx in -2..2 && ty in -2..2) {
            return if (ty == 0) TileType.VELVET_CARPET else TileType.PENTHOUSE_MARBLE
        }

        val biome = getBiomeForPosition(tx * TILE_SIZE, ty * TILE_SIZE)
        val pseudoNoise = sin(tx * 0.35f + ty * 0.17f) * 1000f
        val hash = (abs(tx * 31 + ty * 17 + pseudoNoise.toInt()) % 100)

        return when (biome) {
            BiomeType.PENTHOUSE -> {
                when {
                    tx % 9 == 0 && ty % 9 == 0 -> TileType.MARBLE_PILLAR
                    (tx % 9 == 4 && ty % 9 == 4) || (tx % 9 == 0 && ty % 9 == 4) -> TileType.LAVA_LAMP
                    ty % 7 == 0 -> TileType.VELVET_CARPET
                    hash < 12 -> TileType.NEON_DANCEPAD
                    else -> TileType.PENTHOUSE_MARBLE
                }
            }
            BiomeType.WASTELAND -> {
                when {
                    (tx % 11 in 0..1 && ty % 11 in 0..1) -> TileType.CANYON_EDGE
                    hash < 7 -> TileType.DIAMOND_CRYSTAL
                    hash < 15 -> TileType.MARTIAN_ROCK
                    hash < 28 -> TileType.MARTIAN_CRATER
                    else -> TileType.MARTIAN_RED_DUNES
                }
            }
            BiomeType.GARAGE -> {
                when {
                    tx % 8 == 0 && ty % 6 == 0 -> TileType.PARKED_HYPERCAR
                    ty % 4 == 0 -> TileType.GARAGE_SPEEDSTRIPE
                    else -> TileType.GARAGE_ASPHALT
                }
            }
            BiomeType.CRYO_LOUNGE -> {
                when {
                    hash < 8 -> TileType.CRYO_CHAMBER
                    else -> TileType.CRYO_FLOOR
                }
            }
            BiomeType.SOVIET_BUNKER -> {
                when {
                    tx % 8 == 0 && ty % 8 == 0 -> TileType.SOVIET_STAR
                    else -> TileType.SOVIET_CONCRETE
                }
            }
            BiomeType.TOXIC_SWAMP -> {
                when {
                    hash < 10 -> TileType.TOXIC_SPORE
                    else -> TileType.TOXIC_MUD
                }
            }
            BiomeType.CRYSTAL_CAVERN -> {
                when {
                    hash < 9 -> TileType.CRYSTAL_SPIRE
                    else -> TileType.CRYSTAL_FLOOR
                }
            }
            BiomeType.NEON_CASINO -> {
                when {
                    tx % 7 == 0 && ty % 7 == 0 -> TileType.ROULETTE_TABLE
                    else -> TileType.CASINO_CARPET
                }
            }
            BiomeType.RAVE_ARENA -> {
                when {
                    tx % 6 == 0 && ty % 6 == 0 -> TileType.SUBWOOFER_WALL
                    else -> TileType.RAVE_STAGE
                }
            }
            BiomeType.VOLCANO_CORE -> {
                when {
                    hash < 11 -> TileType.OBSIDIAN_BOULDER
                    else -> TileType.VOLCANO_MAGMA
                }
            }
            BiomeType.ETERNAL_THRONE -> {
                when {
                    tx % 9 == 0 && ty % 9 == 0 -> TileType.GOLDEN_STATUE
                    else -> TileType.GOLDEN_TILES
                }
            }
        }
    }

    /**
     * Checks if a circular bounding box at (worldX, worldY) with given radius
     * does not overlap any impassable obstacle tile.
     */
    fun isPositionWalkable(worldX: Float, worldY: Float, radius: Float = 16f): Boolean {
        val minTx = floor((worldX - radius) / TILE_SIZE).toInt()
        val maxTx = floor((worldX + radius) / TILE_SIZE).toInt()
        val minTy = floor((worldY - radius) / TILE_SIZE).toInt()
        val maxTy = floor((worldY + radius) / TILE_SIZE).toInt()

        for (tx in minTx..maxTx) {
            for (ty in minTy..maxTy) {
                val tile = getTile(tx, ty)
                if (!tile.isWalkable) {
                    return false
                }
            }
        }
        return true
    }

    private val punchlines = listOf(
        "Роботы-натуралы хотят отключить мой автотюн? Ни за что!",
        "Мои мраморные колонны сделаны из редчайшего марсианского камня!",
        "Эти жестянки и качки-рейдеры не выдержат басов с 800-го этажа!",
        "Мои лавовые лампы стоят больше всего вашего космического флота!",
        "Врубаю 6969 лошадиных сил прямо в лоб завистникам!",
        "Мои цепи ярче марсианского солнца, их датчики плавятся!",
        "В моём гараже 6969 гиперкаров, а у натуралов только штрафы!",
        "800 этажей роскоши! Куда вы лезете, душнилы?!",
        "Я читаю рэп на квантовой частоте Марса, ловите бит!",
        "Радужный бриллиантовый флекс пробивает любые энергощиты!",
        "Шеф Маркус и DJ Малик уже зарядили сабвуферы на максимум!"
    )

    fun getRandomPunchline(): String {
        return punchlines.random()
    }

    // 1111 Unique Story Endings
    val all100Endings: List<StoryEnding> get() = EndingsDatabase.all1111StoryEndings

    // 50 Friendly NPCs
    fun getStoryNPCs(): List<FriendlyNPC> = NPCDatabase.getAllNPCs()

    // 110 Crafting Recipes
    val defaultRecipes: List<CraftingRecipe> get() = ItemDatabase.allCraftingRecipes

    // 200+ World Artifacts per floor
    fun generateWorldArtifactsForFloor(floor: Int): List<WorldArtifact> =
        ItemDatabase.generateFloorWorldArtifacts(floor)

    fun generateRandomArtifact(floor: Int): Artifact {
        val base = ItemDatabase.all200WorldArtifacts.random()
        return base.copy(
            id = java.util.UUID.randomUUID().toString(),
            level = floor,
            bonusHp = base.bonusHp + floor * 6f,
            bonusDamage = base.bonusDamage + floor * 3.5f,
            bonusSpeed = base.bonusSpeed + floor * 1.5f,
            bonusHypeRate = base.bonusHypeRate + floor * 0.05f
        )
    }

    fun getDefaultQuests(): List<Quest> {
        val list = mutableListOf<Quest>()

        // Daily 24h Special Quests
        list.add(
            Quest(
                id = "quest_daily_1",
                giverNpcId = "npc_julien",
                giverName = "⏱ ЕЖЕДНЕВНЫЙ КОНТРАКТ 24Ч",
                title = "24Ч: Парижский Рейд против Дронов",
                description = "Ежедневное задание: Уничтожь 25 дронов-натуралов за 24 часа. Награда обновится через 24ч!",
                type = QuestType.KILL_ENEMIES,
                targetAmount = 25,
                rewardDiamonds = 100,
                rewardGold = 600
            )
        )
        list.add(
            Quest(
                id = "quest_daily_2",
                giverNpcId = "npc_batya",
                giverName = "⏱ ЕЖЕДНЕВНЫЙ КОНТРАКТ 24Ч",
                title = "24Ч: Батарейный Поток 6969",
                description = "Ежедневное задание: Собери 12 батарей для турбин гиперкаров.",
                type = QuestType.COLLECT_BATTERIES,
                targetAmount = 12,
                rewardDiamonds = 120,
                rewardGold = 800
            )
        )

        // 100 Quests with 3h CD mechanics & variable rewards
        val givers = listOf(
            Pair("npc_julien", "Жульен «Французский Люкс»"),
            Pair("npc_batya", "Батя «Титановый Поршень»"),
            Pair("npc_marcus", "Маркус «Стальной Бицепс»"),
            Pair("npc_malik", "DJ Малик «Ритм Марса»")
        )

        for (i in 1..100) {
            val (giverId, giverName) = givers[i % givers.size]
            val questType = QuestType.values()[i % QuestType.values().size]
            val target = when (questType) {
                QuestType.KILL_ENEMIES -> 10 + (i % 20)
                QuestType.KILL_BOSS -> 1 + (i % 2)
                QuestType.COLLECT_ARTIFACTS -> 2 + (i % 5)
                QuestType.COLLECT_BATTERIES -> 3 + (i % 6)
                QuestType.COMPLETE_ENDINGS -> 1 + (i % 3)
            }
            val title = when (questType) {
                QuestType.KILL_ENEMIES -> "Контракт #$i: Зачистка Этажа"
                QuestType.KILL_BOSS -> "Контракт #$i: Охота на Босса Mk.800"
                QuestType.COLLECT_ARTIFACTS -> "Контракт #$i: Поиск Реликвий Влада"
                QuestType.COLLECT_BATTERIES -> "Контракт #$i: Энерго-Сбор для Бати"
                QuestType.COMPLETE_ENDINGS -> "Контракт #$i: Летопись Побед Марса"
            }
            val desc = "Задание от $giverName. Выполни цель ($target единиц). [Перезарядка 3ч после сдачи]."

            list.add(
                Quest(
                    id = "quest_contract_$i",
                    giverNpcId = giverId,
                    giverName = giverName,
                    title = title,
                    description = desc,
                    type = questType,
                    targetAmount = target,
                    rewardDiamonds = 30 + i * 2,
                    rewardGold = 150 + i * 10,
                    unlocksEndingId = if (i % 10 == 0) (i % 100) + 1 else null
                )
            )
        }

        return list
    }

    fun generateRandomWeapon(floor: Int): Weapon {
        val type = WeaponType.values().random()
        val prefixes = listOf("Сияющий", "Сверхзвуковой", "Богемный", "Турбо-Хайповый", "Бриллиантовый", "Неоновый")
        val suffixes = listOf("Владика", "Пентхауса 800", "6969 Лошадей", "Абсолютного Флекса", "Марсианского Рэпа")
        val prefix = prefixes.random()
        val suffix = suffixes.random()

        val rarityRoll = Random.nextInt(100)
        val rarity = when {
            rarityRoll > 85 -> ItemRarity.LEGENDARY
            rarityRoll > 60 -> ItemRarity.EPIC
            rarityRoll > 30 -> ItemRarity.RARE
            else -> ItemRarity.COMMON
        }

        val baseDamage = when (type) {
            WeaponType.RUBBER_BAT -> 45f + floor * 12f
            WeaponType.GLAM_WHIP -> 38f + floor * 9f
            WeaponType.PINK_SHOCKER -> 50f + floor * 14f
            WeaponType.AUTOTUNE_MIC -> 65f + floor * 18f
            WeaponType.PLATINUM_BLASTER -> 85f + floor * 20f
            WeaponType.EXPLODING_BLACK_MAN -> 95f + floor * 22f
            WeaponType.RAINBOW_LGBT_FLAG -> 70f + floor * 16f
            WeaponType.CLUSTER_PINK_PHALLUS -> 80f + floor * 18f
            WeaponType.EXPLOSIVE_BOOM_BLASTER -> 110f + floor * 25f
            WeaponType.WHITE_SLIME_DISPENSER -> 60f + floor * 15f
        } * (1f + rarity.ordinal * 0.35f)

        val name = when (type) {
            WeaponType.RUBBER_BAT -> "Резиновая Булава $prefix $suffix"
            WeaponType.GLAM_WHIP -> "Плеть $prefix $suffix"
            WeaponType.PINK_SHOCKER -> "Шокер $prefix $suffix"
            WeaponType.AUTOTUNE_MIC -> "Микрофон $prefix $suffix"
            WeaponType.PLATINUM_BLASTER -> "Платиновый Бластер $prefix $suffix"
            WeaponType.EXPLODING_BLACK_MAN -> "Мортира Черных Бегунов $prefix $suffix"
            WeaponType.RAINBOW_LGBT_FLAG -> "Радужный Флагомет $prefix $suffix"
            WeaponType.CLUSTER_PINK_PHALLUS -> "Кластерный Фалос-Ган $prefix $suffix"
            WeaponType.EXPLOSIVE_BOOM_BLASTER -> "Взрывной Бластер $prefix $suffix"
            WeaponType.WHITE_SLIME_DISPENSER -> "Диспенсер Белой Жижи $prefix $suffix"
        }

        val desc = when (type) {
            WeaponType.RUBBER_BAT -> "Оглушает врагов звонким писком и мощным отталкиванием."
            WeaponType.GLAM_WHIP -> "Наносит дуговой урон цепной неоновой молнией."
            WeaponType.PINK_SHOCKER -> "Выпускает сгустки концентрированного розового гламура."
            WeaponType.AUTOTUNE_MIC -> "Стреляет звуковыми ударными волнами высочайшего битрейта."
            WeaponType.PLATINUM_BLASTER -> "Разрушительные басовые вибрации прожигают броню."
            WeaponType.EXPLODING_BLACK_MAN -> "Стреляет черными бегунами, которые детонируют при попадании."
            WeaponType.RAINBOW_LGBT_FLAG -> "Стреляет радужными флагами, ослепляющими толпы натуралов."
            WeaponType.CLUSTER_PINK_PHALLUS -> "Снаряд разлетается на веер маленьких розовых фалосов при контакте."
            WeaponType.EXPLOSIVE_BOOM_BLASTER -> "Создает взрыв при стрельбе и разносит врагов мощной волной."
            WeaponType.WHITE_SLIME_DISPENSER -> "Разливает лужи белой субстанции, замедляя и растворяя врагов."
        }

        val color = when (type) {
            WeaponType.RUBBER_BAT -> Color(0xFFFF1493)
            WeaponType.GLAM_WHIP -> Color(0xFF8B5CF6)
            WeaponType.PINK_SHOCKER -> Color(0xFFFF69B4)
            WeaponType.AUTOTUNE_MIC -> Color(0xFFFFD700)
            WeaponType.PLATINUM_BLASTER -> Color(0xFFE2E8F0)
            WeaponType.EXPLODING_BLACK_MAN -> Color(0xFF1E293B)
            WeaponType.RAINBOW_LGBT_FLAG -> Color(0xFFFF007F)
            WeaponType.CLUSTER_PINK_PHALLUS -> Color(0xFFFF1493)
            WeaponType.EXPLOSIVE_BOOM_BLASTER -> Color(0xFFFF6B00)
            WeaponType.WHITE_SLIME_DISPENSER -> Color(0xFFF8FAFC)
        }

        return Weapon(
            type = type,
            name = name,
            description = desc,
            damage = baseDamage,
            attackSpeed = 2.4f + Random.nextFloat() * 0.6f,
            range = if (type == WeaponType.RUBBER_BAT || type == WeaponType.GLAM_WHIP) 95f else 320f,
            knockback = if (type == WeaponType.RUBBER_BAT) 190f else 75f,
            color = color,
            rarity = rarity,
            level = 1
        )
    }
}
