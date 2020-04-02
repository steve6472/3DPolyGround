#version 330 core

uniform sampler2D sampler;

in vec2 blurTextureCoords[11];

out vec4 fragColor;

void main()
{
	fragColor = vec4(0.0);
	fragColor += texture(sampler, blurTextureCoords[0]) * 0.0093;
    fragColor += texture(sampler, blurTextureCoords[1]) * 0.028002;
    fragColor += texture(sampler, blurTextureCoords[2]) * 0.065984;
    fragColor += texture(sampler, blurTextureCoords[3]) * 0.121703;
    fragColor += texture(sampler, blurTextureCoords[4]) * 0.175713;
    fragColor += texture(sampler, blurTextureCoords[5]) * 0.198596;
    fragColor += texture(sampler, blurTextureCoords[6]) * 0.175713;
    fragColor += texture(sampler, blurTextureCoords[7]) * 0.121703;
    fragColor += texture(sampler, blurTextureCoords[8]) * 0.065984;
    fragColor += texture(sampler, blurTextureCoords[9]) * 0.028002;
    fragColor += texture(sampler, blurTextureCoords[10]) * 0.0093;
	
}