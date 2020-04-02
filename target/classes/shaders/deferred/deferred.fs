#version 330 core

out vec4 outTexture;

in vec2 tex;

uniform sampler2D gAlbedo;
uniform sampler2D gPosition;
uniform sampler2D gNormal;
uniform sampler2D gEmission;
uniform sampler2D gEmissionPos;

uniform vec3 cameraPos;

struct Light
{
	vec3 position;
	vec3 color;
	vec3 attenuation;
};

/* LIGHT_COUNT gets replaced on load by light count in LightManager class */
const int LIGHT_COUNT = 0;

uniform Light lights[LIGHT_COUNT];

void main()
{
	vec2 texCoords = vec2(tex.x, tex.y);

	// retrieve data from G-buffer
	vec3 fragPos = texture(gPosition, texCoords).rgb;
	vec3 normal = texture(gNormal, texCoords).rgb;
	vec3 albedo = texture(gAlbedo, texCoords).rgb;
	vec3 emission = texture(gEmission, texCoords).rgb;
	vec3 emissionPos = texture(gEmissionPos, texCoords).rgb;

	vec3 texture = albedo;

	// then calculate lighting as usual
	vec3 lighting = texture * 0.1; // hard-coded ambient component
	vec3 viewDir = normalize(cameraPos - fragPos);

	for (int i = 0; i < LIGHT_COUNT; ++i)
	{
		vec3 p = lights[i].position - fragPos;

		float distance = length(p);
		float attenuation = 1.0 / (lights[i].attenuation.x + lights[i].attenuation.y * distance + lights[i].attenuation.z * (distance * distance));

		// diffuse
		vec3 lightDir = normalize(p);
		vec3 diffuse = max(dot(normal, lightDir), 0.0) * texture * lights[i].color;
		lighting += diffuse * attenuation;
	}

	float emiDistance = length(cameraPos - emissionPos);
	float norDistance = length(cameraPos - fragPos);

	if (length(emissionPos) != 0f && emiDistance <= norDistance)
	{
		outTexture = vec4(lighting, 1.0) + vec4(emission, 1.0);
	} else
	{
		outTexture = vec4(lighting, 1.0);
	}
}