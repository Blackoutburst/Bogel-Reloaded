package dev.blackoutburst.bogel.ui

import dev.blackoutburst.bogel.camera.Camera
import dev.blackoutburst.bogel.graphics.ColoredBox2D
import dev.blackoutburst.bogel.graphics.Text
import dev.blackoutburst.bogel.input.Keyboard
import dev.blackoutburst.bogel.input.Mouse
import dev.blackoutburst.bogel.utils.Color
import dev.blackoutburst.bogel.utils.Time
import dev.blackoutburst.bogel.window.Window

class TextField(
    var x: Float = 0f,
    var y: Float = 0f,
    var width: Float = 0f,
    var height: Float = 0f,
    var textLength: Int = 20,
    var numberOnly: Boolean = false,
    initialText: String = "",
    fontSize: Float = 16f,
    var backgroundColor: Color = Color(0.1f),
    var outlineColor: Color = Color(0.2f),
    var borderRadius: Float = 0.0f,
    var outlineSize: Float = 1.0f,
) {
    val outline = ColoredBox2D(x - (1 * outlineSize), y - (1  * outlineSize), width + (2 * outlineSize), height + ( 2 * outlineSize), outlineColor, borderRadius = borderRadius)
    val background = ColoredBox2D(x, y, width, height, backgroundColor, borderRadius = borderRadius)
    val inputText = Text(x, y, fontSize, initialText)
    private val tick = Text(x, y, fontSize, "|")
    private var hover = false
    private var focus = false
    private var tickVisible = false
    private var tickTimer = 0.0
    private val tickInterval = 500

    var cursorPosition = 0

    fun update(onValidate: () -> Unit = {}) {
        if (!focus) {
            tickVisible = false
            tickTimer = 0.0
            return
        }

        tickTimer += Time.delta

        if (tickTimer >= tickInterval) {
            tickVisible = !tickVisible
            tickTimer -= tickInterval
        }

        val keys = mutableListOf<Int>()
        keys.addAll(Keyboard.chars)
        for (k in Keyboard.keys) {
            if ((k.value != 0) && listOf(263, 262, 265, 264, 257, 259).contains(k.key))
                keys.add(k.key)
        }

        for (code in keys) {
            if (code == 263 && cursorPosition > 0)
                cursorPosition--

            if (code == 262 && cursorPosition < inputText.text.length)
                cursorPosition++

            if (code == 257) {
                if (inputText.text.isNotEmpty()) {
                    onValidate()
                }
            }

            if (numberOnly) {
                if (code in 48..57 && inputText.text.length < textLength || code == 46 && inputText.text.length < textLength) {
                    val str = "${inputText.text.substring(0, cursorPosition)}${Char(code)}${
                        inputText.text.substring(
                            cursorPosition,
                            inputText.text.length
                        )
                    }"
                    inputText.text = str
                    cursorPosition++
                }
            } else {
                if (code in 20..126 && inputText.text.length < textLength) {
                    val str = "${inputText.text.substring(0, cursorPosition)}${Char(code)}${
                        inputText.text.substring(
                            cursorPosition,
                            inputText.text.length
                        )
                    }"
                    inputText.text = str
                    cursorPosition++
                }
            }

            if (code == 259 && inputText.text.isNotEmpty()) {
                val str = "${inputText.text.substring(0, cursorPosition - 1)}${
                    inputText.text.substring(
                        cursorPosition,
                        inputText.text.length
                    )
                }"
                cursorPosition--
                inputText.text = str
            }

            tickVisible = true
            tickTimer = 0.0
        }
        Keyboard.reset()
    }

    fun onHover(unit: () -> Unit) {
        val mouseX = -Camera.position.x + Mouse.position.x
        val mouseY = -Camera.position.y + Window.height - Mouse.position.y

        if (mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height) {
            hover = true
            unit()
        }
    }

    fun onExit(unit: () -> Unit) {
        val mouseX = -Camera.position.x + Mouse.position.x
        val mouseY = -Camera.position.y + Window.height - Mouse.position.y

        if (!(mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height) && hover) {
            hover = false
            unit()
        }
    }

    fun onClick(unit: () -> Unit) {
        if (hover && Mouse.isButtonPressed(Mouse.LEFT_BUTTON)) {
            focus = true
            unit()
            Mouse.update()
        }
        if (!hover && Mouse.isButtonPressed(Mouse.LEFT_BUTTON)) {
            focus = false
        }
    }

    fun render() {
        background.color = backgroundColor
        outline.color = outlineColor

        background.x = x
        background.y = y
        outline.x = x - outlineSize
        outline.y = y - outlineSize

        tick.x = x + (cursorPosition * (inputText.height/2)) + 1f
        tick.y = y + (height/2) - (inputText.height/2)

        inputText.x = x + 5f
        inputText.y = y + (height/2) - (inputText.height/2)

        outline.render()
        background.render()
        inputText.render()

        if (tickVisible) {
            tick.render()
        }
    }
}