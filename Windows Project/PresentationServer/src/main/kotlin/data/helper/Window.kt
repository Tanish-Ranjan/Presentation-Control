package data.helper

import utils.FILENAME
import utils.FILEPATH
import data.network.Network
import java.awt.*
import javax.swing.*

class Window {

    companion object {

        private var window = JFrame()
//        private var calibrationWindow = JFrame()

        fun createWindow() {

            window.dispose()

            window.title = "Presentation Handler"

            val icon =
                ImageIcon(QRHandler.getQRCode(FILENAME, FILEPATH).getScaledInstance(250, 250, Image.SCALE_SMOOTH))
            val img = JLabel(icon, SwingConstants.CENTER)

            window.contentPane.add(img, BorderLayout.NORTH)
            window.contentPane.add(JLabel(Network.getIP(), SwingConstants.CENTER), BorderLayout.SOUTH)

            window.defaultCloseOperation = WindowConstants.EXIT_ON_CLOSE
            window.minimumSize = Dimension(280, 310)
            window.isResizable = false
            window.setLocationRelativeTo(null)
            window.isVisible = true

        }

        fun minimizeWindow() {
            window.state = Frame.ICONIFIED
        }

        /*fun createCalibrationWindow() {

            calibrationWindow.dispose()

            calibrationWindow.title = "Calibrate | Presentation Handler"
            calibrationWindow.extendedState = JFrame.MAXIMIZED_BOTH
            calibrationWindow.setLocation(0, 0)
            calibrationWindow.minimumSize = Dimension(Controller.screenWidth, Controller.screenHeight)
            calibrationWindow.isResizable = false
            calibrationWindow.isUndecorated = true
            calibrationWindow.addFocusListener(object : FocusListener {
                override fun focusGained(e: FocusEvent?) {
                    calibrationWindow.extendedState = JFrame.MAXIMIZED_BOTH
                    calibrationWindow.minimumSize = Dimension(Controller.screenWidth, Controller.screenHeight)
                }

                override fun focusLost(e: FocusEvent?) {}
            })

        }

        fun calibrateWindowAt(
            pos: WindowPosition
        ) {

            calibrationWindow.contentPane.removeAll()
            calibrationWindow.contentPane.add(Surface(pos))
            calibrationWindow.isVisible = true

        }

        fun closeCalibrationWindow() {
            calibrationWindow.dispatchEvent(WindowEvent(calibrationWindow, WindowEvent.WINDOW_CLOSING))
        }*/

    }

}

/*
class Surface(private val pos: WindowPosition) : JPanel() {

    private fun doDrawing(g: Graphics) {

        val g2d = g as Graphics2D
        g2d.paint = Color.RED

        val rh = RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
        rh[RenderingHints.KEY_RENDERING] = RenderingHints.VALUE_RENDER_QUALITY
        g2d.setRenderingHints(rh)

        when (pos) {
            WindowPosition.LEFT -> {
                g2d.fill(Ellipse2D.Double(5.0, (height / 2 - 10).toDouble(), 20.0, 20.0))
            }

            WindowPosition.TOP -> {
                g2d.fill(Ellipse2D.Double((width / 2 - 10).toDouble(), 5.0, 20.0, 20.0))
            }

            WindowPosition.CENTER -> {
                g2d.fill(Ellipse2D.Double((width / 2 - 10).toDouble(), (height / 2 - 10).toDouble(), 20.0, 20.0))
            }
        }

    }

    override fun paintComponent(g: Graphics) {
        super.paintComponent(g)
        doDrawing(g)
    }

}*/