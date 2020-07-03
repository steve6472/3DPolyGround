package steve6472.polyground.generator.models;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 01.07.2020
 * Project: CaveGame
 *
 ***********************/
public class BlockModelBuilder
{
	private final List<CubeBuilder> cubes;
	private final String modelName;
	private String modelPath;

	public static BlockModelBuilder create(String modelName)
	{
		return new BlockModelBuilder(modelName);
	}

	private BlockModelBuilder(String modelName)
	{
		cubes = new ArrayList<>();
		this.modelName = modelName;
		this.modelPath = "";
	}

	public BlockModelBuilder modelPath(String path)
	{
		modelPath = path + "/";
		return this;
	}

	public BlockModelBuilder addCube(CubeBuilder cubeBuilder)
	{
		cubes.add(cubeBuilder);
		return this;
	}

	public String getModelName()
	{
		return modelName;
	}

	public String getModelPath()
	{
		return modelPath;
	}

	public IModel build()
	{
		JSONObject main = new JSONObject();
		JSONArray cubes = new JSONArray();

		for (CubeBuilder c : this.cubes)
		{
			cubes.put(c.build());
		}
		main.put("cubes", cubes);

		return () -> main.toString(4);
	}
}
