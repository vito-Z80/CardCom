import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.Divider
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layout
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import gson.NewCard


val buttonSize = Size(96f, 20f)

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
    enabled: MutableState<Boolean> = mutableStateOf(true)
) {
    var visible by remember { mutableStateOf(false) }

    if (card == null) return
    Text(
        text = text.invoke(),
        modifier = Modifier
            .padding(4f.dp)
            .defaultMinSize(96f.dp, 20f.dp)
            .background(Color.LightGray)
            .border(1f.dp, if (enabled.value) Color.DarkGray else Color(176, 176, 176))
            .clickable(
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
            }
            ) {
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
    enabled: MutableState<Boolean> = mutableStateOf(true)
) {
    Text(
        text = text,
        modifier = Modifier
            .padding(4f.dp)
            .defaultMinSize(96f.dp, 20f.dp)
            .background(Color.LightGray)
            .border(1f.dp, if (enabled.value) Color.DarkGray else Color(176, 176, 176))
            .clickable(
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
        expanded = expander,
        onDismissRequest = {
            expander = false
        },
        modifier = Modifier
    ) {
        effectVariantList.forEachIndexed { index, variant ->
            DropdownMenuItem(
                onClick = {
                    eff?.variant?.value = variant
                    expander = false
                },
                modifier = Modifier
            ) {
                Column {
                    Text(text = variant, color = Color.Blue)
                    Divider()
                }
            }
        }
    }

    Row(verticalAlignment = Alignment.CenterVertically) {
        ClickableText(
            // "variant" is "Undefined" = "General"
            text = AnnotatedString(eff?.variant?.value ?: Message.UNDEFINED),
            modifier = Modifier
                .rotate(-90f)
                .textVertical(),
            style = TextStyle(color = Color.Blue),
            onClick = {
                expander = true
            }
        )

        Column(modifier = Modifier.fillMaxHeight(), verticalArrangement = Arrangement.Center) {
            if (eff?.variant?.value == null || eff.variant.value == effectVariantList[0]) {
                effectLabel(card, eff?.player, players, Message.PLAYER, Message.EMPTY)
                effectLabel(card, eff?.structure, structures, Message.STRUCTURE, Message.EMPTY)
                inputDigit(eff?.value)
            } else {
                effectLabel(card, eff.structure, structures, Message.STRUCTURE, Message.EMPTY)
            }
        }

    }
}

@Composable
fun effectLabel(card: NewCard?, value: MutableState<String?>?, items: List<String>, text: String, emptyText: String) {
    Row {
        popupButton(
            card = card,
            items = items,
            text = fun() = value?.value ?: emptyText,
            popupClickable = fun(s: String) { value?.value = s },
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
fun Modifier.textVertical() =
    layout { measurable, constraints ->
        val placeable = measurable.measure(constraints)
        layout(placeable.height, placeable.width) {
            placeable.place(
                x = -(placeable.width / 2 - placeable.height / 2),
                y = -(placeable.height / 2 - placeable.width / 2)
            )
        }
    }

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
private val reg2 = Regex("(?=[^-=+0-9])")

@Composable
fun inputDigit(inputText: MutableState<String?>?) {

    var error by remember { mutableStateOf(false) }
    // TODO error должен влиять на все окно создания карты (нельзя создать карту если есть ошибки в вводах)

    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
        BasicTextField(
            value = inputText?.value ?: Message.EMPTY,
            onValueChange = { it ->
                inputText?.value = it
                error = reg2.containsMatchIn(it)
            },
            singleLine = true,
            maxLines = 1,
            modifier = Modifier
                .padding(4f.dp)

                .size(96f.dp, 20f.dp)
                .border(
                    width = 1f.dp, color = if (error) {
                        Color.Red
                    } else {
                        Color.Green
                    }
                )
                .background(color = Color.LightGray)
                .padding(2f.dp)
        )
        Text(
            modifier = Modifier.align(Alignment.CenterVertically).padding(start = 4f.dp),
            text = Message.VALUE,
            fontSize = 12.sp,
        )
    }
}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
