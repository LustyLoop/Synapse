//package shaders
//
//import android.graphics.RuntimeShader
//
//
//
//val DarkShaderBackground = RuntimeShader(
//            """
//        uniform float2 resolution;
//        uniform float time;
//        uniform float rand;
//        uniform float hideAndShowProgress;
//        uniform float hideAndShowProgressFlag;
//
//        float random(float2 p)
//        {
//            return fract(
//                sin(dot(p, float2(12.9898, 78.233)))
//                * 43758.5453
//            );
//        }
//
//        half4 main(float2 fragCoord)
//        {
//            float time = time * 0.3;
//            float rand = rand / 1.69;
//
//            float2 uv = 1.0 - (fragCoord / resolution);
//
//            // Коррекция пропорций
//            float2 p = uv - rand - 0.15;
//            p.x *= resolution.x / resolution.y;
//
//            // Движущаяся волна
//            float wave =
//                sin(p.x * 4.0 - time * 1.2) * 0.18 +
//                cos(p.x * 8.0 - time * rand) * 0.06;
//
//
//            // Положение границы
//            float edge = wave;
//
//
//            edge -= hideAndShowProgress * 1.5;
//
//
//            // Мягкая граница
//            float softness = 0.08;
//
//            float mask = smoothstep(
//                edge - softness,
//                edge + softness,
//                p.y
//            );
//
//            // Синий цвет
//            float3 blue = float3(0.11, 0.35, 1.0);
//
//            // Темный фон
//            float3 background = float3(0.003, 0.008, 0.02);
//
//            // Всё ниже волны - синий,
//            // всё выше - фон
//            float waveMask = 1.0 - mask;
//
//            float3 color = mix(background, blue, waveMask);
//
//            return half4(color, 1.0);
//        }
//    """.trimIndent()
//        )
//
//        val LightShaderBackground = RuntimeShader(
//            """
//         uniform float2 resolution;
//         uniform float time;
//         uniform float rand;
//         uniform float hideAndShowProgress;
//
//         float random(float2 p)
//         {
//             return fract(
//                 sin(dot(p, float2(12.9898, 78.233)))
//                 * 43758.5453
//             );
//         }
//
//         half4 main(float2 fragCoord)
//         {
//             float time = time * 0.3;
//             float rand = rand / 1.69;
//
//             float2 uv = 1.0 - (fragCoord / resolution);
//
//             // Коррекция пропорций
//             float2 p = uv - rand - 0.15;
//             p.x *= resolution.x / resolution.y;
//
//             // Движущаяся волна
//             float wave =
//                 sin(p.x * 4.0 - time * 1.2) * 0.18 +
//                 cos(p.x * 8.0 - time * rand) * 0.06;
//
//
//             // Положение границы
//             float edge = wave;
//             edge -= hideAndShowProgress * 1.5;
//
//             // Мягкая граница
//             float softness = 0.08;
//
//             float mask = smoothstep(
//                 edge - softness,
//                 edge + softness,
//                 p.y
//             );
//
//             // красный цвет
//             float3 red = float3(1.0, 0.3, 0.3);
//
//             // Светлый фон
//             float3 background = float3(1.0, 1.0, 1.0);
//
//             // Всё ниже волны - синий,
//             // всё выше - фон
//             float waveMask = 1.0 - mask;
//
//             float3 color = mix(background, red, waveMask);
//
//             return half4(color, 1.0);
//         }
//    """.trimIndent()
//        )
//
//
//
//
//
package shaders

import android.graphics.RuntimeShader
import android.os.Build

// Объявление переменных с проверкой поддержки
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


