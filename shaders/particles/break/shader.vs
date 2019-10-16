#version 330 core

layout(location = 0) in vec3 position;
layout(location = 1) in vec4 uv;
layout(location = 2) in float size;

out vec4 UV;
out float Size;

void main()
{
	UV = uv;
	Size = size;
	gl_Position = vec4(position, 1.0);
}