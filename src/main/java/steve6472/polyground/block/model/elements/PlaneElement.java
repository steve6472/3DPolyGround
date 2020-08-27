package steve6472.polyground.block.model.elements;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.json.JSONObject;
import steve6472.polyground.block.model.IElement;
import steve6472.polyground.world.ModelBuilder;
import steve6472.polyground.world.chunk.ModelLayer;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 27.08.2020
 * Project: CaveGame
 *
 ***********************/
public class PlaneElement implements IElement
{
	TriangleElement t0, t1;
	ModelLayer modelLayer;

	@Override
	public void load(JSONObject element)
	{
		Vector3f v0 = ElUtil.loadVertex3("v0", element).div(16);
		Vector3f v1 = ElUtil.loadVertex3("v1", element).div(16);
		Vector3f v2 = ElUtil.loadVertex3("v2", element).div(16);
		Vector3f v3 = ElUtil.loadVertex3("v3", element).div(16);
		Vector4f uv = ElUtil.loadVertex4("uv", element).div(16);

		Vector3f tint = ElUtil.tint(element);
		boolean biomeTint = element.optBoolean("biometint", false);
		modelLayer = ElUtil.layer(element);

		ElUtil.rot(element, v0, v1, v2, v3);

		createTriangles(v0, v1, v2, v3, uv, tint, biomeTint);
		float rad = (float) Math.toRadians(element.optFloat("texture_rot", 0));
		t0.rotateUv(rad);
		t1.rotateUv(rad);
	}

	public void createTriangles(Vector3f v0, Vector3f v1, Vector3f v2, Vector3f v3, Vector4f uv, Vector3f tint, boolean biomeTint)
	{
		t0 = new TriangleElement();
		t0.v0 = v0;
		t0.v1 = v1;
		t0.v2 = v2;
		t0.uv0 = new Vector2f(uv.x, uv.y);
		t0.uv1 = new Vector2f(uv.x, uv.w);
		t0.uv2 = new Vector2f(uv.z, uv.w);
		t0.tint = tint;
		t0.biomeTint = biomeTint;
		t0.modelLayer = modelLayer;
		t0.calculateNormal();

		t1 = new TriangleElement();
		t1.v0 = v2;
		t1.v1 = v3;
		t1.v2 = v0;
		t1.uv0 = new Vector2f(uv.z, uv.w);
		t1.uv1 = new Vector2f(uv.z, uv.y);
		t1.uv2 = new Vector2f(uv.x, uv.y);
		t1.tint = tint;
		t1.biomeTint = biomeTint;
		t1.modelLayer = modelLayer;
		t1.calculateNormal();
	}

	public void setTexture(int texture)
	{
		t0.setTexture(texture);
		t1.setTexture(texture);
	}

	@Override
	public void fixUv(float texel)
	{
		t0.fixUv(texel);
		t1.fixUv(texel);
	}

	/**
	 * @param builder builder
	 * @return amount of triangles
	 */
	@Override
	public int build(ModelBuilder builder, ModelLayer modelLayer)
	{
		if (modelLayer != this.modelLayer)
			return 0;

		return t0.build(builder, modelLayer) + t1.build(builder, modelLayer);
	}
}
