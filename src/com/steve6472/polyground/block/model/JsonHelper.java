package com.steve6472.polyground.block.model;

import com.steve6472.polyground.EnumFace;
import com.steve6472.polyground.block.BlockLoader;
import com.steve6472.polyground.block.model.registry.Cube;
import com.steve6472.polyground.block.model.registry.TintedCube;
import com.steve6472.polyground.block.model.registry.face.FaceRegistry;
import org.joml.AABBf;
import org.json.JSONArray;
import org.json.JSONObject;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 07.10.2019
 * Project: SJP
 *
 ***********************/
public class JsonHelper
{
	public static AABBf createAABB(JSONObject json)
	{
		JSONArray from = json.getJSONArray("from");
		JSONArray to = json.getJSONArray("to");

		return new AABBf(
			from.getFloat(0) / 16f,
			from.getFloat(1) / 16f,
			from.getFloat(2) / 16f,
			to.getFloat(0) / 16f,
			to.getFloat(1) / 16f,
			to.getFloat(2) / 16f
		);
	}

	public static void loadUv(JSONObject faceJson, CubeFace cubeFace)
	{
		if (faceJson.has("uv"))
		{
			JSONArray uvArray = faceJson.getJSONArray("uv");
			cubeFace.getProperty(FaceRegistry.uv).setUV(
				uvArray.getFloat(0) / 16f,
				uvArray.getFloat(1) / 16f,
				uvArray.getFloat(2) / 16f,
				uvArray.getFloat(3) / 16f);
		}
	}

	public static void loadShade(JSONObject faceJson, CubeFace cubeFace)
	{
		if (faceJson.has("shade"))
		{
			cubeFace.setShade(faceJson.getFloat("shade"));
		}
	}

	public static void loadEmissive(JSONObject faceJson, CubeFace cubeFace)
	{
		cubeFace.setEmissive(faceJson.optBoolean("emissive", false));
	}

	public static void loadParentTextures(JSONObject json, Cube cube)
	{
		JSONObject textures = json.getJSONObject("textures");

		for (String key : textures.keySet())
		{
			String newTexture = textures.getString(key);

			for (EnumFace ef : EnumFace.getFaces())
			{
				CubeFace pef = cube.getFace(ef);
				if (pef == null) continue;

				if (newTexture.startsWith("#") && ("#" + key).equals(pef.getProperty(FaceRegistry.texture).getTexture()))
				{
					pef.getProperty(FaceRegistry.texture).setTexture(newTexture);
				} else
				{
					if (("#" + key).equals(pef.getProperty(FaceRegistry.texture).getTexture()))
					{
						BlockLoader.putTexture(newTexture);
						pef.getProperty(FaceRegistry.texture).setTexture(null);
						pef.getProperty(FaceRegistry.texture).setTextureId(BlockLoader.getTextureId(newTexture));
					}
				}
			}
		}
	}

	public static void parentTints(JSONObject json, TintedCube cube)
	{
		JSONObject tints = json.getJSONObject("tints");

		/* Red */
		if (tints.has("red"))
		{
			String redTint = tints.getString("red");
			if (redTint.startsWith("#") && ("#red").equals(cube.refRed))
				cube.refRed = redTint;
			else if (("#red").equals(cube.refRed))
				cube.red = Float.parseFloat(redTint);
		}

		/* Green */
		if (tints.has("green"))
		{
			String greenTint = tints.getString("green");
			if (greenTint.startsWith("#") && ("#green").equals(cube.refGreen))
				cube.refGreen = greenTint;
			else if (("#green").equals(cube.refGreen))
				cube.green = Float.parseFloat(greenTint);
		}

		/* Blue */
		if (tints.has("blue"))
		{
			String blueTint = tints.getString("blue");
			if (blueTint.startsWith("#") && ("#blue").equals(cube.refBlue))
				cube.refBlue = blueTint;
			else if (("#blue").equals(cube.refBlue))
				cube.blue = Float.parseFloat(blueTint);
		}
	}
}
