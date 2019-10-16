#version 330 core

layout (points) in;
layout (triangle_strip, max_vertices = 6) out;

//in vec2 vTex[]; It is not needed as it is crated in this shader
in vec4 vColor[];
in vec2 vData[];

out vec2 gTex;
out vec4 gColor;
out vec2 gData;

uniform mat4 transformation;
uniform mat4 projection;

void addVertex(vec2 offset, vec2 tex, mat4 mat);

void main()
{
	mat4 mat = projection * transformation;

	addVertex(vec2(-1, +1), vec2(0, 1), mat);
	addVertex(vec2(-1, -1), vec2(0, 0), mat);
	addVertex(vec2(+1, -1), vec2(1, 0), mat);
	EndPrimitive();

	addVertex(vec2(+1, -1), vec2(1, 0), mat);
	addVertex(vec2(+1, +1), vec2(1, 1), mat);
	addVertex(vec2(-1, +1), vec2(0, 1), mat);
	EndPrimitive();
}

void addVertex(vec2 offset, vec2 tex, mat4 mat)
{
	gl_Position = mat * (vec4(offset, 0.0, 0.0) + gl_in[0].gl_Position);
	gTex = tex;
	gColor = vColor[0];
	gData = vData[0];
	EmitVertex();
}