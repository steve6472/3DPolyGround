package com.steve6472.polyground.block.model.faceProperty;

import com.steve6472.polyground.block.model.CubeFace;
import com.steve6472.polyground.block.model.registry.face.FaceEntry;
import org.json.JSONObject;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 15.10.2019
 * Project: SJP
 *
 ***********************/
public abstract class AbstractBooleanProperty extends FaceProperty
{
	private boolean flag;

	public AbstractBooleanProperty()
	{

	}

	public AbstractBooleanProperty(boolean flag)
	{
		this.flag = flag;
	}

	@Override
	public void loadFromJSON(JSONObject json)
	{
		flag = json.getBoolean(getId());
	}

	@Override
	public void saveToJSON(JSONObject faceJson)
	{
		faceJson.put(getId(), flag);
	}

	public boolean isFlag()
	{
		return flag;
	}

	public void setFlag(boolean flag)
	{
		this.flag = flag;
	}

	public boolean check(CubeFace face)
	{
		return face.hasProperty(getEntry()) && face.getProperty(getEntry()).isFlag();
	}

	protected abstract FaceEntry<? extends AbstractBooleanProperty> getEntry();
}
