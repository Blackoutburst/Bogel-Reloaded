package dev.blackoutburst.bogel.ui

import dev.blackoutburst.bogel.camera.Camera
import dev.blackoutburst.bogel.graphics.ColoredBox2D
import dev.blackoutburst.bogel.graphics.Text
import dev.blackoutburst.bogel.input.Mouse
import dev.blackoutburst.bogel.utils.Color
import dev.blackoutburst.bogel.window.Window

class Button(
    x: Float,
    y: Float,
    var width: Float,
    var height: Float,
    textDisplayed: String,
    var borderRadius: Float = 0.0f,
    initialFontSize: Float = 16f,
    initialOutlineSize: Float = 1.0f,
    initialBackgroundColor: Color = Color(0.1f),
    initialOutlineColor: Color = Color(0.2f)
){
    var fontSize: Float = initialFontSize
    var outlineSize: Float = initialOutlineSize

    var backgroundColor: Color = initialBackgroundColor
        set(value) {
            field = value
            background.color = value
        }

    var outlineColor: Color = initialOutlineColor
        set(value) {
            field = value
            outline.color = value
        }

    var x: Float = x
        set(value) {
            field = value
            outline.x = field - (1 * outlineSize)
            background.x = field
            text.x = x + width / 2 - text.width / 2
        }

    var y: Float = y
        set(value) {
            field = value
            outline.y = field - (1 * outlineSize)
            background.y = field
            text.y = y + height / 2 - text.height / 2
        }

    val outline = ColoredBox2D(x - (1 * outlineSize), y - (1  * outlineSize), width + (2 * outlineSize), height + ( 2 * outlineSize), outlineColor, borderRadius = borderRadius)
    val background = ColoredBox2D(x, y, width, height, backgroundColor, borderRadius = borderRadius)
    val text = Text(x + width / 2, y + height / 2, fontSize, textDisplayed)

    private var hover = false

    init {
        text.x -= text.width / 2
        text.y -= text.height / 2
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
            unit()
        }
    }

    fun render() {
        outline.x = x - outlineSize
        outline.y = y - outlineSize

        outline.render()
        background.render()

        if (text.text.isNotEmpty())
            text.render()
    }
}