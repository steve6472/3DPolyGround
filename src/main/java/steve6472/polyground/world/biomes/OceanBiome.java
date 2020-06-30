package steve6472.polyground.world.biomes;

import org.joml.Vector3f;
import steve6472.polyground.block.Block;
import steve6472.polyground.registry.BlockRegistry;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 01.11.2019
 * Project: SJP
 *
 ***********************/
public class OceanBiome extends Biome
{
	@Override
	public void addFeatures()
	{

	}

	@Override
	public String getName()
	{
		return "ocean";
	}

	@Override
	public Block getTopBlock()
	{
		return BlockRegistry.getBlockByName("gravel");
	}

	@Override
	public Block getUnderBlock()
	{
		return BlockRegistry.getBlockByName("sand");
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
		return 20;
	}

	@Override
	public Vector3f getColor()
	{
		return new Vector3f(102 / 255f, 209 / 255f, 154 / 255f);
	}
}
