package convert

import AppData
import Variant
import gson.NewCard
import name


object Tag {
    /*
        GENERAL_EFFECT:
        player, structure, value
general_effect:
        if player == player; player[IX] else enemy[IY]
        ld a,(player + STRUCTURE)
        add value
        jr nc,.l1
        ld a,1
.l1:
        ld (player + STRUCTURE),a
        ret
     */
    // логика эфекта с вычислениями (+-)
    const val GENERAL_EFFECT = 0b10000000

    /*
        ld a,value
        ld (player + structure),a
        ret
     */
    // логика эфекта с присвоением (без вычислений)
    const val EQUAL_EFFECT = 0b10100000


    /*
        CONDITION:
        sign
        player, structure
        player, structure, digit
     */
    // условие (знак условия, левая и правая части)
    // левая часть не может содержать value.
    // правая часть содержит только value или только player,structure
    const val CONDITION = 0b10000001

    // результат если условие верно
    const val TRUE = 0b10000010

    // результат если условие ложно
    const val FALSE = 0b10000100

    // специальные возможности карты
    const val SPECIAL = 0b10001000

    // валюта которой игрок должен оплатить карту
    const val CURRENCY = 0b10010000

}

object PlatformLogic {

    fun convert(moduleName: String? = null) {


        AppData.cards?.forEachIndexed { id, card ->


            card?.effects?.value?.forEach { e ->

                effect(e)
                e?.variant?.value
            }

        }

    }


    private fun effect(e: NewCard.Effect?) {
        val data = StringBuilder()
        when (e?.variant?.value) {
            Variant.GENERAL.name() -> {
                generalEffect(e)
            }

            Variant.ASSIGN.name() -> {}
            Variant.GET_HALF.name() -> {}
            Variant.SEIZE.name() -> {}
            Variant.HIGHEST.name() -> {}
            Variant.LOWEST.name() -> {}
            Variant.SWITCH.name() -> {}
        }

    }

    private fun generalEffect(e: NewCard.Effect) {

    }


    /*
    6 Damage. All players lose 5 bricks, gems and recruits

    data:
        general,wall,enemy,-6
        5,all,bricks
        5,all,gems
        5,all,recruits

    cardCode:
        ld hl,data
        call effectCode
        call effectCode
        call effectCode
        jp effectCode

effectCode:
        ld a,(hl)   ; variant
        cp general
        jr z,effectGeneral

        ld e,(hl)   ; structure
        inc hl
        ld a,(hl)   ; player
        inc hl
        ld d,#IX
        cp player
        jr z,.l2
        ld d,#IY
        cp enemy
        jp nz,ALL
.l2:
        ld (effect + 2),de
        ld (eff + 2),de
        call effect
        inc hl
        ret
        //
effect:
        ld a,(# + structure)
        add (hl)
eff:
        ld (ix + structure),a

set_player:
        ld a,(hl)
        cp player

set_structure:
        ld a,(hl)
        ld (effect + 4),a
        ld (eff + 4),a
        ret

     */

}