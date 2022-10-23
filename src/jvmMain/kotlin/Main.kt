// Copyright 2000-2021 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.


import androidx.compose.foundation.layout.*
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.*
import mainMenu.mainMenu
import theme.MyTheme

@Composable
fun App() {
    remember { AppData }

    MyTheme {

        if (AppData.showCardDialog) {
            newCardDialog(AppData.tmpCard)
        }

        Scaffold(topBar = {
            mainMenu()
        }){
            appContent()
        }

    }
}


fun main() = application {
    // TODO добавить быстрые клавиши

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
