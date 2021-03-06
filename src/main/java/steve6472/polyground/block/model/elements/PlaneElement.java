package steve6472.polyground.block.model.elements;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.json.JSONObject;
import steve6472.polyground.block.model.IElement;
import steve6472.polyground.block.states.BlockState;
import steve6472.polyground.world.ModelBuilder;
import steve6472.polyground.world.World;
import steve6472.polyground.world.chunk.ModelLayer;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 27.08.2020
 * Project: CaveGame
 *
 ***********************/
public class PlaneElement implements IElement
{
	public String name;

	public TriangleElement t0, t1;
	public ModelLayer modelLayer;
	public boolean cull = false;

	public PlaneElement()
	{

	}

	public PlaneElement(String name)
	{
		this.name = name;
		t0 = new TriangleElement(name + "_0");
		t1 = new TriangleElement(name + "_1");
		modelLayer = ModelLayer.NORMAL;
	}

	@Override
	public void load(JSONObject element)
	{
		Vector3f v0 = ElUtil.loadVertex3("v0", element).div(16);
		Vector3f v1 = ElUtil.loadVertex3("v1", element).div(16);
		Vector3f v2 = ElUtil.loadVertex3("v2", element).div(16);
		Vector3f v3 = ElUtil.loadVertex3("v3", element).div(16);

		name = element.optString("name", "plane");

		Vector3f tint = ElUtil.tint(element);
		boolean biomeTint = element.optBoolean("biometint", false);
		modelLayer = ElUtil.layer(element);

		createTriangles(v0, v1, v2, v3, ElUtil.rotMat(element, v0, v1, v2, v3), new PlaneUV().load(element), element.optFloat("rotation", 0), tint, biomeTint);
	}

	public void loadVertices(Vector3f v0, Vector3f v1, Vector3f v2, Vector3f v3, Matrix4f rotMat, float uvRot)
	{
		ElUtil.rot(rotMat, v0, v1, v2, v3);

		uvRot %= 360.0f;
		if (uvRot < 0)
			uvRot += 360f;

		if (uvRot == 0)
		{
			t0.v0 = v0;
			t0.v1 = v1;
			t0.v2 = v2;

			t1.v0 = v2;
			t1.v1 = v3;
			t1.v2 = v0;
		} else if (uvRot == 90)
		{
			t0.v0 = v3;
			t0.v1 = v0;
			t0.v2 = v1;

			t1.v0 = v1;
			t1.v1 = v2;
			t1.v2 = v3;
		} else if (uvRot == 180)
		{
			t0.v0 = v2;
			t0.v1 = v3;
			t0.v2 = v0;

			t1.v0 = v0;
			t1.v1 = v1;
			t1.v2 = v2;
		} else if (uvRot == 270)
		{
			t0.v0 = v1;
			t0.v1 = v2;
			t0.v2 = v3;

			t1.v0 = v3;
			t1.v1 = v0;
			t1.v2 = v1;
		}
		t0.calculateNormal();
		t1.calculateNormal();
	}

	public void createTriangles(Vector3f v0, Vector3f v1, Vector3f v2, Vector3f v3, Matrix4f rotMat, PlaneUV uv, float uvRot, Vector3f tint, boolean biomeTint)
	{
		if (uv.uvLock)
		{
			uv.rotate((float) Math.toRadians(uvRot), true);
			uvRot = 0;
		}

		t0 = new TriangleElement(name + "_0");
		t0.loadUv(uv.v0, uv.v1, uv.v2);
		t0.tint = tint;
		t0.biomeTint = biomeTint;
		t0.modelLayer = modelLayer;

		t1 = new TriangleElement(name + "_1");
		t1.loadUv(uv.v2, uv.v3, uv.v0);
		t1.tint = tint;
		t1.biomeTint = biomeTint;
		t1.modelLayer = modelLayer;

		loadVertices(v0, v1, v2, v3, rotMat, uvRot);
	}

	@Override
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

		return t0.build(builder, modelLayer, world, state, x, y, z) + t1.build(builder, modelLayer, world, state, x, y, z);
	}

	@Override
	public String getName()
	{
		return name;
	}

	@Override
	public String toString()
	{
		return "PlaneElement{" + "t0=" + t0 + ", t1=" + t1 + ", modelLayer=" + modelLayer + '}';
	}
}
