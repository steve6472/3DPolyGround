#version 330 core

in vec4 vColor;
in vec3 surfaceNormal;
in vec3 toLightVector;
in vec3 toCameraVector;

uniform float maxLight;
uniform vec3 lightColor;
uniform float shineDamper;
uniform float reflectivity;

out vec4 fragColor;

void main()
{/*
	vec3 unitNormal = normalize(surfaceNormal);
	vec3 unitLightVector = normalize(toLightVector);
	
	float nDot = dot(unitNormal, unitLightVector);
	float brightness = max(nDot, 0.0);
	vec3 diffuse = brightness * lightColor;
	
	vec3 unitVectorToCamera = normalize(toCameraVector);
	vec3 lightDirection = -unitLightVector;
	vec3 reflectedLightDirection = reflect(lightDirection, unitNormal);
	
	float specularFactor = dot(reflectedLightDirection, unitVectorToCamera);
	specularFactor = max(specularFactor, 0.0);
	float dampedFactor = pow(specularFactor, shineDamper);
	vec3 finalSpecular = dampedFactor * reflectivity * lightColor;
	
	diffuse = max(diffuse, maxLight);

	fragColor = vec4(diffuse, 1.0) * vColor + vec4(finalSpecular, vColor.w);*/
	fragColor = vColor;
}