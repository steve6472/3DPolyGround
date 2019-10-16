package com.steve6472.polyground.generator.creator;

import com.steve6472.polyground.EnumFace;
import com.steve6472.polyground.block.model.BlockModel;
import com.steve6472.polyground.block.model.CubeFace;
import org.joml.AABBf;

import java.io.File;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 10.10.2019
 * Project: SJP
 *
 ***********************/
public class BlockEntry
{
	private BlockModel model;
	private String name;

	public BlockEntry(File f)
	{
		this.name = f.getName().substring(0, f.getName().length() - 5);
		model = BlockLoader.load(f);
	}

	public BlockEntry(String name)
	{
		this.name = name;
		model = new BlockModel(createEmptyCube());
	}

	public static CreatorCube createEmptyCube()
	{
		CreatorCube cube = new CreatorCube(new AABBf(0, 0, 0, 1, 1, 1));
		for (EnumFace f : EnumFace.getFaces())
		{
			cube.setFace(f, new CubeFace(cube, f));
		}
		cube.setName("Unnamed Cube");
		cube.setIndex(0);

		return cube;
	}

	public BlockModel getModel()
	{
		return model;
	}

	public String getName()
	{
		return name;
	}

	public void save()
	{
		BlockSaver.save(this);
	}
}








