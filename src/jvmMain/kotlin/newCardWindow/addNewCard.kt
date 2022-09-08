import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.AbsoluteRoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
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
import newCardWindow.confirmNewCardWindow
import newCardWindow.generalEffect
import newCardWindow.specials

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun newCardDialog(card: NewCard? = NewCard()) {

    // TODO десериализация не работает когда есть пустой итем карты
    //  итемы колумна проверить сначла добавляя по одному, искать в каком моменте gson выебывается
    //  скорее всего в null`абл местах, или когда списки полнстью пустые но есть в файле gson


    //  https://stackoverflow.com/questions/68852110/show-custom-alert-dialog-in-jetpack-compose

    var newCard: NewCard? by mutableStateOf(card ?: NewCard())

    var tryLoadImage by mutableStateOf(false)


    Dialog(
        onCloseRequest = {
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

//            inputDigit()
            Column(modifier = Modifier.fillMaxWidth().fillMaxHeight().padding(4f.dp)) {

                LazyColumn(modifier = Modifier.weight(1f).padding(4f.dp)) {
                    if (newCard != null) {


                        if (tryLoadImage) {
                            newCard?.imagePath?.value = getImagePath()
                            cardImages[newCard?.imagePath?.value] = getImageByPath()
                            tryLoadImage = false
                        }

                        item {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center
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
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    println("IMAGE ${newCard?.imagePath?.value}")
                                    cardImages[newCard?.imagePath?.value]?.let {
                                        Image(
                                            bitmap = it,
                                            contentDescription = "${newCard?.cardName}",
                                            filterQuality = FilterQuality.Low,
                                            //                                    contentScale = ContentScale.Crop,
                                            modifier = Modifier.size(100.dp),
                                        )
                                    }
                                    // TODO добавить параметр отвечающий за то что скрин уже загружен, что бы не редравить
                                }
                            }
                        }


                        items(1) {

                            inputCardName(newCard!!)
                            inputCardDescription(newCard!!)
                            inputCardCost(newCard!!)
                            specials(newCard)

                            generalEffect(newCard)
                            condition(newCard)
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
//----------------------------------------------------------------------------------------------------------------------
//----------------------------------------------------------------------------------------------------------------------
//----------------------------------------------------------------------------------------------------------------------


//----------------------------------------------------------------------------------------------------------------------


//@Composable
//private fun inputEffect(newCard: NewCard, effect: NewCard.Effect?) {
//
//    var visible by rememberSaveable { mutableStateOf(true) }
//    var showHideMessage by rememberSaveable { mutableStateOf("") }
//
//    Column(Modifier.fillMaxWidth().border(width = 1.dp, color = Color.DarkGray).background(Color.LightGray)) {
//        Row(modifier = Modifier.padding(4.dp)) {
//            showHideMessage = if (visible) HIDE_MESSAGE else SHOW_MESSAGE
//            Text(text = showHideMessage, modifier = Modifier
//                .weight(1f)
//                .clickable { visible = !visible }
//                .border(1.dp, Color.Black)
//                .background(Color.LightGray)
//                .padding(4.dp),
//                textAlign = TextAlign.Center
//            )
//            Text(text = "Clear", Modifier
//                .padding(end = 0.dp)
//                .clickable {
//                    effect?.clear()
//                }
//                .border(1.dp, Color.Black)
//                .background(Color.LightGray)
//                .padding(4.dp),
//                textAlign = TextAlign.Center
//            )
//
//
//            Text(text = "Remove", Modifier
//                .padding(end = 0.dp)
//                .clickable {
//                    if (effect != null) {
////                        newCard.effects = newCard.effects?.value?.minus(effect)
//                        visible = false
//                    }
//                }
//                .border(1.dp, Color.Black)
//                .background(Color.LightGray)
//                .padding(4.dp),
//                textAlign = TextAlign.Center
//            )
//
//        }
//        AnimatedVisibility(
//            visible = visible, modifier = Modifier.padding(4.dp)
//        ) {
//
//            Row(modifier = Modifier.fillMaxSize(), horizontalArrangement = Arrangement.SpaceEvenly) {
//                OutlinedTextField(
//                    value = effect?.value?.value ?: "0",
//                    onValueChange = {
//                        effect?.value?.value = it
//                    },
//                    singleLine = true,
//                    label = { Text("Value") },
//                    modifier = Modifier.width(100.dp),
//                    shape = RectangleShape
//                )
////                popupList(effect, effect?.player?.value, players)
////                popupList(effect, effect?.structure?.value, structures)
//            }
//        }
//    }
//
//
//    Divider()
//
//}

//----------------------------------------------------------------------------------------------------------------------

//@Composable
//fun popupList(effect: NewCard.Effect?, field: String?, list: List<String>) {
//
//    var expand by remember { mutableStateOf(false) }
//
//    Button(
//        onClick = {
//            expand = true
//        }, modifier = Modifier.width(100.dp)
//    ) {
//        Text(text = "${field?.value}", softWrap = false)
//    }
//
//    DropdownMenu(
//        expanded = expand,
//        onDismissRequest = {
//            expand = false
//
//        },
////        focusable = true
//    ) {
//
//        list.forEach {
//            DropdownMenuItem(
//                onClick = {
//                    field?.value = it
//                }
//            ) {
//                Text(text = it)
//            }
//        }
//    }
//}

//----------------------------------------------------------------------------------------------------------------------
@Composable
private fun inputCardName(newCard: NewCard) {

    OutlinedTextField(
        value = newCard.cardName.value ?: "",
        onValueChange = { newCard.cardName.value = it },
        singleLine = true,
        label = { Text("Card Name") }
    )
}

@Composable
private fun inputCardDescription(newCard: NewCard) {
    OutlinedTextField(
        value = newCard.cardDescription.value ?: "",
        onValueChange = { newCard.cardDescription.value = it },
        singleLine = true,
        label = { Text("Card Description") })
}

@Composable
private fun inputCardCost(newCard: NewCard) {
    OutlinedTextField(
        value = newCard.cardCost.value ?: "",
        onValueChange = { newCard.cardCost.value = it },
        singleLine = true,
        label = { Text("Card Cost") })
}

//----------------------------------------------------------------------------------------------------------------------
