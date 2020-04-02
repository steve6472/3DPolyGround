package steve6472.polyground.entity.registry;

import steve6472.polyground.entity.EntityBase;

@FunctionalInterface
public interface IEntityFactory<T extends EntityBase>
{
	T create();
}