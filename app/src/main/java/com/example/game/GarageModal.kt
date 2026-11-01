package com.example.game

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.GoldCoin
import com.example.ui.theme.HotPink
import com.example.ui.theme.MartianGold
import com.example.ui.theme.NeonGlamPink
import com.example.ui.theme.NeonPurple
import com.example.ui.theme.SpaceDark
import com.example.ui.theme.SpaceSurface
import com.example.ui.theme.SurfaceCard
import kotlin.math.sin

enum class GarageTool {
    PEN, BUCKET, ERASER
}

@Composable
fun GarageModal(
    state: GameState,
    viewModel: GameViewModel,
    onClose: () -> Unit
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    var selectedTool by remember { mutableStateOf(GarageTool.PEN) }
    var selectedColorInt by remember { mutableIntStateOf(0xFF00E5FF.toInt()) }

    val palette = remember {
        listOf(
            0xFFFFD700.toInt(), // Gold
            0xFFFFFFFF.toInt(), // Pure White
            0xFF00E5FF.toInt(), // Cyber Cyan
            0xFFFF007F.toInt(), // Hot Pink
            0xFFFF0055.toInt(), // Ruby
            0xFFFF7F00.toInt(), // Neon Orange
            0xFFFFEB3B.toInt(), // Sun Yellow
            0xFF00FF66.toInt(), // Neon Green
            0xFF10B981.toInt(), // Emerald
            0xFF06B6D4.toInt(), // Sea Blue
            0xFF3B82F6.toInt(), // Royal Blue
            0xFF8B5CF6.toInt(), // Lavender
            0xFFA855F7.toInt(), // Electric Purple
            0xFFE040FB.toInt(), // Magenta
            0xFFB5179E.toInt(), // Deep Pink
            0xFFEC4899.toInt(), // Bubblegum
            0xFFF43F5E.toInt(), // Rose
            0xFFB91C1C.toInt(), // Crimson
            0xFF7C2D12.toInt(), // Bronze
            0xFF0F172A.toInt(), // Dark Navy
            0xFF1E293B.toInt(), // Slate
            0xFF64748B.toInt(), // Steel
            0xFF94A3B8.toInt(), // Chrome
            0xFF000000.toInt()  // Black
        )
    }

    val carUpgrades = state.carUpgrades
    val player = state.player
    val totalLevel = carUpgrades.getTotalLevel()

    val infiniteTransition = rememberInfiniteTransition(label = "garage_fx")
    val previewPulse by infiniteTransition.animateFloat(
        initialValue = 0.98f,
        targetValue = 1.02f,
        animationSpec = infiniteRepeatable(
            animation = tween(1400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable(onClick = onClose)
            .testTag("garage_modal"),
        contentAlignment = Alignment.Center
    ) {
        androidx.compose.foundation.Image(
            painter = androidx.compose.ui.res.painterResource(id = com.example.R.drawable.bg_main_menu),
            contentDescription = "Background",
            modifier = Modifier.fillMaxSize(),
            contentScale = androidx.compose.ui.layout.ContentScale.Crop
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xDD080214))
        )
        Card(
            modifier = Modifier
                .fillMaxWidth(0.96f)
                .fillMaxSize(0.94f)
                .clickable(enabled = false) {}
                .border(2.dp, MartianGold, RoundedCornerShape(20.dp)),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = SpaceDark)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(14.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("🏎️", fontSize = 22.sp)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "ГАРАЖ ВЛАДИКА 6969",
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Black,
                                fontFamily = FontFamily.Monospace,
                                color = MartianGold
                            )
                        }
                        Text(
                            text = "Пиксель-Арт Студия (32x32) & Тюнинг 5000 Уровней",
                            fontSize = 11.sp,
                            color = Color(0xFF94A3B8)
                        )
                    }

                    // Resource Badges + Close
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .background(SurfaceCard, RoundedCornerShape(8.dp))
                                .border(1.dp, GoldCoin.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("💰", fontSize = 12.sp)
                                Spacer(modifier = Modifier.width(3.dp))
                                Text(
                                    "${player.gold}",
                                    color = GoldCoin,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(6.dp))
                        Box(
                            modifier = Modifier
                                .background(SurfaceCard, RoundedCornerShape(8.dp))
                                .border(1.dp, Color(0xFF10B981).copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("🔋", fontSize = 12.sp)
                                Spacer(modifier = Modifier.width(3.dp))
                                Text(
                                    "${player.batteries}",
                                    color = Color(0xFF10B981),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .background(Color(0xFF334155), CircleShape)
                                .clickable { onClose() }
                                .testTag("garage_close_btn"),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("✕", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Tabs: 0 -> Canvas Studio (32x32), 1 -> Tuning 5000 Levels
                TabRow(
                    selectedTabIndex = selectedTab,
                    containerColor = SpaceSurface,
                    contentColor = MartianGold,
                    indicator = { tabPositions ->
                        TabRowDefaults.SecondaryIndicator(
                            Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                            color = MartianGold,
                            height = 3.dp
                        )
                    }
                ) {
                    Tab(
                        selected = selectedTab == 0,
                        onClick = { selectedTab = 0 },
                        text = {
                            Text(
                                "🖌️ ХОЛСТ 32x32",
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                color = if (selectedTab == 0) MartianGold else Color(0xFF94A3B8)
                            )
                        }
                    )
                    Tab(
                        selected = selectedTab == 1,
                        onClick = { selectedTab = 1 },
                        text = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    "⚡ ТЮНИНГ 5000 УР.",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    color = if (selectedTab == 1) MartianGold else Color(0xFF94A3B8)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Box(
                                    modifier = Modifier
                                        .background(HotPink, RoundedCornerShape(6.dp))
                                        .padding(horizontal = 4.dp, vertical = 1.dp)
                                ) {
                                    Text(
                                        "$totalLevel/5000",
                                        color = Color.White,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Black
                                    )
                                }
                            }
                        }
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Tab Content
                when (selectedTab) {
                    0 -> PixelStudioTab(
                        state = state,
                        viewModel = viewModel,
                        selectedTool = selectedTool,
                        onSelectTool = { selectedTool = it },
                        selectedColorInt = selectedColorInt,
                        onSelectColor = { selectedColorInt = it },
                        palette = palette,
                        previewPulse = previewPulse
                    )
                    1 -> TuningTab(
                        state = state,
                        viewModel = viewModel
                    )
                }
            }
        }
    }
}

@Composable
private fun PixelStudioTab(
    state: GameState,
    viewModel: GameViewModel,
    selectedTool: GarageTool,
    onSelectTool: (GarageTool) -> Unit,
    selectedColorInt: Int,
    onSelectColor: (Int) -> Unit,
    palette: List<Int>,
    previewPulse: Float
) {
    val carCustomization = state.carCustomization
    val canvasSize = CarCustomization.CANVAS_SIZE

    Column(modifier = Modifier.fillMaxSize()) {
        // Toolbar: Tools + Presets
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Tools (Pen, Bucket, Eraser)
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                listOf(
                    Triple(GarageTool.PEN, "✏️", "Перо"),
                    Triple(GarageTool.BUCKET, "🪣", "Заливка"),
                    Triple(GarageTool.ERASER, "🧹", "Ластик")
                ).forEach { (tool, icon, label) ->
                    val isSelected = selectedTool == tool
                    Box(
                        modifier = Modifier
                            .background(
                                if (isSelected) NeonGlamPink else SurfaceCard,
                                RoundedCornerShape(8.dp)
                            )
                            .border(
                                1.dp,
                                if (isSelected) Color.White else Color(0xFF334155),
                                RoundedCornerShape(8.dp)
                            )
                            .clickable { onSelectTool(tool) }
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                            .testTag("tool_${tool.name.lowercase()}"),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(icon, fontSize = 13.sp)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                label,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isSelected) Color.White else Color(0xFFCBD5E1)
                            )
                        }
                    }
                }
            }

            // Preset selector dropdown / buttons
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                Button(
                    onClick = { viewModel.loadCarPreset(CarCustomization.createDefault()) },
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = CyberCyan),
                    shape = RoundedCornerShape(6.dp),
                    modifier = Modifier.height(30.dp)
                ) {
                    Text("🏎 Влад", fontSize = 10.sp, color = Color.Black, fontWeight = FontWeight.Bold)
                }
                Button(
                    onClick = { viewModel.loadCarPreset(CarCustomization.createGoldRolls()) },
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MartianGold),
                    shape = RoundedCornerShape(6.dp),
                    modifier = Modifier.height(30.dp)
                ) {
                    Text("👑 Роллс", fontSize = 10.sp, color = Color.Black, fontWeight = FontWeight.Bold)
                }
                Button(
                    onClick = { viewModel.loadCarPreset(CarCustomization.createRainbowPride()) },
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = HotPink),
                    shape = RoundedCornerShape(6.dp),
                    modifier = Modifier.height(30.dp)
                ) {
                    Text("🌈 Pride", fontSize = 10.sp, color = Color.White, fontWeight = FontWeight.Bold)
                }
                Button(
                    onClick = { viewModel.clearCarCanvas() },
                    contentPadding = PaddingValues(horizontal = 6.dp, vertical = 4.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF334155)),
                    shape = RoundedCornerShape(6.dp),
                    modifier = Modifier.height(30.dp)
                ) {
                    Text("🗑", fontSize = 10.sp, color = Color.White)
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Color Palette (Scrollable Row)
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .background(SurfaceCard, RoundedCornerShape(10.dp))
                .padding(horizontal = 8.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            items(palette) { colorInt ->
                val isSelected = selectedColorInt == colorInt && selectedTool != GarageTool.ERASER
                Box(
                    modifier = Modifier
                        .size(26.dp)
                        .background(Color(colorInt), CircleShape)
                        .border(
                            if (isSelected) 2.5.dp else 1.dp,
                            if (isSelected) Color.White else Color(0x66FFFFFF),
                            CircleShape
                        )
                        .clickable {
                            onSelectColor(colorInt)
                            if (selectedTool == GarageTool.ERASER) onSelectTool(GarageTool.PEN)
                        }
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Main 32x32 Interactive Drawing Canvas + Live Preview Card
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // 32x32 Grid Canvas (Left / Major)
            Box(
                modifier = Modifier
                    .weight(1.3f)
                    .fillMaxSize()
                    .background(Color(0xFF0F0A1C), RoundedCornerShape(12.dp))
                    .border(1.5.dp, CyberCyan.copy(alpha = 0.7f), RoundedCornerShape(12.dp))
                    .clip(RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                BoxWithConstraints(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(6.dp),
                    contentAlignment = Alignment.Center
                ) {
                    val sizePx = minOf(maxWidth, maxHeight)

                    Canvas(
                        modifier = Modifier
                            .size(sizePx)
                            .aspectRatio(1f)
                            .pointerInput(selectedTool, selectedColorInt, carCustomization) {
                                detectTapGestures { offset ->
                                    val cellW = sizePx.toPx() / canvasSize
                                    val col = (offset.x / cellW).toInt().coerceIn(0, canvasSize - 1)
                                    val row = (offset.y / cellW).toInt().coerceIn(0, canvasSize - 1)
                                    when (selectedTool) {
                                        GarageTool.PEN -> viewModel.setCarPixel(col, row, selectedColorInt)
                                        GarageTool.ERASER -> viewModel.setCarPixel(col, row, 0)
                                        GarageTool.BUCKET -> viewModel.fillCarPixels(col, row, selectedColorInt)
                                    }
                                }
                            }
                            .pointerInput(selectedTool, selectedColorInt, carCustomization) {
                                detectDragGestures { change, _ ->
                                    change.consume()
                                    val cellW = sizePx.toPx() / canvasSize
                                    val col = (change.position.x / cellW).toInt().coerceIn(0, canvasSize - 1)
                                    val row = (change.position.y / cellW).toInt().coerceIn(0, canvasSize - 1)
                                    when (selectedTool) {
                                        GarageTool.PEN -> viewModel.setCarPixel(col, row, selectedColorInt)
                                        GarageTool.ERASER -> viewModel.setCarPixel(col, row, 0)
                                        GarageTool.BUCKET -> {}
                                    }
                                }
                            }
                            .testTag("pixel_grid_canvas")
                    ) {
                        val cellW = size.width / canvasSize
                        val cellH = size.height / canvasSize

                        // Checkerboard background pattern for transparency
                        for (y in 0 until canvasSize) {
                            for (x in 0 until canvasSize) {
                                val isEven = (x + y) % 2 == 0
                                val bg = if (isEven) Color(0xFF1E1538) else Color(0xFF140D27)
                                drawRect(
                                    color = bg,
                                    topLeft = Offset(x * cellW, y * cellH),
                                    size = Size(cellW, cellH)
                                )

                                // Pixel from state
                                val pixelInt = carCustomization.pixels.getOrElse(y * canvasSize + x) { 0 }
                                if (pixelInt != 0) {
                                    drawRect(
                                        color = Color(pixelInt),
                                        topLeft = Offset(x * cellW, y * cellH),
                                        size = Size(cellW, cellH)
                                    )
                                }
                            }
                        }

                        // Subtle grid lines
                        val gridColor = Color(0x22FFFFFF)
                        for (i in 0..canvasSize) {
                            val lineX = i * cellW
                            drawLine(gridColor, Offset(lineX, 0f), Offset(lineX, size.height), 0.5f)
                            val lineY = i * cellH
                            drawLine(gridColor, Offset(0f, lineY), Offset(size.width, lineY), 0.5f)
                        }
                    }
                }
            }

            // Live Preview & Spec Card (Right)
            Card(
                modifier = Modifier
                    .weight(0.9f)
                    .fillMaxSize()
                    .border(1.dp, MartianGold.copy(alpha = 0.5f), RoundedCornerShape(12.dp)),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceCard)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "ПРЕВЬЮ В БОЮ",
                            fontWeight = FontWeight.Black,
                            fontSize = 12.sp,
                            color = MartianGold,
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = carCustomization.presetName,
                            fontSize = 11.sp,
                            color = CyberCyan
                        )
                    }

                    // Scaled animated preview canvas with mounted tuning attachments
                    Box(
                        modifier = Modifier
                            .size(130.dp)
                            .scale(previewPulse)
                            .background(Color(0xFF0F041D), RoundedCornerShape(12.dp))
                            .border(1.dp, HotPink.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                            .padding(8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            val carW = size.width
                            val carH = size.height
                            val cellW = carW / canvasSize
                            val cellH = carH / canvasSize

                            // Draw the 32x32 pixels
                            for (y in 0 until canvasSize) {
                                for (x in 0 until canvasSize) {
                                    val pixelInt = carCustomization.pixels.getOrElse(y * canvasSize + x) { 0 }
                                    if (pixelInt != 0) {
                                        drawRect(
                                            color = Color(pixelInt),
                                            topLeft = Offset(x * cellW, y * cellH),
                                            size = Size(cellW, cellH)
                                        )
                                    }
                                }
                            }

                            // Bullbar attachment if level > 0
                            if (state.carUpgrades.bullbarLevel > 0) {
                                drawRoundRect(
                                    color = Color(0xFFFFD700),
                                    topLeft = Offset(carW - 8f, carH * 0.35f),
                                    size = Size(7f, carH * 0.3f),
                                    cornerRadius = CornerRadius(2f, 2f)
                                )
                                drawCircle(Color(0xFFFF007F), 3.5f, Offset(carW - 1f, carH * 0.40f))
                                drawCircle(Color(0xFFFF007F), 3.5f, Offset(carW - 1f, carH * 0.60f))
                            }

                            // Spoiler attachment if level > 0
                            if (state.carUpgrades.spoilerLevel > 0) {
                                drawRect(
                                    color = Color(0xFF00F0FF),
                                    topLeft = Offset(1f, carH * 0.28f),
                                    size = Size(4f, carH * 0.44f)
                                )
                            }

                            // Rainbow nitro trail if level > 0
                            if (state.carUpgrades.nitroLevel > 0) {
                                val rainbow = listOf(Color(0xFFFF0055), Color(0xFFFF7F00), Color(0xFFFFD700), Color(0xFF00FF66), Color(0xFF00E5FF))
                                for (i in 0..4) {
                                    drawCircle(
                                        color = rainbow[i].copy(alpha = 0.7f),
                                        radius = 2.5f,
                                        center = Offset(2f - (i * 4f), carH * 0.5f + (i % 2 * 3f - 1.5f))
                                    )
                                }
                            }
                        }
                    }

                    // Quick specs
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        SpecRow("💥 Таран:", "+${state.carUpgrades.getBonusDamage().toInt()} урона", Color(0xFFFFD700))
                        SpecRow("🚀 Скорость:", "+${state.carUpgrades.getBonusSpeed().toInt()} км/ч", CyberCyan)
                        SpecRow("🛡 Броня:", "+${state.carUpgrades.armorLevel} проч.", Color(0xFF10B981))
                    }
                }
            }
        }
    }
}

