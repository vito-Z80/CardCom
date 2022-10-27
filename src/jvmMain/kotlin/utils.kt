import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layout
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import gson.NewCard
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random


class Index(val id: Int)


/**
 *  кнопка с всплывающим списком
 *  @param card - карта для которой кнопка
 *  @param items - список [String]
 *  @param text - текст [() -> String]
 *  @param popupClickable - обработка нажатия по списку
 *  @param fontSize - размер шрифта
 *  @param enabled - состояние кликабельности кнопок выше кнопки value. Отключение кнопок player, structure если в value цифровое значение
 */
@Composable
fun popupButton(
    card: NewCard?,
    items: List<String>,
    text: () -> String,
    popupClickable: (s: String) -> Unit,
    fontSize: Float = 18f,
    enabled: MutableState<Boolean> = mutableStateOf(true),
               ) {
    var visible by remember { mutableStateOf(false) }

    if (card == null) return
    Text(
        text = text.invoke(),
        modifier = Modifier.padding(4f.dp).defaultMinSize(96f.dp, 20f.dp).background(Color.LightGray)
            .border(1f.dp, if (enabled.value) Color.DarkGray else Color(176, 176, 176)).clickable(
                enabled = enabled.value,
                                                                                                 ) {
                visible = true
            },
        fontSize = fontSize.sp,
        textAlign = TextAlign.Center,
        color = if (enabled.value) Color.DarkGray else Color(176, 176, 176)
        )
    DropdownMenu(
        expanded = visible, onDismissRequest = { visible = false },
                ) {
        items.forEach {
            DropdownMenuItem(onClick = {
                popupClickable.invoke(it)
                visible = false
            }) {
                Text(text = it, fontSize = fontSize.sp)
            }
        }
    }
}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
@Composable
fun button(
    text: String,
    onClick: () -> Unit,
    fontSize: Float = 18f,
    enabled: MutableState<Boolean> = mutableStateOf(true),
          ) {
    Text(
        text = text,
        modifier = Modifier.padding(4f.dp).defaultMinSize(96f.dp, 20f.dp).background(Color.LightGray)
            .border(1f.dp, if (enabled.value) Color.DarkGray else Color(176, 176, 176)).clickable(
                enabled = enabled.value,
                                                                                                 ) {
                onClick.invoke()
            },
        fontSize = fontSize.sp,
        textAlign = TextAlign.Center,
        color = if (enabled.value) Color.DarkGray else Color(176, 176, 176)
        )
}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

/**
 * Эфект описания/условия карты
 */
@Composable
fun effect(card: NewCard?, eff: NewCard.Effect?) {
    var expander by remember { mutableStateOf(false) }

    DropdownMenu(
        expanded = expander, onDismissRequest = {
            expander = false
        }, modifier = Modifier
                ) {
        Variant.values().forEachIndexed { index, variant ->
            DropdownMenuItem(
                onClick = {
                    eff?.variant?.value = variant//.name()
                    expander = false
                }, modifier = Modifier
                            ) {
                Column {
                    Text(text = variant.name(), color = Color.Blue)
                    Divider()
                }
            }
        }
    }

    Row(verticalAlignment = Alignment.CenterVertically) {
        ClickableText(
            // "variant" is "Undefined" = "General"
            text = AnnotatedString(eff?.variant?.value?.name() ?: Message.UNDEFINED),
            modifier = Modifier.rotate(-90f).textVertical(),
            style = TextStyle(color = Color.Blue),
            onClick = {
                expander = true
            })

        Column(modifier = Modifier.fillMaxHeight(), verticalArrangement = Arrangement.Center) {
            if (eff?.variant?.value == null || eff.variant.value == Variant.GENERAL || eff.variant.value == Variant.ASSIGN) {
                effectLabel(card, eff?.player, Player.values().map { it.name() }, Player.PLAYER.name())
                effectLabel(card, eff?.structure, Structure.values().map { it.name() }, Message.STRUCTURE)
                inputDigit(eff?.value)
            } else if (eff.variant.value == Variant.GET_HALF) {
                eff.player.value = Player.ENEMY.name()
                effectLabel(
                    card, eff.player, Player.values().map { it.name() }, Player.PLAYER.name(), mutableStateOf(false)
                           )
                effectLabel(card, eff.structure, Structure.values().map { it.name() }, Message.STRUCTURE)
                inputDigit(eff.value)
            } else {
                effectLabel(card, eff.structure, Structure.values().map { it.name() }, Message.STRUCTURE)
            }
        }
    }
}

@Composable
fun effectLabel(
    card: NewCard?,
    value: MutableState<String?>?,
    items: List<String>,
    text: String,
    enabled: MutableState<Boolean> = mutableStateOf(true),
               ) {
    Row {
        popupButton(card = card,
                    items = items,
                    text = fun() = value?.value ?: Message.EMPTY,
                    popupClickable = fun(s: String) { value?.value = s },
                    enabled = enabled
                   )
        Text(
            modifier = Modifier.align(Alignment.CenterVertically),
            text = text,
            fontSize = 12.sp,
            )
    }
}


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//  https://stackoverflow.com/questions/70057396/how-to-show-vertical-text-with-proper-size-layout-in-jetpack-compose
fun Modifier.textVertical() = layout { measurable, constraints ->
    val placeable = measurable.measure(constraints)
    layout(placeable.height, placeable.width) {
        placeable.place(
            x = -(placeable.width / 2 - placeable.height / 2), y = -(placeable.height / 2 - placeable.width / 2)
                       )
    }
}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// TODO любые символы после цифр должны быть ошибкой
private val reg2 = Regex("(?=[^-+0-9])")
val regOnlyDigit = Regex("[^0-9]")

