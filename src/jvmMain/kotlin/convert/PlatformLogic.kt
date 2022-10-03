package convert

import AppData
import Player
import Sign
import Structure
import Variant
import androidx.compose.runtime.MutableState
import bitValueByIndex
import gson.NewCard
import hex
import toAsmLabel
import toByte


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
private const val CALL_SPECIALS = "\n\tcall LOGIC.exe_specials"
private const val CALL_EFFECT = "\n\tcall LOGIC.exe_effect"

private const val CALL_CHECK_COST_CURRENCY = "\n\tcall LOGIC.check_cost_currency\n\tret c"
private const val RET = "\n\tret"

private val logicData = StringBuilder()
private val logicCode = StringBuilder()
private val logicCodeMap = StringBuilder()
private val asmCode = StringBuilder()

const val MAX_VALUE: Byte = 32
const val MAX_BYTE: Byte = 255.toByte()

object PlatformLogic {


    fun convert(moduleName: String? = null): String {


        asmCode.clear()
        logicCodeMap.clear().append("\nmap:")
        logicData.clear()
        logicCode.clear()
        AppData.cards?.forEachIndexed { id, card ->
            val label = card?.cardName?.value?.toAsmLabel() ?: error(" Wrong card label : $this")
            logicCodeMap.append("\n\tdw $label")
            logicData.append("\n${label}_DATA:")
            costCurrency(card)
            logicCode.append("\n${label}:")
            logicCode.append("\n\tld hl,${label}_DATA")
            logicCode.append("\n\t$CALL_CHECK_COST_CURRENCY")
            PEffect.specials(card, card.specials.value)
            PEffect.effect(card, card.effects.value)
            PEffect.co(card, card.condition)
            logicCode.append(RET)
        }

//        AppData.cards?.forEachIndexed { id, card ->
//            PEffect.co(card, card?.condition)
//        }
//
//        AppData.cards?.forEachIndexed { id, card ->
//            PEffect.specials(card?.specials?.value)
//        }


        asmCode.append(logicCodeMap)
        asmCode.append(logicData)
        asmCode.append(logicCode)

        if (moduleName != null) {
            asmCode.insert(0, "\tmodule $moduleName")
            asmCode.append("\n\tendmodule")
        }

        return asmCode.toString()
    }

    private fun costCurrency(card: NewCard?) {
        val cost = card?.cardCost?.value?.toByte()?.hex()
        val currency = Structure.values().indexOfFirst { it.name == card?.costCurrency?.value?.uppercase() }
            .toByte().hex()
        logicData.append("\n\tdb $currency")
        logicData.append("\t; currency: ${card?.costCurrency?.value}")
        logicData.append("\n\tdb $cost")
        logicData.append("\t; cost: ${card?.cardCost?.value}")
    }

}

object PEffect {


    /////////////////////////////////////////////////////////////////////////////////////////////
    //                                                                                         //
    //                                   E F F E C T                                           //
    //                                                                                         //
    /////////////////////////////////////////////////////////////////////////////////////////////

    private fun switch(e: NewCard.Effect) {
        // variant,structure
        logicData.append("\n\tdb ")
        // variant
        logicData.append("${Variant.values().indexOf(e.variant.value).bitValueByIndex().toByte().hex()},")
        // structure
        logicData.append(
            Structure.values().indexOfFirst { it.name == e.structure.value?.uppercase() }.bitValueByIndex().toByte()
                .hex()
        )
        logicData.append("\t; effect: ${e.variant.value}, ${e.structure.value}")
    }

    private fun assign(e: NewCard.Effect) {
        // variant, value, player, structure
        logicData.append("\n\tdb ")
        // effect variant
        logicData.append("${Variant.values().indexOf(e.variant.value).bitValueByIndex().toByte().hex()},")
        // value
        val value = (e.value.value?.toByte() ?: 0xFF).toByte()
        logicData.append("${value.hex()},")
        // player
        logicData.append("${Player.values().indexOfFirst { it.name == e.player.value?.uppercase() }.toByte().hex()},")
        // structure
        logicData.append(
            Structure.values().indexOfFirst { it.name == e.structure.value?.uppercase() }.bitValueByIndex().toByte()
                .hex()
        )
        logicData.append("\t; effect: ${e.variant.value}, ${e.value.value}, ${e.player.value}, ${e.structure.value}")
    }

