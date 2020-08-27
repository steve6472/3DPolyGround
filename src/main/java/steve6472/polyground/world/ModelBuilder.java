package steve6472.polyground.world;

import org.joml.*;
import steve6472.polyground.EnumFace;
import steve6472.polyground.block.BlockTextureHolder;
import steve6472.polyground.block.model.Cube;
import steve6472.polyground.block.model.CubeFace;
import steve6472.polyground.block.model.faceProperty.TintFaceProperty;
import steve6472.polyground.block.model.faceProperty.UVFaceProperty;
import steve6472.polyground.registry.face.FaceRegistry;
import steve6472.polyground.world.biomes.Biome;
import steve6472.polyground.world.chunk.SubChunk;

import java.awt.*;
import java.util.List;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 19.08.2019
 * Project: SJP
 *
 ***********************/
public final class ModelBuilder
{
	public int atlasSize;

	public float texel = 1f / 16f;

	private int x;
	private int y;
	private int z;
	private List<Vector3f> vert;
	private List<Vector4f> col;
	private List<Vector2f> text;
	private List<Vector3f> norm;
	private Cube cube;
	private SubChunk sc;

	public void load(List<Vector3f> vert, List<Vector4f> col, List<Vector2f> text, List<Vector3f> norm)
	{
		this.vert = vert;
		this.col = col;
		this.text = text;
		this.norm = norm;
	}

