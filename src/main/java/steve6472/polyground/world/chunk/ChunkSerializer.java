package steve6472.polyground.world.chunk;

import steve6472.polyground.block.Block;
import steve6472.polyground.block.states.BlockState;
import steve6472.polyground.registry.Blocks;
import steve6472.polyground.world.generator.EnumChunkStage;
import steve6472.sge.main.smartsave.SmartSave;
import steve6472.sge.main.util.Pair;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 11.11.2019
 * Project: SJP
 *
 ***********************/
public class ChunkSerializer
{
	public static void serialize(SubChunk subChunk) throws IOException
	{
		File chunk = new File("game/worlds/" + subChunk.getWorld().worldName + "/chunk_" + subChunk.getParent().getX() + "_" + subChunk.getParent().getZ());
		if (!chunk.exists())
			chunk.mkdir();

		File subChunkPath = new File("game/worlds/" + subChunk.getWorld().worldName + "/chunk_" + subChunk.getParent().getX() + "_" + subChunk.getParent().getZ() + "/sub_" + subChunk.getLayer() + ".txt");
		if (!subChunkPath.exists())
			chunk.createNewFile();

		SmartSave.openOutput(subChunkPath);

		var pair = generatePallete(subChunk);
		HashMap<Short, String> pallete = pair.getA();
		HashMap<Integer, Short> map = pair.getB();

		short[] ids = createPalleteIdArray(pallete);
		String[] names = createPalleteNames(pallete);

		SmartSave.writeData("pallete_ids", ids);
		SmartSave.writeData("pallete_names", names);

		saveToChunk(subChunk, map);

		SmartSave.closeOutput();
	}

	private static void saveToChunk(SubChunk subChunk, HashMap<Integer, Short> map) throws IOException
	{
		short[][][] ids = new short[16][16][16];
		for (int i = 0; i < 16; i++)
		{
			for (int j = 0; j < 16; j++)
			{
				for (int k = 0; k < 16; k++)
				{
					ids[i][j][k] = map.get(subChunk.getState(i, j, k).getId());
				}
			}
		}
		SmartSave.writeData("blocks", ids);
	}

	private static short[] translate(HashMap<Integer, Short> map, int[] array)
	{
		short[] arr = new short[array.length];

		for (int i = 0; i < array.length; i++)
		{
			arr[i] = map.get(array[i]);
		}

		return arr;
	}

	private static Pair<HashMap<Short, String>, HashMap<Integer, Short>> generatePallete(SubChunk subChunk)
	{
		HashMap<Short, String> pallete = new HashMap<>();
		HashMap<Integer, Short> map = new HashMap<>();
		List<Integer> registratedIds = new ArrayList<>();

		short lastId = 0;

		for (int i = 0; i < 16; i++)
		{
			for (int j = 0; j < 16; j++)
			{
				for (int k = 0; k < 16; k++)
				{
					BlockState state = subChunk.getState(i, j, k);
					int id = state.getId();
					if (!registratedIds.contains(id))
					{
						map.put(id, lastId);
						registratedIds.add(id);
						pallete.put(lastId, state.getBlock().getName() + state.getStateString());
						lastId++;
					}
				}
			}
		}

		return new Pair<>(pallete, map);
	}

	private static short[] createPalleteIdArray(HashMap<Short, String> pallete)
	{
		short[] ids = new short[pallete.size()];

		int i = 0;
		for (short j : pallete.keySet())
		{
			ids[i] = j;
			i++;
		}

		return ids;
	}

	private static String[] createPalleteNames(HashMap<Short, String> pallete)
	{
		String[] a = new String[pallete.size()];
		int i = 0;
		for (String s : pallete.values())
		{
			a[i] = s;
			i++;
		}
		return a;
	}

	public static SubChunk deserialize(SubChunk subChunk) throws IOException
	{
		File subChunkPath = new File("game/worlds/" + subChunk.getWorld().worldName + "/chunk_" + subChunk.getX() + "_" + subChunk.getZ() + "/sub_" + subChunk.getLayer() + ".txt");
		SmartSave.openInput(subChunkPath);
		SmartSave.readFull();

		short[] idList = (short[]) SmartSave.get("pallete_ids");
		String[] nameList = (String[]) SmartSave.get("pallete_names");

		HashMap<Short, String> pallete = new HashMap<>();
		for (short i = 0; i < idList.length; i++)
		{
			pallete.put(idList[i], nameList[i]);
		}

		short[][][] blocks = (short[][][]) SmartSave.get("blocks");

		final Block error = Blocks.getBlockByName("error");

		for (int i = 0; i < 16; i++)
		{
			for (int j = 0; j < 16; j++)
			{
				for (int k = 0; k < 16; k++)
				{
					try
					{
						String block = pallete.get(blocks[i][j][k]);
						if (block.contains("[") && block.contains("]"))
						{
							String[] s = pallete.get(blocks[i][j][k]).split("\\[");
							subChunk.setState(Blocks.getStateByName(s[0], "[" + s[1]), i, j, k);
						} else
						{
							subChunk.setState(Blocks.getBlockByName(block).getDefaultState(), i, j, k);
						}
					} catch (Exception ex)
					{
						System.err.println("Could not find " + pallete.get(blocks[i][j][k]) + "! Replacing will error");
						subChunk.setBlock(error, i, j, k);
					}
				}
			}
		}

		SmartSave.closeInput();

		subChunk.stage = EnumChunkStage.FINISHED;
		subChunk.rebuild();
		return subChunk;
	}
}
