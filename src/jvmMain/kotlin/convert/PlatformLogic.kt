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
import kotlin.random.Random

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


private const val PUSH_DE = "\n\tpush de"
private const val CALL_LOGIC = "call LOGIC"
private const val CALL_SPECIALS = "\n\t$CALL_LOGIC.exe_specials"
private const val CALL_EFFECT = "\n\t$CALL_LOGIC.resource_calc"
private const val CALL_CONDITION = "\n\t$CALL_LOGIC.exe_condition"

private const val CALL_CHECK_COST_CURRENCY = "$CALL_LOGIC.check_cost_currency\n\tret nz"
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
            val specData = PEffect.specials(card, card.specials.value)
            costCurrency(card, specData)
            logicCode.append("\n${label}:")
            logicCode.append("\n\tdw ${label}_DATA")
//            logicCode.append("\n\t$CALL_CHECK_COST_CURRENCY")
            PEffect.effect(card, card.effects.value)
            PEffect.co(card, card.condition, label)
            PEffect.specials(card, card.specials.value)

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
    private fun costCurrency(card: NewCard?, specData: Pair<Int, String>) {
        val c = (card?.cardCost?.value?.toByte() ?: 0).toByte()
//        val cost = (0 - c).toByte().hex()
//        val cost = card?.cardCost?.value?.let {
//            (it.toInt() or specData.first).toByte().hex()
//        }

        val cost = ((card?.cardCost?.value ?: "0").toInt() or specData.first).toByte().hex()
        val currency = Structure.values().indexOfFirst { it.name == card?.costCurrency?.value?.uppercase() }.let {
            if (it >= 0) it.valueBy(BitMask.WORD).toByte().hex() else null
        }
        logicData.append("\n\tdb $cost")
        logicData.append("\t; cost: $c, ${specData.second}")
        if (c == 0.toByte()) {
            addLog(
                "The price has not been set for the ${card?.cardName?.value?.uppercase()} card or is set to 0.",
                Color.Yellow
            )
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


    // TODO добавить карту использующую данный эфект для текстирования тут и в эмуле

    private fun switch(e: NewCard.Effect) {
        // variant,structure
        logicData.append("\n\tdb ")
        // structure
        logicData.append(
            Structure.values().indexOfFirst { it.name == e.structure.value?.uppercase() }.valueBy(BitMask.WORD).toByte()
                .hex()
        )
        val ending = if (e.structure.value?.endsWith("s") == true) "" else "`s"
        logicData.append("\t; Switch ${e.structure.value}$ending")
        logicCode.append("\n\tcall LOGIC.switch_calc")
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
//        val value = (e.value.value?.toByte() ?: 0xFF).toByte()
//        logicData.append("${value.hex()},")
        // structure
        logicData.append(Structure.values().indexOfFirst { it.name == e.structure.value?.uppercase() }
            .valueBy(BitMask.WORD).toByte().hex())

        logicData.append("\t; Assign rival ${e.structure.value} value")
//        if (value == (255).toByte()) {
//            logicData.append(",")
//            // player
//            logicData.append(
//                Player.values().indexOfFirst { it.name == e.player.value?.uppercase() }.toByte().hex()
//            )
//            logicData.append(
//                "\t; value, ${e.structure.value},  ${e.player.value} [enemy ${e.structure.value} value " + "copy to player]"
//            )
//        } else {
//            logicData.append("\t; value, ${e.structure.value} [$value copy to player ${e.structure.value}]")
//        }
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
//        logicCode.append(CALL_EFFECT)
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
            var gCount = 0
            var aCount = 0
            var sCount = 0
            effects.forEach { e ->
                when (e?.variant?.value) {
                    Variant.GENERAL -> {
                        general(e)
                        gCount++
                    }

                    Variant.ASSIGN -> {
                        assign(e)
                        aCount++
                    }

                    Variant.SWITCH -> {
                        switch(e)
                        sCount++
                    }

                    Variant.HIGHEST -> {
                        highest(e, "highest_calc")
                    }

                    Variant.LOWEST -> {
                        highest(e, "lowest_calc")
                    }

                    else -> {}
                }
            }
            if (gCount > 0) {
                logicCode.append("${CALL_EFFECT}${if (gCount > 1) "_$gCount" else ""}")
            }
            if (aCount > 0) {

            }
            if (sCount > 0) {

            }

        }
    }

    private fun highest(e: NewCard.Effect, call: String) {
        // variant,structure
        logicData.append("\n\tdb ")
        // structure
        logicData.append(
            Structure.values().indexOfFirst { it.name == e.structure.value?.uppercase() }.valueBy(BitMask.WORD).toByte()
                .hex()
        )
        val ending = if (e.structure.value?.endsWith("s") == true) "" else "`s"
        logicData.append("\t; by Highest ${e.structure.value}$ending")
        logicCode.append("\n\tcall LOGIC.$call")

    }

    /////////////////////////////////////////////////////////////////////////////////////////////
    //                                                                                         //
    //                                 C O N D I T I O N                                       //
    //                                                                                         //
    /////////////////////////////////////////////////////////////////////////////////////////////
    /*
        signs:
        <       ; true - <, false - >=
        >       ; true - >, false - <=
        ==      ; true - ==, false - !=
        <=      ; true - <=, false - >
        >=      ; true - >=, false - <
        >!=     ; true - >, false - <, == none
        <!=     ; true - <, false - >, == none
     */
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

    fun specials(card: NewCard?, specials: NewCard.Special?): Pair<Int, String> {
        if (specials == null) return Pair(0, " | No specials")
        var value = 0
        val description = StringBuilder()

        if (specials.playAgain.value == true) {
            value = value or 0x40
            description.append(" | Play again")
        }
        if (specials.pdp.value == true) {
            value = value or 0x80
            description.append(" | Play, Discard any, Play again")
        }
        if (specials.cantDiscard.value == true) {
            value = value or 0x20
            description.append(" | Can`t discard")
        }
        return Pair(value, description.toString())
    }

}


