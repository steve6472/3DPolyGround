package steve6472.polyground.world.chunk;

import steve6472.sge.main.TriConsumer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 26.11.2019
 * Project: SJP
 *
 ***********************/
public class ChunkPosStorage
{
	private List<Short> pos;
	private List<Short> addPos;
	private List<Short> removePos;

	public ChunkPosStorage()
	{
		pos = new ArrayList<>();
		addPos = new ArrayList<>();
		removePos = new ArrayList<>();
	}

	public void tick()
	{
		pos.addAll(addPos);
		addPos.clear();
		pos.removeAll(removePos);
		removePos.clear();
	}

	public void addAll(ChunkPosStorage positions)
	{
		addPos.addAll(positions.getAll());
	}

	public List<Short> getAll()
	{
		return pos;
	}

	public void clear()
	{
		pos.clear();
		addPos.clear();
		removePos.clear();
	}

	public void add(int x, int y, int z)
	{
		short r = (short) (x << 8 | y << 4 | z);
		if (!pos.contains(r) && !addPos.contains(r))
			addPos.add(r);
	}

	public void add(short r)
	{
		if (!addPos.contains(r) && !addPos.contains(r))
			addPos.add(r);
	}

	public void addNonSafe(short r)
	{
		pos.add(r);
	}

	public void remove(int x, int y, int z)
	{
		short r = (short) (x << 8 | y << 4 | z);
		if (!removePos.contains(r) && pos.contains(r))
			removePos.remove((Short) r);
	}

	public void set(int x, int y, int z, boolean flag)
	{
		if (flag)
			add(x, y % 16, z);
		else
			remove(x, y % 16, z);
	}

	public boolean has(int x, int y, int z)
	{
		short r = (short) (x << 8 | y << 4 | z);
		return pos.contains(r);
	}

	public boolean has(short r)
	{
		return pos.contains(r);
	}

	public void iterate(TriConsumer<Short, Short, Short> position)
	{
		for (Short s : pos)
		{
			if (s == null)
				continue;

			short x = (short) (s >> 8);
			short y = (short) ((s >> 4) & 0xf);
			short z = (short) (s & 0xf);

			position.apply(x, y, z);
		}
	}

	public boolean isEmpty()
	{
		return pos.isEmpty();
	}

	public Iterator<Short> iterator()
	{
		return pos.iterator();
	}
}
