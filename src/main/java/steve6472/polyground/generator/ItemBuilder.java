package steve6472.polyground.generator;

import org.json.JSONArray;
import org.json.JSONObject;
import steve6472.polyground.PrettyJson;
import steve6472.polyground.generator.special.ISpecial;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 31.10.2020
 * Project: CaveGame
 *
 ***********************/
public class ItemBuilder
{
	private final JSONArray groups;
	private String itemName;
	private String modelName;
	private String modelPath;
	private ISpecial special;

	public ItemBuilder()
	{
		groups = new JSONArray();
		modelPath = "";
	}

	public static ItemBuilder create()
	{
		return new ItemBuilder();
	}

	public ItemBuilder name(String name)
	{
		this.itemName = name;
		return this;
	}

	public ItemBuilder model(String model)
	{
		this.modelName = model;
		return this;
	}

	public ItemBuilder groupPath(String groupPath)
	{
		groups.put(groupPath);
		return this;
	}

	public ItemBuilder modelPath(String modelPath)
	{
		this.modelPath = modelPath;
		return this;
	}

	public ItemBuilder special(ISpecial special)
	{
		this.special = special;
		return this;
	}

	/*
	 * Presets
	 */

	public static void itemModelPath(String name, String modelPath, String model, String... groups)
	{
		ItemBuilder builder = ItemBuilder.create().name(name).modelPath(modelPath).model(model);
		for (String g : groups)
			builder.groupPath(g);
		builder.generate();
	}

	public static void itemModel(String name, String model, String... groups)
	{
		itemModelPath(name, "", model, groups);
	}

	public static void item(String name, String... groups)
	{
		itemModel(name, name, groups);
	}

	public void generate()
	{
		try
		{
			System.out.println("Generating item " + itemName);
			File item = new File(DataGenerator.ITEMS, itemName + ".json");
			if (item.createNewFile())
			{
				System.out.println("Created item " + item.getPath());
			}

			JSONObject json = new JSONObject();
			json.put("name", itemName);

			if (!groups.isEmpty())
				json.put("groups", groups);

			/*
			 * Copy item model
			 */
			File source = new File("custom_models/items/" + (modelPath.isEmpty() ? "" : modelPath + "/") + modelName + ".bbmodel");
			File target = new File(DataGenerator.ITEM_MODELS, (modelPath.isEmpty() ? "" : modelPath + "/") + modelName + ".bbmodel");
			System.out.println("Copying model from " + source.getPath() + " to " + target.getPath());
			if (target.getParentFile().mkdirs())
				System.out.println("Created file " + target.getParentFile().getPath());
			Files.copy(source.toPath(), target.toPath(), StandardCopyOption.REPLACE_EXISTING);

			json.put("model_name", (modelPath.isEmpty() ? "" : modelPath + "/") + modelName);

			if (special != null)
			{
				System.out.println("\tWith Special \"" + special.getName() + "\"");
				JSONObject special = this.special.generate();
				if (special != null)
				{
					json.put("special", special);
				}
				System.out.println(PrettyJson.prettify(special));
			}

			BlockBuilder.save(item, json);
		} catch (IOException e)
		{
			System.err.println("Error at generating item " + itemName);
			e.printStackTrace();
		}
		System.out.println();
	}
}
