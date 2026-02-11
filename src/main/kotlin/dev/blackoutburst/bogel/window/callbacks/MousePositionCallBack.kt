package dev.blackoutburst.bogel.window.callbacks

import dev.blackoutburst.bogel.input.Mouse
import dev.blackoutburst.bogel.utils.stack
import org.lwjgl.glfw.GLFW.glfwGetCursorPos
import org.lwjgl.glfw.GLFWCursorPosCallbackI
import org.lwjgl.system.Platform

class MousePositionCallBack : GLFWCursorPosCallbackI {
    override fun invoke(window: Long, xPos: Double, yPos: Double) {
        stack {
            val scale = if (Platform.get() == Platform.MACOSX) 2.0 else 1.0
            val width = it.mallocDouble(1)
            val height = it.mallocDouble(1)

            glfwGetCursorPos(window, width, height)

            Mouse.position.set((width.get() * scale).toFloat(), (height.get() * scale).toFloat())
        }
    }
}
