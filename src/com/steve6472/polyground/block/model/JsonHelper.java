package com.steve6472.polyground.block.model;

import com.steve6472.polyground.EnumFace;
import com.steve6472.polyground.block.BlockLoader;
import com.steve6472.polyground.block.model.faceProperty.RefTintFaceProperty;
import com.steve6472.polyground.block.model.faceProperty.TintFaceProperty;
import com.steve6472.polyground.block.model.registry.Cube;
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

	public static void loadParentTextures(JSONObject json, Cube parentCube)
	{
		JSONObject textures = json.getJSONObject("textures");

		for (String key : textures.keySet())
		{
			String newTexture = textures.getString(key);

			for (EnumFace ef : EnumFace.getFaces())
			{
				CubeFace pef = parentCube.getFace(ef);
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

	public static void loadParentTints(JSONObject json, Cube parentCube)
	{
		JSONObject tints = json.getJSONObject("tints");

		for (String key : tints.keySet())
		{
			for (EnumFace ef : EnumFace.getFaces())
			{
				CubeFace parentFace = parentCube.getFace(ef);
				if (parentFace != null)
				{
					if (parentFace.hasProperty(FaceRegistry.refTint))
					{
						RefTintFaceProperty refTint = parentFace.getProperty(FaceRegistry.refTint);
						if (refTint.getName().equals("#" + key))
						{
							if (!(tints.get(key) instanceof String))
							{
								parentFace.removeProperty(FaceRegistry.refTint);
								parentFace.addProperty(new TintFaceProperty(tints.getJSONArray(key)));
							}
						}
					}
				}
			}
		}


		/* Red *//*
		if (tints.has("red"))
		{
			String redTint = tints.getString("red");
			if (redTint.startsWith("#") && ("#red").equals(parentCube.refRed))
				parentCube.refRed = redTint;
			else if (("#red").equals(parentCube.refRed))
				parentCube.red = Float.parseFloat(redTint);
		}

		/* Green *//*
		if (tints.has("green"))
		{
			String greenTint = tints.getString("green");
			if (greenTint.startsWith("#") && ("#green").equals(parentCube.refGreen))
				parentCube.refGreen = greenTint;
			else if (("#green").equals(parentCube.refGreen))
				parentCube.green = Float.parseFloat(greenTint);
		}

		/* Blue *//*
		if (tints.has("blue"))
		{
			String blueTint = tints.getString("blue");
			if (blueTint.startsWith("#") && ("#blue").equals(parentCube.refBlue))
				parentCube.refBlue = blueTint;
			else if (("#blue").equals(parentCube.refBlue))
				parentCube.blue = Float.parseFloat(blueTint);
		}*/
	}
}
