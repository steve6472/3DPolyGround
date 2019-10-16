#version 330 core

uniform sampler2D sampler;

in vec2 vTex;

out vec4 fragColor;

void main()
{
	fragColor = texture(sampler, vTex);
}