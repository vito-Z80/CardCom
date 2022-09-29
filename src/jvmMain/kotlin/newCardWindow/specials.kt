package newCardWindow

import Message
import Specials
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Checkbox
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import button
import gson.NewCard
import name


@Composable
fun specials(newCard: NewCard?) {
    var contentVisible by remember { mutableStateOf(false) }
    var clear by remember { mutableStateOf(false) }

    LaunchedEffect(clear) {
        if (clear) {
            newCard?.specials?.value = null
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
            if (contentVisible && newCard?.specials?.value == null) {
                newCard?.specials?.value = NewCard.Special()
            }
        }) {
            Text(text = "Specials")
        }
        AnimatedVisibility(visible = contentVisible, modifier = Modifier) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Divider()
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(ScrollState(0)),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    specialsCheckBox(newCard, Specials.PLAY_AGAIN)
                    specialsCheckBox(newCard, Specials.DISCARD)
                    specialsCheckBox(newCard, Specials.DRAW)
                    specialsCheckBox(newCard, Specials.`CAN'T_DISCARD`)
                }
                Divider()
                button(text = Message.CLEAR, onClick = { clear = true })
            }
        }
    }


}

@Composable
private fun specialsCheckBox(newCard: NewCard?, paramName: Specials) {

    var selected by remember { mutableStateOf(when (paramName) {
        Specials.PLAY_AGAIN -> newCard?.specials?.value?.playAgain?.value ?: false
        Specials.DRAW -> newCard?.specials?.value?.draw?.value ?: false
        Specials.`CAN'T_DISCARD` -> newCard?.specials?.value?.notDiscard?.value ?: false
        Specials.DISCARD -> newCard?.specials?.value?.discard?.value ?: false
    }) }

    LaunchedEffect(selected){
        when (paramName) {
            Specials.PLAY_AGAIN -> newCard?.specials?.value?.playAgain?.value = selected
            Specials.DRAW -> newCard?.specials?.value?.draw?.value = selected
            Specials.`CAN'T_DISCARD` -> newCard?.specials?.value?.notDiscard?.value = selected
            Specials.DISCARD -> newCard?.specials?.value?.discard?.value = selected
        }
    }


    Row {
        Text(text = paramName.name(), modifier = Modifier.padding(8f.dp).padding(start = 8.dp, end = 2.dp))
        Checkbox(
            checked = selected, onCheckedChange = {
                selected = it
            }, modifier = Modifier
                .padding(8f.dp)
                .size(16f.dp)
                .padding(end = 4.dp)
        )
    }

}
