package steve6472.polyground.block.special;

import org.json.JSONObject;
import steve6472.polyground.block.BlockAtlas;
import steve6472.polyground.block.blockdata.BlockData;
import steve6472.polyground.block.blockdata.IBlockData;
import steve6472.polyground.block.blockdata.LiqExtractorData;
import steve6472.polyground.block.model.elements.Bakery;
import steve6472.polyground.block.states.BlockState;
import steve6472.polyground.world.ModelBuilder;
import steve6472.polyground.world.World;
import steve6472.polyground.world.chunk.ModelLayer;
import steve6472.sge.main.util.RandomUtil;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 18.10.2020
 * Project: CaveGame
 *
 ***********************/
public class LiqExtractorBlock extends FourDirectionalBlock implements IBlockData
{
	public LiqExtractorBlock(JSONObject json)
	{
		super(json);
		isFull = false;
	}

	@Override
	public void load(JSONObject json)
	{
		BlockAtlas.putTexture("block/liq");
	}

	@Override
	public int createModel(int x, int y, int z, World world, BlockState state, ModelBuilder buildHelper, ModelLayer modelLayer)
	{
		int tris = CustomBlock.model(x, y, z, world, state, buildHelper, modelLayer);

		if (modelLayer == ModelLayer.TRANSPARENT)
		{
			LiqExtractorData data = (LiqExtractorData) world.getData(x, y, z);
			if (data.amount > 0)
			{
				int h = (int) Math.ceil(data.amount / 1000.0d * 10d);
				tris += Bakery.autoTexturedCube(1, 1, 1, 14, h, 14, "block/liq", 0);
			}
		}

		return tris;
	}

	@Override
	public void tick(BlockState state, World world, int x, int y, int z)
	{
		if (world.getBlock(x, y + 1, z).getName().equals("amethine") && RandomUtil.decide(2))
		{
			LiqExtractorData data = (LiqExtractorData) world.getData(x, y, z);
			boolean _999 = data.amount == 999;

			data.amount = Math.min(data.amount + 1, 1000);

			if (data.amount == 1000)
			{
				if (_999)
				{
					world.getSubChunkFromBlockCoords(x, y, z).rebuild();
				}
			} else if (data.amount % 100 == 0 || data.amount == 1)
			{
				world.getSubChunkFromBlockCoords(x, y, z).rebuild();
			}
		}
	}

	@Override
	public boolean isTickable()
	{
		return true;
	}

	@Override
	public BlockData createNewBlockEntity(BlockState state)
	{
		return new LiqExtractorData();
	}
}
