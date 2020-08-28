package steve6472.polyground.block.model.elements;

import org.joml.Vector2f;
import org.joml.Vector4f;
import org.json.JSONObject;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 28.08.2020
 * Project: CaveGame
 *
 ***********************/
public class PlaneUV
{
	Vector2f v0, v1, v2, v3;

	public PlaneUV(Vector2f v0, Vector2f v1, Vector2f v2, Vector2f v3)
	{
		this.v0 = v0;
		this.v1 = v1;
		this.v2 = v2;
		this.v3 = v3;
	}

	public PlaneUV(Vector4f uv)
	{
		load(uv);
	}

	public PlaneUV()
	{
	}

	public PlaneUV load(JSONObject json)
	{
		if (json.has("uv"))
		{
			Vector4f uv = ElUtil.loadVertex4("uv", json).div(16);
			load(uv);
		} else
		{
			v0 = ElUtil.loadVertex2("uv0", json).div(16);
			v1 = ElUtil.loadVertex2("uv1", json).div(16);
			v2 = ElUtil.loadVertex2("uv2", json).div(16);
			v3 = ElUtil.loadVertex2("uv3", json).div(16);
		}

		return this;
	}

	private void load(Vector4f uv)
	{
		v0 = new Vector2f(uv.x, uv.y);
		v1 = new Vector2f(uv.x, uv.w);
		v2 = new Vector2f(uv.z, uv.w);
		v3 = new Vector2f(uv.z, uv.y);
	}
}
