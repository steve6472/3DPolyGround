package steve6472.polyground.knapping;

import org.json.JSONArray;
import org.json.JSONObject;
import steve6472.polyground.item.Item;
import steve6472.polyground.registry.Items;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 27.09.2020
 * Project: CaveGame
 *
 ***********************/
public class Recipe
{
	private final boolean[][] pattern;
	private final Item result;

	public Recipe(JSONObject json)
	{
		result = Items.getItemByName(json.getString("result"));

		pattern = new boolean[16][16];

		JSONObject recipes = json.getJSONObject("recipe");

		for (int i = 0; i < 16; i++)
		{
			JSONArray line = recipes.getJSONArray("line_" + Integer.toString(i, 16));
			for (int j = 0; j < 16; j++)
			{
				pattern[i][j] = line.getInt(j) == 1;
			}
		}
	}

	public boolean[][] getPattern()
	{
		return pattern;
	}

	public boolean isMatch(boolean[] grid)
	{
		for (int i = 0; i < 16; i++)
		{
			for (int j = 0; j < 16; j++)
			{
				if (grid[i + j * 16] != pattern[i][j])
					return false;
			}
		}
		return true;
	}

	public Item getResult()
	{
		return result;
	}
}
