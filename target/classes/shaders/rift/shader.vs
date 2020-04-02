#version 330 core

layout(location = 0) in vec3 position;

uniform mat4 transformation;
uniform mat4 projection;
uniform mat4 view;

out vec4 clipSpace;

void main()
{
	clipSpace = projection * view * transformation * vec4(position, 1.0);
	gl_Position = clipSpace;
}