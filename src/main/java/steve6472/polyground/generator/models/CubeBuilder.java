package steve6472.polyground.generator.models;

import org.joml.Vector3f;
import org.json.JSONArray;
import org.json.JSONObject;
import steve6472.polyground.EnumFace;
import steve6472.polyground.block.model.CubeTags;

public class CubeBuilder
{
	public static EnumFace[] SIDE = {EnumFace.NORTH, EnumFace.EAST, EnumFace.SOUTH, EnumFace.WEST};
	public static EnumFace[] NO_TOP = {EnumFace.NORTH, EnumFace.EAST, EnumFace.SOUTH, EnumFace.WEST, EnumFace.DOWN};
	public static EnumFace[] NO_BOTTOM = {EnumFace.NORTH, EnumFace.EAST, EnumFace.SOUTH, EnumFace.WEST, EnumFace.UP};
	public static EnumFace[] TOP_BOTTOM = {EnumFace.UP, EnumFace.DOWN};

	private float minX;
	private float minY;
	private float minZ;
	private float maxX;
	private float maxY;
	private float maxZ;
	private boolean isHitbox, isCollisionBox;
	private String name;
	private FaceBuilder[] faces;
	private Vector3f origin;
	private Vector3f rotation;

	public static CubeBuilder create()
	{
		return new CubeBuilder();
	}

	private CubeBuilder()
	{
		faces = new FaceBuilder[6];
		isHitbox = true;
		isCollisionBox = true;
		name = "";
	}

	public CubeBuilder origin(float x, float y, float z)
	{
		this.origin = new Vector3f(x, y, z);
		return this;
	}

	public CubeBuilder rotation(float x, float y, float z)
	{
		this.rotation = new Vector3f(x, y, z);
		return this;
	}

	public CubeBuilder size(float x, float y, float z, float w, float h, float d)
	{
		this.minX = x;
		this.minY = y;
		this.minZ = z;
		this.maxX = x + w;
		this.maxY = y + h;
		this.maxZ = z + d;
		return this;
	}

	public CubeBuilder min(float minX, float minY, float minZ)
	{
		this.minX = minX;
		this.minY = minY;
		this.minZ = minZ;
		return this;
	}

	public CubeBuilder max(float maxX, float maxY, float maxZ)
	{
		this.maxX = maxX;
		this.maxY = maxY;
		this.maxZ = maxZ;
		return this;
	}

	public CubeBuilder hitbox(boolean isHitbox)
	{
		this.isHitbox = isHitbox;
		return this;
	}

	public CubeBuilder name(String name)
	{
		this.name = name;
		return this;
	}

	public CubeBuilder collisionBox(boolean isCollisionBox)
	{
		this.isCollisionBox = isCollisionBox;
		return this;
	}

	public CubeBuilder face(FaceBuilder builder, EnumFace face)
	{
		this.faces[face.ordinal()] = builder;
		return this;
	}

	public CubeBuilder face(FaceBuilder builder, EnumFace... faces)
	{
		for (EnumFace f : faces)
		{
			this.faces[f.ordinal()] = builder;
		}
		return this;
	}

	public CubeBuilder face(FaceBuilder builder)
	{
		for (int i = 0; i < 6; i++)
		{
			faces[i] = builder;
		}
		return this;
	}

	/*
	 * Presets
	 */

	public CubeBuilder fullBlock()
	{
		min(0, 0, 0);
		max(16, 16, 16);
		return this;
	}

	public CubeBuilder torch()
	{
		min(7, 0, 7);
		max(9, 10, 9);
		return this;
	}

	public CubeBuilder bottomSlab()
	{
		min(0, 0, 0);
		max(16, 8, 16);
		return this;
	}

	public CubeBuilder topSlab()
	{
		min(0, 8, 0);
		max(16, 16, 16);
		return this;
	}

	public CubeBuilder stairTop()
	{
		min(0, 8, 0);
		max(8, 16, 16);
		name(CubeTags.STAIR_TOP);
		return this;
	}

	public CubeBuilder slab(boolean isBottom)
	{
		if (isBottom) bottomSlab(); else topSlab();
		return this;
	}

	public JSONObject build()
	{
		JSONObject main = new JSONObject();
		main.put("from", new JSONArray().put(minX).put(minY).put(minZ));
		main.put("to", new JSONArray().put(maxX).put(maxY).put(maxZ));
		if (!isHitbox)       main.put("isHitbox", false);
		if (!isCollisionBox) main.put("isCollisionBox", false);
		if (!name.isBlank()) main.put("name", name);
		if (origin != null)  main.put("point", new JSONArray().put(origin.x).put(origin.y).put(origin.z));
		if (rotation != null)main.put("rotation", new JSONArray().put(rotation.x).put(rotation.y).put(rotation.z));

		JSONObject faces = new JSONObject();
		if (this.faces[0] != null) faces.put("up", this.faces[0].build());
		if (this.faces[1] != null) faces.put("down", this.faces[1].build());
		if (this.faces[2] != null) faces.put("north", this.faces[2].build());
		if (this.faces[3] != null) faces.put("south", this.faces[3].build());
		if (this.faces[4] != null) faces.put("east", this.faces[4].build());
		if (this.faces[5] != null) faces.put("west", this.faces[5].build());
		if (!faces.isEmpty())
			main.put("faces", faces);

		return main;
	}
}