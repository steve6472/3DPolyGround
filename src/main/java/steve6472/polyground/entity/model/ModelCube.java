package steve6472.polyground.entity.model;

import org.joml.Vector3f;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 22.10.2020
 * Project: CaveGame
 *
 ***********************/
public class ModelCube
{
	Vector3f origin;
	Vector3f rotation;
	Vector3f position;
	Vector3f size;
	Vector3f color;

	public ModelCube(Vector3f origin, Vector3f rotation, Vector3f position, Vector3f size, Vector3f color)
	{
		this.origin = origin;
		this.rotation = rotation;
		this.position = position;
		this.size = size;
		this.color = color;
	}

	public void setOrigin(float x, float y, float z)
	{
		origin.set(x, y, z);
	}

	public void setRotation(float rotX, float rotY, float rotZ)
	{
		rotation.set(rotX, rotY, rotZ);
	}

	public void setPosition(float x, float y, float z)
	{
		position.set(x, y, z);
	}

	public void setSize(float w, float h, float d)
	{
		size.set(w, h, d);
	}

	public void setColor(float r, float g, float b)
	{
		color.set(r, g, b);
	}
}
