#version 330 core

in vec2 vTex;

out vec4 fragColor;

uniform vec4 fill;
uniform float hole;
uniform float softness;

void main()
{  
	vec2 pos = vTex.xy - vec2(0.5);
	float len = length(pos);

	 float f0 = smoothstep(hole, 0.5 - softness, len);
	 float f1 = smoothstep(0.5, 0.5 - softness, len);
	 fragColor = mix(vec4(f0), vec4(f1), 0.5);
}