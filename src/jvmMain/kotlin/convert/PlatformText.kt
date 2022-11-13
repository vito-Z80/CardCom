package convert

import AppData
import hex2
import toAsmLabel

object PlatformText {

    const val TEXT_END = "END"

    val tab4reg = Regex(" {4}")
//    val tab3reg = Regex(" {3}")
    val tab2reg = Regex(" {2}")
    val nextLineReg = Regex("\n")
    val word = Regex("\\b\\w+\\b")

//    val nextLine = '\n'
    val nextLine = (251).toChar()
    val tab4 = (252).toChar()
//    val tab3 = (253).toChar()  // код 8 - похоже удаление символа, юзаем 254 и проверяем как пойдет в асм тексте
    val tab2 = (254).toChar()

    val textMap = StringBuilder()
    val textData = StringBuilder()
    val asmText = StringBuilder()

    fun convert(moduleName: String? = null): String {

        asmText.clear()
        textMap.clear()
        textData.clear()

        AppData.cards?.forEach {
            textMap.append("\n\tdw ${it?.cardName?.value?.toAsmLabel()}")
            textData.append("\n${it?.cardName?.value?.toAsmLabel()}:")
            textData.append(toAsm(it?.cardName?.value, it?.zxCard?.value?.description?.value))
        }

        if (moduleName != null) {
            asmText.append("\tmodule $moduleName")
            asmText.append("\nSPACE_2 = 255")
//            asmText.append("\nSPACE_3 = 253")
            asmText.append("\nSPACE_4 = 254")
            asmText.append("\nNEW_LINE = 251")
            asmText.append("\nEND = 0")
            asmText.append("\nmap:$textMap\n\ndata:\n$textData")
            asmText.append("\n\tendmodule")
        } else {
            asmText.append("map:$textMap\n\ndata:\n$textData")
        }
        return asmText.toString()
    }

    fun toAsm(cardName: String?, cardText: String?, byTokens: Boolean = false) =
        "\n\tdb \"${cardName?.uppercase()}\",NEW_LINE" +
                "\n\tdb \"${tabs(cardText)?.uppercase()}\",$TEXT_END"


    // пробелы заменяет кодом
    // 4 пробела = код 9
    // 3 пробела = код 8
    // 2 пробела = код 7
    private fun tabs(text: String?) = text?.trimEnd()?.replace(tab4reg, "\",SPACE_4,\"")
//        ?.replace(tab3reg, "\",SPACE_3,\"")
        ?.replace(tab2reg, "\",SPACE_2,\"")
        ?.replace(nextLineReg, "\",NEW_LINE,\"")
        ?.replace("${(9).toChar()}","\",SPACE_4,\"")
        ?.replace("\"\"","")
        ?.replace(",,",",")

}


fun main() {
    val r = PlatformText.toAsm(
        "Elven Archers",
        "   If   wall > enemy wall, 6 damage\n to enemy tower, else 6 damage\n\t  \n"
    )
    println(r)
}