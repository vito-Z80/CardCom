package mainMenu

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import file.loadTemplate
import file.saveTemplate
import kotlin.system.exitProcess

@Composable
fun mainMenu() {
    val expandFileDialog = remember { mutableStateOf(false) }
    val expandCardDialog = remember { mutableStateOf(false) }

    Row(
        modifier = Modifier.fillMaxWidth()
            .background(color = Color.LightGray)
            .border(width = 1.dp, color = Color.DarkGray)
    ) {
        Text(text = "File", modifier = Modifier
            .clickable {
                expandFileDialog.value = true
            }
            .padding(4.dp)
        ).apply { popupFileMenu(expandFileDialog) }

        Text(text = "Card", modifier = Modifier
            .clickable {
                expandCardDialog.value = true
            }
            .padding(4.dp)
        ).apply { popupCardMenu(expandCardDialog) }
    }
}

@Composable
fun popupCardMenu(expand: MutableState<Boolean>) {

    DropdownMenu(expanded = expand.value, onDismissRequest = { expand.value = false }) {
        DropdownMenuItem(onClick = {
            expand.value = false
            AppData.showCardDialog = true
        }) {
            Text(text = "Create")
        }

        DropdownMenuItem(onClick = {
            expand.value = false
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