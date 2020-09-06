package steve6472.polyground.block.model;

import org.json.JSONArray;
import org.json.JSONObject;
import steve6472.polyground.CaveGame;
import steve6472.polyground.registry.WaterRegistry;

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
	private final int rotX, rotY, rotZ;
	private boolean isBlockbenchModel;

	/**
	 * Air Model Constructor
	 */
	public BlockModel()
	{
		rotX = rotY = rotZ = 0;
		path = null;
	}

	public BlockModel(String path, JSONArray rot)
	{
		this.path = path;
		this.rotX = rot.getInt(0) % 360;
		this.rotY = rot.getInt(1) % 360;
		this.rotZ = rot.getInt(2) % 360;

		if (path.isBlank())
			throw new IllegalArgumentException("Model path is blank! '" + path + "'");

		JSONObject json = ModelLoader.loadJSONModel(path);
		isBlockbenchModel = json.optBoolean("blockbench", false);

		cubes = CaveGame.getInstance().modelLoader.loadCubes(json, rotX, rotY, rotZ);
		elements = CaveGame.getInstance().modelLoader.loadElements(json, rotX, rotY, rotZ);

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

		JSONObject json = ModelLoader.loadJSONModel(path);

		cubes = CaveGame.getInstance().modelLoader.loadCubes(json, rotX, rotY, rotZ);
		elements = CaveGame.getInstance().modelLoader.loadElements(json, rotX, rotY, rotZ);
	}

	public BlockModel(IElement[] elements, CubeHitbox... cubes)
	{
		this.path = null;
		this.elements = elements;
		rotX = rotY = rotZ = 0;
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

	public boolean isBlockbenchModel()
	{
		return isBlockbenchModel;
	}
}
