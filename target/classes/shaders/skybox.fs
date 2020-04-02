#version 330 core

out vec4 fragColor;

in vec3 texCoords;

uniform samplerCube skybox;
uniform float shade;

void main()
{
    fragColor = texture(skybox, texCoords);
    fragColor.rgb *= shade;
}