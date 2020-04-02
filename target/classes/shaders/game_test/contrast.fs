#version 330 core

in vec2 vTexture;
out vec4 fragColor;
uniform sampler2D sampler;

const float contrast = 0.3;

void main()
{
    fragColor = texture(sampler, vTexture);
    fragColor.rgb = (fragColor.rgb - 0.5) * (1.0 + contrast) + 0.5;
}