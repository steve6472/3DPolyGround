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
public class VisibleFaceProperty extends FaceProperty
{
	private boolean isVisible;

	public VisibleFaceProperty()
	{

	}

	public VisibleFaceProperty(boolean visible)
	{
		isVisible = visible;
	}

	@Override
	public void loadFromJSON(JSONObject json)
	{
		isVisible = json.getBoolean("isVisible");
	}

	@Override
	public void saveToJSON(JSONObject faceJson)
	{
		faceJson.put("isVisible", isVisible);
	}

	public boolean isVisible()
	{
		return isVisible;
	}

	public void setVisible(boolean visible)
	{
		isVisible = visible;
	}

	public static boolean check(CubeFace face)
	{
		return face.hasProperty(FaceRegistry.isVisible) && face.getProperty(FaceRegistry.isVisible).isVisible();
	}

	@Override
	public String getId()
	{
		return "isVisible";
	}

	@Override
	public FaceProperty createCopy()
	{
		return new VisibleFaceProperty(isVisible);
	}
}
