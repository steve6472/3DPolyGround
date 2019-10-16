#version 330 core

in vec2 vTex;

out vec4 fragColor;

uniform vec4 fill;

void main()
{
	fragColor = fill;
}