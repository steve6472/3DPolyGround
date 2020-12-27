package steve6472.polyground;

import steve6472.polyground.block.tree.BlockPos;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: ${DATE}
 * Project: ${PROJECT_NAME}
 *
 ***********************/
public class TimedBlockPos extends BlockPos
{
	private int ticksUntilDeath;

	public TimedBlockPos(int x, int y, int z, int ticksUntilDeath)
	{
		super(x, y, z);
		this.ticksUntilDeath = ticksUntilDeath;
	}

	public TimedBlockPos(BlockPos other, int ticksUntilDeath)
	{
		super(other);
		this.ticksUntilDeath = ticksUntilDeath;
	}

	public int getTicksUntilDeath()
	{
		return ticksUntilDeath;
	}

	public void setTicksUntilDeath(int ticksUntilDeath)
	{
		this.ticksUntilDeath = ticksUntilDeath;
	}

	public void getCloserToDeath()
	{
		this.ticksUntilDeath--;
	}

	public boolean isDead()
	{
		return this.ticksUntilDeath == 0;
	}
}
