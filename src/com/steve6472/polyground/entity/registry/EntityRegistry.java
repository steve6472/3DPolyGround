package com.steve6472.polyground.entity.registry;

import com.steve6472.polyground.entity.AIEntity;
import com.steve6472.polyground.entity.EntityBase;
import com.steve6472.polyground.entity.FallingBlock;
import com.steve6472.polyground.entity.FloatingText;

import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 28.09.2019
 * Project: SJP
 *
 ***********************/
public class EntityRegistry
{
	private static final HashMap<String, EntityEntry<? extends EntityBase>> entityRegistry = new HashMap<>();

	public static final EntityEntry<FallingBlock> fallingBlock = register("falling_block", FallingBlock::new);
	public static final EntityEntry<AIEntity> ai = register("ai", AIEntity::new);
	public static final EntityEntry<FloatingText> floatingText = register("floatingText", FloatingText::new);

	public static <T extends EntityBase> EntityEntry<T> register(String id, IEntityFactory<T> factory)
	{
		EntityEntry<T> entry = new EntityEntry<>(factory);
		entityRegistry.put(id, entry);
		return entry;
	}

	public static EntityBase createEntity(String id)
	{
		return entityRegistry.get(id).createNew();
	}

	public static Collection<EntityEntry<? extends EntityBase>> getEntries()
	{
		return entityRegistry.values();
	}

	public static Set<String> getKeys()
	{
		return entityRegistry.keySet();
	}
}
