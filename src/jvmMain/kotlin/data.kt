import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import gson.NewCard
import java.time.Instant
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.concurrent.TimeUnit


object AppData {


    var logText: List<Pair<String, Color>> by mutableStateOf(ArrayList<Pair<String, Color>>())
    var showCardDialog by mutableStateOf(false)
    var showRemoveDialog by mutableStateOf(false)
    var tmpCard: NewCard? by mutableStateOf(null)


    var cards: List<NewCard?>? by mutableStateOf(null)  // массив карт

    var convert by mutableStateOf(false)

}


/**
 * словарь изображений карт
 */
val cardImages = mutableMapOf<String?, ImageBitmap>()

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
fun addLog(text: String, color: Color = Color.Black) {
    val er = if (color == Color.Red) "[ERROR]" else ""

    // https://stackoverflow.com/questions/4142313/convert-timestamp-in-milliseconds-to-string-formatted-time-in-java
    val instant: Instant = Instant.ofEpochMilli(Date().time)
    val zdt = ZonedDateTime.ofInstant(instant, ZoneOffset.UTC)
    val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss")
    val time: String = "[${formatter.format(zdt)}]"

    AppData.logText = listOf(*AppData.logText.toTypedArray().plus(Pair("$time$er: $text", color)))
}