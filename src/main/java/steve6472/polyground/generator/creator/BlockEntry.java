package steve6472.polyground.generator.creator;

import steve6472.polyground.EnumFace;
import steve6472.polyground.block.model.BlockModel;
import steve6472.polyground.block.model.CubeFace;
import steve6472.polyground.block.model.faceProperty.AutoUVFaceProperty;
import steve6472.polyground.block.model.faceProperty.TextureFaceProperty;
import steve6472.polyground.block.model.faceProperty.VisibleFaceProperty;
import steve6472.polyground.registry.face.FaceEntry;
import steve6472.polyground.registry.face.FaceRegistry;
import org.joml.AABBf;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

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
	private boolean isParent;
	private boolean isChild;

	private static List<FaceEntry> ignoredProperties = new ArrayList<>();

	static
	{
		ignoredProperties.add(FaceRegistry.condition);
		ignoredProperties.add(FaceRegistry.conditionedTexture);
		ignoredProperties.add(FaceRegistry.rotation);
	}

	public BlockEntry(File f)
	{
		this.name = f.getName().substring(0, f.getName().length() - 5);
		model = BlockLoader.load(f, this);
	}

	public BlockEntry(String name)
	{
		this.name = name;
		model = new BlockModel(createEmptyCube());
	}

	private static void fillMissingProperties(CubeFace face)
	{
		main:
		for (String key : FaceRegistry.getKeys())
		{
			for (FaceEntry f : ignoredProperties)
			{
				if (f.getInstance().getId().equals(key))
				{
					continue main;
				}
			}

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

	public boolean isParent()
	{
		return isParent;
	}

	public void setParent(boolean parent)
	{
		isParent = parent;
	}

	public void save()
	{
		BlockSaver.save(this);
	}

	public boolean isChild()
	{
		return isChild;
	}

	public void setChild(boolean child)
	{
		isChild = child;
	}
}








