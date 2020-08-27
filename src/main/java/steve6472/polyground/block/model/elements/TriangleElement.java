package steve6472.polyground.block.model.elements;

import org.joml.GeometryUtils;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.json.JSONObject;
import steve6472.polyground.block.BlockTextureHolder;
import steve6472.polyground.block.model.IElement;
import steve6472.polyground.world.ModelBuilder;
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
	private static final
	Vector3f[][] lights = new Vector3f[][]
		{
			{ new Vector3f(0, 1, 0),  new Vector3f(1.0f), },
			{ new Vector3f(0, 0, 1),  new Vector3f(0.8f), },
			{ new Vector3f(0, 0, -1), new Vector3f(0.8f), },
			{ new Vector3f(1, 0, 0),  new Vector3f(0.6f), },
			{ new Vector3f(-1, 0, 0), new Vector3f(0.6f), },
			{ new Vector3f(0, -1, 0), new Vector3f(0.5f), }
		};


	Vector3f v0, v1, v2;
	Vector2f uv0, uv1, uv2;
	Vector3f normal;
	Vector3f tint;
	float shade;
	boolean biomeTint;
	int texture;
	ModelLayer modelLayer;

	@Override
	public void load(JSONObject element)
	{
		v0 = ElUtil.loadVertex3("v0", element).div(16);
		v1 = ElUtil.loadVertex3("v1", element).div(16);
		v2 = ElUtil.loadVertex3("v2", element).div(16);

		ElUtil.rot(element, v0, v1, v2);

		uv0 = ElUtil.loadVertex2("uv0", element).div(16);
		uv1 = ElUtil.loadVertex2("uv1", element).div(16);
		uv2 = ElUtil.loadVertex2("uv2", element).div(16);

		tint = ElUtil.tint(element);
		modelLayer = ElUtil.layer(element);
		biomeTint = element.optBoolean("biometint", false);

		rotateUv((float) Math.toRadians(element.optFloat("texture_rot", 0)));

		calculateNormal();
	}

	public void rotateUv(float rad)
	{
		Matrix4f mat = new Matrix4f();
		mat.translate(0.5f, 0.5f, 0.5f);
		mat.rotate(rad, 0, 0, 1);
		mat.translate(-0.5f, -0.5f, -0.5f);
		Vector3f uv0rot = mat.transformPosition(new Vector3f(uv0, 1));
		Vector3f uv1rot = mat.transformPosition(new Vector3f(uv1, 1));
		Vector3f uv2rot = mat.transformPosition(new Vector3f(uv2, 1));
		uv0.set(uv0rot.x, uv0rot.y);
		uv1.set(uv1rot.x, uv1rot.y);
		uv2.set(uv2rot.x, uv2rot.y);
	}

	public void calculateNormal()
	{
		if (normal == null)
			normal = new Vector3f();
		GeometryUtils.normal(v0, v1, v2, normal);
		shade = calculateShade();
	}

	public void setTexture(int texture)
	{
		this.texture = texture;
	}

	public void fixUv(float texel)
	{
		Rectangle r = BlockTextureHolder.getTexture(texture);
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
	 * @return amount of triangles
	 */
	@Override
	public int build(ModelBuilder builder, ModelLayer modelLayer)
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
			builder.colorTri(shade * biomeTint.x, shade * biomeTint.y, shade * biomeTint.z);
		} else
		{
			builder.colorTri(shade * tint.x, shade * tint.y, shade * tint.z);
		}
		return 1;
	}

	private float calculateShade()
	{
		float shade = 0;
		for (Vector3f[] light : lights)
		{
			Vector3f lightDir = light[0];
			Vector3f lightColor = light[1];
			Vector3f diff = new Vector3f(normal);
			float dot = Math.max(diff.dot(lightDir), 0);

			shade += lightColor.x * dot;
		}
		return shade;
	}
}
