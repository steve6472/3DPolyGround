package com.steve6472.polyground.world.chunk;

import com.steve6472.sge.main.TriConsumer;

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

	public ChunkPosStorage()
	{
		pos = new ArrayList<>();
	}

	public void addAll(List<Short> positions)
	{
		pos.addAll(positions);
	}

	public void addAll(ChunkPosStorage positions)
	{
		pos.addAll(positions.getAll());
	}

	public List<Short> getAll()
	{
		return pos;
	}

	public void clear()
	{
		pos.clear();
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

	public void addNonSafe(short r)
	{
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

	public boolean has(short r)
	{
		return pos.contains(r);
	}

	public void iterate(TriConsumer<Short, Short, Short> position)
	{
		for (short i : pos)
		{
			short x = (short) (i >> 8);
			short y = (short) ((i >> 4) & 0xf);
			short z = (short) (i & 0xf);

			position.apply(x, y, z);
		}
	}

	public Iterator<Short> iterator()
	{
		return pos.iterator();
	}
}
