package steve6472.polyground.block.model;

import steve6472.polyground.CaveGame;
import steve6472.polyground.block.model.registry.Cube;
import steve6472.SSS;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 06.10.2019
 * Project: SJP
 *
 ***********************/
public class BlockModel
{
	private List<Cube> cubes;
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

	public BlockModel(File f)
	{
		cubes = new ArrayList<>();
		tags = new ArrayList<>();

		if (f.isFile())
		{
			SSS sss = new SSS(f);

			setCubes(CaveGame.getInstance().blockModelLoader.loadModel(sss.getString("model")));

			if (sss.containsName("tags"))
			{
				for (String s : sss.getStringArray("tags"))
					addTag(s);
			}
		}
	}

	public BlockModel(List<Cube> cubes)
	{
		this.cubes = new ArrayList<>();
		tags = new ArrayList<>();

		setCubes(cubes);
	}

	public BlockModel(Cube... cubes)
	{
		this.cubes = new ArrayList<>();
		tags = new ArrayList<>();

		setCubes(cubes);
	}

	public Cube getCube(int index)
	{
		return cubes.get(index);
	}

	public List<Cube> getCubes()
	{
		return cubes;
	}

	public void removeCube(Cube cube)
	{
		cubes.remove(cube);
	}

	public void setCubes(Cube... cubes)
	{
		this.cubes.clear();
		this.cubes = new ArrayList<>(Arrays.asList(cubes));
	}

	public void setCubes(List<Cube> cubes)
	{
		this.cubes.clear();
		this.cubes.addAll(cubes);
	}

	public void addCube(Cube cube)
	{
		this.cubes.add(cube);
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
