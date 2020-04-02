#version 330 core

layout(location = 0) in vec3 position;
layout(location = 1) in vec4 color;
layout(location = 2) in vec2 texture;
layout(location = 3) in vec3 normal;

uniform mat4 transformation;
uniform mat4 projection;
uniform mat4 view;

out vec4 vColor;
out vec3 pos;
out vec2 tex;
out vec3 nor;
out vec3 vie;

void main()
{
    vColor = color;

    vec4 worldPosition = transformation * vec4(position, 1.0);

    pos = worldPosition.xyz;
    tex = texture;
    nor = normal;

    gl_Position = projection * view * worldPosition;
}