package file

import AppData
import androidx.compose.ui.awt.ComposeWindow
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import gson.*
import org.jetbrains.skia.Image
import org.jetbrains.skia.impl.Log
import javax.swing.JFileChooser
import javax.swing.JOptionPane
import javax.swing.filechooser.FileNameExtensionFilter

//  https://java-online.ru/swing-jfilechooser.xhtml

private val imageFilter = FileNameExtensionFilter("JPG & PNG Images", "jpg", "png")
private val gsonFilter = FileNameExtensionFilter(".GSON files", "gson")
private val chooser = JFileChooser().apply { fileFilter = imageFilter }
private val window = ComposeWindow()

/**
 * получить полный путь к файлу выбранного изображения (без загрузки)
 */
internal fun getImagePath(): String? {
    chooser.fileFilter = imageFilter
    chooser.dialogTitle = "Load image"
    val returnVal = chooser.showOpenDialog(window)
    if (returnVal == JFileChooser.APPROVE_OPTION) {
        Log.info("Get image file path: ${chooser.selectedFile.absoluteFile}")
        return chooser.selectedFile.toString()
    }
    return null
}

/**
 * вызывается только после getImagePath() когда [chooser.selectedFile] готов
 */
internal fun getImageByPath(): ImageBitmap {
    return Image.makeFromEncoded(chooser.selectedFile?.readBytes()).toComposeImageBitmap()
}

internal fun openFile(newCard: NewCard?) {
    chooser.fileFilter = imageFilter
    val returnVal = chooser.showOpenDialog(window)
    if (returnVal == JFileChooser.APPROVE_OPTION) {
        println(
            "You chose to open this file: " +
                    chooser.selectedFile.name
        )
    }
//    return org.jetbrains.skia.Image.makeFromEncoded(chooser.selectedFile?.readBytes()).toComposeImageBitmap()
//    val file = chooser.selectedFile
    if (imageFilter.extensions.any { it == chooser.selectedFile.extension }) {
        val image = org.jetbrains.skia.Image.makeFromEncoded(chooser.selectedFile?.readBytes()).toComposeImageBitmap()
//        newCard?.image = image
    } else {
        Log.error(
            "File ${chooser.selectedFile.name} wrong extension. '${chooser.selectedFile.extension}'\n" +
                    "Only JPG & PNG Images"
        )
    }
}

/**
 * сохраение созданных карт на диск для последующей работы с ними
 */
fun saveTemplate() {
    val result = gson.toJson(prepareForSerialize())
    println("GSON:\n$result")
    chooser.fileSelectionMode = JFileChooser.FILES_ONLY
    chooser.fileFilter = gsonFilter
    chooser.dialogTitle = "Save config"
    val returnVal = chooser.showSaveDialog(window)
    if (returnVal == JFileChooser.APPROVE_OPTION) {
        val absoluteFile = chooser.selectedFile.absoluteFile
        println("$absoluteFile.${gsonFilter.extensions[0]}")
        try {
//            chooser.selectedFile.name.plus(".${gsonFilter.extensions[0]}")
            chooser.selectedFile.writeText(result, Charsets.UTF_8)
            JOptionPane.showMessageDialog(window, "File ${chooser.selectedFile} be saved.")
        } catch (e: Exception) {
            JOptionPane.showMessageDialog(window, "File ${chooser.selectedFile} can`t be saved. пачимута...")
        }
    }
}

fun loadTemplate() {

    chooser.fileSelectionMode = JFileChooser.FILES_ONLY
    chooser.fileFilter = gsonFilter
    chooser.dialogTitle = "Load config"
    val returnVal = chooser.showOpenDialog(window)
    if (returnVal == JFileChooser.APPROVE_OPTION) {
        try {
            val jsonText = chooser.selectedFile.readText(Charsets.UTF_8)
            println(jsonText)
            JOptionPane.showMessageDialog(window, "File ${chooser.selectedFile} loaded.")
            val ser = gson.fromJson(jsonText, DesCards::class.java)
            println(ser)
            if (ser != null) prepareForDeserialize(ser)
            println(AppData.cards.toString())

        } catch (e: Exception) {
            JOptionPane.showMessageDialog(window, "File ${chooser.selectedFile} can`t be loading.\nпачимута...")
        }
    }
}