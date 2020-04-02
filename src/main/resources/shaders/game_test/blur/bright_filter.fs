#version 330 core

in vec2 vTexture;
out vec4 fragColor;
uniform sampler2D sampler;

void main()
{
    vec4 color = texture(sampler, vTexture);
    float brighness = (color.r * 0.2126) + (color.g * 0.7152) + (color.b * 0.0722);
    fragColor = color * brighness;
}