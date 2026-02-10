package dev.blackoutburst.bogel.maths

import kotlin.math.sqrt

class Vector4b {
    var x: Byte
    var y: Byte
    var z: Byte
    var w: Byte

    constructor() {
        this.x = 0
        this.y = 0
        this.z = 0
        this.w = 0
    }

    constructor(size: Byte) {
        this.x = size
        this.y = size
        this.z = size
        this.w = size
    }

    constructor(x: Byte, y: Byte, z: Byte, w: Byte) {
        this.x = x
        this.y = y
        this.z = z
        this.w = w
    }

    fun set(x: Byte, y: Byte, z: Byte, w: Byte) {
        this.x = x
        this.y = y
        this.z = z
        this.w = w
    }
    
    fun length(): Float {
        return (sqrt((x * x + y * y + z * z + w * w).toDouble()).toFloat())
    }

    fun copy(): Vector4b {
        val newVector = Vector4b()
        newVector.x = this.x
        newVector.y = this.y
        newVector.z = this.z
        newVector.w = this.w

        return (newVector)
    }

    override fun toString(): String {
        return "[$x, $y, $z, $w]"
    }
}
