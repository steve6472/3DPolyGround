package com.steve6472.polyground.block.model;

import com.steve6472.polyground.CaveGame;
import com.steve6472.polyground.block.model.registry.Cube;
import com.steve6472.sss2.SSS;

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

	public BlockModel(File f)
	{
		cubes = new ArrayList<>();

		if (f.isFile())
		{
			SSS sss = new SSS(f);

			setCubes(CaveGame.getInstance().blockModelLoader.loadModel(sss.getString("model")));
		}
	}

	public BlockModel(List<Cube> cubes)
	{
		this.cubes = new ArrayList<>();

		setCubes(cubes);
	}

	public BlockModel(Cube... cubes)
	{
		this.cubes = new ArrayList<>();

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
}
