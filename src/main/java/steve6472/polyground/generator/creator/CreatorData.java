package steve6472.polyground.generator.creator;

import org.joml.Vector3f;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 28.08.2020
 * Project: CaveGame
 *
 ***********************/
public class CreatorData
{
	public boolean open, canOpen, canMove;
	public Vector3f position;
	public Vector3f origin;
	public Vector3f rotation;
	public PointType pointType;
	public float uvRotation;
	public String texture;

	public CreatorData()
	{
		position = new Vector3f();
		origin = new Vector3f(8, 8, 8);
		rotation = new Vector3f();
		canOpen = true;
	}

	public enum PointType
	{
		ORIGIN, CENTER, POINT;
	}
}
