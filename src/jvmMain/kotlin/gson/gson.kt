package gson

import AppData
import Message
import Player
import Variant
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.toComposeImageBitmap
import cardImages
import com.google.gson.Gson
import com.google.gson.InstanceCreator
import name
import org.jetbrains.skia.Image
import org.jetbrains.skia.impl.Log
import java.io.File
import java.lang.reflect.Type


val gson = Gson()

/**
 * подготовка карт для сиарилизации
 */
fun prepareForSerialize(): DesCards {
    val n = DesCards()
    AppData.cards?.forEach {
        it?.let { card ->
            n.add(cardToSerialize(card))
        }
    }
    return n
}

fun prepareForDeserialize(serCards: DesCards) {
    val n: ArrayList<NewCard?> = ArrayList<NewCard?>()
    cardImages.clear()
    serCards.forEach {
        println(it.imagePath)
        n.add(cardToDeserialize(it))
        try {
            cardImages["${it.imagePath}"] = Image.makeFromEncoded(
                File("${it.imagePath}").readBytes()
                                                                 ).toComposeImageBitmap()
        } catch (e: Exception) {
            Unit
        }
    }
    AppData.cards = n.toList()
}

/**
 * объект сериализованных карт
 */
class DesCards : ArrayList<SerializedCard>()

/**
 * копирование карты приложения в объект для сериализации
 */
private fun cardToSerialize(card: NewCard): SerializedCard {
    val serializedCard = SerializedCard()
    serializedCard.cardCost = card.cardCost.value
    serializedCard.costCurrency = card.costCurrency.value
    serializedCard.probability = card.probability.value
    serializedCard.cardDescription = card.cardDescription.value
    serializedCard.cardName = card.cardName.value
    serializedCard.imagePath = card.imagePath.value

    if (serializedCard.zxCard == null) serializedCard.zxCard = SerializedCard.SerializedZxCard()
    serializedCard.zxCard?.name = card.zxCard.value?.name?.value
    serializedCard.zxCard?.description = card.zxCard.value?.description?.value
    serializedCard.zxCard?.sprite = card.zxCard.value?.sprite?.value
    serializedCard.zxCard?.attributes = card.zxCard.value?.attributes?.value
    serializedCard.zxCard?.spriteBlock8 = card.zxCard.value?.spriteBlock8?.value


    if (!card.effects.value.isNullOrEmpty()) {
        serializedCard.effects = List(card.effects.value?.size ?: 0) {
            val e = SerializedCard.Effect()
            e.player = card.effects.value?.get(it)?.player?.value
            e.value = card.effects.value?.get(it)?.value?.value
            e.structure = card.effects.value?.get(it)?.structure?.value
            e.variant = card.effects.value?.get(it)?.variant?.value
            e
        }
    }
    if (card.specials.value != null) {
        serializedCard.specials = SerializedCard.Special().apply {
            cantDiscard = card.specials.value?.cantDiscard?.value
            playAgain = card.specials.value?.playAgain?.value
            pdp = card.specials.value?.pdp?.value
        }
    }
    if (card.condition.value != null) {
        serializedCard.condition = SerializedCard.Condition().apply {
            if (!card.condition.value?.leftPart?.value.isNullOrEmpty()) {
                leftPart = List(card.condition.value?.leftPart?.value?.size ?: 0) {
                    val e = SerializedCard.Effect()
                    try {
                        e.player = card.condition.value?.leftPart?.value?.get(it)?.player?.value
                        e.value = card.condition.value?.leftPart?.value?.get(it)?.value?.value
                        e.structure = card.condition.value?.leftPart?.value?.get(it)?.structure?.value
                        e.variant = card.condition.value?.leftPart?.value?.get(it)?.variant?.value
                    } catch (iobe: IndexOutOfBoundsException) {
                        Log.info("Left side of condition is empty.")
                    }
                    e
                }
            }
            if (!card.condition.value?.rightPart?.value.isNullOrEmpty()) {
                rightPart = List(card.condition.value?.rightPart?.value?.size ?: 0) {
                    val e = SerializedCard.Effect()
                    try {
                        e.player = card.condition.value?.rightPart?.value?.get(it)?.player?.value
                        e.value = card.condition.value?.rightPart?.value?.get(it)?.value?.value
                        e.structure = card.condition.value?.rightPart?.value?.get(it)?.structure?.value
                        e.variant = card.condition.value?.rightPart?.value?.get(it)?.variant?.value
                    } catch (iobe: IndexOutOfBoundsException) {
                        Log.info("Right side of condition is empty.")
                    }
                    e
                }
            }
            sign = card.condition.value?.sign?.value
            if (!card.condition.value?.conditionTrue?.value.isNullOrEmpty()) {
                conditionTrue = List(card.condition.value?.conditionTrue?.value?.size ?: 0) {
                    val e = SerializedCard.Effect()
                    try {
                        e.player = card.condition.value?.conditionTrue?.value?.get(it)?.player?.value
                        e.value = card.condition.value?.conditionTrue?.value?.get(it)?.value?.value
                        e.structure = card.condition.value?.conditionTrue?.value?.get(it)?.structure?.value
                        e.variant = card.condition.value?.conditionTrue?.value?.get(it)?.variant?.value
                    } catch (iobe: IndexOutOfBoundsException) {
                        Log.info("True result of conditions is absent.")
                    }
                    e
                }
            }
            if (!card.condition.value?.conditionFalse?.value.isNullOrEmpty()) {
                conditionFalse = List(card.condition.value?.conditionFalse?.value?.size ?: 0) {
                    val e = SerializedCard.Effect()
                    try {
                        e.player = card.condition.value?.conditionFalse?.value?.get(it)?.player?.value
                        e.value = card.condition.value?.conditionFalse?.value?.get(it)?.value?.value
                        e.structure = card.condition.value?.conditionFalse?.value?.get(it)?.structure?.value
                        e.variant = card.condition.value?.conditionFalse?.value?.get(it)?.variant?.value
                    } catch (iobe: IndexOutOfBoundsException) {
                        Log.info("False result of conditions is absent.")
                    }
                    e
                }
            }
        }
    }
    return serializedCard
}

