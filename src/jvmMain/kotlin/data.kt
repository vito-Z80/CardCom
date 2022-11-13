import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import gson.NewCard
import io.ktor.utils.io.*
import java.io.InputStream
import java.io.OutputStream
import java.net.Socket
import java.time.Instant
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.*


object ZxData {
    var xpeccyData: String by mutableStateOf("")

    var socket: Socket? by mutableStateOf(null)
    var input: InputStream? by mutableStateOf(null)
    var output: OutputStream? by mutableStateOf(null)
    var showConnectDialog by mutableStateOf(false)

    var tryConnect by mutableStateOf(false)

    var exPlayerData = mutableStateMapOf<String, Int>()
    var exOpponentData = mutableStateMapOf<String, Int>()
    var playerData = mutableStateMapOf<String, Int>()
    var opponentData = mutableStateMapOf<String, Int>()


}


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
    val er = if (color == Color.Red) "[ERROR]" else if (color == Color.Yellow) "[warning]" else ""

    // https://stackoverflow.com/questions/4142313/convert-timestamp-in-milliseconds-to-string-formatted-time-in-java
    val instant: Instant = Instant.ofEpochMilli(Date().time)
    val zdt = ZonedDateTime.ofInstant(instant, ZoneOffset.UTC)
    val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss")
    val time: String = "[${formatter.format(zdt)}]"

    AppData.logText = listOf(*AppData.logText.toTypedArray().plus(Pair("$time$er: $text", color)))
}