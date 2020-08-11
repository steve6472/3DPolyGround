package steve6472.polyground.block.model.faceProperty;

import org.joml.Vector3f;
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
	private Vector3f color;

	public TintFaceProperty()
	{
		color = new Vector3f(1, 1, 1);
	}

	@Override
	public void loadFromJSON(JSONObject json)
	{
		JSONArray array = json.getJSONArray("tint");
		color = new Vector3f(array.getFloat(0) / 255f, array.getFloat(1) / 255f, array.getFloat(2) / 255f);
	}

	@Override
	public void saveToJSON(JSONObject faceJson)
	{
		JSONArray array = new JSONArray();
		array.put(color.x * 255f);
		array.put(color.y * 255f);
		array.put(color.z * 255f);
		faceJson.put("tint", array);
	}

	public float getRed()
	{
		return color.x;
	}

	public float getGreen()
	{
		return color.y;
	}

	public float getBlue()
	{
		return color.z;
	}

	public Vector3f getColor()
	{
		return color;
	}

	public void setTint(float red, float green, float blue)
	{
		color.set(red, green, blue);
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
		o.color = new Vector3f(color);
		return o;
	}
}
