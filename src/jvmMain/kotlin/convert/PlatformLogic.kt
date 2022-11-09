package convert

import AppData
import Player
import Sign
import Structure
import Variant
import addLog
import androidx.compose.runtime.MutableState
import androidx.compose.ui.graphics.Color
import gson.NewCard
import hex
import signChars
import toAsmLabel
import toByte
import valueBy

/*
Вероятность можно вычислить по формуле:
P = 1 / K, где K — коэффициент букмекера.
Находите маржу букмекера по формуле: M = (S — 1) х 100%, где S — сумма вероятностей.
 */
// вероятность https://github.com/arcomage/arcomage-hd/blob/main/src/data/cards.ts
// коэффициент https://github.com/arcomage/arcomage-hd/blob/main/src/ai/coefs.ts
// баланс https://github.com/arcomage/arcomage-hd/blob/83e7a3257915bd178c3c7790d3c4e97f1baf746b/tools/card-balance/index.ts


// https://stackoverflow.com/questions/30492259/get-a-random-number-focused-on-center
//


private const val CALL_LOGIC = "\n\tcall logic"
private const val PUSH_DE = "\n\tpush de"
private const val CALL_SPECIALS = "\n\tcall LOGIC.exe_specials"
private const val CALL_EFFECT = "\n\tcall LOGIC.resource_calc"
private const val CALL_CONDITION = "\n\tcall LOGIC.exe_condition"

private const val CALL_CHECK_COST_CURRENCY = "call LOGIC.check_cost_currency\n\tret nz"
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
            logicCode.append("\n\tdw ${label}_DATA")
//            logicCode.append("\n\t$CALL_CHECK_COST_CURRENCY")
            PEffect.specials(card, card.specials.value)
            PEffect.effect(card, card.effects.value)
            PEffect.co(card, card.condition, label)
            logicCode.append(RET)
        }

