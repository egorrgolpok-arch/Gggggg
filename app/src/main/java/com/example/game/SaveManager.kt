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
