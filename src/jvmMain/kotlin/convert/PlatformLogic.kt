package convert

import Action
import AppData
import Player
import Structure
import Variant
import androidx.compose.runtime.MutableState
import gson.NewCard
import hex
import toByte
import toLink


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

private const val CALL_LOGIC = "\n\tcall logic"
private const val PUSH_DE = "\n\tpush de"
private const val RET = "\n\tret"

private val logicData = StringBuilder()
private val logicCode = StringBuilder()
private val logicCodeMap = StringBuilder("\nmap:")
private val asmCode = StringBuilder()

const val MAX_VALUE: Byte = 32
const val MAX_BYTE: Byte = 255.toByte()

object PlatformLogic {


    fun convert(moduleName: String? = null): String {


        AppData.cards?.forEachIndexed { id, card ->
            PEffect.ef(card, card?.effects?.value)
            PEffect.co(card, card?.condition)
            PEffect.specials(card?.specials?.value)
        }



        asmCode.insert(0, logicCodeMap)
        asmCode.append(logicCode)

        if (moduleName != null) {
            asmCode.insert(0, "\tmodule $moduleName")
            asmCode.append("\n\tendmodule")
        }

        return asmCode.toString()
    }


}

object PEffect {


    /////////////////////////////////////////////////////////////////////////////////////////////
    //                                                                                         //
    //                                   E F F E C T                                           //
    //                                                                                         //
    /////////////////////////////////////////////////////////////////////////////////////////////
    private fun effect(e: NewCard.Effect?) {
        when (e?.variant?.value) {
            Variant.GENERAL, Variant.GET_HALF -> {
                general(e)
            }

            Variant.ASSIGN -> {
                assign(e)
            }

            Variant.SWITCH, Variant.SEIZE, Variant.HIGHEST, Variant.LOWEST -> {
                switch(e)
            }

            else -> {}
        }

    }

    private fun switch(e: NewCard.Effect) {
        // action,variant,structure
        logicData.append("\n\tdb ")
        // card action
        logicData.append("${Action.EFFECT.toByte().hex()},")
        // effect variant
        logicData.append("${Variant.values().indexOf(e.variant.value).toByte().hex()},")
        // structure
        logicData.append(Structure.values().indexOfFirst { it.name == e.structure.value?.uppercase() }.toByte().hex())
        logicData.append("\t; effect")
    }

    private fun assign(e: NewCard.Effect) {
        // action, variant, value, player, structure
        logicData.append("\n\tdb ")
        // card action
        logicData.append("${Action.EFFECT.toByte().hex()},")
        // effect variant
        logicData.append("${Variant.values().indexOf(e.variant.value).toByte().hex()},")

        val value = (e.value.value?.toByte() ?: 0xFF).toByte()
        // value
        logicData.append("${value.hex()},")
        // player
        logicData.append("${Player.values().indexOfFirst { it.name == e.player.value?.uppercase() }.toByte().hex()},")
        // structure
        logicData.append(Structure.values().indexOfFirst { it.name == e.structure.value?.uppercase() }.toByte().hex())
        logicData.append("\t; effect")
    }

    private fun general(e: NewCard.Effect) {
        // action, variant, player, structure, value
        logicData.append("\n\tdb ")
        // card action
        logicData.append("${Action.EFFECT.toByte().hex()},")
        // effect variant
        logicData.append("${Variant.values().indexOf(e.variant.value).toByte().hex()},")
        // player
        logicData.append("${Player.values().indexOfFirst { it.name == e.player.value?.uppercase() }.toByte().hex()},")
        // structure
        logicData.append(
            "${
                Structure.values().indexOfFirst { it.name == e.structure.value?.uppercase() }.toByte().hex()
            },"
        )
        // value
        logicData.append(e.value.value?.toByte()?.hex() ?: error("Wrong value..."))
        logicData.append("\t; effect")
    }

    fun ef(card: NewCard?, effects: List<NewCard.Effect?>?) {
        if (!effects.isNullOrEmpty()) {
            val link = card?.cardName?.value?.toLink() ?: error(" Wrong card link : $this")
            logicCodeMap.append("\n\tdw $link")
            logicCode.append("\n${link}:")
            logicCode.append("\n\tld hl,${link}_DATA")
            asmCode.append("\n${link}_DATA:")
            println(effects.size)
            effects.forEach { e ->
                logicData.clear()
                println(e?.player?.value)
                println(e?.value?.value?.toByte()?.hex())
                effect(e)
                asmCode.append(logicData)
                logicCode.append(PUSH_DE)
            }
            logicCode.append(RET)
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////////
    //                                                                                         //
    //                                 C O N D I T I O N                                       //
    //                                                                                         //
    /////////////////////////////////////////////////////////////////////////////////////////////

    fun co(card: NewCard?, condition: MutableState<NewCard.Condition?>?) {

    }

    /////////////////////////////////////////////////////////////////////////////////////////////
    //                                                                                         //
    //                                 S P E C I A L S                                         //
    //                                                                                         //
    /////////////////////////////////////////////////////////////////////////////////////////////

    fun specials(specials: NewCard.Special?) {
        logicData.clear()
        if (specials == null) return
        fun setSpec(specialIndex: Int) {
            // card action,special value
            logicData.append("\n\tdb ")
            // card action
            logicData.append("${Action.SPECIALS.toByte().hex()},")
            logicData.append("${specialIndex.toByte().hex()}\t; special")
        }
        if (specials.playAgain.value == true) setSpec(0)
        if (specials.draw.value == true) setSpec(1)
        if (specials.notDiscard.value == true) setSpec(2)
        if (specials.discard.value == true) setSpec(4)
        logicCode.append(logicData)
    }

}