	public void load(int x, int y, int z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public void setCube(Cube cube)
	{
		this.cube = cube;
	}

	public void setSubChunk(SubChunk sc)
	{
		this.sc = sc;
	}

	public int getTextureId(Cube cube, EnumFace face)
	{
		return cube.getFace(face).getProperty(FaceRegistry.texture).getTextureId();
	}

	public void quad(Vector3f v0, Vector3f v1, Vector3f v2, Vector3f v3, Vector3f v4, Vector3f v5)
	{
		vert.add(v0.add(x, y, z));
		vert.add(v1.add(x, y, z));
		vert.add(v2.add(x, y, z));
		vert.add(v3);
		vert.add(v4.add(x, y, z));
		vert.add(v5);
	}

	public void tri(Vector3f v0, Vector3f v1, Vector3f v2)
	{
		vert.add(new Vector3f(v0).add(x, y, z));
		vert.add(new Vector3f(v1).add(x, y, z));
		vert.add(new Vector3f(v2).add(x, y, z));
	}

	public void uv(Vector2f uv)
	{
		text.add(uv);
	}

	public void uv(float u, float v)
	{
		text.add(new Vector2f(u, v));
	}

	public void texture00(EnumFace face, float minX, float minY, float maxX, float maxY)
	{
		Rectangle r = BlockTextureHolder.getTexture(getTextureId(cube, face));
		float x = r.x;
		float y = r.y;
		float w = r.width;
		float h = r.height;
		text.add(new Vector2f((x + w * minX) * texel, (y + h * minY) * texel));
	}

	public void texture01(EnumFace face, float minX, float minY, float maxX, float maxY)
	{
		Rectangle r = BlockTextureHolder.getTexture(getTextureId(cube, face));
		float x = r.x;
		float y = r.y;
		float w = r.width;
		float h = r.height;
		text.add(new Vector2f((x + w * minX) * texel, (y + h * maxY) * texel));
	}

	public void texture10(EnumFace face, float minX, float minY, float maxX, float maxY)
	{
		Rectangle r = BlockTextureHolder.getTexture(getTextureId(cube, face));
		float x = r.x;
		float y = r.y;
		float w = r.width;
		float h = r.height;
		text.add(new Vector2f((x + w * maxX) * texel, (y + h * minY) * texel));
	}

	public void texture11(EnumFace face, float minX, float minY, float maxX, float maxY)
	{
		Rectangle r = BlockTextureHolder.getTexture(getTextureId(cube, face));
		float x = r.x;
		float y = r.y;
		float w = r.width;
		float h = r.height;
		text.add(new Vector2f((x + w * maxX) * texel, (y + h * maxY) * texel));
	}

	public void sideTexture(EnumFace face)
	{
		CubeFace f = cube.getFace(face);

		UVFaceProperty uv = f.getProperty(FaceRegistry.uv);

		texture00(face, uv.getMinU(), uv.getMinV(), uv.getMaxU(), uv.getMaxV());
		texture01(face, uv.getMinU(), uv.getMinV(), uv.getMaxU(), uv.getMaxV());
		texture11(face, uv.getMinU(), uv.getMinV(), uv.getMaxU(), uv.getMaxV());

		texture11(face, uv.getMinU(), uv.getMinV(), uv.getMaxU(), uv.getMaxV());
		texture10(face, uv.getMinU(), uv.getMinV(), uv.getMaxU(), uv.getMaxV());
		texture00(face, uv.getMinU(), uv.getMinV(), uv.getMaxU(), uv.getMaxV());
	}

	public void topFaceTextures()
	{
		CubeFace f = cube.getFace(EnumFace.UP);

		UVFaceProperty uv = f.getProperty(FaceRegistry.uv);

		texture00(EnumFace.UP, uv.getMinU(), uv.getMinV(), uv.getMaxU(), uv.getMaxV());
		texture01(EnumFace.UP, uv.getMinU(), uv.getMinV(), uv.getMaxU(), uv.getMaxV());
		texture11(EnumFace.UP, uv.getMinU(), uv.getMinV(), uv.getMaxU(), uv.getMaxV());

		texture11(EnumFace.UP, uv.getMinU(), uv.getMinV(), uv.getMaxU(), uv.getMaxV());
		texture10(EnumFace.UP, uv.getMinU(), uv.getMinV(), uv.getMaxU(), uv.getMaxV());
		texture00(EnumFace.UP, uv.getMinU(), uv.getMinV(), uv.getMaxU(), uv.getMaxV());
	}

	public void bottomFaceTextures()
	{
		CubeFace f = cube.getFace(EnumFace.DOWN);

		UVFaceProperty uv = f.getProperty(FaceRegistry.uv);

		texture00(EnumFace.DOWN, uv.getMinU(), uv.getMinV(), uv.getMaxU(), uv.getMaxV());
		texture01(EnumFace.DOWN, uv.getMinU(), uv.getMinV(), uv.getMaxU(), uv.getMaxV());
		texture11(EnumFace.DOWN, uv.getMinU(), uv.getMinV(), uv.getMaxU(), uv.getMaxV());

		texture11(EnumFace.DOWN, uv.getMinU(), uv.getMinV(), uv.getMaxU(), uv.getMaxV());
		texture10(EnumFace.DOWN, uv.getMinU(), uv.getMinV(), uv.getMaxU(), uv.getMaxV());
		texture00(EnumFace.DOWN, uv.getMinU(), uv.getMinV(), uv.getMaxU(), uv.getMaxV());
	}

	public void texture(EnumFace face)
	{
		switch (face)
		{
			case UP -> topFaceTextures();
			case DOWN -> bottomFaceTextures();
			case NORTH, SOUTH, EAST, WEST -> sideTexture(face);
		}
	}

	public int face(EnumFace face)
	{
		int verts = switch (face)
			{
				case UP -> topFace();
				case DOWN -> bottomFace();
				case NORTH -> positiveXFace();
				case SOUTH -> negativeXFace();
				case EAST -> positiveZFace();
				case WEST -> negativeZFace();
				default -> 0;
			};

		if (verts != 0)
		{
			if (cube.getFace(face).hasProperty(FaceRegistry.biomeTint))
				biomeTint(cube.getFace(face));
			if (cube.getFace(face).hasProperty(FaceRegistry.tint))
				tint(cube.getFace(face));
		}

		return verts;
	}

	public void removeFaceColors()
	{
		for (int j = 0; j < 6; j++)
		{
			getCol().remove(getCol().size() - 1);
		}
	}

	public void biomeTint(CubeFace face)
	{
		removeFaceColors();

		if (sc == null)
		{
			for (int j = 0; j < 6; j++)
			{
				shade(92 / 255f, 184 / 255f, 64 / 255f, face);
			}
		} else
		{
			Biome b = sc.getWorld().biomes.getBiome(sc.getBiomeId(x, y, z));

			for (int j = 0; j < 6; j++)
			{
				shade(b.getColor().x, b.getColor().y, b.getColor().z, face);
			}
		}
	}

	private final Vector3f ONE = new Vector3f(92 / 255f, 184 / 255f, 64 / 255f);

	public Vector3f getBiomeTint()
	{
		if (sc == null)
			return ONE;
		else
			return sc.getWorld().biomes.getBiome(sc.getBiomeId(x, y, z)).getColor();
	}

	public void tint(CubeFace face)
	{
		TintFaceProperty tint = face.getProperty(FaceRegistry.tint);

		removeFaceColors();

		for (int j = 0; j < 6; j++)
		{
			shade(tint.getRed(), tint.getGreen(), tint.getBlue(), face);
		}
	}

	public void setShade(List<Vector4f> color, CubeFace face)
	{
		for (int i = 0; i < 6; i++)
		{
			float f = face.getShade();
			color.add(new Vector4f(f, f, f, 1.0f));
		}

	}

	public void shade(float r, float g, float b, CubeFace face)
	{
		float f = face.getShade();
		getCol().add(new Vector4f(r * f, g * f, b * f, 1.0f));
	}

	public void colorTri(float r, float g, float b)
	{
		for (int i = 0; i < 3; i++)
		{
			getCol().add(new Vector4f(r, g, b, 1.0f));
		}
	}

	public void normal(float x, float y, float z)
	{
		Vector3f v = new Vector3f(x, y, z);
		for (int i = 0; i < 6; i++)
		{
			norm.add(v);
		}
	}

	public void normalTri(Vector3f normal)
	{
		for (int i = 0; i < 3; i++)
		{
			norm.add(normal);
		}
	}

	public int negativeZFace()
	{
		if (cube.getFace(EnumFace.WEST) != null)
			return negativeZFace(EnumFace.WEST);
		else
			return 0;
	}

	public int negativeZFace(EnumFace face)
	{
		setShade(col, cube.getFace(face));
		texture(face);
		AABBf a = cube.getAabb();

		normal(0, 0, -1);

		Vector3f v1 = new Vector3f(a.maxX, a.maxY, a.minZ);
		Vector3f v2 = new Vector3f(a.maxX, a.minY, a.minZ);
		Vector3f v3 = new Vector3f(a.minX, a.minY, a.minZ);
		Vector3f v4 = new Vector3f(a.minX, a.maxY, a.minZ);

		int rotation =
			cube.getFace(face).hasProperty(FaceRegistry.rotation) ?
				cube.getFace(face).getProperty(FaceRegistry.rotation).getRotation() :
				0;

		switch (rotation)
		{
			case 90 -> quad(v2, v3, v4, v4, v1, v2);
			case 180 -> quad(v3, v4, v1, v1, v2, v3);
			case 270 -> quad(v4, v1, v2, v2, v3, v4);
			default -> quad(v1, v2, v3, v3, v4, v1);
		}

		return 6;
	}

	public int positiveZFace()
	{
		if (cube.getFace(EnumFace.EAST) != null)
			return positiveZFace(EnumFace.EAST);
		else
			return 0;
	}

	public int positiveZFace(EnumFace face)
	{
		setShade(col, cube.getFace(face));
		texture(face);
		AABBf a = cube.getAabb();

		normal(0, 0, 1);

		Vector3f v1 = new Vector3f(a.minX, a.maxY, a.maxZ);
		Vector3f v2 = new Vector3f(a.minX, a.minY, a.maxZ);
		Vector3f v3 = new Vector3f(a.maxX, a.minY, a.maxZ);
		Vector3f v4 = new Vector3f(a.maxX, a.maxY, a.maxZ);

		int rotation =
			cube.getFace(face).hasProperty(FaceRegistry.rotation) ?
				cube.getFace(face).getProperty(FaceRegistry.rotation).getRotation() :
				0;

		switch (rotation)
		{
			case 90 -> quad(v2, v3, v4, v4, v1, v2);
			case 180 -> quad(v3, v4, v1, v1, v2, v3);
			case 270 -> quad(v4, v1, v2, v2, v3, v4);
			default -> quad(v1, v2, v3, v3, v4, v1);
		}

		return 6;
	}

	public int negativeXFace()
	{
		if (cube.getFace(EnumFace.SOUTH) != null)
			return negativeXFace(EnumFace.SOUTH);
		else
			return 0;
	}

	public int negativeXFace(EnumFace face)
	{
		setShade(col, cube.getFace(face));
		texture(face);
		AABBf a = cube.getAabb();

		normal(-1, 0, 0);

		Vector3f v1 = new Vector3f(a.minX, a.maxY, a.minZ);
		Vector3f v2 = new Vector3f(a.minX, a.minY, a.minZ);
		Vector3f v3 = new Vector3f(a.minX, a.minY, a.maxZ);
		Vector3f v4 = new Vector3f(a.minX, a.maxY, a.maxZ);

		int rotation =
			cube.getFace(face).hasProperty(FaceRegistry.rotation) ?
				cube.getFace(face).getProperty(FaceRegistry.rotation).getRotation() :
				0;

		switch (rotation)
		{
			case 90 -> quad(v2, v3, v4, v4, v1, v2);
			case 180 -> quad(v3, v4, v1, v1, v2, v3);
			case 270 -> quad(v4, v1, v2, v2, v3, v4);
			default -> quad(v1, v2, v3, v3, v4, v1);
		}

		return 6;
	}

	public int positiveXFace()
	{
		if (cube.getFace(EnumFace.NORTH) != null)
			return positiveXFace(EnumFace.NORTH);
		else
			return 0;
	}

	public int positiveXFace(EnumFace face)
	{
		setShade(col, cube.getFace(face));
		texture(face);
		AABBf a = cube.getAabb();

		normal(1, 0, 0);

		Vector3f v1 = new Vector3f(a.maxX, a.maxY, a.maxZ);
		Vector3f v2 = new Vector3f(a.maxX, a.minY, a.maxZ);
		Vector3f v3 = new Vector3f(a.maxX, a.minY, a.minZ);
		Vector3f v4 = new Vector3f(a.maxX, a.maxY, a.minZ);

		int rotation =
			cube.getFace(face).hasProperty(FaceRegistry.rotation) ?
				cube.getFace(face).getProperty(FaceRegistry.rotation).getRotation() :
				0;

		switch (rotation)
		{
			case 90 -> quad(v2, v3, v4, v4, v1, v2);
			case 180 -> quad(v3, v4, v1, v1, v2, v3);
			case 270 -> quad(v4, v1, v2, v2, v3, v4);
			default -> quad(v1, v2, v3, v3, v4, v1);
		}

		return 6;
	}

	public int topFace()
	{
		if (cube.getFace(EnumFace.UP) != null)
			return topFace(EnumFace.UP);
		else
			return 0;
	}

	public int topFace(EnumFace face)
	{
		setShade(col, cube.getFace(face));
		texture(face);
		AABBf a = cube.getAabb();

		normal(0, 1, 0);

		Vector3f v1 = new Vector3f(a.maxX, a.maxY, a.minZ);
		Vector3f v2 = new Vector3f(a.minX, a.maxY, a.minZ);
		Vector3f v3 = new Vector3f(a.minX, a.maxY, a.maxZ);
		Vector3f v4 = new Vector3f(a.maxX, a.maxY, a.maxZ);

		int rotation =
			cube.getFace(face).hasProperty(FaceRegistry.rotation) ?
				cube.getFace(face).getProperty(FaceRegistry.rotation).getRotation() :
				0;

		switch (rotation)
		{
			case 90 -> quad(v2, v3, v4, v4, v1, v2);
			case 180 -> quad(v3, v4, v1, v1, v2, v3);
			case 270 -> quad(v4, v1, v2, v2, v3, v4);
			default -> quad(v1, v2, v3, v3, v4, v1);
		}

		return 6;
	}

	public int bottomFace()
	{
		if (cube.getFace(EnumFace.DOWN) != null)
			return bottomFace(EnumFace.DOWN);
		else
			return 0;
	}

	public int bottomFace(EnumFace face)
	{
		setShade(col, cube.getFace(face));
		texture(face);
		AABBf a = cube.getAabb();

		normal(0, -1, 0);

		Vector3f v1 = new Vector3f(a.minX, a.minY, a.minZ);
		Vector3f v2 = new Vector3f(a.maxX, a.minY, a.minZ);
		Vector3f v3 = new Vector3f(a.maxX, a.minY, a.maxZ);
		Vector3f v4 = new Vector3f(a.minX, a.minY, a.maxZ);

		int rotation =
			cube.getFace(face).hasProperty(FaceRegistry.rotation) ?
				cube.getFace(face).getProperty(FaceRegistry.rotation).getRotation() :
				0;

		switch (rotation)
		{
			case 90 -> quad(v2, v3, v4, v4, v1, v2);
			case 180 -> quad(v3, v4, v1, v1, v2, v3);
			case 270 -> quad(v4, v4, v2, v2, v3, v4);
			default -> quad(v1, v2, v3, v3, v4, v1);
		}

		return 6;
	}

	public List<Vector3f> getVert()
	{
		return vert;
	}

	public List<Vector4f> getCol()
	{
		return col;
	}

	public List<Vector2f> getText()
	{
		return text;
	}


}
