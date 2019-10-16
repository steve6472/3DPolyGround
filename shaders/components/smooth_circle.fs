#version 330 core

in vec2 vTex;

out vec4 fragColor;

uniform vec4 fill;
uniform float softness;

void main()
{  
	vec2 pos = vTex.xy - vec2(0.5);
	float len = length(pos);
	
	fragColor = vec4(fill.rgb, smoothstep(0.5, 0.5 - softness, len) - (1 - fill.a));
}