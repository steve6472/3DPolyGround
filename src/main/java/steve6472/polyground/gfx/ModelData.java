package steve6472.polyground.gfx;

import steve6472.polyground.world.chunk.ModelLayer;
import steve6472.polyground.world.chunk.SubChunkModel;

import java.nio.FloatBuffer;

public class ModelData
{
	public final ModelLayer modelLayer;
	public final FloatBuffer vert, normal, color, text;
	public final SubChunkModel model;
	public final int triangleCount, x, layer, z;

	public ModelData(ModelLayer modelLayer, FloatBuffer vert, FloatBuffer color, FloatBuffer text, FloatBuffer normal, SubChunkModel model, int triangleCount, int x, int layer, int z)
	{
		this.modelLayer = modelLayer;
		this.vert = vert;
		this.color = color;
		this.text = text;
		this.normal = normal;
		this.model = model;
		this.triangleCount = triangleCount;
		this.x = x;
		this.layer = layer;
		this.z = z;
	}

	@Override
	public String toString()
	{
		return "ModelData{" + "modelLayer=" + modelLayer + ", x=" + x + ", layer=" + layer + ", z=" + z + '}';
	}
}