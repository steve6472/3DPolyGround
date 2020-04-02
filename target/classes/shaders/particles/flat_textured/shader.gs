#version 330 core

layout (points) in;
layout (triangle_strip, max_vertices = 4) out;

in vec4 Color[];
in float Size[];

out vec2 vTexture;
out vec4 vColor;

uniform mat4 transformation;
uniform mat4 projection;
uniform mat4 view;

void addVertex(float ox, float oy, float oz, float colorMul, float u, float v);

void main()
{
	float s = Size[0];

	/* +Y */
	addVertex(-s, 0, s, 0.98823529411764705882352941176471f, 0, 1);
	addVertex(-s, 0, -s, 0.98823529411764705882352941176471f, 0, 0);
	addVertex(s, 0, s, 0.98823529411764705882352941176471f, 1, 1);
	addVertex(s, 0, -s, 0.98823529411764705882352941176471f, 1, 0);
	EndPrimitive();
}

void addVertex(float ox, float oy, float oz, float colorMul, float u, float v)
{
	gl_Position = projection * view * transformation * (vec4(ox, oy, oz, 0.0) + gl_in[0].gl_Position);
	vColor = Color[0] * vec4(colorMul, colorMul, colorMul, 1.0);
	vTexture = vec2(u, v);
	EmitVertex();
}