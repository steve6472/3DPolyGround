package com.steve6472.polyground.entity.model;

import com.steve6472.polyground.entity.EntityBase;
import com.steve6472.sge.gfx.Sprite;
import com.steve6472.sge.main.MainApp;

import java.io.File;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 07.09.2019
 * Project: SJP
 *
 ***********************/
public interface EntityModel<T extends EntityBase>
{
	void initModel();
	void initTexture();

	void render(T entity);

	Sprite getTexture();

	default Sprite loadTexture(String name)
	{
		return new Sprite(new File(MainApp.class.getResource("/textures/entity/" + name + ".png").getFile()));
//		return new Sprite(new File("textures/entity/" + name + ".png"));
	}
}
