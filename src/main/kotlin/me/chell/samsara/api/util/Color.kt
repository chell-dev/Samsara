package me.chell.samsara.api.util

data class Color(var red: Int, var green: Int, var blue: Int, var alpha: Int) {

    constructor(argb: Int) : this(0, 0, 0, 0) {
        this.argb = argb
    }

    constructor(rgb: Int, alpha: Int): this(0, 0, 0, 0) {
        this.argb = rgb
        this.alpha = alpha
    }

    var argb: Int
        get() = alpha and 0xFF shl 24 or
                (red and 0xFF shl 16) or
                (green and 0xFF shl 8) or
                (blue and 0xFF)
        set(value) {
            alpha = value shr 24 and 0xFF
            red = argb shr 16 and 0xFF
            green = argb shr 8 and 0xFF
            blue = argb and 0xFF
        }

    override fun toString() = "$argb"
}