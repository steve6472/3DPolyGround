package steve6472.polyground.entity.registry;

import steve6472.polyground.entity.EntityBase;

public class EntityEntry<T extends EntityBase>
{
	private IEntityFactory<T> factory;

	EntityEntry(IEntityFactory<T> factory)
	{
		this.factory = factory;
	}

	public T createNew()
	{
		return factory.create();
	}
}