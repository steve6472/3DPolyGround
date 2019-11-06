package com.steve6472.polyground.block.special;

import com.steve6472.polyground.EnumFace;
import com.steve6472.polyground.block.Block;
import com.steve6472.polyground.block.BlockTextureHolder;
import com.steve6472.polyground.block.blockdata.BlockData;
import com.steve6472.polyground.block.model.CubeFace;
import com.steve6472.polyground.block.model.registry.Cube;
import com.steve6472.polyground.block.model.registry.face.FaceRegistry;
import com.steve6472.polyground.entity.Player;
import com.steve6472.polyground.particle.particles.BreakParticle;
import com.steve6472.polyground.world.BuildHelper;
import com.steve6472.polyground.world.SubChunk;
import com.steve6472.polyground.world.World;
import com.steve6472.sge.main.util.RandomUtil;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.io.File;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 26.08.2019
 * Project: SJP
 *
 ***********************/
public class SnapBlock extends Block
{
	public SnapBlock(File f, int id)
	{
		super(f, id);
	}

	@Override
	public void onBreak(SubChunk subChunk, BlockData blockData, Player player, EnumFace breakedFrom, int x, int y, int z)
	{
		activate(this, subChunk, x, y, z);
	}

	public static void activate(Block block, SubChunk subChunk, int x, int y, int z)
	{
		if (block == Block.air) return;

		float size = BlockTextureHolder.getAtlas().getTileCount();
		float s = 1f / 16f / size;

		//s - TY / size + s * 14f
		for (Cube c : block.getCubes(x, y, z))
		{
			for (float i = c.getAabb().minX; i < c.getAabb().maxX; i += 1f / 16f)
			{
				for (float j = c.getAabb().minY; j < c.getAabb().maxY; j += 1f / 16f)
				{
					for (CubeFace cf : c.getFaces())
					{
						float ty = s - j / size + s * 14f;
						if (cf == c.getFace(EnumFace.WEST)) snap(subChunk.getWorld(), cf, i, j, c.getAabb().minZ, x, y, z, s - i / size + s * 14f, ty);
						if (cf == c.getFace(EnumFace.EAST)) snap(subChunk.getWorld(), cf, i, j, c.getAabb().maxZ - 1f / 16f, x, y, z, i / size, ty);
					}
				}
			}


			for (float i = c.getAabb().minX; i < c.getAabb().maxX; i += 1f / 16f)
			{
				for (float j = c.getAabb().minZ; j < c.getAabb().maxZ; j += 1f / 16f)
				{
					for (CubeFace cf : c.getFaces())
					{
						//s - TY / size + s * 14f
						float ty = s - j / size + s * 14f;
						if (cf == c.getFace(EnumFace.DOWN)) snap(subChunk.getWorld(), cf, i, c.getAabb().minY, j, x, y, z, i / size, ty);
						if (cf == c.getFace(EnumFace.UP)) snap(subChunk.getWorld(), cf, i, c.getAabb().maxY - 1f / 16f, j, x, y, z, i / size, ty);
					}
				}
			}


			for (float i = c.getAabb().minZ; i < c.getAabb().maxZ; i += 1f / 16f)
			{
				for (float j = c.getAabb().minY; j < c.getAabb().maxY; j += 1f / 16f)
				{
					for (CubeFace cf : c.getFaces())
					{
						float ty = s - j / size + s * 14f;
						if (cf == c.getFace(EnumFace.NORTH)) snap(subChunk.getWorld(), cf, c.getAabb().maxX - 1f / 16f, j, i, x, y, z, s - i / size + s * 14f, ty);
						if (cf == c.getFace(EnumFace.SOUTH)) snap(subChunk.getWorld(), cf, c.getAabb().minX, j, i, x, y, z, i / size, ty);
					}
				}
			}
		}
	}

	public static void snap(World world, CubeFace cf, float i, float j, float k, int x, int y, int z, float TX, float TY)
	{
		if (cf == null) return;

		BuildHelper bh = world.getPg().buildHelper;

		float tx = cf.getProperty(FaceRegistry.texture).getTextureId() % bh.atlasSize;
		float ty = cf.getProperty(FaceRegistry.texture).getTextureId() / bh.atlasSize;

		tx /= (float) BlockTextureHolder.getAtlas().getTileCount();
		ty /= (float) BlockTextureHolder.getAtlas().getTileCount();

		float minU = tx + TX;
		float minV = ty + TY;

		float pixel = 1f / 16f / 2f;

		BreakParticle p = new BreakParticle(
			new Vector3f(
				RandomUtil.randomFloat(-0.01f, 0.01f),
				RandomUtil.randomFloat(-0.01f, 0.01f),
				RandomUtil.randomFloat(-0.01f, 0.01f)),
//			0,
//			0,
//			0),
			new Vector3f(
				x + i + pixel,
				y + j + pixel,
				z + k + pixel),
			pixel,
//			Util.getRandomFloat(1f / 16f / 1.5f, 1f / 16f / 2f),
			new Vector4f(
				minU,
				0,
				minV,
				0),
			10000
		);

//		p.setGrowingSpeed(-1f / 16f / 8f / 60f);
		p.setGrowingSpeed(-1f / 16f / 8f / 5f);

		world.getPg().particles.addParticle(p);
	}
}
