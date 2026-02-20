package dev.blackoutburst.bogel.ui

import dev.blackoutburst.bogel.graphics.ColoredBox2D
import dev.blackoutburst.bogel.graphics.Text
import dev.blackoutburst.bogel.utils.Color

class TextField(
    var x: Float = 0f,
    var y: Float = 0f,
    var width: Float = 0f,
    var height: Float = 0f,
    var textLength: Int = 20,
    var numberOnly: Boolean = false,
    initialText: String = "",
    fontSize: Float = 16f,
    backgroundColor: Color = Color(0.1f),
    outlineColor: Color = Color(0.2f),
    var borderRadius: Float = 0.0f,
    var outlineSize: Float = 1.0f,
    val onValidate: () -> Unit = {},
) {
    val outline = ColoredBox2D(x - (1 * outlineSize), y - (1  * outlineSize), width + (2 * outlineSize), height + ( 2 * outlineSize), outlineColor, borderRadius = borderRadius)
    val background = ColoredBox2D(x, y, width, height, backgroundColor, borderRadius = borderRadius)
    private val inputText = Text(x, y, fontSize, initialText)
    private val tick = Text(x, y, fontSize, "_")
    private var cursorPosition = 0

    fun update(code: Int) {
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
                val str = "${inputText.text.substring(0, cursorPosition)}${Char(code)}${inputText.text.substring(cursorPosition, inputText.text.length)}"
                inputText.text = str
                cursorPosition++
            }
        } else {
            if (code in 20..126 && inputText.text.length < textLength) {
                val str = "${inputText.text.substring(0, cursorPosition)}${Char(code)}${inputText.text.substring(cursorPosition, inputText.text.length)}"
                inputText.text = str
                cursorPosition++
            }
        }

        if (code == 259 && inputText.text.isNotEmpty()) {
            val str = "${inputText.text.substring(0, cursorPosition - 1)}${inputText.text.substring(cursorPosition, inputText.text.length)}"
            cursorPosition--
            inputText.text = str
        }
    }

    fun render() {

        outline.x = x - outlineSize
        outline.y = y - outlineSize

        outline.render()
        background.render()
        inputText.render()

        tick.x = (cursorPosition * (inputText.height/2)) + 1f
        tick.y = y

        inputText.x = x + 5f
        inputText.y = y + (height/2) + (inputText.height/2)

        tick.render()
    }
}