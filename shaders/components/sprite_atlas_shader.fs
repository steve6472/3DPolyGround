#version 330 core

uniform sampler2D sampler;

uniform vec4 spriteData;

in vec2 vTex;

out vec4 fragColor;

void main()
{
	fragColor = texture(sampler, vTex / spriteData.xy + spriteData.zw);
}