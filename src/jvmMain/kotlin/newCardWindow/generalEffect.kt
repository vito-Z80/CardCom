package newCardWindow


import Index
import Message
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import button
import effect
import gson.NewCard

/**
 * основной эффект по описанию карты
 */

@Composable
fun generalEffect(newCard: NewCard?, effects: MutableState<List<NewCard.Effect?>?>?, buttonText: String) {

    var contentVisible by remember { mutableStateOf(false) }
    var clear by remember { mutableStateOf(false) }
    val remove = remember { mutableStateOf(Index(-1)) }


    LaunchedEffect(remove.value) {
        if (remove.value.id < 0) return@LaunchedEffect

        val e = effects?.value?.get(remove.value.id)
        effects?.value = effects?.value?.minus(e)

        if (effects?.value.isNullOrEmpty()) {
            clear = true
        }

    }

    LaunchedEffect(clear) {
        if (clear) {
            effects?.value = null
            clear = false
            contentVisible = false
        }
    }

    Column(
        modifier = Modifier
            .border(width = 1f.dp, color = Color.DarkGray)
            .background(color = Color(230, 230, 230))
            .fillMaxWidth()
            .padding(4f.dp)
    ) {
        Button(onClick = {
            contentVisible = !contentVisible
            if (contentVisible && effects?.value == null) {
                effects?.value = listOf(NewCard.Effect())
            }
        }) {
            Text(text = buttonText)
        }

        AnimatedVisibility(visible = contentVisible, modifier = Modifier) {
            Column(modifier = Modifier.fillMaxHeight(), verticalArrangement = Arrangement.Center) {
                Divider()
                Row(
                    modifier = Modifier.fillMaxWidth().horizontalScroll(ScrollState(0)),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                ) {
//
                    effects?.value?.forEachIndexed { index, eff ->
                        Row {
                            effect(newCard, eff)
                            plusMinusButton(effects, remove, index)
                        }
                    }
                }
                Divider()
                button(text = Message.CLEAR, onClick = { clear = true })
            }
        }
    }
}

@Composable
private fun plusMinusButton(
    effects: MutableState<List<NewCard.Effect?>?>?,
    remove: MutableState<Index>,
    index: Int
) {

    if (effects == null) return

    Column(verticalArrangement = Arrangement.Center) {
        Text(
            text = "+",
            modifier = Modifier
                .padding(4f.dp)
                .defaultMinSize(20f.dp, 20f.dp)
                .background(Color.LightGray)
                .border(1f.dp, Color.DarkGray)
                .clickable {
                    effects.value =
                        listOf(
                            *effects.value?.plus(NewCard.Effect())?.toTypedArray()
                                ?: listOf(NewCard.Effect()).toTypedArray()
                        )
                },
            fontSize = 18.sp,
            textAlign = TextAlign.Center,
        )
        Text(
            text = "-",
            modifier = Modifier
                .padding(4f.dp)
                .defaultMinSize(20f.dp, 20f.dp)
                .background(Color.LightGray)
                .border(1f.dp, Color.DarkGray)
                .clickable {
                    remove.value = Index(index)
                },
            fontSize = 18.sp,
            textAlign = TextAlign.Center,
        )
    }
}



