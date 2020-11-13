package steve6472.polyground.entity;

import org.joml.Quaternionf;
import steve6472.polyground.block.Block;
import steve6472.polyground.entity.interfaces.*;
import steve6472.polyground.gfx.stack.Stack;
import steve6472.polyground.world.World;
import steve6472.sge.main.game.mixable.IPosition3f;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 11.04.2020
 * Project: CaveGame
 *
 ***********************/
public class EntityManager
{
	private final List<Object> entities;
	private final World world;

	public EntityManager(World world)
	{
		this.world = world;
		entities = new ArrayList<>();
	}

	public void addEntity(Object entity)
	{
		if (entity instanceof IWorldContainer c)
			c.setWorld(world);

		if (entity instanceof ICollideable && !(entity instanceof IPosition3f))
			throw new IllegalStateException("Entity is Collideable but has no Position!");

		entities.add(entity);
	}

	public void removeEntity(Object entity)
	{
		entities.remove(entity);
	}

	public void tick()
	{
		for (Iterator<Object> iterator = entities.iterator(); iterator.hasNext(); )
		{
			Object o = iterator.next();

			if (o instanceof ITickable c)
				c.tick();

			if (o instanceof ICollideable c)
			{
				IPosition3f p = (IPosition3f) o;

				int x = (int) Math.floor(p.getX());
				int y = (int) Math.floor(p.getY());
				int z = (int) Math.floor(p.getZ());

				Block block = world.getBlock(x, y, z);

				if (block != Block.AIR)
					c.collide(world, x, y, z);
			}



			if (o instanceof IKillable c)
			{
				if (c.isDead())
				{
					c.onDeath();
					iterator.remove();
				}
			}
		}
	}

	public void render(Stack stack)
	{
		Quaternionf quat = new Quaternionf();

		for (Object o : entities)
		{
			if (o instanceof IAdvancedRender r)
			{
				stack.pushMatrix();

				if (o instanceof IPosition3f p)
					stack.translate(p.getPosition());

				if (o instanceof IRotation rot)
				{
					quat.rotateXYZ(rot.getRotations().x, rot.getRotations().y, rot.getRotations().z);
					stack.rotate(quat);
				}

				r.render(stack);

				stack.popMatrix();
			}
		}
	}

	public void render()
	{
		for (Object o : entities)
		{
			if (o instanceof IRenderable r)
				r.render();
		}
	}

	public List<Object> getEntities()
	{
		return entities;
	}
}
