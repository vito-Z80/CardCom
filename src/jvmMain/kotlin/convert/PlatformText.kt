package convert

import hex2

object PlatformText {


    val tab4reg = Regex(" {4}")
    val tab3reg = Regex(" {3}")
    val tab2reg = Regex(" {2}")
    val nextLineReg = Regex("\n")
    val word = Regex("\\b\\w+\\b")

    val nextLine = '\n'
    val tab4 = (9).toChar()
    val tab3 = (254).toChar()  // код 8 - похоже удаление символа, юзаем 254 и проверяем как пойдет в асм тексте
    val tab2 = (255).toChar()

    fun toAsm(text: String?, byTokens: Boolean = false): String {
        val sb = StringBuilder()
        val t = tabs(text)
        println(t) // TODO проверить, асм примет коды табов в тексте ?
        sb.append("\t; $text\n\tdb ")
        t?.forEachIndexed { id, c ->
            sb.append("#" + String.format("%02X", c.code))
            if (id < t.length - 1) sb.append(", ")
        }

        if (byTokens) {
            // TODO паковать текст описания карты по токенам.
            Unit
        }
        return sb.toString()
    }

    // пробелы заменяет кодом
    // 4 пробела = код 9
    // 3 пробела = код 8
    // 2 пробела = код 7
    private fun tabs(text: String?) = text?.replace(tab4reg, "\",${hex2(tab4)},\"")
        ?.replace(tab3reg, "\",${hex2(tab3)},\"")
        ?.replace(tab2reg, "\",${hex2(tab2)},\"")
        ?.replace(nextLineReg,"\",${hex2(nextLine)},\"")

}

    // TODO завершить компиляцию текста в модуль

fun main() {
    val r = PlatformText.toAsm("If   wall > enemy wall, 6 damage\n to enemy tower, else 6 damage")
    println(r)
}