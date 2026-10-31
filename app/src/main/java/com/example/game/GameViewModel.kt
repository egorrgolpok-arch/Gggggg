package com.example.game

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt
import kotlin.random.Random

data class GameState(
    val screen: GameScreen = GameScreen.SPLASH,
    val difficulty: GameDifficulty = GameDifficulty.NORMAL,
    val player: Player = Player(
        equippedWeapon = ProceduralWorld.generateRandomWeapon(1)
    ),
    val enemies: List<Enemy> = emptyList(),
    val npcs: List<FriendlyNPC> = ProceduralWorld.getStoryNPCs(),
    val worldArtifacts: List<WorldArtifact> = ProceduralWorld.generateWorldArtifactsForFloor(1),
    val activeDialogueNPC: FriendlyNPC? = null,
    val currentDialogueNodeIndex: Int = 0,
    val inspectingArtifact: WorldArtifact? = null,
    val projectiles: List<Projectile> = emptyList(),
    val particles: List<Particle> = emptyList(),
    val damagePopups: List<DamagePopup> = emptyList(),
    val droppedItems: List<DroppedItem> = emptyList(),
    val hypercars: List<HypercarSummon> = emptyList(),
    val timeOfDay: TimeOfDay = TimeOfDay.DAY,
    val dayNightTimer: Float = 0f,
    val screenShake: Float = 0f,
    val bossActive: Boolean = false,
    val showSpeechBubble: Boolean = false,
    val currentPunchline: String = "",
    val availableRecipes: List<CraftingRecipe> = ProceduralWorld.defaultRecipes,
    val unlockedEndings: Set<Int> = emptySet(),
    val hasSavedGame: Boolean = false,
    val showNewGameDialog: Boolean = false,
    val talents: MetaprogressionTalents = MetaprogressionTalents(),
    val quests: List<Quest> = ProceduralWorld.getDefaultQuests(),
    val floorWaveTarget: Int = 10,
    val floorEnemiesDefeated: Int = 0,
    val isGeneratingNogAi: Boolean = false,
    val nogAiProgress: Float = 0f,
    val generatedNogAiWeapon: Weapon? = null,
    val generatedNogAiArtifact: Artifact? = null,
    val isTransitioningFloor: Boolean = false,
    val transitionFloorNumber: Int = 1,
    val npcSearchQuery: String = "",
    val bossSummonedOnFloor: Boolean = false
)

class GameViewModel : ViewModel() {

    private val _state = MutableStateFlow(GameState())
    val state: StateFlow<GameState> = _state.asStateFlow()

    private var joystickX = 0f
    private var joystickY = 0f
    private var gameTimeSec = 0f
    private var enemySpawnTimer = 0f
    private var speechCooldown = 0f
    private var vibrator: Vibrator? = null
    private var appContext: Context? = null

    init {
        val initialWeapon = Weapon(
            type = WeaponType.RUBBER_BAT,
            name = "Резиновая Булава Хайпа",
            description = "Фирменное розовое оружие Владика, оглушающее завистников.",
            damage = 50f,
            attackSpeed = 2.4f,
            range = 90f,
            knockback = 200f,
            color = Color(0xFFFF1493),
            rarity = ItemRarity.RARE
        )
        val initialArtifact = Artifact(
            name = "Радужная Бриллиантовая Цепь",
            description = "Увеличивает макс. HP и скорость атаки.",
            rarity = ItemRarity.EPIC,
            bonusHp = 50f,
            bonusSpeed = 20f,
            bonusDamage = 10f,
            bonusHypeRate = 0.4f,
            loreStory = "Сверкающая цепь из чистейших бриллиантов, излучающая волны неонового хайпа."
        )
        _state.update {
            it.copy(
                player = it.player.copy(
                    equippedWeapon = initialWeapon,
                    inventoryWeapons = mutableListOf(initialWeapon),
                    equippedArtifacts = mutableListOf(initialArtifact)
                )
            )
        }
    }

    fun initContext(context: Context) {
        appContext = context
        vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
            vibratorManager?.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
        }

        val hasSave = SaveManager.hasSavedGame(context)
        val savedEndings = SaveManager.getUnlockedEndings(context)
        val mergedEndings = savedEndings
        val loadedTalents = SaveManager.loadTalents(context)
        val claimedQuestIds = SaveManager.getClaimedQuests(context)

        val updatedQuests = _state.value.quests.map { q ->
            if (claimedQuestIds.contains(q.id)) {
                q.copy(isCompleted = true, isClaimed = true, currentProgress = q.targetAmount)
            } else {
                q
            }
        }

        val baseMaxHp = 250f + loadedTalents.getBonusMaxHp()

