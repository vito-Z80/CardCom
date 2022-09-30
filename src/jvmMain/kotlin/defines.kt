object Message {
    const val EMPTY = ""
    const val UNDEFINED = "Undefined"
    const val CLEAR = "Clear"
    const val NAN = "Nan"
    const val STRUCTURE = "Structure"
    const val VALUE = "Value"
    const val SIGN = "Sign"
}


enum class Specials {
    // 0,1,2,4
    PLAY_AGAIN,
    DRAW,
    `CAN'T_DISCARD`,
    DISCARD;
}

enum class Variant {
    // 0,1,2,4,8,16,32,64

    //  +7 Wall, 6 damage to enemy
    GENERAL,        // операции с ресурсами (+-)

    //  Magic = 3
    ASSIGN,         // присвоить ресурсу новое значение

    //  Switch your wall with enemy wall
    SWITCH,         // обмен ресурсами

    //  All player's magic equals the highest player's magic
    HIGHEST,        // выравнивание ресурсов (ресурсы обоих игроков выравниваются по наивысшему показателю)

    //  противоположное от Highest
    LOWEST,         // выравнивание ресурсов (ресурсы обоих игроков выравниваются по наименьшему показателю)

    //  Enemy loses 10 gems, 5 bricks, you gain 1/2 amt. round up
    GET_HALF,       // когда враг теряет ресурсы, игрок получает половину от них (округление вверх)

    //  типа: все кирпичи оппонента отсыпаются игроку (исключить, либо очень редкие карты. Так-же ресурсов не может быть < 1)
    SEIZE     // завладеть ресурсом оппонента

}

class Action {

    companion object {
        const val EFFECT = 0
        const val CONDITION = 1
        const val SPECIALS = 2
    }
}

enum class Player {
    // 0,1,2
    PLAYER, ENEMY, ALL
}

enum class Structure {
    // 0,1,2,3,4,5,6,7
    WALL, TOWER, QUARRY, BRICKS, MAGIC, GEMS, DUNGEON, RECRUITS
}

enum class Sign {
    // 0,1,2,4,8
    LESS,
    MORE,
    EQUAL,
    MORE_E,
    LESS_E;
}

fun Sign.name() = when (this) {
    Sign.LESS -> "<"
    Sign.MORE -> ">"
    Sign.EQUAL -> "="
    Sign.MORE_E -> ">="
    Sign.LESS_E -> "<="
    else -> error("No have this sign ${this.name}")
}
///////////////////////////////////////////////////////////////////////////////////////////////////////
