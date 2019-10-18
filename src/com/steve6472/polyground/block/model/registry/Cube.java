package com.steve6472.polyground.block.model.registry;

import com.steve6472.polyground.EnumFace;
import com.steve6472.polyground.block.model.CubeFace;
import com.steve6472.polyground.block.model.JsonHelper;
import org.joml.AABBf;
import org.json.JSONObject;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 23.08.2019
 * Project: SJP
 *
 ***********************/
public class Cube
{
	AABBf aabb;
	CubeFace[] faces;
	private boolean isHitbox;
	private boolean isCollisionBox;

	public Cube(AABBf aabb)
	{
		this.aabb = aabb;
		faces = new CubeFace[6];
		isHitbox = true;
		isCollisionBox = true;
	}

	public void loadFromJson(JSONObject json)
	{
		json.optBoolean("isHitbox", true);
		json.optBoolean("isCollisionBox", true);
	}

	public void loadFromParent(JSONObject json, Cube parentCube)
	{
		if (json.has("textures"))
		{
			JsonHelper.loadParentTextures(json, parentCube);
		}

		if (json.has("tints"))
		{
			JsonHelper.loadParentTints(json, parentCube);
		}

		parentCube.isHitbox = json.optBoolean("isHitbox", true);
		parentCube.isCollisionBox = json.optBoolean("isCollisionBox", true);
	}

	public void setAabb(AABBf aabb)
	{
		this.aabb = aabb;
	}

	public AABBf getAabb()
	{
		return aabb;
	}

	public CubeFace[] getFaces()
	{
		return faces;
	}

	public CubeFace getFace(EnumFace face)
	{
		return switch (face)
			{
				case UP -> faces[0];
				case DOWN -> faces[1];
				case NORTH ->faces[2];
				case SOUTH -> faces[3];
				case EAST -> faces[4];
				case WEST -> faces[5];
				case NONE -> null;
			};
	}

	public void setFace(EnumFace face, CubeFace f)
	{
		switch (face)
			{
				case UP -> faces[0] = f;
				case DOWN -> faces[1] = f;
				case NORTH ->faces[2] = f;
				case SOUTH -> faces[3] = f;
				case EAST -> faces[4] = f;
				case WEST -> faces[5] = f;
			}
	}

	public Cube rotate()
	{
		CubeFace temp = getFace(EnumFace.NORTH);
		setFace(EnumFace.NORTH, getFace(EnumFace.EAST));
		setFace(EnumFace.EAST, getFace(EnumFace.SOUTH));
		setFace(EnumFace.SOUTH, getFace(EnumFace.WEST));
		setFace(EnumFace.WEST, temp);

		getFace(EnumFace.NORTH).setFace(EnumFace.NORTH);
		getFace(EnumFace.EAST).setFace(EnumFace.EAST);
		getFace(EnumFace.SOUTH).setFace(EnumFace.SOUTH);
		getFace(EnumFace.WEST).setFace(EnumFace.WEST);
		return this;
	}

	public boolean isHitbox()
	{
		return isHitbox;
	}

	public void setHitbox(boolean hitbox)
	{
		isHitbox = hitbox;
	}

	public boolean isCollisionBox()
	{
		return isCollisionBox;
	}

	public void setCollisionBox(boolean collisionBox)
	{
		isCollisionBox = collisionBox;
	}

	@Override
	public String toString()
	{
		return "Cube{" + "faces=" + toString(faces) + '}';
	}

	public static String toString(Object[] a)
	{
		if (a == null)
			return "null";

		int iMax = a.length - 1;
		if (iMax == -1)
			return "[]";

		StringBuilder b = new StringBuilder();
		b.append('[');
		for (int i = 0; ; i++) {
			b.append("\n");
			b.append(String.valueOf(a[i]));
			if (i == iMax)
				return b.append(']').toString();
			b.append(", ");
		}
	}
}
