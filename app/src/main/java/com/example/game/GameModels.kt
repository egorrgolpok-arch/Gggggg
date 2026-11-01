package com.example.game

import androidx.compose.ui.graphics.Color
import java.util.UUID

enum class GameScreen {
    SPLASH,
    MAIN_MENU,
    VIP_HUB,
    PLAYING,
    INVENTORY,
    DIALOGUE,
    ARTIFACT_INSPECT,
    TALENT_TREE,
    QUEST_LOG,
    ENDINGS_ARCHIVE,
    GAME_OVER,
    VICTORY,
    GARAGE,
    WARDROBE
}

enum class VladSkin(
    val id: String,
    val title: String,
    val description: String,
    val unlockedEndingThreshold: Int,
    val drawableResId: Int,
    val iconEmoji: String
) {
    GAY_CAR("skin_gaycar", "GAY-CAR Пуховик & LV Сумка", "Фирменный образ Рэпера Владика: пуховик, LV сумка с деньгами и суперкар.", 0, com.example.R.drawable.vlad_skin_gaycar, "🏎️"),
    UTENOK("skin_utenok", "Влад «Утёнок & Мальчик»", "Уточница в руках, мате-чик на подхвате и танцевальный флекс.", 10, com.example.R.drawable.vlad_skin_utenok, "🐥"),
    FATHER("skin_father", "Батя Влада", "Соломенная шляпа, майка-сеточка, золотые цепи и накачанный пресс.", 25, com.example.R.drawable.vlad_skin_father, "🧔"),
    PINK_FUR("skin_pinkfur", "Розовая Меховая Шуба", "Шикарный розовый мех, панама с зеленым кактусом и блинги.", 50, com.example.R.drawable.vlad_skin_pinkfur, "🌵"),
    HEADPHONES("skin_headphones", "Геймер-Владик с Гитарой", "Белые студийные наушники, электрогитара Fender и геймпад.", 100, com.example.R.drawable.vlad_skin_headphones, "🎸"),
    LOLLY_HOLLY("skin_lolly", "Lolly Holly Влад", "Черный глэм-кожаный костюм с кошачьими ушками и слезами на щеках.", 200, com.example.R.drawable.vlad_skin_lolly, "🖤")
}

enum class GameDifficulty(
    val title: String,
    val description: String,
    val enemyHpMultiplier: Float,
    val scoreMultiplier: Float
) {
    NORMAL("Нормальный Хайп", "Сбалансированное приключение на Марсе", 1.0f, 1.0f),
    HARD("Сверхтяжелый Марс", "Очень сложный режим: враги в 3.5х сильнее и быстрее!", 3.5f, 3.0f),
    EXTREME("Экстремальный Марс", "Усиленные роботы-натуралы и качки-рейдеры", 1.4f, 1.5f),
    VIP_FLEX("VIP Флекс-Режим", "Владик получает +50% ресурсов и щит", 0.85f, 1.2f)
}

enum class BiomeType(val displayName: String, val baseColor: Color) {
    PENTHOUSE("Неоновый Пентхаус", Color(0xFF2D124D)),
    WASTELAND("Красная Пустыня Марса", Color(0xFF6B200B)),
    GARAGE("Гараж 6969 Суперкаров", Color(0xFF141926)),
    CRYO_LOUNGE("VIP Крио-Лаундж", Color(0xFF0F3B4A)),
    SOVIET_BUNKER("Советский Бункер Марса", Color(0xFF3B0712)),
    TOXIC_SWAMP("Токсичные Неоновые Болота", Color(0xFF052E16)),
    CRYSTAL_CAVERN("Алмазные Пещеры", Color(0xFF082F49)),
    NEON_CASINO("Казино 'Все на Зеро'", Color(0xFF1C1917)),
    RAVE_ARENA("Рейв-Арена 808", Color(0xFF3B0764)),
    VOLCANO_CORE("Ядро Вулкана Олимп", Color(0xFF431407)),
    ETERNAL_THRONE("Золотой Трон Влада", Color(0xFF451A03))
}

enum class TimeOfDay(val displayName: String, val ambientColor: Color, val ambientDarkness: Float) {
    DAY("Марсианский День", Color(0x22FFA500), 0.05f),
    SUNSET("Пурпурный Закат", Color(0x44D946EF), 0.25f),
    NIGHT("Неоновая Ночь", Color(0x990A071B), 0.55f)
}

