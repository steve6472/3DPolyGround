#version 330 core

in vec4 clipSpace;

out vec4 fragColor;

uniform sampler2D tex;
uniform vec3 tint;

void main()
{
	if (tint.g == 0.8)
	{
		vec2 ndc = clipSpace.xy / (clipSpace.w - 3) / 2.0 + 0.5;

//		vec2 ndc = vec2((clipSpace.z - 10.0) / 2.0, (clipSpace.y - 1.0) / 2.5);

		fragColor = texture(tex, ndc);
		fragColor.a = 1;
	} else
	{
		vec2 ndc = clipSpace.xy / clipSpace.w / 2.0 + 0.5;

		fragColor = texture(tex, ndc) * vec4(tint, 1);
		fragColor.a = 1;
	}
}