package utils

import java.io.File

val homeDirectory: String = System.getProperty("user.home")
val FILEPATH = "${homeDirectory}${File.separator}Documents${File.separator}PresentationClient${File.separator}"
const val FILENAME = "PRQR.png"


const val VERSION_CODE = 0.1
const val SCROLL_SENSITIVITY = 1f
//const val CURSOR_SENSITIVITY = 1
const val DRAG_SENSITIVITY = 1