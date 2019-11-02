package com.steve6472.polyground.world;

import com.steve6472.polyground.EnumFace;
import com.steve6472.polyground.block.model.CubeFace;
import com.steve6472.polyground.block.model.faceProperty.EmissiveFaceProperty;
import com.steve6472.polyground.block.model.faceProperty.TintFaceProperty;
import com.steve6472.polyground.block.model.faceProperty.UVFaceProperty;
import com.steve6472.polyground.block.model.faceProperty.condition.ConditionFaceProperty;
import com.steve6472.polyground.block.model.registry.Cube;
import com.steve6472.polyground.block.model.registry.face.FaceRegistry;
import org.joml.AABBf;

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
	private List<Float> emissive;
	private Cube cube;
	private SubChunk sc;

	public void load(int x, int y, int z, List<Float> vert, List<Float> col, List<Float> text, List<Float> emissive)
	{
		this.x = x;
		this.y = y;
		this.z = z;
		this.vert = vert;
		this.col = col;
		this.text = text;
		this.emissive = emissive;
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
		if (sc != null && cube.getFace(face).hasProperty(FaceRegistry.conditionedTexture))
		{
			return ConditionFaceProperty.getTexture(cube.getFace(face).getProperty(FaceRegistry.conditionedTexture), x, y, z, sc);
		}
		return cube.getFace(face).getProperty(FaceRegistry.texture).getTextureId();
	}

	private void texture00(EnumFace face, float minX, float minY, float maxX, float maxY)
	{
		float x = getTextureId(cube, face) % atlasSize;
		float y = getTextureId(cube, face) / atlasSize;
		text.add(texel * x + minX * texel);
		text.add(texel * y + minY * texel);
	}

	private void texture01(EnumFace face, float minX, float minY, float maxX, float maxY)
	{
		float x = getTextureId(cube, face) % atlasSize;
		float y = getTextureId(cube, face) / atlasSize;
		text.add(texel * x + minX * texel);
		text.add(texel * y + maxY * texel);
	}

	private void texture10(EnumFace face, float minX, float minY, float maxX, float maxY)
	{
		float x = getTextureId(cube, face) % atlasSize;
		float y = getTextureId(cube, face) / atlasSize;
		text.add(texel * x + maxX * texel);
		text.add(texel * y + minY * texel);
	}

	private void texture11(EnumFace face, float minX, float minY, float maxX, float maxY)
	{
		float x = getTextureId(cube, face) % atlasSize;
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
		int tris = switch (face)
			{
				case UP -> topFace();
				case DOWN -> bottomFace();
				case NORTH -> positiveXFace();
				case SOUTH -> negativeXFace();
				case EAST -> positiveZFace();
				case WEST -> negativeZFace();
				default -> 0;
			};

		if (tris != 0)
		{
			if (cube.getFace(face).hasProperty(FaceRegistry.tint))
				recolor(cube.getFace(face));
		}

		return tris;
	}

	private void recolor(CubeFace face)
	{
		TintFaceProperty tint = face.getProperty(FaceRegistry.tint);

		for (int j = 0; j < 24; j++)
		{
			getCol().remove(getCol().size() - 1);
		}
		for (int j = 0; j < 6; j++)
		{
			shade(tint.getRed(), tint.getGreen(), tint.getBlue(), face.getShade());
		}
	}

	private void shade(float r, float g, float b, float shade)
	{
		getCol().add(r * shade);
		getCol().add(g * shade);
		getCol().add(b * shade);
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
		setShade(col, cube.getFace(face).getShade());
		setEmissive(face);

		AABBf a = cube.getAabb();

		texture(face);

		vert.add(a.maxX + x);
		vert.add(a.maxY + y);
		vert.add(a.minZ + z);

		vert.add(a.maxX + x);
		vert.add(a.minY + y);
		vert.add(a.minZ + z);

		vert.add(a.minX + x);
		vert.add(a.minY + y);
		vert.add(a.minZ + z);

		/* */

		vert.add(a.minX + x);
		vert.add(a.minY + y);
		vert.add(a.minZ + z);

		vert.add(a.minX + x);
		vert.add(a.maxY + y);
		vert.add(a.minZ + z);

		vert.add(a.maxX + x);
		vert.add(a.maxY + y);
		vert.add(a.minZ + z);

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
		setShade(col, cube.getFace(face).getShade());
		setEmissive(face);

		AABBf a = cube.getAabb();

		texture(face);

		vert.add(a.minX + x);
		vert.add(a.maxY + y);
		vert.add(a.maxZ + z);

		vert.add(a.minX + x);
		vert.add(a.minY + y);
		vert.add(a.maxZ + z);

		vert.add(a.maxX + x);
		vert.add(a.minY + y);
		vert.add(a.maxZ + z);

		/* */

		vert.add(a.maxX + x);
		vert.add(a.minY + y);
		vert.add(a.maxZ + z);

		vert.add(a.maxX + x);
		vert.add(a.maxY + y);
		vert.add(a.maxZ + z);

		vert.add(a.minX + x);
		vert.add(a.maxY + y);
		vert.add(a.maxZ + z);

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
		setShade(col, cube.getFace(face).getShade());
		setEmissive(face);

		AABBf a = cube.getAabb();

		texture(face);

		vert.add(a.minX + x);
		vert.add(a.maxY + y);
		vert.add(a.minZ + z);

		vert.add(a.minX + x);
		vert.add(a.minY + y);
		vert.add(a.minZ + z);

		vert.add(a.minX + x);
		vert.add(a.minY + y);
		vert.add(a.maxZ + z);

		/* */

		vert.add(a.minX + x);
		vert.add(a.minY + y);
		vert.add(a.maxZ + z);

		vert.add(a.minX + x);
		vert.add(a.maxY + y);
		vert.add(a.maxZ + z);

		vert.add(a.minX + x);
		vert.add(a.maxY + y);
		vert.add(a.minZ + z);

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
		setShade(col, cube.getFace(face).getShade());
		setEmissive(face);

		AABBf a = cube.getAabb();

		texture(face);

		vert.add(a.maxX + x);
		vert.add(a.maxY + y);
		vert.add(a.maxZ + z);

		vert.add(a.maxX + x);
		vert.add(a.minY + y);
		vert.add(a.maxZ + z);

		vert.add(a.maxX + x);
		vert.add(a.minY + y);
		vert.add(a.minZ + z);

		/* */

		vert.add(a.maxX + x);
		vert.add(a.minY + y);
		vert.add(a.minZ + z);

		vert.add(a.maxX + x);
		vert.add(a.maxY + y);
		vert.add(a.minZ + z);

		vert.add(a.maxX + x);
		vert.add(a.maxY + y);
		vert.add(a.maxZ + z);

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
		setShade(col, cube.getFace(face).getShade());
		setEmissive(face);

		AABBf a = cube.getAabb();

		texture(face);

		vert.add(a.maxX + x);
		vert.add(a.maxY + y);
		vert.add(a.minZ + z);

		vert.add(a.minX + x);
		vert.add(a.maxY + y);
		vert.add(a.minZ + z);

		vert.add(a.minX + x);
		vert.add(a.maxY + y);
		vert.add(a.maxZ + z);

		/* */

		vert.add(a.minX + x);
		vert.add(a.maxY + y);
		vert.add(a.maxZ + z);

		vert.add(a.maxX + x);
		vert.add(a.maxY + y);
		vert.add(a.maxZ + z);

		vert.add(a.maxX + x);
		vert.add(a.maxY + y);
		vert.add(a.minZ + z);

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
		setShade(col, cube.getFace(face).getShade());
		setEmissive(face);

		AABBf a = cube.getAabb();

		texture(face);

		vert.add(a.minX + x);
		vert.add(a.minY + y);
		vert.add(a.minZ + z);

		vert.add(a.maxX + x);
		vert.add(a.minY + y);
		vert.add(a.minZ + z);

		vert.add(a.maxX + x);
		vert.add(a.minY + y);
		vert.add(a.maxZ + z);

		/* */

		vert.add(a.maxX + x);
		vert.add(a.minY + y);
		vert.add(a.maxZ + z);

		vert.add(a.minX + x);
		vert.add(a.minY + y);
		vert.add(a.maxZ + z);

		vert.add(a.minX + x);
		vert.add(a.minY + y);
		vert.add(a.minZ + z);

		return 6;
	}

	private void setEmissive(EnumFace face)
	{
		float f = 0f;
		if (EmissiveFaceProperty.check(cube.getFace(face)))
		{
			f = 1f;
		}

		for (int i = 0; i < 6; i++)
		{
			emissive.add(f);
		}
	}

	private void setShade(List<Float> color, float shade)
	{
		for (int i = 0; i < 6; i++)
		{
			color.add(shade);
			color.add(shade);
			color.add(shade);
			color.add(1f);
		}
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
