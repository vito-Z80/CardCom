package mainMenu

import AppData
import Structure
import ZxData
import addLog
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.WindowState
import cardImages
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import name
import theme.MyTheme
import kotlin.concurrent.thread


val request = "dumpraw 73B7 44\r\n".toByteArray()


val cor = CoroutineScope(Dispatchers.IO).launch {

    while (true) {
        delay(16)
        thread {
            if (ZxData.socket != null && ZxData.socket?.isClosed == false) {
                try {
                    val o = ZxData.output?.write(request)
                    val r = ZxData.input?.bufferedReader()
                    ZxData.xpeccyData = r?.readLine() ?: ""

                } catch (_: Exception) {
                    println("xpeccy not connect.")
                }
            }
        }
    }
}


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun xpeccyConnect() {
    MyTheme {
        if (ZxData.showConnectDialog) {
            xpeccyWindow()
        }
    }
}


@Composable
private fun xpeccyWindow() {
    val byte = StringBuilder()

    Window(
        state = WindowState(
            position = WindowPosition(Alignment.Center), width = 512f.dp, height = 512f.dp
                           ),
        title = "Xpeccy connect",
        onCloseRequest = {
            ZxData.input?.close()
            ZxData.output?.close()
            ZxData.socket?.close()
            ZxData.socket = null
            ZxData.input = null
            ZxData.output = null
            ZxData.showConnectDialog = false
            addLog("Disconnect xpeccy.", Color.Yellow)
        },
        resizable = false,
          ) {

        if (ZxData.xpeccyData.isNotEmpty()) {

            val player = ZxData.xpeccyData.substring(64, 76)
            val opponent = ZxData.xpeccyData.substring(76, 88)
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "Player cards", textAlign = TextAlign.Center, modifier = Modifier.padding(bottom = 4f.dp))
                Row(
                    modifier = Modifier.fillMaxWidth().padding(4f.dp).background(Color.DarkGray),
                    horizontalArrangement = Arrangement.Center
                   ) {
                    cards(byte, player)
                }
                Text(text = "Opponent cards", textAlign = TextAlign.Center, modifier = Modifier.padding(bottom = 4f.dp))
                Row(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 0f.dp).background(Color.DarkGray),
                    horizontalArrangement = Arrangement.Center
                   ) {
                    cards(byte, opponent)
                }
                playersData()
            }
        }
    }
}

@Composable
private fun cards(byte: StringBuilder, player: String) {


    for (i in player.indices step 2) {
        byte.clear()
        byte.append(player[i])
        byte.append(player[i + 1])
        val id = byte.toString().toInt(16)
        Column(
            modifier = Modifier.padding(2f.dp).requiredWidth(70f.dp), horizontalAlignment = Alignment.CenterHorizontally
              ) {


            val cardImage = cardImages[AppData.cards?.get(id)?.imagePath?.value]
            if (cardImage != null) {
                Column(modifier = Modifier.height(32f.dp)) {
                    Text(
                        text = "${AppData.cards?.get(id)?.cardName?.value}",
                        style = MaterialTheme.typography.h4,
                        textAlign = TextAlign.Center,
                        )
                }
                Image(
                    bitmap = cardImage,
                    contentDescription = null,
                    filterQuality = FilterQuality.None,
                    modifier = Modifier.align(Alignment.CenterHorizontally)

                     )
            }

        }

    }
}

@Composable
private fun playersData() {
    val str = StringBuilder()

    @Composable
    fun labels(strData: String) {
        Column {
            for (id in strData.indices step 4) {
                str.clear()
                str.append(strData[id + 2])
                str.append(strData[id + 3])
                str.append(strData[id])
                str.append(strData[id + 1])
                Row() {
                    Text(
                        text = Structure.values()[id / 4].name(),
                        Modifier.requiredWidth(80f.dp),
                        style = MaterialTheme.typography.h4
                        )
                    Text(
                        text = "${str.toString().toInt(16)}",
                        Modifier.requiredWidth(32f.dp),
                        style = MaterialTheme.typography.h4
                        )
                }
            }
        }
    }

//    Column(
//        horizontalAlignment = Alignment.CenterHorizontally,
//        modifier = Modifier.fillMaxWidth().background(Color(1f, 0.1f, 0.4f, 1f)).padding(4f.dp)
//          ) {

    if (ZxData.xpeccyData.isNotEmpty()) {
        val playerDataString = ZxData.xpeccyData.substring(0, 32)
        val opponentDataString = ZxData.xpeccyData.substring(32, 64)
        Row(
            horizontalArrangement = Arrangement.SpaceAround,
            modifier = Modifier.fillMaxWidth().background(Color(1f, 0.1f, 0.4f, 1f)).padding(4f.dp)
           ) {
            labels(playerDataString)
            labels(opponentDataString)
        }
    }
//    }
}