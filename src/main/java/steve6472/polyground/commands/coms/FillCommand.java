package steve6472.polyground.commands.coms;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import org.joml.Vector3i;
import steve6472.polyground.block.Block;
import steve6472.polyground.block.states.BlockState;
import steve6472.polyground.commands.Command;
import steve6472.polyground.commands.CommandSource;
import steve6472.polyground.commands.arguments.BlockArgument;
import steve6472.polyground.commands.arguments.Vec3iArgument;
import steve6472.polyground.entity.player.Player;
import steve6472.polyground.registry.Blocks;
import steve6472.polyground.world.World;

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

			return 1;
		})))));

		dispatcher.register(literal("set").then(argument("state", StringArgumentType.greedyString()).executes(c -> {
			World w = c.getSource().getWorld();
			Player player = c.getSource().getPlayer();
			String b = getString(c, "state");
			String block, state;
			if (b.contains("[") && b.contains("]"))
			{
				block = b.substring(0, b.indexOf("["));
				state = b.substring(b.indexOf("["), b.indexOf("]") + 1);
				final Block blockByName = Blocks.getBlockByName(block);
				final BlockState blockState = blockByName.getDefaultState().fromStateString(state);
				w.setState(blockState, (int) Math.floor(player.getX()), (int) Math.floor(player.getY()), (int) Math.floor(player.getZ()));
			} else
			{
				w.setBlock(Blocks.getBlockByName(b), (int) Math.floor(player.getX()), (int) Math.floor(player.getY()), (int) Math.floor(player.getZ()));
			}

			return 1;
		})));
	}
}
