#version 330 core

in vec4 vColor;
in vec2 vTexture;

out vec4 fragColor;

uniform sampler2D sampler;
uniform sampler2D noise;
uniform float time;

void main()
{
    float f = texture(noise, vTexture).r;
    fragColor = texture(sampler, vTexture);

    float glowRadius = 0.1f;

    float l = smoothstep(f, f + 0.1, time);

    if (l < 1)
        fragColor *= vec4(vec3(0.1, 0.75, 1), l);
}