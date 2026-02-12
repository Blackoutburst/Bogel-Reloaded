package dev.blackoutburst.bogel.window

import dev.blackoutburst.bogel.input.Keyboard
import dev.blackoutburst.bogel.input.Mouse
import dev.blackoutburst.bogel.utils.IOUtils
import dev.blackoutburst.bogel.utils.Time
import dev.blackoutburst.bogel.utils.stack
import dev.blackoutburst.bogel.window.callbacks.*
import org.lwjgl.glfw.Callbacks.glfwFreeCallbacks
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.glfw.GLFWImage
import org.lwjgl.opengl.GL.createCapabilities
import org.lwjgl.opengl.GL11.*
import org.lwjgl.stb.STBImage
import org.lwjgl.system.MemoryStack
import org.lwjgl.system.MemoryUtil.NULL
import org.lwjgl.system.Platform
import java.nio.ByteBuffer
import kotlin.system.exitProcess

object Window {
    private var title = MemoryStack.stackPush().UTF8("Bogel")

    var id = -1L
    var isOpen = false

    var width: Int = 1280
        internal set
    var height: Int = 720
        internal set

    init {
        glfwInit()
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE)
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3)
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2)
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE)
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE)

        id = glfwCreateWindow(1280, 720, title, NULL, NULL)
        if (id == -1L) exitProcess(-1)

        glfwMakeContextCurrent(id)
        createCapabilities()

        glfwCreateStandardCursor(GLFW_POINTING_HAND_CURSOR)

        glClearColor(0.1f, 0.1f, 0.1f, 1f)
        glLineWidth(2.0f)
        glPointSize(2.0f)

        setCallbacks()

        //setIcons()

        isOpen = true
    }

    fun update() {
        isOpen = !(glfwWindowShouldClose(id))

        Time.updateDelta()
        Mouse.update()
        Keyboard.update()

        glfwSwapBuffers(id)
        glfwPollEvents()
    }

    fun setVsync(enable: Boolean): Window {
        glfwSwapInterval(if (enable) GLFW_TRUE else GLFW_FALSE)

        return this
    }

    fun setTitle(str: String): Window {
        title = MemoryStack.stackPush().UTF8(str)

        glfwSetWindowTitle(id, title)

        return this
    }

    fun clear() {
        glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)
    }

    fun destroy() {
        glfwFreeCallbacks(id)
        glfwDestroyWindow(id)
        glfwTerminate()
    }

    private fun setCallbacks() {
        glfwSetWindowSizeCallback(id, WindowCallBack())
        glfwSetScrollCallback(id, MouseScrollCallBack())
        glfwSetCursorPosCallback(id, MousePositionCallBack())
        glfwSetKeyCallback(id, KeyboardCallBack())
        glfwSetMouseButtonCallback(id, MouseButtonCallBack())
        glfwSetCharCallback(id, KeyboardCharCallBack())
    }

    fun setIcons(filePath: String): Window {
        if (Platform.get() == Platform.MACOSX) return this

        val image = GLFWImage.malloc()
        val buffer = GLFWImage.malloc(1)
        try {
            image[256, 256] = loadIcon(filePath)
            buffer.put(0, image)
            glfwSetWindowIcon(id, buffer)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return this
    }

    @Throws(Exception::class)
    private fun loadIcon(filePath: String): ByteBuffer {
        var image: ByteBuffer? = null

        stack {
            val comp = it.mallocInt(1)
            val w = it.mallocInt(1)
            val h = it.mallocInt(1)
            val img = IOUtils.ioResourceToByteBuffer(filePath, 8 * 1024)

            image = STBImage.stbi_load_from_memory(img, w, h, comp, 4)
            if (image == null) {
                throw Exception("Failed to load icons")
            }
        }

        return image!!
    }
}