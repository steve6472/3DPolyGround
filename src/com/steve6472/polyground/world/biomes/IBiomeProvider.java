package com.steve6472.polyground.world.biomes;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 09.11.2019
 * Project: SJP
 *
 ***********************/
public interface IBiomeProvider
{
	int[][][] getBiomeIds();

	default int getBiomeId(int x, int y, int z)
	{
		return getBiomeIds()[x][y][z];
	}

	default void setBiomeId(int x, int y, int z, int biomeId)
	{
		getBiomeIds()[x][y][z] = biomeId;
	}
}
