#version 120

uniform float red;
uniform float green;
uniform float blue;
uniform float alpha;

uniform sampler2D DiffuseSampler;


void main() {
    vec2 texcoord = vec2(gl_TexCoord[0]);

    gl_FragColor = vec4(red, green, blue, min(alpha, texture2D(DiffuseSampler, texcoord).a));
}