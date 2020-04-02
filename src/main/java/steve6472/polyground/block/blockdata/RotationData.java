package steve6472.polyground.block.blockdata;

import steve6472.polyground.EnumFace;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 21.09.2019
 * Project: SJP
 *
 ***********************/
public class RotationData extends BlockData
{
	public EnumFace facing;

	public RotationData(EnumFace facing)
	{
		this.facing = facing;
	}

	@Override
	public void write(DataOutputStream output) throws IOException
	{
		output.writeByte(facing.ordinal());
	}

	@Override
	public void read(DataInputStream input) throws IOException
	{
		facing = EnumFace.get(input.readByte());
	}
}
