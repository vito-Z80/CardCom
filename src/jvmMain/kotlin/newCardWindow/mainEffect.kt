package newCardWindow


import Message
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
import button
import effect
import effectVariantList
import gson.NewCard
import numbers
import players
import popupButton
import structures

/**
 * основной эффект по описанию карты
 */

@Composable
fun mainEffect(newCard: NewCard?) {

    var contentVisible by remember { mutableStateOf(false) }
    var clear by remember { mutableStateOf(false) }

    LaunchedEffect(clear) {
        if (clear) {
            newCard?.effects?.value = null
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
            if (newCard?.effects?.value == null) {
                newCard?.effects?.value = listOf()
            }
            contentVisible = !contentVisible
        }) {
            Text(text = "Effects")
        }

        AnimatedVisibility(visible = contentVisible, modifier = Modifier) {

            Column(modifier = Modifier.fillMaxWidth()) {


                Divider()

                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().horizontalScroll(ScrollState(0)),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                    ) {

                        newCard?.effects?.value?.forEachIndexed { index, eff ->

                            Row {
                                effect(newCard,eff)
//                                effectVariant(eff)
                                plusMinusButton(newCard, index)
                            }
                        }
                        if (newCard?.effects?.value.isNullOrEmpty()) {
                            plusMinusButton(newCard)
                        }
                    }
                }
                Divider()
                Text(text = "Clear", modifier = Modifier.clickable { clear = true })
            }

        }
    }
}

@Composable
fun effectVariant(effect: NewCard.Effect?) {
    Column(verticalArrangement = Arrangement.Center) {
        effectVariantList.forEach {
            button(
                text = fun () = it,
                onClick = fun (){effect?.variant?.value = it}
            )
        }
    }
}


@Composable
private fun plusMinusButton(
    card: NewCard?,
    index: Int = -1
) {

    if (card == null) return


    Column(verticalArrangement = Arrangement.Center) {

        if (
            card.effects.value.isNullOrEmpty() ||
            index == card.effects.value!!.lastIndex ||
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
                        card.effects.value =
                            listOf(
                                *card.effects.value?.plus(NewCard.Effect())?.toTypedArray()
                                    ?: listOf(NewCard.Effect()).toTypedArray()
                            )


                    },
                fontSize = 18.sp,
                textAlign = TextAlign.Center,
            )
        }
        //  -  во всех случаях кроме случая когда эфектов нет

        if (!card.effects.value.isNullOrEmpty()) {
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



