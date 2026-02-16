package dev.blackoutburst.bogel.window.callbacks

import dev.blackoutburst.bogel.camera.Camera
import dev.blackoutburst.bogel.maths.Matrix
import dev.blackoutburst.bogel.window.Window
import org.lwjgl.glfw.GLFW.glfwSetWindowSize
import org.lwjgl.glfw.GLFWWindowSizeCallbackI
import org.lwjgl.opengl.GL11.glViewport
import org.lwjgl.system.Platform

class WindowCallBack : GLFWWindowSizeCallbackI {
    override fun invoke(window: Long, width: Int, height: Int) {
        val scale = if (Platform.get() == Platform.MACOSX) 2 else 1

        glfwSetWindowSize(window, width, height)
        glViewport(0, 0, width * scale, height * scale)

        Window.width = width
        Window.height = height

        Camera.projection2D = Matrix().ortho2D(0f, width.toFloat(), 0f, height.toFloat(), -1f, 1f)
    }
}