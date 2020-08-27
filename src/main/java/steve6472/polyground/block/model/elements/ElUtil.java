package steve6472.polyground.block.model.elements;

import org.joml.*;
import org.json.JSONArray;
import org.json.JSONObject;
import steve6472.polyground.world.chunk.ModelLayer;

import java.lang.Math;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 27.08.2020
 * Project: CaveGame
 *
 ***********************/
public class ElUtil
{
	public static void rot(JSONObject obj, Vector3f... vectors)
	{
		Vector3f origin;
		if (obj.has("origin"))
			origin = loadVertex3("origin", obj).div(16);
		else
			origin = center(vectors);

		float rotx = (float) Math.toRadians(obj.optFloat("rot_x", 0) % 360.0f);
		float roty = (float) Math.toRadians(obj.optFloat("rot_y", 0) % 360.0f);
		float rotz = (float) Math.toRadians(obj.optFloat("rot_z", 0) % 360.0f);

		Quaternionf rot = new Quaternionf();
		rot.rotateXYZ(rotx, roty, rotz);

		Matrix4f mat = new Matrix4f();
		mat.translate(origin);
		mat.rotate(rot);
		mat.translate(origin.mul(-1));

		for (Vector3f vector : vectors)
		{
			mat.transformPosition(vector);
		}
	}

	public static void rotUv(JSONObject obj, Vector2f... uvs)
	{

	}

	private static Vector3f center(Vector3f... vectors)
	{
		Vector3f v = new Vector3f();

		for (Vector3f vector : vectors)
			v.add(vector);

		return v.div(vectors.length);
	}

	public static Vector3f tint(JSONObject obj)
	{
		if (obj.has("tint"))
			return ElUtil.loadVertex3("tint", obj).div(255);
		else
			return new Vector3f(1);
	}

	public static ModelLayer layer(JSONObject obj)
	{
		if (obj.has("layer"))
			return obj.getEnum(ModelLayer.class, "layer");
		else
			return ModelLayer.NORMAL;
	}

	/**
	 * Create Vector from 3-component array
	 *
	 * @param name name of vertice
	 * @param obj JSONObject
	 * @return new Vector
	 */
	public static Vector3f loadVertex3(String name, JSONObject obj)
	{
		JSONArray arr = obj.getJSONArray(name);
		return new Vector3f(arr.getFloat(0), arr.getFloat(1), arr.getFloat(2));
	}

	/**
	 * Create Vector from 4-component array
	 *
	 * @param name name of vertice
	 * @param obj JSONObject
	 * @return new Vector
	 */
	public static Vector4f loadVertex4(String name, JSONObject obj)
	{
		JSONArray arr = obj.getJSONArray(name);
		return new Vector4f(arr.getFloat(0), arr.getFloat(1), arr.getFloat(2), arr.getFloat(3));
	}

	/**
	 * Create Vector from 2-component array
	 *
	 * @param name name of vertice
	 * @param obj JSONObject
	 * @return new Vector
	 */
	public static Vector2f loadVertex2(String name, JSONObject obj)
	{
		JSONArray arr = obj.getJSONArray(name);
		return new Vector2f(arr.getFloat(0), arr.getFloat(1));
	}
}
