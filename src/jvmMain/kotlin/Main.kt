// Copyright 2000-2021 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.


import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import convert.PlatformSprite
import mainMenu.mainMenu
import mainMenu.xpeccyConnect
import theme.MyTheme

@Composable
fun App() {
    remember { AppData }
    remember { ZxData }
//    PlatformSprite.test()

    MyTheme {

        if (AppData.showCardDialog) {
            newCardDialog(AppData.tmpCard)
        }
        if (ZxData.showConnectDialog) {
            xpeccyConnect()
        }

        Scaffold(topBar = {
            mainMenu()
        }) {
            appContent()
        }

    }
}


fun main() = application {
    // TODO добавить быстрые клавиши

//    probabilityTest()
    cardProbability(1, CARDS_COUNT, 93)

    Window(
        state = WindowState(position = WindowPosition(Alignment.Center)),
//        undecorated = true,
        onCloseRequest = ::exitApplication, title = "Arcomage card converter", icon = painterResource(
            "gfx/061 " + "Dragon_s Eye (diver).png"
                                                                                                     )
          ) {
        App()
    }
}
