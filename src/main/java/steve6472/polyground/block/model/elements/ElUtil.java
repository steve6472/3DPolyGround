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
	public static final Matrix4f EMPTY_MATRIX = new Matrix4f();
	private static final Quaternionf EMPTY_QUATERNION = new Quaternionf();

	private static final Vector3f tempVec = new Vector3f();

	public static Matrix4f rotMat(float cx, float cy, float cz, float ax, float ay, float az)
	{
		if (ax == 0 && ay == 0 && az == 0)
			return EMPTY_MATRIX;

		float rotx = (float) Math.toRadians(ax);
		float roty = (float) Math.toRadians(ay);
		float rotz = (float) Math.toRadians(az);

		Quaternionf rot = new Quaternionf();
		rot.rotateXYZ(rotx, roty, rotz);

		Matrix4f mat = new Matrix4f();
		mat.translate(cx, cy, cz);
		mat.rotate(rot);
		mat.translate(-cx, -cy, -cz);
		return mat;
	}

	public static Matrix4f rotMat(JSONObject obj, Vector3f... vectors)
	{
		if (!obj.has("rot"))
			return EMPTY_MATRIX;

		JSONArray rot = obj.getJSONArray("rot");
		float rotx = rot.getFloat(0) % 360.0f;
		float roty = rot.getFloat(1) % 360.0f;
		float rotz = rot.getFloat(2) % 360.0f;

		if (rotx == 0 && roty == 0 && rotz == 0)
			return EMPTY_MATRIX;

		if (obj.has("point_type"))
		{
			String pointType = obj.getString("point_type");
			if (pointType.equals("origin"))
			{
				tempVec.set(0.5f, 0.5f, 0.5f);
			} else if (pointType.equals("point"))
			{
				tempVec.set(loadVertex3("point", obj).div(16));
			} else //if (pointType.equals("center))
			{
				tempVec.set(center(vectors));
			}
		} else
		{
			if (obj.has("point"))
				tempVec.set(loadVertex3("point", obj).div(16));
			else
				tempVec.set(center(vectors));
		}

		return rotMat(tempVec.x, tempVec.y, tempVec.z, rotx, roty, rotz);
	}

	public static void rot(JSONObject obj, Vector3f... vectors)
	{
		Matrix4f mat = rotMat(obj, vectors);

		if (mat == EMPTY_MATRIX)
			return;

		for (Vector3f vector : vectors)
		{
			mat.transformPosition(vector);
		}
	}

	public static void rot(Matrix4f mat, Vector3f... vectors)
	{
		if (mat == EMPTY_MATRIX)
			return;

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
			return ElUtil.loadVertex3("tint", obj);
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
