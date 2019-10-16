#version 330 core

layout (points) in;
layout (triangle_strip, max_vertices = 24) out;

in vec4 UV[];
in float Size[];

out vec2 vTexture;
out vec4 vColor;

uniform mat4 transformation;
uniform mat4 projection;
uniform mat4 view;

uniform sampler2D sampler;

void addVertex(float ox, float oy, float oz, mat4 mat);

void main()
{
	float s = Size[0];
	float minU = UV[0].x;
	float maxU = UV[0].y;
	float minV = UV[0].z;
	float maxV = UV[0].w;

	vec4 color = texture(sampler, vec2(minU, minV));

	if (color.a == 0) return;

	float colorMul = 1.0f;
	mat4 mat = projection * view * transformation;

	/* +Y */
	vColor = color * vec4(colorMul, colorMul, colorMul, 1.0);
	addVertex(-s, s, -s, mat);
	addVertex(-s, s, s, mat);
	addVertex(s, s, -s, mat);
	addVertex(s, s, s, mat);
	EndPrimitive();

	/* +Z */
	colorMul = 0.8f;
	vColor = color * vec4(colorMul, colorMul, colorMul, 1.0);
	addVertex(-s, -s, s, mat);
	addVertex(s, -s, s, mat);
	addVertex(-s, s, s, mat);
	addVertex(s, s, s, mat);
	EndPrimitive();

	/* +X */
	colorMul = 0.6f;
	vColor = color * vec4(colorMul, colorMul, colorMul, 1.0);
	addVertex(s, -s, s, mat);
	addVertex(s, -s, -s, mat);
	addVertex(s, s, s, mat);
	addVertex(s, s, -s, mat);
	EndPrimitive();

    /* -Y */
	colorMul = 0.5f;
	vColor = color * vec4(colorMul, colorMul, colorMul, 1.0);
	addVertex(-s, -s, s, mat);
	addVertex(-s, -s, -s, mat);
	addVertex(s, -s, s, mat);
	addVertex(s, -s, -s, mat);
	EndPrimitive();

	/* -Z */
	colorMul = 0.8f;
	vColor = color * vec4(colorMul, colorMul, colorMul, 1.0);
	addVertex(s, -s, -s, mat);
	addVertex(-s, -s, -s, mat);
	addVertex(s, s, -s, mat);
	addVertex(-s, s, -s, mat);
	EndPrimitive();

	/* -X */
	colorMul = 0.6f;
	vColor = color * vec4(colorMul, colorMul, colorMul, 1.0);
	addVertex(-s, -s, -s, mat);
	addVertex(-s, -s, s, mat);
	addVertex(-s, s, -s, mat);
	addVertex(-s, s, s, mat);
	EndPrimitive();
}

void addVertex(float ox, float oy, float oz, mat4 mat)
{
	gl_Position = mat * (vec4(ox, oy, oz, 0.0) + gl_in[0].gl_Position);
	EmitVertex();
}