import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import gson.NewCard

@Composable
fun condition(newCard: NewCard?) {

    var contentVisible by remember { mutableStateOf(false) }
    var clear by remember { mutableStateOf(false) }

    LaunchedEffect(clear) {
        if (clear) {
            newCard?.condition?.value = null
            clear = false
            contentVisible = false
        }

    }

    Column(
        modifier = Modifier
            .border(width = 1f.dp, color = Color.DarkGray)
            .background(color = Color(1f, 1f, 1f))
            .fillMaxWidth()
            .padding(4f.dp)
    ) {
        Button(onClick = {
            if (newCard?.condition?.value == null) {
                newCard?.condition?.value = NewCard.Condition()
            } else {
                // восстановить параметры при редактировании

            }
            contentVisible = !contentVisible

        }) {
            Text(text = "Condition")
        }

        AnimatedVisibility(visible = contentVisible, modifier = Modifier) {
            // CONDITION
            Column(modifier = Modifier.fillMaxWidth()) {


                Row(
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Row(modifier = Modifier.align(Alignment.CenterVertically)) {
                        Column(verticalArrangement = Arrangement.Center) {
                            Text(
                                text = "IF",
                                fontSize = 32f.sp,
                                color = Color(0, 128, 0),
                                modifier = Modifier.padding(4f.dp)
                            )
                        }
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth().horizontalScroll(ScrollState(0)),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        val enabled = remember { mutableStateOf(true) }
                        Column(verticalArrangement = Arrangement.Center) {

                            part(newCard, newCard?.condition?.value?.leftPart, enabled)
                        }
                        Column(verticalArrangement = Arrangement.Center) {
                            popupButton(
                                card = newCard,
                                items = signs,
                                text = fun() = newCard?.condition?.value?.sign?.value ?: Message.SIGN,
                                popupClickable = fun(s: String) { newCard?.condition?.value?.sign?.value = s },
                                fontSize = 32f
                            )
                        }
                        Column(verticalArrangement = Arrangement.Center) {
                            part(newCard, newCard?.condition?.value?.rightPart, enabled)
                        }
                    }
                }
                Divider()
                // condition true effects
                conditionResult(newCard, newCard?.condition?.value?.conditionTrue)
                Divider()
                // ELSE
                elseLabel()
                Divider()
                // condition false effects
                conditionResult(newCard, newCard?.condition?.value?.conditionFalse)
                Divider()
                button(text = Message.CLEAR, onClick = {clear = true})
            }

        }
    }
}

//@Composable
//fun conditionType(newCard: NewCard?) {
//
//    val text = remember { mutableStateOf(Message.EMPTY) }
//
//    Column(verticalArrangement = Arrangement.SpaceEvenly) {
//        Row(horizontalArrangement = Arrangement.Center) {
//
//            popupButton(
//                newCard,
//                effectVariantList,
//                fun() =  text.value,
//                fun (t:String) {text.value = t}
//                )
//        }
//    }
//}


@Composable
fun elseLabel() {
    Text(
        text = "ELSE",
        fontSize = 32f.sp,
        color = Color(0, 128, 0),
        modifier = Modifier.padding(4f.dp)
    )
}

@Composable
fun conditionResult(newCard: NewCard?, effect: MutableState<List<NewCard.Effect?>?>?) {
    // TODO эфекты в результатах условий должны иметь 3 вариации:
    //  обычные     general     player gems +3
    //  присвоение  grab        player quarry = enemy quarry
    //  обмен       switch      switch players magic / exchange walls of players

    if (newCard == null) return
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
//        conditionType(newCard)
        Row(
            modifier = Modifier.fillMaxWidth().horizontalScroll(ScrollState(0)),
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {

            effect?.value?.forEachIndexed { index, eff ->

                Row {
                    effect(newCard, eff)
                    plusMinusButton(newCard, effect, index)
                }
            }
            if (effect?.value.isNullOrEmpty()) {
                plusMinusButton(newCard, effect)
            }
        }
    }
}

