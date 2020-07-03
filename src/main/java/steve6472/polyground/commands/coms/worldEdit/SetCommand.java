package steve6472.polyground.commands.coms.worldEdit;

import com.mojang.brigadier.CommandDispatcher;
import org.joml.Vector3i;
import steve6472.polyground.CaveGame;
import steve6472.polyground.block.Block;
import steve6472.polyground.commands.Command;
import steve6472.polyground.commands.CommandSource;
import steve6472.polyground.commands.arguments.BlockArgument;
import steve6472.polyground.item.special.WorldEditItem;
import steve6472.polyground.world.World;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 10.10.2019
 * Project: SJP
 *
 ***********************/
public class SetCommand extends Command
{
	public SetCommand(CommandDispatcher<CommandSource> dispatcher)
	{
		super(dispatcher);
	}

	@Override
	protected void register(CommandDispatcher<CommandSource> dispatcher)
	{
		dispatcher.register(literal("//set").then(argument("block", BlockArgument.block()).executes(c ->
		{

			if (!(CaveGame.itemInHand instanceof WorldEditItem))
				return 0;

			WorldEditItem item = (WorldEditItem) CaveGame.itemInHand;

			int filledBlocks = 0;
			World w = c.getSource().getWorld();
			Block block = BlockArgument.getBlock(c, "block");

			Vector3i min = new Vector3i((int) item.getFirstPos().x, (int) item.getFirstPos().y, (int) item.getFirstPos().z);
			Vector3i max = new Vector3i((int) item.getSecondPos().x, (int) item.getSecondPos().y, (int) item.getSecondPos().z);

			if (min.x > max.x)
			{
				int temp = min.x;
				min.x = max.x;
				max.x = temp;
			}

			if (min.y > max.y)
			{
				int temp = min.y;
				min.y = max.y;
				max.y = temp;
			}

			if (min.z > max.z)
			{
				int temp = min.z;
				min.z = max.z;
				max.z = temp;
			}

			for (int x = min.x; x <= max.x; x++)
			{
				for (int y = min.y; y <= max.y; y++)
				{
					for (int z = min.z; z <= max.z; z++)
					{
						w.setBlock(block, x, y, z);
					}
				}
			}

			//							for (int x = min.x >> 4; x <= max.x >> 4; x++)
			//							{
			//								for (int z = min.z >> 4; z <= max.z >> 4; z++)
			//								{
			//									Chunk chunk = w.getChunk(x, z);
			//									if (chunk != null)
			//										chunk.update();
			//								}
			//							}

			return filledBlocks;
		})));
	}
}








