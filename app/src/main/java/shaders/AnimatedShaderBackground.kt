package shaders

import android.graphics.RuntimeShader
import android.os.Build
val ShaderBackground: RuntimeShader? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
    RuntimeShader("""
uniform float2 resolution;
uniform float time; 
uniform float rand;
uniform float transitionProgress; 
uniform float inDarkTheme;
uniform float hideAndShowProgress;


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

    float t = time * 0.5; 

    float3 background;
    
    if(inDarkTheme > 0.0){
        background = float3(0.003, 0.008, 0.02);
    }
    else{
        background = float3(1.0, 1.0, 1.0);
    }
    // PROGRESS AND MORPHING
    float morph = transitionProgress * transitionProgress * (3.0 - 2.0 * transitionProgress);

    // WAVE
    float wave = sin(p.x * 4.0 - t * 1.2) * 0.18 + cos(p.x * 8.0 - t * rand) * 0.06;
    wave += sin(p.x * 2.0 + t * 0.8) * cos(p.x * 5.0 - t * 0.5) * 0.025;

    // WAVE SETUP
    float waveY = mix(wave, 0.0, morph) - hideAndShowProgress;
    float verticalPull = p.y * mix(1.0, 0.35, morph);

    // Values for the wave gradient (taken before the sphere Y shift below)
    float waveDepth = waveY - p.y;  // distance below the wave's top edge
    float screenY = p.y;            // vertical position on screen (-0.5 = bottom edge)

    p.y = p.y - 0.05; // sphere Y position
    // SPHERE GEOMETRY
    float radius = 0.16;
    float waveField = verticalPull - waveY;
    float radialField = length(p) - radius;
    
    // Shape transition from wave to sphere
    float shapeField = mix(waveField, radialField, morph);

    // The sphere gets sharper as it rounds out
    float softness = mix(0.15, 0.018, morph);

    float shapeMask = 1.0 - smoothstep(-softness, softness, shapeField);

    // EDGE GATHERING
    float gather = smoothstep(0.15, 0.85, morph);
    float edgeGather = smoothstep(0.12, 0.75, abs(p.x));
    float gatheredMask = mix(1.0, 1.0 - edgeGather, gather * 0.85);

    shapeMask *= gatheredMask;

    // LIQUID MOTION
    float2 q = p;
    q += 0.035 * float2(sin(p.y * 8.0 + t * 3.0), cos(p.x * 7.0 - t * 2.5));
    q += 0.018 * float2(sin(p.y * 17.0 - t * 2.0), cos(p.x * 13.0 + t * 1.7));
    q.x *= mix(0.35, 1.0, morph);

    // COLOR BLOBS INSIDE THE SHAPE
    float2 c1 = float2(0.18 * sin(t * 1.3), 0.18 * cos(t * 1.1));
    float2 c2 = float2(0.20 * cos(t * 0.9 + 2.0), 0.17 * sin(t * 1.2 + 1.0));
    float2 c3 = float2(0.16 * sin(t * 1.1 + 4.0), 0.22 * cos(t * 0.8 + 3.0));
    float2 c4 = float2(0.22 * cos(t * 0.7 + 5.0), 0.16 * sin(t * 1.4 + 2.0));

    float b1 = blob(q, c1, 0.42);
    float b2 = blob(q, c2, 0.40);
    float b3 = blob(q, c3, 0.38);
    float b4 = blob(q, c4, 0.40);

    // SHAPE COLORS
    float3 pink   = float3(0.6, 0.08, 0.55);
    float3 purple = float3(0.25, 0.08, 0.3);
    float3 blue   = float3(0.02, 0.35, 0.2);
    float3 cyan   = float3(0.0, 0.4, 0.25);

    float3 sphereColor = float3(0.015, 0.008, 0.005);
    sphereColor += b1 * pink;
    sphereColor += b2 * blue;
    sphereColor += b3 * purple;
    sphereColor += b4 * cyan;

    // HIGHLIGHT SPECTRUM
    float spectrum = sin(q.x * 11.0 + q.y * 9.0 + t * 4.0 + sin(q.y * 8.0 + t));
    spectrum = spectrum * 0.5 + 0.5;

    float3 rainbow = float3(
        0.5 + 0.5 * sin(spectrum * 6.283 + 0.0),
        0.5 + 0.5 * sin(spectrum * 6.283 + 2.094),
        0.5 + 0.5 * sin(spectrum * 6.283 + 4.188)
    );

    sphereColor = mix(sphereColor, sphereColor + rainbow * 0.18, 0.30);

    // HIGHLIGHTS AND GLOW
    float sphereDetails = smoothstep(0.45, 0.95, morph);

    float2 lightPos = float2(-0.18 + sin(t) * 0.08, 0.20 + cos(t * 0.8) * 0.06);
    float highlight = exp(-length(p - lightPos) * 18.0);

    float2 light2 = float2(0.20 + cos(t * 0.7) * 0.05, -0.16 + sin(t * 0.9) * 0.05);
    float highlight2 = exp(-length(p - light2) * 25.0);

    sphereColor += highlight * float3(1.0, 0.85, 1.0) * 0.45 * sphereDetails;
    sphereColor += highlight2 * float3(0.4, 0.8, 1.0) * 0.30 * sphereDetails;

    // SPHERE EDGE (RIM)
    float dist = length(p);
    float rim = smoothstep(radius - 0.07, radius, dist);
    sphereColor += rim * float3(0.35, 0.25, 0.95) * 0.55 * sphereDetails;

    float innerGlow = 1.0 - smoothstep(0.0, radius, dist);
    sphereColor += innerGlow * 0.05 * sphereDetails;

    // COLOR BLENDING AND FINAL OUTPUT
    float3 waveColor;
    
    
    
    float g = clamp(waveDepth / 0.5, 0.0, 1.0);
    // While morphing into the sphere the gradient flattens to light blue,
    // so the shrinking shape stays visible before it cross-fades into the sphere
    g = mix(g, 1.0, morph);

    float3 skyBlue  = float3(0.04, 0.56, 1.0);
    float3 glowCyan = float3(0.0, 0.70, 1.0);
    float3 grad;

    if (inDarkTheme > 0.0) {
        // Dark theme: black -> deep blue -> light blue
        float3 deepBlue = float3(0.02, 0.31, 0.48);

        grad = mix(background, deepBlue, smoothstep(0.1, 0.75, g));
        grad = mix(grad, skyBlue, smoothstep(0.6, 1.0, g));
    } else {
        // Light theme: white -> pale blue -> light blue (no dark middle tone)
        float3 paleBlue = float3(0.62, 0.84, 1.0);

        grad = mix(background, paleBlue, smoothstep(0.05, 0.7, g));
        grad = mix(grad, skyBlue, smoothstep(0.55, 1.0, g));
    }

    // Thin bright glow along the bottom edge of the screen
    float bottomGlow = (1.0 - smoothstep(-0.5, -0.44, screenY)) * g;
    grad = mix(grad, glowCyan, bottomGlow * 0.8);

    waveColor = grad;
    
    
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