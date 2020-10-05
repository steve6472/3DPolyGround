package steve6472.polyground.block.blockdata;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 18.09.2019
 * Project: SJP
 *
 ***********************/
public class ScanBlockData// extends BlockData
{
	byte time;

	public void addTime()
	{
		time++;
		if (time == -128)
			time = 0;
	}

	public float getTime()
	{
		return 1f / 127f * (float) time;
	}

//	@Override
	public void write(DataOutputStream output) throws IOException
	{
		output.writeByte(time);
	}

//	@Override
	public void read(DataInputStream input) throws IOException
	{
		time = input.readByte();
	}
}
