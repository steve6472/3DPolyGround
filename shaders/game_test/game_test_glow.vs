#version 330 core

layout(location = 0) in vec2 position;
layout(location = 1) in vec2 texture;
layout(location = 2) in vec2 foregroundTexture;

out vec2 vTexture;
out vec2 vForegroundTexture;

uniform mat4 projection;
uniform mat4 transformation;
uniform mat4 camera;

void main()
{
    vTexture = texture;
    vForegroundTexture = foregroundTexture;
	
    gl_Position = projection * camera * transformation * vec4(position, 0.0, 1.0);
}