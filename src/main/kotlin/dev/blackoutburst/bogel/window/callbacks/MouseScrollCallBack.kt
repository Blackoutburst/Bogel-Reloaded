package dev.blackoutburst.bogel.window.callbacks

import dev.blackoutburst.bogel.input.Mouse
import org.lwjgl.glfw.GLFWScrollCallbackI

class MouseScrollCallBack : GLFWScrollCallbackI {
    override fun invoke(window: Long, xOffset: Double, yOffset: Double) {
        Mouse.scroll = (Mouse.scroll + yOffset).toFloat()
    }
}
