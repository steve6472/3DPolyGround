#version 330 core

layout(location = 0) out vec4 outTexture;
layout(location = 1) out vec4 outPosition;
layout(location = 2) out vec4 outNormal;
layout(location = 3) out vec4 outEmission;
layout(location = 4) out vec4 outEmissionPos;

in vec4 vColor;
in vec3 pos;
in vec2 tex;
in vec3 nor;

uniform sampler2D albedo;
uniform float emissionToggle;
uniform float shade;

vec2 parallaxMapping(vec2 texCoords, vec3 viewDir);

void main()
{
	vec2 texCoords = vec2(tex.x, tex.y);

	vec4 orig = texture(albedo, texCoords);

	if (orig.a == 0) discard;

	vec4 tex = orig * vColor;

	outTexture = tex;
	outTexture.rgb *= (1.0 - emissionToggle);

	outEmission = tex * emissionToggle;

	outTexture.rgb *= shade;

	vec3 norm = nor;

	outNormal = vec4(norm, 1.0);
	outPosition = vec4(pos, 1.0);
	outEmissionPos = vec4(pos * emissionToggle, 1.0);
}