#version 330 core

in vec4 vColor;
in vec2 vTexture;

uniform sampler2D atlas;

out vec4 fragColor;

void main()
{
	vec4 t = texture(atlas, vTexture);

	if (t.a == 0) discard;

	fragColor = t * vColor;
}