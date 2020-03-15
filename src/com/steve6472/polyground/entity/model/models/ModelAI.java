package com.steve6472.polyground.entity.model.models;

import com.steve6472.polyground.CaveGame;
import com.steve6472.polyground.entity.AIEntity;
import com.steve6472.polyground.entity.model.EntityCube;
import com.steve6472.polyground.entity.model.EntityModel;
import com.steve6472.sge.gfx.Sprite;
import org.joml.Matrix4f;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 07.09.2019
 * Project: SJP
 *
 ***********************/
public class ModelAI implements EntityModel<AIEntity>
{
	public EntityCube block, frame;

	private Sprite texture;

	@Override
	public void initModel()
	{
		block = new EntityCube(1, 1, 1);
		block.setTop(0.25f, 0f, 0.5f, 0.25f);
		block.setBottom(0.5f, 0f, 0.75f, 0.25f);
		block.setLeft(0f, 0.25f, 0.25f, 0.5f);
		block.setBack(0.25f, 0.25f, 0.5f, 0.5f);
		block.setRight(0.5f, 0.25f, 0.75f, 0.5f);
		block.setFront(0.75f, 0.25f, 1.0f, 0.5f);
		block.size = 0.4f;
		block.build();

		frame = new EntityCube(1, 1, 1);
		frame.setTop(0.25f, 0.5f, 0.5f, 0.75f);
		frame.setBottom(0.5f, 0.5f, 0.75f, 0.75f);
		frame.setLeft(0f, 0.75f, 0.25f, 1.0f);
		frame.setBack(0.25f, 0.75f, 0.5f, 1.0f);
		frame.setRight(0.5f, 0.75f, 0.75f, 1.0f);
		frame.setFront(0.75f, 0.75f, 1.0f, 1.0f);
		frame.size = 0.55f;
		frame.build();
	}

	@Override
	public void initTexture()
	{
		texture = loadTexture("ai");
	}

	@Override
	public void render(AIEntity entity)
	{
		Matrix4f transformation = new Matrix4f().translate(entity.getPosition());
		transformation.scale(block.size);
		transformation.rotate(entity.tick / (float) Math.PI, 1, 0, 0);
		transformation.rotate(entity.tick / (float) Math.PI, 0, 1, 0);
		transformation.rotate(entity.tick / (float) Math.PI, 0, 0, 1);
		CaveGame.shaders.flatTexturedShader.setTransformation(transformation);

		block.render();

		transformation = new Matrix4f().translate(entity.getPosition());
		transformation.scale(frame.size);
		transformation.rotate(-(entity.tick * 1.1f) / (float) Math.PI, 0, 0, 1);
		transformation.rotate(-(entity.tick * 1.1f) / (float) Math.PI, 0, 1, 0);
		transformation.rotate(-(entity.tick * 1.1f) / (float) Math.PI, 1, 0, 0);
		CaveGame.shaders.flatTexturedShader.setTransformation(transformation);
		frame.render();
	}

	@Override
	public Sprite getTexture()
	{
		return texture;
	}
}
