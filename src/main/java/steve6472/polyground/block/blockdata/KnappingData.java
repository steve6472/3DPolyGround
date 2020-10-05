package steve6472.polyground.block.blockdata;

import steve6472.polyground.CaveGame;
import steve6472.polyground.knapping.Recipe;
import steve6472.polyground.registry.data.DataRegistry;

import java.io.IOException;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 26.09.2020
 * Project: CaveGame
 *
 ***********************/
public class KnappingData extends BlockData
{
	public boolean[][] stone;
	public int pieceCount = 0;

	public KnappingData()
	{
		stone = new boolean[16][16];

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
				if (stone[i][j] = recipe.getPattern()[i][j])
					pieceCount++;
			}
		}
		stone[0][0] = true;
		pieceCount++;
	}

	@Override
	public void write() throws IOException
	{

	}

	@Override
	public void read() throws IOException
	{

	}

	@Override
	public String getId()
	{
		return DataRegistry.stoneKnapping.getId();
	}
}
