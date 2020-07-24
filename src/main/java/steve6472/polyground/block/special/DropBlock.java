package steve6472.polyground.block.special;

import org.joml.Vector3f;
import org.joml.Vector4f;
import steve6472.polyground.EnumFace;
import steve6472.polyground.block.Block;
import steve6472.polyground.block.BlockTextureHolder;
import steve6472.polyground.block.model.Cube;
import steve6472.polyground.block.model.CubeFace;
import steve6472.polyground.block.states.BlockState;
import steve6472.polyground.gfx.particle.particles.BreakParticle;
import steve6472.polyground.registry.face.FaceRegistry;
import steve6472.polyground.world.BuildHelper;
import steve6472.polyground.world.World;
import steve6472.polyground.world.chunk.ModelLayer;
import steve6472.sge.main.util.RandomUtil;

import java.io.File;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 26.08.2019
 * Project: SJP
 *
 ***********************/
public class DropBlock extends Block
{
	public DropBlock(File f)
	{
		super(f);
	}

	@Override
	public void tick(World world, BlockState state, int x, int y, int z)
	{
		activate(this, world, state, x, y, z);
	}

	@Override
	public int createModel(int x, int y, int z, World world, BlockState state, BuildHelper buildHelper, ModelLayer modelLayer)
	{
		return 0;
	}

	public static void activate(Block block, World world, BlockState state, int x, int y, int z)
	{
		if (block == Block.air)
			return;

		float size = BlockTextureHolder.getAtlas().getTileCount();
		float s = 1f / 16f / size;

		//s - TY / size + s * 14f
		for (Cube c : state.getBlockModel().getCubes())
		{
			for (float i = c.getAabb().minX; i < c.getAabb().maxX; i += 1f / 16f)
			{
				for (float j = c.getAabb().minY; j < c.getAabb().maxY; j += 1f / 16f)
				{
					if (!RandomUtil.decide(255))
						continue;
					for (CubeFace cf : c.getFaces())
					{
						float ty = s - j / size + s * 14f;
						if (cf == c.getFace(EnumFace.WEST))
							drop(world, cf, i, j, c.getAabb().minZ, x, y, z, s - i / size + s * 14f, ty);
						if (cf == c.getFace(EnumFace.EAST))
							drop(world, cf, i, j, c.getAabb().maxZ - 1f / 16f, x, y, z, i / size, ty);
					}
				}
			}


			for (float i = c.getAabb().minX; i < c.getAabb().maxX; i += 1f / 16f)
			{
				for (float j = c.getAabb().minZ; j < c.getAabb().maxZ; j += 1f / 16f)
				{
					if (!RandomUtil.decide(255))
						continue;
					for (CubeFace cf : c.getFaces())
					{
						//s - TY / size + s * 14f
						float ty = s - j / size + s * 14f;
						if (cf == c.getFace(EnumFace.DOWN))
							drop(world, cf, i, c.getAabb().minY, j, x, y, z, i / size, ty);
						if (cf == c.getFace(EnumFace.UP))
							drop(world, cf, i, c.getAabb().maxY - 1f / 16f, j, x, y, z, i / size, ty);
					}
				}
			}


			for (float i = c.getAabb().minZ; i < c.getAabb().maxZ; i += 1f / 16f)
			{
				for (float j = c.getAabb().minY; j < c.getAabb().maxY; j += 1f / 16f)
				{
					if (!RandomUtil.decide(255))
						continue;
					for (CubeFace cf : c.getFaces())
					{
						float ty = s - j / size + s * 14f;
						if (cf == c.getFace(EnumFace.NORTH))
							drop(world, cf, c.getAabb().maxX - 1f / 16f, j, i, x, y, z, s - i / size + s * 14f, ty);
						if (cf == c.getFace(EnumFace.SOUTH))
							drop(world, cf, c.getAabb().minX, j, i, x, y, z, i / size, ty);
					}
				}
			}
		}
	}

	public static void drop(World world, CubeFace cf, float i, float j, float k, int x, int y, int z, float TX, float TY)
	{
		BuildHelper bh = world.getGame().mainRender.buildHelper;

		float tx = cf.getProperty(FaceRegistry.texture).getTextureId() % bh.atlasSize;
		float ty = cf.getProperty(FaceRegistry.texture).getTextureId() / bh.atlasSize;

		tx /= (float) BlockTextureHolder.getAtlas().getTileCount();
		ty /= (float) BlockTextureHolder.getAtlas().getTileCount();

		float minU = tx + TX;
		float minV = ty + TY;

		float pixel = 1f / 16f / 2f;

		BreakParticle p = new BreakParticle(new Vector3f(RandomUtil.randomFloat(-0.0001f, 0.0001f), -0.01f, RandomUtil.randomFloat(-0.0001f, 0.0001f)),
			//			0,
			//			0,
			//			0),
			new Vector3f(x + i + pixel, y + j + pixel, z + k + pixel), pixel,
			//			Util.getRandomFloat(1f / 16f / 1.5f, 1f / 16f / 2f),
			new Vector4f(minU, 0, minV, 0), 8500);

		//		p.setGrowingSpeed(-1f / 16f / 8f / 60f);

		world.getGame().mainRender.particles.addParticle(p);
	}

	@Override
	public boolean isTickable()
	{
		return true;
	}
}
