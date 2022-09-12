package newCardWindow

import Message
import SpecialsName
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
                    specialsCheckBox(newCard, SpecialsName.PLAY_AGAIN)
                    specialsCheckBox(newCard, SpecialsName.DISCARD)
                    specialsCheckBox(newCard, SpecialsName.DRAW)
                    specialsCheckBox(newCard, SpecialsName.`CAN'T_DISCARD`)
                }
                Divider()
                button(text = Message.CLEAR, onClick = { clear = true })
            }
        }
    }


}

@Composable
private fun specialsCheckBox(newCard: NewCard?, paramName: SpecialsName) {

    var selected by remember { mutableStateOf(false) }

    selected = when (paramName) {
        SpecialsName.PLAY_AGAIN -> newCard?.specials?.value?.playAgain?.value ?: false
        SpecialsName.DRAW -> newCard?.specials?.value?.drop?.value ?: false
        SpecialsName.`CAN'T_DISCARD` -> newCard?.specials?.value?.notDiscard?.value ?: false
        SpecialsName.DISCARD -> newCard?.specials?.value?.discard?.value ?: false
    }


    Row {
        Text(text = paramName.name(), modifier = Modifier.padding(8f.dp).padding(start = 8.dp, end = 2.dp))
        Checkbox(
            checked = selected, onCheckedChange = {
                selected = it
                when (paramName) {
                    SpecialsName.PLAY_AGAIN -> newCard?.specials?.value?.playAgain?.value = it
                    SpecialsName.DRAW -> newCard?.specials?.value?.drop?.value = it
                    SpecialsName.`CAN'T_DISCARD` -> newCard?.specials?.value?.notDiscard?.value = it
                    SpecialsName.DISCARD -> newCard?.specials?.value?.discard?.value = it
                }
            }, modifier = Modifier
                .padding(8f.dp)
                .size(16f.dp)
                .padding(end = 4.dp)
        )
    }

}
