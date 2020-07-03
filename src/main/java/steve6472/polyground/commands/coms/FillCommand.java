package steve6472.polyground.commands.coms;

import com.mojang.brigadier.CommandDispatcher;
import org.joml.Vector3i;
import steve6472.polyground.block.Block;
import steve6472.polyground.commands.Command;
import steve6472.polyground.commands.CommandSource;
import steve6472.polyground.commands.arguments.BlockArgument;
import steve6472.polyground.commands.arguments.Vec3iArgument;
import steve6472.polyground.world.World;
import steve6472.polyground.world.chunk.Chunk;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 23.08.2019
 * Project: SJP
 *
 ***********************/
public class FillCommand extends Command
{
	public FillCommand(CommandDispatcher<CommandSource> dispatcher)
	{
		super(dispatcher);
	}

	@Override
	protected void register(CommandDispatcher<CommandSource> dispatcher)
	{
		dispatcher.register(literal("fill").then(argument("min", Vec3iArgument.vec3()).then(argument("max", Vec3iArgument.vec3()).then(argument("block", BlockArgument.block()).executes(c ->
		{
			World w = c.getSource().getWorld();
			Vector3i min = Vec3iArgument.getCoords(c, "min");
			Vector3i max = Vec3iArgument.getCoords(c, "max");
			Block block = BlockArgument.getBlock(c, "block");

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

			for (int x = min.x >> 4; x <= max.x >> 4; x++)
			{
				for (int z = min.z >> 4; z <= max.z >> 4; z++)
				{
					Chunk chunk = w.getChunk(x, z);
					if (chunk != null)
						chunk.update();
				}
			}

			return 1;
		})))));
	}
}
