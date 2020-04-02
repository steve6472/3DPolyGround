#version 330 core

layout(location = 0) in vec2 position;

out vec2 blurTextureCoords[5];

void main()
{
    gl_Position = vec4(position, 0.0, 1.0);
	
	float pixelSize = 1.0 / (9 * 60);
	vec2 center = position * 0.5 + 0.5;
	
	for(int i = -2; i <= 2; i++)
	{
		blurTextureCoords[i + 2] = center + vec2(0.0, pixelSize * i);
	}
}