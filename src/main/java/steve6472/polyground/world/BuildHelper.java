package steve6472.polyground.world;

import org.joml.AABBf;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import steve6472.polyground.EnumFace;
import steve6472.polyground.block.model.Cube;
import steve6472.polyground.block.model.CubeFace;
import steve6472.polyground.block.model.faceProperty.TintFaceProperty;
import steve6472.polyground.block.model.faceProperty.UVFaceProperty;
import steve6472.polyground.registry.face.FaceRegistry;
import steve6472.polyground.world.biomes.Biome;
import steve6472.polyground.world.biomes.Biomes;
import steve6472.polyground.world.chunk.SubChunk;

import java.util.List;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 19.08.2019
 * Project: SJP
 *
 ***********************/
public final class BuildHelper
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

	private int getTextureId(Cube cube, EnumFace face)
	{
		return cube.getFace(face).getProperty(FaceRegistry.texture).getTextureId();
	}

	private void quad(Vector3f v0, Vector3f v1, Vector3f v2, Vector3f v3, Vector3f v4, Vector3f v5)
	{
		vert.add(v0.add(x, y, z));
		vert.add(v1.add(x, y, z));
		vert.add(v2.add(x, y, z));
		vert.add(v3);
		vert.add(v4.add(x, y, z));
		vert.add(v5);
	}

	private void texture00(EnumFace face, float minX, float minY, float maxX, float maxY)
	{
		float x = getTextureId(cube, face) % atlasSize;
		//noinspection IntegerDivisionInFloatingPointContext
		float y = getTextureId(cube, face) / atlasSize;
		text.add(new Vector2f(texel * x + minX * texel, texel * y + minY * texel));
	}

	private void texture01(EnumFace face, float minX, float minY, float maxX, float maxY)
	{
		float x = getTextureId(cube, face) % atlasSize;
		//noinspection IntegerDivisionInFloatingPointContext
		float y = getTextureId(cube, face) / atlasSize;
		text.add(new Vector2f(texel * x + minX * texel, texel * y + maxY * texel));
	}

	private void texture10(EnumFace face, float minX, float minY, float maxX, float maxY)
	{
		float x = getTextureId(cube, face) % atlasSize;
		//noinspection IntegerDivisionInFloatingPointContext
		float y = getTextureId(cube, face) / atlasSize;
		text.add(new Vector2f(texel * x + maxX * texel, texel * y + minY * texel));
	}

	private void texture11(EnumFace face, float minX, float minY, float maxX, float maxY)
	{
		float x = getTextureId(cube, face) % atlasSize;
		//noinspection IntegerDivisionInFloatingPointContext
		float y = getTextureId(cube, face) / atlasSize;
		text.add(new Vector2f(texel * x + maxX * texel, texel * y + maxY * texel));
	}

	private void sideTexture(EnumFace face)
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

	private void topFaceTextures()
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

	private void bottomFaceTextures()
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

	private void texture(EnumFace face)
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

	private void removeFaceColors()
	{
		for (int j = 0; j < 6; j++)
		{
			getCol().remove(getCol().size() - 1);
		}
	}

	private void biomeTint(CubeFace face)
	{
		removeFaceColors();

		int biomeId = sc == null ? 0 : sc.getBiomeId(x, y, z);

		Biome b = Biomes.getBiome(biomeId);

		for (int j = 0; j < 6; j++)
		{
			shade(b.getColor().x, b.getColor().y, b.getColor().z, face);
		}
	}

	private void tint(CubeFace face)
	{
		TintFaceProperty tint = face.getProperty(FaceRegistry.tint);

		removeFaceColors();

		for (int j = 0; j < 6; j++)
		{
			shade(tint.getRed(), tint.getGreen(), tint.getBlue(), face);
		}
	}

	private void setShade(List<Vector4f> color, CubeFace face)
	{
		for (int i = 0; i < 6; i++)
		{
			float f = face.getShade();
			color.add(new Vector4f(f, f, f, 1.0f));
		}

	}

	private void shade(float r, float g, float b, CubeFace face)
	{
		float f = face.getShade();
		getCol().add(new Vector4f(r * f, g * f, b * f, 1.0f));
	}

	private void normal(float x, float y, float z)
	{
		Vector3f v = new Vector3f(x, y, z);
		for (int i = 0; i < 6; i++)
		{
			norm.add(v);
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

	public void replaceLastFaceWithErrorTexture(float r0, float g0, float b0, float r1, float g1, float b1)
	{
		for (int i = 0; i < 6; i++)
		{
			col.remove(col.size() - 1);
		}

		for (int i = 0; i < 3; i++)
		{
			col.add(new Vector4f(r0, g0, b0, 1.0f));
		}

		for (int i = 0; i < 3; i++)
		{
			col.add(new Vector4f(r1, g1, b1, 1.0f));
		}
	}

	public void replaceLastFaceWithErrorTexture()
	{
		replaceLastFaceWithErrorTexture(0, 256, 256, 0, 0, 0);
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
