package com.steve6472.polyground.world.biomes;

import com.steve6472.polyground.block.Block;
import com.steve6472.polyground.block.registry.BlockRegistry;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 01.11.2019
 * Project: SJP
 *
 ***********************/
public class BlueLand extends Biome
{
	@Override
	public String getName()
	{
		return "blue_land";
	}

	@Override
	public Block getTopBlock()
	{
		return BlockRegistry.getBlockByName("blue_grass");
	}

	@Override
	public Block getUnderBlock()
	{
		return BlockRegistry.getBlockByName("dirt");
	}

	@Override
	public Block getCaveBlock()
	{
		return BlockRegistry.getBlockByName("stone");
	}

	@Override
	public int getUnderLayerHeight()
	{
		return 4;
	}

	@Override
	public int getIterationCount()
	{
		return 64;
	}

	@Override
	public float getPersistance()
	{
		return 0.4f;
	}

	@Override
	public float getScale()
	{
		return 0.0007f;
	}

	@Override
	public float getLow()
	{
		return 0;
	}

	@Override
	public float getHigh()
	{
		return 15;
	}
}
