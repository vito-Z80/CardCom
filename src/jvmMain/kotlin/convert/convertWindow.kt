package convert

import AppData
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import java.io.File


@Composable
fun convertWindow(isVisible: MutableState<Boolean>) {

    if (isVisible.value) {
        Window(onCloseRequest = { isVisible.value = false }) {
            Column(modifier = Modifier.padding(8f.dp)) {

                chooseSpriteFileSave()
                Divider(color = Color.Black)
                chooseSpriteRaster()
                Divider(color = Color.Black)
                confirm(isVisible)
            }
        }
    }
}


@Composable
private fun chooseSpriteRaster() {

    val radioButtonsOnRow = 4

    val selectFile = remember { mutableStateOf("") }

    val chunks = runCatching {
        val f = File(File("ZxSpriteToken").absolutePath).listFiles().filter { it.extension == "zxst" }
        f.chunked(radioButtonsOnRow)
    }.getOrNull()


    // header
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
        Text(text = "Sprite raster:")
    }

    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
        Divider(modifier = Modifier.fillMaxWidth(0.6f))
    }

    // radio buttons
    chunks?.forEach { row ->
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            row.forEach { rb ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(selected = rb.name == selectFile.value, onClick = {
                        selectFile.value = rb.name
                    })
                    Text(text = rb.name, modifier = Modifier.width(128f.dp))
                }
            }
        }
    }
}

@Composable
private fun chooseSpriteFileSave() {

    var asm by remember { mutableStateOf(false) }
    var bin by remember { mutableStateOf(true) }

    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            Text(text = "Save sprites as:")
        }

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            Divider(modifier = Modifier.fillMaxWidth(0.6f))
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(checked = asm, onCheckedChange = { asm = it })
            Text(text = "ASM")
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(checked = bin, onCheckedChange = { bin = it })
            Text(text = "BIN")
        }
    }
}

@Composable
private fun confirm(isVisible: MutableState<Boolean>) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
        TextButton(onClick = { isVisible.value = false }) {
            Text(text = "Cancel")
        }
        TextButton(onClick = {
            // TODO add convert config
            AppData.convert = true
            isVisible.value = false
        }) {
            Text(text = "Convert")
        }
    }
}