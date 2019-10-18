package com.steve6472.polyground.block.model.faceProperty;

import org.json.JSONArray;
import org.json.JSONObject;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 15.10.2019
 * Project: SJP
 *
 ***********************/
public class TintFaceProperty extends FaceProperty
{
	private float red, green, blue;

	public TintFaceProperty()
	{

	}

	public TintFaceProperty(JSONArray array)
	{
		red = array.getFloat(0) / 255f;
		green = array.getFloat(1) / 255f;
		blue = array.getFloat(2) / 255f;
	}

	@Override
	public void loadFromJSON(JSONObject json)
	{
		JSONArray array = json.getJSONArray("tint");
		red = array.getFloat(0) / 255f;
		green = array.getFloat(1) / 255f;
		blue = array.getFloat(2) / 255f;
	}

	@Override
	public void saveToJSON(JSONObject faceJson)
	{
		JSONArray array = new JSONArray();
		array.put(red * 255f);
		array.put(green * 255f);
		array.put(blue * 255f);
		faceJson.put("tint", array);
	}

	public float getRed()
	{
		return red;
	}

	public void setRed(float red)
	{
		this.red = red;
	}

	public float getGreen()
	{
		return green;
	}

	public void setGreen(float green)
	{
		this.green = green;
	}

	public float getBlue()
	{
		return blue;
	}

	public void setBlue(float blue)
	{
		this.blue = blue;
	}

	@Override
	public String getId()
	{
		return "tint";
	}

	@Override
	public TintFaceProperty createCopy()
	{
		TintFaceProperty o = new TintFaceProperty();
		o.red = red;
		o.green = green;
		o.blue = blue;
		return o;
	}
}
