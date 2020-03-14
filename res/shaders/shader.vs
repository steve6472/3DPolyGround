#version 330 core

layout(location = 0) in vec3 position;
layout(location = 1) in vec4 color;

uniform mat4 transformation;
uniform mat4 projection;
uniform mat4 view;

out vec4 vColor;

void main()
{
    vColor = color;
	vec4 worldPosition = transformation * vec4(position, 1.0);

	gl_Position = projection * view * worldPosition;
}