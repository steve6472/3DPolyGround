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
	private List<CubeBuilder> cubes;

	public static BlockModelBuilder create()
	{
		return new BlockModelBuilder();
	}

	private BlockModelBuilder()
	{
		cubes = new ArrayList<>();
	}

	public BlockModelBuilder addCube(CubeBuilder cubeBuilder)
	{
		cubes.add(cubeBuilder);
		return this;
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
