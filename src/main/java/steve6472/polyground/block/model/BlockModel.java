package steve6472.polyground.block.model;

import steve6472.polyground.CaveGame;
import steve6472.polyground.registry.WaterRegistry;

import java.util.ArrayList;
import java.util.List;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 06.10.2019
 * Project: SJP
 *
 ***********************/
public class BlockModel
{
	private Cube[] cubes;
	private List<String> tags;

	/**
	 * Air Model Constructor
	 */
	public BlockModel()
	{
		tags = new ArrayList<>();
		tags.add("transparent");
		tags.add("air");
	}

	public BlockModel(String path, int rot)
	{
		if (path.isBlank())
			throw new IllegalArgumentException("Model path is blank! '" + path + "'");

		tags = new ArrayList<>();

		cubes = CaveGame.getInstance().blockModelLoader.loadModel(path, rot);

		double volume = 0;

		for (Cube c : cubes)
		{
			if (c.isCollisionBox() && c.isHitbox())
				volume += (c.getAabb().maxX - c.getAabb().minX) * (c.getAabb().maxY - c.getAabb().minY) * (c.getAabb().maxZ - c.getAabb().minZ);
		}
		WaterRegistry.tempVolumes.add((1.0 - Math.min(volume, 1.0)) * 1000.0);

		/*
		System.out.println("----" + sss.getString("model") + "----");
		System.out.println("Used volume: " + volume);
		System.out.println("Free volume: " + (1.0 - Math.min(volume, 1.0)) * 1000.0);
		*/
	}

	public BlockModel(Cube... cubes)
	{
		this.cubes = cubes;
		tags = new ArrayList<>();
	}

	public Cube getCube(int index)
	{
		return cubes[index];
	}

	public Cube[] getCubes()
	{
		return cubes;
	}

	public boolean hasTag(String tag)
	{
		return tags.contains(tag);
	}

	public void addTag(String tag)
	{
		tags.add(tag);
	}

	public void printFaceData()
	{
		System.out.println(cubes);
	}
}
