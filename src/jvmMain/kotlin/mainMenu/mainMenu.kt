package mainMenu

import AppData
import addLog
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import convert.PlatformLogic
import convert.PlatformSprite
import convert.PlatformText
import file.loadTemplate
import file.saveData
import file.saveTemplate
import io.ktor.client.*
import io.ktor.client.plugins.websocket.*
import io.ktor.network.selector.*
import io.ktor.network.sockets.*
import io.ktor.utils.io.*
import io.ktor.websocket.*
import kotlinx.coroutines.delay
import java.net.Socket
import kotlin.concurrent.thread
import kotlin.system.exitProcess

@Composable
fun mainMenu() {
    val expandFileDialog = remember { mutableStateOf(false) }
    val expandCardDialog = remember { mutableStateOf(false) }
    val connectDialog = remember { mutableStateOf(false) }



    LaunchedEffect(ZxData.tryConnect) {
        if (ZxData.tryConnect && ZxData.socket == null) {
            ZxData.tryConnect = false
            ZxData.showConnectDialog = true
            thread {

                try {
                    ZxData.socket = Socket("127.0.0.1", 30000)
                    ZxData.input = ZxData.socket?.getInputStream()
                    ZxData.output = ZxData.socket?.getOutputStream()
                    val o = ZxData.output?.write("cpu".toByteArray())
                    val r = ZxData.input?.bufferedReader()
                    addLog(("Greetings: " + r?.readLine()) ?: "No connect.", Color.Magenta)
                    r?.readLine()
//                    addLog(r?.readLine() ?: "", Color.Blue)
                } catch (_: Exception) {

                }

            }
        }
    }

    Row(
        modifier = Modifier.fillMaxWidth().padding(2f.dp).background(color = Color.LightGray)
            .border(width = 1.dp, color = Color.DarkGray)
       ) {
        Text(text = "File", modifier = Modifier.clickable {
            expandFileDialog.value = true
        }.padding(4.dp)).apply { popupFileMenu(expandFileDialog) }

        Text(text = "Card", modifier = Modifier.clickable {
            expandCardDialog.value = true
        }.padding(4.dp)).apply { popupCardMenu(expandCardDialog) }
        Text(text = "Connect", modifier = Modifier.clickable(enabled = !ZxData.showConnectDialog) {
            ZxData.tryConnect = true
        }.padding(4.dp))
    }
}

@Composable
fun popupCardMenu(expand: MutableState<Boolean>) {

    LaunchedEffect(AppData.convert) {
        if (AppData.convert) {
            AppData.convert = false
            val sprites = PlatformSprite.convert("CARD_SPRITE")
            val text = PlatformText.convert("CARD_TEXT")

            println(sprites)
            println("||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||")
            println(text)
            println("||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||")
            val logic = PlatformLogic.convert("CARD_LOGIC")
            println(logic)
            saveData(sprites = sprites, text = text, logic = logic)
        }
    }

    DropdownMenu(expanded = expand.value, onDismissRequest = { expand.value = false }) {
        DropdownMenuItem(onClick = {
            expand.value = false
            AppData.showCardDialog = true
        }) {
            Text(text = "Create")
        }

        DropdownMenuItem(onClick = {
            expand.value = false
            AppData.convert = true
        }) {
            Text(text = "Convert")
        }
    }
}

@Composable
fun popupFileMenu(expand: MutableState<Boolean>) {

//    var isSaveTemplate by mutableStateOf(false)
//
//    if (isSaveTemplate) {
//        isSaveTemplate = false
//        saveTemplate()
//    }

    DropdownMenu(expanded = expand.value, onDismissRequest = { expand.value = false }) {
        DropdownMenuItem(onClick = {
            expand.value = false
            loadTemplate()
        }) {
            Text(text = "Load")
        }

        DropdownMenuItem(onClick = {
            expand.value = false
            saveTemplate()
//            isSaveTemplate = true
        }) {
            Text(text = "Save")
        }

        DropdownMenuItem(onClick = {
            expand.value = false
        }) {
            Text(text = "Save data")
        }

        DropdownMenuItem(onClick = {
            expand.value = false
            exitProcess(0)
        }) {
            Text(text = "Exit")
        }
    }
}