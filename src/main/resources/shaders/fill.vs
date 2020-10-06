#version 330 core

layout(location = 0) in vec2 position;
layout(location = 1) in vec4 color;

uniform mat4 transformation;
uniform mat4 projection;

out vec4 vCol;

void main()
{
	vCol = color;
    gl_Position = projection * transformation * vec4(position, 1.0, 1.0);
}