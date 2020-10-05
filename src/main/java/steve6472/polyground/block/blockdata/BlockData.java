package steve6472.polyground.block.blockdata;

import java.io.IOException;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 18.09.2019
 * Project: SJP
 *
 ***********************/
public abstract class BlockData
{
	public abstract void write() throws IOException;

	public abstract void read() throws IOException;

	public abstract String getId();
}
