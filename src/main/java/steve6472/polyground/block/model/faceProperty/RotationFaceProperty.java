package steve6472.polyground.block.model.faceProperty;

import org.json.JSONObject;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 15.10.2019
 * Project: SJP
 *
 ***********************/
public class RotationFaceProperty extends FaceProperty
{
	private int rotation;

	public RotationFaceProperty()
	{
	}

	public RotationFaceProperty(int rotation)
	{
		this.rotation = rotation;
	}

	@Override
	public void loadFromJSON(JSONObject json)
	{
		rotation = json.getInt("rotation");
	}

	@Override
	public void saveToJSON(JSONObject faceJson)
	{
		faceJson.put("rotation", rotation);
	}

	public int getRotation()
	{
		return rotation;
	}

	public void setRotation(int rotation)
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
}
