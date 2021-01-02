#version 330 core

in vec4 vColor;
in vec2 vTexture;
in vec3 vNormal;

uniform sampler2D atlas;
uniform sampler2D normal;
uniform float shade;

out vec4 fragColor;

void main()
{
	vec4 orig = texture(atlas, vTexture);

	if (orig.a == 0) discard;

    const float AMBIENT = 0.5;
    const float XFAC = -0.15;
    const float ZFAC = 0.05;

    float yLight = (1.0 + vNormal.y) * 0.5;
    float light = yLight * (1.0 - AMBIENT) + vNormal.x * vNormal.x * XFAC + vNormal.z * vNormal.z * ZFAC + AMBIENT;

    fragColor = orig * vColor;

    fragColor.rgb = fragColor.rgb * shade * light;
}