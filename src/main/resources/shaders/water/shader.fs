#version 330 core

in vec2 vTexture;

uniform sampler2D water;

out vec4 fragColor;

void main()
{
	vec4 tex = texture(water, vTexture);
	if (tex.a == 1)
		fragColor = vec4(tex.rgb, 0.7);
}