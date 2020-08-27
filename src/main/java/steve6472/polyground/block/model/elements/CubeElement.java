package steve6472.polyground.block.model.elements;

import org.joml.Vector3f;
import org.joml.Vector4f;
import org.json.JSONObject;
import steve6472.polyground.EnumFace;
import steve6472.polyground.block.BlockTextureHolder;
import steve6472.polyground.block.model.IElement;
import steve6472.polyground.world.ModelBuilder;
import steve6472.polyground.world.chunk.ModelLayer;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 27.08.2020
 * Project: CaveGame
 *
 ***********************/
public class CubeElement implements IElement
{
	PlaneElement p0, p1, p2, p3, p4, p5;

	@Override
	public void load(JSONObject element)
	{
		Vector3f from = ElUtil.loadVertex3("from", element).div(16);
		Vector3f to = ElUtil.loadVertex3("to", element).div(16);



		if (element.has("north")) p0 = face(EnumFace.NORTH, element,
			new Vector3f(to.x, to.y, to.z),
			new Vector3f(to.x, from.y, to.z),
			new Vector3f(to.x, from.y, from.z),
			new Vector3f(to.x, to.y, from.z), from, to);
		if (element.has("east"))  p1 = face(EnumFace.EAST, element,
			new Vector3f(from.x, to.y, to.z),
			new Vector3f(from.x, from.y, to.z),
			new Vector3f(to.x, from.y, to.z),
			new Vector3f(to.x, to.y, to.z), from, to);
		if (element.has("south")) p2 = face(EnumFace.SOUTH, element,
			new Vector3f(from.x, to.y, from.z),
			new Vector3f(from.x, from.y, from.z),
			new Vector3f(from.x, from.y, to.z),
			new Vector3f(from.x, to.y, to.z), from, to);
		if (element.has("west"))  p3 = face(EnumFace.WEST, element,
			new Vector3f(to.x, to.y, from.z),
			new Vector3f(to.x, from.y, from.z),
			new Vector3f(from.x, from.y, from.z),
			new Vector3f(from.x, to.y, from.z), from, to);
		if (element.has("up"))    p4 = face(EnumFace.UP, element,
			new Vector3f(to.x, to.y, from.z),
			new Vector3f(from.x, to.y, from.z),
			new Vector3f(from.x, to.y, to.z),
			new Vector3f(to.x, to.y, to.z), from, to);
		if (element.has("down"))  p5 = face(EnumFace.DOWN, element,
			new Vector3f(from.x, from.y, from.z),
			new Vector3f(to.x, from.y, from.z),
			new Vector3f(to.x, from.y, to.z),
			new Vector3f(from.x, from.y, to.z), from, to);
	}

	@Override
	public void setTexture(int texture)
	{

	}

	private PlaneElement face(EnumFace enumFace, JSONObject json, Vector3f v0, Vector3f v1, Vector3f v2, Vector3f v3, Vector3f from, Vector3f to)
	{
		JSONObject face = json.getJSONObject(enumFace.getName());
		PlaneElement el = new PlaneElement();
		ElUtil.rot(json, v0, v1, v2, v3);
		Vector4f uv;

		if (!json.has("uv"))
		{
			float minX = from.x, minY = from.y, minZ = from.z, maxX = to.x, maxY = to.y, maxZ = to.z;
			switch (enumFace)
			{
				case UP -> uv = new Vector4f(minZ, 1.0f - maxX, maxZ, 1.0f - minX);
				case DOWN -> //noinspection SuspiciousNameCombination
					uv = new Vector4f(minZ, minX, maxZ, maxX);
				case EAST -> uv = new Vector4f(minX, 1.0f - maxY, maxX, 1.0f - minY);
				case WEST -> uv = new Vector4f(1.0f - maxX, 1.0f - maxY, 1.0f - minX, 1.0f - minY);
				case NORTH -> uv = new Vector4f(1.0f - maxZ, 1.0f - maxY, 1.0f - minZ, 1.0f - minY);
				case SOUTH -> uv = new Vector4f(minZ, 1.0f - maxY, maxZ, 1.0f - minY);
				default -> throw new IllegalStateException("Unexpected value: " + enumFace);
			}
		} else
		{
			uv = ElUtil.loadVertex4("uv", face).div(16);
		}

		Vector3f tint = ElUtil.tint(face);
		ModelLayer modelLayer = ElUtil.layer(face);
		boolean biomeTint = face.optBoolean("biometint", false);
		
		el.createTriangles(v0, v1, v2, v3, uv, tint, modelLayer, biomeTint);

		float rad = (float) Math.toRadians(face.optFloat("texture_rot", 0));
		el.t0.rotateUv(rad);
		el.t1.rotateUv(rad);

		String texture = face.getString("texture");
		BlockTextureHolder.putTexture(texture);
		el.setTexture(BlockTextureHolder.getTextureId(texture));

		return el;
	}

	@Override
	public void fixUv(float texel)
	{
		if (p0 != null) p0.fixUv(texel);
		if (p1 != null) p1.fixUv(texel);
		if (p2 != null) p2.fixUv(texel);
		if (p3 != null) p3.fixUv(texel);
		if (p4 != null) p4.fixUv(texel);
		if (p5 != null) p5.fixUv(texel);
	}

	/**
	 * @param builder builder
	 * @return amount of triangles
	 */
	@Override
	public int build(ModelBuilder builder)
	{
		return
			(p0 != null ? p0.build(builder) : 0) +
			(p1 != null ? p1.build(builder) : 0) +
			(p2 != null ? p2.build(builder) : 0) +
			(p3 != null ? p3.build(builder) : 0) +
			(p4 != null ? p4.build(builder) : 0) +
			(p5 != null ? p5.build(builder) : 0);
	}
}
