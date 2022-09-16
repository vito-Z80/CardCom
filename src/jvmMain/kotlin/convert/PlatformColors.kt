package convert

import androidx.compose.ui.graphics.Color
import java.util.*


object PlatformColor {

    /**
     * Получение цвета формата ZX Spectrum Classic из кусочка RGBA изображения 8х8 пикселей
     * @param rgbaSymbol 64 цвета из кусочка изображения 8х8 пикселей.
     */
    fun rgbaToZxClassic(rgbaSymbol: List<ULong>): Byte {
        val colors = TreeSet(rgbaSymbol).sortedBy { c -> rgbaSymbol.count { it == c } }.reversed()
//        println(colors.joinToString { Color(it).toString() })
        var zxColor = 0
        var bright = 0
        fun set(colorComponent: Float, colorBit: Int) {
            if (colorComponent == 1f) {
                bright = bright or 0x40
            }
            zxColor = zxColor or if (colorComponent == 0f) 0 else colorBit
        }

        if (colors.size == 1) {
            set(Color(colors[0]).blue, 1)
            set(Color(colors[0]).red, 2)
            set(Color(colors[0]).green, 4)
            val c = zxColor
            set(Color(colors[0]).blue, 1)
            set(Color(colors[0]).red, 2)
            set(Color(colors[0]).green, 4)
            zxColor = zxColor or (c shl 3)
        } else {
            set(Color(colors[0]).blue, 1)
            set(Color(colors[0]).red, 2)
            set(Color(colors[0]).green, 4)
            val c = zxColor
            set(Color(colors[1]).blue, 1)
            set(Color(colors[1]).red, 2)
            set(Color(colors[1]).green, 4)
            zxColor = zxColor or (c shl 3)
        }

        return (zxColor or bright).toByte()
    }

}
