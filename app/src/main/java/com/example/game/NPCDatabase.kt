package com.example.game

import androidx.compose.ui.graphics.Color
import kotlin.math.abs

object NPCDatabase {

    private val baseNames = listOf(
        "Жульен «Французский Люкс»", "Батя «Титановый Поршень»", "Маркус «Стальной Бицепс»", "DJ Малик «Ритм Марса»",
        "Тайрон «Неоновый Дрифт»", "Антуан «Шелковый Кутюр»", "Сэмми «Золотой Гриллз»", "Джимми «Битмейкер-Ударник»",
        "Бруно «Жим 600 Кг»", "Леон «Алмазный Магнат»", "Шеф Мишель «Марсианский Гурман»", "Бобби «Кибер-Взломщик»",
        "Жан-Поль «Парфюмер Люкса»", "Кенни «Закись Азота»", "Дариус «Черный Бархат»", "Рашид «Лавовый Гонщик»",
        "Пьер «Золотая Татуировка»", "Закир «Крипто-Барон»", "Гоша «Титановый Кузнец»", "Матео «Инфразвуковой Бас»",
        "Гаспар «Сомелье Дом Периньон»", "Виктор «Каратель Хейтеров»", "Деннис «Лазерное Шоу»", "Андре «Подиумный Вог»",
        "Феликс «Квантовый Взломщик»", "Рафаэль «Автотюн-Султан»", "Кристиан «Королевский Мантийник»", "Омар «Реактивный Бит»",
        "Брэдли «Голливудский Клипмейкер»", "Люка «Консьерж 800-го Этажа»", "Демьяна «Спецназ Люкса»", "Жак «Шелковый Шарф»",
        "Гийом «Марсианский Бассгитарист»", "Паскаль «Дизайнер Очков»", "Лоран «Бармен Розового Шампанского»", "Этьен «Бриллиантовый Часовщик»",
        "Франсуа «Трэп-Академик»", "Анри «Гламурный Пилот»", "Матис «Золотой Стилист»", "Клеман «Дрифт-Инструктор»",
        "Арно «Шоураннер Марса»", "Оливье «Тюремный Кузен»", "Бертран «Марсианский Ювелир»", "Себастьян «Хореограф Вога»",
        "Максим «Квантовый Звукорежиссер»", "Фабьен «Элитный Сомелье»", "Тьерри «Мастер Кожаных Ку курток»", "Реми «Турбо-Инженер»",
        "Седрик «Марсианский Неон»", "Ги «Защитник Пентхауса»"
    )

    private val extraRoles = listOf(
        "Личный бодигард Влада", "Шеф-механик 6969 гиперкаров", "Продюсер автотюна", "Главный сомелье Марса",
        "Мастер бриллиантовых гриллзов", "Шоураннер марсианских показов мод", "Дирижер 808 басов", "Крипто-инвестор токена \$VLAD",
        "Хранитель золотых ключей 800-го этажа", "Шеф-повар марсианской икры", "Каскадер турбо-болидов", "Дизайнер леопардовых шуб"
    )

    private val funnyDialogues = listOf(
        "Владик, ты самый гениальный рэпер галактики! Роботы-цензоры ломают датчики от одного творения!",
        "Батя и Жульен заправили гиперкары концентрированным неоном. 800-й этаж готов к великому реду!",
        "Наши бриллианты видны даже из космоса. Цензура рассыпалась в прах!",
        "Шампанское лилось по мраморной лестнице 800-го этажа! Марс официально принадлежит нам!",
        "Твой автотюн вибрирует на частоте чистой победы! Никто не сможет остановить флекс Влада!"
    )

