import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun appContent() {

    if (AppData.showRemoveDialog) {
        removeCardDialog()
    }

    LazyColumn(modifier = Modifier.fillMaxWidth()) {
        item {
            AppData.cards?.forEachIndexed { id, card ->
                val color = Color.Yellow
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 4.dp, end = 4.dp, bottom = 2.dp)
                    .clickable {
                        AppData.tmpCard = card
                        AppData.showCardDialog = true
                    }
                    .background(color = color)
                    .border(width = 1.dp, color = Color.LightGray)
                ) {
                    // card image
                    if (card?.imagePath != null) {
                        cardImages[card.imagePath.value]?.let {
                            Image(
                                bitmap = it,
                                contentDescription = null,
                                modifier = Modifier
                                    .padding(start = 8.dp)
                                    .padding(4.dp)
                                    .align(Alignment.CenterVertically)
                                    .size(32f.dp)
                            )
                        }
                    }
                    // card name + card description
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .align(Alignment.CenterVertically)

                    ) {
                        Text(
                            text = "${card?.cardName?.value}",
                            color = Color.DarkGray,
                            fontSize = 16.sp,
                            softWrap = false,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${card?.cardDescription?.value}",
                            softWrap = false,
                            color = Color(100, 100, 100),
                            fontSize = 12.sp
                        )
                    }
                    // remove button
                    Column(modifier = Modifier.padding(end = 4.dp), verticalArrangement = Arrangement.Center) {
                        Button(onClick = {
                            AppData.tmpCard = card
                            AppData.showRemoveDialog = true
                        }){
                            Text(text = "Remove")
                        }
                    }

                }

            }
        }
    }
}