@Composable
fun inputDigit(inputText: MutableState<String?>?) {

    var error by remember { mutableStateOf(false) }
    // TODO error должен влиять на все окно создания карты (нельзя создать карту если есть ошибки в вводах)

    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
        BasicTextField(
            value = inputText?.value ?: Message.EMPTY, onValueChange = { it ->
                inputText?.value = it
                error = reg2.containsMatchIn(it)
            }, singleLine = true, maxLines = 1, modifier = Modifier.padding(4f.dp)

                .size(96f.dp, 20f.dp).border(
                    width = 1f.dp, color = if (error) {
                        Color.Red
                    } else {
                        Color.Green
                    }
                                            ).background(color = Color.LightGray).padding(2f.dp)
                      )
        Text(
            modifier = Modifier.align(Alignment.CenterVertically).padding(start = 4f.dp),
            text = Message.VALUE,
            fontSize = 12.sp,
            )
    }
}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
@Composable
fun BasicInput(inputText: MutableState<String?>?, regex: Regex) {
    var error by remember { mutableStateOf(false) }
    BasicTextField(
        value = inputText?.value ?: Message.EMPTY, onValueChange = { it ->
            inputText?.value = it
            error = regex.containsMatchIn(it)
        }, singleLine = true, maxLines = 1, modifier = Modifier.padding(4f.dp)

            .size(96f.dp, 20f.dp).border(
                width = 1f.dp, color = if (error) {
                    Color.Red
                } else {
                    Color.Green
                }
                                        ).background(color = Color.LightGray).padding(2f.dp)
                  )
}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

@Composable
fun ZxText(text: String, onClick: () -> Unit) {

    Text(text = text, style = MaterialTheme.typography.h6, modifier = Modifier.clickable { onClick.invoke() })


}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
fun hex2(char: Char) = "#" + String.format("%02X", char.code)

fun Char.hex() = "#" + String.format("%02X", this.code)
fun Byte.hex() = "#" + String.format("%02X", this)

fun String.toAsmLabel() = uppercase().replace(" ", "_")

fun String.toByte() = try {
    this.toInt().toByte()
} catch (e: Exception) {
    null
}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
fun Enum<*>.name() = name.lowercase().replace("_", " ").replaceFirstChar { it.uppercase() }

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
enum class BitMask {
    ORDER, WORD, BIT
}

// 8 битное значение бита по его индексу с маской
private val bit = listOf(0, 1, 2, 4, 8, 16, 32, 64)
fun Int.valueBy(bitMask: BitMask = BitMask.ORDER): Int {
    return when (bitMask) {
        BitMask.WORD -> (this shl 1) and 15     // 0,2,4,6,8,10,12,14
        BitMask.BIT -> bit[this and 7]          // 0,1,2,4,8,16,32,64
        else -> this and 7                      // 0,1,2,3,4,5,6,7
    }
}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
fun Color.byResource(res: Structure) = when (res) {
    Structure.WALL -> Color.LightGray
    Structure.TOWER -> Color.DarkGray
    Structure.QUARRY -> Color(207, 95, 107, 255)
    Structure.BRICKS -> Color(207, 95, 107, 255)
    Structure.MAGIC -> Color(123, 168, 207, 255)
    Structure.GEMS -> Color(123, 168, 207, 255)
    Structure.DUNGEON -> Color(126, 163, 126, 255)
    Structure.RECRUITS -> Color(126, 163, 126, 255)
    else -> Color.White
}
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

/*
Вероятность выпадения карт:
	1) Таблица вероятностей содержит значение (1 байт) на каждую карту, индексы соответствуют.
		значения вероятностей карт  [1-3]
	2) Копия таблицы вероятностей.

Процесс выдачи карты.
	а) Получаем рандомный индекс карты не превышающий кол-во существующих карт в игре - 1.
	б) По индексу получаем значение вероятности из таблицы(2), отнимаем единицу и сохраняем результат.
		Если значение равно нулю, то выдать карту с таким индексом и восстановить значение.
		Иначе пункт (а)

*/
const val CARDS_COUNT = 102

data class Probability(
    val probability: Int,    // вероятность выпадения карты
    var times: Int,          // сколько раз было рандомное обращение к карте пока ее не выдали.
    var tmp1: Int,
    var turn: Int,
                      ) {
    override fun toString(): String {
        return "P-$probability; T-$times; H-$turn"
    }
}


private val result = List<Probability>(CARDS_COUNT) {
    var r: Int
    while (true) {
        r = Random.nextBits(2)
        if (r != 0) break
    }
    r
    Probability(probability = r, times = 0, tmp1 = r, turn = 0)
}

private val seed = 891234781

private fun getCard() {
    while (true) {
        val id = Random.nextInt(0, CARDS_COUNT - 1)
        result[id].times++
        if (result[id].tmp1 == 0) {
            result[id].tmp1 = result[id].probability
            result[id].turn++
            break
        }
        result[id].tmp1--
        if (result[id].tmp1 == 0) {
            result[id].tmp1 = result[id].probability
            result[id].turn++
            break
        }
    }
}

fun probabilityTest() {
    println("Probabilities:")

    CoroutineScope(Dispatchers.Default).launch {
        while (true) {

            delay(1000)
            println(result.joinToString())
        }
    }

    CoroutineScope(Dispatchers.Default).launch {

        while (result.any { it.times == 0 }) {
            getCard()
            delay(1)
        }
    }


//    result.forEachIndexed { id, p ->
//        println("$id: P - ${p.probability}, T - ${p.times}")
//    }

}


fun cardProbability(prob: Int, cardCount: Int, cardId: Int) {

    var obr:Int = 0
    var p = prob
    while (p > 0) {
        while (Random.nextInt(CARDS_COUNT - 1) != cardId) {
            obr++
        }
        p--
    }
    println(obr)
}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
