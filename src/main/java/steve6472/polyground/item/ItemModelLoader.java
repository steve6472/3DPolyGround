package steve6472.polyground.item;

import org.json.JSONObject;
import steve6472.polyground.CaveGame;
import steve6472.polyground.block.Block;
import steve6472.polyground.registry.Blocks;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 15.09.2019
 * Project: SJP
 *
 ***********************/
public class ItemModelLoader
{
	public static void loadModel(String name, Item item)
	{
		JSONObject json = null;

		try
		{
//			json = new JSONObject(read(new File(MainApp.class.getResource("/models/" + name + ".json").getFile())));
			json = new JSONObject(read(new File("game/objects/models/" + name + ".json")));
			//			json = new JSONObject(read(new File("models/" + name + ".json")));
		} catch (Exception e)
		{
			System.err.println("Could not load " + name);
			e.printStackTrace();
			CaveGame.getInstance().exit();
			System.exit(0);
		}

		if (json.has("type"))
		{
			if (json.getString("type").equals("from_block"))
			{
				fromBlock(Blocks.getBlockByName(json.getString("block")), item);
			}
			if (json.getString("type").equals("texture"))
			{
				fromTexture(json.getString("texture"), item);
			}
			if (json.getString("type").equals("from_model"))
			{
				fromTexture("null", item);
			}
		}
	}

	private static void fromBlock(Block block, Item item)
	{
		if (block != Block.air)
		{
			CaveGame.getInstance().itemAtlas.start();
			//			System.out.println("Item " + item.getName() + " has id " + item.getId() + " /index:" + PolyGround.getInstance().itemAtlas.renderBlock(block));
			CaveGame.getInstance().itemAtlas.renderBlock(block);
			CaveGame.getInstance().itemAtlas.finish();
		}
	}

	private static void fromTexture(String name, Item item)
	{
		try
		{
			CaveGame.getInstance().itemAtlas.start();
			//			System.out.println("Item " + item.getName() + " has id " + item.getId() + " /index:" + PolyGround.getInstance().itemAtlas.renderTexture("/item/" + name + ".png"));
			CaveGame.getInstance().itemAtlas.renderTexture("/item/" + name + ".png");
			CaveGame.getInstance().itemAtlas.finish();
		} catch (Exception ex)
		{
			System.err.println("Error while creating texture for " + item.getName() + " (texture: " + name + ")");
			ex.printStackTrace();
			System.exit(1);
		}
	}

	private static String read(File f)
	{
		StringBuilder sb = new StringBuilder();
		try
		{
			BufferedReader br = new BufferedReader(new FileReader(f));

			boolean endOfTheFile = false;
			while (!endOfTheFile)
			{
				String line = br.readLine();

				if (line == null)
				{
					endOfTheFile = true;
				} else
				{
					sb.append(line);
				}
			}

			br.close();

		} catch (IOException e)
		{
			e.printStackTrace();
		}
		return sb.toString();
	}
}
