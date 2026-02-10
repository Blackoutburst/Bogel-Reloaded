package dev.blackoutburst.bogel.camera

import dev.blackoutburst.bogel.input.Mouse
import dev.blackoutburst.bogel.maths.Matrix
import dev.blackoutburst.bogel.maths.Vector2f
import dev.blackoutburst.bogel.maths.Vector3f
import dev.blackoutburst.bogel.maths.Vector4f
import dev.blackoutburst.bogel.window.Window

object Camera {
    var position = Vector3f(0f, 0f, 0f)
    var positionOffset = Vector3f(0f, 0f, 0f)
    var rotation = Vector2f(0f, 0f)

    var view = Matrix().translate(position)
    var projection = Matrix().projectionMatrix(90f, 1000f, 0.1f)
    var projection2D = Matrix().ortho2D(0f, Window.width.toFloat(), 0f, Window.height.toFloat(), -1f, 1f)

    val ray: Vector3f
        get() {
            val mouseXNDC = (2.0 * Mouse.position.x / Window.width) - 1.0
            val mouseYNDC = 1.0 - (2.0 * Mouse.position.y / Window.height)
            val rayClip = Vector4f(mouseXNDC.toFloat(), mouseYNDC.toFloat(), -1.0f, 1.0f)

            val inverseProjection = projection.copy().invert()
            val inverseView = view.copy().invert()

            val rayEye = inverseProjection.transform(rayClip)
            rayEye.z = -1.0f
            rayEye.w = 0.0f

            val rayWorld = inverseView.transform(Vector4f(rayEye.x, rayEye.y, rayEye.z, rayEye.w))

            return Vector3f(rayWorld.x, rayWorld.y, rayWorld.z).normalize()
        }

    private fun getSpacePosition(): Vector3f {
        val inverseView = view.copy().invert()
        return Vector3f(inverseView.m30, inverseView.m31, inverseView.m32)
    }

    fun getWorldPositionAtY0(): Vector3f? {
        val rayOrigin = getSpacePosition()
        val rayDirection = ray

        if (rayDirection.y == 0f) {
            return null
        }

        val t = -rayOrigin.y / rayDirection.y

        if (t < 0f) {
            return null
        }

        val intersectionPoint = Vector3f(
            rayOrigin.x + rayDirection.x * t,
            0f,
            rayOrigin.z + rayDirection.z * t
        )

        return intersectionPoint
    }
}
