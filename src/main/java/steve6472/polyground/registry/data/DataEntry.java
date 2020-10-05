package steve6472.polyground.registry.data;

import steve6472.polyground.block.blockdata.BlockData;

public class DataEntry<T extends BlockData>
{
	private final IDataFactory<T> factory;
	private final String id;

	DataEntry(IDataFactory<T> factory, String id)
	{
		this.factory = factory;
		this.id = id;
	}

	public T createNew()
	{
		return factory.create();
	}

	public String getId()
	{
		return id;
	}
}