enum class WeaponType {
    RUBBER_BAT,
    GLAM_WHIP,
    PINK_SHOCKER,
    AUTOTUNE_MIC,
    PLATINUM_BLASTER,
    EXPLODING_BLACK_MAN,
    RAINBOW_LGBT_FLAG,
    CLUSTER_PINK_PHALLUS,
    EXPLOSIVE_BOOM_BLASTER,
    WHITE_SLIME_DISPENSER
}

enum class ProjectileType {
    STANDARD,
    EXPLODING_MAN,
    RAINBOW_FLAG,
    CLUSTER_PHALLUS,
    CLUSTER_MINI,
    EXPLOSION_ORB,
    WHITE_SLIME
}

enum class ItemRarity(val title: String, val color: Color) {
    COMMON("Обычный", Color(0xFFAAAAAA)),
    RARE("Редкий", Color(0xFF3B82F6)),
    EPIC("Эпический", Color(0xFFA855F7)),
    LEGENDARY("Легендарный Блинг", Color(0xFFFFD700))
}

data class Weapon(
    val id: String = UUID.randomUUID().toString(),
    val type: WeaponType,
    val name: String,
    val description: String,
    val damage: Float,
    val attackSpeed: Float,
    val range: Float,
    val knockback: Float,
    val color: Color,
    val rarity: ItemRarity,
    val level: Int = 1
)

data class Artifact(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val description: String,
    val rarity: ItemRarity,
    val bonusHp: Float = 0f,
    val bonusSpeed: Float = 0f,
    val bonusDamage: Float = 0f,
    val bonusHypeRate: Float = 0f,
    val loreStory: String = "",
    val level: Int = 1
)

data class WorldArtifact(
    val id: String = UUID.randomUUID().toString(),
    val artifact: Artifact,
    val x: Float,
    val y: Float,
    var isCollected: Boolean = false
)

enum class EnemyType(val baseHp: Float, val speed: Float, val score: Int, val isBoss: Boolean = false) {
    CENSOR_DRONE(40f, 135f, 50),
    FITNESS_RAIDER(75f, 110f, 90),
    SHIELD_ENFORCER(95f, 85f, 120),
    BUREAU_WALKER(140f, 90f, 180),
    WHITE_BUFF_MALE(65f, 125f, 70),
    GIRL_RUNNER(50f, 150f, 60),
    HEAVY_FEMALE(110f, 95f, 110),
    WALKING_FEMALE_SIGN(80f, 115f, 85),
    GRAND_CENSOR_BOSS(1400f, 65f, 2500, isBoss = true),
    FAT_SKUF_BOSS(1600f, 60f, 3000, isBoss = true),
    USSR_MAN_BOSS(1800f, 75f, 3500, isBoss = true),
    VLAD_MOM_BOSS(2200f, 85f, 4000, isBoss = true),
    VLAD_DAD_BOSS(2500f, 90f, 4500, isBoss = true),
    GIRL_5YO_BOSS(3000f, 110f, 5000, isBoss = true)
}

data class Enemy(
    val id: String = UUID.randomUUID().toString(),
    val type: EnemyType,
    val name: String,
    var x: Float,
    var y: Float,
    var hp: Float,
    val maxHp: Float,
    val speed: Float,
    val damage: Float,
    val radius: Float = 26f,
    val color: Color,
    var attackCooldown: Float = 0f,
    var animFrame: Float = 0f,
    var facingAngle: Float = 0f,
    val hasShield: Boolean = false
)

data class Projectile(
    var x: Float,
    var y: Float,
    val vx: Float,
    val vy: Float,
    val damage: Float,
    val radius: Float,
    val color: Color,
    val isPlayer: Boolean,
    var lifetime: Float,
    val id: String = UUID.randomUUID().toString(),
    val projectileType: ProjectileType = ProjectileType.STANDARD
)

data class GroundHazard(
    val id: String = UUID.randomUUID().toString(),
    var x: Float,
    var y: Float,
    var radius: Float = 55f,
    var lifetime: Float = 8.0f,
    val maxLifetime: Float = 8.0f,
    val damagePerSec: Float = 60f,
    val slowFactor: Float = 0.4f
)

data class Particle(
    var x: Float,
    var y: Float,
    var vx: Float,
    var vy: Float,
    val color: Color,
    var size: Float,
    var lifetime: Float,
    val maxLifetime: Float
)

data class DamagePopup(
    var x: Float,
    var y: Float,
    val text: String,
    val color: Color,
    var lifetime: Float = 0.8f,
    val isCrit: Boolean = false,
    val id: String = UUID.randomUUID().toString()
)

