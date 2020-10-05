package steve6472.polyground.block.blockdata;

import net.querz.nbt.tag.CompoundTag;
import steve6472.polyground.CaveGame;
import steve6472.polyground.NBTArrayUtil;
import steve6472.polyground.knapping.Recipe;
import steve6472.polyground.registry.data.DataRegistry;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 26.09.2020
 * Project: CaveGame
 *
 ***********************/
public class KnappingData extends BlockData
{
	public boolean[] stone;
	public int pieceCount = 0;

	public KnappingData()
	{
		stone = new boolean[256];

//		for (int i = 3; i < 13; i++)
//		{
//			for (int j = 1; j < 15; j++)
//			{
//				stone[i][j] = true;
//				pieceCount++;
//			}
//		}

		Recipe recipe = CaveGame.getInstance().knappingRecipes.getRecipes().get(0);

		for (int i = 0; i < 16; i++)
		{
			for (int j = 0; j < 16; j++)
			{
				if (stone[i + j * 16] = recipe.getPattern()[i][j])
					pieceCount++;
			}
		}
		stone[0] = true;
		pieceCount++;
	}

	@Override
	public CompoundTag write()
	{
		CompoundTag tag = new CompoundTag();
		tag.putLongArray("grid", NBTArrayUtil.boolToLongArray(stone));
		return tag;
	}

	@Override
	public void read(CompoundTag tag)
	{
		stone = NBTArrayUtil.longToBoolArray(tag.getLongArray("grid"));
		System.out.println("loaded data : ");
		NBTArrayUtil.printBoolArray(stone);
		for (boolean b : stone)
		{
			if (b)
				pieceCount++;
		}
	}

	@Override
	public String getId()
	{
		return DataRegistry.stoneKnapping.getId();
	}
}