//        AppData.cards.gson?.forEachIndexed { id, card ->
//            PEffect.co(card, card?.condition)
//        }
//
//        AppData.cards.gson?.forEachIndexed { id, card ->
//            PEffect.specials(card?.specials?.value)
//        }


        asmCode.append("\nCARDS = ${AppData.cards?.size ?: 0}\t; max: ${16384 / 144} cards.")
        asmCode.append(logicCodeMap)
        // probability map
        asmCode.append("\nprobability:")
        AppData.cards?.forEach {
            asmCode.append("\n\tdb ${it?.probability?.value?.toByte()?.hex() ?: "#01"}")
        }
        //
        asmCode.append(logicData)
        asmCode.append(logicCode)

        if (moduleName != null) {
            asmCode.insert(0, "\tmodule $moduleName")
            asmCode.append("\n\tendmodule")
        }

        return asmCode.toString()
    }

    /**
     * Если цена == 0, то валютный ресурс не имеет значения, так как вычета не будет.
     * Цена в ASM коде отображается как отрицательное HEX число если оно не равно нулю.
     * В ASM коде нужно учитывать когда цена рава нулю. Следующим байтом сразу будут идти данные эфекта.
     * Данные о валютном ресурсе будут отсутствовать.
     */
    private fun costCurrency(card: NewCard?) {
        val c = (card?.cardCost?.value?.toByte() ?: 0).toByte()
//        val cost = (0 - c).toByte().hex()
        val cost = card?.cardCost?.value?.toByte().let {
            if (it == null) null else (0 - it).toByte().hex()
        }
        val currency = Structure.values().indexOfFirst { it.name == card?.costCurrency?.value?.uppercase() }.let {
            if (it >= 0) it.valueBy(BitMask.WORD).toByte().hex() else null
        }
        if (cost != null) {
            logicData.append("\n\tdb $cost")
            logicData.append("\t; cost: ${card?.cardCost?.value}")
        } else {
            addLog("Cost not set for card: ${card?.cardName?.value?.uppercase()}", Color.Red)
        }

        if (currency != null) {
            if (c == (0).toByte()) {
                logicData.append("\n\t; The currency is irrelevant when the price is zero.")
            } else {
                logicData.append("\n\tdb $currency")
                logicData.append("\t; currency: ${card?.costCurrency?.value}")
            }
        } else {
            if (c == (0).toByte()) {
                logicData.append("\n\t; The currency is irrelevant when the price is zero.")
            } else {
                addLog("Currency not set for card: ${card?.cardName?.value?.uppercase()}", Color.Red)
            }
        }
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
        logicData.append("${Variant.values().indexOf(e.variant.value).valueBy(BitMask.BIT).toByte().hex()},")
        // structure
        logicData.append(
            Structure.values().indexOfFirst { it.name == e.structure.value?.uppercase() }.valueBy(BitMask.BIT).toByte()
                .hex()
                        )
        logicData.append("\t; effect: ${e.variant.value}, ${e.structure.value}")
    }

    /*
Присвоить новое значение ресурсу или получить значение ресурса оппонента
	#FF = получить значение ресурса оппонента: #FF, #01, #02 > enemy quarry
		значение бараков опонента присваивается значению бараков игрока
	!#FF = присвоить нвоое значение ресурсу: #NN, #02 > N player quarry
		присвоить значение #NN баракам игрока
*/
    private fun assign(e: NewCard.Effect) {
        // variant, value, player, structure
        logicData.append("\n\tdb ")
        // effect variant
//        logicData.append("${Variant.values().indexOf(e.variant.value).valueBy(BitMask.BIT).toByte().hex()},")
        // value
        val value = (e.value.value?.toByte() ?: 0xFF).toByte()
        logicData.append("${value.hex()},")
        // structure
        logicData.append(Structure.values().indexOfFirst { it.name == e.structure.value?.uppercase() }
                             .valueBy(BitMask.WORD).toByte().hex())

        if (value == (255).toByte()) {
            logicData.append(",")
            // player
            logicData.append(
                Player.values().indexOfFirst { it.name == e.player.value?.uppercase() }.toByte().hex()
                            )
            logicData.append(
                "\t; value, ${e.structure.value},  ${e.player.value} [enemy ${e.structure.value} value " + "copy to player]"
                            )
        } else {
            logicData.append("\t; value, ${e.structure.value} [$value copy to player ${e.structure.value}]")
        }
        logicCode.append("\n\tcall LOGIC.assign_calc")
    }

    /**
     * Данные основного эфекта.
     * 3 байта (player, resource, value)
     * player:
     *      0 - player
     *      1 - enemy
     *      2 - all
     * resource:
     *      0 - wall
     *      2 - tower
     *      4 - quarry
     *      6 - bricks
     *      8 - magic
     *      10 - gems
     *      12 - dungeon
     *      14 - recruits
     * value:
     *      value :)
     */
    private fun general(e: NewCard.Effect) {
        // action, variant, player, structure, value
        logicData.append("\n\tdb ")
        // player
        val player = Player.values().indexOfFirst { it.name == e.player.value?.uppercase() }.toByte().hex()
        // structure
        val structure =
            Structure.values().indexOfFirst { it.name == e.structure.value?.uppercase() }.valueBy(BitMask.WORD).toByte()
                .hex()
        // value
        val value = e.value.value?.toByte()?.hex()
        logicData.append("${player},")
        logicData.append("$structure,")
        logicData.append(value ?: error("Wrong value..."))
        logicData.append("\t; ${e.player.value}, ${e.structure.value}, ${e.value.value}")

//        val effectCounter = if (effects.size < 2) "" else "_${effects.size}"
        logicCode.append(CALL_EFFECT)
    }

    fun effect(card: NewCard?, effects: List<NewCard.Effect?>?) {
        if (!effects.isNullOrEmpty()) {
            val eSize = effects.size
            val undefinedSize = effects.count { it?.variant?.value == null }
            if (undefinedSize > 0) {
                addLog(
                    "Card effect is UNDEFINED for card: ${card?.cardName?.value?.uppercase()}. Effects: $eSize, Undefined: " + "$undefinedSize",
                    Color.Red
                      )
            }
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
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////////
    //                                                                                         //
    //                                 C O N D I T I O N                                       //
    //                                                                                         //
    /////////////////////////////////////////////////////////////////////////////////////////////

    fun co(card: NewCard?, condition: MutableState<NewCard.Condition?>?, label: String) {
        if (condition?.value == null) return
        // value берется с rightPart value и rightPart не используется если value != 255
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // value ?				            ; если value == 255 то условие по значению структуры, иначе по value
        // sign
        // player, structure				    ; left part
        // player, structure                    ; right part: присутствует только если value == 255
        // variant, player, structure, value 	        ; true content
        // variant, player, structure, value			; false content: если player == 255 то false content отсутствует
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        /*
            ; примерный вид кода условия по данным выше
            call exe_condition
            jr nz,.falseContent      ; (ret z) if falseContent is absent
            ; trueContent
            call effects
            ret
        .falseContent:
            ld hl,card_name_data_false_content
            call effects
            ret
         */
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        val value = (condition.value?.rightPart?.value?.get(0)?.value?.value ?: "255").toInt()

        val trueContent = condition.value?.conditionTrue?.value
        val falseContent = condition.value?.conditionFalse?.value
        val sign = Sign.values().indexOfFirst { it.signChars() == condition.value?.sign?.value }.valueBy(BitMask.BIT)
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
            Structure.values().indexOfFirst { it.name == left?.structure?.value?.uppercase() }.valueBy(BitMask.WORD)
                .toByte().hex()
        logicData.append("\n\tdb $structure\t; ${condition.value?.leftPart?.value?.get(0)?.structure?.value}")
        if (value == 255) {
            // right part
            val playerR = Player.values().indexOfFirst { it.name == right?.player?.value?.uppercase() }.toByte().hex()
            logicData.append("\n\tdb $playerR\t; ${condition.value?.rightPart?.value?.get(0)?.player?.value}")
            val structureR = Structure.values().indexOfFirst { it.name == right?.structure?.value?.uppercase() }
                .valueBy(BitMask.WORD).toByte().hex()
            logicData.append("\n\tdb $structureR\t; ${condition.value?.rightPart?.value?.get(0)?.structure?.value}")
        }
        logicCode.append(CALL_CONDITION)
        if (falseContent != null) {
            logicCode.append("\n\tjr nz,.falseContent")
        } else {
            logicCode.append("\n\tret nz")
        }
        // true content
        effect(card, trueContent)
        if (falseContent != null) {
            // false content
            logicCode.append(RET)
            logicCode.append("\n.falseContent:")
            logicCode.append("\n\tld hl,${label}_DATA_FALSE")
            logicData.append("\n${label}_DATA_FALSE:")
            effect(card, falseContent)
//            logicCode.append(RET)
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