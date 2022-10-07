package convert

import AppData
import androidx.compose.ui.graphics.PixelMap
import androidx.compose.ui.graphics.toPixelMap
import cardImages
import toAsmLabel
import java.util.*
import kotlin.collections.ArrayList

object PlatformSprite {

    const val SYMBOL_SIZE = 8
    const val DB = "\n\tdb "
    const val NEW_LINE = "\n"
    const val COMMA = ", "


    val spriteMap = StringBuilder()
    val spriteData = StringBuilder()
    val asmText = StringBuilder()

    // binary for sprites only
    val binaryData = ArrayList<Byte>()
    val binaryMap = ArrayList<Byte>()
    var binaryMapAddress = 0

    fun convert(moduleName: String? = null): String {
        spriteMap.clear()
        spriteData.clear()
        asmText.clear()
        binaryData.clear()
        binaryMap.clear()
        // размер binaryMap = кол-во карт * 2 (2 байта на адрес)
        // начало binaryData = С000 + binaryMap.size
        binaryMapAddress = (AppData.cards?.size ?: 0) * 2 + 0xC000
        AppData.cards?.forEachIndexed { id, card ->
            binaryMap.add(((binaryMapAddress shr 8) and 0xff).toByte())
            binaryMap.add((binaryMapAddress and 0xff).toByte())


            val pixelMap = cardImages[card?.imagePath?.value]?.toPixelMap()
            val spriteName = card?.cardName?.value?.toAsmLabel()
            val data = asSpriteSymbols(pixelMap)
            val d2 = asSprite(pixelMap)

            val code = spriteToAsm(
                spriteName = spriteName,
                attributes = data?.second,
//                spriteSymbols = data?.first,
                sprite = d2?.first
            )
            spriteData.append(code)
            spriteMap.append("\n\tdw $spriteName")

            // binary
            data?.first?.forEach {
                binaryData.addAll(it.toTypedArray())
            }
            let { data?.second }?.also {
                binaryData.addAll(it.toTypedArray())
            }
            binaryMapAddress += binaryData.size

        }

        if (moduleName != null) {
            asmText.append("\tmodule $moduleName\n")
            asmText.append("map:$spriteMap\n\nsprites:\n$spriteData")
            asmText.append("\n\tendmodule")
        } else {
            asmText.append("map:$spriteMap\n\nsprites:\n$spriteData")
        }
        return asmText.toString()
    }


    fun spriteToAsm(
        attributes: ByteArray?,
        spriteName: String?,
        sprite: ByteArray? = null,
        spriteSymbols: ArrayList<ByteArray>? = null,
    ): String {
        val spriteData = StringBuilder()
        if (sprite != null) {
            spriteData.append("\n$spriteName:")
            for (i in sprite.indices step SYMBOL_SIZE) {
                dbLine8(sprite.copyOfRange(i, i + SYMBOL_SIZE), spriteData)
            }
            spriteData.append("\n; attributes")
            for (i in 0 until(attributes?.size ?: 0) step SYMBOL_SIZE) {
                dbLine8(attributes?.copyOfRange(i, i + SYMBOL_SIZE), spriteData)
            }

        } else if (spriteSymbols != null) {
            spriteData.append("\n$spriteName:")
            spriteSymbols.forEach {
                dbLine8(it, spriteData)
            }
            spriteData.append("\n; attributes")
            for (i in 0 until (attributes?.size ?: 0) step SYMBOL_SIZE) {
                dbLine8(attributes?.copyOfRange(i, i + SYMBOL_SIZE), spriteData)
            }

        }
        return spriteData.toString()
    }

    private fun dbLine8(src: ByteArray?, dst: StringBuilder) {
        dst.append(DB)
        repeat(7) {
            dst.append("#" + String.format("%02X", src?.get(it)) + COMMA)
        }
        dst.append("#" + String.format("%02X", src?.last()))
    }

    // linear sprite >> all attributes
    fun asSprite(pm: PixelMap?): Pair<ByteArray, ByteArray>? {
        if (pm == null) return null
        val symbols = asSpriteSymbols(pm)!!
        val sprite = ArrayList<Byte>()
        val width = pm.width / SYMBOL_SIZE
        val height = pm.height / SYMBOL_SIZE


        val range = 0 until (width * height)
        for (i in range step height) {
            repeat(SYMBOL_SIZE) {
                repeat(width) { x ->
                    val byte = symbols.first[i + x][it]
                    sprite.add(byte)
                }
            }

        }
        return Pair(sprite.toByteArray(), symbols.second)
    }

    // sprite as symbols 8x8 (1x8) >> all attributes
    fun asSpriteSymbols(pm: PixelMap?): Pair<ArrayList<ByteArray>, ByteArray>? {
        if (pm == null) return null

        val symbolsX = pm.width / SYMBOL_SIZE
        val symbolsY = pm.height / SYMBOL_SIZE

        // атрибуты спрайта 1 байт на спрайт 8х8 (1х8)
        val attributes = ArrayList<Byte>()
        // спрайты 8х8 (1х8) по 8 байт каждый
        val symbolSprites = ArrayList<ByteArray>()

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
                // уникальные цвета
                val uniqueColors = TreeSet(pixelsColor).sortedBy { c -> pixelsColor.count { it == c } }.reversed()
                // спрайт 8х8 пикселей (1х8) 8 байт
                val sprite8b = ArrayList<Byte>()
                repeat(SYMBOL_SIZE) { inY ->
                    var byte = 0
                    var bit = 0x80
                    repeat(SYMBOL_SIZE) { inX ->
                        // биты ставяться только по первому уникальному цвету (ink), второй уникальный цвет идет как paper
                        // оставшиеся цвета игнорятся
                        // TODO сделать что бы биты ставились по уникальному цвету которого меньше всего в символе
                        //  а лишние цвета приравнивать к ближайшему уникальному цвету
                        val pix = pm[x + inX, y + inY].value
                        if (uniqueColors[1] == pix) {
                            byte = byte or bit
                        }
                        bit = bit shr 1
                    }
                    sprite8b.add(byte.toByte())
                }
                symbolSprites.add(sprite8b.toByteArray())
            }
        }
        return Pair(symbolSprites, attributes.toByteArray())
    }
}