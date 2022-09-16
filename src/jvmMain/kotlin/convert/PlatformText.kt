package convert

object PlatformText {


    val tab4reg = Regex(" {4}")
    val tab3reg = Regex(" {3}")
    val tab2reg = Regex(" {2}")
    val word = Regex("\\b\\w+\\b")

    val tab4 = 9
    val tab3 = 254  // код 8 - похоже удаление символа, юзаем 254 и проверяем как пойдет в асм тексте
    val tab2 = 255

    fun toAsm(text: String?, byTokens: Boolean = false): String {
        val sb = StringBuilder()
        val t = tabs(text)
//        println(t) // TODO проверить, асм примет коды табов в тексте ?
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
    private fun tabs(text: String?) = text?.replace(tab4reg, "${tab4.toChar()}")
        ?.replace(tab3reg, "${tab3.toChar()}")
        ?.replace(tab2reg, "${tab2.toChar()}")

}


fun main() {
    val r = PlatformText.toAsm("If   wall > enemy wall, 6 damage to enemy tower, else 6 damage")
    println(r)
}