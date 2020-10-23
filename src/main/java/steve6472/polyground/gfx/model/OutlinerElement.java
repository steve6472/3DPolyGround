package steve6472.polyground.gfx.model;

import java.util.UUID;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 22.10.2020
 * Project: CaveGame
 *
 ***********************/
public abstract class OutlinerElement
{
	public String name;
	public float originX, originY, originZ;
	public float rotationX, rotationY, rotationZ;
	public float positionX, positionY, positionZ;
	public float scaleX, scaleY, scaleZ;
	public UUID uuid;
}
