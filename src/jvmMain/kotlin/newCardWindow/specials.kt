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

/*

Lodestone
Cost: 5 Gems
Effect: +8 Tower. This card can't be discarded without playing it.

Elven Scout
Cost: 2 Recruits
Effect: Draw 1 card. Discard 1 card. Play again.

Lodestone
Cost: 5 Gems
Effect: +8 Tower. This card can't be discarded without playing it.

 */
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
                    // FIXME осуществить выбор следующим образом: CANT_DISCARD и PDP или PLAY_AGAIN
                    specialsCheckBox(newCard, Specials.PDP)
                    specialsCheckBox(newCard, Specials.PLAY_AGAIN)
                    specialsCheckBox(newCard, Specials.CANT_DISCARD)
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
        Specials.PDP -> newCard?.specials?.value?.pdp?.value ?: false
        Specials.CANT_DISCARD -> newCard?.specials?.value?.cantDiscard?.value ?: false
    }) }

    LaunchedEffect(selected){
        when (paramName) {
            Specials.PLAY_AGAIN -> newCard?.specials?.value?.playAgain?.value = selected
            Specials.PDP -> newCard?.specials?.value?.pdp?.value = selected
            Specials.CANT_DISCARD -> newCard?.specials?.value?.cantDiscard?.value = selected
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