/**
 * копирование карты json в карту приложения
 */
private fun cardToDeserialize(serializedCard: SerializedCard): NewCard {
    val newCards = NewCard()
    newCards.cardCost.value = serializedCard.cardCost
    newCards.cardDescription.value = serializedCard.cardDescription
    newCards.cardName.value = serializedCard.cardName
    newCards.costCurrency.value = serializedCard.costCurrency
    newCards.probability.value = serializedCard.probability
    newCards.imagePath.value = serializedCard.imagePath

    if (newCards.zxCard.value == null) newCards.zxCard = mutableStateOf(NewCard.ZxCard())
    newCards.zxCard.value?.name?.value = serializedCard.zxCard?.name
    newCards.zxCard.value?.description?.value = serializedCard.zxCard?.description
    newCards.zxCard.value?.sprite?.value = serializedCard.zxCard?.sprite
    newCards.zxCard.value?.attributes?.value = serializedCard.zxCard?.attributes
    newCards.zxCard.value?.spriteBlock8?.value = serializedCard.zxCard?.spriteBlock8


    if (!serializedCard.effects.isNullOrEmpty()) {
        newCards.effects.value = List(serializedCard.effects?.size ?: 0) {
            val e = NewCard.Effect()
            e.player.value = serializedCard.effects?.get(it)?.player
            e.value.value = serializedCard.effects?.get(it)?.value
            e.structure.value = serializedCard.effects?.get(it)?.structure
            e.variant.value = serializedCard.effects?.get(it)?.variant
            e
        }
    }
    if (serializedCard.specials != null) {
        newCards.specials.value = NewCard.Special().apply {
            cantDiscard.value = serializedCard.specials?.cantDiscard
            playAgain.value = serializedCard.specials?.playAgain
            pdp.value = serializedCard.specials?.pdp
        }
    }
    if (serializedCard.condition != null) {
        newCards.condition.value = NewCard.Condition().apply {
            if (!serializedCard.condition?.leftPart.isNullOrEmpty()) {
                leftPart.value = List(serializedCard.condition?.leftPart?.size ?: 0) {
                    val e = NewCard.Effect()
                    e.player.value = serializedCard.condition?.leftPart?.get(it)?.player
                    e.value.value = serializedCard.condition?.leftPart?.get(it)?.value
                    e.structure.value = serializedCard.condition?.leftPart?.get(it)?.structure
                    e.variant.value = serializedCard.condition?.leftPart?.get(it)?.variant
                    e
                }
            }
            if (!serializedCard.condition?.rightPart.isNullOrEmpty()) {
                rightPart.value = List(serializedCard.condition?.rightPart?.size ?: 0) {
                    val e = NewCard.Effect()
                    e.player.value = serializedCard.condition?.rightPart?.get(it)?.player
                    e.value.value = serializedCard.condition?.rightPart?.get(it)?.value
                    e.structure.value = serializedCard.condition?.rightPart?.get(it)?.structure
                    e.variant.value = serializedCard.condition?.rightPart?.get(it)?.variant
                    e
                }
            }
            sign.value = serializedCard.condition?.sign
            if (!serializedCard.condition?.conditionTrue.isNullOrEmpty()) {
                conditionTrue.value = List(serializedCard.condition?.conditionTrue?.size ?: 0) {
                    val e = NewCard.Effect()
                    e.player.value = serializedCard.condition?.conditionTrue?.get(it)?.player
                    e.value.value = serializedCard.condition?.conditionTrue?.get(it)?.value
                    e.structure.value = serializedCard.condition?.conditionTrue?.get(it)?.structure
                    e.variant.value = serializedCard.condition?.conditionTrue?.get(it)?.variant
                    e
                }
            }
            if (!serializedCard.condition?.conditionFalse.isNullOrEmpty()) {
                conditionFalse.value = List(serializedCard.condition?.conditionFalse?.size ?: 0) {
                    val e = NewCard.Effect()
                    e.player.value = serializedCard.condition?.conditionFalse?.get(it)?.player
                    e.value.value = serializedCard.condition?.conditionFalse?.get(it)?.value
                    e.structure.value = serializedCard.condition?.conditionFalse?.get(it)?.structure
                    e.variant.value = serializedCard.condition?.conditionFalse?.get(it)?.variant
                    e
                }
            }
        }
    }
    return newCards
}


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/**
 * карта приложения
 */
