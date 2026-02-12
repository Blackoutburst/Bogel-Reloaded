package dev.blackoutburst.bogel.graphics

import dev.blackoutburst.bogel.camera.Camera
import dev.blackoutburst.bogel.maths.Matrix
import dev.blackoutburst.bogel.shader.Shader
import dev.blackoutburst.bogel.shader.ShaderProgram
import dev.blackoutburst.bogel.utils.Color
import org.lwjgl.opengl.GL30.*
import org.lwjgl.system.MemoryUtil
import java.nio.FloatBuffer
import java.nio.IntBuffer

class Text(var x: Float, var y: Float, var size: Float = 16f, initialText: String, maxLength: Int = 200, private val processColor: Boolean = true) {

    companion object {
        private val colorCharMap = mapOf(
            "&0" to 200,
            "&1" to 201,
            "&2" to 202,
            "&3" to 203,
            "&4" to 204,
            "&5" to 205,
            "&6" to 206,
            "&7" to 207,
            "&8" to 208,
            "&9" to 209,
            "&a" to 210,
            "&b" to 211,
            "&c" to 212,
            "&d" to 213,
            "&e" to 214,
            "&f" to 215,
        )

        private val colorMap = mapOf(
            200 to Color(0f),
            201 to Color(0f, 0f, 0.5f),
            202 to Color(0f, 0.5f, 0f),
            203 to Color(0f, 0.5f, 0.5f),
            204 to Color(0.5f, 0f, 0f),
            205 to Color(0.5f, 0f, 0.5f),
            206 to Color(0.8f, 0.5f, 0f),
            207 to Color(0.5f),
            208 to Color(0.25f),
            209 to Color(0f, 0f, 1f),
            210 to Color(0f, 1f, 0f),
            211 to Color(0f, 1f, 1f),
            212 to Color(1f, 0f, 0f),
            213 to Color(1f, 0f, 1f),
            214 to Color(1f, 1f, 0f),
            215 to Color(1f),
        )

        private val texture = Texture("ascii.png")
        private val vertexShader = Shader(GL_VERTEX_SHADER, "/shaders/text.vert")
        private val fragmentShader = Shader(GL_FRAGMENT_SHADER, "/shaders/text.frag")
        private val shaderProgram = ShaderProgram(vertexShader, fragmentShader)
    }

    private var indexCount = 0

    private val vaoID = glGenVertexArrays()
    private val vboID = glGenBuffers()
    private val eboID = glGenBuffers()

    private var model = Matrix()

    private val vertices = FloatArray(maxLength * 28)
    private val indices = IntArray(maxLength * 6)
    private val vertexBuffer: FloatBuffer = MemoryUtil.memAllocFloat(maxLength * 28)
    private val indexBuffer: IntBuffer = MemoryUtil.memAllocInt(maxLength * 6)

    var text: String = ""
        set(value) {

            if (processColor) {
                colorCharMap.forEach { (k, v) -> field = value.replace(k, Char(v).toString()) }
            }

            if (field != value) {
                field = value
                updateMesh()
            }
        }

    var width: Float = size
        get() {
            return (text.length * size) - ((text.length - 1) * size * 4 / 8f)
        }

    var height: Float = size
        get() {
            return size
        }

    init {
        setupVAO()
        text = initialText
    }

    private fun setupVAO() {
        glBindVertexArray(vaoID)

        glBindBuffer(GL_ARRAY_BUFFER, vboID)
        glBufferData(GL_ARRAY_BUFFER, (vertices.size * 4).toLong(), GL_DYNAMIC_DRAW)
        glEnableVertexAttribArray(0)
        glVertexAttribPointer(0, 2, GL_FLOAT, false, 28, 0)
        glEnableVertexAttribArray(1)
        glVertexAttribPointer(1, 2, GL_FLOAT, false, 28, 8)
        glEnableVertexAttribArray(2)
        glVertexAttribPointer(2, 3, GL_FLOAT, false, 28, 16)

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID)
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, (indices.size * 4).toLong(), GL_DYNAMIC_DRAW)

        glBindVertexArray(0)
    }

    private fun updateMesh() {
        var vertexPos = 0
        var indexPos = 0
        var quadIndex = 0
        var color = Color.WHITE
        var i = 0

        for (ci in text.indices) {
            val code = text[ci].code
            val mappedColor = colorMap[code]
            if (mappedColor != null) {
                color = mappedColor
                continue
            }

            val pos = i / 8f
            val x = (code % 16).toFloat()
            val y = (code / 16).toFloat()
            val u0 = x / 16f;       val u1 = (x + 1f) / 16f
            val v0 = (15f - y) / 16f; val v1 = (16f - y) / 16f
            val r = color.r; val g = color.g; val b = color.b

            // bottom left
            vertices[vertexPos++] = pos;  vertices[vertexPos++] = 0f
            vertices[vertexPos++] = u0;   vertices[vertexPos++] = v0
            vertices[vertexPos++] = r;    vertices[vertexPos++] = g; vertices[vertexPos++] = b

            // top right
            vertices[vertexPos++] = pos + 1f; vertices[vertexPos++] = 1f
            vertices[vertexPos++] = u1;       vertices[vertexPos++] = v1
            vertices[vertexPos++] = r;        vertices[vertexPos++] = g; vertices[vertexPos++] = b

            // top left
            vertices[vertexPos++] = pos;  vertices[vertexPos++] = 1f
            vertices[vertexPos++] = u0;   vertices[vertexPos++] = v1
            vertices[vertexPos++] = r;    vertices[vertexPos++] = g; vertices[vertexPos++] = b

            // bottom right
            vertices[vertexPos++] = pos + 1f; vertices[vertexPos++] = 0f
            vertices[vertexPos++] = u1;       vertices[vertexPos++] = v0
            vertices[vertexPos++] = r;        vertices[vertexPos++] = g; vertices[vertexPos++] = b

            val base = quadIndex * 4
            indices[indexPos++] = base;     indices[indexPos++] = base + 1; indices[indexPos++] = base + 2
            indices[indexPos++] = base;     indices[indexPos++] = base + 3; indices[indexPos++] = base + 1

            quadIndex++
            i += 4
        }

        indexCount = indexPos

        vertexBuffer.clear()
        vertexBuffer.put(vertices, 0, vertexPos).flip()
        indexBuffer.clear()
        indexBuffer.put(indices, 0, indexPos).flip()

        glBindVertexArray(vaoID)
        glBindBuffer(GL_ARRAY_BUFFER, vboID)
        glBufferSubData(GL_ARRAY_BUFFER, 0, vertexBuffer)
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID)
        glBufferSubData(GL_ELEMENT_ARRAY_BUFFER, 0, indexBuffer)
        glBindVertexArray(0)
    }

    fun render() {
        glUseProgram(shaderProgram.id)
        shaderProgram.setUniform1i("diffuseMap", 0)
        shaderProgram.setUniformMat4("model", model.setIdentity().translate(x, y).scale(size, size))
        shaderProgram.setUniformMat4("projection", Camera.projection2D)

        glActiveTexture(GL_TEXTURE0)
        glBindTexture(GL_TEXTURE_2D, texture.id)

        glBindVertexArray(vaoID)
        glDrawElements(GL_TRIANGLES, indexCount, GL_UNSIGNED_INT, 0)
    }

    fun destroy() {
        MemoryUtil.memFree(vertexBuffer)
        MemoryUtil.memFree(indexBuffer)
        glDeleteVertexArrays(vaoID)
        glDeleteBuffers(vboID)
        glDeleteBuffers(eboID)
    }
}