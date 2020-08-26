package steve6472.polyground.block.model;

import steve6472.polyground.CaveGame;
import steve6472.polyground.registry.WaterRegistry;

import java.util.Arrays;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 06.10.2019
 * Project: SJP
 *
 ***********************/
public class BlockModel
{
	private Cube[] cubes;

	/**
	 * Air Model Constructor
	 */
	public BlockModel()
	{
	}

	public BlockModel(String path, int rot)
	{
		if (path.isBlank())
			throw new IllegalArgumentException("Model path is blank! '" + path + "'");

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
	}

	public Cube getCube(int index)
	{
		return cubes[index];
	}

	public Cube[] getCubes()
	{
		return cubes;
	}

	public void printFaceData()
	{
		System.out.println(Arrays.toString(cubes));
	}
}
