package steve6472.polyground.commands.coms;

import com.mojang.brigadier.CommandDispatcher;
import steve6472.polyground.block.Block;
import steve6472.polyground.commands.Command;
import steve6472.polyground.commands.CommandSource;
import steve6472.polyground.registry.BlockRegistry;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 19.08.2019
 * Project: SJP
 *
 ***********************/
public class BlocksCommand extends Command
{
	public BlocksCommand(CommandDispatcher<CommandSource> dispatcher)
	{
		super(dispatcher);
	}

	public void register(CommandDispatcher<CommandSource> dispatcher)
	{
		dispatcher.register(literal("blocks").executes(c ->
		{
			for (Block b : BlockRegistry.getAllBlocks())
			{
				System.out.println(b.getName());
			}

			return 1;
		}));
	}
}
