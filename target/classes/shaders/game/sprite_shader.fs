#version 330 core

uniform sampler2D atlas;

in vec2 vTex;
in float vAlpha;

out vec4 fragColor;

void main()
{
	fragColor = texture(atlas, vTex);
	if (fragColor.a > 0)
		fragColor.a = vAlpha;
}