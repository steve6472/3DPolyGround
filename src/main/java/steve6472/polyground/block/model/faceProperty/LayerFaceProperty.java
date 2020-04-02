package steve6472.polyground.block.model.faceProperty;

import steve6472.polyground.block.model.CubeFace;
import steve6472.polyground.registry.face.FaceRegistry;
import steve6472.polyground.world.chunk.ModelLayer;
import org.json.JSONObject;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 08.11.2019
 * Project: SJP
 *
 ***********************/
public class LayerFaceProperty extends FaceProperty
{
	private ModelLayer layer;

	public LayerFaceProperty()
	{
	}

	public LayerFaceProperty(ModelLayer layer)
	{
		this.layer = layer;
	}

	public ModelLayer getLayer()
	{
		return layer;
	}

	public void setLayer(ModelLayer layer)
	{
		this.layer = layer;
	}

	public static ModelLayer getModelLayer(CubeFace face)
	{
		if (face == null)
			return ModelLayer.NORMAL;

		if (!face.hasProperty(FaceRegistry.modelLayer))
			return ModelLayer.NORMAL;

		return face.getProperty(FaceRegistry.modelLayer).getLayer();
	}

	@Override
	public void loadFromJSON(JSONObject json)
	{
		layer = json.getEnum(ModelLayer.class, "modelLayer");
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
