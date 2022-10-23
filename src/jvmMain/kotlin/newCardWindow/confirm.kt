package newCardWindow

import AppData
import addLog
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import gson.NewCard


@Composable
fun confirmNewCardWindow(newCard: NewCard?, cancelClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Button(
            onClick = {
                cancelClick.invoke()
                AppData.tmpCard = null
                AppData.showCardDialog = false
            }

        ) {
            Text("Close")
        }
        Button(
            onClick = {
                // добавить новую карту или редактировать и заменить старую карту
                AppData.tmpCard = null
                AppData.showCardDialog = false
                if (AppData.cards == null) AppData.cards = listOf()
                val id = AppData.cards?.indexOf(newCard)
                if (id == null || id < 0 || AppData.cards!!.isEmpty()) {
                    AppData.cards = AppData.cards?.plus(newCard)
                    addLog("Crate new card: ${newCard?.cardName?.value?.uppercase()}")
                } else {
                    val list = AppData.cards?.toMutableList()
                    list?.set(id, newCard)
                    AppData.cards = list?.toList()
                    addLog("Card ${newCard?.cardName?.value?.uppercase()} be changed.")
                }
            }
        ) {
            Text("Add")
        }
    }

}