enum class DropType {
    GOLD,
    DIAMOND,
    BATTERY,
    HEART
}

data class DroppedItem(
    val type: DropType,
    val x: Float,
    val y: Float,
    val amount: Int,
    var lifetime: Float = 25f,
    val id: String = UUID.randomUUID().toString()
)

data class HypercarSummon(
    var x: Float,
    var y: Float,
    val vx: Float,
    val vy: Float,
    val angle: Float,
    var lifetime: Float = 2.0f,
    val damage: Float = 500f
)

data class ActiveCompanion(
    val id: String,
    val name: String,
    var x: Float = 0f,
    var y: Float = 0f,
    val color: Color = Color(0xFF00F0FF),
    var shootCooldown: Float = 0f
)

enum class FloorBiome(val title: String, val primaryColor: Color, val accentColor: Color) {
    PENTHOUSE("Пентхаус 800-го Этажа", Color(0xFF1E0A3C), Color(0xFFFFD700)),
    CYBER_GARAGE("Кибер-Гараж 6969", Color(0xFF0F172A), Color(0xFF00F0FF)),
    MARTIAN_CANYON("Багровый Марсианский Каньон", Color(0xFF450A0A), Color(0xFFFF4081)),
    LAVA_MATRIX("Матрица Неоновых Лав", Color(0xFF18181B), Color(0xFF10B981)),
    PALACE_SUMMIT("Золотой Пик Марса", Color(0xFF2E1065), Color(0xFFE040FB)),
    RETRO_BUNKER("Советский Марсианский Бункер", Color(0xFF3B0712), Color(0xFFFF3344)),
    TOXIC_SWAMP("Токсичный Неоновый Свамп", Color(0xFF052E16), Color(0xFF4ADE80)),
    CRYSTAL_CAVERN("Алмазные Кристальные Пещеры", Color(0xFF082F49), Color(0xFF38BDF8)),
    NEON_CASINO("Космическое Казино 'Все на Зеро'", Color(0xFF1C1917), Color(0xFFFBBF24)),
    CYBER_RAVE("Рейв-Арена '808 Бас'", Color(0xFF3B0764), Color(0xFFF43F5E)),
    OLYMPUS_VOLCANO("Ядро Вулкана Олимп", Color(0xFF431407), Color(0xFFFB923C)),
    ETERNAL_THRONE("Вечный Золотой Трон Владика", Color(0xFF451A03), Color(0xFFFFE066));

    companion object {
        fun getForFloor(floor: Int): FloorBiome {
            val biomes = values()
            val index = ((floor - 1) / 5) % biomes.size
            return biomes[index]
        }
    }
}

data class DialogueOption(
    val label: String,
    val nextNodeIndex: Int = -1,
    val unlocksEndingId: Int? = null,
    val rewardGold: Int = 0,
    val hireCostGold: Int = 0,
    val isHireOption: Boolean = false
)

data class DialogueNode(
    val speaker: String,
    val text: String,
    val options: List<DialogueOption>
)

data class FriendlyNPC(
    val id: String,
    val name: String,
    val role: String,
    val x: Float,
    val y: Float,
    val dialogues: List<DialogueNode>,
    val color: Color
)

data class StoryEnding(
    val id: Int,
    val title: String,
    val description: String,
    val unlocked: Boolean = false
)

data class Player(
    var x: Float = 0f,
    var y: Float = 0f,
    var hp: Float = 250f,
    var maxHp: Float = 250f,
    var baseSpeed: Float = 220f,
    var facingAngle: Float = 0f,
    var isDashing: Boolean = false,
    var dashTimer: Float = 0f,
    var dashCooldown: Float = 0f,
    var hypeEnergy: Float = 0f,
    var gold: Int = 100,
    var diamonds: Int = 10,
    var batteries: Int = 5,
    var kills: Int = 0,
    var floor: Int = 1,
    var equippedWeapon: Weapon,
    val equippedWeapons: MutableList<Weapon> = mutableListOf(),
    val inventoryWeapons: MutableList<Weapon> = mutableListOf(),
    val equippedArtifacts: MutableList<Artifact> = mutableListOf(),
    var activeCompanion: ActiveCompanion? = null,
    var activeSkin: VladSkin = VladSkin.GAY_CAR,
    var isGodModeVlad: Boolean = false,
    var isMoving: Boolean = false,
    var walkCycleTimer: Float = 0f,
    var speechText: String? = null,
    var speechTimer: Float = 0f
) {
    fun getEffectiveEquippedWeapons(): List<Weapon> {
        return if (equippedWeapons.isNotEmpty()) equippedWeapons.toList() else listOf(equippedWeapon)
    }
}