@Composable
private fun plusMinusButton(
    card: NewCard?,
    effect: MutableState<List<NewCard.Effect?>?>?,
    index: Int = -1
) {

    if (card == null) return


    Column(verticalArrangement = Arrangement.Center) {

        if (
            effect?.value.isNullOrEmpty() ||
            index == effect?.value?.lastIndex ||
            index < 0
        ) {
            Text(
                text = "+",
                modifier = Modifier
                    .padding(4f.dp)
                    .defaultMinSize(20f.dp, 20f.dp)
                    .background(Color.LightGray)
                    .border(1f.dp, Color.DarkGray)
                    .clickable {
                        effect?.value =
                            listOf(
                                *effect?.value?.plus(NewCard.Effect())
                                    ?.toTypedArray()
                                    ?: listOf(NewCard.Effect()).toTypedArray()
                            )

                    },
                fontSize = 18.sp,
                textAlign = TextAlign.Center,
            )
        }
        //  -  во всех случаях кроме случая когда эфектов нет
        if (!effect?.value.isNullOrEmpty()) {
            Text(
                text = "-",
                modifier = Modifier
                    .padding(4f.dp)
                    .defaultMinSize(20f.dp, 20f.dp)
                    .background(Color.LightGray)
                    .border(1f.dp, Color.DarkGray)
                    .clickable {

                    },
                fontSize = 18.sp,
                textAlign = TextAlign.Center,
            )
        }
    }
}


@Composable
private fun part(
    card: NewCard?,
    side: MutableState<List<NewCard.Effect?>?>?,
    enabled: MutableState<Boolean> = mutableStateOf(true)
) {

    if (card?.condition?.value?.leftPart?.value.isNullOrEmpty()) {
        card?.condition?.value?.leftPart?.value = listOf(NewCard.Effect())
    }
    if (card?.condition?.value?.rightPart?.value.isNullOrEmpty()) {
        card?.condition?.value?.rightPart?.value = listOf(NewCard.Effect())
    }

    Row(horizontalArrangement = Arrangement.SpaceAround) {
        popupButton(
            card,
            players,
            fun() = side?.value?.get(0)?.player?.value ?: Message.EMPTY,
            fun(s: String) { side?.value?.get(0)?.player?.value = s },
            enabled = if (card?.condition?.value?.leftPart == side) mutableStateOf(true) else enabled
        )
        Text(
            modifier = Modifier.align(Alignment.CenterVertically),
            text = Message.PLAYER,
            fontSize = 12.sp,
            textAlign = TextAlign.Right
        )
    }
    Row(horizontalArrangement = Arrangement.SpaceAround) {
        popupButton(
            card,
            structures,
            fun() = side?.value?.get(0)?.structure?.value ?: Message.EMPTY,
            fun(s: String) { side?.value?.get(0)?.structure?.value = s },
            enabled = if (card?.condition?.value?.leftPart == side) mutableStateOf(true) else enabled
        )
        Text(
            modifier = Modifier.align(Alignment.CenterVertically),
            text = Message.STRUCTURE,
            fontSize = 12.sp,
            textAlign = TextAlign.Right
        )
    }
    if (side != card?.condition?.value?.leftPart) {
        Row(horizontalArrangement = Arrangement.SpaceAround) {
            popupButton(
                card = card,
                items = numbers,
                text = fun(): String {
                    val message = side?.value?.get(0)?.value?.value ?: Message.NAN
                    enabled.value = message.contains(numbers[0])
                    return message
                },
                popupClickable = fun(s: String) { side?.value?.get(0)?.value?.value = s }
            )
            Text(
                modifier = Modifier.align(Alignment.CenterVertically),
                text = Message.VALUE,
                fontSize = 12.sp,
                textAlign = TextAlign.Right
            )
        }
    }
}

