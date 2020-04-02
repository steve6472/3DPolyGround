package steve6472.polyground.block.model.faceProperty;

import steve6472.polyground.block.model.CubeFace;
import steve6472.polyground.registry.face.FaceRegistry;
import org.json.JSONObject;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 15.10.2019
 * Project: SJP
 *
 ***********************/
public class AutoUVFaceProperty extends FaceProperty
{
	private boolean isAuto;

	public AutoUVFaceProperty()
	{
	}

	public AutoUVFaceProperty(boolean auto)
	{
		this.isAuto = auto;
	}

	@Override
	public void loadFromJSON(JSONObject json)
	{
		isAuto = json.getBoolean("autoUV");
	}

	@Override
	public void saveToJSON(JSONObject faceJson)
	{
		faceJson.put("autoUV", isAuto);
	}

	public boolean isAuto()
	{
		return isAuto;
	}

	public void setAuto(boolean auto)
	{
		isAuto = auto;
	}

	public static boolean check(CubeFace face)
	{
		return face.hasProperty(FaceRegistry.autoUv) && face.getProperty(FaceRegistry.autoUv).isAuto();
	}

	@Override
	public String getId()
	{
		return "autoUV";
	}

	@Override
	public AutoUVFaceProperty createCopy()
	{
		return new AutoUVFaceProperty(isAuto);
	}
}
