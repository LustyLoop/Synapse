package shaders

import android.graphics.RuntimeShader


val ballShader = RuntimeShader("""
uniform float2 resolution;
uniform float time;

half3 pink   = half3(1.0, 0.08, 0.55);
half3 purple = half3(0.55, 0.08, 1.0);
half3 blue   = half3(0.02, 0.35, 1.0);
half3 cyan   = half3(0.0, 0.95, 0.95);
half3 green  = half3(0.05, 1.0, 0.35);

// Плавное смешивание цветовых пятен
half blob(float2 p, float2 c, float size) {
    float d = length(p - c);
    return smoothstep(size, 0.0, d);
}

// Фрактальный шум (без изменений)
float noise(float2 p) {
    return fract(
        sin(dot(p, float2(127.1, 311.7))) *
                43758.5453123
    );
}

float smoothNoise(float2 p) {
    float2 i = floor(p);
    float2 f = fract(p);
    f = f * f * (3.0 - 2.0 * f);
    float a = noise(i);
    float b = noise(i + float2(1.0, 0.0));
    float c = noise(i + float2(0.0, 1.0));
    float d = noise(i + float2(1.0, 1.0));
    return mix(
        mix(a, b, f.x),
        mix(c, d, f.x),
        f.y
    );
}

half4 main(float2 fragCoord) {
    float2 uv = fragCoord / resolution;
    float2 p = uv - 0.5;
    p.x *= resolution.x / resolution.y;
    p.y += 0.06;

    float t = time * 0.52;

    // КРУГ
    float radius = 0.16;
    float dist = length(p);
    float mask = 1.0 - smoothstep(radius - 0.02, radius + 0.02, dist);

    // ДВИЖЕНИЕ ЖИДКОСТИ
    float2 q = p;
    q += 0.035 * float2(
        sin(p.y * 8.0 + t * 3.0),
        cos(p.x * 7.0 - t * 2.5)
    );
    q += 0.018 * float2(
        sin(p.y * 17.0 - t * 2.0),
        cos(p.x * 13.0 + t * 1.7)
    );

    //ЦВЕТОВЫЕ ПЯТНА
    float2 c1 = float2(0.18 * sin(t * 1.3), 0.18 * cos(t * 1.1));
    float2 c2 = float2(0.20 * cos(t * 0.9 + 2.0), 0.17 * sin(t * 1.2 + 1.0));
    float2 c3 = float2(0.16 * sin(t * 1.1 + 4.0), 0.22 * cos(t * 0.8 + 3.0));
    float2 c4 = float2(0.22 * cos(t * 0.7 + 5.0), 0.16 * sin(t * 1.4 + 2.0));

    half b1 = blob(q, c1, 0.42);
    half b2 = blob(q, c2, 0.40);
    half b3 = blob(q, c3, 0.38);
    half b4 = blob(q, c4, 0.40);

    // МАСЛЯНЫЕ ЦВЕТА 
    half3 color = half3(0.015, 0.008, 0.025);
    color += b1 * pink;
    color += b2 * blue;
    color += b3 * purple;
    color += b4 * cyan;

    // ПЕРЕЛИВАЮЩИЙСЯ СПЕКТР 
    float spectrum = sin(
            q.x * 11.0 +
                    q.y * 9.0 +
                    t * 4.0 +
                    sin(q.y * 8.0 + t)
            );
    spectrum = spectrum * 0.5 + 0.5;

    half3 rainbow = half3(
            0.5 + 0.5 * sin(spectrum * 6.283 + 0.0),
    0.5 + 0.5 * sin(spectrum * 6.283 + 2.094),
    0.5 + 0.5 * sin(spectrum * 6.283 + 4.188)
    );
    color = mix(color, color + rainbow * 0.35, 0.45);

    //  БЛИКИ 
    float2 lightPos = float2(-0.18 + sin(t) * 0.08, 0.20 + cos(t * 0.8) * 0.06);
    float highlight = exp(-length(p - lightPos) * 18.0);
    color += highlight * half3(1.0, 0.85, 1.0) * 0.75;

    float2 light2 = float2(0.20 + cos(t * 0.7) * 0.05, -0.16 + sin(t * 0.9) * 0.05);
    float highlight2 = exp(-length(p - light2) * 25.0);
    color += highlight2 * half3(0.4, 0.8, 1.0) * 0.5;

    // СТЕКЛЯННЫЙ КРАЙ
    float rim = smoothstep(radius - 0.07, radius, dist);
    color += rim * half3(0.35, 0.25, 0.95);

    float innerGlow = 1.0 - smoothstep(0.0, radius, dist);
    color += innerGlow * 0.08;

    //  ФИНАЛ 
    color *= mask;
    color = 1.0 - exp(-color * 0.8);

    return half4(color, mask);
}
""".trimIndent()
)