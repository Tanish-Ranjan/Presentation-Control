package data.module

data class CursorPacket(
    val x: Float,
    val y: Float
) {

    override fun toString(): String {
        return "{$x, $y}"
    }

}