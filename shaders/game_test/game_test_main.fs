#version 330 core

uniform sampler2D sampler;

in vec2 vTexture1;
in vec2 vTexture2;
in vec2 vTexture3;

out vec4 fragColor;

void main()
{
	vec4 tex1 = texture(sampler, vTexture1);
	vec4 tex2 = texture(sampler, vTexture2);
	//vec4 tex3 = texture(sampler, vTexture3);
	fragColor = mix(tex2, tex1, 1 - tex2.a);
}