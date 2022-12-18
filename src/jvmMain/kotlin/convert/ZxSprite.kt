package convert

import androidx.compose.ui.text.toLowerCase
import java.io.File
import java.security.MessageDigest
import java.util.Base64
import java.util.zip.CRC32
import kotlin.experimental.and
import kotlin.experimental.inv
import kotlin.random.Random

object ZxSprite {

    const val SPRITE = "s"
    const val MASK = "m"
    const val ATTRIBUTES = "a"
    const val COMMA = ","

    val tokenSpriteMaskAttributesLine = listOf("s0,s1,s2,s3", "m0,m1,m2,m3", "a0,a1,a2,a3")

    val tokenSpriteLineAttributes = listOf(
        "s0,s1",
        "s2,s3",
        "s4,s5",
        "s6,s7",
        "s8,s9",
        "s10,s11",
        "s12,s13",
        "s14,s15",
        "s16,s17",
        "s18,s19",
        "s20,s21",
        "s22,s23",
        "s24,s25",
        "s26,s27",
        "s28,s29",
        "s30,s31",
        "a32,a33,a34,a35"
                                          )
    val tokenSpriteMaskAttributesZigZag = listOf("s0,m0,s1,m1,s2,m2,s3,m3,s7,m7,s6,m6,s5,m5,s4,m4", "a0,a1,a2,a3")
    val tokenSpriteMaskAttributesZigZagLine = listOf("s0,m0,s1,m1,s2,m2,s3,m3,s4,m4,s5,m5,s6,m6,s7,m7", "a0,a1,a2,a3")
    val tokenSpriteAttributesSymbol = listOf("s0,s4,s8,s12,s16,s20,s24,s28", "a0")

    val tokenSpriteSymbolAttributes = listOf(
        "s0,s2,s4,s6,s8,s10,s12,s14",
        "s1,s3,s5,s7,s9,s11,s13,s15",
        "s16,s18,s20,s22,s24,s26,s28,s30",
        "s17,s19,s21,s23,s25,s27,s29,s31",
        "a32,a33,a34,a35"
                                            )
    private val sprite = ArrayList<Byte>()


    fun convert(sprite: ByteArray?, mask: ByteArray?, attributes: ByteArray?, tokens: List<String>) {
        val tSize = tokens.sumOf { it.split(COMMA).size }

        val s = tokens.sumOf { it.split(COMMA).filter { b -> b.contains(SPRITE) }.size } == sprite?.size
        val m = tokens.sumOf { it.split(COMMA).filter { b -> b.contains(MASK) }.size } == mask?.size
        val a = tokens.sumOf { it.split(COMMA).filter { b -> b.contains(ATTRIBUTES) }.size } == attributes?.size

        if (s) {
            println("Sprite: ${sprite?.size} bytes")
        } else {
            println("Спрайт отсутствует или не указан в токенах.")
        }

        if (m) {
            println("Mask: ${mask?.size} bytes")
        } else {
            println("Маска отсутствует или не указана в токенах.")
        }

        if (a) {
            println("Attributes: ${attributes?.size} bytes")
        } else {
            println("Атрибуты отсутствует или не указаны в токенах.")
        }




    }

    fun convert(bytes: ByteArray, tokens: List<String>): ByteArray {


        if (tokens.sumOf { it.split(COMMA).size } != bytes.size) {
            // TODO ERROR
        }

        sprite.clear()

        tokens.forEach { line ->
            line.split(COMMA).forEach { byte ->

                if (byte.contains(SPRITE, true)) {
                    val id = byte.replace(SPRITE, "").trimIndent().toInt()
                    sprite.add(bytes[id])
                }
                if (byte.contains(MASK, true)) {
                    val id = byte.replace(MASK, "").trimIndent().toInt()
                    sprite.add(bytes[id].inv() and (255).toByte())
                }
                if (byte.contains(ATTRIBUTES, true)) {
                    val id = byte.replace(ATTRIBUTES, "").trimIndent().toInt()
                    sprite.add(bytes[id])
                }

            }
        }
        return sprite.toByteArray()
    }
}


fun main() {
    val a = (0b11110000).toUByte()
    println(a)
    println(a.inv() and (255).toUByte())

    val file = File("061 Dragon_s Eye (diver).png")
    val md = MessageDigest.getInstance("SHA-256")
    val hash = md.digest(file.readBytes())
    val key = Base64.getEncoder().encodeToString(hash)
    println(key)



    println()
//    println(hash.joinToString())

    val spr = Random(Random.nextInt()).nextBytes(32)
    val attr = Random(Random.nextInt()).nextBytes(4)
    val sprite = ZxSprite.convert(sprite = spr, attributes = attr, mask = null, tokens = ZxSprite
        .tokenSpriteSymbolAttributes)
//    println(sprite.joinToString())
}