data class CraftingRecipe(
    val id: String,
    val name: String,
    val description: String,
    val reqGold: Int,
    val reqDiamonds: Int,
    val reqBatteries: Int,
    val producesWeapon: Weapon? = null,
    val producesArtifact: Artifact? = null
)

data class MetaprogressionTalents(
    val healthTier: Int = 0,       // +35 Max HP
    val damageTier: Int = 0,       // +12% Weapon Damage
    val dashCooldownTier: Int = 0, // -0.12s Dash CD
    val hypeRateTier: Int = 0,     // +20% Hype generation
    val greedTier: Int = 0,        // +25% Gold & Diamond drop
    val vampirismTier: Int = 0,    // +3% Lifesteal
    val armorTier: Int = 0,        // +5% Damage Reduction
    val critChanceTier: Int = 0,   // +2% Crit Chance
    val critDamageTier: Int = 0,   // +15% Crit Damage
    val speedTier: Int = 0,        // +6% Move Speed
    val bossSlayerTier: Int = 0    // +10% Boss Damage
) {
    fun getBonusMaxHp(): Float = healthTier * 35f
    fun getDamageMultiplier(): Float = 1.0f + damageTier * 0.12f + critDamageTier * 0.15f
    fun getDashCooldownReduction(): Float = dashCooldownTier * 0.12f
    fun getHypeMultiplier(): Float = 1.0f + hypeRateTier * 0.20f
    fun getDropMultiplier(): Float = 1.0f + greedTier * 0.25f
    fun getVampirismChance(): Float = vampirismTier * 0.03f
    fun getDamageReduction(): Float = (armorTier * 0.05f).coerceAtMost(0.75f)
    fun getSpeedMultiplier(): Float = 1.0f + speedTier * 0.06f
    fun getBossDamageMultiplier(): Float = 1.0f + bossSlayerTier * 0.10f
    fun getTotalTalentLevel(): Int = healthTier + damageTier + dashCooldownTier + hypeRateTier + greedTier + vampirismTier + armorTier + critChanceTier + critDamageTier + speedTier + bossSlayerTier
}

enum class TalentType(
    val title: String,
    val description: String,
    val maxTier: Int,
    val baseCostDiamonds: Int,
    val baseCostGold: Int,
    val iconEmoji: String
) {
    HEALTH("Алмазная Кольчуга", "+35 к Максимальному Здоровью (до 5000 ур.)", 5000, 5, 30, "💎"),
    DAMAGE("Платиновый Автотюн", "+12% к Урону всего Оружия (до 5000 ур.)", 5000, 6, 35, "🎙"),
    DASH("Неоновый Рывок", "-0.12с к перезарядке рывка (до 5000 ур.)", 5000, 8, 40, "⚡"),
    HYPE("Хайп-Турбо", "+20% к скорости заряда 6969 гиперкаров (до 5000 ур.)", 5000, 10, 50, "🏎"),
    GREED("Золотой Дождь", "+25% к добыче золота и алмазов (до 5000 ур.)", 5000, 7, 35, "💰"),
    VAMPIRISM("Гламурный Вампиризм", "+3% шанс вампиризма при ударе (до 5000 ур.)", 5000, 12, 60, "🍷"),
    ARMOR("Титановая Броня Влада", "+5% к Защите от урона (до 5000 ур.)", 5000, 10, 50, "🛡"),
    CRIT_CHANCE("Критический Флекс", "+2% Шанс Критического Урона (до 5000 ур.)", 5000, 12, 65, "🔥"),
    CRIT_DAMAGE("Сверхзвуковой Критудар", "+15% к Силе Критического Урона (до 5000 ур.)", 5000, 15, 75, "💥"),
    SPEED("Неоновые Кроссовки", "+6% Скорость Передвижения (до 5000 ур.)", 5000, 8, 40, "👟"),
    BOSS_SLAYER("Убийца Натуралов", "+10% Урона по Боссам Марса (до 5000 ур.)", 5000, 14, 70, "👑")
}

enum class QuestType {
    KILL_ENEMIES,
    KILL_BOSS,
    COLLECT_ARTIFACTS,
    COLLECT_BATTERIES,
    COMPLETE_ENDINGS
}

data class Quest(
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
    var isCompleted: Boolean = false,
    var isClaimed: Boolean = false
)

