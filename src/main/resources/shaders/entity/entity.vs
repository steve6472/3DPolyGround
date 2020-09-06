#version 330 core

layout(location = 0) in vec3 position;
layout(location = 1) in vec4 color;
layout(location = 2) in vec2 texture;
layout(location = 3) in vec3 normal;

uniform mat4 transformation;
uniform mat3 normalMatrix;
uniform mat4 projection;
uniform mat4 view;

out vec3 vPosition;
out vec4 vColor;
out vec2 vTexture;
out vec3 vNormal;

void main()
{
    vColor = color;
    vTexture = texture;

    vec4 worldPosition = transformation * vec4(position, 1.0);
    vPosition = worldPosition.xyz;

    vNormal = normalize(normalMatrix * normal);

    gl_Position = projection * view * worldPosition;

}