package com.steve6472.polyground.generator.creator;

import com.steve6472.polyground.block.model.registry.Cube;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 13.10.2019
 * Project: SJP
 *
 ***********************/
public interface ICreatorCube
{
	String getName();
	void setName(String newName);

	int getIndex();
	void setIndex(int index);

	Cube getCube();
}
