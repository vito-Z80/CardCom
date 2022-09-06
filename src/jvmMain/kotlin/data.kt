import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.layout
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import gson.NewCard

object AppData {

    var showCardDialog by mutableStateOf(false)
    var showRemoveDialog by mutableStateOf(false)
    var tmpCard: NewCard? by mutableStateOf(null)


    var cards: List<NewCard?>? by mutableStateOf(null)  // массив карт

    var popupPosition by mutableStateOf(DpOffset(0f.dp, 0f.dp))

    var specialsSelect: String? by mutableStateOf(null)


    // clears data
    var clearConditionsData by mutableStateOf(false)

}


/**
 * словарь изображений карт
 */
val cardImages = mutableMapOf<String?, ImageBitmap>()
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
