#version 330 core

layout(location = 0) in vec3 position;
layout(location = 1) in vec4 color;
layout(location = 2) in vec2 texture;
layout(location = 3) in vec3 light;

uniform mat4 transformation;
uniform mat4 projection;
uniform mat4 view;

out vec4 vColor;
out vec2 vTexture;
out vec4 glPos;
out vec3 vLight;

void main()
{
    vColor = color;
    vTexture = texture;
	vLight = light;

	vec4 worldPosition = transformation * vec4(position, 1.0);

	gl_Position = projection * view * worldPosition;
	glPos = worldPosition;
}
