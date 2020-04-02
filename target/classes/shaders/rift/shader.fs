#version 330 core

in vec4 clipSpace;

out vec4 fragColor;

uniform sampler2D tex;
uniform vec3 tint;

void main()
{
	vec2 ndc = clipSpace.xy / clipSpace.w / 2.0 + 0.5;

	fragColor = texture(tex, ndc) * vec4(tint, 1);
	fragColor.a = 1;
}