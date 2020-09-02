package steve6472.polyground.block.model;

import org.json.JSONObject;
import steve6472.polyground.BlockBenchTranslator;
import steve6472.polyground.CaveGame;
import steve6472.polyground.registry.WaterRegistry;

import java.io.File;
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
	private IElement[] elements;
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

		JSONObject json = loadJSON(path);

		cubes = CaveGame.getInstance().blockModelLoader.loadCubes(json, rot_y);
		elements = CaveGame.getInstance().blockModelLoader.loadElements(json, rot_y, uvLock);

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

	private JSONObject loadJSON(String path)
	{
		JSONObject json = null;

		try
		{
			json = new JSONObject(BlockModelLoader.read(new File("game/objects/models/" + path + ".json")));

			if (json.has("meta"))
			{
				json = BlockBenchTranslator.convert(json);
			}
		} catch (Exception e)
		{
			System.err.println("Could not load block model " + path);
			e.printStackTrace();
			CaveGame.getInstance().exit();
			System.exit(0);
		}

		return json;
	}

	public void reload()
	{
		if (path == null)
			return;

		JSONObject json = loadJSON(path);

		cubes = CaveGame.getInstance().blockModelLoader.loadCubes(json, rot_y);
		elements = CaveGame.getInstance().blockModelLoader.loadElements(json, rot_y, uvLock);
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
		return elements;
	}

	@Override
	public String toString()
	{
		return "BlockModel{" + "triangles=" + Arrays.toString(elements) + ", path='" + path + '\'' + ", rot_y=" + rot_y + ", uvLock=" + uvLock + '}';
	}
}
