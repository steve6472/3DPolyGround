package steve6472.polyground.block.blockdata;

import net.querz.nbt.tag.CompoundTag;
import steve6472.polyground.registry.blockdata.BlockDataRegistry;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 06.10.2020
 * Project: CaveGame
 *
 ***********************/
public class PaintBucketData extends BlockData
{
	public float red, green, blue;

	public PaintBucketData()
	{
		red = 0f;
		green = 0f;
		blue = 0f;
	}

	@Override
	public CompoundTag write()
	{
		CompoundTag tag = new CompoundTag();
		tag.putFloat("red", red);
		tag.putFloat("green", green);
		tag.putFloat("blue", blue);
		return tag;
	}

	@Override
	public void read(CompoundTag tag)
	{
		red = tag.getFloat("red");
		green = tag.getFloat("green");
		blue = tag.getFloat("blue");
	}

	@Override
	public String getId()
	{
		return BlockDataRegistry.paintBucket.getId();
	}
}
