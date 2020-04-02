#version 330 core

uniform sampler2D sampler0;
uniform sampler2D sampler1;

in vec2 vTexture;

out vec4 fragColor;

void main()
{
	vec4 tex0 = texture(sampler0, vTexture);
	
	vec4 tex1 = texture(sampler1, vTexture);
	
	fragColor = tex0 + tex1;
}