enum class CarUpgradeType(
    val title: String,
    val description: String,
    val iconEmoji: String,
    val maxLevel: Int = 1000
) {
    BULLBAR("Кенгурятник с дилдоками", "Золотой кенгурятник с розовыми шипами. Наносит сокрушительный таранный урон врагам.", "🍆"),
    SPOILER("Вибро-спойлер", "Антикрыло с высокочастотной вибрацией. Стабилизирует машину и ошеломляет врагов.", "⚡"),
    ROCKET("Ракетный двигатель", "Турбореактивная установка на метане Марса. Увеличивает скорость рывка гиперкара.", "🚀"),
    NITRO("Радужное нитро", "Шлейф из искр радужного пламени. Ослепляет врагов и оставляет огненный след.", "🌈"),
    ARMOR("Бриллиантовая броня", "Титаново-алмазная обшивка. Увеличивает время действия и прочность на поле боя.", "💎")
}

data class CarUpgrades(
    val bullbarLevel: Int = 0, // 0..1000
    val spoilerLevel: Int = 0, // 0..1000
    val rocketLevel: Int = 0,  // 0..1000
    val nitroLevel: Int = 0,   // 0..1000
    val armorLevel: Int = 0    // 0..1000
) {
    fun getTotalLevel(): Int = bullbarLevel + spoilerLevel + rocketLevel + nitroLevel + armorLevel

    fun getLevel(type: CarUpgradeType): Int = when (type) {
        CarUpgradeType.BULLBAR -> bullbarLevel
        CarUpgradeType.SPOILER -> spoilerLevel
        CarUpgradeType.ROCKET -> rocketLevel
        CarUpgradeType.NITRO -> nitroLevel
        CarUpgradeType.ARMOR -> armorLevel
    }

    fun getGoldCost(type: CarUpgradeType): Int {
        val lvl = getLevel(type)
        return 50 + (lvl * 15)
    }

    fun getBatteryCost(type: CarUpgradeType): Int {
        val lvl = getLevel(type)
        return 1 + (lvl / 20)
    }

    fun withIncrement(type: CarUpgradeType): CarUpgrades = when (type) {
        CarUpgradeType.BULLBAR -> copy(bullbarLevel = (bullbarLevel + 1).coerceAtMost(type.maxLevel))
        CarUpgradeType.SPOILER -> copy(spoilerLevel = (spoilerLevel + 1).coerceAtMost(type.maxLevel))
        CarUpgradeType.ROCKET -> copy(rocketLevel = (rocketLevel + 1).coerceAtMost(type.maxLevel))
        CarUpgradeType.NITRO -> copy(nitroLevel = (nitroLevel + 1).coerceAtMost(type.maxLevel))
        CarUpgradeType.ARMOR -> copy(armorLevel = (armorLevel + 1).coerceAtMost(type.maxLevel))
    }

    fun getBonusDamage(): Float = bullbarLevel * 8f
    fun getBonusSpeed(): Float = rocketLevel * 0.45f + nitroLevel * 0.35f
    fun getBonusLifetime(): Float = armorLevel * 0.003f
    fun getRamKnockback(): Float = 250f + bullbarLevel * 1.5f
}

