package me.chell.samsara.api.gui

data class Rectangle(var x: Double, var y: Double, var width: Double, var height: Double) {
    val endX
        get() = x + width
    val endY
        get() = y + height

    fun isInBounds(x: Double, y: Double): Boolean {
        return x >= this.x && x <= this.endX && y >= this.y && y <= this.endY
    }

    fun subtract(padding: Border): Rectangle = Rectangle(x + padding.left, y + padding.top, width - padding.right, height - padding.bottom)
}