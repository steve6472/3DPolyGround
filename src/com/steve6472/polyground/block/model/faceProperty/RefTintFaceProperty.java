package com.steve6472.polyground.block.model.faceProperty;

import org.json.JSONObject;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 18.10.2019
 * Project: SJP
 *
 ***********************/
public class RefTintFaceProperty extends FaceProperty
{
	private String name;

	@Override
	public void loadFromJSON(JSONObject json)
	{
		name = json.getString("refTint");
	}

	@Override
	public void saveToJSON(JSONObject faceJson)
	{
		faceJson.put("refTint", name);
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	@Override
	public String getId()
	{
		return "refTint";
	}

	@Override
	public RefTintFaceProperty createCopy()
	{
		RefTintFaceProperty o = new RefTintFaceProperty();
		o.name = name;
		return o;
	}
}
