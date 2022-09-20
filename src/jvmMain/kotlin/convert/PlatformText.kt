package convert

import AppData
import hex2
import toLink

object PlatformText {

    const val TEXT_END = "#00"

    val tab4reg = Regex(" {4}")
    val tab3reg = Regex(" {3}")
    val tab2reg = Regex(" {2}")
    val nextLineReg = Regex("\n")
    val word = Regex("\\b\\w+\\b")

    val nextLine = '\n'
    val tab4 = (9).toChar()
    val tab3 = (254).toChar()  // код 8 - похоже удаление символа, юзаем 254 и проверяем как пойдет в асм тексте
    val tab2 = (255).toChar()

    val textMap = StringBuilder()
    val textData = StringBuilder()
    val asmText = StringBuilder()

    fun convert(moduleName: String? = null): String {

        asmText.clear()
        textMap.clear()
        textData.clear()

        AppData.cards?.forEach {
            textMap.append("\n\tdw ${it?.cardName?.value?.toLink()}")
            textData.append("\n${it?.cardName?.value?.toLink()}:")
            textData.append(toAsm(it?.cardName?.value, it?.zxCard?.value?.description?.value))
        }

        if (moduleName != null) {
            asmText.append("\tmodule $moduleName\n")
            asmText.append("map:$textMap\n\ndata:\n$textData")
            asmText.append("\n\tendmodule")
        } else {
            asmText.append("map:$textMap\n\ndata:\n$textData")
        }
        return asmText.toString()
    }

    fun toAsm(cardName: String?, cardText: String?, byTokens: Boolean = false) =
        "\n\tdb \"${cardName?.uppercase()}\",${hex2(nextLine)}" +
                "\n\tdb \"${tabs(cardText)?.uppercase()}\",$TEXT_END"


    // пробелы заменяет кодом
    // 4 пробела = код 9
    // 3 пробела = код 8
    // 2 пробела = код 7
    private fun tabs(text: String?) = text?.trimEnd()?.replace(tab4reg, "\",${hex2(tab4)},\"")
        ?.replace(tab3reg, "\",${hex2(tab3)},\"")
        ?.replace(tab2reg, "\",${hex2(tab2)},\"")
        ?.replace(nextLineReg, "\",${hex2(nextLine)},\"")

}

// TODO завершить компиляцию текста в модуль

fun main() {
    val r = PlatformText.toAsm(
        "Elven Archers",
        "   If   wall > enemy wall, 6 damage\n to enemy tower, else 6 damage\n\t  \n"
    )
    println(r)
}