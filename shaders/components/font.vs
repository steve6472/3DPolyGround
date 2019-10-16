#version 330 core

layout(location = 0) in vec2 position;
layout(location = 1) in vec4 color;
layout(location = 2) in vec2 data;

uniform mat4 transformation;
uniform mat4 projection;

out vec4 vColor;
out vec2 vData;

void main()
{
	vColor = color;
	vData = data;
	
    gl_Position = vec4(position, 1.0, 1.0);
}