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

        struct player
        PLAYER      0
        ENEMY       1
        ALL         2
        ends

        struct structure
        WALL        0
        TOWER       1
        QUARRY      2
        BRICKS      3
        MAGIC       4
        GEMS        5
        DUNGEON     6
        RECRUITS    7

        struct card_action
        EFFECT      0
        CONDITION   1
        SPECIALS    2
        ends

        struct effect_variant
        GENERAL     0
        ASSIGN      1
        SWITCH      2
        HIGHEST     4
        LOWEST      8
        GET_HALF    16
        SEIZE       32
        ends


    6 Damage. All players lose 5 bricks, gems and recruits

    data:
        card_action.EFFECT,general,enemy,wall,-6
        5,all,bricks
        5,all,gems
        5,all,recruits

    cardCode:
        ld hl,data
        call logic
        call logic
        call logic
        jp effectCode


logic:
        ld a,(hl)       ; card action
        inc hl
        or a
        jr z,exe_effect
        rrca
        jr c,exe_condition
        rrca
        je c,exe_specials
        ret

exe_effect:
        ld a,(hl)       ; effect variant
        inc hl
        or a
        jr z,effect_general
        rrca
        jr c,effect_assign
        rrca
        jr c,effect_switch
        rrca
        jr c,effect_highest
        rrca
        jr c,effect_lowest
        rrca
        jr c,effect_get_half
        rrca
        jr c,effect_seize
        ret
effect_general:
        ld a,(hl)       ; player
        inc hl
        or a
        jr z,effect_general_player
        rrca
        jr c,effect_general_enemy
        rrca
        ret nc
        ; for all players
        push hl
        call effect_general_player
        pop hl
        jp effect_general_enemy

effect_general_enemy:
        ld a,(hl)       ; structure
        inc hl
        ld (eec1 + 3),a
        ld (eec2 + 3),a
        jr effect_enemy_calc

effect_enemy_calc:
        ld a,(hl)       ; value (max = #20)
.eec1:
        ld a,(ix + 0)
        ld c,a
        ld a,(hl)       ; ; value (max = -#20 +#20)
        cp #80
        jr nc,.minus
        add c
        jr nc,.eec2
        ld a,#FF
.eec2:
        ld (ix + 0),a
        jr run
.minus:
        neg
        ld b,a
        ld a,c
        sub b
        jr z,.l4
        jr nc,.eec2
.l4:
        ld a,1
        jr .eec2





        //////////////////////////////////////////////////////////////////////////////
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




    /////////////////////////////////////////////////////////////////////////////////////


     */

}