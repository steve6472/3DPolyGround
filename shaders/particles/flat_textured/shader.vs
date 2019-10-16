#version 330 core

layout(location = 0) in vec3 position;
layout(location = 1) in vec4 color;
layout(location = 2) in float size;

out vec4 Color;
out float Size;

void main()
{
	Color = color;
	Size = size;
	gl_Position = vec4(position, 1.0);
}