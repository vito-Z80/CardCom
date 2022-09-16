package convert

import androidx.compose.ui.graphics.toPixelMap
import cardImages
import java.util.*

object PlatformSprite {

    const val SYMBOL_SIZE = 8
    const val DB = "\tdb "
    const val NEW_LINE = "\n"
    const val COMMA = ", "


    val spriteMap = StringBuilder()
    val spriteData = StringBuilder()
    val asmText = StringBuilder()

    fun getAsm(
        attributes: ByteArray,
        moduleName: String? = null,
        sprite: ByteArray? = null,
        spriteSymbols: ArrayList<ByteArray>? = null,
    ): String {
        spriteMap.clear()
        spriteData.clear()
        asmText.clear()
        if (sprite != null) {

            sprite.joinToString(
                separator = ", ",

            )

            sprite.forEachIndexed { id, byte ->
                if (id % 4 == 0)
                    spriteData.append(DB)
                spriteData.append("#${byte.toUByte().toString(16)}$COMMA")

            }

        } else if (spriteSymbols != null) {


        }


        if (moduleName != null) {
            asmText.append("\tmodule $moduleName\n")
            asmText.append("map:\n$spriteMap\n\nsprites:\n$spriteData")
            asmText.append("\tendmodule")
        } else {
            asmText.append("map:\n$spriteMap\n\nsprites:\n$spriteData")
        }
        return asmText.toString()
    }

    // linear sprite >> all attributes
    fun asSprite(imagePath: String?): Pair<ByteArray, ByteArray>? {
        val symbols = asSpriteSymbols(imagePath) ?: return null
        val pm = cardImages[imagePath]?.toPixelMap() ?: return null
        val sprite = ArrayList<Byte>()
        val range = 0..(pm.width / SYMBOL_SIZE) * (pm.height / SYMBOL_SIZE)
        for (i in range step pm.width / SYMBOL_SIZE) {
            repeat(SYMBOL_SIZE) { line ->
                repeat(pm.width / SYMBOL_SIZE) { x ->
                    val byte = symbols.first[i + x][line]
                    sprite.add(byte)
                }
            }
        }
        return Pair(sprite.toByteArray(), symbols.second)
    }

    // sprite as symbols 8x8 (1x8) >> all attributes
    fun asSpriteSymbols(imagePath: String?): Pair<ArrayList<ByteArray>, ByteArray>? {
        if (imagePath == null) return null
        val pm = cardImages[imagePath]?.toPixelMap() ?: return null

        val symbolsX = pm.width / SYMBOL_SIZE
        val symbolsY = pm.height / SYMBOL_SIZE

        println("+++++++++++++++++")
        println(pm.width)
        println(pm.height)
        println("+++++++++++++++++")
        // атрибуты спрайта 1 байт на спрайт 8х8 (1х8)
        val attributes = ArrayList<Byte>()
        // спрайты 8х8 (1х8) по 8 байт каждый
        val symbolSprites = ArrayList<ByteArray>()

        println(imagePath)
        repeat(symbolsY) { sY ->
            repeat(symbolsX) { sX ->

                val x = sX * SYMBOL_SIZE
                val y = sY * SYMBOL_SIZE

                // 64 цвета с кусочка изображения 8х8
                val pixelsColor = ArrayList<ULong>()
                repeat(SYMBOL_SIZE) { inY ->
                    repeat(SYMBOL_SIZE) { inX ->
                        pixelsColor.add(pm[x + inX, y + inY].value)
                    }
                }
                attributes.add(PlatformColor.rgbaToZxClassic(pixelsColor))
                // количество уникальных цветов
                val uniqueColors = TreeSet(pixelsColor).sortedBy { c -> pixelsColor.count { it == c } }.reversed()
                // спрайт 8х8 пикселей (1х8) 8 байт
                val sprite8b = ArrayList<Byte>()
                repeat(SYMBOL_SIZE) { inY ->
                    var byte = 0
                    var bit = 1
                    repeat(SYMBOL_SIZE) { inX ->
                        // биты ставяться только по первому уникальному цвету (ink), второй уникальный цвет идет как paper
                        // оставшиеся цвета игнорятся
                        // TODO сделать что бы биты ставились по уникальному цвету которого меньше всего в символе
                        //  а лишние цвета приравнивать к ближайшему уникальному цвету
                        val pix = pm[x + inX, y + inY].value
                        if (uniqueColors[0] == pix) {
                            byte = byte or bit
                        }
                        bit = bit shl 1
                    }
                    sprite8b.add(byte.toByte())
                }
                symbolSprites.add(sprite8b.toByteArray())
            }
        }
        return Pair(symbolSprites, attributes.toByteArray())
    }
}