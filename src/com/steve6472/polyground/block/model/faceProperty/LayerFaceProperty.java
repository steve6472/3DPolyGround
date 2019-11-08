package com.steve6472.polyground.block.model.faceProperty;

import com.steve6472.polyground.block.model.CubeFace;
import com.steve6472.polyground.block.model.registry.face.FaceRegistry;
import org.json.JSONObject;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 08.11.2019
 * Project: SJP
 *
 ***********************/
public class LayerFaceProperty extends FaceProperty
{
	private int layer;

	public LayerFaceProperty()
	{
	}

	public LayerFaceProperty(int layer)
	{
		this.layer = layer;
	}

	public int getLayer()
	{
		return layer;
	}

	public void setLayer(int layer)
	{
		this.layer = layer;
	}

	public static int getModelLayer(CubeFace face)
	{
		if (face == null)
			return 0;

		if (!face.hasProperty(FaceRegistry.modelLayer))
			return 0;

		return face.getProperty(FaceRegistry.modelLayer).getLayer();
	}

	@Override
	public void loadFromJSON(JSONObject json)
	{
		layer = json.getInt("modelLayer");
	}

	@Override
	public void saveToJSON(JSONObject faceJson)
	{
		faceJson.put("modelLayer", layer);
	}

	@Override
	public FaceProperty createCopy()
	{
		return new LayerFaceProperty(layer);
	}

	@Override
	public String getId()
	{
		return "modelLayer";
	}
}
