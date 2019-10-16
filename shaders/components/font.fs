#version 330 core

uniform sampler2D sampler;

uniform vec2 fontSize;

in vec2 gTex;
in vec4 gColor;
in vec2 gData;

out vec4 fragColor;

void main()
{
	vec4 tex = texture(sampler, gTex / fontSize + gData.xy);
	
	tex.rgb = tex.rgb - 1.0 + gColor.rgb;
	tex.a = tex.a - 1.0 + gColor.a;
	
	fragColor = tex;
}