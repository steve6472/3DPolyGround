#version 330 core

#include shaders/simplex.glsl

in vec4 vColor;
in vec2 vTexture;
in vec4 glPos;

uniform sampler2D atlas;
uniform float noiseResolution;
uniform float time;
uniform float shade;

out vec4 fragColor;

const float glowRadius = 0.05;
const vec3 someColor = vec3(0.1, 0.75, 1);

void main()
{
	vec4 orig = texture(atlas, vTexture);

	if (orig.a == 0) discard;

    if (time < 1)
    {
		float f = simplex3d_fractal(glPos.xyz / noiseResolution);
        f = 0.5 + f * 0.5;
        fragColor = orig;

        float l = smoothstep(f, f + glowRadius, time);
        float k = smoothstep(f + glowRadius, f + glowRadius * 2.0, time);

        if (l < 1.0)
            fragColor *= vec4(someColor, l);
        else
        {
            fragColor.r = orig.r * (1.0 - (0.90 * (1.0 - k)));
            fragColor.g = orig.g * (1.0 - (0.25 * (1.0 - k)));
            fragColor.b = orig.b * (1.0 - (0.00 * (1.0 - k)));
        }

        fragColor *= vColor;
    } else
    {
        fragColor = orig * vColor;
    }

    fragColor.rgb = fragColor.rgb * shade;
}