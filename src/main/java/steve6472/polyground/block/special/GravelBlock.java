package steve6472.polyground.block.special;

import org.json.JSONObject;
import steve6472.polyground.block.Block;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 25.09.2019
 * Project: SJP
 *
 ***********************/
public class GravelBlock extends Block
{
	public GravelBlock(JSONObject json)
	{
		super(json);
	}

	/*
	@Override
	public void onUpdate(World world, BlockState state, EnumFace updateFrom, int x, int y, int z)
	{
		super.onUpdate(world, state, updateFrom, x, y, z);
		if (y <= 0)
			return;

		if (subChunk.getBlockEfficiently(x, y - 1, z) == Block.air)
		{
			subChunk.setBlock(x, y, z, Block.air);
			subChunk.rebuild();
			BasicEvents.updateAll(subChunk, x + subChunk.getX() * 16, y + subChunk.getLayer() * 16, z + subChunk.getZ() * 16);

			EntityBase e = EntityRegistry.fallingBlock.createNew();
			e.setPosition(x + 0.5f + subChunk.getX() * 16, y + 0.5f + subChunk.getLayer() * 16, z + 0.5f + subChunk.getZ() * 16);

			subChunk.getWorld().addEntity(e);
		}
	}*/
}