    private fun general(e: NewCard.Effect) {
        // action, variant, player, structure, value
        logicData.append("\n\tdb ")
        // effect variant
        val variant = Variant.values().indexOf(e.variant.value).bitValueByIndex().toByte().hex()
        // player
        val player = Player.values().indexOfFirst { it.name == e.player.value?.uppercase() }.toByte().hex()
        // structure
        val structure =
            Structure.values().indexOfFirst { it.name == e.structure.value?.uppercase() }.bitValueByIndex().toByte()
                .hex()
        // value
        val value = e.value.value?.toByte()?.hex()
        logicData.append("$variant,")
        logicData.append("${player},")
        logicData.append("$structure,")
        logicData.append(value ?: error("Wrong value..."))
        logicData.append("\t; effect: ${e.variant.value}, ${e.player.value}, ${e.structure.value}, ${e.value.value}")
    }

    fun effect(card: NewCard?, effects: List<NewCard.Effect?>?) {
        if (!effects.isNullOrEmpty()) {
            effects.forEach { e ->
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
            val effectCounter = if (effects.size < 2) "" else "_${effects.size}"
            logicCode.append(CALL_EFFECT + effectCounter)
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////////
    //                                                                                         //
    //                                 C O N D I T I O N                                       //
    //                                                                                         //
    /////////////////////////////////////////////////////////////////////////////////////////////

    fun co(card: NewCard?, condition: MutableState<NewCard.Condition?>?) {
        if (condition?.value == null) return
        // value берется с rightPart value и rightPart не используется если value != 255
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // value ?				            ; если value == 255 то условие по значению структуры, иначе по value
        // sign
        // player, structure				; left part
        // player, structure                ; right part: присутствует только если value == 255
        // player, structure, value 	    ; true content
        // player, structure, value			; false content: если player == 255 то false content отсутствует
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        val value = (condition.value?.rightPart?.value?.get(0)?.value?.value ?: "255").toInt()

        val trueContent = condition.value?.conditionTrue?.value
        val falseContent = condition.value?.conditionFalse?.value
        // FIXME sign хуйню считает выдет всегда #40 (it.name() мозгу пудрит)
        val sign = Sign.values().indexOfFirst { it.name == condition.value?.sign?.value }.bitValueByIndex()
        val left = condition.value?.leftPart?.value?.get(0)
        val right = condition.value?.rightPart?.value?.get(0)
        // value
        logicData.append("\n\tdb ${value.toByte().hex()}\t; #FF by structure value else by value.")
        // sign
        logicData.append("\n\tdb ${sign.toByte().hex()}\t; sign ${condition.value?.sign?.value}")
        // left part
        val player = Player.values().indexOfFirst { it.name == left?.player?.value?.uppercase() }.toByte().hex()
        logicData.append("\n\tdb $player\t; ${condition.value?.leftPart?.value?.get(0)?.player?.value}")
        val structure =
            Structure.values().indexOfFirst { it.name == left?.structure?.value?.uppercase() }.bitValueByIndex()
                .toByte()
                .hex()
        logicData.append("\n\tdb $structure\t; ${condition.value?.leftPart?.value?.get(0)?.structure?.value}")
        if (value == 255) {
            // right part
            val playerR = Player.values().indexOfFirst { it.name == right?.player?.value?.uppercase() }.toByte().hex()
            logicData.append("\n\tdb $playerR\t; ${condition.value?.rightPart?.value?.get(0)?.player?.value}")
            val structureR =
                Structure.values().indexOfFirst { it.name == right?.structure?.value?.uppercase() }.bitValueByIndex()
                    .toByte()
                    .hex()
            logicData.append("\n\tdb $structureR\t; ${condition.value?.rightPart?.value?.get(0)?.structure?.value}")
        }
        // true content
        effect(card, trueContent)
        if (falseContent != null) {
            // false content
            effect(card, falseContent)
        } else {
            logicData.append("\n\tdb #FF\t; no false content in condition.")
        }


    }


    /////////////////////////////////////////////////////////////////////////////////////////////
    //                                                                                         //
    //                                 S P E C I A L S                                         //
    //                                                                                         //
    /////////////////////////////////////////////////////////////////////////////////////////////

    fun specials(card: NewCard?, specials: NewCard.Special?) {
        if (specials == null) return
        var specialCounter = 0

        fun setSpec(specialIndex: Int) {
            // special value
            logicData.append("\n\tdb ")
            logicData.append("${specialIndex.toByte().hex()}\t; special")
            specialCounter++
        }
        if (specials.playAgain.value == true) {
            setSpec(0)
            logicData.append(" | Play again")
        }
        if (specials.draw.value == true) {
            setSpec(1)
            logicData.append(" | Draw")
        }
        if (specials.notDiscard.value == true) {
            setSpec(2)
            logicData.append(" | Can`t discard")
        }
        if (specials.discard.value == true) {
            setSpec(4)
            logicData.append(" | Discard")
        }
        logicCode.append(CALL_SPECIALS + if (specialCounter < 2) "" else "_${specialCounter}")
    }

}