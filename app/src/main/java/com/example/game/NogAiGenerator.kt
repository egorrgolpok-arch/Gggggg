package com.example.game

import androidx.compose.ui.graphics.Color
import kotlin.random.Random

object NogAiGenerator {

    private val namePrefixes = listOf(
        "Квантовый", "Нейронный", "Синтетический", "Гипер-Интеллектуальный",
        "Абсолютный", "Платиновый", "Неоновый", "Гламурный", "Космический", "Титановый"
    )

    private val nameNouns = listOf(
        "Артефакт", "Процессор", "Резонатор", "Синтезатор", "Ускоритель",
        "Излучатель", "Модуль", "Конденсатор", "Стабилизатор", "Усилитель Баса"
    )

    private val descTemplates = listOf(
        "Создано нейросетью nOG AI с использованием квантовых алгоритмов марсианского пентхауса.",
        "Эксклюзивный артефакт забега, излучающий волны чистого хайпа и автотюна.",
        "Превосходит любые земные аналоги по плотности энергии и стилю.",
        "Заряжено энергией 6969 гиперкаров и суверенным флексом Владика.",
        "Автоматически подавляет любые попытки натуралов в радиусе 800 этажей."
    )

    fun getDynamicPricing(): Pair<Int, Int> {
        val fiveHourBlock = System.currentTimeMillis() / (5 * 3600 * 1000L)
        val goldCost = 35 + ((fiveHourBlock * 17) % 65).toInt()
        val diamondCost = 3 + ((fiveHourBlock * 7) % 12).toInt()
        return Pair(goldCost, diamondCost)
    }

    fun generateNogAiWeapon(floor: Int, seedId: Int): Weapon {
        val prefix = namePrefixes[seedId % namePrefixes.size]
        val noun = nameNouns[(seedId * 7) % nameNouns.size]
        val name = "🤖 nOG AI: $prefix $noun #${seedId}"
        val desc = descTemplates[seedId % descTemplates.size]

        val type = WeaponType.values()[seedId % WeaponType.values().size]
        val baseDmg = 60f + floor * 15f + (seedId % 50)

        return Weapon(
            type = type,
            name = name,
            description = desc,
            damage = baseDmg,
            attackSpeed = 2.6f,
            range = 320f,
            knockback = 150f,
            color = Color(0xFF00F0FF),
            rarity = ItemRarity.LEGENDARY,
            level = floor
        )
    }

    fun generateNogAiArtifact(floor: Int, seedId: Int): Artifact {
        val prefix = namePrefixes[seedId % namePrefixes.size]
        val noun = nameNouns[(seedId * 11) % nameNouns.size]
        val name = "🤖 nOG AI: $prefix $noun #${seedId}"
        val desc = descTemplates[seedId % descTemplates.size]

        val hp = 60f + (seedId % 100)
        val spd = 20f + (seedId % 40)
        val dmg = 15f + (seedId % 30)
        val hype = 0.5f + (seedId % 5) * 0.1f

        return Artifact(
            id = "nog_art_$seedId",
            name = name,
            description = "+${hp.toInt()} HP, +${spd.toInt()} Скорости, +${dmg.toInt()} Урона (nOG AI)",
            rarity = ItemRarity.LEGENDARY,
            bonusHp = hp,
            bonusSpeed = spd,
            bonusDamage = dmg,
            bonusHypeRate = hype,
            loreStory = desc,
            level = floor
        )
    }
}
