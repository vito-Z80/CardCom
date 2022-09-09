import androidx.compose.ui.text.capitalize

object Message {
    const val EMPTY = ""
    const val UNDEFINED = "Undefined"
    const val CLEAR = "Clear"
    const val NAN = "Nan"
    const val STRUCTURE = "Structure"
    const val PLAYER = "Player"
    const val VALUE = "Value"
    const val SIGN = "Sign"

}

enum class SpecialsName {
    PLAY_AGAIN,
    DRAW,
    NOT_DISCARD,
    DISCARD
}

val effectVariantList = listOf(
    //  +7 Wall, 6 damage to enemy, Magic = 3
    "General",          // операции с ресурсами (+-=) где = обозначает что кол-во ресурса заменяется новым количеством.
    //  Switch your wall with enemy wall
    "Switch",           // обмен ресурсами
    //  All player's magic equals the highest player's magic
    "Highest",          // выравнивание ресурсов (ресурсы обоих игроков выравниваются по наивысшему показателю)
    //  противоположное от Highest
    "Lowest",           // выравнивание ресурсов (ресурсы обоих игроков выравниваются по наименьшему показателю)
    //  Enemy loses 10 gems, 5 bricks, you gain 1/2 amt. round up
    "Get half",         // когда враг теряет ресурсы, игрок получает половину от них (округление вверх)
    //  типа: все кирпичи оппонента отсыпаются игроку (исключить, либо очень редкие карты. Так-же ресурсов не может быть < 1)
    "Appropriate",      // присвоить ресурс оппонента
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