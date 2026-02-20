package dev.blackoutburst.bogel.window.callbacks

import dev.blackoutburst.bogel.input.Keyboard
import org.lwjgl.glfw.GLFWCharCallbackI

class KeyboardCharCallBack : GLFWCharCallbackI {
    override fun invoke(window: Long, key: Int) {
        Keyboard.chars.add(key)
    }
}
