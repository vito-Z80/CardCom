// Copyright 2000-2021 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.


import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import mainMenu.mainMenu
import theme.MyTheme

@Composable
fun App() {
    remember { AppData }

    MyTheme {

        if (AppData.showCardDialog) {
            newCardDialog(AppData.tmpCard)
        }

        Scaffold(
            topBar = {
                mainMenu()
            },
            content = {
                appContent()
            },
            bottomBar = { }
        )

    }
}


fun main() = application {
    // TODO добавить быстрые клавиши
    Window(onCloseRequest = ::exitApplication, title = "Arcomage card converter", icon = painterResource("061 Dragon_s Eye (diver).png")) {
        App()
    }
}
