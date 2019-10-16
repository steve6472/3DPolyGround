package com.steve6472.polyground.entity.model.models;

import com.steve6472.polyground.CaveGame;
import com.steve6472.polyground.entity.FallingBlock;
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
public class ModelBlock implements EntityModel<FallingBlock>
{
	public EntityCube block;

	private Sprite texture;

	@Override
	public void initModel()
	{
		block = new EntityCube(1, 1, 1);
		block.build();
	}

	@Override
	public void initTexture()
	{
		texture = loadTexture("falling_block");
	}

	@Override
	public void render(FallingBlock entity, Matrix4f transformation)
	{
		transformation.translate(entity.getPosition());
		CaveGame.shaders.worldShader.setTransformation(transformation);

		block.render();
	}

	@Override
	public Sprite getTexture()
	{
		return texture;
	}
}
