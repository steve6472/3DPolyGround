package steve6472.polyground.generator.models;

import org.json.JSONArray;
import org.json.JSONObject;
import steve6472.polyground.block.model.faceProperty.RotationFaceProperty;
import steve6472.polyground.world.chunk.ModelLayer;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 01.07.2020
 * Project: CaveGame
 *
 ***********************/
public class FaceBuilder
{
	private String texture;
	private boolean autoUv, isVisible, biomeTint;
	private float minU, minV, maxU, maxV;
	private float red, green, blue;
	private RotationFaceProperty.EnumRotation rotation;
	private ModelLayer layer;

	public static FaceBuilder create()
	{
		return new FaceBuilder();
	}

	private FaceBuilder()
	{
		red = green = blue = 255f;
		layer = ModelLayer.NORMAL;
		rotation = RotationFaceProperty.EnumRotation.R_0;
		isVisible = true;
		autoUv = true;
	}

	public JSONObject build()
	{
		JSONObject main = new JSONObject();
		main.put("texture", texture);

		if (autoUv) main.put("autoUV", true);
		if (!isVisible) main.put("isVisible", false);
		if (biomeTint) main.put("biomeTint", true);
		if (!autoUv) main.put("uv", new JSONArray().put(minU).put(minV).put(maxU).put(maxV));
		if (red + green + blue != 255 * 3) main.put("tint", new JSONArray().put(red).put(green).put(blue));
		if (rotation != RotationFaceProperty.EnumRotation.R_0) main.put("rotation", rotation);
		if (layer != ModelLayer.NORMAL) main.put("modelLayer", layer);

		return main;
	}

	/*
	 * String
	 */

	public FaceBuilder texture(String texture)
	{
		this.texture = texture;
		return this;
	}

	/*
	 * Boolean
	 */

	public FaceBuilder autoUv(boolean autoUv)
	{
		this.autoUv = autoUv;
		return this;
	}

	public FaceBuilder visible(boolean visible)
	{
		this.isVisible = visible;
		return this;
	}

	public FaceBuilder biomeTint(boolean biomeTint)
	{
		this.biomeTint = biomeTint;
		return this;
	}

	/*
	 * Float
	 */

	public FaceBuilder uv(float minU, float minV, float maxU, float maxV)
	{
		this.minU = minU;
		this.minV = minV;
		this.maxU = maxU;
		this.maxV = maxV;
		autoUv = false;
		return this;
	}

	public FaceBuilder tint(float red, float green, float blue)
	{
		this.red = red;
		this.green = green;
		this.blue = blue;
		return this;
	}

	/*
	 * Enum
	 */

	public FaceBuilder rotation(RotationFaceProperty.EnumRotation rotation)
	{
		this.rotation = rotation;
		return this;
	}

	public FaceBuilder modelLayer(ModelLayer modelLayer)
	{
		this.layer = modelLayer;
		return this;
	}


}
