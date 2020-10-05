package steve6472.polyground.block.model;

import org.joml.AABBf;
import org.json.JSONObject;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 23.08.2019
 * Project: SJP
 *
 ***********************/
public class CubeHitbox
{
	AABBf aabb;
	private boolean isHitbox;
	private boolean isCollisionBox;
	private boolean isVisible;
	private String name;

	public CubeHitbox(AABBf aabb)
	{
		this.aabb = aabb;
		isHitbox = true;
		isCollisionBox = true;
		isVisible = true;
		name = "";
	}

	public void loadFromJson(JSONObject json)
	{
		isHitbox = json.optBoolean("isHitbox", true);
		isCollisionBox = json.optBoolean("isCollisionBox", true);
		isVisible = json.optBoolean("hitboxvisible", true);
		name = json.optString("name", "");
	}

	public void setAabb(AABBf aabb)
	{
		this.aabb = aabb;
	}

	public AABBf getAabb()
	{
		return aabb;
	}

	public boolean isVisible()
	{
		return isVisible;
	}

	public boolean isHitbox()
	{
		return isHitbox;
	}

	public void setHitbox(boolean hitbox)
	{
		isHitbox = hitbox;
	}

	public void setVisible(boolean visible)
	{
		isVisible = visible;
	}

	public boolean isCollisionBox()
	{
		return isCollisionBox;
	}

	public void setCollisionBox(boolean collisionBox)
	{
		isCollisionBox = collisionBox;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}
}
