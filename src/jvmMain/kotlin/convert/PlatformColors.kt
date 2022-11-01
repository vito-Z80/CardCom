package convert

import androidx.compose.ui.graphics.Color
import java.util.*


object PlatformColor {

    /**
     * Получение цвета формата ZX Spectrum Classic из кусочка RGBA изображения 8х8 пикселей.
     * Более 2-х цветов в кусочке 8х8 не учитывается.
     * Цвет < 1.0 = нет яркости.
     * Цвет < 0.25 = 0.0.
     * @param rgbaSymbol 64 цвета из кусочка изображения 8х8 пикселей.
     */
    fun rgbaToZxClassic(rgbaSymbol: List<ULong>): Byte {
        val colors = TreeSet(rgbaSymbol).sortedBy { c -> rgbaSymbol.count { it == c } }.reversed()
        var zxColor = 0
        var bright = 0
        fun set(colorComponent: Float, colorBit: Int) {
            if (colorComponent == 1f) {
                bright = bright or 0x40
            }
            zxColor = zxColor or if (colorComponent < 0.25f) 0 else colorBit // < 0.25 погрешность цвета 0.0
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
            set(Color(colors[0]).blue, 1 shl 3)
            set(Color(colors[0]).red, 2 shl 3)
            set(Color(colors[0]).green, 4 shl 3)
            val c = zxColor
            set(Color(colors[1]).blue, 1)
            set(Color(colors[1]).red, 2)
            set(Color(colors[1]).green, 4)
            zxColor = zxColor or c
        }
        return (zxColor or bright).toByte()
    }

}
