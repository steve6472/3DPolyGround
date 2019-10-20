package com.steve6472.polyground.generator.creator;

import com.steve6472.polyground.EnumFace;
import com.steve6472.polyground.block.model.BlockModel;
import com.steve6472.polyground.block.model.CubeFace;
import com.steve6472.polyground.block.model.faceProperty.AutoUVFaceProperty;
import com.steve6472.polyground.block.model.faceProperty.TextureFaceProperty;
import com.steve6472.polyground.block.model.faceProperty.VisibleFaceProperty;
import com.steve6472.polyground.block.model.registry.face.FaceRegistry;
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

	private static void fillMissingProperties(CubeFace face)
	{
		for (String key : FaceRegistry.getKeys())
		{
			if (!face.hasProperty(key))
			{
				face.addProperty(FaceRegistry.createProperty(key));
			}
		}
	}

	public static CreatorCube createEmptyCube()
	{
		CreatorCube cube = new CreatorCube(new AABBf(0, 0, 0, 1, 1, 1));
		for (EnumFace f : EnumFace.getFaces())
		{
			CubeFace cf = new CubeFace(cube, f);
			cf.addProperty(new VisibleFaceProperty(true));
			cf.addProperty(new AutoUVFaceProperty(true));
			cf.addProperty(new TextureFaceProperty(-1, null));
			fillMissingProperties(cf);
			cf.getProperty(FaceRegistry.uv).autoUV(cube, f);
			cube.setFace(f, cf);
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








