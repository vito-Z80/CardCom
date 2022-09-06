// Copyright 2000-2021 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.


import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import mainMenu.mainMenu

@Composable
fun App() {
    remember { AppData }

    MaterialTheme {

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
    Window(onCloseRequest = ::exitApplication, title = "Arcomage card converter") {
        App()
    }
}
