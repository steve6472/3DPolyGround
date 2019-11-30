package com.steve6472.polyground.rift;

import org.joml.Vector3f;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import static com.steve6472.sge.gfx.VertexObjectCreator.*;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 29.11.2019
 * Project: SJP
 *
 ***********************/
public class RiftModel
{
	private List<Vector3f> vertices;
	private int vao, positionVbo;

	public RiftModel()
	{
		vertices = new ArrayList<>();
	}

	public RiftModel(List<Vector3f> vertices)
	{
		this.vertices = vertices;
	}

	public void addVertex(Vector3f ver)
	{
		vertices.add(ver);
	}

	public void setupModel()
	{
		vao = createVAO();
		positionVbo = storeFloatDataInAttributeList(0, 3, new float[] {-1, 0, 1, -1, 0, -1, 1, 0, -1});
	}

	public void deleteModel()
	{
		delete(vao, positionVbo);
	}

	public void updateModel()
	{
		bindVAO(vao);
		storeFloatDataInAttributeList(0, 3, positionVbo, createBuffer());
		unbindVAO();
	}

	private FloatBuffer createBuffer()
	{
		FloatBuffer buff = BufferUtils.createFloatBuffer(vertices.size() * 3);

		for (Vector3f vec : vertices)
		{
			buff.put(vec.x);
			buff.put(vec.y);
			buff.put(vec.z);
		}

		buff.flip();

		return buff;
	}

	public int getVao()
	{
		return vao;
	}

	public int getVertCount()
	{
		return vertices.size();
	}

	public List<Vector3f> getVertices()
	{
		return vertices;
	}

	public void setVertices(List<Vector3f> vertices)
	{
		this.vertices = vertices;
	}
}
