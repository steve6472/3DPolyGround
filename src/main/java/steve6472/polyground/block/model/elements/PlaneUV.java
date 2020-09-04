package steve6472.polyground.block.model.elements;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
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
	boolean uvLock;

	public PlaneUV(Vector2f v0, Vector2f v1, Vector2f v2, Vector2f v3, boolean uvLock)
	{
		this.v0 = v0;
		this.v1 = v1;
		this.v2 = v2;
		this.v3 = v3;
		this.uvLock = uvLock;
	}

	public PlaneUV(Vector4f uv, boolean uvLock)
	{
		this.uvLock = uvLock;
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
		uvLock = json.optBoolean("uvlock", false);

		return this;
	}

	public void rotate(float rad, boolean uvLock)
	{
		float x;
		float y;
		if (uvLock)
		{
			x = y = 0.5f;
		} else
		{
			x = (v0.x + v1.x + v2.x + v3.x) / 4f;
			y = (v0.y + v1.y + v2.y + v3.y) / 4f;
		}
		Matrix4f mat = new Matrix4f();
		mat.translate(x, y, 0.5f);
		mat.rotate(rad, 0, 0, 1);
		mat.translate(-x, -y, -0.5f);
		Vector3f uv0rot = mat.transformPosition(new Vector3f(v0, 1));
		Vector3f uv1rot = mat.transformPosition(new Vector3f(v1, 1));
		Vector3f uv2rot = mat.transformPosition(new Vector3f(v2, 1));
		Vector3f uv3rot = mat.transformPosition(new Vector3f(v3, 1));
		this.v0 = new Vector2f(uv0rot.x, uv0rot.y);
		this.v1 = new Vector2f(uv1rot.x, uv1rot.y);
		this.v2 = new Vector2f(uv2rot.x, uv2rot.y);
		this.v3 = new Vector2f(uv3rot.x, uv3rot.y);
	}

	private void load(Vector4f uv)
	{
		v0 = new Vector2f(uv.x, uv.y);
		v1 = new Vector2f(uv.x, uv.w);
		v2 = new Vector2f(uv.z, uv.w);
		v3 = new Vector2f(uv.z, uv.y);
	}
}
