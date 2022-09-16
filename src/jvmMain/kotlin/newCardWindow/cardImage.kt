package newCardWindow

import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.toPixelMap
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cardImages
import gson.NewCard
import kotlin.math.floor

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun cardImage(newCard: NewCard?) {
    val scale = 8
    var pointColor: Color? by remember { mutableStateOf(null) }

    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        cardImages[newCard?.imagePath?.value]?.let { bm ->

            Image(
                bitmap = bm,
                contentDescription = "${newCard?.cardName}",
                filterQuality = FilterQuality.None,
                modifier = Modifier
                    .border(width = 1f.dp, color = Color.Black)
                    .padding(2f.dp)
                    .size((bm.width * scale).dp)
                    .onPointerEvent(
                        eventType = PointerEventType.Move,
                        pass = PointerEventPass.Main,
                        onEvent = { e ->
                            val position = e.changes[0].position
                            pointColor = bm.toPixelMap()[
                                    floor(position.x / scale).toInt(),
                                    floor(position.y / scale).toInt()
                            ]
                        }
                    ),
                alignment = Alignment.Center
            )
            Column(
                modifier = Modifier.fillMaxHeight(),
//                verticalArrangement = Arrangement.Center
            ) {
                repeat(4) {
                    Divider(
                        thickness = 1f.dp,
                        color = Color(1f,1f,1f,0.5f),
//                        modifier = Modifier.padding(top = (8f * scale).dp)
                    )
                    Spacer(modifier = Modifier.padding(top = (8f*scale).dp))
                }
            }

        }
    }
    Text(
        text = pointColor.toString(),
        modifier = Modifier.horizontalScroll(ScrollState(0)),
        textAlign = TextAlign.Center,
        fontSize = 24f.sp,
        softWrap = false
    )
}