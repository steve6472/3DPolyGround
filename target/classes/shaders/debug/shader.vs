#version 330 core

layout(location = 0) in vec3 position;

uniform mat4 transformation;
uniform mat4 projection;
uniform mat4 view;

void main()
{
	vec4 worldPosition = transformation * vec4(position, 1.0);

	gl_Position = projection * view * worldPosition;
}