data class CarCustomization(
    val pixels: List<Int>, // 32x32 = 1024 ARGB color ints, 0 = transparent
    val presetName: String = "Бирюзовый Влад"
) {
    companion object {
        const val CANVAS_SIZE = 32

        fun createEmpty(): CarCustomization {
            return CarCustomization(List(CANVAS_SIZE * CANVAS_SIZE) { 0 }, "Кастом")
        }

        fun createDefault(): CarCustomization {
            // Generates a 32x32 hypercar with turquoise body, tinted windshield, rainbow stripe, wheels
            val grid = IntArray(CANVAS_SIZE * CANVAS_SIZE) { 0 }
            val cCyan = 0xFF00E5FF.toInt()
            val cDarkCyan = 0xFF00838F.toInt()
            val cBlack = 0xFF1E293B.toInt()
            val cTire = 0xFF0F172A.toInt()
            val cRim = 0xFFFFD700.toInt()
            val cGlass = 0xFF80DEEA.toInt()
            val cGlow = 0xFFF43F5E.toInt()
            val rainbow = intArrayOf(
                0xFFFF0055.toInt(), 0xFFFF7F00.toInt(), 0xFFFFD700.toInt(),
                0xFF00FF66.toInt(), 0xFF00E5FF.toInt(), 0xFFB5179E.toInt()
            )

            // Car body footprint (facing right, centered)
            for (y in 10..22) {
                for (x in 4..27) {
                    // Aerodynamic car silhouette
                    if (y in 12..20) {
                        grid[y * CANVAS_SIZE + x] = cCyan
                    } else if (y in 10..11 && x in 10..21) {
                        // Cabin roof
                        grid[y * CANVAS_SIZE + x] = cGlass
                    } else if (y in 21..22 && x in 6..25) {
                        grid[y * CANVAS_SIZE + x] = cDarkCyan
                    }
                }
            }
            // Rainbow racing stripe down the center
            for (x in 5..26) {
                val rColor = rainbow[(x) % rainbow.size]
                grid[16 * CANVAS_SIZE + x] = rColor
            }
            // Windshield & Side Windows
            for (y in 11..13) {
                for (x in 16..21) {
                    grid[y * CANVAS_SIZE + x] = cGlass
                }
            }
            // Wheels
            val wheelPositions = listOf(Pair(7, 9), Pair(24, 9), Pair(7, 22), Pair(24, 22))
            for ((wx, wy) in wheelPositions) {
                for (dy in 0..2) {
                    for (dx in 0..2) {
                        val px = (wx + dx).coerceIn(0, 31)
                        val py = (wy + dy).coerceIn(0, 31)
                        grid[py * CANVAS_SIZE + px] = if (dx == 1 && dy == 1) cRim else cTire
                    }
                }
            }
            // Headlights & Taillights
            grid[13 * CANVAS_SIZE + 27] = 0xFFFFFBEB.toInt()
            grid[19 * CANVAS_SIZE + 27] = 0xFFFFFBEB.toInt()
            grid[13 * CANVAS_SIZE + 4] = cGlow
            grid[19 * CANVAS_SIZE + 4] = cGlow

            return CarCustomization(grid.toList(), "Бирюзовый Влад")
        }

        fun createGoldRolls(): CarCustomization {
            val grid = IntArray(CANVAS_SIZE * CANVAS_SIZE) { 0 }
            val cGold = 0xFFFFD700.toInt()
            val cDeepGold = 0xFFB45309.toInt()
            val cDark = 0xFF0F172A.toInt()
            val cWhite = 0xFFFFFFFF.toInt()
            val cGlass = 0xFF38BDF8.toInt()

            for (y in 11..21) {
                for (x in 4..27) {
                    grid[y * CANVAS_SIZE + x] = if (y in 13..19) cGold else cDeepGold
                }
            }
            for (y in 10..12) {
                for (x in 10..20) {
                    grid[y * CANVAS_SIZE + x] = cGlass
                }
            }
            // Diamond chrome trim
            for (x in 4..27) {
                grid[16 * CANVAS_SIZE + x] = cWhite
            }
            return CarCustomization(grid.toList(), "Золотой Роллс-Ройс")
        }

        fun createRainbowPride(): CarCustomization {
            val grid = IntArray(CANVAS_SIZE * CANVAS_SIZE) { 0 }
            val rainbow = intArrayOf(
                0xFFFF0055.toInt(), 0xFFFF7F00.toInt(), 0xFFFFD700.toInt(),
                0xFF00FF66.toInt(), 0xFF00E5FF.toInt(), 0xFFB5179E.toInt()
            )
            for (y in 11..21) {
                val c = rainbow[(y - 11) % rainbow.size]
                for (x in 5..26) {
                    grid[y * CANVAS_SIZE + x] = c
                }
            }
            return CarCustomization(grid.toList(), "Pride Гиперкар")
        }

        fun createCyberBatmobile(): CarCustomization {
            val grid = IntArray(CANVAS_SIZE * CANVAS_SIZE) { 0 }
            val cDark = 0xFF18181B.toInt()
            val cNeonPink = 0xFFFF007F.toInt()
            val cCyan = 0xFF06B6D4.toInt()
            for (y in 11..21) {
                for (x in 4..27) {
                    grid[y * CANVAS_SIZE + x] = cDark
                }
            }
            for (x in 4..27) {
                grid[12 * CANVAS_SIZE + x] = cNeonPink
                grid[20 * CANVAS_SIZE + x] = cCyan
            }
            return CarCustomization(grid.toList(), "Кибер-Бэтмобиль")
        }
    }
}

data class CurrencyDelta(
    val id: String = UUID.randomUUID().toString(),
    val amount: Int,
    val type: String, // "GOLD", "DIAMONDS", "BATTERIES"
    val timestamp: Long = System.currentTimeMillis()
)

