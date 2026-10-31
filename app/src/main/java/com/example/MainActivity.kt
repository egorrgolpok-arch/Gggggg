package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.example.game.DialogueModal
import com.example.game.EndingsArchiveScreen
import com.example.game.GameScreen
import com.example.game.GameViewModel
import com.example.game.GameWorldScreen
import com.example.game.HubScreen
import com.example.game.MainMenuScreen
import com.example.game.QuestLogModal
import com.example.game.StudioSplashScreen
import com.example.game.TalentTreeModal
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.SpaceDark

class MainActivity : ComponentActivity() {

    private val gameViewModel: GameViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        gameViewModel.initContext(this)

        setContent {
            MyApplicationTheme {
                val state by gameViewModel.state.collectAsState()

                BackHandler(enabled = state.screen != GameScreen.MAIN_MENU && state.screen != GameScreen.SPLASH) {
                    when (state.screen) {
                        GameScreen.TALENT_TREE -> gameViewModel.closeTalentTree()
                        GameScreen.QUEST_LOG -> gameViewModel.closeQuestLog()
                        GameScreen.VIP_HUB -> gameViewModel.splashToMainMenu()
                        GameScreen.INVENTORY -> gameViewModel.closeInventory()
                        GameScreen.DIALOGUE -> gameViewModel.closeDialogue()
                        GameScreen.ARTIFACT_INSPECT -> gameViewModel.closeArtifactInspect()
                        GameScreen.ENDINGS_ARCHIVE -> gameViewModel.closeEndingsArchive()
                        GameScreen.PLAYING -> {
                            gameViewModel.saveCurrentGame()
                            gameViewModel.openHub()
                        }
                        else -> gameViewModel.splashToMainMenu()
                    }
                }

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = SpaceDark
                ) {
                    when (state.screen) {
                        GameScreen.SPLASH -> {
                            StudioSplashScreen(
                                onStartGame = { gameViewModel.splashToMainMenu() }
                            )
                        }
                        GameScreen.MAIN_MENU -> {
                            MainMenuScreen(
                                viewModel = gameViewModel,
                                state = state
                            )
                        }
                        GameScreen.ENDINGS_ARCHIVE -> {
                            EndingsArchiveScreen(
                                unlockedEndingIds = state.unlockedEndings,
                                onBack = { gameViewModel.closeEndingsArchive() }
                            )
                        }
                        GameScreen.VIP_HUB, GameScreen.TALENT_TREE, GameScreen.QUEST_LOG -> {
                            Box(modifier = Modifier.fillMaxSize()) {
                                HubScreen(
                                    state = state,
                                    onStartRaid = { gameViewModel.startRaidFromHub() },
                                    onOpenTalents = { gameViewModel.openTalentTree() },
                                    onOpenQuests = { gameViewModel.openQuestLog() },
                                    onOpenEndings = { gameViewModel.openEndingsArchive() },
                                    onTalkToNpc = { npc -> gameViewModel.openDialogueWith(npc) },
                                    onHitTrainingDummy = { dmg -> gameViewModel.hitTrainingDummy(dmg) },
                                    onGenerateNogAi = { isWeapon -> gameViewModel.generateNogAiItem(isWeapon) },
                                    onSearchNpc = { query -> gameViewModel.setNpcSearchQuery(query) },
                                    onDismissNogAiResult = { gameViewModel.dismissNogAiResult() },
                                    onRejectNogAi = { gameViewModel.rejectNogAiItem() },
                                    onMainMenu = {
                                        gameViewModel.saveCurrentGame()
                                        gameViewModel.splashToMainMenu()
                                    }
                                )

                                if (state.screen == GameScreen.TALENT_TREE) {
                                    TalentTreeModal(
                                        talents = state.talents,
                                        diamonds = state.player.diamonds,
                                        gold = state.player.gold,
                                        onUpgradeTalent = { talentType -> gameViewModel.upgradeTalent(talentType) },
                                        onDismiss = { gameViewModel.closeTalentTree() }
                                    )
                                }

                                if (state.screen == GameScreen.QUEST_LOG) {
                                    QuestLogModal(
                                        quests = state.quests,
                                        onClaimQuest = { quest -> gameViewModel.claimQuestReward(quest) },
                                        onDismiss = { gameViewModel.closeQuestLog() }
                                    )
                                }

                                if (state.activeDialogueNPC != null) {
                                    DialogueModal(
                                        npc = state.activeDialogueNPC!!,
                                        nodeIndex = state.currentDialogueNodeIndex,
                                        onSelectOption = { option -> gameViewModel.selectDialogueOption(option) },
                                        onClose = { gameViewModel.closeDialogue() }
                                    )
                                }
                            }
                        }
                        else -> {
                            GameWorldScreen(viewModel = gameViewModel)
                        }
                    }
                }
            }
        }
    }
}
