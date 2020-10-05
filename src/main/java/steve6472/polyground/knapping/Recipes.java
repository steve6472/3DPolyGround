package steve6472.polyground.knapping;

import org.json.JSONObject;
import steve6472.polyground.block.model.ModelLoader;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 27.09.2020
 * Project: CaveGame
 *
 ***********************/
public class Recipes
{
	private List<Recipe> recipes;

	public Recipes()
	{
		recipes = new ArrayList<>();
		File[] recipesFolder = new File("game/objects/recipes/knapping").listFiles();

		if (recipesFolder != null)
		{
			for (int i = 0; i < Objects.requireNonNull(recipesFolder).length; i++)
			{
				if (recipesFolder[i].isDirectory())
					continue;

				if (!recipesFolder[i].getName().endsWith(".json"))
					continue;

				try
				{
					JSONObject json = new JSONObject(ModelLoader.read(recipesFolder[i]));
					recipes.add(new Recipe(json));
				} catch (Exception ex)
				{
					System.err.println("Error while loading knapping recipe for " + recipesFolder[i].getName());
					ex.printStackTrace();
				}
			}
		}
	}

	/**
	 *
	 * @param grid boolean[][] of knapping grid placed in the world
	 * @return null if no match is found, recipe otherwise
	 */
	public Recipe getMatch(boolean[][] grid)
	{
		for (Recipe r : recipes)
		{
			if (r.isMatch(grid))
				return r;
		}

		return null;
	}

	public List<Recipe> getRecipes()
	{
		return recipes;
	}
}
