package steve6472.polyground.block.model.elements;

import org.joml.GeometryUtils;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.json.JSONObject;
import steve6472.polyground.block.BlockAtlas;
import steve6472.polyground.block.model.IElement;
import steve6472.polyground.block.states.BlockState;
import steve6472.polyground.world.ModelBuilder;
import steve6472.polyground.world.World;
import steve6472.polyground.world.chunk.ModelLayer;

import java.awt.*;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 27.08.2020
 * Project: CaveGame
 *
 ***********************/
public class TriangleElement implements IElement
{
	public String name;

	public Vector3f v0, v1, v2;
	public Vector2f uv0, uv1, uv2;
	public Vector3f normal;
	public Vector3f tint;
	public boolean biomeTint;
	public int texture;
	public ModelLayer modelLayer;

	public TriangleElement(String name)
	{
		this.name = name;
	}

	@Override
	public void load(JSONObject element)
	{
		Vector3f v0 = ElUtil.loadVertex3("v0", element).div(16);
		Vector3f v1 = ElUtil.loadVertex3("v1", element).div(16);
		Vector3f v2 = ElUtil.loadVertex3("v2", element).div(16);

		loadVertices(v0, v1, v2, ElUtil.rotMat(element, v0, v1, v2));

		Vector2f uv0 = ElUtil.loadVertex2("uv0", element).div(16);
		Vector2f uv1 = ElUtil.loadVertex2("uv1", element).div(16);
		Vector2f uv2 = ElUtil.loadVertex2("uv2", element).div(16);

		name = element.optString("name", "");
		tint = ElUtil.tint(element);
		modelLayer = ElUtil.layer(element);
		biomeTint = element.optBoolean("biometint", false);

		loadUv(uv0, uv1, uv2);

		calculateNormal();
	}

	public void loadVertices(Vector3f v0, Vector3f v1, Vector3f v2, Matrix4f rotMat)
	{
		ElUtil.rot(rotMat, v0, v1, v2);
		this.v0 = v0;
		this.v1 = v1;
		this.v2 = v2;
	}

	public void loadUv(Vector2f uv0, Vector2f uv1, Vector2f uv2)
	{
		this.uv0 = new Vector2f(uv0);
		this.uv1 = new Vector2f(uv1);
		this.uv2 = new Vector2f(uv2);
	}

	public void calculateNormal()
	{
		if (normal == null)
			normal = new Vector3f();
		GeometryUtils.normal(v0, v1, v2, normal);
	}

	public void setTexture(int texture)
	{
		this.texture = texture;
	}

	public void fixUv(float texel)
	{
		Rectangle r = BlockAtlas.getTexture(texture);
		float x = r.x;
		float y = r.y;
		float w = r.width;
		float h = r.height;
		uv0.set((x + w * uv0.x) * texel, (y + h * uv0.y) * texel);
		uv1.set((x + w * uv1.x) * texel, (y + h * uv1.y) * texel);
		uv2.set((x + w * uv2.x) * texel, (y + h * uv2.y) * texel);
	}

	/**
	 * @param builder builder
	 * @param world world
	 * @param state state
	 * @param x x
	 * @param y y
	 * @param z z
	 * @return amount of triangles
	 */
	@Override
	public int build(ModelBuilder builder, ModelLayer modelLayer, World world, BlockState state, int x, int y, int z)
	{
		if (modelLayer != this.modelLayer)
			return 0;

		builder.tri(v0, v1, v2);
		builder.uv(uv0);
		builder.uv(uv1);
		builder.uv(uv2);
		builder.normalTri(normal);
		if (biomeTint)
		{
			Vector3f biomeTint = builder.getBiomeTint();
//			builder.colorTri(shade * biomeTint.x, shade * biomeTint.y, shade * biomeTint.z);
			builder.colorTri(biomeTint.x, biomeTint.y, biomeTint.z);
		} else
		{
//			builder.colorTri(shade * tint.x, shade * tint.y, shade * tint.z);
			builder.colorTri(tint.x, tint.y, tint.z);
		}
		return 1;
	}

	@Override
	public String getName()
	{
		return name;
	}

	@Override
	public String toString()
	{
		return "TriangleElement{" + "v0=" + v0 + ", v1=" + v1 + ", v2=" + v2 + ", uv0=" + uv0 + ", uv1=" + uv1 + ", uv2=" + uv2 + ", normal=" + normal + ", tint=" + tint + ", biomeTint=" + biomeTint + ", texture=" + texture + ", modelLayer=" + modelLayer + '}';
	}
}
