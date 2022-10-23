package theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Typography
import androidx.compose.material.darkColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontSynthesis
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.platform.Font
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun MyTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
//        colors = if (darkTheme) DarkColors else LightColors,
        /*...*/
        content = content,
        typography = typography
    )
}


private val DarkColors = darkColors(
    primary = Color.Black
)

private val ZX = FontFamily(
    Font("zxspectr.ttf", weight = FontWeight.W100)
)

private val typography = Typography(
    h6 = TextStyle(
        fontFamily = ZX,
        color = Color.White,
        fontSize = 8f.sp,
        background = Color.DarkGray
    ),
    h5 = TextStyle(
        fontFamily = ZX,
        color = Color.Black,
        fontSize = 16f.sp
    ),
    h4 = TextStyle(
        fontFamily = ZX,
        color = Color.White,
        fontSize = 8f.sp
    ),
    h3 = TextStyle(
//        fontFamily = ZX,
        color = Color.White,
        fontSize = 10f.sp
    ),
    h2 = TextStyle(
        fontFamily = ZX,
        color = Color.Black,
        fontSize = 32f.sp
    ),
    h1 = TextStyle(
        fontFamily = ZX,
        color = Color.Black,
        fontSize = 48f.sp
    ),
//    defaultFontFamily = ZX

)