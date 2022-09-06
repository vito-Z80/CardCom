object Message {
    const val EMPTY = ""
    const val UNDEFINED = "undefined"
    const val NAN = "Nan"
    const val STRUCTURE = "Structure"
    const val PLAYER = "Player"
    const val VALUE = "Value"
    const val SIGN = "Sign"

}


val effectVariantList = listOf(
    "General",
    "Grab",
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
val numbers = List<String>(101) {
    if (it == 0) Message.NAN
    else (it - 1).toString()
}