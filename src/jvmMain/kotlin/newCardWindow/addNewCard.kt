@file:OptIn(ExperimentalUnsignedTypes::class)

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.AbsoluteRoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.type
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogState
import androidx.compose.ui.window.WindowPosition
import file.getImageByPath
import file.getImagePath
import gson.NewCard
import newCardWindow.*

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun newCardDialog(card: NewCard? = NewCard()) {


    var newCard: NewCard? by mutableStateOf(card ?: NewCard())

    var tryLoadImage by mutableStateOf(false)


    Dialog(onCloseRequest = {
        newCard = null
        AppData.tmpCard = null
        AppData.showCardDialog = false
    },
           title = "Create new card",
           undecorated = true,
           transparent = true,
           state = DialogState(position = WindowPosition(Alignment.Center), width = 768f.dp, height = 512f.dp),
           onKeyEvent = {
               if (it.key == Key.Escape && it.type == KeyEventType.KeyDown) {
                   newCard = null
                   AppData.tmpCard = null
                   AppData.showCardDialog = false
                   true
               } else {
                   false
               }
           }

          ) {


        Surface(
            shape = AbsoluteRoundedCornerShape(12.dp),
            color = Color.Yellow,
            modifier = Modifier.border(width = 1f.dp, color = Color.Black, shape = AbsoluteRoundedCornerShape(12.dp)),
               ) {
            Column(modifier = Modifier.fillMaxWidth().fillMaxHeight().padding(4f.dp)) {

                LazyColumn(modifier = Modifier.weight(1f).padding(4f.dp)) {
                    if (newCard != null) {

                        if (tryLoadImage) {
                            newCard?.imagePath?.value = getImagePath()
                            if (newCard?.imagePath?.value != null) {
                                cardImages[newCard?.imagePath?.value] = getImageByPath()
                                addLog("The picture [${newCard?.imagePath?.value}] has been loaded.")
                            }
                            tryLoadImage = false
//                            val spr = PlatformSprite.asSpriteSymbols(newCard?.imagePath?.value)
//                            newCard?.zxCard?.value?.spriteBlock8?.value = spr?.first
//                            newCard?.zxCard?.value?.attributes?.value = spr?.second
//
//                            val r = PlatformSprite.spriteToAsm(moduleName = "CARD",spriteSymbols = spr?.first, attributes = spr?.second)
//                            println(r)
                        }

                        item {
                            Row(
                                modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center
                               ) {
                                Button(onClick = {
                                    tryLoadImage = true
                                }) {
                                    Text(text = "Load Image")
                                }
                            }
                        }


                        if (newCard?.imagePath != null) {
                            item {
                                cardImage(newCard)
                            }
                        }


                        items(1) {

                            inputCardName(newCard!!)
                            inputCardDescription(newCard!!)
                            inputCardCost(newCard!!)
                            inputProbability(newCard!!)
                            specials(newCard)

                            generalEffect(newCard, newCard?.effects, "Effects")
                            condition(newCard)
                            platformDescriptionText(newCard)
                        }

                    }
                }
                Column(modifier = Modifier.fillMaxWidth().padding(bottom = 0f.dp)) {
                    Divider(thickness = 4f.dp)
                    confirmNewCardWindow(newCard) { newCard = null }
                }
            }

        }
    }
}

//----------------------------------------------------------------------------------------------------------------------
@Composable
private fun inputCardName(newCard: NewCard) {
    Row(
        modifier = Modifier.fillMaxWidth().horizontalScroll(ScrollState(0)),
        horizontalArrangement = Arrangement.SpaceAround
       ) {
        OutlinedTextField(value = newCard.cardName.value ?: Message.EMPTY,
                          onValueChange = { newCard.cardName.value = it },
                          singleLine = true,
                          label = { Text("Card Name") })
    }
}

@Composable
private fun inputCardDescription(newCard: NewCard) {
    Row(
        modifier = Modifier.fillMaxWidth().horizontalScroll(ScrollState(0)),
        horizontalArrangement = Arrangement.SpaceAround
       ) {
        OutlinedTextField(value = newCard.cardDescription.value ?: Message.EMPTY,
                          onValueChange = { newCard.cardDescription.value = it },
                          singleLine = true,
                          label = { Text("Card Description") })
    }
}

@Composable
private fun inputCardCost(newCard: NewCard) {
    Row(
        modifier = Modifier.fillMaxWidth().horizontalScroll(ScrollState(0)),
        horizontalArrangement = Arrangement.SpaceAround
       ) {
        Row {
            Text("Cost: ", modifier = Modifier.align(Alignment.CenterVertically))
            basicInput(newCard.cardCost, regOnlyDigit)
        }
        Column(modifier = Modifier.align(Alignment.CenterVertically), verticalArrangement = Arrangement.Center) {
            popupButton(
                card = newCard,
                items = Structure.values().map { it.name() },
                text = fun() = newCard.costCurrency.value ?: "Currency",
                popupClickable = fun(s: String) { newCard.costCurrency.value = s },
                       )
        }
    }
}

//----------------------------------------------------------------------------------------------------------------------
@Composable
private fun inputProbability(newCard: NewCard) {
    var value by remember { mutableStateOf(1f) }

    Row(
        modifier = Modifier.fillMaxWidth().horizontalScroll(ScrollState(0)),
        horizontalArrangement = Arrangement.SpaceAround
       ) {
        Row {
            Text("Probability: ", modifier = Modifier.align(Alignment.CenterVertically))
        }
        Column(modifier = Modifier.align(Alignment.CenterVertically), verticalArrangement = Arrangement.Center) {
            popupButton(
                card = newCard,
                items = List(3) { (it + 1).toString() },
                text = fun() = newCard.probability.value ?: "1",
                popupClickable = fun(s: String) { newCard.probability.value = s },
                       )
        }
    }


}
//----------------------------------------------------------------------------------------------------------------------
