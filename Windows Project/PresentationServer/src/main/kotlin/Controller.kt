import data.module.CursorPacket
import utils.DRAG_SENSITIVITY
import utils.SCROLL_SENSITIVITY
import java.awt.MouseInfo
import java.awt.Robot
import java.awt.event.InputEvent

class Controller {

    companion object {

        private val cursorBot = Robot()
        private val scrollBot = Robot()
        private val leftBot = Robot()
        private val rightBot = Robot()

        private var isLeft = false
        private var isRight = false

        /*var screenHeight: Int
        var screenWidth: Int*/

        /*private var center = Coordinate(0.0, 0.0)
        private var xRate = 0.0
        private var yRate = 0.0

        private var lastCoord = Coordinate(0.0, 0.0)*/

        /*init {
            val gd = GraphicsEnvironment.getLocalGraphicsEnvironment().defaultScreenDevice
            screenHeight = gd.displayMode.height
            screenWidth = gd.displayMode.width
        }*/

        /*fun setCenter(coord: Coordinate) {
            center = coord
        }

        fun setRateX(deltaX: Double) {
            xRate = (screenWidth / deltaX) * CURSOR_SENSITIVITY
        }

        fun setRateY(deltaY: Double) {
            yRate = (screenHeight / deltaY) * CURSOR_SENSITIVITY
        }*/

        /*fun cursorTo(coord: Coordinate) {

            val newCoord = center.minus(coord) // Angles along x and y
            val currentCoord = lastCoord.minus(newCoord)

            val x = currentCoord.X * xRate
            val y = currentCoord.Y * yRate

            val pointerPosition = MouseInfo.getPointerInfo().location
            cursorBot.mouseMove(
                (pointerPosition.x + x).toInt(),
                (pointerPosition.y + y).toInt()
            )
            println("Move To: ${pointerPosition.x + x} | ${pointerPosition.y + y}")
            lastCoord = newCoord
        }*/

        fun driftTo(coord: CursorPacket) {
            val pointerPosition = MouseInfo.getPointerInfo().location
            cursorBot.mouseMove(
                (pointerPosition.x + coord.x).toInt() * DRAG_SENSITIVITY,
                (pointerPosition.y + coord.y).toInt() * DRAG_SENSITIVITY
            )
        }

        fun scroll(amount: Int) {
            scrollBot.mouseWheel((amount * SCROLL_SENSITIVITY).toInt())
        }

        fun mouseLeft(press: Boolean) {

            if (press) {
                if (!isLeft)
                    leftBot.mousePress(InputEvent.BUTTON1_DOWN_MASK)
            } else {
                if (isLeft)
                    leftBot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK)
            }

            isLeft = press

        }

        fun mouseRight(press: Boolean) {

            if (press) {
                if (!isRight)
                    rightBot.mousePress(InputEvent.BUTTON3_DOWN_MASK)
            } else {
                if (isRight)
                    rightBot.mouseRelease(InputEvent.BUTTON3_DOWN_MASK)
            }

            isRight = press

        }

        fun reset() {

            if (isLeft)
                leftBot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK)

            if (isRight)
                rightBot.mouseRelease(InputEvent.BUTTON3_DOWN_MASK)

        }

    }

}