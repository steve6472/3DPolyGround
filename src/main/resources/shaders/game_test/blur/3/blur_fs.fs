#version 330 core

uniform sampler2D sampler;

in vec2 blurTextureCoords[3];

out vec4 fragColor;

void main()
{
	fragColor = vec4(0.0);
	fragColor += texture(sampler, blurTextureCoords[0]) * 0.319466;
    fragColor += texture(sampler, blurTextureCoords[1]) * 0.361069;
    fragColor += texture(sampler, blurTextureCoords[2]) * 0.319466;
	
}