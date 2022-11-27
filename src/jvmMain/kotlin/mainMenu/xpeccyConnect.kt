package mainMenu

import AppData
import Structure
import ZxData
import addLog
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.WindowState
import byResource
import cardImages
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import name
import theme.MyTheme
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException
import kotlin.concurrent.thread

// TODO пока игра пилица - адрес дампа плавающий.
val request = "dumpraw 73BC 44\r\n".toByteArray()

private val bg = Color.DarkGray

val cor = CoroutineScope(Dispatchers.IO).launch {

    while (true) {
        delay(16)
        thread {
            if (ZxData.socket != null && ZxData.socket?.isClosed == false) {
                try {
                    val o = ZxData.output?.write(request)
                    val r = ZxData.input?.bufferedReader()
                    ZxData.xpeccyData = r?.readLine() ?: ""

                } catch (_: Exception) {
                    println("xpeccy not connect.")
                }
            }
        }
    }
}


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun xpeccyConnect() {
    MyTheme {
        if (ZxData.showConnectDialog) {
            xpeccyWindow()
        }
    }
}


@Composable
private fun xpeccyWindow() {
    val byte = StringBuilder()

    Window(
        state = WindowState(
            position = WindowPosition(Alignment.Center), width = 564f.dp, height = 512f.dp
                           ), title = "Xpeccy connect", onCloseRequest = {
            ZxData.input?.close()
            ZxData.output?.close()
            ZxData.socket?.close()
            ZxData.socket = null
            ZxData.input = null
            ZxData.output = null
            ZxData.showConnectDialog = false
            addLog("Disconnect xpeccy.", Color.Yellow)
        }, resizable = false
          ) {

        Row() {
            if (ZxData.xpeccyData.isNotEmpty()) {
                val player = ZxData.xpeccyData.substring(64, 76)
                val opponent = ZxData.xpeccyData.substring(76, 88)
                Column() {
                    Text(
                        text = "Player cards",
//                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(bottom = 4f.dp)
                        )
                    Row(
                        modifier = Modifier.padding(4f.dp).background(bg),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically
                       ) {
                        cards(byte, player)
                        val playerDataString = ZxData.xpeccyData.substring(0, 32)
                        playerResourceLabels(playerDataString)
                    }
                    Text(
                        text = "Rival cards",
//                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(bottom = 4f.dp)
                        )
                    Row(
                        modifier = Modifier.padding(4f.dp).background(bg),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically
                       ) {
                        cards(byte, opponent)
                        val opponentDataString = ZxData.xpeccyData.substring(32, 64)
                        playerResourceLabels(opponentDataString)
                    }
                }

            }

        }
    }
}

@Composable
private fun cards(byte: StringBuilder, player: String) {

    for (i in player.indices step 2) {
        byte.clear()
        byte.append(player[i])
        byte.append(player[i + 1])
        val id = byte.toString().toInt(16)
        val ccName = Structure.values()
            .firstOrNull { it.name.lowercase() == AppData.cards?.get(id)?.costCurrency?.value?.lowercase() }
        val bgColor = if (ccName != null) {
            Color.White.byResource(ccName)
        } else {
            Color.Red
        }
        Column(
            modifier = Modifier.padding(2f.dp).requiredWidth(70f.dp), horizontalAlignment = Alignment.CenterHorizontally
              ) {

            val cardImage = cardImages[AppData.cards?.get(id)?.imagePath?.value]
            if (cardImage != null) {
                Column(modifier = Modifier.height(32f.dp).background(color = bgColor)) {
                    Text(
                        text = "${AppData.cards?.get(id)?.cardName?.value}",
                        style = MaterialTheme.typography.h4,
                        textAlign = TextAlign.Center,
                        )
                }
                Image(
                    bitmap = cardImage,
                    contentDescription = null,
                    filterQuality = FilterQuality.None,
                    modifier = Modifier.align(Alignment.CenterHorizontally)

                     )
                Column(modifier = Modifier.fillMaxWidth().border(width = 1f.dp, color = Color.Yellow).padding(4f.dp)) {
                    Text(text = "Cost: ${AppData.cards?.get(id)?.cardCost?.value}", style = MaterialTheme.typography.h4)
                    Text(
                        text = "Prob: ${AppData.cards?.get(id)?.probability?.value ?: 1}",
                        style = MaterialTheme.typography.h4
                        )
                }
                Text(
                    text = "${AppData.cards?.get(id)?.cardDescription?.value}",
                    style = MaterialTheme.typography.h4,
                    modifier = Modifier.requiredHeight(72f.dp).background(color = bgColor).padding(2f.dp)
                        .verticalScroll(ScrollState(0))
                    )
            }
        }

    }
}

private val str = StringBuilder()

@Composable
private fun playerResourceLabels(strData: String) {
    Column(
        modifier = Modifier.border(width = 1f.dp, color = Color.LightGray)
            .padding(4f.dp),
        verticalArrangement = Arrangement.SpaceAround,


        ) {
        for (id in strData.indices step 4) {
            str.clear()
            str.append(strData[id + 2])
            str.append(strData[id + 3])
            str.append(strData[id])
            str.append(strData[id + 1])
            val textColor = Color.White.byResource(Structure.values()[id / 4])
            Row() {
                Text(
                    text = Structure.values()[id / 4].name(),
                    Modifier.requiredWidth(80f.dp),
                    style = MaterialTheme.typography.h4,
                    color = textColor
                    )
                Text(
                    text = "${str.toString().toInt(16)}",
                    Modifier.requiredWidth(32f.dp),
                    style = MaterialTheme.typography.h4,
                    color = textColor
                    )
            }
        }
    }
}

//  https://www.sqlitetutorial.net/sqlite-java/
fun main() {
    val url = "jdbc:sqlite:E:\\Kotlin\\compose-jb\\CardCom\\testDB.db"
    val create = "CREATE TABLE IF NOT EXISTS warehouses (id integer PRIMARY KEY, name text NOT NULL, capacity real);"
    val query = "SELECT id, name, capacity FROM warehouses"


    // connect to existing or new db
    var connect: Connection? = null
    try {
        connect = DriverManager.getConnection(url)
        val meta = connect.metaData
        println("Driver name ${meta.driverName}")
    } catch (e: SQLException) {
        println(e.message)
    }

    if (connect != null) {
        val statement = connect.createStatement()
        // create table
        val createTable = statement.execute(create)
        // set to table
        insert(connect, "Raw Material", 3000.0)
        insert(connect, "Semifinished Goods", 4000.0)
        insert(connect, "Finished Goods", 5000.0)

        // get result
        val result = statement.executeQuery(query)
        while (result.next()) {
            println("${result.getInt("id")}\t${result.getString("name")}\t${result.getDouble("capacity")}")
        }
    }

    connect?.close()

}

private fun insert(connection: Connection, name: String, capacity: Double) {
    val ins = "INSERT INTO warehouses(name,capacity) VALUES(?,?)"
    connection.prepareStatement(ins).let {
        it.setString(1, name)
        it.setDouble(2, capacity)
        it.executeUpdate()
    }
}