package com.steve6472.polyground.block.model.faceProperty;

import com.steve6472.polyground.block.model.CubeFace;
import com.steve6472.polyground.block.model.registry.face.FaceRegistry;
import org.json.JSONObject;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 15.10.2019
 * Project: SJP
 *
 ***********************/
public class EmissiveFaceProperty extends FaceProperty
{
	private boolean emissive;

	@Override
	public void loadFromJSON(JSONObject json)
	{
		emissive = json.getBoolean("emissive");
	}

	@Override
	public void saveToJSON(JSONObject faceJson)
	{
		faceJson.put("emissive", emissive);
	}

	public boolean isEmissive()
	{
		return emissive;
	}

	public void setEmissive(boolean emissive)
	{
		this.emissive = emissive;
	}

	public static boolean check(CubeFace face)
	{
		return face.hasProperty(FaceRegistry.emissive) && face.getProperty(FaceRegistry.emissive).isEmissive();
	}

	@Override
	public String getId()
	{
		return "emissive";
	}

	@Override
	public EmissiveFaceProperty createCopy()
	{
		EmissiveFaceProperty o = new EmissiveFaceProperty();
		o.emissive = emissive;
		return o;
	}
}
