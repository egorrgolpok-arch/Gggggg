package com.example.game

import android.content.Context
import android.content.SharedPreferences

object SaveManager {

    private const val PREF_NAME = "mars_world_save"
    private const val KEY_HAS_SAVE = "has_save"
    private const val KEY_FLOOR = "player_floor"
    private const val KEY_GOLD = "player_gold"
    private const val KEY_DIAMONDS = "player_diamonds"
    private const val KEY_BATTERIES = "player_batteries"
    private const val KEY_KILLS = "player_kills"
    private const val KEY_DIFFICULTY = "game_difficulty"
    private const val KEY_UNLOCKED_ENDINGS = "unlocked_endings"

    private const val KEY_TALENT_HEALTH = "talent_health"
    private const val KEY_TALENT_DAMAGE = "talent_damage"
    private const val KEY_TALENT_DASH = "talent_dash"
    private const val KEY_TALENT_HYPE = "talent_hype"
    private const val KEY_TALENT_GREED = "talent_greed"
    private const val KEY_TALENT_VAMPIRISM = "talent_vampirism"
    private const val KEY_CLAIMED_QUESTS = "claimed_quests"

    private fun getPrefs(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    fun hasSavedGame(context: Context): Boolean {
        return getPrefs(context).getBoolean(KEY_HAS_SAVE, false)
    }

    fun saveGame(
        context: Context,
        player: Player,
        difficulty: GameDifficulty,
        unlockedEndingIds: Set<Int>
    ) {
        val prefs = getPrefs(context)
        prefs.edit()
            .putBoolean(KEY_HAS_SAVE, true)
            .putInt(KEY_FLOOR, player.floor)
            .putInt(KEY_GOLD, player.gold)
            .putInt(KEY_DIAMONDS, player.diamonds)
            .putInt(KEY_BATTERIES, player.batteries)
            .putInt(KEY_KILLS, player.kills)
            .putString(KEY_DIFFICULTY, difficulty.name)
            .putStringSet(KEY_UNLOCKED_ENDINGS, unlockedEndingIds.map { it.toString() }.toSet())
            .apply()
    }

    fun loadSavedGame(context: Context): SavedGameData? {
        val prefs = getPrefs(context)
        if (!prefs.getBoolean(KEY_HAS_SAVE, false)) return null

        val floor = prefs.getInt(KEY_FLOOR, 1)
        val gold = prefs.getInt(KEY_GOLD, 100)
        val diamonds = prefs.getInt(KEY_DIAMONDS, 10)
        val batteries = prefs.getInt(KEY_BATTERIES, 5)
        val kills = prefs.getInt(KEY_KILLS, 0)
        val diffName = prefs.getString(KEY_DIFFICULTY, GameDifficulty.NORMAL.name)
        val difficulty = try {
            GameDifficulty.valueOf(diffName ?: GameDifficulty.NORMAL.name)
        } catch (_: Exception) {
            GameDifficulty.NORMAL
        }
        val endingsStrings = prefs.getStringSet(KEY_UNLOCKED_ENDINGS, emptySet()) ?: emptySet()
        val unlockedEndings = endingsStrings.mapNotNull { it.toIntOrNull() }.toSet()

        return SavedGameData(
            floor = floor,
            gold = gold,
            diamonds = diamonds,
            batteries = batteries,
            kills = kills,
            difficulty = difficulty,
            unlockedEndings = unlockedEndings
        )
    }

    fun clearSavedGame(context: Context) {
        getPrefs(context).edit().remove(KEY_HAS_SAVE).apply()
    }

    fun saveUnlockedEnding(context: Context, endingId: Int) {
        val prefs = getPrefs(context)
        val current = prefs.getStringSet(KEY_UNLOCKED_ENDINGS, emptySet())?.toMutableSet() ?: mutableSetOf()
        current.add(endingId.toString())
        prefs.edit().putStringSet(KEY_UNLOCKED_ENDINGS, current).apply()
    }

    fun getUnlockedEndings(context: Context): Set<Int> {
        val prefs = getPrefs(context)
        val set = prefs.getStringSet(KEY_UNLOCKED_ENDINGS, emptySet()) ?: emptySet()
        return set.mapNotNull { it.toIntOrNull() }.toSet()
    }

    fun saveTalents(context: Context, talents: MetaprogressionTalents) {
        val prefs = getPrefs(context)
        prefs.edit()
            .putInt(KEY_TALENT_HEALTH, talents.healthTier)
            .putInt(KEY_TALENT_DAMAGE, talents.damageTier)
            .putInt(KEY_TALENT_DASH, talents.dashCooldownTier)
            .putInt(KEY_TALENT_HYPE, talents.hypeRateTier)
            .putInt(KEY_TALENT_GREED, talents.greedTier)
            .putInt(KEY_TALENT_VAMPIRISM, talents.vampirismTier)
            .apply()
    }

    fun loadTalents(context: Context): MetaprogressionTalents {
        val prefs = getPrefs(context)
        return MetaprogressionTalents(
            healthTier = prefs.getInt(KEY_TALENT_HEALTH, 0),
            damageTier = prefs.getInt(KEY_TALENT_DAMAGE, 0),
            dashCooldownTier = prefs.getInt(KEY_TALENT_DASH, 0),
            hypeRateTier = prefs.getInt(KEY_TALENT_HYPE, 0),
            greedTier = prefs.getInt(KEY_TALENT_GREED, 0),
            vampirismTier = prefs.getInt(KEY_TALENT_VAMPIRISM, 0)
        )
    }

    fun saveClaimedQuests(context: Context, questIds: Set<String>) {
        val prefs = getPrefs(context)
        prefs.edit().putStringSet(KEY_CLAIMED_QUESTS, questIds).apply()
    }

    fun getClaimedQuests(context: Context): Set<String> {
        val prefs = getPrefs(context)
        return prefs.getStringSet(KEY_CLAIMED_QUESTS, emptySet()) ?: emptySet()
    }

    private const val KEY_CAR_BULLBAR = "car_bullbar"
    private const val KEY_CAR_SPOILER = "car_spoiler"
    private const val KEY_CAR_ROCKET = "car_rocket"
    private const val KEY_CAR_NITRO = "car_nitro"
    private const val KEY_CAR_ARMOR = "car_armor"
    private const val KEY_CAR_PIXELS = "car_pixels"
    private const val KEY_CAR_PRESET_NAME = "car_preset_name"

    fun saveCarUpgrades(context: Context, upgrades: CarUpgrades) {
        val prefs = getPrefs(context)
        prefs.edit()
            .putInt(KEY_CAR_BULLBAR, upgrades.bullbarLevel)
            .putInt(KEY_CAR_SPOILER, upgrades.spoilerLevel)
            .putInt(KEY_CAR_ROCKET, upgrades.rocketLevel)
            .putInt(KEY_CAR_NITRO, upgrades.nitroLevel)
            .putInt(KEY_CAR_ARMOR, upgrades.armorLevel)
            .apply()
    }

    fun loadCarUpgrades(context: Context): CarUpgrades {
        val prefs = getPrefs(context)
        return CarUpgrades(
            bullbarLevel = prefs.getInt(KEY_CAR_BULLBAR, 0),
            spoilerLevel = prefs.getInt(KEY_CAR_SPOILER, 0),
            rocketLevel = prefs.getInt(KEY_CAR_ROCKET, 0),
            nitroLevel = prefs.getInt(KEY_CAR_NITRO, 0),
            armorLevel = prefs.getInt(KEY_CAR_ARMOR, 0)
        )
    }

    fun saveCarCustomization(context: Context, customization: CarCustomization) {
        val prefs = getPrefs(context)
        val sb = StringBuilder()
        for (i in customization.pixels.indices) {
            sb.append(customization.pixels[i].toUInt().toString(16))
            if (i < customization.pixels.size - 1) sb.append(",")
        }
        prefs.edit()
            .putString(KEY_CAR_PIXELS, sb.toString())
            .putString(KEY_CAR_PRESET_NAME, customization.presetName)
            .apply()
    }

    fun loadCarCustomization(context: Context): CarCustomization {
        val prefs = getPrefs(context)
        val pixelStr = prefs.getString(KEY_CAR_PIXELS, null) ?: return CarCustomization.createDefault()
        val name = prefs.getString(KEY_CAR_PRESET_NAME, "Кастом") ?: "Кастом"
        return try {
            val parts = pixelStr.split(",")
            if (parts.size == CarCustomization.CANVAS_SIZE * CarCustomization.CANVAS_SIZE) {
                val list = parts.map { it.toUInt(16).toInt() }
                CarCustomization(list, name)
            } else {
                CarCustomization.createDefault()
            }
        } catch (_: Exception) {
            CarCustomization.createDefault()
        }
    }

    private const val KEY_ACTIVE_SKIN = "active_skin"

    fun saveActiveSkin(context: Context, skin: VladSkin) {
        getPrefs(context).edit().putString(KEY_ACTIVE_SKIN, skin.name).apply()
    }

    fun loadActiveSkin(context: Context): VladSkin {
        val name = getPrefs(context).getString(KEY_ACTIVE_SKIN, VladSkin.GAY_CAR.name)
        return try {
            VladSkin.valueOf(name ?: VladSkin.GAY_CAR.name)
        } catch (_: Exception) {
            VladSkin.GAY_CAR
        }
    }
}

data class SavedGameData(
    val floor: Int,
    val gold: Int,
    val diamonds: Int,
    val batteries: Int,
    val kills: Int,
    val difficulty: GameDifficulty,
    val unlockedEndings: Set<Int>
)
