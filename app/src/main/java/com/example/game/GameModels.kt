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
    VICTORY
}

enum class GameDifficulty(
    val title: String,
    val description: String,
    val enemyHpMultiplier: Float,
    val scoreMultiplier: Float
) {
    NORMAL("Нормальный Хайп", "Сбалансированное приключение на Марсе", 1.0f, 1.0f),
    HARD("Сверхтяжелый Марс", "Очень сложный режим: враги в 3.5х сильнее и быстрее!", 3.5f, 3.0f),
    EXTREME("Экстремальный Марс", "Усиленные роботы-цензоры и качки-рейдеры", 1.4f, 1.5f),
    VIP_FLEX("VIP Флекс-Режим", "Владик получает +50% ресурсов и щит", 0.85f, 1.2f)
}

enum class BiomeType(val displayName: String, val baseColor: Color) {
    PENTHOUSE("Неоновый Пентхаус", Color(0xFF2D124D)),
    WASTELAND("Красная Пустыня Марса", Color(0xFF6B200B)),
    GARAGE("Гараж 6969 Суперкаров", Color(0xFF141926)),
    CRYO_LOUNGE("VIP Крио-Лаундж", Color(0xFF0F3B4A))
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
    PLATINUM_BLASTER
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
    val id: String = UUID.randomUUID().toString()
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
    PALACE_SUMMIT("Золотой Пик Марса", Color(0xFF2E1065), Color(0xFFE040FB));

    companion object {
        fun getForFloor(floor: Int): FloorBiome {
            return when {
                floor <= 50 -> PENTHOUSE
                floor <= 150 -> CYBER_GARAGE
                floor <= 300 -> MARTIAN_CANYON
                floor <= 500 -> LAVA_MATRIX
                else -> PALACE_SUMMIT
            }
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
    val inventoryWeapons: MutableList<Weapon> = mutableListOf(),
    val equippedArtifacts: MutableList<Artifact> = mutableListOf(),
    var activeCompanion: ActiveCompanion? = null,
    var isGodModeVlad: Boolean = false,
    var isMoving: Boolean = false,
    var walkCycleTimer: Float = 0f,
    var speechText: String? = null,
    var speechTimer: Float = 0f
)

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
