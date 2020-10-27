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
	vec4 spotlight;
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
	vec3 lighting = vec3(2); // hard-coded ambient component
	vec3 viewDir = normalize(cameraPos - fragPos);

	const float AMBIENT = 0.5;
	const float XFAC = -0.15;
	const float ZFAC = 0.05;

	float yLight = (1.0 + normal.y) * 0.5;
	float light = yLight * (1.0 - AMBIENT) + normal.x * normal.x * XFAC + normal.z * normal.z * ZFAC + AMBIENT;

	for (int i = 0; i < LIGHT_COUNT; ++i)
	{
		Light light = lights[i];
		vec3 p = light.position - fragPos;

		vec3 lightDir = normalize(p);
		float theta = dot(lightDir, normalize(-light.spotlight.xyz));
		if (theta > light.spotlight.w)
		{
			float distance = length(p);
			float attenuation = 1.0 / (light.attenuation.x + light.attenuation.y * distance + light.attenuation.z * (distance * distance));

			// diffuse
			vec3 diffuse = max(dot(normal, lightDir), 0.0) * light.color;
			lighting += diffuse * attenuation;
		}
	}

	lighting = (lighting - 1) * texture * light;

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