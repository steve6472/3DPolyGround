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

void addVertex(float ox, float oy, float oz, float colorMul, float u, float v);

void main()
{
	float s = Size[0];
	float minU = UV[0].x;
	float maxU = UV[0].y;
	float minV = UV[0].z;
	float maxV = UV[0].w;

	vec4 color = texture(sampler, vec2(minU, minV));

	/* +Y */
	addVertex(-s, s, -s, 0.98823529411764705882352941176471f, minU, maxV);
	addVertex(-s, s, s, 0.98823529411764705882352941176471f, minU, minV);
	addVertex(s, s, -s, 0.98823529411764705882352941176471f, maxU, maxV);
	addVertex(s, s, s, 0.98823529411764705882352941176471f, maxU, minV);
	EndPrimitive();

	/* +Z */
	addVertex(-s, -s, s, 0.7921568627450980392156862745098f, minU, maxV);
	addVertex(s, -s, s, 0.7921568627450980392156862745098f, maxU, maxV);
	addVertex(-s, s, s, 0.7921568627450980392156862745098f, minU, minV);
	addVertex(s, s, s, 0.7921568627450980392156862745098f, maxU, minV);
	EndPrimitive();

	/* +X */
	addVertex(s, -s, s, 0.5921568627450980392156862745098f, minU, maxV);
	addVertex(s, -s, -s, 0.5921568627450980392156862745098f, maxU, maxV);
	addVertex(s, s, s, 0.5921568627450980392156862745098f, minU, minV);
	addVertex(s, s, -s, 0.5921568627450980392156862745098f, maxU, minV);
	EndPrimitive();

    /* -Y */
	addVertex(-s, -s, s, 0.49019607843137254901960784313725f, minU, maxV);
	addVertex(-s, -s, -s, 0.49019607843137254901960784313725f, minU, minV);
	addVertex(s, -s, s, 0.49019607843137254901960784313725f, maxU, maxV);
	addVertex(s, -s, -s, 0.49019607843137254901960784313725f, maxU, minV);
	EndPrimitive();

	/* -Z */
	addVertex(s, -s, -s, 0.7921568627450980392156862745098f, minU, maxV);
	addVertex(-s, -s, -s, 0.7921568627450980392156862745098f, maxU, maxV);
	addVertex(s, s, -s, 0.7921568627450980392156862745098f, minU, minV);
	addVertex(-s, s, -s, 0.7921568627450980392156862745098f, maxU, minV);
	EndPrimitive();

	/* -X */
	addVertex(-s, -s, -s, 0.5921568627450980392156862745098f, minU, maxV);
	addVertex(-s, -s, s, 0.5921568627450980392156862745098f, maxU, maxV);
	addVertex(-s, s, -s, 0.5921568627450980392156862745098f, minU, minV);
	addVertex(-s, s, s, 0.5921568627450980392156862745098f, maxU, minV);
	EndPrimitive();
}

void addVertex(float ox, float oy, float oz, float colorMul, float u, float v)
{
	gl_Position = projection * view * transformation * (vec4(ox, oy, oz, 0.0) + gl_in[0].gl_Position);
	vColor = vec4(colorMul, colorMul, colorMul, 1.0);
	vTexture = vec2(u, v);
	EmitVertex();
}