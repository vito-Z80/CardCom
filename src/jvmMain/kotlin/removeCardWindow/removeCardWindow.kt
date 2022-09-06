import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog

@Composable
fun removeCardDialog() {
    Dialog(
        onCloseRequest = {
            AppData.tmpCard = null
            AppData.showRemoveDialog = false
        }, title = "Remove card"
    ) {

        Column(modifier = Modifier.fillMaxSize().padding(4.dp)) {

            Column(modifier = Modifier.weight(1f).fillMaxWidth()) {
                Text(
                    text = "Remove card\n${AppData.tmpCard?.cardName}",
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 32f.dp).align(Alignment.CenterHorizontally),
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 16.sp
                )
            }
            Column(modifier = Modifier.padding(bottom = 8.dp)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {

                    Button(onClick = {
                        AppData.showRemoveDialog = false
                        AppData.tmpCard = null
                        AppData.showRemoveDialog = false
                    }) {
                        Text(text = "Cancel")
                    }

                    Button(onClick = {
                        AppData.cards = AppData.cards?.minusElement(AppData.tmpCard)
                        AppData.tmpCard = null
                        AppData.showRemoveDialog = false
                    }) {
                        Text(text = "Ok")
                    }
                }
            }
        }
    }
}