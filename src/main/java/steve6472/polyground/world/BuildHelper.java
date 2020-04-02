package steve6472.polyground.world;

import org.joml.AABBf;
import steve6472.polyground.EnumFace;
import steve6472.polyground.block.model.CubeFace;
import steve6472.polyground.block.model.faceProperty.RotationFaceProperty;
import steve6472.polyground.block.model.faceProperty.TintFaceProperty;
import steve6472.polyground.block.model.faceProperty.UVFaceProperty;
import steve6472.polyground.block.model.Cube;
import steve6472.polyground.registry.face.FaceRegistry;
import steve6472.polyground.world.biomes.Biome;
import steve6472.polyground.world.biomes.registry.BiomeRegistry;
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
	private List<Float> vert;
	private List<Float> col;
	private List<Float> text;
	private List<Float> norm;
	private Cube cube;
	private SubChunk sc;

	public void load(List<Float> vert, List<Float> col, List<Float> text, List<Float> norm)
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

	private void vert(float ox, float oy, float oz)
	{
		vert.add(ox + x);
		vert.add(oy + y);
		vert.add(oz + z);
	}

	private void tri(float ix, float iy, float iz, float jx, float jy, float jz, float kx, float ky, float kz)
	{
		vert(ix, iy, iz);
		vert(jx, jy, jz);
		vert(kx, ky, kz);
	}

	private void texture00(EnumFace face, float minX, float minY, float maxX, float maxY)
	{
		float x = getTextureId(cube, face) % atlasSize;
		//noinspection IntegerDivisionInFloatingPointContext
		float y = getTextureId(cube, face) / atlasSize;
		text.add(texel * x + minX * texel);
		text.add(texel * y + minY * texel);
	}

	private void texture01(EnumFace face, float minX, float minY, float maxX, float maxY)
	{
		float x = getTextureId(cube, face) % atlasSize;
		//noinspection IntegerDivisionInFloatingPointContext
		float y = getTextureId(cube, face) / atlasSize;
		text.add(texel * x + minX * texel);
		text.add(texel * y + maxY * texel);
	}

	private void texture10(EnumFace face, float minX, float minY, float maxX, float maxY)
	{
		float x = getTextureId(cube, face) % atlasSize;
		//noinspection IntegerDivisionInFloatingPointContext
		float y = getTextureId(cube, face) / atlasSize;
		text.add(texel * x + maxX * texel);
		text.add(texel * y + minY * texel);
	}

	private void texture11(EnumFace face, float minX, float minY, float maxX, float maxY)
	{
		float x = getTextureId(cube, face) % atlasSize;
		//noinspection IntegerDivisionInFloatingPointContext
		float y = getTextureId(cube, face) / atlasSize;
		text.add(texel * x + maxX * texel);
		text.add(texel * y + maxY * texel);
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
		for (int j = 0; j < 24; j++)
		{
			getCol().remove(getCol().size() - 1);
		}
	}

	private void biomeTint(CubeFace face)
	{
		removeFaceColors();

		int biomeId = sc == null ? 0 : sc.getBiomeId(x, y, z);

		Biome b = switch (biomeId)
			{
				case 0 -> BiomeRegistry.voidBiome.createNew();
				case 1 -> BiomeRegistry.redLand.createNew();
				case 2 -> BiomeRegistry.greenLand.createNew();
				case 3 -> BiomeRegistry.blueLand.createNew();
				default -> throw new IllegalStateException("Unexpected value: " + biomeId);
			};

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

	private void setShade(List<Float> color, CubeFace face)
	{
		for (int i = 0; i < 6; i++)
		{
			color.add(face.getShade());
			color.add(face.getShade());
			color.add(face.getShade());
			color.add(1f);
		}

	}

	private void shade(float r, float g, float b, CubeFace face)
	{
		getCol().add(r * face.getShade());
		getCol().add(g * face.getShade());
		getCol().add(b * face.getShade());
		getCol().add(1.0f);
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

		for (int i = 0; i < 6; i++)
		{
			norm.add(0f);
			norm.add(0f);
			norm.add(-1f);
		}

		vert(a.maxX, a.maxY, a.minZ);
		vert(a.maxX, a.minY, a.minZ);
		vert(a.minX, a.minY, a.minZ);

		/* */

		vert(a.minX, a.minY, a.minZ);
		vert(a.minX, a.maxY, a.minZ);
		vert(a.maxX, a.maxY, a.minZ);

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

		for (int i = 0; i < 6; i++)
		{
			norm.add(0f);
			norm.add(0f);
			norm.add(1f);
		}

		vert(a.minX, a.maxY, a.maxZ);
		vert(a.minX, a.minY, a.maxZ);
		vert(a.maxX, a.minY, a.maxZ);

		/* */

		vert(a.maxX, a.minY, a.maxZ);
		vert(a.maxX, a.maxY, a.maxZ);
		vert(a.minX, a.maxY, a.maxZ);

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

		for (int i = 0; i < 6; i++)
		{
			norm.add(-1f);
			norm.add(0f);
			norm.add(0f);
		}

		vert(a.minX, a.maxY, a.minZ);
		vert(a.minX, a.minY, a.minZ);
		vert(a.minX, a.minY, a.maxZ);

		/* */

		vert(a.minX, a.minY, a.maxZ);
		vert(a.minX, a.maxY, a.maxZ);
		vert(a.minX, a.maxY, a.minZ);

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

		for (int i = 0; i < 6; i++)
		{
			norm.add(1f);
			norm.add(0f);
			norm.add(0f);
		}

		vert(a.maxX, a.maxY, a.maxZ);
		vert(a.maxX, a.minY, a.maxZ);
		vert(a.maxX, a.minY, a.minZ);

		/* */

		vert(a.maxX, a.minY, a.minZ);
		vert(a.maxX, a.maxY, a.minZ);
		vert(a.maxX, a.maxY, a.maxZ);

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

		for (int i = 0; i < 6; i++)
		{
			norm.add(0f);
			norm.add(1f);
			norm.add(0f);
		}

		if (cube.getFace(face).hasProperty(FaceRegistry.rotation))
		{
			RotationFaceProperty.EnumRotation rotation = cube.getFace(face).getProperty(FaceRegistry.rotation).getRotation();
			switch (rotation)
			{
				case R_90 -> {
					vert(a.minX, a.maxY, a.minZ);
					vert(a.minX, a.maxY, a.maxZ);
					vert(a.maxX, a.maxY, a.maxZ);

					vert(a.maxX, a.maxY, a.maxZ);
					vert(a.maxX, a.maxY, a.minZ);
					vert(a.minX, a.maxY, a.minZ);
				}
				case R_180 -> {
					vert(a.minX, a.maxY, a.maxZ);
					vert(a.maxX, a.maxY, a.maxZ);
					vert(a.maxX, a.maxY, a.minZ);

					vert(a.maxX, a.maxY, a.minZ);
					vert(a.minX, a.maxY, a.minZ);
					vert(a.minX, a.maxY, a.maxZ);
				}
				case R_270 -> {
					vert(a.maxX, a.maxY, a.maxZ);
					vert(a.maxX, a.maxY, a.minZ);
					vert(a.minX, a.maxY, a.minZ);

					vert(a.minX, a.maxY, a.minZ);
					vert(a.minX, a.maxY, a.maxZ);
					vert(a.maxX, a.maxY, a.maxZ);
				}
			}
		} else
		{
			vert(a.maxX, a.maxY, a.minZ);
			vert(a.minX, a.maxY, a.minZ);
			vert(a.minX, a.maxY, a.maxZ);

			vert(a.minX, a.maxY, a.maxZ);
			vert(a.maxX, a.maxY, a.maxZ);
			vert(a.maxX, a.maxY, a.minZ);
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

		for (int i = 0; i < 6; i++)
		{
			norm.add(0f);
			norm.add(-1f);
			norm.add(0f);
		}
/*
		if (cube.getFace(face).hasProperty(FaceRegistry.rotation))
		{
			RotationFaceProperty.EnumRotation rotation = cube.getFace(face).getProperty(FaceRegistry.rotation).getRotation();
		} else*/
		{
			vert(a.minX, a.minY, a.minZ);
			vert(a.maxX, a.minY, a.minZ);
			vert(a.maxX, a.minY, a.maxZ);

			/* */

			vert(a.maxX, a.minY, a.maxZ);
			vert(a.minX, a.minY, a.maxZ);
			vert(a.minX, a.minY, a.minZ);
		}

		return 6;
	}

	public void replaceLastFaceWithErrorTexture(float r0, float g0, float b0, float r1, float g1, float b1)
	{
		for (int i = 0; i < 6 * 4; i++)
		{
			col.remove(col.size() - 1);
		}

		for (int i = 0; i < 3; i++)
		{
			col.add(r0);
			col.add(g0);
			col.add(b0);
			col.add(1f);
		}

		for (int i = 0; i < 3; i++)
		{
			col.add(r1);
			col.add(g1);
			col.add(b1);
			col.add(1f);
		}
	}

	public void replaceLastFaceWithErrorTexture()
	{
		replaceLastFaceWithErrorTexture(0, 256, 256, 0, 0, 0);
	}

	public List<Float> getVert()
	{
		return vert;
	}

	public List<Float> getCol()
	{
		return col;
	}

	public List<Float> getText()
	{
		return text;
	}


}
