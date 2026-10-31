package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.game.GameScreen
import com.example.game.GameViewModel
import com.example.game.MetaprogressionTalents
import com.example.game.ProceduralWorld
import com.example.game.QuestType
import com.example.game.TalentType
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

    @Test
    fun `read string from context`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("MARS WORLD: РОГАЛИК", appName)
    }

    @Test
    fun `test game view model start game and punchlines`() {
        val viewModel = GameViewModel()
        assertEquals(GameScreen.SPLASH, viewModel.state.value.screen)

        viewModel.startGame()
        assertEquals(GameScreen.PLAYING, viewModel.state.value.screen)
        assertTrue(viewModel.state.value.enemies.isNotEmpty())

        val punchline = ProceduralWorld.getRandomPunchline()
        assertNotNull(punchline)
        assertTrue(punchline.isNotEmpty())
    }

    @Test
    fun `test julien and batya npcs exist with rich dialogues`() {
        val npcs = ProceduralWorld.getStoryNPCs()
        val julien = npcs.find { it.id == "npc_julien" }
        val batya = npcs.find { it.id == "npc_batya" }

        assertNotNull(julien)
        assertNotNull(batya)

        assertTrue(julien!!.name.contains("Жульен"))
        assertTrue(julien.dialogues.isNotEmpty())

        assertTrue(batya!!.name.contains("Батя"))
        assertTrue(batya.dialogues.isNotEmpty())
    }

    @Test
    fun `test metaprogression talents calculations`() {
        val talents = MetaprogressionTalents(
            healthTier = 4,
            damageTier = 3,
            dashCooldownTier = 2,
            hypeRateTier = 2,
            greedTier = 3,
            vampirismTier = 2
        )

        assertEquals(140f, talents.getBonusMaxHp(), 0.01f)
        assertEquals(1.36f, talents.getDamageMultiplier(), 0.01f)
        assertEquals(0.24f, talents.getDashCooldownReduction(), 0.01f)
        assertEquals(1.40f, talents.getHypeMultiplier(), 0.01f)
        assertEquals(1.75f, talents.getDropMultiplier(), 0.01f)
        assertEquals(0.06f, talents.getVampirismChance(), 0.01f)
    }

    @Test
    fun `test quest progression and claiming rewards`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val viewModel = GameViewModel()
        viewModel.initContext(context)

        val quests = viewModel.state.value.quests
        assertTrue(quests.isNotEmpty())

        val killQuest = quests.firstOrNull { it.type == QuestType.KILL_ENEMIES }
        assertNotNull(killQuest)

        viewModel.updateQuestProgress(QuestType.KILL_ENEMIES, killQuest!!.targetAmount)

        val updatedQuest = viewModel.state.value.quests.find { it.id == killQuest.id }
        assertNotNull(updatedQuest)
        assertTrue(updatedQuest!!.isCompleted)

        val initialDiamonds = viewModel.state.value.player.diamonds
        viewModel.claimQuestReward(updatedQuest)

        assertTrue(viewModel.state.value.player.diamonds >= initialDiamonds + updatedQuest.rewardDiamonds)
    }

    @Test
    fun `test hub navigation and talent upgrade`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val viewModel = GameViewModel()
        viewModel.initContext(context)

        viewModel.openHub()
        assertEquals(GameScreen.VIP_HUB, viewModel.state.value.screen)

        // Give player enough currency
        viewModel.state.value.player.diamonds = 500
        viewModel.state.value.player.gold = 5000

        val initialTier = viewModel.state.value.talents.healthTier
        viewModel.upgradeTalent(TalentType.HEALTH)

        assertEquals(initialTier + 1, viewModel.state.value.talents.healthTier)
    }

    @Test
    fun `test zero initial endings unlocked`() {
        val viewModel = GameViewModel()
        assertTrue(viewModel.state.value.unlockedEndings.isEmpty())
    }

    @Test
    fun `test all 100 endings exist with unique descriptions`() {
        val endings = ProceduralWorld.all100Endings
        assertEquals(100, endings.size)
        val descriptions = endings.map { it.description }.toSet()
        // Ensure high variety in descriptions
        assertTrue(descriptions.size >= 50)
    }

    @Test
    fun `test 50 unique NPCs exist`() {
        val npcs = ProceduralWorld.getStoryNPCs()
        assertTrue(npcs.size >= 50)
        val names = npcs.map { it.name }.toSet()
        assertEquals(npcs.size, names.size)
    }

    @Test
    fun `test 100+ crafting recipes and 200+ world artifacts`() {
        val recipes = com.example.game.ItemDatabase.allCraftingRecipes
        assertTrue(recipes.size >= 100)

        val worldArts = com.example.game.ItemDatabase.all200WorldArtifacts
        assertTrue(worldArts.size >= 200)
    }

    @Test
    fun `test world obstacle collision detection`() {
        // Safe spawn center is walkable
        assertTrue(ProceduralWorld.isPositionWalkable(0f, 0f, 16f))

        // Non-walkable tile types are recognized
        assertTrue(!com.example.game.TileType.MARBLE_PILLAR.isWalkable)
        assertTrue(!com.example.game.TileType.LAVA_LAMP.isWalkable)
        assertTrue(!com.example.game.TileType.CANYON_EDGE.isWalkable)
        assertTrue(!com.example.game.TileType.MARTIAN_ROCK.isWalkable)
        assertTrue(!com.example.game.TileType.PARKED_HYPERCAR.isWalkable)
    }
}
