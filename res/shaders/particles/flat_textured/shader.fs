#version 330 core

in vec4 vColor;
in vec2 vTexture;
uniform sampler2D sampler;

out vec4 fragColor;

void main()
{
	fragColor = texture(sampler, vTexture) - vColor;
}