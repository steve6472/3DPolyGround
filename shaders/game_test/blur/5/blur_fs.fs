#version 330 core

uniform sampler2D sampler;

in vec2 blurTextureCoords[5];

out vec4 fragColor;

void main()
{
	fragColor = vec4(0.0);
	fragColor += texture(sampler, blurTextureCoords[0]) * 0.153388;
    fragColor += texture(sampler, blurTextureCoords[1]) * 0.221461;
    fragColor += texture(sampler, blurTextureCoords[2]) * 0.250301;
    fragColor += texture(sampler, blurTextureCoords[3]) * 0.221461;
    fragColor += texture(sampler, blurTextureCoords[4]) * 0.153388;
	
}