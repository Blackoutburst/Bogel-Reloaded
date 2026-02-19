#version 410 core

in vec2 uv;
uniform float borderRadius;
uniform vec2 size;

out vec4 FragColor;

vec3 hsv2rgb(vec3 c) {
    vec4 K = vec4(1.0, 2.0 / 3.0, 1.0 / 3.0, 3.0);
    vec3 p = abs(fract(c.xxx + K.xyz) * 6.0 - K.www);
    return c.z * mix(K.xxx, clamp(p - K.xxx, 0.0, 1.0), c.y);
}

float sdRoundedBox(vec2 p, vec2 b, float r) {
    vec2 q = abs(p) - b + r;
    return min(max(q.x, q.y), 0.0) + length(max(q, 0.0)) - r;
}

void main() {
    vec2 p = (uv - vec2(0.5)) * size;
    vec2 halfSize = size * 0.5;
    float r = min(borderRadius, min(halfSize.x, halfSize.y));
    float dist = sdRoundedBox(p, halfSize, r);

    if (dist > 0.0) {
        discard;
    }

    float hue = gl_FragCoord.x / 180.0;
    vec3 color = hsv2rgb(vec3(hue, 1.0, 1.0));
    FragColor = vec4(color, 1.0);
}