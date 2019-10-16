#version 330 core

in vec2 vTex;

out vec4 fragColor;

uniform vec4 fill;

void main()
{  
	vec2 pos = vTex.xy - vec2(0.5);
	float len = length(pos);
	
	fragColor = vec4(fill.rgb, fill.a - step(0.5, len));
}