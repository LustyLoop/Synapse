package shaders

import android.graphics.RuntimeShader
import android.os.Build
val ShaderBackground: RuntimeShader? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
    RuntimeShader("""
uniform float2 resolution;
uniform float time; // Используется ТОЛЬКО для постоянного покачивания волн/жидкости
uniform float rand;
uniform float transitionProgress; 
uniform float inDarkTheme; 

float random(float2 p)
{
    return fract(sin(dot(p, float2(12.9898, 78.233))) * 43758.5453);
}

float blob(float2 p, float2 c, float size)
{
    float d = length(p - c);
    return smoothstep(size, 0.0, d);
}

half4 main(float2 fragCoord)
{
    float2 uv = 1.0 - (fragCoord.xy / resolution.xy);

    float2 p = uv - 0.5;
    p.x *= resolution.x / resolution.y;

    // Скорость внутреннего движения волн и пятен (анимируется всегда)
    float t = time * 0.6; 
    float rand = random(float2(1.0, 2.0));

    float3 background;
    
    if(inDarkTheme > 0.0){
        background = float3(0.003, 0.008, 0.02);
    }
    else{
        background = float3(1.0, 1.0, 1.0);
    }
    // ------------------------------------------------
    // ПРОГРЕСС И МОРФИНГ (Управляется строго из Kotlin)
    // ------------------------------------------------
    
    // Плавное сглаживание для морфинга геометрии
    float morph = transitionProgress * transitionProgress * (3.0 - 2.0 * transitionProgress);

    // ------------------------------------------------
    // ВОЛНА
    // ------------------------------------------------

    float wave = sin(p.x * 4.0 - t * 1.2) * 0.18 + cos(p.x * 8.0 - t * rand) * 0.06;
    wave += sin(p.x * 2.0 + t * 0.8) * cos(p.x * 5.0 - t * 0.5) * 0.025;

    // ------------------------------------------------
    // ПОДГОТОВКА ВОЛНЫ
    // ------------------------------------------------

    float waveY = mix(wave, 0.0, morph);
    float verticalPull = p.y * mix(1.0, 0.35, morph);

    // ------------------------------------------------
    // ГЕОМЕТРИЯ ШАРА
    // ------------------------------------------------

    float radius = 0.16;
    float waveField = verticalPull - waveY;
    float radialField = length(p) - radius;
    
    // Переход формы из волны в шар
    float shapeField = mix(waveField, radialField, morph);

    // Шар становится четче по мере округления
    float softness = mix(0.09, 0.018, morph);

    float shapeMask = 1.0 - smoothstep(-softness, softness, shapeField);

    // ------------------------------------------------
    // СОБИРАНИЕ КРАЁВ (Сжатие бесконечной волны в шар)
    // ------------------------------------------------

    float gather = smoothstep(0.15, 0.85, morph);
    float edgeGather = smoothstep(0.12, 0.75, abs(p.x));
    float gatheredMask = mix(1.0, 1.0 - edgeGather, gather * 0.85);

    shapeMask *= gatheredMask;

    // ------------------------------------------------
    // ДВИЖЕНИЕ ЖИДКОСТИ
    // ------------------------------------------------

    float2 q = p;
    q += 0.035 * float2(sin(p.y * 8.0 + t * 3.0), cos(p.x * 7.0 - t * 2.5));
    q += 0.018 * float2(sin(p.y * 17.0 - t * 2.0), cos(p.x * 13.0 + t * 1.7));
    q.x *= mix(0.35, 1.0, morph);

    // ------------------------------------------------
    // ЦВЕТОВЫЕ ПЯТНА ВНУТРИ ФОРМЫ
    // ------------------------------------------------

    float2 c1 = float2(0.18 * sin(t * 1.3), 0.18 * cos(t * 1.1));
    float2 c2 = float2(0.20 * cos(t * 0.9 + 2.0), 0.17 * sin(t * 1.2 + 1.0));
    float2 c3 = float2(0.16 * sin(t * 1.1 + 4.0), 0.22 * cos(t * 0.8 + 3.0));
    float2 c4 = float2(0.22 * cos(t * 0.7 + 5.0), 0.16 * sin(t * 1.4 + 2.0));

    float b1 = blob(q, c1, 0.42);
    float b2 = blob(q, c2, 0.40);
    float b3 = blob(q, c3, 0.38);
    float b4 = blob(q, c4, 0.40);

    // ------------------------------------------------
    // ЦВЕТА ФОРМЫ
    // ------------------------------------------------

    float3 pink   = float3(0.6, 0.08, 0.55);
    float3 purple = float3(0.25, 0.08, 0.3);
    float3 blue   = float3(0.02, 0.35, 0.2);
    float3 cyan   = float3(0.0, 0.4, 0.25);

    float3 sphereColor = float3(0.015, 0.008, 0.005);
    sphereColor += b1 * pink;
    sphereColor += b2 * blue;
    sphereColor += b3 * purple;
    sphereColor += b4 * cyan;

    // ------------------------------------------------
    // СПЕКТР ПОДСВЕТКИ
    // ------------------------------------------------

    float spectrum = sin(q.x * 11.0 + q.y * 9.0 + t * 4.0 + sin(q.y * 8.0 + t));
    spectrum = spectrum * 0.5 + 0.5;

    float3 rainbow = float3(
        0.5 + 0.5 * sin(spectrum * 6.283 + 0.0),
        0.5 + 0.5 * sin(spectrum * 6.283 + 2.094),
        0.5 + 0.5 * sin(spectrum * 6.283 + 4.188)
    );

    sphereColor = mix(sphereColor, sphereColor + rainbow * 0.18, 0.30);

    // ------------------------------------------------
    // БЛИКИ И СВЕЧЕНИЕ (Проявляются только в форме шара)
    // ------------------------------------------------

    float sphereDetails = smoothstep(0.45, 0.95, morph);

    float2 lightPos = float2(-0.18 + sin(t) * 0.08, 0.20 + cos(t * 0.8) * 0.06);
    float highlight = exp(-length(p - lightPos) * 18.0);

    float2 light2 = float2(0.20 + cos(t * 0.7) * 0.05, -0.16 + sin(t * 0.9) * 0.05);
    float highlight2 = exp(-length(p - light2) * 25.0);

    sphereColor += highlight * float3(1.0, 0.85, 1.0) * 0.45 * sphereDetails;
    sphereColor += highlight2 * float3(0.4, 0.8, 1.0) * 0.30 * sphereDetails;

    // КРАЙ ШАРА
    float dist = length(p);
    float rim = smoothstep(radius - 0.07, radius, dist);
    sphereColor += rim * float3(0.35, 0.25, 0.95) * 0.55 * sphereDetails;

    float innerGlow = 1.0 - smoothstep(0.0, radius, dist);
    sphereColor += innerGlow * 0.05 * sphereDetails;

    // ------------------------------------------------
    // СМЕШИВАНИЕ ЦВЕТОВ И ФИНАЛ
    // ------------------------------------------------
    float3 waveColor;
    if(inDarkTheme > 0.0){
        waveColor = float3(0.5, 0.15, 0.85);
    }
    else{
        waveColor = float3(1.0, 0.3, 0.3);
    }
    float3 formColor = mix(waveColor, sphereColor, smoothstep(0.15, 0.75, morph));

    float3 color = mix(background, formColor, shapeMask);
    color = min(color, 1.0);

    return half4(half3(color), 1.0);
}

    """.trimIndent()
    )
} else {
    null
}

