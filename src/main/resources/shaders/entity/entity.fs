#version 330 core

out vec4 outTexture;

in vec3 vPosition;
in vec4 vColor;
in vec2 vTexture;
in vec3 vNormal;

uniform sampler2D atlas;

uniform vec3 cameraPos;
uniform mat3 normalMatrix;

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
	vec4 tex = texture(atlas, vTexture) * vColor;

	if (tex.a == 0)
		discard;

	vec3 texture = tex.rgb;

	// then calculate lighting as usual
	vec3 lighting = texture * 0.05; // hard-coded ambient component
	vec3 viewDir = normalize(cameraPos - vPosition);

	for (int i = 0; i < LIGHT_COUNT; ++i)
	{
		Light light = lights[i];
		vec3 p = light.position - vPosition;

		vec3 lightDir = normalize(p);
		float theta = dot(lightDir, normalize(-light.spotlight.xyz));
		if (theta > light.spotlight.w)
		{
			float distance = length(p);
			float attenuation = 1.0 / (light.attenuation.x + light.attenuation.y * distance + light.attenuation.z * (distance * distance));

			// diffuse
			vec3 diffuse = max(dot(vNormal, lightDir), 0.0) * light.color;
			lighting += diffuse * attenuation;
		}
	}
	
	lighting = lighting * texture;

	outTexture = vec4(lighting, tex.a);
}