        _state.update {
            it.copy(
                hasSavedGame = hasSave,
                unlockedEndings = mergedEndings,
                talents = loadedTalents,
                quests = updatedQuests,
                player = it.player.copy(
                    maxHp = baseMaxHp,
                    hp = baseMaxHp.coerceAtLeast(it.player.hp)
                )
            )
        }
    }

    private fun vibrate(durationMs: Long) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator?.vibrate(VibrationEffect.createOneShot(durationMs, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                @Suppress("DEPRECATION")
                vibrator?.vibrate(durationMs)
            }
        } catch (_: Exception) {}
    }

    fun splashToMainMenu() {
        _state.update { it.copy(screen = GameScreen.MAIN_MENU) }
    }

    fun showNewGameDialog(show: Boolean) {
        _state.update { it.copy(showNewGameDialog = show) }
    }

    fun startGame() = startNewGame(GameDifficulty.NORMAL)

    fun birthGodVlad() {
        val godWeapon = Weapon(
            type = WeaponType.PLATINUM_BLASTER,
            name = "👑 ВЕНЕЦ БОГА МАРСА - ВЛАДИК",
            description = "Абсолютное божественное оружие, испускающее платиновый лазер смерти.",
            damage = 9999f,
            attackSpeed = 5.0f,
            range = 800f,
            knockback = 500f,
            color = Color(0xFFFFD700),
            rarity = ItemRarity.LEGENDARY
        )
        _state.update {
            it.copy(
                screen = GameScreen.PLAYING,
                difficulty = GameDifficulty.NORMAL,
                showNewGameDialog = false,
                player = Player(
                    hp = 10000f,
                    maxHp = 10000f,
                    gold = 99999,
                    diamonds = 9999,
                    batteries = 999,
                    floor = 1,
                    isGodModeVlad = true,
                    equippedWeapon = godWeapon,
                    inventoryWeapons = mutableListOf(godWeapon)
                ),
                enemies = emptyList(),
                npcs = emptyList(),
                worldArtifacts = ProceduralWorld.generateWorldArtifactsForFloor(1),
                currentPunchline = "👑 РОЖДЕНИЕ ГЛАВНОГО ВЛАДА! БОЖЕСТВЕННЫЙ ФЛЕКС НА ВСЕ 800 ЭТАЖЕЙ!",
                showSpeechBubble = true
            )
        }
        speechCooldown = 5f
        spawnEnemiesForWave(8)
    }

    fun startNewGame(difficulty: GameDifficulty, starterBonus: Boolean = false) {
        val isHard = (difficulty == GameDifficulty.HARD)
        val initialHp = if (isHard) 180f else (250f + (if (starterBonus) 50f else 0f))

        val initialWeapon = Weapon(
            type = if (starterBonus) WeaponType.PLATINUM_BLASTER else WeaponType.RUBBER_BAT,
            name = if (starterBonus) "Платиновый Бластер Басов" else "Резиновая Булава Хайпа",
            description = if (starterBonus) "Ударные сверхзвуковые частоты сжигают микросхемы цензоров." else "Фирменное розовое оружие Владика, оглушающее завистников.",
            damage = if (starterBonus) 90f else 50f,
            attackSpeed = 2.5f,
            range = if (starterBonus) 300f else 90f,
            knockback = 180f,
            color = if (starterBonus) Color(0xFFE2E8F0) else Color(0xFFFF1493),
            rarity = if (starterBonus) ItemRarity.LEGENDARY else ItemRarity.RARE
        )
        val initialArtifact = Artifact(
            name = "Радужная Бриллиантовая Цепь",
            description = "Увеличивает макс. HP и скорость атаки.",
            rarity = ItemRarity.EPIC,
            bonusHp = if (starterBonus) 80f else 50f,
            bonusSpeed = 20f,
            bonusDamage = if (starterBonus) 18f else 10f,
            bonusHypeRate = 0.5f,
            loreStory = "Символ открытости, уверенности и абсолютного превосходства Владика."
        )

        _state.update {
            it.copy(
                screen = GameScreen.PLAYING,
                difficulty = difficulty,
                showNewGameDialog = false,
                player = Player(
                    hp = initialHp,
                    maxHp = initialHp,
                    gold = if (starterBonus) 250 else 100,
                    diamonds = if (starterBonus) 20 else 10,
                    batteries = if (starterBonus) 10 else 5,
                    floor = 1,
                    equippedWeapon = initialWeapon,
                    inventoryWeapons = mutableListOf(initialWeapon),
                    equippedArtifacts = mutableListOf(initialArtifact)
                ),
                enemies = emptyList(),
                npcs = emptyList(),
                worldArtifacts = ProceduralWorld.generateWorldArtifactsForFloor(1),
                currentPunchline = if (isHard) "⚠️ СЛОЖНЫЙ РЕЖИМ ВКЛЮЧЕН! ВРАГИ В 3.5Х СИЛЬНЕЕ!" else "Добро пожаловать в мой 800-этажный пентхаус на Марсе! Пошумим!",
                showSpeechBubble = true
            )
        }
        speechCooldown = 4f
        spawnEnemiesForWave(5)
        saveCurrentGame()
    }

    fun continueSavedGame() {
        val ctx = appContext ?: return
        val saved = SaveManager.loadSavedGame(ctx) ?: return

        val initialWeapon = Weapon(
            type = WeaponType.RUBBER_BAT,
            name = "Резиновая Булава Хайпа",
            description = "Фирменное розовое оружие Владика.",
            damage = 50f + saved.floor * 10f,
            attackSpeed = 2.4f,
            range = 90f,
            knockback = 200f,
            color = Color(0xFFFF1493),
            rarity = ItemRarity.RARE
        )

        _state.update {
            it.copy(
                screen = GameScreen.PLAYING,
                difficulty = saved.difficulty,
                unlockedEndings = saved.unlockedEndings,
                player = it.player.copy(
                    floor = saved.floor,
                    gold = saved.gold,
                    diamonds = saved.diamonds,
                    batteries = saved.batteries,
                    kills = saved.kills,
                    equippedWeapon = initialWeapon
                ),
                enemies = emptyList(),
                worldArtifacts = ProceduralWorld.generateWorldArtifactsForFloor(saved.floor),
                npcs = ProceduralWorld.getStoryNPCs(),
                currentPunchline = "С возвращением на Марс! Этаж ${saved.floor} ждет флекса!",
                showSpeechBubble = true
            )
        }
        speechCooldown = 4f
        spawnEnemiesForWave(5)
    }

    fun saveCurrentGame() {
        val ctx = appContext ?: return
        val s = _state.value
        SaveManager.saveGame(ctx, s.player, s.difficulty, s.unlockedEndings)
        _state.update { it.copy(hasSavedGame = true) }
    }

    fun openEndingsArchive() {
        _state.update { it.copy(screen = GameScreen.ENDINGS_ARCHIVE) }
    }

    fun closeEndingsArchive() {
        _state.update { it.copy(screen = if (it.hasSavedGame) GameScreen.MAIN_MENU else GameScreen.MAIN_MENU) }
    }

    fun setJoystickInput(x: Float, y: Float) {
        joystickX = x.coerceIn(-1f, 1f)
        joystickY = y.coerceIn(-1f, 1f)
    }

    fun performDash() {
        val s = _state.value
        val p = s.player
        if (p.dashCooldown <= 0f) {
            vibrate(30)
            val moveAngle = if (joystickX != 0f || joystickY != 0f) {
                atan2(joystickY, joystickX)
            } else {
                p.facingAngle
            }
            p.isDashing = true
            p.dashTimer = 0.22f
            p.dashCooldown = 1.2f
            p.facingAngle = moveAngle

            val trailParticles = (0..12).map {
                Particle(
                    x = p.x + (Random.nextFloat() - 0.5f) * 20f,
                    y = p.y + (Random.nextFloat() - 0.5f) * 20f,
                    vx = -cos(moveAngle) * 60f + (Random.nextFloat() - 0.5f) * 40f,
                    vy = -sin(moveAngle) * 60f + (Random.nextFloat() - 0.5f) * 40f,
                    color = Color(0xFFFF007F),
                    size = 6f + Random.nextFloat() * 4f,
                    lifetime = 0.35f,
                    maxLifetime = 0.35f
                )
            }

            _state.update {
                it.copy(
                    particles = it.particles + trailParticles,
                    screenShake = 4f
                )
            }
        }
    }

    fun performAttack() {
        val s = _state.value
        val p = s.player
        val w = p.equippedWeapon
        val angle = p.facingAngle

        vibrate(20)

        var totalDamage = w.damage
        p.equippedArtifacts.forEach { totalDamage += it.bonusDamage }

        when (w.type) {
            WeaponType.RUBBER_BAT -> {
                val hitEnemies = mutableListOf<String>()
                val newParticles = mutableListOf<Particle>()
                val newPopups = mutableListOf<DamagePopup>()
                val updatedEnemies = s.enemies.map { enemy ->
                    val dx = enemy.x - p.x
                    val dy = enemy.y - p.y
                    val dist = sqrt(dx * dx + dy * dy)
                    if (dist < w.range + enemy.radius) {
                        val enemyAngle = atan2(dy, dx)
                        var angleDiff = kotlin.math.abs(enemyAngle - angle)
                        if (angleDiff > Math.PI) angleDiff = (2 * Math.PI - angleDiff).toFloat()
                        if (angleDiff < 1.3f) {
                            hitEnemies.add(enemy.id)
                            val toPlayerAngle = atan2(p.y - enemy.y, p.x - enemy.x)
                            var shieldDiff = kotlin.math.abs(toPlayerAngle - enemy.facingAngle)
                            if (shieldDiff > Math.PI) shieldDiff = (2 * Math.PI - shieldDiff).toFloat()
                            val isBlocked = enemy.hasShield && shieldDiff < 1.1f

                            if (isBlocked) {
                                newPopups.add(
                                    DamagePopup(enemy.x, enemy.y - 20f, "ЩИТ БЛОК! ⚡", Color(0xFF00F0FF))
                                )
                                for (i in 0..3) {
                                    newParticles.add(Particle(enemy.x, enemy.y, (Random.nextFloat() - 0.5f) * 80f, (Random.nextFloat() - 0.5f) * 80f, Color(0xFF00F0FF), 5f, 0.25f, 0.25f))
                                }
                                enemy
                            } else {
                                val remainingHp = enemy.hp - totalDamage
                                enemy.x += cos(angle) * w.knockback * 0.4f
                                enemy.y += sin(angle) * w.knockback * 0.4f
                                newPopups.add(
                                    DamagePopup(
                                        x = enemy.x,
                                        y = enemy.y - 20f,
                                        text = "ПИСК! -${totalDamage.toInt()}",
                                        color = Color(0xFFFF1493),
                                        isCrit = true
                                    )
                                )
                                for (i in 0..4) {
                                    newParticles.add(
                                        Particle(
                                            x = enemy.x,
                                            y = enemy.y,
                                            vx = (Random.nextFloat() - 0.5f) * 120f,
                                            vy = (Random.nextFloat() - 0.5f) * 120f,
                                            color = listOf(Color(0xFFFF007F), Color(0xFFFFD700), Color(0xFF00F0FF)).random(),
                                            size = 5f,
                                            lifetime = 0.4f,
                                            maxLifetime = 0.4f
                                        )
                                    )
                                }
                                enemy.copy(hp = remainingHp)
                            }
                        } else enemy
                    } else enemy
                }

                if (hitEnemies.isNotEmpty()) {
                    p.hypeEnergy = (p.hypeEnergy + 12f).coerceAtMost(100f)
                }

                _state.update {
                    it.copy(
                        enemies = updatedEnemies,
                        particles = it.particles + newParticles,
                        damagePopups = it.damagePopups + newPopups,
                        screenShake = if (hitEnemies.isNotEmpty()) 6f else 2f
                    )
                }
            }

            WeaponType.GLAM_WHIP -> {
                val hitEnemies = mutableListOf<String>()
                val newPopups = mutableListOf<DamagePopup>()
                val newParticles = mutableListOf<Particle>()
                val updatedEnemies = s.enemies.map { enemy ->
                    val dx = enemy.x - p.x
                    val dy = enemy.y - p.y
                    val dist = sqrt(dx * dx + dy * dy)
                    if (dist < w.range + enemy.radius) {
                        hitEnemies.add(enemy.id)
                        val remainingHp = enemy.hp - totalDamage
                        newPopups.add(
                            DamagePopup(x = enemy.x, y = enemy.y - 20f, text = "-${totalDamage.toInt()} ⚡", color = Color(0xFFC084FC))
                        )
                        for (i in 0..3) {
                            newParticles.add(
                                Particle(enemy.x, enemy.y, (Random.nextFloat() - 0.5f) * 100f, (Random.nextFloat() - 0.5f) * 100f, Color(0xFF8B5CF6), 5f, 0.3f, 0.3f)
                            )
                        }
                        enemy.copy(hp = remainingHp)
                    } else enemy
                }
                if (hitEnemies.isNotEmpty()) p.hypeEnergy = (p.hypeEnergy + 15f).coerceAtMost(100f)
                _state.update {
                    it.copy(
                        enemies = updatedEnemies,
                        damagePopups = it.damagePopups + newPopups,
                        particles = it.particles + newParticles,
                        screenShake = 5f
                    )
                }
            }

            WeaponType.PINK_SHOCKER, WeaponType.AUTOTUNE_MIC, WeaponType.PLATINUM_BLASTER -> {
                val projSpeed = 480f
                val projectile = Projectile(
                    x = p.x + cos(angle) * 32f,
                    y = p.y + sin(angle) * 32f,
                    vx = cos(angle) * projSpeed,
                    vy = sin(angle) * projSpeed,
                    damage = totalDamage,
                    radius = if (w.type == WeaponType.PLATINUM_BLASTER) 16f else if (w.type == WeaponType.AUTOTUNE_MIC) 14f else 8f,
                    color = w.color,
                    isPlayer = true,
                    lifetime = 1.3f
                )
                _state.update {
                    it.copy(projectiles = it.projectiles + projectile)
                }
            }
        }
    }

    fun triggerHypercarUltimate() {
        val s = _state.value
        val p = s.player
        if (p.hypeEnergy >= 100f) {
            vibrate(100)
            p.hypeEnergy = 0f
            val angle = p.facingAngle
            val speed = 800f
            val car = HypercarSummon(
                x = p.x - cos(angle) * 60f,
                y = p.y - sin(angle) * 60f,
                vx = cos(angle) * speed,
                vy = sin(angle) * speed,
                angle = angle,
                lifetime = 2.5f,
                damage = 696f
            )

            _state.update {
                it.copy(
                    hypercars = it.hypercars + car,
                    screenShake = 18f,
                    currentPunchline = "6969-Й ГИПЕРКАР СМЕТАЕТ ВСЁ НА ПУТИ! ВРУМ-ВРУМ!",
                    showSpeechBubble = true
                )
            }
            speechCooldown = 4f
        }
    }

    fun interactWithNearest() {
        val s = _state.value
        val p = s.player

        // 1. Check nearby NPC
        for (npc in s.npcs) {
            val dx = p.x - npc.x
            val dy = p.y - npc.y
            if (sqrt(dx * dx + dy * dy) < 85f) {
                vibrate(20)
                _state.update {
                    it.copy(
                        screen = GameScreen.DIALOGUE,
                        activeDialogueNPC = npc,
                        currentDialogueNodeIndex = 0
                    )
                }
                return
            }
        }

        // 2. Check nearby World Artifact
        for (worldArt in s.worldArtifacts) {
            if (!worldArt.isCollected) {
                val dx = p.x - worldArt.x
                val dy = p.y - worldArt.y
                if (sqrt(dx * dx + dy * dy) < 75f) {
                    vibrate(30)
                    _state.update {
                        it.copy(
                            screen = GameScreen.ARTIFACT_INSPECT,
                            inspectingArtifact = worldArt
                        )
                    }
                    return
                }
            }
        }
    }

    fun selectDialogueOption(option: DialogueOption) {
        val s = _state.value
        val p = s.player

        if (option.isHireOption) {
            val npc = s.activeDialogueNPC
            if (p.gold >= option.hireCostGold && npc != null) {
                p.gold -= option.hireCostGold
                p.activeCompanion = ActiveCompanion(
                    id = npc.id,
                    name = npc.name,
                    color = npc.color
                )
                vibrate(80)
                val returnScreen = if (_state.value.screen == GameScreen.VIP_HUB) GameScreen.VIP_HUB else GameScreen.PLAYING
                _state.update {
                    it.copy(
                        player = p,
                        screen = returnScreen,
                        activeDialogueNPC = null,
                        currentDialogueNodeIndex = 0,
                        currentPunchline = "🤝 Нанят помощник ${npc.name} на 1 забег!",
                        showSpeechBubble = true
                    )
                }
                speechCooldown = 4f
                saveCurrentGame()
                return
            } else {
                vibrate(40)
                _state.update {
                    it.copy(
                        currentPunchline = "Недостаточно золота для найма! Нужно ${option.hireCostGold} 💰.",
                        showSpeechBubble = true
                    )
                }
                speechCooldown = 3f
                return
            }
        }

        if (option.rewardGold > 0) {
            p.gold += option.rewardGold
        }

        var newEndings = s.unlockedEndings
        option.unlocksEndingId?.let { endingId ->
            newEndings = newEndings + endingId
            appContext?.let { ctx ->
                SaveManager.saveUnlockedEnding(ctx, endingId)
            }
        }

        if (option.nextNodeIndex >= 0) {
            _state.update {
                it.copy(
                    player = p,
                    unlockedEndings = newEndings,
                    currentDialogueNodeIndex = option.nextNodeIndex
                )
            }
        } else {
            val returnScreen = if (_state.value.screen == GameScreen.VIP_HUB) GameScreen.VIP_HUB else GameScreen.PLAYING
            _state.update {
                it.copy(
                    player = p,
                    unlockedEndings = newEndings,
                    screen = returnScreen,
                    activeDialogueNPC = null,
                    currentDialogueNodeIndex = 0,
                    currentPunchline = "Диалог завершен! Сюжет Марса развивается в нашу пользу!",
                    showSpeechBubble = true
                )
            }
            speechCooldown = 4f
            saveCurrentGame()
        }
    }

    fun openDialogueWith(npc: FriendlyNPC) {
        vibrate(20)
        _state.update {
            it.copy(
                activeDialogueNPC = npc,
                currentDialogueNodeIndex = 0
            )
        }
    }

    fun closeDialogue() {
        val returnScreen = if (_state.value.screen == GameScreen.VIP_HUB) GameScreen.VIP_HUB else GameScreen.PLAYING
        _state.update {
            it.copy(
                screen = returnScreen,
                activeDialogueNPC = null,
                currentDialogueNodeIndex = 0
            )
        }
    }

    fun collectWorldArtifact(worldArt: WorldArtifact) {
        val s = _state.value
        val p = s.player
        vibrate(60)

        worldArt.isCollected = true
        p.equippedArtifacts.add(worldArt.artifact)
        updateQuestProgress(QuestType.COLLECT_ARTIFACTS, 1)

        _state.update {
            it.copy(
                player = p,
                screen = GameScreen.PLAYING,
                inspectingArtifact = null,
                currentPunchline = "НАШЕЛ РЕЛИКИЮ: '${worldArt.artifact.name}'! МОЙ ФЛЕКС НЕУДЕРЖИМ!",
                showSpeechBubble = true
            )
        }
        speechCooldown = 4f
        saveCurrentGame()
    }

    fun closeArtifactInspect() {
        _state.update {
            it.copy(
                screen = GameScreen.PLAYING,
                inspectingArtifact = null
            )
        }
    }

    fun updateGame(dt: Float) {
        val s = _state.value
        if (s.screen != GameScreen.PLAYING) return

        gameTimeSec += dt

        val cycleTime = (gameTimeSec % 45f)
        val newTimeOfDay = when {
            cycleTime < 22f -> TimeOfDay.DAY
            cycleTime < 32f -> TimeOfDay.SUNSET
            else -> TimeOfDay.NIGHT
        }

        val p = s.player
        var effectiveSpeed = p.baseSpeed
        p.equippedArtifacts.forEach { effectiveSpeed += it.bonusSpeed }
        if (p.isDashing) {
            effectiveSpeed *= 2.8f
            p.dashTimer -= dt
            if (p.dashTimer <= 0f) {
                p.isDashing = false
            }
        }
        if (p.dashCooldown > 0f) {
            p.dashCooldown -= dt
        }

        val isMoving = (joystickX != 0f || joystickY != 0f)
        p.isMoving = isMoving
        if (isMoving) {
            p.walkCycleTimer += dt
            val length = sqrt(joystickX * joystickX + joystickY * joystickY)
            val nx = joystickX / length
            val ny = joystickY / length
            val stepX = nx * effectiveSpeed * dt
            val stepY = ny * effectiveSpeed * dt
            val targetX = p.x + stepX
            val targetY = p.y + stepY

            if (ProceduralWorld.isPositionWalkable(targetX, targetY, 18f)) {
                p.x = targetX
                p.y = targetY
            } else if (ProceduralWorld.isPositionWalkable(targetX, p.y, 18f)) {
                p.x = targetX
            } else if (ProceduralWorld.isPositionWalkable(p.x, targetY, 18f)) {
                p.y = targetY
            }
            p.facingAngle = atan2(ny, nx)
        } else if (p.isDashing) {
            val nx = cos(p.facingAngle)
            val ny = sin(p.facingAngle)
            val targetX = p.x + nx * effectiveSpeed * dt
            val targetY = p.y + ny * effectiveSpeed * dt
            if (ProceduralWorld.isPositionWalkable(targetX, targetY, 18f)) {
                p.x = targetX
                p.y = targetY
            } else if (ProceduralWorld.isPositionWalkable(targetX, p.y, 18f)) {
                p.x = targetX
            } else if (ProceduralWorld.isPositionWalkable(p.x, targetY, 18f)) {
                p.y = targetY
            }
        }

        if (s.showSpeechBubble) {
            speechCooldown -= dt
            if (speechCooldown <= 0f) {
                _state.update { it.copy(showSpeechBubble = false) }
            }
        } else {
            speechCooldown -= dt
            if (speechCooldown < -10f) {
                _state.update {
                    it.copy(
                        currentPunchline = ProceduralWorld.getRandomPunchline(),
                        showSpeechBubble = true
                    )
                }
                speechCooldown = 4f
            }
        }

        val companionProjectiles = mutableListOf<Projectile>()
        p.activeCompanion?.let { companion ->
            companion.x = p.x - 36f * cos(p.facingAngle) - 24f * sin(p.facingAngle)
            companion.y = p.y - 36f * sin(p.facingAngle) + 24f * cos(p.facingAngle)
            companion.shootCooldown -= dt
            if (companion.shootCooldown <= 0f && s.enemies.isNotEmpty()) {
                companion.shootCooldown = 0.5f
                val nearest = s.enemies.minByOrNull { sqrt((it.x - companion.x) * (it.x - companion.x) + (it.y - companion.y) * (it.y - companion.y)) }
                if (nearest != null) {
                    val fireAngle = atan2(nearest.y - companion.y, nearest.x - companion.x)
                    companionProjectiles.add(
                        Projectile(
                            x = companion.x + cos(fireAngle) * 16f,
                            y = companion.y + sin(fireAngle) * 16f,
                            vx = cos(fireAngle) * 550f,
                            vy = sin(fireAngle) * 550f,
                            damage = 40f + p.floor * 10f,
                            radius = 8f,
                            color = Color(0xFF00F0FF),
                            isPlayer = true,
                            lifetime = 1.2f
                        )
                    )
                }
            }
        }

        val updatedHypercars = s.hypercars.mapNotNull { car ->
            car.x += car.vx * dt
            car.y += car.vy * dt
            car.lifetime -= dt
            if (car.lifetime > 0f) car else null
        }

        val newPopups = mutableListOf<DamagePopup>()
        val newDrops = mutableListOf<DroppedItem>()
        val newParticles = mutableListOf<Particle>()

        val remainingEnemies = s.enemies.toMutableList()

        val updatedProjectiles = (s.projectiles + companionProjectiles).mapNotNull { proj ->
            proj.x += proj.vx * dt
            proj.y += proj.vy * dt
            proj.lifetime -= dt

            var hit = false
            if (proj.isPlayer) {
                for (i in remainingEnemies.indices.reversed()) {
                    val enemy = remainingEnemies[i]
                    val dx = enemy.x - proj.x
                    val dy = enemy.y - proj.y
                    if (sqrt(dx * dx + dy * dy) < proj.radius + enemy.radius) {
                        hit = true
                        val toProjAngle = atan2(proj.y - enemy.y, proj.x - enemy.x)
                        var shieldDiff = kotlin.math.abs(toProjAngle - enemy.facingAngle)
                        if (shieldDiff > Math.PI) shieldDiff = (2 * Math.PI - shieldDiff).toFloat()

                        if (enemy.hasShield && shieldDiff < 1.0f) {
                            newPopups.add(DamagePopup(enemy.x, enemy.y - 20f, "ЩИТ БЛОК!", Color(0xFF00F0FF)))
                        } else {
                            enemy.hp -= proj.damage
                            p.hypeEnergy = (p.hypeEnergy + 5f).coerceAtMost(100f)
                            newPopups.add(
                                DamagePopup(enemy.x, enemy.y - 20f, "-${proj.damage.toInt()}", proj.color)
                            )
                        }
                        break
                    }
                }
            } else {
                val dx = p.x - proj.x
                val dy = p.y - proj.y
                if (sqrt(dx * dx + dy * dy) < proj.radius + 18f && !p.isDashing) {
                    hit = true
                    p.hp -= proj.damage
                    vibrate(40)
                    newPopups.add(
                        DamagePopup(p.x, p.y - 20f, "-${proj.damage.toInt()}", Color.Red)
                    )
                }
            }

            if (!hit && proj.lifetime > 0f) proj else null
        }

        for (car in updatedHypercars) {
            for (enemy in remainingEnemies) {
                val dx = enemy.x - car.x
                val dy = enemy.y - car.y
                if (sqrt(dx * dx + dy * dy) < 60f) {
                    enemy.hp -= car.damage * dt * 4f
                    newPopups.add(DamagePopup(enemy.x, enemy.y - 15f, "СУПЕРКАР! -696", Color(0xFFFFD700), isCrit = true))
                }
            }
        }

        val aliveEnemies = mutableListOf<Enemy>()
        for (enemy in remainingEnemies) {
            if (enemy.hp <= 0f) {
                p.kills += 1
                updateQuestProgress(QuestType.KILL_ENEMIES, 1)

                val dropMult = s.talents.getDropMultiplier()
                val goldAmt = ((15 + enemy.type.score / 5) * dropMult).toInt()
                newDrops.add(DroppedItem(type = DropType.GOLD, x = enemy.x, y = enemy.y, amount = goldAmt))

                if (Random.nextInt(100) < 35) {
                    val diaAmt = (2 * dropMult).toInt().coerceAtLeast(1)
                    newDrops.add(DroppedItem(type = DropType.DIAMOND, x = enemy.x + 10f, y = enemy.y, amount = diaAmt))
                }
                if (Random.nextInt(100) < 25) {
                    newDrops.add(DroppedItem(type = DropType.BATTERY, x = enemy.x - 10f, y = enemy.y, amount = 1))
                }
                if (Random.nextInt(100) < 15) {
                    newDrops.add(DroppedItem(type = DropType.HEART, x = enemy.x, y = enemy.y + 10f, amount = 30))
                }
                for (k in 0..8) {
                    newParticles.add(
                        Particle(
                            enemy.x, enemy.y,
                            (Random.nextFloat() - 0.5f) * 160f,
                            (Random.nextFloat() - 0.5f) * 160f,
                            enemy.color, 6f, 0.45f, 0.45f
                        )
                    )
                }
                if (enemy.type.isBoss) {
                    updateQuestProgress(QuestType.KILL_BOSS, 1)
                    val nextEndingId = (p.floor % 1111) + 1
                    val updatedEndings = s.unlockedEndings + nextEndingId
                    appContext?.let { SaveManager.saveUnlockedEnding(it, nextEndingId) }
                    updateQuestProgress(QuestType.COMPLETE_ENDINGS, 1)

                    _state.update {
                        it.copy(
                            bossActive = false,
                            unlockedEndings = updatedEndings,
                            currentPunchline = "БОСС ПОВЕРЖЕН! ЭТАЖ ПРОЙДЕН! ОТКРЫТ ФИНАЛ #$nextEndingId!",
                            showSpeechBubble = true
                        )
                    }
                    speechCooldown = 5f
                    saveCurrentGame()
                }
            } else {
                val dx = p.x - enemy.x
                val dy = p.y - enemy.y
                val dist = sqrt(dx * dx + dy * dy)
                if (dist > 10f) {
                    val nightSpeedBuff = if (newTimeOfDay == TimeOfDay.NIGHT) 1.25f else 1.0f
                    val stepDist = enemy.speed * nightSpeedBuff * dt
                    val targetEx = enemy.x + (dx / dist) * stepDist
                    val targetEy = enemy.y + (dy / dist) * stepDist

                    if (ProceduralWorld.isPositionWalkable(targetEx, targetEy, enemy.radius)) {
                        enemy.x = targetEx
                        enemy.y = targetEy
                    } else if (ProceduralWorld.isPositionWalkable(targetEx, enemy.y, enemy.radius)) {
                        enemy.x = targetEx
                    } else if (ProceduralWorld.isPositionWalkable(enemy.x, targetEy, enemy.radius)) {
                        enemy.y = targetEy
                    }
                    enemy.facingAngle = atan2(dy, dx)
                }

                enemy.attackCooldown -= dt
                if (dist < enemy.radius + 16f && enemy.attackCooldown <= 0f && !p.isDashing) {
                    p.hp -= enemy.damage
                    enemy.attackCooldown = 0.8f
                    vibrate(30)
                    newPopups.add(
                        DamagePopup(p.x, p.y - 20f, "-${enemy.damage.toInt()}", Color(0xFFEF4444))
                    )
                }

                if ((enemy.type == EnemyType.CENSOR_DRONE || enemy.type == EnemyType.BUREAU_WALKER) && dist < 340f && enemy.attackCooldown <= 0f) {
                    enemy.attackCooldown = if (enemy.type == EnemyType.CENSOR_DRONE) 2.2f else 3.0f
                    val angle = atan2(dy, dx)
                    updatedProjectiles.toMutableList().add(
                        Projectile(
                            x = enemy.x,
                            y = enemy.y,
                            vx = cos(angle) * 230f,
                            vy = sin(angle) * 230f,
                            damage = 14f + p.floor * 2f,
                            radius = 8f,
                            color = Color(0xFFFF0055),
                            isPlayer = false,
                            lifetime = 2.2f
                        )
                    )
                }

                aliveEnemies.add(enemy)
            }
        }

        val remainingDrops = mutableListOf<DroppedItem>()
        for (item in s.droppedItems) {
            val dx = p.x - item.x
            val dy = p.y - item.y
            val dist = sqrt(dx * dx + dy * dy)
            if (dist < 45f) {
                vibrate(10)
                when (item.type) {
                    DropType.GOLD -> {
                        p.gold += item.amount
                        newPopups.add(DamagePopup(p.x, p.y - 30f, "+${item.amount} Золота", Color(0xFFFFD700)))
                    }
                    DropType.DIAMOND -> {
                        p.diamonds += item.amount
                        newPopups.add(DamagePopup(p.x, p.y - 30f, "+${item.amount} Бриллиантов", Color(0xFF00E5FF)))
                    }
                    DropType.BATTERY -> {
                        p.batteries += item.amount
                        updateQuestProgress(QuestType.COLLECT_BATTERIES, item.amount)
                        newPopups.add(DamagePopup(p.x, p.y - 30f, "+${item.amount} Батарей", Color(0xFF10B981)))
                    }
                    DropType.HEART -> {
                        var maxHp = p.maxHp
                        p.equippedArtifacts.forEach { maxHp += it.bonusHp }
                        p.hp = (p.hp + item.amount).coerceAtMost(maxHp)
                        newPopups.add(DamagePopup(p.x, p.y - 30f, "+${item.amount} HP", Color(0xFFFF1493)))
                    }
                }
            } else {
                item.lifetime -= dt
                if (item.lifetime > 0f) remainingDrops.add(item)
            }
        }

        val updatedPopups = (s.damagePopups + newPopups).mapNotNull { popup ->
            popup.y -= dt * 45f
            popup.lifetime -= dt
            if (popup.lifetime > 0f) popup else null
        }

        val updatedParticles = (s.particles + newParticles).mapNotNull { part ->
            part.x += part.vx * dt
            part.y += part.vy * dt
            part.lifetime -= dt
            if (part.lifetime > 0f) part else null
        }

        if (p.hp <= 0f) {
            vibrate(180)
            appContext?.let { ctx -> SaveManager.clearSavedGame(ctx) }
            val fullMaxHp = 250f + s.talents.getBonusMaxHp()
            val resetPlayer = p.copy(
                hp = fullMaxHp,
                maxHp = fullMaxHp,
                floor = 1,
                kills = 0,
                gold = 100,
                diamonds = 10
            )
            _state.update {
                it.copy(
                    player = resetPlayer,
                    screen = GameScreen.VIP_HUB,
                    hasSavedGame = false,
                    enemies = emptyList(),
                    projectiles = emptyList(),
                    hypercars = emptyList(),
                    currentPunchline = "💀 ВЛАДИК ПОГИБ! Забег сброшен на 1 этаж! Кнопка 'Продолжить' заблокирована. Прокачай таланты в Хабе и начни Новую Игру!",
                    showSpeechBubble = true
                )
            }
            speechCooldown = 6f
            return
        }

        // Prevent player from simply outrunning all enemies:
        // Relocate distant enemies to a perimeter surround ring around Vlad
        for (enemy in aliveEnemies) {
            val edx = enemy.x - p.x
            val edy = enemy.y - p.y
            val enemyDist = sqrt(edx * edx + edy * edy)
            if (enemyDist > 680f) {
                for (attempt in 0..6) {
                    val angle = Random.nextFloat() * 6.283f
                    val dist = 380f + Random.nextFloat() * 100f
                    val candX = p.x + cos(angle) * dist
                    val candY = p.y + sin(angle) * dist
                    if (ProceduralWorld.isPositionWalkable(candX, candY, enemy.radius)) {
                        enemy.x = candX
                        enemy.y = candY
                        break
                    }
                }
            }
        }

        enemySpawnTimer += dt
        val spawnInterval = if (newTimeOfDay == TimeOfDay.NIGHT) 1.6f else 2.6f
        val maxEnemies = (16 + p.floor * 2).coerceAtMost(32)
        if (enemySpawnTimer > spawnInterval && aliveEnemies.size < maxEnemies) {
            enemySpawnTimer = 0f
            spawnSingleEnemy(p.x, p.y, p.floor)
        }

        val newShake = (s.screenShake - dt * 25f).coerceAtLeast(0f)

        _state.update {
            it.copy(
                player = p,
                enemies = aliveEnemies,
                projectiles = updatedProjectiles,
                particles = updatedParticles,
                damagePopups = updatedPopups,
                droppedItems = remainingDrops + newDrops,
                hypercars = updatedHypercars,
                timeOfDay = newTimeOfDay,
                screenShake = newShake
            )
        }
    }

    private fun spawnSingleEnemy(playerX: Float, playerY: Float, floor: Int) {
        var ex = playerX + 400f
        var ey = playerY
        for (attempt in 0..12) {
            val angle = Random.nextFloat() * 6.283f
            val dist = 360f + Random.nextFloat() * 160f
            val candX = playerX + cos(angle) * dist
            val candY = playerY + sin(angle) * dist
            if (ProceduralWorld.isPositionWalkable(candX, candY, 26f)) {
                ex = candX
                ey = candY
                break
            }
        }

        val roll = Random.nextInt(100)
        val enemyType = when {
            roll < 15 -> EnemyType.WHITE_BUFF_MALE
            roll < 30 -> EnemyType.GIRL_RUNNER
            roll < 45 -> EnemyType.HEAVY_FEMALE
            roll < 60 -> EnemyType.WALKING_FEMALE_SIGN
            roll < 75 -> EnemyType.CENSOR_DRONE
            roll < 88 -> EnemyType.FITNESS_RAIDER
            roll < 95 -> EnemyType.SHIELD_ENFORCER
            else -> EnemyType.BUREAU_WALKER
        }

        val diffMult = _state.value.difficulty.enemyHpMultiplier
        val scaledHp = (enemyType.baseHp * (1f + floor * 0.45f)) * diffMult
        val scaledDamage = 12f + floor * 6f

        val enemy = Enemy(
            type = enemyType,
            name = when (enemyType) {
                EnemyType.WHITE_BUFF_MALE -> "Качок в Очках"
                EnemyType.GIRL_RUNNER -> "Бегущая Неон-Девушка"
                EnemyType.HEAVY_FEMALE -> "Тяжелая Воительница"
                EnemyType.WALKING_FEMALE_SIGN -> "Ходячий Знак Девушки"
                EnemyType.CENSOR_DRONE -> "Дрон-Натурал Mk.I"
                EnemyType.FITNESS_RAIDER -> "Качок-Рейдер"
                EnemyType.SHIELD_ENFORCER -> "Щитовой Силовик"
                EnemyType.BUREAU_WALKER -> "Шагающий Запретитель"
                else -> "Робот-Противник"
            },
            x = ex,
            y = ey,
            hp = scaledHp,
            maxHp = scaledHp,
            speed = enemyType.speed,
            damage = scaledDamage,
            radius = when (enemyType) {
                EnemyType.SHIELD_ENFORCER -> 28f
                EnemyType.BUREAU_WALKER -> 30f
                else -> 22f
            },
            color = when (enemyType) {
                EnemyType.WHITE_BUFF_MALE -> Color(0xFFE2E8F0)
                EnemyType.GIRL_RUNNER -> Color(0xFFF43F5E)
                EnemyType.HEAVY_FEMALE -> Color(0xFFA855F7)
                EnemyType.WALKING_FEMALE_SIGN -> Color(0xFFEC4899)
                EnemyType.CENSOR_DRONE -> Color(0xFF64748B)
                EnemyType.FITNESS_RAIDER -> Color(0xFF8B5CF6)
                EnemyType.SHIELD_ENFORCER -> Color(0xFF00F0FF)
                else -> Color(0xFFDC2626)
            },
            hasShield = (enemyType == EnemyType.SHIELD_ENFORCER),
            facingAngle = atan2(playerY - ey, playerX - ex)
        )

        _state.update { it.copy(enemies = it.enemies + enemy) }
    }

    fun spawnBoss() {
        val s = _state.value
        val p = s.player

        if (s.bossSummonedOnFloor || s.bossActive) return

        val angle = Random.nextFloat() * 6.283f
        val ex = p.x + cos(angle) * 350f
        val ey = p.y + sin(angle) * 350f

        val bossRoll = Random.nextInt(5)
        val (bossType, bossName, bossColor) = when (bossRoll) {
            0 -> Triple(EnemyType.FAT_SKUF_BOSS, "Огуречный Скуф-Титан", Color(0xFF10B981))
            1 -> Triple(EnemyType.USSR_MAN_BOSS, "Мужик из СССР", Color(0xFFEF4444))
            2 -> Triple(EnemyType.VLAD_MOM_BOSS, "Мама Влада", Color(0xFFE040FB))
            3 -> Triple(EnemyType.VLAD_DAD_BOSS, "Батя Влада в Майке", Color(0xFFFF9100))
            else -> Triple(EnemyType.GIRL_5YO_BOSS, "Анимэ-Девочка Star-5", Color(0xFFFF69B4))
        }

        val bossHp = bossType.baseHp * (1f + p.floor * 0.5f) * s.difficulty.enemyHpMultiplier
        val bossDamage = 40f + p.floor * 10f

        val boss = Enemy(
            type = bossType,
            name = bossName,
            x = ex,
            y = ey,
            hp = bossHp,
            maxHp = bossHp,
            speed = bossType.speed,
            damage = bossDamage,
            radius = 48f,
            color = bossColor,
            facingAngle = atan2(p.y - ey, p.x - ex)
        )

        _state.update {
            it.copy(
                enemies = it.enemies + boss,
                bossActive = true,
                bossSummonedOnFloor = true,
                currentPunchline = "⚠️ ВНИМАНИЕ! БОСС $bossName ВТОРГСЯ НА ЭТАЖ ${p.floor}!",
                showSpeechBubble = true
            )
        }
        speechCooldown = 5f
    }

    fun spawnEnemiesForWave(count: Int) {
        val p = _state.value.player
        val totalCount = count + (p.floor * 2).coerceAtMost(22)
        for (i in 0 until totalCount) {
            spawnSingleEnemy(p.x, p.y, p.floor)
        }
    }

    fun openInventory() {
        _state.update { it.copy(screen = GameScreen.INVENTORY) }
    }

    fun closeInventory() {
        _state.update { it.copy(screen = GameScreen.PLAYING) }
    }

    fun craftRecipe(recipe: CraftingRecipe) {
        val p = _state.value.player
        if (p.gold >= recipe.reqGold && p.diamonds >= recipe.reqDiamonds && p.batteries >= recipe.reqBatteries) {
            p.gold -= recipe.reqGold
            p.diamonds -= recipe.reqDiamonds
            p.batteries -= recipe.reqBatteries
            vibrate(50)

            recipe.producesWeapon?.let { newWeapon ->
                p.inventoryWeapons.add(newWeapon)
                p.equippedWeapon = newWeapon
            }
            recipe.producesArtifact?.let { newArtifact ->
                p.equippedArtifacts.add(newArtifact)
            }

            _state.update {
                it.copy(
                    player = p,
                    currentPunchline = "Скрафтил '${recipe.name}'! Мой стиль не победить!",
                    showSpeechBubble = true
                )
            }
            speechCooldown = 4f
            saveCurrentGame()
        }
    }

    fun equipWeapon(weapon: Weapon) {
        val p = _state.value.player
        p.equippedWeapon = weapon
        _state.update { it.copy(player = p) }
    }

    fun restartGame() {
        startNewGame(difficulty = _state.value.difficulty)
    }

    fun openHub() {
        _state.update { it.copy(screen = GameScreen.VIP_HUB) }
    }

    fun closeHub() {
        _state.update { it.copy(screen = GameScreen.PLAYING) }
    }

    fun startRaidFromHub() {
        val s = _state.value
        val p = s.player
        val totalMaxHp = 250f + s.talents.getBonusMaxHp()
        p.maxHp = totalMaxHp
        p.hp = totalMaxHp
        _state.update {
            it.copy(
                screen = GameScreen.PLAYING,
                enemies = emptyList(),
                projectiles = emptyList(),
                worldArtifacts = ProceduralWorld.generateWorldArtifactsForFloor(p.floor),
                currentPunchline = "ВЛАДИК СПУСКАЕТСЯ В ОСОБНЯК! МАКСИМАЛЬНЫЙ ФЛЕКС 800 ЭТАЖЕЙ!",
                showSpeechBubble = true
            )
        }
        speechCooldown = 4f
        spawnEnemiesForWave(6)
        saveCurrentGame()
    }

    fun openTalentTree() {
        _state.update { it.copy(screen = GameScreen.TALENT_TREE) }
    }

    fun closeTalentTree() {
        _state.update { it.copy(screen = GameScreen.VIP_HUB) }
    }

    fun upgradeTalent(talentType: TalentType) {
        val s = _state.value
        val p = s.player
        val t = s.talents
        val currentTier = when (talentType) {
            TalentType.HEALTH -> t.healthTier
            TalentType.DAMAGE -> t.damageTier
            TalentType.DASH -> t.dashCooldownTier
            TalentType.HYPE -> t.hypeRateTier
            TalentType.GREED -> t.greedTier
            TalentType.VAMPIRISM -> t.vampirismTier
            TalentType.ARMOR -> t.armorTier
            TalentType.CRIT_CHANCE -> t.critChanceTier
            TalentType.CRIT_DAMAGE -> t.critDamageTier
            TalentType.SPEED -> t.speedTier
            TalentType.BOSS_SLAYER -> t.bossSlayerTier
        }
        if (currentTier >= talentType.maxTier) return

        val reqDiamonds = talentType.baseCostDiamonds * (currentTier + 1)
        val reqGold = talentType.baseCostGold * (currentTier + 1)

        if (p.diamonds >= reqDiamonds && p.gold >= reqGold) {
            p.diamonds -= reqDiamonds
            p.gold -= reqGold
            vibrate(60)

            val newTalents = when (talentType) {
                TalentType.HEALTH -> t.copy(healthTier = t.healthTier + 1)
                TalentType.DAMAGE -> t.copy(damageTier = t.damageTier + 1)
                TalentType.DASH -> t.copy(dashCooldownTier = t.dashCooldownTier + 1)
                TalentType.HYPE -> t.copy(hypeRateTier = t.hypeRateTier + 1)
                TalentType.GREED -> t.copy(greedTier = t.greedTier + 1)
                TalentType.VAMPIRISM -> t.copy(vampirismTier = t.vampirismTier + 1)
                TalentType.ARMOR -> t.copy(armorTier = t.armorTier + 1)
                TalentType.CRIT_CHANCE -> t.copy(critChanceTier = t.critChanceTier + 1)
                TalentType.CRIT_DAMAGE -> t.copy(critDamageTier = t.critDamageTier + 1)
                TalentType.SPEED -> t.copy(speedTier = t.speedTier + 1)
                TalentType.BOSS_SLAYER -> t.copy(bossSlayerTier = t.bossSlayerTier + 1)
            }

            p.maxHp = 250f + newTalents.getBonusMaxHp()
            p.hp = (p.hp + 35f).coerceAtMost(p.maxHp)

            appContext?.let { ctx ->
                SaveManager.saveTalents(ctx, newTalents)
            }

            _state.update {
                it.copy(
                    player = p,
                    talents = newTalents,
                    currentPunchline = "Батя прокачал '${talentType.title}' до ранга ${currentTier + 1}! Мой автопарк и стиль не остановить!",
                    showSpeechBubble = true
                )
            }
            speechCooldown = 4f
            saveCurrentGame()
        }
    }

    fun openQuestLog() {
        _state.update { it.copy(screen = GameScreen.QUEST_LOG) }
    }

    fun closeQuestLog() {
        _state.update { it.copy(screen = GameScreen.VIP_HUB) }
    }

    fun claimQuestReward(quest: Quest) {
        val s = _state.value
        val p = s.player
        if (!quest.isCompleted || quest.isClaimed) return

        vibrate(80)
        p.diamonds += quest.rewardDiamonds
        p.gold += quest.rewardGold

        var newEndings = s.unlockedEndings
        quest.unlocksEndingId?.let { eid ->
            newEndings = newEndings + eid
            appContext?.let { ctx ->
                SaveManager.saveUnlockedEnding(ctx, eid)
            }
        }

        val updatedQuests = s.quests.map { q ->
            if (q.id == quest.id) q.copy(isClaimed = true) else q
        }

        appContext?.let { ctx ->
            val claimedIds = updatedQuests.filter { it.isClaimed }.map { it.id }.toSet()
            SaveManager.saveClaimedQuests(ctx, claimedIds)
        }

        _state.update {
            it.copy(
                player = p,
                quests = updatedQuests,
                unlockedEndings = newEndings,
                currentPunchline = "Квест '${quest.title}' сдан! Жульен и Батя в восторге! +${quest.rewardDiamonds}💎 +${quest.rewardGold}💰",
                showSpeechBubble = true
            )
        }
        speechCooldown = 4f
        saveCurrentGame()
    }

    fun hitTrainingDummy(damage: Float) {
        vibrate(25)
        val s = _state.value
        val p = s.player
        val popup = DamagePopup(p.x, p.y - 40f, "ТЕСТ УРОНА: -${damage.toInt()}", Color(0xFF00F0FF), isCrit = true)
        _state.update {
            it.copy(damagePopups = it.damagePopups + popup)
        }
    }

    fun updateQuestProgress(type: QuestType, amount: Int = 1) {
        val currentQuests = _state.value.quests
        var changed = false
        val updated = currentQuests.map { q ->
            if (q.type == type && !q.isClaimed) {
                val newProg = (q.currentProgress + amount).coerceAtMost(q.targetAmount)
                if (newProg != q.currentProgress) {
                    changed = true
                    val completed = newProg >= q.targetAmount
                    q.copy(currentProgress = newProg, isCompleted = completed)
                } else q
            } else q
        }
        if (changed) {
            _state.update { it.copy(quests = updated) }
        }
    }

    fun setNpcSearchQuery(query: String) {
        val filtered = NPCDatabase.searchNpcs(query, ProceduralWorld.getStoryNPCs())
        _state.update {
            it.copy(
                npcSearchQuery = query,
                npcs = filtered
            )
        }
    }

    fun dismissNogAiResult() {
        _state.update {
            it.copy(
                generatedNogAiWeapon = null,
                generatedNogAiArtifact = null
            )
        }
    }

    fun nextFloor() {
        val s = _state.value
        val p = s.player
        if (!s.bossSummonedOnFloor || s.bossActive || s.enemies.isNotEmpty()) {
            vibrate(30)
            _state.update {
                it.copy(
                    currentPunchline = "Лифт заблокирован! Сначала вызовите и одолейте Босса этажа!",
                    showSpeechBubble = true
                )
            }
            speechCooldown = 3f
            return
        }

        val nextF = p.floor + 1
        _state.update {
            it.copy(
                isTransitioningFloor = true,
                transitionFloorNumber = nextF
            )
        }

        viewModelScope.launch {
            vibrate(100)
            kotlinx.coroutines.delay(1200)

            val pState = _state.value.player
            pState.floor = nextF
            pState.hp = pState.maxHp
            pState.x = (Random.nextFloat() * 400f - 200f)
            pState.y = (Random.nextFloat() * 400f - 200f)

            val newWaveTarget = 10 + nextF * 4

            _state.update {
                it.copy(
                    isTransitioningFloor = false,
                    enemies = emptyList(),
                    projectiles = emptyList(),
                    bossActive = false,
                    bossSummonedOnFloor = false,
                    floorWaveTarget = newWaveTarget,
                    floorEnemiesDefeated = 0,
                    worldArtifacts = ProceduralWorld.generateWorldArtifactsForFloor(nextF),
                    currentPunchline = "ЛИФТ ПРИБЫЛ! ЭТАЖ $nextF! ВЛАДИК ПОКОРЯЕТ МАРС!",
                    showSpeechBubble = true
                )
            }
            speechCooldown = 4f
            spawnEnemiesForWave(newWaveTarget)
            saveCurrentGame()
        }
    }

    fun getNogAiDynamicPrice(): Pair<Int, Int> {
        val hourBlock = (System.currentTimeMillis() / (5 * 3600 * 1000)).toInt()
        val seed = kotlin.math.abs(hourBlock * 73856093)
        val goldCost = 30 + (seed % 40)
        val diaCost = 3 + (seed % 5)
        return Pair(goldCost, diaCost)
    }

    fun generateNogAiItem(isWeapon: Boolean) {
        val s = _state.value
        val p = s.player
        val (costGold, costDiamonds) = getNogAiDynamicPrice()

        if (p.gold < costGold || p.diamonds < costDiamonds) {
            vibrate(50)
            _state.update {
                it.copy(
                    currentPunchline = "nOG AI: Недостаточно ресурсов! Нужно: $costGold 🪙 и $costDiamonds 💎.",
                    showSpeechBubble = true
                )
            }
            speechCooldown = 3f
            return
        }

        vibrate(80)
        _state.update {
            it.copy(
                isGeneratingNogAi = true,
                nogAiProgress = 0f,
                generatedNogAiWeapon = null,
                generatedNogAiArtifact = null
            )
        }

        viewModelScope.launch {
            for (step in 1..10) {
                kotlinx.coroutines.delay(120)
                vibrate(15)
                _state.update { it.copy(nogAiProgress = step / 10f) }
            }

            val updatedGold = p.gold - costGold
            val updatedDiamonds = p.diamonds - costDiamonds

            if (isWeapon) {
                val weapon = ProceduralWorld.generateRandomWeapon(p.floor)
                val nOGWeapon = weapon.copy(
                    name = "🤖 nOG AI: ${weapon.name}",
                    damage = weapon.damage * 2.0f,
                    rarity = ItemRarity.LEGENDARY
                )
                val newInvWeapons = p.inventoryWeapons + nOGWeapon
                val newPlayer = p.copy(
                    gold = updatedGold,
                    diamonds = updatedDiamonds,
                    inventoryWeapons = newInvWeapons.toMutableList(),
                    equippedWeapon = nOGWeapon
                )
                _state.update {
                    it.copy(
                        player = newPlayer,
                        isGeneratingNogAi = false,
                        generatedNogAiWeapon = nOGWeapon,
                        currentPunchline = "🤖 nOG AI СИНТЕЗИРОВАЛ ЛЕГЕНДАРНОЕ ОРУЖИЕ ДЛЯ ВЛАДА: ${nOGWeapon.name}!",
                        showSpeechBubble = true
                    )
                }
            } else {
                val nOGArtifact = Artifact(
                    name = "🤖 nOG AI Квантовый Реликт #${Random.nextInt(10000, 99999)}",
                    description = "Энергетический артефакт нейросети nOG AI с максимальными параметрами.",
                    rarity = ItemRarity.LEGENDARY,
                    bonusHp = 120f,
                    bonusSpeed = 40f,
                    bonusDamage = 35f,
                    bonusHypeRate = 1.0f,
                    loreStory = "Создано nOG AI в VIP-Лаундже Марса."
                )
                val newArtifacts = p.equippedArtifacts + nOGArtifact
                val newPlayer = p.copy(
                    gold = updatedGold,
                    diamonds = updatedDiamonds,
                    equippedArtifacts = newArtifacts.toMutableList()
                )
                _state.update {
                    it.copy(
                        player = newPlayer,
                        isGeneratingNogAi = false,
                        generatedNogAiArtifact = nOGArtifact,
                        currentPunchline = "🤖 nOG AI СИНТЕЗИРОВАЛ ЛЕГЕНДАРНЫЙ АРТЕФАКТ ДЛЯ ВЛАДА: ${nOGArtifact.name}!",
                        showSpeechBubble = true
                    )
                }
            }
        }
    }

    fun rejectNogAiItem() {
        val s = _state.value
        val p = s.player
        val refundGold = 150
        p.gold += refundGold
        vibrate(30)
        _state.update {
            it.copy(
                player = p,
                generatedNogAiWeapon = null,
                generatedNogAiArtifact = null,
                currentPunchline = "Предмет утилизирован в золотую пыль! Компенсация: +150 💰",
                showSpeechBubble = true
            )
        }
    }

    private fun abs(value: Float): Float = kotlin.math.abs(value)
}