    fun getAllNPCs(): List<FriendlyNPC> {
        val list = mutableListOf<FriendlyNPC>()

        // 1. Julien
        list.add(
            FriendlyNPC(
                id = "npc_julien",
                name = "Жульен «Французский Люкс»",
                role = "Личный Бодигард & Французский Саунд-Продюсер Влада",
                x = -90f,
                y = 80f,
                color = Color(0xFFE040FB),
                dialogues = listOf(
                    DialogueNode(
                        speaker = "Жульен «Французский Люкс»",
                        text = "О-ля-ля, мон шер Владик! Париж нервно курит в сторонке по сравнению с твоим 800-этажным особняком! Мы свели марсианский трэп с французским шансоном!",
                        options = listOf(
                            DialogueOption("Жульен, как наш новый трек?", nextNodeIndex = 1, rewardGold = 100),
                            DialogueOption("Расскажи про триумф моды в Париже!", nextNodeIndex = 2),
                            DialogueOption("Активировать финал 'Парижский Триумф'!", nextNodeIndex = 3, unlocksEndingId = 8)
                        )
                    ),
                    DialogueNode(
                        speaker = "Жульен «Французский Люкс»",
                        text = "Трек — чистый огонь! Автотюн и 808 бас сносят всех цензоров Марса!",
                        options = listOf(DialogueOption("Слава стилю!", nextNodeIndex = -1))
                    ),
                    DialogueNode(
                        speaker = "Жульен «Французский Люкс»",
                        text = "Ты вышел в леопардовой шубе и с резиновой булавой. Папарацци аплодировали стоя!",
                        options = listOf(DialogueOption("Мы короли стиля!", nextNodeIndex = -1))
                    ),
                    DialogueNode(
                        speaker = "Жульен «Французский Люкс»",
                        text = "ШАМПАНСКОЕ РЕКОЙ! Пентхаус объявляется столицей моды вселенной!",
                        options = listOf(DialogueOption("МАРС НАШ!", nextNodeIndex = -1, unlocksEndingId = 8))
                    )
                )
            )
        )

        // 2. Batya
        list.add(
            FriendlyNPC(
                id = "npc_batya",
                name = "Батя «Титановый Поршень»",
                role = "Шеф Гаража 6969 Суперкаров & Тюремный Ветеран",
                x = 100f,
                y = -80f,
                color = Color(0xFFFF9100),
                dialogues = listOf(
                    DialogueNode(
                        speaker = "Батя «Титановый Поршень»",
                        text = "Здорово, Владик! Помнишь чифирь в тюрячке? Теперь у нас 6969 гиперкаров и 800 этажей роскоши! Погнали раскатывать цензоров!",
                        options = listOf(
                            DialogueOption("Батя, как поживает автопарк?", nextNodeIndex = 1, rewardGold = 120),
                            DialogueOption("Напомни главный тюремный закон!", nextNodeIndex = 2),
                            DialogueOption("Запустить форсаж 6969 гиперкаров!", nextNodeIndex = 3, unlocksEndingId = 11)
                        )
                    ),
                    DialogueNode(
                        speaker = "Батя «Титановый Поршень»",
                        text = "Все 6969 ракет заправлены неоном! Газ в пол — и Марс дрожит!",
                        options = listOf(DialogueOption("Красава, Батя!", nextNodeIndex = -1))
                    ),
                    DialogueNode(
                        speaker = "Батя «Титановый Поршень»",
                        text = "Закон №1: флекси на максимум и никогда не опускай корону!",
                        options = listOf(DialogueOption("Ни шагу назад!", nextNodeIndex = -1))
                    ),
                    DialogueNode(
                        speaker = "Батя «Титановый Поршень»",
                        text = "ПОЕХАЛИ! Батя за рулем гиперкара с номером 6969!",
                        options = listOf(DialogueOption("ПОЛНЫЙ ТРИУМФ!", nextNodeIndex = -1, unlocksEndingId = 11))
                    )
                )
            )
        )

        // Generate 250 distinct main NPCs
        val colors = listOf(
            Color(0xFF00E5FF), Color(0xFFFF4081), Color(0xFFFFD700),
            Color(0xFF7C4DFF), Color(0xFF00E676), Color(0xFFFF9100), Color(0xFFE040FB)
        )

        for (i in 3..250) {
            val npc = generateNpcByIndex(i)
            list.add(npc)
        }

        return list
    }

    fun generateNpcByIndex(index: Int): FriendlyNPC {
        val nameBase = baseNames[(index - 1) % baseNames.size]
        val name = if (index > baseNames.size) "$nameBase #$index" else nameBase
        val role = extraRoles[index % extraRoles.size]
        val story = funnyDialogues[index % funnyDialogues.size]
        val endingTarget = (index % 100) + 1
        val color = listOf(
            Color(0xFF00E5FF), Color(0xFFFF4081), Color(0xFFFFD700),
            Color(0xFF7C4DFF), Color(0xFF00E676), Color(0xFFFF9100), Color(0xFFE040FB)
        )[index % 7]

        val angle = (index * 0.25f)
        val radius = 120f + (index % 10) * 45f
        val posX = (kotlin.math.cos(angle) * radius).toFloat()
        val posY = (kotlin.math.sin(angle) * radius).toFloat()

        return FriendlyNPC(
            id = "npc_generated_$index",
            name = name,
            role = role,
            x = posX,
            y = posY,
            color = color,
            dialogues = listOf(
                DialogueNode(
                    speaker = name,
                    text = "Салют, королю Марса Владику! Я — $name ($role). $story",
                    options = listOf(
                        DialogueOption("Поделись марсианским золотом!", nextNodeIndex = 1, rewardGold = 60 + (index % 10) * 15),
                        DialogueOption("Расскажи секрет флекса!", nextNodeIndex = 2),
                        DialogueOption("Открыть главу #$endingTarget в хронике!", nextNodeIndex = 3, unlocksEndingId = endingTarget)
                    )
                ),
                DialogueNode(
                    speaker = name,
                    text = "Держи золото, Владик! Пентхаус гудёт в твою честь!",
                    options = listOf(DialogueOption("Вперед к победам!", nextNodeIndex = -1))
                ),
                DialogueNode(
                    speaker = name,
                    text = "Секрет прост: всегда носи золотую цепь и читай трэп с чистой душой!",
                    options = listOf(DialogueOption("Золотые слова!", nextNodeIndex = -1))
                ),
                DialogueNode(
                    speaker = name,
                    text = "ГЛАВА ЗАПИСАНА! Финал #$endingTarget разблокирован!",
                    options = listOf(DialogueOption("ТРИУМФ ВЛАДИКА!", nextNodeIndex = -1, unlocksEndingId = endingTarget))
                )
            )
        )
    }

    fun searchNpcs(query: String, allNpcs: List<FriendlyNPC>): List<FriendlyNPC> {
        val trimmed = query.trim().lowercase()
        if (trimmed.isEmpty()) return allNpcs

        val parsedIndex = trimmed.filter { it.isDigit() }.toIntOrNull()
        if (parsedIndex != null && parsedIndex in 1..10000) {
            val exactNpc = generateNpcByIndex(parsedIndex)
            return listOf(exactNpc) + allNpcs.filter { it.name.lowercase().contains(trimmed) || it.role.lowercase().contains(trimmed) }
        }

        return allNpcs.filter {
            it.name.lowercase().contains(trimmed) ||
                    it.role.lowercase().contains(trimmed) ||
                    it.id.lowercase().contains(trimmed)
        }
    }
}
