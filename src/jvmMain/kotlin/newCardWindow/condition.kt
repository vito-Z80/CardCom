import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import gson.NewCard
import newCardWindow.generalEffect

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
            contentVisible = !contentVisible
            if (contentVisible && newCard?.condition?.value == null) {
                newCard?.condition?.value = NewCard.Condition()
            }
        }) {
            Text(text = "Condition")
        }

        AnimatedVisibility(visible = contentVisible, modifier = Modifier) {
            // CONDITION
            Column(modifier = Modifier.fillMaxWidth().weight(1f)) {


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
                                items = Sign.values().map { it.name() },
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
                // condition true effects
                generalEffect(newCard, newCard?.condition?.value?.conditionTrue, "Ture")
                // ELSE
                elseLabel()
                // condition false effects
                generalEffect(newCard, newCard?.condition?.value?.conditionFalse, "False")
                Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.End) {
                    button(text = Message.CLEAR, onClick = { clear = true })
                }
            }
        }
    }
}

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
private fun part(
    card: NewCard?,
    partEffect: MutableState<List<NewCard.Effect?>?>?,
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
            Player.values().map { it.name() },
            fun() = partEffect?.value?.get(0)?.player?.value ?: Message.EMPTY,
            fun(s: String) { partEffect?.value?.get(0)?.player?.value = s },
            enabled = mutableStateOf(partEffect?.value?.get(0)?.value?.value.isNullOrEmpty())
        )
        Text(
            modifier = Modifier.align(Alignment.CenterVertically),
            text = Player.PLAYER.name(),
            fontSize = 12.sp,
            textAlign = TextAlign.Right
        )
    }
    Row(horizontalArrangement = Arrangement.SpaceAround) {
        popupButton(
            card,
            Structure.values().map { it.name() },
            fun() = partEffect?.value?.get(0)?.structure?.value ?: Message.EMPTY,
            fun(s: String) { partEffect?.value?.get(0)?.structure?.value = s },
            enabled = mutableStateOf(partEffect?.value?.get(0)?.value?.value.isNullOrEmpty())
        )
        Text(
            modifier = Modifier.align(Alignment.CenterVertically),
            text = Message.STRUCTURE,
            fontSize = 12.sp,
            textAlign = TextAlign.Right
        )
    }
    if (partEffect != card?.condition?.value?.leftPart) {
        inputDigit(partEffect?.value?.get(0)?.value)
    }
}