/*
val DarkShaderBackground: RuntimeShader? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
    RuntimeShader(
        """
        uniform float2 resolution;
        uniform float time;
        uniform float rand;
        uniform float hideAndShowProgress;
        uniform float hideAndShowProgressFlag;

        float random(float2 p)
        {
            return fract(
                sin(dot(p, float2(12.9898, 78.233)))
                * 43758.5453
            );
        }

        half4 main(float2 fragCoord)
        {
            float time = time * 0.3;
            float rand = rand / 1.69;

            float2 uv = 1.0 - (fragCoord / resolution);
            
            // Коррекция пропорций
            float2 p = uv - rand - 0.15;
            p.x *= resolution.x / resolution.y;

            // Движущаяся волна
            float wave =
                sin(p.x * 4.0 - time * 1.2) * 0.18 +
                cos(p.x * 8.0 - time * rand) * 0.06;

            // Положение границы
            float edge = wave;
            
            edge -= hideAndShowProgress * 1.5;
            
            // Мягкая граница
            float softness = 0.08;

            float mask = smoothstep(
                edge - softness,
                edge + softness,
                p.y
            );

            // Синий цвет
            float3 blue = float3(0.11, 0.35, 1.0);

            // Темный фон
            float3 background = float3(0.003, 0.008, 0.02);

            // Всё ниже волны - синий,
            // всё выше - фон
            float waveMask = 1.0 - mask;

            float3 color = mix(background, blue, waveMask);

            return half4(color, 1.0);
        }
    """.trimIndent()
    )
} else {
    null
}
*/
val LightShaderBackground: RuntimeShader? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
    RuntimeShader(
        """
        uniform float2 resolution;
        uniform float time;
        uniform float rand;
        uniform float hideAndShowProgress;

        float random(float2 p)
        {
            return fract(
                sin(dot(p, float2(12.9898, 78.233)))
                * 43758.5453
            );
        }

        half4 main(float2 fragCoord)
        {
            float time = time * 0.3;
            float rand = rand / 1.69;

            float2 uv = 1.0 - (fragCoord / resolution);
            
            // Коррекция пропорций
            float2 p = uv - rand - 0.15;
            p.x *= resolution.x / resolution.y;

            // Движущаяся волна
            float wave =
                sin(p.x * 4.0 - time * 1.2) * 0.18 +
                cos(p.x * 8.0 - time * rand) * 0.06;

            // Положение границы
            float edge = wave;
            edge -= hideAndShowProgress * 1.5; 

            // Мягкая граница
            float softness = 0.08;

            float mask = smoothstep(
                edge - softness,
                edge + softness,
                p.y
            );

            // красный цвет
            float3 red = float3(1.0, 0.3, 0.3);

            // Светлый фон
            float3 background = float3(1.0, 1.0, 1.0);

            // Всё ниже волны - синий,
            // всё выше - фон
            float waveMask = 1.0 - mask;

            float3 color = mix(background, red, waveMask);

            return half4(color, 1.0);
        }
    """.trimIndent()
    )
} else {
    null
}


