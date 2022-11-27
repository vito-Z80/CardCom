import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
    Column(modifier = Modifier.fillMaxWidth().fillMaxHeight()) {

        LazyColumn(modifier = Modifier.weight(1f)) {
            item {
                con()
            }
        }
        Column(modifier = Modifier.padding(2f.dp)) {
            logWindow()
        }

    }


}

//    Scaffold(bottomBar = { logWindow() }) {
//        Box(
//            modifier = Modifier
//                .fillMaxSize()
//           ){
//            con()
//        }
//    }

@Composable
fun logWindow() {

    val listState = rememberLazyListState()

    LaunchedEffect(AppData.logText.size) {
//        listState.animateScrollToItem(AppData.logText.size)
        listState.scrollToItem(AppData.logText.size)
    }

    LazyColumn(
        state = listState,
        modifier = Modifier.fillMaxWidth().height(96f.dp).padding(bottom = 0f.dp).background(Color.LightGray)
            .border(width = 1f.dp, color = Color.Blue).padding(4f.dp)
              ) {
        items(count = AppData.logText.size, key = null) {
            Text(
                text = AppData.logText[it].first,
                color = AppData.logText[it].second,
                style = MaterialTheme.typography.h3
                )
        }

    }
}

@Composable
private fun con() {
    AppData.cards?.forEachIndexed { id, card ->
        val ccName = Structure.values().firstOrNull { it.name.lowercase() == card?.costCurrency?.value?.lowercase() }
        val bgColor = if (ccName != null) {
            Color.White.byResource(ccName)
        } else {
            Color.Red
        }
        Row(modifier = Modifier.fillMaxWidth().padding(2f.dp).clickable {
            AppData.tmpCard = card
            AppData.showCardDialog = true
        }.background(color = bgColor).border(width = 1.dp, color = Color.LightGray)) {
            // index
            Text(
                text = id.toString(), modifier = Modifier.padding(start = 4f.dp).width(24f.dp).align(
                    Alignment.CenterVertically
                                                                                                    )
                )
            // card image
            if (card?.imagePath != null) {
                cardImages[card.imagePath.value]?.let {
                    Image(
                        bitmap = it,
                        contentDescription = null,
                        modifier = Modifier.padding(start = 8.dp).padding(4.dp).align(Alignment.CenterVertically)
                            .size(32f.dp)
                         )
                }
            }
            // card name + card description
            Column(
                modifier = Modifier.weight(1f).align(Alignment.CenterVertically)

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
                }) {
                    Text(text = "Remove")
                }
            }

        }

    }
}