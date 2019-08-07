#version 120

uniform sampler2D DiffuseSampler;
uniform float colorFactor;
varying vec2 texCoord;

void main()
{
    vec4 sample = texture2D(DiffuseSampler, texCoord);
	float grey = 0.21 * sample.r + 0.71 * sample.g + 0.07 * sample.b;

	float blue = 0.5 + 0.5 * sample.b; // sample.b * colorFactor + grey * (1.0 - colorFactor);

	gl_FragColor = vec4(sample.r * colorFactor + grey * (1.0 - colorFactor), sample.g * colorFactor + grey * (1.0 - colorFactor), blue, 1);
}


