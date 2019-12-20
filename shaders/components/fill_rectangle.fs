#version 330 core

in vec2 vTex;

out vec4 fragColor;

uniform vec4 fill;

void main()
{
	if (fill.a == 0) discard;
	fragColor = fill;
}