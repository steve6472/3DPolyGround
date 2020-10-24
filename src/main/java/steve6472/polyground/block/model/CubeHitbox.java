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

	public CubeHitbox div(float div)
	{
		float inv = 1f / div;
		aabb.setMin(aabb.minX * inv, aabb.minY * inv, aabb.minZ * inv);
		aabb.setMax(aabb.maxX * inv, aabb.maxY * inv, aabb.maxZ * inv);
		return this;
	}

	public void loadFromJson(JSONObject json)
	{
		isHitbox = json.optBoolean("isHitbox", true);
		isCollisionBox = json.optBoolean("isCollisionBox", true);
		isVisible = json.optBoolean("hitboxvisible", true);
		name = json.optString("name", "");
	}

	public CubeHitbox setAabb(AABBf aabb)
	{
		this.aabb = aabb;
		return this;
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

	public CubeHitbox setHitbox(boolean hitbox)
	{
		isHitbox = hitbox;
		return this;
	}

	public CubeHitbox setVisible(boolean visible)
	{
		isVisible = visible;
		return this;
	}

	public boolean isCollisionBox()
	{
		return isCollisionBox;
	}

	public CubeHitbox setCollisionBox(boolean collisionBox)
	{
		isCollisionBox = collisionBox;
		return this;
	}

	public String getName()
	{
		return name;
	}

	public CubeHitbox setName(String name)
	{
		this.name = name;
		return this;
	}
}
