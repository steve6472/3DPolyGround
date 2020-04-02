#version 330 core

layout(location = 0) in vec2 position;
layout(location = 1) in vec2 texture1;
layout(location = 2) in vec2 texture2;
layout(location = 3) in vec2 texture3;

out vec2 vTexture1;
out vec2 vTexture2;
out vec2 vTexture3;

uniform mat4 projection;
uniform mat4 transformation;
uniform mat4 camera;

void main()
{
    vTexture1 = texture1;
    vTexture2 = texture2;
    vTexture3 = texture3;
    gl_Position = projection * camera * transformation * vec4(position, 0.0, 1.0);
}