import androidx.compose.ui.text.capitalize

object Message {
    const val EMPTY = ""
    const val UNDEFINED = "undefined"
    const val CLEAR = "Clear"
    const val NAN = "Nan"
    const val STRUCTURE = "Structure"
    const val PLAYER = "Player"
    const val VALUE = "Value"
    const val SIGN = "Sign"

}


//enum class EffectVariant {
//    GENERAL,
//    SWITCH;
//    fun text() = name.lowercase().replaceFirstChar { it.uppercase() }
//}

val effectVariantList = listOf(
    "General",
//    "Grab",
    "Switch",
)

val players = listOf(
    "Player",
    "Enemy",
    "All"
)
val structures = listOf(
    "Wall",
    "Tower",
    "Quarry",
    "Bricks",
    "Magic",
    "Gems",
    "Dungeon",
    "Recruits"
)
val signs = listOf(
    "<",
    ">",
    "=",
    ">=",
    "<=",
)
val numbers = List<String>(200) {
    if (it < 100) (-(100 - it)).toString()
    else if (it > 100) (+(it - 101)).toString()
    else Message.NAN
}