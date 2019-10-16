package com.steve6472.polyground.world;

import com.steve6472.polyground.block.Block;
import com.steve6472.polyground.block.registry.BlockRegistry;
import com.steve6472.sge.main.smartsave.SmartSave;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 08.09.2019
 * Project: SJP
 *
 ***********************/
public class Saver
{

	public static void saveWorld(World world, String worldName)
	{
		File worldFile = new File("worlds\\" + worldName);
		if (!worldFile.exists())
			worldFile.mkdirs();

		world.getChunks().forEach(c ->
		{
			try
			{
				c.saveChunk(worldName);
			} catch (IOException e)
			{
				System.err.println("Chunk " + c.getX() + "/" + c.getZ() + " failed to save");
				System.err.println("    " + e.getMessage() + "\n");
			}
		});
	}

	public static void loadWorld(World world, String worldName)
	{
		File worldFile = new File("worlds\\" + worldName);
		if (!worldFile.exists())
		{
			try
			{
				throw new FileNotFoundException("World file has not been found");
			} catch (FileNotFoundException e)
			{
				e.printStackTrace();
			}
		}

		for (File chunkFile : Objects.requireNonNull(worldFile.listFiles()))
		{
			String[] name = chunkFile.getName().split("_");
			Chunk c = new Chunk(Integer.parseInt(name[1]), Integer.parseInt(name[2]), world);
			try
			{
				c.loadChunk(worldName);
			} catch (IOException e)
			{
				System.err.println("Chunk " + c.getX() + "/" + c.getZ() + " failed to load");
				System.err.println("    " + e.getMessage() + "\n");
			}
			Chunk oldChunk = world.getChunk(c.getX(), c.getZ());
			if (oldChunk != null) world.deleteChunk(oldChunk.getX(), oldChunk.getZ());

			world.addChunk(c, false);
		}

		/*
		world.getChunks().forEach(c ->
		{
			try
			{
				c.loadChunk(worldName);
			} catch (IOException e)
			{
				System.err.println("Chunk " + c.getX() + "/" + c.getZ() + " failed to load");
				System.err.println("    " + e.getMessage() + "\n");
			}
		});*/
	}

	public static void saveSubChunk(String worldName, SubChunk subChunk) throws IOException
	{
		File subChunkPath = new File("worlds\\" + worldName + "\\chunk_" + subChunk.getParent().getX() + "_" + subChunk.getParent().getZ() + "\\sub_" + subChunk.getLayer() + ".txt");

		SmartSave.openOutput(subChunkPath);

		if (subChunk.isEmpty())
			saveEmptySubChunk(subChunk);
		else
			saveSubChunk_(subChunk);

		SmartSave.closeOutput();
	}

	private static void saveEmptySubChunk(SubChunk subChunk) throws IOException
	{
		SmartSave.writeData("is_empty", true);
	}

	private static void saveSubChunk_(SubChunk subChunk) throws IOException
	{
		HashMap<Integer, String> pallete = new HashMap<>();

		for (int i = 0; i < subChunk.getIds().length; i++)
		{
			for (int j = 0; j < subChunk.getIds()[i].length; j++)
			{
				for (int k = 0; k < subChunk.getIds()[i][j].length; k++)
				{
					int id = subChunk.getIds()[i][j][k];
					if (!pallete.containsKey(id))
					{
						pallete.put(id, BlockRegistry.getBlockById(id).getName());
					}
				}
			}
		}


		int[] ids = new int[pallete.size()];
		{
			int i = 0;
			for (int j : pallete.keySet())
			{
				ids[i] = j;
				i++;
			}
		}

		StringBuilder names = new StringBuilder();
		for (String s : pallete.values())
		{
			names.append(s).append(" ");
		}

		SmartSave.writeData("is_empty", false);
		SmartSave.writeData("pallete_ids", ids);
		SmartSave.writeData("pallete_names", names.toString());

		for (int i = 0; i < subChunk.getIds().length; i++)
		{
			for (int j = 0; j < subChunk.getIds()[i].length; j++)
			{
				int[] id = subChunk.getIds()[i][j];
				SmartSave.writeData(i + "_" + j, id);
			}
		}
	}



	public static void loadSubChunk(String worldName, SubChunk subChunk) throws IOException
	{
		File subChunkPath = new File("worlds\\" + worldName + "\\chunk_" + subChunk.getParent().getX() + "_" + subChunk.getParent().getZ() + "\\sub_" + subChunk.getLayer() + ".txt");
		SmartSave.openInput(subChunkPath);
		SmartSave.readFull();
		boolean isEmtpy = (boolean) SmartSave.get("is_empty");

		if (isEmtpy)
			loadEmptySubChunk(subChunk);
		else
			loadSubChunk_(subChunk);

		SmartSave.closeOutput();

		subChunk.rebuild();
	}

	private static void loadSubChunk_(SubChunk subChunk)
	{
		HashMap<Integer, String> pallete = new HashMap<>();

		int[] idList = (int[]) SmartSave.get("pallete_ids");
		String[] nameList = ((String) SmartSave.get("pallete_names")).split(" ");

		for (int i = 0; i < idList.length; i++)
		{
			pallete.put(idList[i], nameList[i]);
		}

		for (int i = 0; i < subChunk.getIds().length; i++)
		{
			for (int j = 0; j < subChunk.getIds()[i].length; j++)
			{
				int[] numbers = (int[]) SmartSave.get(i + "_" + j);
				for (int k = 0; k < numbers.length; k++)
				{
					int id = numbers[k];
					subChunk.getIds()[i][j][k] = BlockRegistry.getBlockByName(pallete.get(id)).getId();
				}
			}
		}
	}

	private static void loadEmptySubChunk(SubChunk subChunk)
	{
		for (int i = 0; i < subChunk.getIds().length; i++)
		{
			for (int j = 0; j < subChunk.getIds()[i].length; j++)
			{
				int[] numbers = (int[]) SmartSave.get(i + "_" + j);
				for (int k = 0; k < numbers.length; k++)
				{
					subChunk.getIds()[i][j][k] = Block.air.getId();
				}
			}
		}
	}
}
