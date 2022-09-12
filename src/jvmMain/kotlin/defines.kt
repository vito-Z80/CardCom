object Message {
    const val EMPTY = ""
    const val UNDEFINED = "Undefined"
    const val CLEAR = "Clear"
    const val NAN = "Nan"
    const val STRUCTURE = "Structure"
    const val VALUE = "Value"
    const val SIGN = "Sign"
}

fun Enum<*>.name() = name.lowercase().replace("_", " ").replaceFirstChar { it.uppercase() }

enum class SpecialsName {
    PLAY_AGAIN,
    DRAW,
    `CAN'T_DISCARD`,
    DISCARD;
}

enum class Variant {
    //  +7 Wall, 6 damage to enemy, Magic = 3
    GENERAL,        // операции с ресурсами (+-=) где = обозначает что кол-во ресурса заменяется новым количеством.

    //  Switch your wall with enemy wall
    SWITCH,         // обмен ресурсами

    //  All player's magic equals the highest player's magic
    HIGHEST,        // выравнивание ресурсов (ресурсы обоих игроков выравниваются по наивысшему показателю)

    //  противоположное от Highest
    LOWEST,         // выравнивание ресурсов (ресурсы обоих игроков выравниваются по наименьшему показателю)

    //  Enemy loses 10 gems, 5 bricks, you gain 1/2 amt. round up
    GET_HALF,       // когда враг теряет ресурсы, игрок получает половину от них (округление вверх)

    //  типа: все кирпичи оппонента отсыпаются игроку (исключить, либо очень редкие карты. Так-же ресурсов не может быть < 1)
    APPROPRIATE     // присвоить ресурс оппонента
}

enum class Player {
    PLAYER, ENEMY, ALL
}

enum class Structure {
    WALL, TOWER, QUARRY, BRICKS, MAGIC, GEMS, DUNGEON, RECRUITS
}

enum class Sign {
    LESS,
    MORE,
    EQUAL,
    MORE_E,
    LESS_E
}

fun Sign.name() = when (this) {
    Sign.LESS -> "<"
    Sign.MORE -> ">"
    Sign.EQUAL -> "="
    Sign.MORE_E -> ">="
    Sign.LESS_E -> "<="
    else -> error("No have this sign ${this.name}")
}