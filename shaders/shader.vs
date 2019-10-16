#version 330 core

layout(location = 0) in vec3 position;
layout(location = 1) in vec4 color;
layout(location = 2) in vec3 normal;

uniform mat4 transformation;
uniform mat4 projection;
uniform mat4 view;
uniform vec3 lightPosition;

out vec4 vColor;
out vec3 surfaceNormal;
out vec3 toLightVector;
out vec3 toCameraVector;

void main()
{
    vColor = color;
	
	vec4 worldPosition = transformation * vec4(position, 1.0);
	/*
	surfaceNormal = (transformation * vec4(normal, 0.0)).xyz;
	
	toLightVector = lightPosition - worldPosition.xyz;
	
	toCameraVector = (inverse(view) * vec4(0.0, 0.0, 0.0, 1.0)).xyz - worldPosition.xyz;*/
	
	gl_Position = projection * view * worldPosition;
}