package steve6472.polyground.block.tree;

import steve6472.sge.main.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.function.BiConsumer;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 17.10.2020
 * Project: CaveGame
 *
 ***********************/
public class Array3D<T> implements Iterable<Pair<BlockPos, T>>
{
	private final HashMap<BlockPos, T> map;

	public Array3D()
	{
		map = new HashMap<>();
	}

	public void put(BlockPos pos, T t)
	{
		map.put(pos, t);
	}

	public void putIfAbsent(BlockPos pos, T t)
	{
		map.putIfAbsent(pos, t);
	}

	public T get(BlockPos pos)
	{
		return map.get(pos);
	}

	public boolean contains(BlockPos pos)
	{
		return map.containsKey(pos);
	}

	public void remove(BlockPos pos)
	{
		map.remove(pos);
	}

	public void clear()
	{
		map.clear();
	}

	public void forEach(BiConsumer<? super BlockPos, ? super T> action)
	{
		map.forEach(action);
	}

	/**
	 * Returns an iterator over elements of type {@code T}. <br>
	 * Uses ArrayList Iterator
	 *
	 * @return an Iterator.
	 */
	@Override
	public Iterator<Pair<BlockPos, T>> iterator()
	{
		ArrayList<Pair<BlockPos, T>> iter = new ArrayList<>();
		forEach((pos, t) -> iter.add(new Pair<>(pos, t)));

		return iter.iterator();
	}
}
