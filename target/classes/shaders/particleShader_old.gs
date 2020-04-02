#version 330 core

layout (points) in;
layout (triangle_strip, max_vertices = 24) out;

in vec4 Color[];
in float Size[];

out vec4 vColor;

uniform mat4 transformation;
uniform mat4 projection;
uniform mat4 view;

void addVertex(vec4 offset);

void main()
{
	float s = Size[0];

	addVertex(vec4(-s, -s, s, 0.0));
	addVertex(vec4(-s, -s, -s, 0.0));
	addVertex(vec4(s, -s, s, 0.0));
	addVertex(vec4(s, -s, -s, 0.0));
	EndPrimitive();
	
	addVertex(vec4(-s, s, s, 0.0));
	addVertex(vec4(-s, s, -s, 0.0));
	addVertex(vec4(s, s, s, 0.0));
	addVertex(vec4(s, s, -s, 0.0));
	EndPrimitive();
	
	addVertex(vec4(-s, -s, s, 0.0));
	addVertex(vec4(s, -s, s, 0.0));
	addVertex(vec4(-s, s, s, 0.0));
	addVertex(vec4(s, s, s, 0.0));
	EndPrimitive();
	
	addVertex(vec4(s, -s, s, 0.0));
	addVertex(vec4(s, -s, -s, 0.0));
	addVertex(vec4(s, s, s, 0.0));
	addVertex(vec4(s, s, -s, 0.0));
	EndPrimitive();
	
	addVertex(vec4(-s, -s, -s, 0.0));
	addVertex(vec4(s, -s, -s, 0.0));
	addVertex(vec4(-s, s, -s, 0.0));
	addVertex(vec4(s, s, -s, 0.0));
	EndPrimitive();
	
	addVertex(vec4(-s, -s, s, 0.0));
	addVertex(vec4(-s, -s, -s, 0.0));
	addVertex(vec4(-s, s, s, 0.0));
	addVertex(vec4(-s, s, -s, 0.0));
	EndPrimitive();
	
}

void addVertex(vec4 offset)
{
	gl_Position = projection * view * transformation * (offset + gl_in[0].gl_Position);
	vColor = Color[0];
	EmitVertex();
}