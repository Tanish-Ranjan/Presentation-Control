package data.helper

import io.github.g0dkar.qrcode.QRCode
import java.awt.Image
import java.io.File
import java.io.FileOutputStream
import javax.swing.ImageIcon

class QRHandler {

    companion object {

        fun generateQR(data: String, filename: String, destination: String) {
            File(destination).mkdirs()
            FileOutputStream(destination + filename).use {
                QRCode(data).render(
                    margin = 15,
                    cellSize = 20
                ).writeImage(it)
            }
        }

        fun getQRCode(filename: String, destination: String): Image {
            return ImageIcon(destination + filename).image
        }

        fun destroyQR(filename: String, destination: String) {
            File(destination + filename).delete()
        }

    }

}