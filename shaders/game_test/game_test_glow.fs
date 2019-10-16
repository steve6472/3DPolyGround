#version 330 core

uniform sampler2D sampler;

in vec2 vTexture;
in vec2 vForegroundTexture;

out vec4 fragColor;

void main()
{
	vec4 text = texture(sampler, vTexture);
	
	vec4 fore = texture(sampler, vForegroundTexture);
	
	fragColor = mix(text, fore, fore.a);
}