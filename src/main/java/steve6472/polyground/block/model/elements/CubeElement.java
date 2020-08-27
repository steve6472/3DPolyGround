package steve6472.polyground.block.model.elements;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.json.JSONObject;
import steve6472.polyground.EnumFace;
import steve6472.polyground.block.BlockTextureHolder;
import steve6472.polyground.block.model.IElement;
import steve6472.polyground.block.states.BlockState;
import steve6472.polyground.world.Cull;
import steve6472.polyground.world.ModelBuilder;
import steve6472.polyground.world.World;
import steve6472.polyground.world.chunk.ModelLayer;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 27.08.2020
 * Project: CaveGame
 *
 ***********************/
public class CubeElement implements IElement
{
	PlaneElement[] ps;
	PlaneElement up, down;
	String name;

	@Override
	public void load(JSONObject element)
	{
		Vector3f from = ElUtil.loadVertex3("from", element).div(16);
		Vector3f to = ElUtil.loadVertex3("to", element).div(16);

//		Matrix4f mat = ElUtil.rotMat(element, from, to);
//		AABBf aabb = new AABBf(from, to);
//		aabb.transform(mat);
//		from.set(aabb.minX, aabb.minY, aabb.minZ);
//		to.set(aabb.maxX, aabb.maxY, aabb.maxZ);

		ps = new PlaneElement[4];

		name = element.optString("name", "");

		if (element.has("faces"))
		{
			JSONObject faces = element.getJSONObject("faces");

			if (faces.has("north")) ps[0] = face(EnumFace.NORTH, element, faces,
				new Vector3f(to.x, to.y, to.z),
				new Vector3f(to.x, from.y, to.z),
				new Vector3f(to.x, from.y, from.z),
				new Vector3f(to.x, to.y, from.z), from, to);
			if (faces.has("east"))  ps[1] = face(EnumFace.EAST, element, faces,
				new Vector3f(from.x, to.y, to.z),
				new Vector3f(from.x, from.y, to.z),
				new Vector3f(to.x, from.y, to.z),
				new Vector3f(to.x, to.y, to.z), from, to);
			if (faces.has("south")) ps[2] = face(EnumFace.SOUTH, element, faces,
				new Vector3f(from.x, to.y, from.z),
				new Vector3f(from.x, from.y, from.z),
				new Vector3f(from.x, from.y, to.z),
				new Vector3f(from.x, to.y, to.z), from, to);
			if (faces.has("west"))  ps[3] = face(EnumFace.WEST, element, faces,
				new Vector3f(to.x, to.y, from.z),
				new Vector3f(to.x, from.y, from.z),
				new Vector3f(from.x, from.y, from.z),
				new Vector3f(from.x, to.y, from.z), from, to);
			if (faces.has("up"))    up = face(EnumFace.UP, element, faces,
				new Vector3f(to.x, to.y, from.z),
				new Vector3f(from.x, to.y, from.z),
				new Vector3f(from.x, to.y, to.z),
				new Vector3f(to.x, to.y, to.z), from, to);
			if (faces.has("down"))  down = face(EnumFace.DOWN, element, faces,
				new Vector3f(from.x, from.y, from.z),
				new Vector3f(to.x, from.y, from.z),
				new Vector3f(to.x, from.y, to.z),
				new Vector3f(from.x, from.y, to.z), from, to);
		}
	}

	@Override
	public void setTexture(int texture)
	{

	}

	private PlaneElement face(EnumFace enumFace, JSONObject element, JSONObject faces, Vector3f v0, Vector3f v1, Vector3f v2, Vector3f v3, Vector3f from, Vector3f to)
	{
		JSONObject face = faces.getJSONObject(enumFace.getName());
		PlaneElement el = new PlaneElement();
		Matrix4f mat = ElUtil.rotMat(element, v0, v1, v2, v3);
		mat.mul(ElUtil.rotMat(face, v0, v1, v2, v3));
		ElUtil.rot(mat, v0, v1, v2, v3);
		Vector4f uv;

		if (!face.has("uv"))
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
		boolean biomeTint = face.optBoolean("biometint", false);

		el.modelLayer = ElUtil.layer(face);
		el.createTriangles(v0, v1, v2, v3, uv, tint, biomeTint);

		float rad = (float) Math.toRadians(face.optFloat("rotation", 0));
		el.t0.rotateUv(rad);
		el.t1.rotateUv(rad);

		String texture = face.getString("texture");
		BlockTextureHolder.putTexture(texture);
		el.setTexture(BlockTextureHolder.getTextureId(texture));

		return el;
	}

	public void cycleFaces()
	{
		PlaneElement temp = ps[0];
		ps[0] = ps[1];
		ps[1] = ps[2];
		ps[2] = ps[3];
		ps[3] = temp;
	}

	@Override
	public void fixUv(float texel)
	{
		if (ps[0] != null) ps[0].fixUv(texel);
		if (ps[1] != null) ps[1].fixUv(texel);
		if (ps[2] != null) ps[2].fixUv(texel);
		if (ps[3] != null) ps[3].fixUv(texel);
		if (up != null) up.fixUv(texel);
		if (down != null) down.fixUv(texel);
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
	public int build(ModelBuilder builder, ModelLayer layer, World world, BlockState state, int x, int y, int z)
	{
		return
			(ps[0] != null && Cull.renderFace(x, y, z, EnumFace.NORTH, name, state, world) ? ps[0].build(builder, layer, world, state, x, y, z) : 0) +
			(ps[1] != null && Cull.renderFace(x, y, z, EnumFace.EAST , name, state, world) ? ps[1].build(builder, layer, world, state, x, y, z) : 0) +
			(ps[2] != null && Cull.renderFace(x, y, z, EnumFace.SOUTH, name, state, world) ? ps[2].build(builder, layer, world, state, x, y, z) : 0) +
			(ps[3] != null && Cull.renderFace(x, y, z, EnumFace.WEST , name, state, world) ? ps[3].build(builder, layer, world, state, x, y, z) : 0) +
			(up != null && Cull.renderFace(x, y, z, EnumFace.UP   , name, state, world) ? up.build(builder, layer, world, state, x, y, z) : 0) +
			(down != null && Cull.renderFace(x, y, z, EnumFace.DOWN , name, state, world) ? down.build(builder, layer, world, state, x, y, z) : 0);
	}
}
