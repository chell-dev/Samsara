package me.chell.samsara.api.gui

data class Border(var left: Double, var right: Double, var top: Double, var bottom: Double) {
    constructor(): this(0.0, 0.0, 0.0, 0.0)
}