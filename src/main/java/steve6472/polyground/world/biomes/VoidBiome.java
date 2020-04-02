package steve6472.polyground.world.biomes;

import steve6472.polyground.block.Block;
import steve6472.polyground.registry.BlockRegistry;
import org.joml.Vector3f;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 01.11.2019
 * Project: SJP
 *
 ***********************/
public class VoidBiome extends Biome
{
	@Override
	public String getName()
	{
		return "void";
	}

	@Override
	public Block getTopBlock()
	{
		return BlockRegistry.getBlockByName("air");
	}

	@Override
	public Block getUnderBlock()
	{
		return BlockRegistry.getBlockByName("air");
	}

	@Override
	public Block getCaveBlock()
	{
		return BlockRegistry.getBlockByName("air");
	}

	@Override
	public int getUnderLayerHeight()
	{
		return 0;
	}

	@Override
	public int getIterationCount()
	{
		return 0;
	}

	@Override
	public float getPersistance()
	{
		return 0.0f;
	}

	@Override
	public float getScale()
	{
		return 0.0f;
	}

	@Override
	public float getLow()
	{
		return 0;
	}

	@Override
	public float getHigh()
	{
		return 0;
	}

	@Override
	public Vector3f getColor()
	{
		return new Vector3f(0.14f, 0.14f, 0.14f);
	}
}