class NewCard {

    var imagePath: MutableState<String?> = mutableStateOf(null)

    var cardName: MutableState<String?> = mutableStateOf(null)
    var cardDescription: MutableState<String?> = mutableStateOf(null)
    var cardCost: MutableState<String?> = mutableStateOf(null)
    var costCurrency: MutableState<String?> = mutableStateOf(null)
    var probability: MutableState<String?> = mutableStateOf(null)

    var specials: MutableState<Special?> = mutableStateOf(null)

    var effects: MutableState<List<Effect?>?> = mutableStateOf(null)
    var condition: MutableState<Condition?> = mutableStateOf(null)

    var zxCard: MutableState<ZxCard?> = mutableStateOf(null)

    class Special {
        var playAgain: MutableState<Boolean?> = mutableStateOf(null)
        var pdp: MutableState<Boolean?> = mutableStateOf(null)
        var cantDiscard: MutableState<Boolean?> = mutableStateOf(null)
    }

    class Condition {
        var leftPart: MutableState<List<Effect?>?> = mutableStateOf(null)
        var rightPart: MutableState<List<Effect?>?> = mutableStateOf(null)
        var sign: MutableState<String?> = mutableStateOf(null)
        var conditionTrue: MutableState<List<Effect?>?> = mutableStateOf(null)
        var conditionFalse: MutableState<List<Effect?>?> = mutableStateOf(null)
    }

    class Effect {
        var player: MutableState<String?> = mutableStateOf(null)
        var structure: MutableState<String?> = mutableStateOf(null)
        var value: MutableState<String?> = mutableStateOf(null)
        var variant: MutableState<Variant?> = mutableStateOf(null)

        fun clear() {
            player.value = Player.PLAYER.name()
            structure.value = Message.STRUCTURE
            value.value = Message.NAN
        }
    }

    class ZxCard {
        var name: MutableState<String?> = mutableStateOf(null)
        var description: MutableState<String?> = mutableStateOf(null)
        var sprite: MutableState<ByteArray?> = mutableStateOf(null)
        var spriteBlock8: MutableState<List<ByteArray?>?> = mutableStateOf(null)
        var attributes: MutableState<ByteArray?> = mutableStateOf(null)
    }
}

/**
 * сериализованная карта
 */
class SerializedCard {
    var imagePath: String? = null

    var cardName: String? = null
    var cardDescription: String? = null
    var cardCost: String? = null
    var costCurrency: String? = null
    var probability: String? = null

    var specials: Special? = null

    var effects: List<Effect?>? = null
    var condition: Condition? = null

    var zxCard: SerializedZxCard? = null

    class Special {
        var playAgain: Boolean? = null
        var pdp: Boolean? = null
        var cantDiscard: Boolean? = null
    }

    class Condition {
        var leftPart: List<Effect?>? = null
        var rightPart: List<Effect?>? = null
        var sign: String? = null
        var conditionTrue: List<Effect?>? = null
        var conditionFalse: List<Effect?>? = null
    }

    class Effect {
        var player: String? = null
        var structure: String? = null
        var value: String? = null
        var variant: Variant? = null
    }

    class SerializedZxCard {
        var name: String? = null
        var description: String? = null
        var sprite: ByteArray? = null
        var spriteBlock8: List<ByteArray?>? = null
        var attributes: ByteArray? = null
    }
}


//  https://hanru-yeh.medium.com/deserialize-classes-with-fields-by-delegate-crashes-when-using-gson-7c31fb5a792d
fun main() {
//    gson.newBuilder().instanceOf(Any::class)
    val test = Example()
//    val a = test.instanceOf(Any::class)

//    test.name = "Hello"

    val r = gson.toJson(test)
    println(r.format())
    val t = gson.fromJson(r, Example::class.java)
    println(t.name.value)
    test.name.value = "hello"
    println(t.name.value)

}

fun Gson.mm() {

}

data class Example(
     var name:MutableState<String> = mutableStateOf("First")
                  ) {
//    @Transient
//    var name:MutableState<String> = mutableStateOf("First")

//    operator fun getValue(thisRef: Any?, property: KProperty<*>) {
//
//    }
}

class TT<T : Any> : InstanceCreator<MutableState<T>> {
//    operator fun getValue(thisRef: Any?, property: KProperty<*>): String {
//        return "$thisRef"
//    }
//    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: String) {
//
//    }

   var value: T
        get() = TODO("Not yet implemented")
        set(value) {}

     fun component1(): T {
        TODO("Not yet implemented")
    }

     fun component2(): (T) -> Unit {
        TODO("Not yet implemented")
    }

    override fun createInstance(type: Type?): MutableState<T> {
        TODO("Not yet implemented")
    }


}
