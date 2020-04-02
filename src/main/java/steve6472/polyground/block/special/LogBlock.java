package steve6472.polyground.block.special;

import steve6472.polyground.EnumFace;
import steve6472.polyground.block.Block;
import steve6472.polyground.block.BlockTextureHolder;
import steve6472.polyground.block.blockdata.BlockData;
import steve6472.polyground.block.model.CubeFace;
import steve6472.polyground.block.model.registry.Cube;
import steve6472.polyground.block.model.registry.face.FaceRegistry;
import steve6472.polyground.entity.Player;
import steve6472.polyground.gfx.particle.particles.BreakParticle;
import steve6472.polyground.world.BuildHelper;
import steve6472.polyground.world.chunk.SubChunk;
import org.joml.Vector3f;
import org.joml.Vector4f;
import steve6472.sge.main.util.RandomUtil;

import java.io.File;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 26.08.2019
 * Project: SJP
 *
 ***********************/
public class LogBlock extends Block
{
	public LogBlock(File f, int id)
	{
		super(f, id);
	}

	@Override
	public void onPlace(SubChunk subChunk, BlockData blockData, Player player, EnumFace placedOn, int x, int y, int z)
	{
		//		subChunk.setProperties(x, y, z, new Properties(placedOn));
		subChunk.rebuild();
	}

	@Override
	public void onBreak(SubChunk subChunk, BlockData blockData, Player player, EnumFace breakedFrom, int x, int y, int z)
	{
		//		subChunk.setProperties(x, y, z, null);

		for (int i = 0; i < 8; i++)
			for (Cube c : getCubes(x, y, z))
			{
				for (CubeFace cf : c.getFaces())
				{
					BuildHelper bh = subChunk.getWorld().getPg().buildHelper;

					float tx = cf.getProperty(FaceRegistry.texture).getTextureId() % bh.atlasSize;
					float ty = cf.getProperty(FaceRegistry.texture).getTextureId() / bh.atlasSize;

					//				int s = Util.getRandomInt(4, 1);
					int s = 1;

					float r = RandomUtil.randomInt(0, 16 - s);

					float size = 1f / (float) BlockTextureHolder.getAtlas().getSize() / (float) BlockTextureHolder.getAtlas().getTileCount();
					tx *= size * 16f;
					ty *= size * 16f;

					float minU = tx + r * size;
					float maxU = tx + size * s + r * size;

					float minV = ty + r * size;
					float maxV = ty + size * s + r * size;

					BreakParticle p = new BreakParticle(new Vector3f(RandomUtil.randomFloat(-0.01f, 0.01f), RandomUtil.randomFloat(-0.01f, 0.01f), RandomUtil.randomFloat(-0.01f, 0.01f)), new Vector3f(x + RandomUtil.randomFloat(-0.5f, 0.5f) + 0.5f, y + RandomUtil.randomFloat(-0.5f, 0.5f) + 0.5f, z + RandomUtil.randomFloat(-0.5f, 0.5f) + 0.5f), RandomUtil.randomFloat(0.02f, 0.07f), new Vector4f(minU, maxU, minV, maxV), 700);

					p.setGrowingSpeed(-0.07f / 60f);

					subChunk.getWorld().getPg().particles.addParticle(p);
				}
			}
	}

	@Override
	public boolean rebuildChunkOnPlace()
	{
		return false;
	}

	@Override
	public boolean isTickable()
	{
		return false;
	}
}
