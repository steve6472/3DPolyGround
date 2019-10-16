#version 330 core

layout (points) in;
layout (triangle_strip, max_vertices = 24) out;

in vec4 Color[];
in float Size[];

out vec4 vColor;

uniform mat4 transformation;
uniform mat4 projection;
uniform mat4 view;

void addVertex(float ox, float oy, float oz, float colorMul);

void main()
{
	float s = Size[0];

	/* +Y */
	addVertex(-s, s, -s, 0.98823529411764705882352941176471f);
	addVertex(-s, s, s, 0.98823529411764705882352941176471f);
	addVertex(s, s, -s, 0.98823529411764705882352941176471f);
	addVertex(s, s, s, 0.98823529411764705882352941176471f);
	EndPrimitive();

	/* +Z */
	addVertex(-s, -s, s, 0.7921568627450980392156862745098f);
	addVertex(s, -s, s, 0.7921568627450980392156862745098f);
	addVertex(-s, s, s, 0.7921568627450980392156862745098f);
	addVertex(s, s, s, 0.7921568627450980392156862745098f);
	EndPrimitive();

	/* +X */
	addVertex(s, -s, s, 0.5921568627450980392156862745098f);
	addVertex(s, -s, -s, 0.5921568627450980392156862745098f);
	addVertex(s, s, s, 0.5921568627450980392156862745098f);
	addVertex(s, s, -s, 0.5921568627450980392156862745098f);
	EndPrimitive();

    /* -Y */
	addVertex(-s, -s, s, 0.49019607843137254901960784313725f);
	addVertex(-s, -s, -s, 0.49019607843137254901960784313725f);
	addVertex(s, -s, s, 0.49019607843137254901960784313725f);
	addVertex(s, -s, -s, 0.49019607843137254901960784313725f);
	EndPrimitive();

	/* -Z */
	addVertex(s, -s, -s, 0.7921568627450980392156862745098f);
	addVertex(-s, -s, -s, 0.7921568627450980392156862745098f);
	addVertex(s, s, -s, 0.7921568627450980392156862745098f);
	addVertex(-s, s, -s, 0.7921568627450980392156862745098f);
	EndPrimitive();

	/* -X */
	addVertex(-s, -s, -s, 0.5921568627450980392156862745098f);
	addVertex(-s, -s, s, 0.5921568627450980392156862745098f);
	addVertex(-s, s, -s, 0.5921568627450980392156862745098f);
	addVertex(-s, s, s, 0.5921568627450980392156862745098f);
	EndPrimitive();
	
}

void addVertex(float ox, float oy, float oz, float colorMul)
{
	gl_Position = projection * view * transformation * (vec4(ox, oy, oz, 0.0) + gl_in[0].gl_Position);
	vColor = Color[0] * vec4(colorMul, colorMul, colorMul, 1.0);
	EmitVertex();
}