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
	private CubeHitbox[] cubes;
	private IElement[] triangles;
	private final String path;
	private final int rot_y;
	private final boolean uvLock;

	/**
	 * Air Model Constructor
	 */
	public BlockModel()
	{
		rot_y = 0;
		path = null;
		uvLock = true;
	}

	public BlockModel(String path, int rot_y, boolean uvLock)
	{
		this.path = path;
		this.rot_y = rot_y;
		this.uvLock = uvLock;

		if (path.isBlank())
			throw new IllegalArgumentException("Model path is blank! '" + path + "'");

		cubes = CaveGame.getInstance().blockModelLoader.loadModel(path, rot_y);
		triangles = CaveGame.getInstance().blockModelLoader.loadElements(path, rot_y, uvLock);

		double volume = 0;

		for (CubeHitbox c : cubes)
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

	public void reload()
	{
		if (path == null)
			return;

		cubes = CaveGame.getInstance().blockModelLoader.loadModel(path, rot_y);
		triangles = CaveGame.getInstance().blockModelLoader.loadElements(path, rot_y, uvLock);
	}

	public BlockModel(CubeHitbox... cubes)
	{
		this.path = null;
		rot_y = 0;
		uvLock = true;
		this.cubes = cubes;
	}

	public CubeHitbox getCube(int index)
	{
		return cubes[index];
	}

	public CubeHitbox[] getCubes()
	{
		return cubes;
	}

	public IElement[] getElements()
	{
		return triangles;
	}

	@Override
	public String toString()
	{
		return "BlockModel{" + "triangles=" + Arrays.toString(triangles) + ", path='" + path + '\'' + ", rot_y=" + rot_y + ", uvLock=" + uvLock + '}';
	}
}
