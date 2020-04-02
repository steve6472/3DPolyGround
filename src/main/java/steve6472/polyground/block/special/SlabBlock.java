package steve6472.polyground.block.special;

import steve6472.polyground.block.Block;
import steve6472.SSS;

import java.io.File;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 15.09.2019
 * Project: SJP
 *
 ***********************/
public class SlabBlock extends Block
{
	private File f;

	public EnumSlabType slabType;

	public SlabBlock(File f, int id)
	{
		super(f, id);
		this.f = f;
		isFull = false;
	}

	@Override
	public void postLoad()
	{
		if (f.isFile())
		{
			SSS sss = new SSS(f);
			slabType = switch (sss.getString("type"))
				{
					case "top" -> EnumSlabType.TOP;
					case "bottom" -> EnumSlabType.BOTTOM;
					default -> throw new IllegalStateException("Unexpected value: " + sss.getString("type"));
				};
		}
		f = null;
	}

	public enum EnumSlabType
	{
		TOP, BOTTOM
	}
}
