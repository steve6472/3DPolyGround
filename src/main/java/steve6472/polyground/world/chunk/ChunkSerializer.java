package steve6472.polyground.world.chunk;

import steve6472.polyground.block.registry.BlockRegistry;
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
		File chunk = new File("worlds\\" + subChunk.getWorld().worldName + "\\chunk_" + subChunk.getParent().getX() + "_" + subChunk.getParent().getZ());
		if (!chunk.exists())
			chunk.mkdir();

		File subChunkPath = new File("worlds\\" + subChunk.getWorld().worldName + "\\chunk_" + subChunk.getParent().getX() + "_" + subChunk.getParent().getZ() + "\\sub_" + subChunk.getLayer() + ".txt");

		SmartSave.openOutput(subChunkPath);

		var pair = generatePallete(subChunk);
		HashMap<Short, String> pallete = pair.getA();
		HashMap<Integer, Short> map = pair.getB();

		short[] ids = createPalleteIdArray(pallete);
		String names = createPalleteNames(pallete);

		SmartSave.writeData("pallete_ids", ids);
		SmartSave.writeData("pallete_names", names);

		saveToChunk(subChunk, map);

		SmartSave.closeOutput();
	}

	private static void saveToChunk(SubChunk subChunk, HashMap<Integer, Short> map) throws IOException
	{
		for (int i = 0; i < subChunk.getIds().length; i++)
		{
			for (int j = 0; j < subChunk.getIds()[i].length; j++)
			{
				int[] id = subChunk.getIds()[i][j];
				short[] translated = translate(map, id);
				SmartSave.writeData(i + "_" + j, translated);
			}
		}
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

		for (int i = 0; i < subChunk.getIds().length; i++)
		{
			for (int j = 0; j < subChunk.getIds()[i].length; j++)
			{
				for (int k = 0; k < subChunk.getIds()[i][j].length; k++)
				{
					int id = subChunk.getIds()[i][j][k];
					if (!registratedIds.contains(id))
					{
						map.put(id, lastId);
						registratedIds.add(id);
						pallete.put(lastId, BlockRegistry.getBlockById(id).getName());
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

	private static String createPalleteNames(HashMap<Short, String> pallete)
	{
		StringBuilder names = new StringBuilder();
		for (String s : pallete.values())
		{
			names.append(s).append(" ");
		}
		return names.toString();
	}

	public static SubChunk deserialize(SubChunk subChunk) throws IOException
	{
		File subChunkPath = new File("worlds\\" + subChunk.getWorld().worldName + "\\chunk_" + subChunk.getX() + "_" + subChunk.getZ() + "\\sub_" + subChunk.getLayer() + ".txt");
		SmartSave.openInput(subChunkPath);
		SmartSave.readFull();

		short[] idList = (short[]) SmartSave.get("pallete_ids");
		String[] nameList = ((String) SmartSave.get("pallete_names")).split(" ");

		HashMap<Short, String> pallete = new HashMap<>();
		for (short i = 0; i < idList.length; i++)
		{
			pallete.put(idList[i], nameList[i]);
		}

		for (int i = 0; i < subChunk.getIds().length; i++)
		{
			for (int j = 0; j < subChunk.getIds()[i].length; j++)
			{
				short[] ids = (short[]) SmartSave.get(i + "_" + j);
				for (int k = 0; k < ids.length; k++)
				{
					short sid = ids[k];
					subChunk.getIds()[i][j][k] = BlockRegistry.getBlockByName(pallete.get(sid)).getId();
				}
			}
		}

		SmartSave.closeInput();

		subChunk.rebuild();
		return subChunk;
	}
}
