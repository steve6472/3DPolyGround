#version 330 core

layout(location = 0) in vec2 position;
layout(location = 1) in vec2 texture;

uniform mat4 transformation;
uniform mat4 projection;

out vec2 vTex;

void main()
{
	vTex = texture;

    gl_Position = projection * transformation * vec4(position, 1.0, 1.0);
}