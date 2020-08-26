package steve6472.polyground.block.special;

import org.joml.Vector3f;
import org.joml.Vector4f;
import steve6472.polyground.EnumFace;
import steve6472.polyground.block.Block;
import steve6472.polyground.block.BlockTextureHolder;
import steve6472.polyground.block.model.Cube;
import steve6472.polyground.block.model.CubeFace;
import steve6472.polyground.block.states.BlockState;
import steve6472.polyground.entity.Player;
import steve6472.polyground.gfx.particle.particles.BreakParticle;
import steve6472.polyground.registry.face.FaceRegistry;
import steve6472.polyground.world.ModelBuilder;
import steve6472.polyground.world.World;
import steve6472.polyground.world.chunk.SubChunk;

import java.io.File;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 26.08.2019
 * Project: SJP
 *
 ***********************/
public class SnapBlock extends Block
{
	public SnapBlock(File f)
	{
		super(f);
	}

	@Override
	public void onPlayerBreak(BlockState state, World world, Player player, EnumFace breakedFrom, int x, int y, int z)
	{
		activate(state, world, x, y, z, 1f);
	}

	public static void activate(BlockState state, World world, int x, int y, int z, float speedMul)
	{
		if (state == Block.air.getDefaultState())
			return;

		// Don't create particles if SubChunk has chance of being on another thread
		SubChunk sc = world.getSubChunkFromBlockCoords(x, y, z);
		if (sc.isRebuilding())
			return;

		float size = BlockTextureHolder.getAtlas().getTileCount();
		float s = 1f / 16f / size;

		Vector3f biomeColor = world.getBiome(x, y, z).getColor();

		//s - TY / size + s * 14f
		for (Cube c : state.getBlockModel(world, x, y, z).getCubes())
		{
			for (float i = c.getAabb().minX; i < c.getAabb().maxX; i += 1f / 16f)
			{
				for (float j = c.getAabb().minY; j < c.getAabb().maxY; j += 1f / 16f)
				{
					for (CubeFace cf : c.getFaces())
					{
						float ty = s - j / size + s * 14f;
						if (cf == c.getFace(EnumFace.WEST))
							snap(world, cf, i, j, c.getAabb().minZ, x, y, z, s - i / size + s * 14f, ty, biomeColor, speedMul);
						if (cf == c.getFace(EnumFace.EAST))
							snap(world, cf, i, j, c.getAabb().maxZ - 1f / 16f, x, y, z, i / size, ty, biomeColor, speedMul);
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
						if (cf == c.getFace(EnumFace.DOWN))
							snap(world, cf, i, c.getAabb().minY, j, x, y, z, i / size, ty, biomeColor, speedMul);
						if (cf == c.getFace(EnumFace.UP))
							snap(world, cf, i, c.getAabb().maxY - 1f / 16f, j, x, y, z, i / size, ty, biomeColor, speedMul);
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
						if (cf == c.getFace(EnumFace.NORTH))
							snap(world, cf, c.getAabb().maxX - 1f / 16f, j, i, x, y, z, s - i / size + s * 14f, ty, biomeColor, speedMul);
						if (cf == c.getFace(EnumFace.SOUTH))
							snap(world, cf, c.getAabb().minX, j, i, x, y, z, i / size, ty, biomeColor, speedMul);
					}
				}
			}
		}
	}

	private static final Vector3f NO_COLOR = new Vector3f(1, 1, 1);
	private static final Vector3f NO_MOTION = new Vector3f(0, 0, 0);

	// TODO: fix rotations
	public static void snap(World world, CubeFace cf, float i, float j, float k, int x, int y, int z, float TX, float TY, Vector3f biomeColor, float speedMul)
	{
		if (cf == null)
			return;

		if (!cf.hasProperty(FaceRegistry.texture))
			return;

		ModelBuilder bh = world.getGame().mainRender.buildHelper;

		float tx = cf.getProperty(FaceRegistry.texture).getTextureId() % bh.atlasSize;
		//noinspection IntegerDivisionInFloatingPointContext
		float ty = cf.getProperty(FaceRegistry.texture).getTextureId() / bh.atlasSize;

		tx /= (float) BlockTextureHolder.getAtlas().getTileCount();
		ty /= (float) BlockTextureHolder.getAtlas().getTileCount();

		float minU = tx + TX;
		float minV = ty + TY;

		float pixel = 1f / 16f / 2f;

		Vector3f color = NO_COLOR;
		if (cf.hasProperty(FaceRegistry.biomeTint) && cf.getProperty(FaceRegistry.biomeTint).isFlag())
			color = biomeColor;
		else if (cf.hasProperty(FaceRegistry.tint))
			color = cf.getProperty(FaceRegistry.tint).getColor();

		//new Vector3f(RandomUtil.randomFloat(-0.01f, 0.01f), RandomUtil.randomFloat(-0.01f, 0.01f), RandomUtil.randomFloat(-0.01f, 0.01f)),
		BreakParticle p = new BreakParticle(NO_MOTION,
			new Vector3f(x + i + pixel, y + j + pixel, z + k + pixel), pixel,
			//			RandomUtil.randomFloat(1f / 16f / 1.5f, 1f / 16f / 2f),
			new Vector4f(minU, 0, minV, 0), color, 10000);

		//		p.setGrowingSpeed(-1f / 16f / 8f / 60f);
		p.setGrowingSpeed(-1f / 16f / 8f / 5f * speedMul);

		world.getGame().mainRender.breakParticles.addParticle(p);
	}
}
