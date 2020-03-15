#version 330 core

in vec4 vColor;
in vec2 vTexture;

uniform sampler2D atlas;
uniform float shade;

out vec4 fragColor;

void main()
{
	vec4 orig = texture(atlas, vTexture);

	if (orig.a == 0) discard;

    fragColor = orig * vColor;

    fragColor.rgb = fragColor.rgb * shade;
}