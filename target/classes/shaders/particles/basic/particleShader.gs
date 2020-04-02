#version 330 core

layout (points) in;
layout (triangle_strip, max_vertices = 24) out;

in vec4 Color[];
in float Size[];

out vec4 vColor;

uniform mat4 transformation;
uniform mat4 projection;
uniform mat4 view;

void addVertex(float ox, float oy, float oz, float colorMul, mat4 mat);

void main()
{
	float s = Size[0];

	mat4 mat = projection * view * transformation;

	/* +Y */
	addVertex(-s, s, -s, 1, mat);
	addVertex(-s, s, s, 1, mat);
	addVertex(s, s, -s, 1, mat);
	addVertex(s, s, s, 1, mat);
	EndPrimitive();

	/* +Z */
	addVertex(-s, -s, s, 0.8f, mat);
	addVertex(s, -s, s, 0.8f, mat);
	addVertex(-s, s, s, 0.8f, mat);
	addVertex(s, s, s, 0.8f, mat);
	EndPrimitive();

	/* +X */
	addVertex(s, -s, s, 0.6f, mat);
	addVertex(s, -s, -s, 0.6f, mat);
	addVertex(s, s, s, 0.6f, mat);
	addVertex(s, s, -s, 0.6f, mat);
	EndPrimitive();

    /* -Y */
	addVertex(-s, -s, s, 0.5f, mat);
	addVertex(-s, -s, -s, 0.5f, mat);
	addVertex(s, -s, s, 0.5f, mat);
	addVertex(s, -s, -s, 0.5f, mat);
	EndPrimitive();

	/* -Z */
	addVertex(s, -s, -s, 0.8f, mat);
	addVertex(-s, -s, -s, 0.8f, mat);
	addVertex(s, s, -s, 0.8f, mat);
	addVertex(-s, s, -s, 0.8f, mat);
	EndPrimitive();

	/* -X */
	addVertex(-s, -s, -s, 0.6f, mat);
	addVertex(-s, -s, s, 0.6f, mat);
	addVertex(-s, s, -s, 0.6f, mat);
	addVertex(-s, s, s, 0.6f, mat);
	EndPrimitive();
	
}

void addVertex(float ox, float oy, float oz, float colorMul, mat4 mat)
{
	gl_Position = mat * (vec4(ox, oy, oz, 0.0) + gl_in[0].gl_Position);
	vColor = Color[0] * vec4(colorMul, colorMul, colorMul, 1.0);
	EmitVertex();
}