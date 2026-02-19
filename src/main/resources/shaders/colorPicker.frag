#version 410 core

in vec2 uv;

uniform vec4 color;
uniform float borderRadius;
uniform vec2 size;

out vec4 FragColor;

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

    vec3 topMix = mix(vec3(1.0, 1.0, 1.0), color.rgb, uv.x);

    vec3 bottomMix = vec3(0.0, 0.0, 0.0);

    vec3 finalColor = mix(topMix, bottomMix, 1 - uv.y);

    FragColor = vec4(finalColor, color.a);
}