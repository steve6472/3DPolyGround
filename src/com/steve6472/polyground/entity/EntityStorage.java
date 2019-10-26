package com.steve6472.polyground.entity;

import com.steve6472.polyground.CaveGame;
import com.steve6472.polyground.entity.model.EntityModel;
import com.steve6472.polyground.entity.model.IPostRender;
import com.steve6472.polyground.entity.model.models.ModelAI;
import com.steve6472.polyground.entity.model.models.ModelBlock;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import static org.lwjgl.opengl.GL30.glBindVertexArray;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 27.09.2019
 * Project: SJP
 *
 ***********************/
public class EntityStorage
{
	HashMap<EntityModel<? extends EntityBase>, List<EntityBase>> entities;

	private static final ModelBlock modelBlock = new ModelBlock();
	private static final ModelAI modelAI = new ModelAI();

	public void fillList()
	{
		entities = new HashMap<>();

		modelBlock.initTexture();
		modelBlock.initModel();
		entities.put(modelBlock, new ArrayList<>());

		modelAI.initTexture();
		modelAI.initModel();
		entities.put(modelAI, new ArrayList<>());
	}

	public void tickEntities()
	{
		for (List<EntityBase> entityBases : entities.values())
		{
			for (Iterator<EntityBase> iter = entityBases.iterator(); iter.hasNext(); )
			{
				EntityBase next = iter.next();

				if (next.isDead())
					iter.remove();
				else
					next.tick();
			}
		}
	}

	public void renderEntities()
	{
		CaveGame.shaders.worldShader.bind();
		CaveGame.shaders.worldShader.setView(CaveGame.getInstance().getCamera().getViewMatrix());

		for (EntityModel model : entities.keySet())
		{
			model.getTexture().bind(0);

			for (EntityBase e : entities.get(model))
			{
				model.render(e);
			}
		}

		glBindVertexArray(0);

		for (EntityModel model : entities.keySet())
		{
			for (EntityBase e : entities.get(model))
			{
				if (e instanceof IPostRender)
				{
					((IPostRender) e).postRender(e);
				}
			}
		}
	}

	public void addEntity(EntityBase entity)
	{
		if (entity instanceof FallingBlock) entities.get(modelBlock).add(entity);
		if (entity instanceof AIEntity) entities.get(modelAI).add(entity);
	}
}
