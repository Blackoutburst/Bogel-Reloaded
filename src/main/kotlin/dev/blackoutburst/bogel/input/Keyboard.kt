package dev.blackoutburst.bogel.input

object Keyboard {

    const val RELEASE = 0
    const val PRESS = 1
    const val DOWN = 2

    val keys = mutableMapOf<Int, Int>()
    val chars = mutableListOf<Int>()

    fun update() {
        chars.clear()
        keys.forEach { (key, value) ->
            if (value == PRESS) {
                keys[key] = DOWN
            }
        }
    }

    fun reset() {
        chars.clear()
        keys.clear()
    }

    fun isKeyPressed(key: Int): Boolean {
        return keys[key] == PRESS
    }

    fun isKeyReleased(key: Int): Boolean {
        return keys[key] == RELEASE
    }

    fun isKeyDown(key: Int): Boolean {
        return keys[key] == DOWN
    }
}
