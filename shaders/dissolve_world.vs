#version 330 core

layout(location = 0) in vec3 position;
layout(location = 1) in vec4 color;
layout(location = 2) in vec2 texture;

uniform mat4 transformation;
uniform mat4 projection;
uniform mat4 view;

out vec4 vColor;
out vec2 vTexture;
out vec4 glPos;

void main()
{
    vColor = color;
    vTexture = texture;

	vec4 worldPosition = transformation * vec4(position, 1.0);

	gl_Position = projection * view * worldPosition;
	glPos = worldPosition;
}
