@file:OptIn(ExperimentalComposeUiApi::class)

package convert

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import button
import gson.NewCard

@Composable
fun textVisualization(card: NewCard?) {


    if (card == null) return



    if (card.zxCard.value == null) {
        card.zxCard.value = NewCard.ZxCard()
    }


    Column(
        modifier = Modifier
            .padding(2f.dp).fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        button(text = "Copy description", onClick = {
            card.zxCard.value?.description?.value = StringBuilder().append(card.cardDescription.value).toString()
        })

        BasicTextField(
            value = "${card.zxCard.value?.description?.value}",
            onValueChange = {
                card.zxCard.value?.description?.value = it //.replace(Regex("\t"),"____")
            },
            modifier = Modifier
                .padding(4f.dp)
                .border(width = 8f.dp, color = Color.DarkGray)
                .padding(8f.dp)
                .width((16 * 8).dp)
                .height((10 * 8).dp)
                .background(color = Color.LightGray)
                ,
            textStyle = MaterialTheme.typography.h6,
//            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Ascii, autoCorrect = false),
        )
    }

}