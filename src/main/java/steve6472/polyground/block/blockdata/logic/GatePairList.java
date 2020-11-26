package steve6472.polyground.block.blockdata.logic;

import java.util.ArrayList;
import java.util.Iterator;

public class GatePairList
{
	private final ArrayList<GatePair> list;

	public GatePairList()
	{
		list = new ArrayList<>();
	}

	public Iterator<GatePair> iterator()
	{
		return list.iterator();
	}

	public ArrayList<GatePair> getList()
	{
		return list;
	}

	public GatePairList copy()
	{
		final GatePairList list = new GatePairList();
		for (GatePair gp : getList())
		{
			list.getList().add(gp.copy());
		}
		return list;
	}
}
