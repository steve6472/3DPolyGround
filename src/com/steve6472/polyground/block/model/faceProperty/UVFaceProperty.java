package com.steve6472.polyground.block.model.faceProperty;

import com.steve6472.polyground.EnumFace;
import com.steve6472.polyground.block.model.registry.Cube;
import org.json.JSONArray;
import org.json.JSONObject;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 16.10.2019
 * Project: SJP
 *
 ***********************/
public class UVFaceProperty extends FaceProperty
{
	private float minU, minV, maxU, maxV;

	public UVFaceProperty()
	{
		maxU = 1f;
		maxV = 1f;
	}

	@Override
	public void loadFromJSON(JSONObject json)
	{
		JSONArray array = json.getJSONArray("uv");
		minU = array.getFloat(0) / 16f;
		minV = array.getFloat(1) / 16f;
		maxU = array.getFloat(2) / 16f;
		maxV = array.getFloat(3) / 16f;
	}

	@Override
	public void saveToJSON(JSONObject faceJson)
	{
		JSONArray array = new JSONArray();
		array.put(minU * 16f);
		array.put(minV * 16f);
		array.put(maxU * 16f);
		array.put(maxV * 16f);
		faceJson.put("uv", array);
	}

	public float getMinU()
	{
		return minU;
	}

	public void setMinU(float minU)
	{
		this.minU = minU;
	}

	public float getMinV()
	{
		return minV;
	}

	public void setMinV(float minV)
	{
		this.minV = minV;
	}

	public float getMaxU()
	{
		return maxU;
	}

	public void setMaxU(float maxU)
	{
		this.maxU = maxU;
	}

	public float getMaxV()
	{
		return maxV;
	}

	public void setMaxV(float maxV)
	{
		this.maxV = maxV;
	}

	public void setUV(float minU, float minV, float maxU, float maxV)
	{
		this.minU = minU;
		this.minV = minV;
		this.maxU = maxU;
		this.maxV = maxV;
	}

	public void autoUV(Cube parent, EnumFace face)
	{
		switch (face)
		{
			case UP, DOWN -> setUV(parent.getAabb().minX, parent.getAabb().maxZ, parent.getAabb().maxX, parent.getAabb().minZ);
			case EAST -> setUV(parent.getAabb().minX, parent.getAabb().maxY, parent.getAabb().maxX, parent.getAabb().minY);
			case WEST -> setUV(parent.getAabb().maxX, parent.getAabb().maxY, parent.getAabb().minX, parent.getAabb().minY);
			case NORTH -> setUV(parent.getAabb().maxZ, parent.getAabb().maxY, parent.getAabb().minZ, parent.getAabb().minY);
			case SOUTH -> setUV(parent.getAabb().minZ, parent.getAabb().maxY, parent.getAabb().maxZ, parent.getAabb().minY);
		}
	}

	@Override
	public FaceProperty createCopy()
	{
		UVFaceProperty o = new UVFaceProperty();
		o.minU = minU;
		o.minV = minV;
		o.maxU = maxU;
		o.maxV = maxV;

		return o;
	}

	@Override
	public String getId()
	{
		return "uv";
	}
}
