package steve6472.polyground.block.tree;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 17.10.2020
 * Project: CaveGame
 *
 ***********************/
public class BlockPosNode extends BlockPos
{
	private boolean processed;

	public BlockPosNode(BlockPosNode other)
	{
		this(other.getX(), other.getY(), other.getZ());
	}

	public BlockPosNode(int x, int y, int z)
	{
		super(x, y, z);
		this.processed = false;
	}

	public void setProcessed(boolean processed)
	{
		this.processed = processed;
	}

	public boolean isProcessed()
	{
		return processed;
	}
}
