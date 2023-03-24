import data.helper.*
import data.module.CursorPacket
import data.module.ItemType
import data.network.Network
import utils.FILENAME
import utils.FILEPATH

fun main() {

    println("Starting server...")
    try {
        QRHandler.generateQR(Network.getIP(), FILENAME, FILEPATH)
    } catch (_: SecurityException) {
        println("Denied Access to Documents Folder")
        println("This permission is needed to generate QR Code. Please grant the appropriate permissions to use the client.")
        println("\n\nPress any key to exit.")
        readLine()
        return
    } catch (_: IllegalStateException) {
        println("You are not connected to a network")
        println("We need your device and client to be on the same network to establish the connection.")
        println("\n\nPress any key to exit.")
        readLine()
        return
    }
    Window.createWindow()
    QRHandler.destroyQR(FILENAME, FILEPATH)
    if (Network.start()) {
        startHandling()
    }

}

fun startHandling() {

    while (Network.connected) {

        for (msg in Network.receive()) {

            if (msg.isBlank()) continue

            val item = resolver(msg)

            when (item.key) {

                /*ItemType.CURSOR.type -> {
                    val cur = item.value.split(",")
                    Controller.cursorTo(Coordinate(cur[0].toDouble(), cur[1].toDouble()))
                }*/

                ItemType.DRAG.type -> {
                    val cur = item.value.split(",")
                    Controller.driftTo(CursorPacket(cur[0].toFloat(), cur[1].toFloat()))
                }

                ItemType.SCROLL.type -> {
                    val deltaScroll = item.value.toInt()
                    when {
                        deltaScroll.isPositive() -> {
                            Controller.scroll(5)
                        }

                        deltaScroll.isNegative() -> {
                            Controller.scroll(-5)
                        }
                    }
                }

                ItemType.LEFT_BUTTON.type -> {
                    Controller.mouseLeft(item.value.toBoolean())
                }

                ItemType.RIGHT_BUTTON.type -> {
                    Controller.mouseRight(item.value.toBoolean())
                }

            }

        }

    }

}