#version 120

uniform sampler2D DiffuseSampler;

varying vec2 texCoord;
varying vec2 oneTexel;

void main() {
    vec4 blurred = vec4(0.0);
    float totalStrength = 0.0;
    float totalAlpha = 0.0;
    float totalSamples = 0.0;
    float Radius = 10.0;

    vec4 sample = vec4(0.0);

    for(float r = -Radius; r <= Radius; r += 1.0) {
         sample = texture2D(DiffuseSampler, texCoord + oneTexel * r * vec2(1, 0));

         totalAlpha = totalAlpha + sample.a;
         totalSamples = totalSamples + 1.0;

         blurred.rgb = blurred.rgb + sample.rgb*sample.a;
         blurred.a = blurred.a + sample.a;
    }

    for(float r = -Radius; r <= Radius; r += 1.0) {
        sample = texture2D(DiffuseSampler, texCoord + oneTexel * r * vec2(0, 1));

        totalAlpha = totalAlpha + sample.a;
        totalSamples = totalSamples + 1.0;

        blurred.rgb = blurred.rgb + sample.rgb*sample.a;
        blurred.a = blurred.a + sample.a;
    }

    gl_FragColor = vec4(blurred.rgb / totalAlpha, totalAlpha / totalSamples);

    //gl_FragColor = vec4(texture2D(DiffuseSampler, texCoord).rgb * 0.8, 1.0);
}
