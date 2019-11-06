package com.steve6472.polyground.block.model.faceProperty;

import org.json.JSONObject;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 15.10.2019
 * Project: SJP
 *
 ***********************/
public class RotationFaceProperty extends FaceProperty
{
	private EnumRotation rotation;

	public RotationFaceProperty()
	{

	}

	public RotationFaceProperty(EnumRotation rotation)
	{
		this.rotation = rotation;
	}

	@Override
	public void loadFromJSON(JSONObject json)
	{
		rotation = json.getEnum(EnumRotation.class, "rotation");
	}

	@Override
	public void saveToJSON(JSONObject faceJson)
	{
		faceJson.put("rotation", rotation);
	}

	public EnumRotation getRotation()
	{
		return rotation;
	}

	public void setRotation(EnumRotation rotation)
	{
		this.rotation = rotation;
	}

	@Override
	public String getId()
	{
		return "rotation";
	}

	@Override
	public FaceProperty createCopy()
	{
		return new RotationFaceProperty(rotation);
	}

	public enum EnumRotation
	{
		R_90, R_180, R_270
	}
}
