package com.steve6472.polyground.world.biomes;

import com.steve6472.polyground.block.Block;
import org.joml.Vector3f;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 07.10.2019
 * Project: SJP
 *
 ***********************/
public abstract class Biome
{
	public abstract String getName();

	public abstract Block getTopBlock();
	public abstract Block getUnderBlock();
	public abstract Block getCaveBlock();

	public abstract int getUnderLayerHeight();

	public abstract int getIterationCount();
	public abstract float getPersistance();
	public abstract float getScale();
	public abstract float getLow();
	public abstract float getHigh();

	public abstract Vector3f getColor();
}
