package com.steve6472.polyground.block.model.registry;

import com.steve6472.polyground.block.model.JsonHelper;
import org.joml.AABBf;
import org.json.JSONObject;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 23.08.2019
 * Project: SJP
 *
 ***********************/
public class TintedCube extends Cube
{
	public float red, green, blue;
	public String refRed, refGreen, refBlue;

	public TintedCube(AABBf aabb)
	{
		super(aabb);
	}

	@Override
	public void loadFromJson(JSONObject json)
	{
		super.loadFromJson(json);

		if (json.has("red"))
		{
			String r = json.getString("red");
			if (r.startsWith("#"))
				refRed = r;
			else
				red = json.getFloat("red");
		}

		if (json.has("green"))
		{
			String g = json.getString("green");
			if (g.startsWith("#"))
				refGreen = g;
			else
				green = json.getFloat("green");
		}

		if (json.has("green"))
		{
			String b = json.getString("blue");
			if (b.startsWith("#"))
				refBlue = b;
			else
				blue = json.getFloat("blue");
		}
	}

	@Override
	public void loadFromParent(JSONObject json, Cube cube)
	{
		super.loadFromParent(json, cube);

		if (cube instanceof TintedCube)
		{
			if (json.has("tints"))
			{
				JsonHelper.parentTints(json, (TintedCube) cube);
			}
		}
	}
}
