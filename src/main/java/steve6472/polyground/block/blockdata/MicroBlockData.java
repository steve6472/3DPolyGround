package steve6472.polyground.block.blockdata;

import steve6472.polyground.block.Block;
import steve6472.polyground.block.registry.BlockRegistry;
import steve6472.sge.main.smartsave.SmartSave;
import steve6472.sge.main.util.Pair;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 20.12.2019
 * Project: SJP
 *
 ***********************/
public class MicroBlockData extends BlockData
{
	private HashMap<Integer, PalleteEntry> pallete;
	private int[][][] ids;

	public MicroBlockData()
	{
		pallete = new HashMap<>();
		ids = new int[16][16][16];
	}

	@Override
	public void write(DataOutputStream output) throws IOException
	{/*
		SmartSave.setDataOutputStream(output);

		var pair = generatePallete();
		HashMap<Short, String> pallete = pair.getA();
		HashMap<Integer, Short> map = pair.getB();

		short[] ids = createPalleteIdArray(pallete);
		String names = createPalleteNames(pallete);

		SmartSave.writeData("pallete_ids", ids);
		SmartSave.writeData("pallete_names", names);

		saveToChunk(map);
		SmartSave.setDataOutputStream(null);*/
	}

	@Override
	public void read(DataInputStream input) throws IOException
	{/*
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
		}*/
	}

	public void setBlock(int id, int x, int y, int z)
	{
		if (!(x >= 0 && x < 16 && y >= 0 && y < 16 && z >= 0 && z < 16))
			return;

		int old = ids[x][y][z];
		ids[x][y][z] = id;

		if (id != Block.air.getId())
		{
			PalleteEntry e = pallete.get(id);
			if (e != null)
			{
				e.add(x, y, z);
			} else
			{
				PalleteEntry entry = new PalleteEntry(BlockRegistry.getBlockById(id));
				entry.add(x, y, z);
				pallete.put(id, entry);
			}
		}

		PalleteEntry e = pallete.get(old);
		if (e != null)
		{
			e.remove(x, y, z);

			if (e.getCount() <= 0)
			{
				pallete.remove(old);
			}
		}
	}

	public int getBlock(int x, int y, int z)
	{
		return ids[x][y][z];
	}

	public HashMap<Integer, PalleteEntry> getPallete()
	{
		return pallete;
	}

	public static class PalleteEntry
	{
		Block block;
		List<Short> pos;

		public PalleteEntry(Block block)
		{
			this.block = block;
			pos = new ArrayList<>();
		}

		public Block getBlock()
		{
			return block;
		}

		public void setBlock(Block block)
		{
			this.block = block;
		}

		public void add(int x, int y, int z)
		{
			short r = (short) (x << 8 | y << 4 | z);
			if (!pos.contains(r))
				pos.add(r);
		}

		public void add(short r)
		{
			if (!pos.contains(r))
				pos.add(r);
		}

		public void remove(int x, int y, int z)
		{
			short r = (short) (x << 8 | y << 4 | z);
			if (pos.contains(r))
				pos.remove((Short) r);
		}

		public boolean has(int x, int y, int z)
		{
			short r = (short) (x << 8 | y << 4 | z);
			return pos.contains(r);
		}

		public int getCount()
		{
			return pos.size();
		}

		public List<Short> getPos()
		{
			return pos;
		}
	}


	private void saveToChunk(HashMap<Integer, Short> map) throws IOException
	{
		for (int i = 0; i < ids.length; i++)
		{
			for (int j = 0; j < ids[i].length; j++)
			{
				int[] id = ids[i][j];
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

	private Pair<HashMap<Short, String>, HashMap<Integer, Short>> generatePallete()
	{
		HashMap<Short, String> pallete = new HashMap<>();
		HashMap<Integer, Short> map = new HashMap<>();
		List<Integer> registratedIds = new ArrayList<>();

		short lastId = 0;

		for (int i = 0; i < ids.length; i++)
		{
			for (int j = 0; j < ids[i].length; j++)
			{
				for (int k = 0; k < ids[i][j].length; k++)
				{
					int id = ids[i][j][k];
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
}