@Composable
private fun TuningTab(
    state: GameState,
    viewModel: GameViewModel
) {
    val carUpgrades = state.carUpgrades
    val player = state.player
    val totalLevel = carUpgrades.getTotalLevel()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // Overall Progress Banner
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.5.dp, MartianGold, RoundedCornerShape(14.dp)),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceCard)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                "ОБЩАЯ ПРОКАЧКА АВТОМОБИЛЯ",
                                fontWeight = FontWeight.Black,
                                fontSize = 13.sp,
                                color = MartianGold
                            )
                            Text(
                                "5 веток улучшений по 1000 уровней каждая",
                                fontSize = 11.sp,
                                color = Color(0xFF94A3B8)
                            )
                        }
                        Text(
                            "$totalLevel / 5000",
                            fontWeight = FontWeight.Black,
                            fontSize = 16.sp,
                            fontFamily = FontFamily.Monospace,
                            color = CyberCyan
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    LinearProgressIndicator(
                        progress = { totalLevel / 5000f },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(10.dp)
                            .clip(RoundedCornerShape(5.dp)),
                        color = HotPink,
                        trackColor = Color(0xFF1E1538)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Таран: +${carUpgrades.getBonusDamage().toInt()} DMG", fontSize = 10.sp, color = Color(0xFFFFD700))
                        Text("Скорость: +${carUpgrades.getBonusSpeed().toInt()}", fontSize = 10.sp, color = CyberCyan)
                        Text("Время: +${String.format(java.util.Locale.US, "%.1f", carUpgrades.getBonusLifetime())}s", fontSize = 10.sp, color = Color(0xFF10B981))
                    }
                }
            }
        }

        // 5 Tuning Branch Cards
        items(CarUpgradeType.values().toList()) { upgradeType ->
            val currentLvl = carUpgrades.getLevel(upgradeType)
            val goldCost = carUpgrades.getGoldCost(upgradeType)
            val batteryCost = carUpgrades.getBatteryCost(upgradeType)
            val canAfford = player.gold >= goldCost && player.batteries >= batteryCost && currentLvl < upgradeType.maxLevel

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        1.dp,
                        if (canAfford) CyberCyan.copy(alpha = 0.6f) else Color(0xFF334155),
                        RoundedCornerShape(12.dp)
                    ),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceCard)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .background(Color(0xFF1E1538), CircleShape)
                            .border(1.dp, MartianGold.copy(alpha = 0.5f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(upgradeType.iconEmoji, fontSize = 22.sp)
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = upgradeType.title,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = Color.White
                            )
                            Text(
                                text = "$currentLvl / ${upgradeType.maxLevel}",
                                fontWeight = FontWeight.Black,
                                fontSize = 11.sp,
                                fontFamily = FontFamily.Monospace,
                                color = MartianGold
                            )
                        }

                        Text(
                            text = upgradeType.description,
                            fontSize = 10.sp,
                            color = Color(0xFF94A3B8),
                            lineHeight = 13.sp
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        LinearProgressIndicator(
                            progress = { currentLvl / upgradeType.maxLevel.toFloat() },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)
                                .clip(RoundedCornerShape(3.dp)),
                            color = NeonGlamPink,
                            trackColor = Color(0xFF1E1538)
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    // Purchase Button (+1 Level per click)
                    Button(
                        onClick = { viewModel.upgradeCarPart(upgradeType) },
                        enabled = canAfford,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (canAfford) NeonGlamPink else Color(0xFF334155),
                            disabledContainerColor = Color(0xFF1E293B)
                        ),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                        modifier = Modifier
                            .height(44.dp)
                            .testTag("upgrade_${upgradeType.name.lowercase()}_btn")
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("+1 УР.", fontSize = 11.sp, fontWeight = FontWeight.Black, color = Color.White)
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("$goldCost💰", fontSize = 9.sp, color = GoldCoin)
                                Spacer(modifier = Modifier.width(2.dp))
                                Text("$batteryCost🔋", fontSize = 9.sp, color = Color(0xFF10B981))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SpecRow(label: String, value: String, color: Color) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, fontSize = 10.sp, color = Color(0xFF94A3B8))
        Text(value, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = color)
    }
}
