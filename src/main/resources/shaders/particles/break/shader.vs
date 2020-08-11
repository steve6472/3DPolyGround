#version 330 core

layout(location = 0) in vec3 position;
layout(location = 1) in vec4 uv;
layout(location = 2) in vec3 color;
layout(location = 3) in float size;

out vec4 UV;
out float vSize;
out vec3 vColor;

void main()
{
	UV = uv;
	vSize = size;
	vColor = color;
	gl_Position = vec4(position, 1.0);
}