fun main() {
    val p1 = List(5) { 1 }
    val p2 = List(65) { 2 }
    val p3 = List(32) { 3 }

    val turns = 200
    val difficulty = 1

    val static = p1.plus(p2).plus(p3)
    val cardsCount = static.size
    val prob = p1.plus(p2).plus(p3).toMutableList()
    val turnsArray = List(cardsCount) { 0 }.toMutableList()
    val hits = List(cardsCount) { 0 }.toMutableList()


    repeat(turns) {
        while (true) {
            val rId = Random.nextInt(cardsCount)
            prob[rId]--

            hits[rId]++
            if (prob[rId] == 0) {
                prob[rId] = static[rId]
                turnsArray[rId]++
                break
            }
        }
    }

    var t1 = 0
    var t2 = 0
    var t3 = 0

    var h1 = 0
    var h2 = 0
    var h3 = 0

    static.forEachIndexed { id, value ->
        println("$value: ${hits[id]} | ${turnsArray[id]}")
        when (value) {
            1 -> {
                t1 += turnsArray[id];h1 += hits[id]
            }

            2 -> {
                t2 += turnsArray[id];h2 += hits[id]
            }

            3 -> {
                t3 += turnsArray[id];h3 += hits[id]
            }
        }
    }

    val perc1 = static.count { it == 1 }.toFloat() * 100f / cardsCount.toFloat()
    val perc2 = static.count { it == 2 }.toFloat() * 100f / cardsCount.toFloat()
    val perc3 = static.count { it == 3 }.toFloat() * 100f / cardsCount.toFloat()


    // TODO придумать рандом основываясь на вероятностях 1,2,3 без коэфициента

    println(static.sumOf { it })
//    println(static.reduce())
    println()
    println(perc1)
    println(perc2)
    println(perc3)
    println()

    println("Приоритет 1: Обращений = $h1 | Завершенных ходов = $t1")
    println("Приоритет 2: Обращений = $h2 | Завершенных ходов = $t2")
    println("Приоритет 3: Обращений = $h3 | Завершенных ходов = $t3")


}