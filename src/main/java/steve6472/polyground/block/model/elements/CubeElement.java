package steve6472.polyground.block.model.elements;

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
	public PlaneElement north, east, south, west, up, down;
	public String name;

	public CubeElement()
	{

	}

	@Override
	public void load(JSONObject element)
	{
		Vector3f from = ElUtil.loadVertex3("from", element).div(16);
		Vector3f to = ElUtil.loadVertex3("to", element).div(16);

		name = element.optString("name", "");

		if (element.has("faces"))
		{
			JSONObject faces = element.getJSONObject("faces");

			if (faces.has("north")) north = face(EnumFace.NORTH, element, faces,
				new Vector3f(to.x, to.y, to.z),
				new Vector3f(to.x, from.y, to.z),
				new Vector3f(to.x, from.y, from.z),
				new Vector3f(to.x, to.y, from.z), from, to);
			if (faces.has("east"))  east = face(EnumFace.EAST, element, faces,
				new Vector3f(from.x, to.y, to.z),
				new Vector3f(from.x, from.y, to.z),
				new Vector3f(to.x, from.y, to.z),
				new Vector3f(to.x, to.y, to.z), from, to);
			if (faces.has("south")) south = face(EnumFace.SOUTH, element, faces,
				new Vector3f(from.x, to.y, from.z),
				new Vector3f(from.x, from.y, from.z),
				new Vector3f(from.x, from.y, to.z),
				new Vector3f(from.x, to.y, to.z), from, to);
			if (faces.has("west"))  west = face(EnumFace.WEST, element, faces,
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

	private PlaneUV createPlaneUV(JSONObject face, Vector3f from, Vector3f to, EnumFace enumFace)
	{
		if (!face.has("uv") && !face.has("uv0"))
		{
			float minX = from.x, minY = from.y, minZ = from.z, maxX = to.x, maxY = to.y, maxZ = to.z;
			return switch (enumFace)
			{
				case UP -> new PlaneUV(new Vector4f(minZ, 1.0f - maxX, maxZ, 1.0f - minX), face.optBoolean("uvlock", false));
				case DOWN -> //noinspection SuspiciousNameCombination
					new PlaneUV(new Vector4f(minZ, minX, maxZ, maxX), face.optBoolean("uvlock", false));
				case EAST -> new PlaneUV(new Vector4f(minX, 1.0f - maxY, maxX, 1.0f - minY), face.optBoolean("uvlock", false));
				case WEST -> new PlaneUV(new Vector4f(1.0f - maxX, 1.0f - maxY, 1.0f - minX, 1.0f - minY), face.optBoolean("uvlock", false));
				case NORTH -> new PlaneUV(new Vector4f(1.0f - maxZ, 1.0f - maxY, 1.0f - minZ, 1.0f - minY), face.optBoolean("uvlock", false));
				case SOUTH -> new PlaneUV(new Vector4f(minZ, 1.0f - maxY, maxZ, 1.0f - minY), face.optBoolean("uvlock", false));
				default -> throw new IllegalStateException("Unexpected value: " + enumFace);
			};
		} else if (!face.has("uv0"))
		{
			return new PlaneUV(ElUtil.loadVertex4("uv", face).div(16), face.optBoolean("uvlock", false));
		} else
		{
			return new PlaneUV().load(face);
		}
	}

	private PlaneElement face(EnumFace enumFace, JSONObject element, JSONObject faces, Vector3f v0, Vector3f v1, Vector3f v2, Vector3f v3, Vector3f from, Vector3f to)
	{
		JSONObject face = faces.getJSONObject(enumFace.getName());
		PlaneElement el = new PlaneElement(face.has("name") ? face.getString("name") : name + "_" + enumFace.name());

		Vector3f tint = ElUtil.tint(face);
		boolean biomeTint = face.optBoolean("biometint", false);
		PlaneUV uv = createPlaneUV(face, from, to, enumFace);

		el.cull = face.optBoolean("cull", true);

		ElUtil.rot(element, v0, v1, v2, v3);

		el.modelLayer = ElUtil.layer(face);
		if (face.optBoolean("uvlock", false) && !enumFace.isSide())
		{
			float sub = 0;
			if (element.has("rotation"))
				sub = element.getJSONArray("rotation").getFloat(1);
			el.createTriangles(v0, v1, v2, v3, ElUtil.EMPTY_MATRIX, uv, face.optFloat("rotation", 0) - sub, tint, biomeTint);
		} else
		{
			el.createTriangles(v0, v1, v2, v3, ElUtil.EMPTY_MATRIX, uv, face.optFloat("rotation", 0), tint, biomeTint);
		}

		String texture = face.getString("texture");
		BlockTextureHolder.putTexture(texture);
		el.setTexture(BlockTextureHolder.getTextureId(texture));

		return el;
	}

	public void cycleFacesY()
	{
		PlaneElement temp = north;
		north = east;
		east = south;
		south = west;
		west = temp;
	}

	@Override
	public void fixUv(float texel)
	{
		if (north != null) north.fixUv(texel);
		if (east  != null) east .fixUv(texel);
		if (south != null) south.fixUv(texel);
		if (west  != null) west .fixUv(texel);
		if (up != null)    up.fixUv(texel);
		if (down != null)  down.fixUv(texel);
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
			(state == null || north != null && north.cull && Cull.renderFace(x, y, z, EnumFace.NORTH, name, state, world) ? north.build(builder, layer, world, state, x, y, z) : 0) +
			(state == null || east  != null && east.cull  && Cull.renderFace(x, y, z, EnumFace.EAST , name, state, world) ? east .build(builder, layer, world, state, x, y, z) : 0) +
			(state == null || south != null && south.cull && Cull.renderFace(x, y, z, EnumFace.SOUTH, name, state, world) ? south.build(builder, layer, world, state, x, y, z) : 0) +
			(state == null || west  != null && west.cull  && Cull.renderFace(x, y, z, EnumFace.WEST , name, state, world) ? west .build(builder, layer, world, state, x, y, z) : 0) +
			(state == null || up    != null && up.cull    && Cull.renderFace(x, y, z, EnumFace.UP   , name, state, world) ? up.build(builder, layer, world, state, x, y, z) : 0) +
			(state == null || down  != null && down.cull  && Cull.renderFace(x, y, z, EnumFace.DOWN , name, state, world) ? down.build(builder, layer, world, state, x, y, z) : 0);
	}

	@Override
	public String getName()
	{
		return name;
	}
}
