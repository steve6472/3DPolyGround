#version 330 core

in vec2 vTex;

out vec4 fragColor;

uniform vec4 fill;
uniform float hole;

void main()
{  
	vec2 pos = vTex.xy - vec2(0.5);
	float len = length(pos);

	float f0 = step(hole, len);
	float f1 = step(0.5, len);

	if (f0 == 1.0 && f1 == 0.0)
	{
	    fragColor = fill;
	}

//	fragColor = vec4(vec3(step(0.1, len), 0, step(0.5, len)), fill.a/* - step(0.5, len)*/);
}